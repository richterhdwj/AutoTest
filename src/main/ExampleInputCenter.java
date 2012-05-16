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
import javafx.scene.effect.BlendMode;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.databaseModel.WordRecord;
import main.databaseModel.WordTopic;
import main.databaseModel.WordTopicAnswer;
import main.databaseTable.ExampleViewTable;
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
public class ExampleInputCenter {

    private TaksObject taksObject;
    private DataBaseManager dataBaseManager = new DataBaseManager();
    private TableViewCustom tableviewSeting;
    private TableView tableview;
    private WordTopic editWordTopic;
    private WordRecord wordRecord;
    private ScrollPane scrollpane;
    private InnerGroupView innerGroupView;
    public static String rootName = "wordNew";

    public ExampleInputCenter(TaksObject taksObject, WordRecord wordRecord) {
        this.taksObject = taksObject;
        this.wordRecord = wordRecord;
    }

    @SuppressWarnings({"unchecked", "fallthrough"})
    public void ExampleView() throws Exception {
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
                try {
                    innerGroupView.newRoot.getChildren().add(newSavePlant(null));
                } catch (Exception ex) {
                    Logger.getLogger(WordInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Button delButton = new Button("删除");
        delButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                //追加一个确认的内容
                innerGroupView = new InnerGroupView(taksObject);
                Group group = innerGroupView.confirm("确实需要删除吗？");

                innerGroupView.accButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent arg0) {

                        Iterator<TableViewCenter> nowItems = tableview.getItems().iterator();
                        while (nowItems.hasNext()) {
                            TableViewCenter tableViewCenter = nowItems.next();
                            CheckBox checkBox = (CheckBox) tableViewCenter.getObj1().getValue();
                            if (checkBox.isSelected()) {
                                ExampleViewTable exampleView = (ExampleViewTable) tableViewCenter.getObj();
                                WordTopic wordTopic = exampleView.getWordTopic();
                                wordTopic.setSysFlag("0");
                                try {
                                    taksObject.save(wordTopic);
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
                        innerGroupView = null;
                    }
                });

                innerGroupView.canButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent arg0) {
                        innerGroupView.rewakeConfirm(null);
                        innerGroupView = null;
                    }
                });
            }
        });

        Button backButton = new Button("返回");

        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                try {
                    new WordInputCenter(taksObject).WordView();
                } catch (Exception ex) {
                    Logger.getLogger(MainViewPlant.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        buttonBox.getChildren().addAll(newButton, delButton, backButton);

        //然后设定列表区

        //以下是空结构时采用的空框架画图用
        ExampleViewTable newExampleViewTable = new ExampleViewTable();

        Button resButton = new Button("答案");
        resButton.setId("答案");
        newExampleViewTable.addItems(resButton);

        Button editButton = new Button("修改");
        editButton.setId("修改");
        newExampleViewTable.addItems(editButton);

        newExampleViewTable.setCheckBox(null);

        //设定初始
        tableviewSeting = new TableViewCustom(newExampleViewTable, "read");

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
        List<WordTopic> list = dataBaseManager.selectObject(WordTopic.class, " where t.F_SYS_FLAG = '1' and t.F_TITLE = " + wordRecord.getPid());

        //设定提交列表用的列表model
        List<ExampleViewTable> viewList = new ArrayList<ExampleViewTable>();
        for (final WordTopic wordTopic : list) {
            ExampleViewTable exampleViewTable = new ExampleViewTable();

            exampleViewTable.setContect(wordTopic.getContect());
            exampleViewTable.setType(wordTopic.getType());
            exampleViewTable.setWordTopic(wordTopic);

            Button resButton = new Button("答案");
            resButton.setId("答案");
            resButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {
                    innerGroupView = new InnerGroupView(taksObject);

                    innerGroupView.newInnerView(null, null, rootName);
                    try {
                        innerGroupView.newRoot.getChildren().add(answerPlant(wordTopic));
                    } catch (Exception ex) {
                        Logger.getLogger(WordInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            exampleViewTable.addItems(resButton);

            Button editButton = new Button("修改");
            editButton.setId("修改");
            editButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {
                    innerGroupView = new InnerGroupView(taksObject);

                    innerGroupView.newInnerView(null, null, rootName);
                    try {
                        innerGroupView.newRoot.getChildren().add(newSavePlant(wordTopic));
                    } catch (Exception ex) {
                        Logger.getLogger(WordInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            exampleViewTable.addItems(editButton);

            exampleViewTable.setCheckBox(null);

            viewList.add(exampleViewTable);
        }

        tableview = tableviewSeting.setTableViewData(viewList);
    }

    /**
     * 新增/修改平台内容
     *
     * @return
     */
    public Node newSavePlant(WordTopic wordTopic) throws Exception {
        if (wordTopic != null) {
            editWordTopic = wordTopic;
        } else {
            editWordTopic = new WordTopic();
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

        HBox typeHbox = new HBox(2);
        Label typeLabel = new Label("类型");
        final ComboBox<String> typeCombo = new ComboBox<String>();
        typeCombo.setId("edit-ComboBox");
        typeCombo.setEditable(true);
        if (wordTopic != null && wordTopic.getType() != null) {
            typeCombo.setValue(wordTopic.getType());
            typeCombo.setPromptText(wordTopic.getType());
        }
        List<Object[]> list = dataBaseManager.selectObject("select t.F_TYPE from T_WORD_TOPIC t where t.F_SYS_FLAG='1' and t.F_TYPE is not null");

        if (wordTopic == null) {
            typeCombo.getSelectionModel().selectFirst();
        }
        typeCombo.setMaxWidth(150);
        typeHbox.setPadding(new Insets(5,5,5,5));
        typeHbox.setSpacing(10);
        typeHbox.getChildren().addAll(typeLabel, typeCombo);

        VBox wordHbox = new VBox(2);
        Label wordLabel = new Label("题目");
        final TextArea wordText = new TextArea();
        if (wordTopic != null && wordTopic.getContect() != null) {
            wordText.setText(wordTopic.getContect());
        }
        wordHbox.setPadding(new Insets(5,5,5,5));
        wordHbox.setSpacing(10);
        wordText.setPrefWidth(400);
        wordHbox.getChildren().addAll(wordLabel, wordText);

        //设定一个提示值，暂时永久提示下拉输入框输入值后要按回车提交值
        Label tipLabel = new Label("如果类型是输入的，请一定要记得输入完后按回车");
        tipLabel.setWrapText(true);
        tipLabel.setAlignment(Pos.CENTER);
        tipLabel.setFont(new Font("黑体", 12d));

        VBox vbox = new VBox();

        //因为显示的问题目前妥协成这样：首先做成非编辑框用以显示默认值，如果点击则改成编辑框
        typeCombo.setEditable(false);
        typeCombo.setPromptText(typeCombo.getValue());
        typeCombo.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                typeCombo.setEditable(true);
                typeCombo.setValue(typeCombo.getPromptText());
                System.out.println(typeCombo.getValue());
            }
        });

        vbox.getChildren().addAll(firstHbox, typeHbox, wordHbox, tipLabel);

        accButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                if (typeCombo.getValue() != null && !typeCombo.getValue().isEmpty()) {
                    editWordTopic.setType(typeCombo.getValue());
                    editWordTopic.setContect(wordText.getText());
                    editWordTopic.setCreateTime(DateBean.getSysdateTime());
                    if (editWordTopic.getPid() == null) {
                        editWordTopic.setSysFlag("1");
                        editWordTopic.setTitle(wordRecord.getPid());
                    }
                    try {
                        taksObject.save(editWordTopic);
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

    public Node answerPlant(final WordTopic wordtopic) throws Exception {
        final VBox answerBox = new VBox();

        HBox buttonBox = new HBox();
        Button closeButton = new Button("关闭");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {

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
        buttonBox.getChildren().add(closeButton);
        buttonBox.setPadding(new Insets(10,10,10,10));
        buttonBox.setSpacing(20);
        answerBox.getChildren().add(buttonBox);

        HBox connectBox = new HBox();

        final CheckBox trueAnswerBox = new CheckBox("是否正确答案？");
        trueAnswerBox.setSelected(false);

        final TextField textField = new TextField();
        textField.setPromptText("这里填写答案内容");

        Button setInButton = new Button("添加");
        setInButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                if (textField.getText() != null && !textField.getText().isEmpty()) {
                    WordTopicAnswer wordanswer = new WordTopicAnswer();
                    wordanswer.setAnswer(textField.getText());
                    if (trueAnswerBox.isSelected()) {
                        wordanswer.setIstrue("1");
                    } else {
                        wordanswer.setIstrue("0");
                    }
                    wordanswer.setParentId(wordtopic.getPid());
                    wordanswer.setCreateTime(DateBean.getSysdateTime());
                    wordanswer.setType("select");
                    wordanswer.setSysFlag("1");
                    try {
                        taksObject.save(wordanswer);
                    } catch (Exception ex) {
                        Logger.getLogger(ExampleInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    textField.clear();
                    trueAnswerBox.setSelected(false);

                    answerBox.getChildren().remove(scrollpane);
                    try {
                        scrollpane = getWordAnswer(wordtopic);
                    } catch (Exception ex) {
                        Logger.getLogger(ExampleInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    answerBox.getChildren().add(scrollpane);
                }
            }
        });
        connectBox.getChildren().addAll(trueAnswerBox, textField, setInButton);
        connectBox.setPadding(new Insets(5, 10, 10, 10));
        connectBox.setSpacing(10);
        answerBox.getChildren().add(connectBox);

        Button delButton = new Button("删除");
        delButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                VBox vbox = (VBox) scrollpane.getContent();
                Iterator it = vbox.getChildren().iterator();
                while (it.hasNext()) {
                    HBox hbox = (HBox) it.next();
                    CheckBox checkbox = (CheckBox) hbox.getChildren().get(0);
                    if (checkbox.isSelected()) {
                        WordTopicAnswer wordAns = (WordTopicAnswer) checkbox.getUserData();
                        wordAns.setSysFlag("0");
                        try {
                            taksObject.save(wordAns);
                        } catch (Exception ex) {
                            Logger.getLogger(ExampleInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                answerBox.getChildren().remove(scrollpane);
                try {
                    scrollpane = getWordAnswer(wordtopic);
                } catch (Exception ex) {
                    Logger.getLogger(ExampleInputCenter.class.getName()).log(Level.SEVERE, null, ex);
                }
                answerBox.getChildren().add(scrollpane);
            }
        });
        answerBox.getChildren().add(delButton);

        scrollpane = this.getWordAnswer(wordtopic);
        answerBox.getChildren().add(scrollpane);

        return answerBox;
    }

    @SuppressWarnings({"unchecked", "fallthrough"})
    public ScrollPane getWordAnswer(WordTopic wordtopic) throws Exception {
        ScrollPane scrollpane = new ScrollPane();

        List<WordTopicAnswer> list = dataBaseManager.selectObject(WordTopicAnswer.class, " where t.F_PARENT_ID=" + wordtopic.getPid()
                + " and t.f_sys_flag='1'");
        VBox vbox = new VBox();
        vbox.setMinWidth(400);
        
        HBox firstbox=new HBox(3);
        Label checkLabel=new Label(" ");
        checkLabel.setPrefWidth(20);
        Label tureLabel=new Label("是否正确答案");
        tureLabel.setPrefWidth(100);
        Label firanslabel=new Label("答案内容");
        firstbox.setSpacing(20);
        firstbox.getChildren().addAll(checkLabel,tureLabel,firanslabel);
        
        vbox.getChildren().add(firstbox);
        
        for (WordTopicAnswer wordTopicAnswer : list) {
            HBox hbox = new HBox(3);
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(false);
            checkBox.setUserData(wordTopicAnswer);
            checkBox.setPrefWidth(20);

            String hasWord = "否";
            if (wordTopicAnswer.getIstrue().equals("1")) {
                hasWord = "是";
            }

            Label label = new Label(hasWord);
            label.setPrefWidth(100);
            Label ansLabel = new Label(wordTopicAnswer.getAnswer());
            
            hbox.setSpacing(20);
            hbox.getChildren().addAll(checkBox, label, ansLabel);

            vbox.getChildren().add(hbox);
        }
        scrollpane.autosize();
        scrollpane.setContent(vbox);

        return scrollpane;
    }
}
