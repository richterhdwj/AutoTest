/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.innerView;

import java.util.Iterator;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.TaksObject;

/**
 *
 * @author hdwjy
 */
public class InnerGroupView {
    private TaksObject taksObject;
    public ScrollPane newRoot;
    
    public InnerGroupView(TaksObject taksObject){
        this.taksObject=taksObject;
    }
    
    public Group newInnerView(Double width,Double height){
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
        rect1.setStroke(null);
        
        //这样的新视图都在中间显示
        rect1.setLayoutX(taksObject.getPrimaryStage().getScene().getWidth()/2-width/2);
        rect1.setLayoutY(taksObject.getPrimaryStage().getScene().getHeight()/2-height/2);
        
        //将所有的在这个内部新窗口的所有内容置于这个滚动平台内
        newRoot=new ScrollPane();
        newRoot.setLayoutX(taksObject.getPrimaryStage().getScene().getWidth()/2-width/2);
        newRoot.setLayoutY(taksObject.getPrimaryStage().getScene().getHeight()/2-height/2);
        
        newRoot.setPrefSize(width, height);
        
        root.getChildren().addAll(rect1,newRoot);
        
        taksObject.getRoot().getChildren().add(root);
        
        return root;
    }
}
