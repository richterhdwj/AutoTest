/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.innerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.TaksObject;
import main.databaseModel.WordTopic;
import main.databaseModel.WordTopicAnswer;
import support.database.DataBaseManager;

/**
 *
 * @author hdwjy
 */
public class InnerGroupView {

    private TaksObject taksObject;
    public StackPane newRoot;
    public Button accButton;
    public Button canButton;
    private ToggleGroup tg;

    public InnerGroupView(TaksObject taksObject) {
        this.taksObject = taksObject;
    }

    /**
     * 创建一个模态窗口，width是宽度，height是高度，id则是这个模态窗口的id（用来指定，方便从内存中剔除）
     *
     * @param width
     * @param height
     * @param id
     * @return
     */
    @SuppressWarnings({"unchecked", "fallthrough"})
    public Group newInnerView(Double width, Double height, String id) {
        Group root = new Group();

        Iterator<Node> it = taksObject.getRoot().getChildren().iterator();
        while (it.hasNext()) {
            Node node = it.next();
            node.setDisable(true);
        }

        if (width == null) {
            width = taksObject.getPrimaryStage().getScene().getWidth() / 2;
        }

        if (height == null) {
            height = taksObject.getPrimaryStage().getScene().getHeight() / 2;
        }

        Rectangle rect1 = new Rectangle(0, 0, width, height);
        rect1.setFill(Color.WHITESMOKE);
        rect1.setStroke(Color.DARKRED);

        //这样的新视图都在中间显示
        rect1.setLayoutX(taksObject.getPrimaryStage().getScene().getWidth() / 2 - width / 2);
        rect1.setLayoutY(taksObject.getPrimaryStage().getScene().getHeight() / 2 - height / 2);

        //将所有的在这个内部新窗口的所有内容置于这个滚动平台内
        newRoot = new StackPane();
        newRoot.setLayoutX(taksObject.getPrimaryStage().getScene().getWidth() / 2 - width / 2);
        newRoot.setLayoutY(taksObject.getPrimaryStage().getScene().getHeight() / 2 - height / 2);

        newRoot.setPrefSize(width, height);

        root.getChildren().addAll(rect1, newRoot);

        taksObject.getRoot().getChildren().add(root);

        taksObject.rootMap.put(id, root);

        return root;
    }

    /**
     * 创建一个小的用于用户确认的小窗口，目前问题需要将按钮的监听写到外面
     *
     * @param message
     * @return
     */
    @SuppressWarnings({"unchecked", "fallthrough"})
    public Group confirm(String message) {
        Group confirmGroup = this.newInnerView(300d, 150d, "confirm");

        Label label = new Label(message);
        label.setAlignment(Pos.CENTER);

        accButton = new Button("确认");

        canButton = new Button("取消");

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(40);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.getChildren().addAll(accButton, canButton);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        vbox.getChildren().addAll(label, buttonBox);

        newRoot.getChildren().add(vbox);
        newRoot.setAlignment(Pos.CENTER);

        return confirmGroup;
    }

    /**
     * 唤醒被disable的其它内容，mapkey则用来清除相应的模态窗口
     *
     * @param root
     * @param mapKey
     */
    @SuppressWarnings({"unchecked", "fallthrough"})
    public void rewake(Group root, String mapKey) {
        if (root == null) {
            Iterator<Node> it = taksObject.getRoot().getChildren().iterator();
            while (it.hasNext()) {
                Node node = it.next();
                node.setDisable(false);
            }
            taksObject.getRoot().getChildren().remove(taksObject.rootMap.get(mapKey));
            taksObject.rootMap.remove(mapKey);
        } else {
            Iterator<Node> it = taksObject.getRoot().getChildren().iterator();
            while (it.hasNext()) {
                Node node = it.next();
                if (root.equals(node)) {
                    node.setDisable(false);
                }
            }
            taksObject.getRoot().getChildren().remove(taksObject.rootMap.get(mapKey));
            taksObject.rootMap.remove(mapKey);
        }
    }
    /**
     * 确认小平台的默认摸消方法
     *
     * @param root
     */
    private double initX;
    private double initY;

    public void rewakeConfirm(Group root) {
        this.rewake(root, "confirm");
    }

    /**
     * 建立一个新的答案问答器
     */
    public void newOutView() {
        //将问答器的开关打开，避免重复打开
        taksObject.setHasAnswer(false);
        //create stage which has set stage style undecorated
        final Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setResizable(false);
        //create root node of scene, i.e. group
        Group rootGroup = new Group();
        //create scene with set width, height and color
        Scene scene = new Scene(rootGroup, 580, 400, Color.BLACK);
        //set scene to stage
        stage.setScene(scene);
        //center stage on screen
        String os=System.getProperty("os.name");
        int width=1920;
        int height=1080;
        if(!os.startsWith("Mac")){
            width=ViewParamets.getScreenWidth();
            height=ViewParamets.getScreenHeight();
        }
        stage.setX( width - 600);
        stage.setY( height- 500);
        //show the stage
        stage.show();

        //when mouse button is pressed, save the initial position of screen
        rootGroup.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                initX = me.getScreenX() - stage.getX();
                initY = me.getScreenY() - stage.getY();
            }
        });

        //when screen is dragged, translate it accordingly
        rootGroup.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent me) {
                stage.setX(me.getScreenX() - initX);
                stage.setY(me.getScreenY() - initY);
            }
        });
        
        // rectangle with adjustable translate
        Rectangle rect = new Rectangle(580,400,Color.web("fde7e3"));
        
        Button canbutton=new Button("关闭");
        canbutton.setStyle("-fx-base: rgb(30,170,255);");
        canbutton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                stage.close();
            }
        });
        
        
        
        rootGroup.getChildren().addAll(rect,canbutton);
    }
    
    public Node getExamplePlant() throws Exception{
        DataBaseManager dataBaseManager=new DataBaseManager();
        //架构底层
        StackPane stackPane = new StackPane();
        
        //将底层设置为层层叠
        VBox plantBox=new VBox();
        plantBox.setAlignment(Pos.CENTER);
        
        //获取题目数量
        List<Object[]> getCount=dataBaseManager.selectObject("select count(*) from T_WORD_TOPIC t where t.f_sys_flag='1'");
        
        int count=0;
        
        for(Object[] obj:getCount){
            count=Integer.parseInt(obj[0].toString());
        }
        
        //准备好从题目中随即乱抽
        Random random=new Random();
        Integer getCol=random.nextInt(count);
        
        //获取抽选到的题目
        List<WordTopic> getExample=dataBaseManager.selectObject(WordTopic.class," where t.f_sys_flag='1' limit "+getCol+",1");
        
        String example="";
        String pid="";
        for(WordTopic wordTopic:getExample){
            example=wordTopic.getContect();
            pid=wordTopic.getPid();
        }
        
        Label titleLabel=new Label(example);
        titleLabel.setPrefWidth(550);
        titleLabel.setWrapText(true);
        
        //设置答案的排版
        final VBox answerbox=new VBox(4);
        //获取抽选到的题目的答案
        tg = new ToggleGroup();
        List<WordTopicAnswer> getAnswerList=dataBaseManager.selectObject(WordTopicAnswer.class, " where t.F_PARENT_ID = "+pid+" and t.F_SYS_FLAG = '1'");
        
        int i=0;
        for(WordTopicAnswer wordTopicAnswer:getAnswerList){
            if( (i++) > 3){
                break;
            }
            RadioButton rb = new RadioButton(wordTopicAnswer.getAnswer());
            rb.setUserData(wordTopicAnswer);
            rb.setId(wordTopicAnswer.getIstrue());
            rb.setToggleGroup(tg);
            answerbox.getChildren().add(rb);
        }
        
        Button accButton=new Button("确定");
        accButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Iterator it=answerbox.getChildren().iterator();
                while(it.hasNext()){
                    RadioButton rb = (RadioButton)it.next();
                    //寻找选择的答案
                    if(rb.isSelected()){
                        if(rb.getId().equals("1")){
                            //TODO:答对的结果
                        }else{
                            //TODO:答错的结果
                        }
                    }
                }
            }
        });
        
        return stackPane;
    }
}
