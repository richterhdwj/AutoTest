/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.HashMap;
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
    public HashMap rootMap=new HashMap();
    
    private DataBaseManager database=new DataBaseManager();

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
    
    /**
     * 首页的展示
     * @return 
     */
    @SuppressWarnings({"unchecked","fallthrough"})
    public Node ChartPane(){
        
        String[] years = {"2007", "2008", "2009"};
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(years));
        NumberAxis yAxis = new NumberAxis("Units Sold", 0.0d, 3000.0d, 1000.0d);
        
        XYChart.Data temp=new BarChart.Data(years[0], 567d);
        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
            new BarChart.Series("Apples", FXCollections.observableArrayList(
               temp,
               new BarChart.Data(years[1], 1292d),
               new BarChart.Data(years[2], 1292d)
            )),
            new BarChart.Series("Lemons", FXCollections.observableArrayList(
               new BarChart.Data(years[0], 956),
               new BarChart.Data(years[1], 1665),
               new BarChart.Data(years[2], 2559)
            )),
            new BarChart.Series("Oranges", FXCollections.observableArrayList(
               new BarChart.Data(years[0], 1154),
               new BarChart.Data(years[1], 1927),
               new BarChart.Data(years[2], 2774)
            ))
        );
        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
        
        chart.setPrefSize(1024, 480);
        chart.setTitle("综合成绩单");
        
        return chart;
    }
    
    public Object save(Object obj) throws Exception{
        database.save(obj);
        return database.selectObject(obj.getClass(), null);
    }
}
