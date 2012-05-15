/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.innerView;

import java.util.HashMap;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.TaksObject;

/**
 *
 * @author hdwjy
 */
public class InnerGroupView {
    private TaksObject taksObject;
    public StackPane newRoot;
    public Button accButton;
    public Button canButton;
    
    public InnerGroupView(TaksObject taksObject){
        this.taksObject=taksObject;
    }
    
    /**
     * 创建一个模态窗口，width是宽度，height是高度，id则是这个模态窗口的id（用来指定，方便从内存中剔除）
     * @param width
     * @param height
     * @param id
     * @return 
     */
    @SuppressWarnings({"unchecked","fallthrough"})
    public Group newInnerView(Double width,Double height,String id){
        Group root=new Group();
        
        Iterator<Node> it=taksObject.getRoot().getChildren().iterator();
        while(it.hasNext()){
            Node node=it.next();
            node.setDisable(true);
        }
        
        if(width==null)
            width=taksObject.getPrimaryStage().getScene().getWidth()/2;
        
        if(height==null)
            height=taksObject.getPrimaryStage().getScene().getHeight()/2;
        
        Rectangle rect1 = new Rectangle(0, 0, width, height);
        rect1.setFill(Color.WHITESMOKE);
        rect1.setStroke(Color.DARKRED);
        
        //这样的新视图都在中间显示
        rect1.setLayoutX(taksObject.getPrimaryStage().getScene().getWidth()/2-width/2);
        rect1.setLayoutY(taksObject.getPrimaryStage().getScene().getHeight()/2-height/2);
        
        //将所有的在这个内部新窗口的所有内容置于这个滚动平台内
        newRoot=new StackPane();
        newRoot.setLayoutX(taksObject.getPrimaryStage().getScene().getWidth()/2-width/2);
        newRoot.setLayoutY(taksObject.getPrimaryStage().getScene().getHeight()/2-height/2);
        
        newRoot.setPrefSize(width, height);
        
        root.getChildren().addAll(rect1,newRoot);
        
        taksObject.getRoot().getChildren().add(root);
        
        taksObject.rootMap.put(id, root);
        
        return root;
    }
    
    /**
     * 创建一个小的用于用户确认的小窗口，目前问题需要将按钮的监听写到外面
     * @param message
     * @return 
     */
    @SuppressWarnings({"unchecked","fallthrough"})
    public Group confirm(String message){
        Group confirmGroup=this.newInnerView(300d, 150d, "confirm");
        
        Label label=new Label(message);
        label.setAlignment(Pos.CENTER);
        
        accButton=new Button("确认");
        
        canButton=new Button("取消");
        
        HBox buttonBox=new HBox();
        buttonBox.setSpacing(40);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.getChildren().addAll(accButton,canButton);
        
        VBox vbox=new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        
        vbox.getChildren().addAll(label,buttonBox);
        
        newRoot.getChildren().add(vbox);
        newRoot.setAlignment(Pos.CENTER);
        
        return confirmGroup;
    }
    
    /**
     * 唤醒被disable的其它内容，mapkey则用来清除相应的模态窗口
     * @param root
     * @param mapKey 
     */
    @SuppressWarnings({"unchecked","fallthrough"})
    public void rewake(Group root,String mapKey){
        if(root==null){
            Iterator<Node> it=taksObject.getRoot().getChildren().iterator();
            while(it.hasNext()){
                Node node=it.next();
                node.setDisable(false);
            }
            taksObject.getRoot().getChildren().remove(taksObject.rootMap.get(mapKey));
            taksObject.rootMap.remove(mapKey);
        }else{
            Iterator<Node> it=taksObject.getRoot().getChildren().iterator();
            while(it.hasNext()){
                Node node=it.next();
                if(root.equals(node))
                    node.setDisable(false);
            }
            taksObject.getRoot().getChildren().remove(taksObject.rootMap.get(mapKey));
            taksObject.rootMap.remove(mapKey);
        }
    }
    
    /**
     * 确认小平台的默认摸消方法
     * @param root 
     */
    public void rewakeConfirm(Group root){
        this.rewake(root, "confirm");
    }
}
