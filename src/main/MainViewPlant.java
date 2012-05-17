/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.innerView.InnerGroupView;

/**
 *
 * @author hdwjy
 */
public class MainViewPlant {

    TaksObject taksObject;
    TranslateTransition rollTextLabel;
    TranslateTransition rollTextLabelBak;
    Label moveLabel;
    Label moveLabelbak;
    Font textlabelFont = new Font(24);

    private Font getTexlabelFont() {
        return textlabelFont.font("黑体", FontWeight.LIGHT, 24);
    }

    public MainViewPlant(TaksObject taksObject) {
        this.taksObject = taksObject;
        main();
    }

    public void main() {
        Group root = taksObject.getRoot();
        //设定主界面底层
        final BorderPane borderPane = new BorderPane();
        //塞入整体结构参与全局控制
        taksObject.setBorderPane(borderPane);

        //top层设定为menu菜单的内容

        //设定Menu标题栏
        final MenuBar menubar = new MenuBar();
        menubar.setPrefWidth(1024);

        final MenuItem menuAdd = MenuItemBuilder.create().text("新增条目").build();
        menuAdd.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!taksObject.getRoot().getChildren().get(0).getClass().isInstance(new WordInputCenter(taksObject))) {
                    try {
                        new WordInputCenter(taksObject).WordView();
                    } catch (Exception ex) {
                        Logger.getLogger(MainViewPlant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        final MenuItem menuTest = MenuItemBuilder.create().text("开始测验").build();
        menuTest.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                if (!taksObject.isHasAnswer()) {
                    try {
                        new InnerGroupView(taksObject).newOutView();
                    } catch (Exception ex) {
                        Logger.getLogger(MainViewPlant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        final MenuItem menuSetup = MenuItemBuilder.create().text("基础设定").build();

        // Options menu
        Menu menu = MenuBuilder.create().text("开始").items(menuAdd, menuTest, menuSetup).build();

        menubar.getMenus().addAll(menu);

        //然后标题和菜单用VBox统一起来
        VBox topbox = new VBox();
        topbox.getChildren().addAll(menubar);

        //top层设定为标题＋menu菜单的内容
        borderPane.setTop(topbox);

        //center层为图表

        borderPane.setCenter(taksObject.ChartPane());


        root.getChildren().add(borderPane);
    }

    /**
     * 暂时放弃在首页实现（因为没必要）
     *
     * @return
     */
    private StackPane getRollingLabel() {
        StackPane rollingLabel = new StackPane();

        //首先设定滚动标题的内容
        moveLabel = new Label();
        moveLabel.setText("今天需要背诵的词句内容还有：单词 20 个，语法 10个，短句 15个。");

        //然后设定副滚动标题栏的内容
        moveLabelbak = new Label();
        moveLabelbak.setText("今天需要背诵的词句内容还有：单词 20 个，语法 10个，短句 15个。");

        //设定前遮挡
        Rectangle rectFront = new Rectangle(0, 0, 150, 30);
        rectFront.setFill(Color.WHITE);

        //设定后遮挡
        Rectangle rectBack = new Rectangle(700, 0, 150, 30);
        rectBack.setFill(Color.WHITE);

        //设定滚动标题的滚动方式
        rollTextLabel = new TranslateTransition(Duration.seconds(25), moveLabel);
        rollTextLabel.setFromX(taksObject.getPrimaryStage().getScene().getWidth() + 50);
        rollTextLabel.setFromY(20);
        rollTextLabel.setToX(-moveLabel.getText().length() * moveLabel.getFont().getSize());
        rollTextLabel.setCycleCount(Timeline.INDEFINITE);
        rollTextLabel.setAutoReverse(false);

        //设定副滚动标题的滚动方式
        rollTextLabelBak = new TranslateTransition(Duration.seconds(25), moveLabel);
        rollTextLabelBak.setFromX(taksObject.getPrimaryStage().getScene().getWidth() + 50);
        rollTextLabelBak.setFromY(20);
        rollTextLabelBak.setToX(-moveLabel.getText().length() * moveLabel.getFont().getSize());
        rollTextLabelBak.setCycleCount(Timeline.INDEFINITE);
        rollTextLabelBak.setAutoReverse(false);

        rollingLabel.getChildren().addAll(moveLabel, moveLabelbak, rectFront, rectBack);
        return rollingLabel;
    }
}
