/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.databaseModel.WordRecord;
import main.databaseTable.WordViewTable;
import main.innerView.InnerGroupView;
import support.database.DataBaseManager;
import support.tableView.TableViewCustom;

/**
 *
 * @author hdwjy
 */
public class WordInputCenter {
    private TaksObject taksObject;
    private DataBaseManager dataBaseManager=new DataBaseManager();
    
    public WordInputCenter(TaksObject taksObject){
        this.taksObject=taksObject;
    }
    
    @SuppressWarnings({"unchecked","fallthrough"})
    public void WordView() throws Exception{
        //清空原来所有的图表页面
        taksObject.getBorderPane().setCenter(null);
        
        //预计会有按键区跟列表区，所以预先设定为2
        VBox vbox=new VBox(2);
        vbox.setPadding(new Insets(5,0,5,10));
        vbox.setSpacing(10);
        //首先设定按键区
        HBox buttonBox=new HBox();
        
        buttonBox.setPrefHeight(40);
        
        Button newButton=new Button("新增");
        
        newButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                InnerGroupView innerView=new InnerGroupView(taksObject);
                Group newSaveRoot=innerView.newInnerView(null, null);
                
                innerView.newRoot.setContent(newSavePlant());
            }
        });
        
        Button delButton=new Button("删除");
        
        buttonBox.getChildren().addAll(newButton,delButton);
        
        //然后设定列表区
        //首先查询数据
        List<WordRecord> list=dataBaseManager.selectObject(WordRecord.class, null);
        
        //设定提交列表用的列表model
        List<WordViewTable> viewList=new ArrayList<WordViewTable>();
        for(WordRecord wordRecord:list){
            final WordViewTable wordViewTable=new WordViewTable();
            
            wordViewTable.setContect(wordRecord.getContect());
            wordViewTable.setExample(wordRecord.getExample());
            wordViewTable.setTrans(wordRecord.getTrans());
            wordViewTable.setType(wordRecord.getType());
            
            Button sampleButton=new Button("查看");
            sampleButton.setId("例题");
            wordViewTable.addItems(sampleButton);
            
            Button editButton=new Button("修改");
            editButton.setId("修改");
            wordViewTable.addItems(editButton);
            
            wordViewTable.setCheckBox(null);
            
            viewList.add(wordViewTable);
        }
        
        //以下是空结构时采用的空框架画图用
        WordViewTable newWordViewTable=new WordViewTable();
            
        Button sampleButton=new Button("查看");
        sampleButton.setId("例题");
        newWordViewTable.addItems(sampleButton);

        Button editButton=new Button("修改");
        editButton.setId("修改");
        newWordViewTable.addItems(editButton);

        newWordViewTable.setCheckBox(null);
        
        //设定初始
        TableViewCustom tableview=new TableViewCustom(newWordViewTable,"read");
        
        //初始化列表
        tableview.setTableViewColumn();
        
        //将初始化好的列表塞入值然后直接获得列表
        TableView retView=tableview.setTableViewData(viewList);
        retView.setPrefWidth(1024);
        
        //将按钮和列表塞入VBox
        vbox.getChildren().addAll(buttonBox,retView);
        
        //然后将VBOX塞入center
        taksObject.getBorderPane().setCenter(vbox);
    }
    
    /**
     * 新增平台内容
     * @return 
     */
    public Node newSavePlant(){
        
        HBox firstHbox=new HBox();
        Button accButton=new Button("确定");
        Button canButton=new Button("取消");
        
        //设定Hbox之间的相隔距离、和HBox内部构件之间的空间距离
        firstHbox.setPadding(new Insets(25,5,5,5));
        firstHbox.setSpacing(20);
        firstHbox.getChildren().addAll(accButton,canButton);
        
        
        VBox vbox=new VBox();
        vbox.getChildren().addAll(firstHbox);
        return vbox;
    }
}
