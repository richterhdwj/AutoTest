/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.databaseModel.WordRecord;
import main.databaseTable.WordViewTable;
import main.innerView.InnerGroupView;
import support.DateBean;
import support.database.DataBaseManager;
import support.tableView.TableViewCustom;
import support.tableView.TableViewCustom.TableViewCenter;

/**
 *
 * @author hdwjy
 */
public class WordInputCenter {

    private TaksObject taksObject;
    private DataBaseManager dataBaseManager = new DataBaseManager();
    private TableViewCustom tableviewSeting;
    private TableView tableview;
    private WordRecord editWordRecord;
    private InnerGroupView innerGroupView;
    public static String rootName = "wordNew";

    public WordInputCenter(TaksObject taksObject) {
        this.taksObject = taksObject;
    }

    @SuppressWarnings({"unchecked", "fallthrough"})
    public void WordView() throws Exception {
        //清空原来所有的图表页面
        taksObject.getBorderPane().setCenter(null);

        //预计会有按键区跟列表区，所以预先设定为2
        VBox vbox = new VBox(2);
        //首先设定按键区
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(5, 0, 5, 10));
        buttonBox.setSpacing(10);

        buttonBox.setPrefHeight(40);

        Button newButton = new Button("新增");

        newButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                innerGroupView = new InnerGroupView(taksObject);
                
                innerGroupView.newInnerView(null, null, rootName);

                innerGroupView.newRoot.getChildren().add(newSavePlant(null));
            }
        });

        Button delButton = new Button("删除");
        delButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                //追加一个确认的内容
                innerGroupView = new InnerGroupView(taksObject);
                Group group=innerGroupView.confirm("确实需要删除吗？");

                innerGroupView.accButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent arg0) {

                        Iterator<TableViewCenter> nowItems = tableview.getItems().iterator();
                        while (nowItems.hasNext()) {
                            TableViewCenter tableViewCenter = nowItems.next();
                            CheckBox checkBox = (CheckBox) tableViewCenter.getObj1().getValue();
                            if (checkBox.isSelected()) {
                                WordViewTable wordView = (WordViewTable) tableViewCenter.getObj();
                                WordRecord wordRecord = wordView.getWordRecord();
                                wordRecord.setSysFlag("0");
                                try {
                                    taksObject.save(wordRecord);
                                } catch (Exception ex) {
                                    Logger.getLogger(WordInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        try {
                            searchTableView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        innerGroupView.rewakeConfirm(null);
                        innerGroupView=null;
                    }
                });
                
                innerGroupView.canButton.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        innerGroupView.rewakeConfirm(null);
                        innerGroupView=null;
                    }
                });
            }
        });

        buttonBox.getChildren().addAll(newButton, delButton);

        //然后设定列表区

        //以下是空结构时采用的空框架画图用
        WordViewTable newWordViewTable = new WordViewTable();

        Button sampleButton = new Button("查看");
        sampleButton.setId("例题");
        newWordViewTable.addItems(sampleButton);

        Button editButton = new Button("修改");
        editButton.setId("修改");
        newWordViewTable.addItems(editButton);

        newWordViewTable.setCheckBox(null);

        //设定初始
        tableviewSeting = new TableViewCustom(newWordViewTable, "read");

        //初始化列表
        tableviewSeting.setTableViewColumn();

        //将初始化好的列表塞入值然后直接获得列表
        //首先查询数据
        searchTableView();
        tableview.setPrefWidth(1024);

        //将按钮和列表塞入VBox
        vbox.getChildren().addAll(buttonBox, tableview);

        //然后将VBOX塞入center
        taksObject.getBorderPane().setCenter(vbox);
    }

    @SuppressWarnings({"unchecked", "fallthrough"})
    private void searchTableView() throws Exception {
        //查询数据
        // where t.F_SYS_FLAG = '1' limit 0,50 ：limit就是分页用方法
        List<WordRecord> list = dataBaseManager.selectObject(WordRecord.class, " where t.F_SYS_FLAG = '1'");

        //设定提交列表用的列表model
        List<WordViewTable> viewList = new ArrayList<WordViewTable>();
        for (WordRecord wordRecord : list) {
            final WordViewTable wordViewTable = new WordViewTable();

            wordViewTable.setContect(wordRecord.getContect());
            wordViewTable.setExample(wordRecord.getExample());
            wordViewTable.setTrans(wordRecord.getTrans());
            wordViewTable.setType(wordRecord.getType());
            wordViewTable.setWordRecord(wordRecord);

            Button sampleButton = new Button("查看");
            sampleButton.setId("例题");
            wordViewTable.addItems(sampleButton);

            Button editButton = new Button("修改");
            editButton.setId("修改");
            wordViewTable.addItems(editButton);

            wordViewTable.setCheckBox(null);

            viewList.add(wordViewTable);
        }

        tableview = tableviewSeting.setTableViewData(viewList);
    }

    /**
     * 新增/修改平台内容
     *
     * @return
     */
    public Node newSavePlant(WordRecord wordRecord) {
        if (wordRecord != null) {
            editWordRecord = wordRecord;
        } else {
            editWordRecord = new WordRecord();
        }

        HBox firstHbox = new HBox();
        Button accButton = new Button("确定");

        Button canButton = new Button("取消");
        canButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                Iterator<Node> its = taksObject.getRoot().getChildren().iterator();
                while (its.hasNext()) {
                    Node node = its.next();
                    node.setDisable(false);
                }
                taksObject.getRoot().getChildren().remove((Node) taksObject.rootMap.get(rootName));
                taksObject.rootMap.remove(rootName);
            }
        });

        //设定Hbox之间的相隔距离、和HBox内部构件之间的空间距离
        firstHbox.setPadding(new Insets(25, 5, 5, 25));
        firstHbox.setSpacing(20);
        firstHbox.getChildren().addAll(accButton, canButton);

        HBox secondHbox = new HBox();

        HBox typeHbox = new HBox(2);
        Label typeLabel = new Label("类型");
        final ComboBox<String> typeCombo = new ComboBox<String>();
        typeCombo.getItems().add("ENGLISH");
        typeCombo.setEditable(true);

        typeHbox.getChildren().addAll(typeLabel, typeCombo);

        HBox wordHbox = new HBox(2);
        Label wordLabel = new Label("词条");
        final TextField wordText = new TextField();
        wordText.setPrefWidth(250);
        wordHbox.getChildren().addAll(wordLabel, wordText);

        //设定Hbox之间的相隔距离、和HBox内部构件之间的空间距离
        secondHbox.setPadding(new Insets(25, 5, 5, 5));
        secondHbox.setSpacing(20);
        secondHbox.getChildren().addAll(typeHbox, wordHbox);

        HBox thirdHbox = new HBox();
        HBox transHbox = new HBox(2);
        Label transLabel = new Label("解释");
        final TextArea transText = new TextArea();
        transText.setPrefSize(200, 100);
        transHbox.getChildren().addAll(transLabel, transText);

        HBox sampleHbox = new HBox(2);
        Label sampleLabel = new Label("例句");
        final TextArea sampleText = new TextArea();
        sampleText.setPrefSize(200, 100);
        sampleHbox.getChildren().addAll(sampleLabel, sampleText);

        //设定Hbox之间的相隔距离、和HBox内部构件之间的空间距离
        thirdHbox.setPadding(new Insets(25, 5, 5, 5));
        thirdHbox.setSpacing(20);
        thirdHbox.getChildren().addAll(transHbox, sampleHbox);

        //设定一个提示值，暂时永久提示下拉输入框输入值后要按回车提交值
        Label tipLabel = new Label("如果类型是输入的，请一定要记得输入完后按回车");
        tipLabel.setWrapText(true);
        tipLabel.setAlignment(Pos.CENTER);
        tipLabel.setFont(new Font("黑体", 12d));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(firstHbox, secondHbox, thirdHbox, tipLabel);

        accButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                if (typeCombo.getValue() != null && !typeCombo.getValue().isEmpty()) {
                    editWordRecord.setType(typeCombo.getValue());
                    editWordRecord.setContect(wordText.getText());
                    editWordRecord.setExample(sampleText.getText());
                    editWordRecord.setTrans(transText.getText());
                    editWordRecord.setCreateTime(DateBean.getSysdateTime());
                    if (editWordRecord.getPid() == null) {
                        editWordRecord.setSysFlag("1");
                    }
                    try {
                        taksObject.save(editWordRecord);
                    } catch (Exception ex) {
                        Logger.getLogger(WordInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    Iterator<Node> its = taksObject.getRoot().getChildren().iterator();
                    while (its.hasNext()) {
                        Node node = its.next();
                        node.setDisable(false);
                    }
                    taksObject.getRoot().getChildren().remove((Node) taksObject.rootMap.get(rootName));
                    taksObject.rootMap.remove(rootName);
                    try {
                        searchTableView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
        return vbox;
    }
}
