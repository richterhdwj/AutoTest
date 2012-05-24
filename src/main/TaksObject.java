/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.databaseModel.WordRecord;
import main.databaseModel.WordTopic;
import support.DateBean;
import support.database.DataBaseManager;

/**
 *
 * @author hdwjy
 */
public class TaksObject {

    private Group root;
    private Stage primaryStage;
    private BorderPane borderPane;                                              //主界面底层
    private Node firstPage;                                                     //页面临时缓存。
    private Node tempPage;
    private boolean hasAnswer = false;
    private boolean hasFirstPage = true;
    public HashMap rootMap = new HashMap();
    private DataBaseManager database = new DataBaseManager();

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public Node getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Node firstPage) {
        this.firstPage = firstPage;
    }

    public Node getTempPage() {
        return tempPage;
    }

    public void setTempPage(Node tempPage) {
        this.tempPage = tempPage;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public boolean isHasAnswer() {
        return hasAnswer;
    }

    public void setHasAnswer(boolean hasAnswer) {
        this.hasAnswer = hasAnswer;
    }

    public boolean isHasFirstPage() {
        return hasFirstPage;
    }

    public void setHasFirstPage(boolean hasFirstPage) {
        this.hasFirstPage = hasFirstPage;
    }

    /**
     * 首页的展示
     *
     * @return
     */
    @SuppressWarnings({"unchecked", "fallthrough"})
    public Node ChartPane() throws Exception {
        List<Object[]> titlelist = database.selectObject("SELECT distinct t1.f_type FROM t_word_topic t,t_word_record t1 where t.f_title=t1.f_pid and t.f_sys_flag='1' and t1.f_sys_flag='1'");

        String[] titles = new String[titlelist.size()];
        int i = 0;

        List<Object[]> maxValueList = database.selectObject("SELECT ifnull(max(t.f_attention),0) FROM t_word_topic t,t_word_record t1 where t.f_title=t1.f_pid and t.f_sys_flag='1' and t1.f_sys_flag='1'");

        Integer maxValueLength = maxValueList.get(0)[0].toString().length();
        if (maxValueLength < 1) {
            maxValueLength = 1;
        }

        int maxValue = 1;

        for (int m = 0; m < maxValueLength; m++) {
            maxValue = maxValue * 10;
        }

        int nextValue = maxValue / 10;

        if (nextValue < 1) {
            nextValue = 1;
        }

        NumberAxis yAxis = new NumberAxis("正确率", 0, maxValue, nextValue);

        ObservableList attList = FXCollections.observableArrayList();

        ObservableList accList = FXCollections.observableArrayList();

        for (Object[] objs : titlelist) {
            titles[i] = objs[0].toString();

            List<Object[]> trueList = database.selectObject("SELECT ifnull(sum(t.f_attention),0),ifnull(sum(t.f_accept),0) "
                    + "FROM t_word_topic t,t_word_record t1 where "
                    + "t.f_title=t1.f_pid and t.f_sys_flag='1' "
                    + "and t1.f_sys_flag='1' and t1.f_type = '"
                    + objs[0].toString()
                    + "'");
            int attention = Integer.parseInt(trueList.get(0)[0].toString());
            attList.add(new BarChart.Data(objs[0].toString(), attention));
            int accept = Integer.parseInt(trueList.get(0)[1].toString());
            accList.add(new BarChart.Data(objs[0].toString(), accept));

            i++;
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(titles));

        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
                new BarChart.Series("做题总数", attList),
                new BarChart.Series("正确率", accList));

        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);

        chart.setPrefSize(1024, 480);
        chart.setTitle("综合成绩单");

        return chart;
    }

    public Object save(Object obj) throws Exception {
        database.save(obj);
        return database.selectObject(obj.getClass(), null);
    }

    public void setInDataBase(String url) throws Exception {
        File file = new File(url);
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");

        BufferedReader br = new BufferedReader(read);
        StringBuffer sb = new StringBuffer();
        String nowLine = br.readLine();
        
        String wordType=null;
        String word=null;
        String wordExample=null;
        String wordTrans=null;

        String topicExample=null;
        String topicAnswer=null;
        
        while (nowLine != null) {
            if(nowLine.contains(":")){
                nowLine.trim();
                String[] tempLine=nowLine.split(":");
                if(tempLine[0].equals("单词")||tempLine[0].equals("语法")){
                    wordType=tempLine[0];                                       //获取词条类型
                    nowLine=nowLine.substring(nowLine.indexOf(";"));            
                    continue;
                }else if(tempLine[0].equals("例句")){ 
                    wordExample = tempLine[1];                                  //获取例句
                }else{
                    word=tempLine[0];                                           //获取词条主要内容
                    wordTrans=tempLine[1];                                      //获取词条解释
                }
            }else if(nowLine.contains(".")){
                nowLine.trim();
                String[] tempLine=nowLine.split(".",1);                         //含.的都看作题目，.后的为题目
                topicExample=tempLine[1];
                nowLine = br.readLine();                                        //他下面的一行全都看作答案,答案用|号分隔
                tempLine=nowLine.split("|");
                for(String nowAnswer:tempLine){
                    String[] answers=nowAnswer.split(".");
                    
                    WordRecord wordRecord=null;                                 //每个答案进行一次保存。
                    List<WordRecord> list=database.selectObject(WordRecord.class,                       //这里保存词条
                            " where t.F_CONTECT = '"+word+"' ans t.f_sys_flag='1'");
                    if(list.size()>0){
                        wordRecord=list.get(0);                                                         //不重复保存，已有的词条查出来
                    }else{
                        String saveTime=DateBean.getSysdateTime();
                        wordRecord=new WordRecord();
                        wordRecord.setType(wordType);
                        wordRecord.setContect(word);
                        wordRecord.setTrans(wordTrans);
                        wordRecord.setExample(wordExample);
                        wordRecord.setSysFlag("1");
                        wordRecord.setCreateTime(saveTime);
                        save(wordRecord);
                        
                        list=database.selectObject(WordRecord.class," where t.F_CREATTIME = '"+saveTime+"' ans t.f_sys_flag='1'");
                        wordRecord=list.get(0);
                    }
                    //TODO:导入功能完成并测试
                    List<WordTopic> listExample=database.selectObject(WordTopic.class,              //题目也是一样处理
                            " where t.contect = '"+topicExample+"' and t.f_sys_flag='1'");
                }
            }
            nowLine = br.readLine();
        }
    }
}
