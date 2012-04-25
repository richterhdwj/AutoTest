/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.tableView;

import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * 自定义tableview,只要塞入一个model的结构值，就可以自动根据这个结构生成一个tableView表结构。
 * 可通过model中的构造方法来进行tableView的修饰
 *
 * @author Richter
 */
public class TableViewCustom extends TableView {

    public static String READ = "read";
    public static String WRITE = "write";
    private TableView tableViewCustom;  //返回用的值
    private Stage node;                 //获取整体框架
    private Double width;               //获取整体宽度
    private boolean isRead = true;      //判断是否建立的为查看表还是编辑表
    private TableViewColumnSet[] getTableViewSet; //通过结构得到表格建立说明
    private Object getTableClass;

    /**
     * 当flag为read的时候，取tableObject的read()方法的数据， 而为write的时候，则取write()方法的数据
     *
     * @param tableObject
     * @param node
     * @param flag
     */
    public TableViewCustom(Object tableObject, Stage node, String flag) throws Exception {
        getTableClass = tableObject;
        tableViewCustom = new TableView();
        this.node = node;
        width = node.getWidth();
        Class paramets[] = new Class[0];
        Object obj[] = new Object[0];
        if (flag.equals(READ)) {
            getTableViewSet = (TableViewColumnSet[]) tableObject.getClass().getMethod("read", paramets).invoke(tableObject, obj);
        } else if (flag.equals(WRITE)) {
            isRead = false;
            getTableViewSet = (TableViewColumnSet[]) tableObject.getClass().getMethod("read", paramets).invoke(tableObject, obj);
        }
    }

    /**
     * 首先设置表格所需要的列
     */
    public void setTableViewColumn() {
        /**
         * 清空已有的数据
         */
        tableViewCustom.getColumns().clear();
        tableViewCustom.getItems().clear();
        /**
         * 获取每列数据的宽度
         */
        Double autoWidth=this.getTableViewColumnWidth();
        //设置表头属性
        for (TableViewColumnSet tableViewSet : getTableViewSet) {
            /**
             * 首先画出表格列
             */
            TableColumn newSomeObjectCol = new TableColumn();
            newSomeObjectCol.setText(tableViewSet.getColName());
            if (tableViewSet.getTypeName().equals("checkBox")) {
                newSomeObjectCol.setMaxWidth(20);
                newSomeObjectCol.setMinWidth(20);
            } else {
                if (tableViewSet.getColWidth() != null) {
                    newSomeObjectCol.setPrefWidth(tableViewSet.getColWidth());
                }else{
                    newSomeObjectCol.setPrefWidth(autoWidth);
                }
            }

            newSomeObjectCol.setCellValueFactory(new PropertyValueFactory(tableViewSet.getTypeName()));
            /**
             * 将生成的列塞入表结构中
             */
            tableViewCustom.getColumns().add(newSomeObjectCol);
        }
    }
    
    /**
     * 获取默认列宽，如果每列剩余宽度不足80的话，则设为80
     * @return 
     */
    private Double getTableViewColumnWidth(){
        Double width=node.getWidth();
        int col=0;
        for(TableViewColumnSet tableViewSet : getTableViewSet){
            if (tableViewSet.getTypeName().equals("checkBox")) {
                width=width-20;
            }else if (tableViewSet.getColWidth() != null) {
                width=width-tableViewSet.getColWidth();
            }
            col++;
        }
        width=width/col;
        if(width<80){
            width=80.0;
        }
        return width;
    }

    /**
     * 然后塞入所需要的数据
     */
    public void setTableViewData(List list) throws Exception{
        ObservableList data = FXCollections.observableArrayList();
        /**
         * 设定object类型参数tablevalue，循环获取list内的值
         */
        for (Object tableValue : list) {
            //判断塞入的列表值是否和表头一样
            if (!tableValue.getClass().isInstance(getTableClass)){
                data.clear();
                throw new Exception("塞入的列表值与列表表头设定的不一致。");
            }
            //设定一个临时的getInTableValue参数用于获取相应的列表参数值
            Object[] getInTableValue = new Object[getTableViewSet.length];
            
            //循环之前在设定表头时获取到的列表参数设置值
            int col=0;
            for (TableViewColumnSet tableViewSet : getTableViewSet) {
                if(isRead){
                    //设定列表为阅读时的参数值
                    if(tableViewSet.isIsRead()){
                        if(tableViewSet.getTypeName().equals("checkbox")){
                            CheckBox check = new CheckBox();
                            check.setId(tableViewSet.getColName());
                            check.setSelected(false);
                            getInTableValue[col]=new SimpleObjectProperty<CheckBox>(check);
                        }else if(tableViewSet.getTypeName().equals("button")){
                            getInTableValue[col]=new SimpleObjectProperty(tableViewSet.getValueType());
                        }else{
                            getInTableValue[col]=new SimpleStringProperty(tableViewSet.getValue().toString());
                        }
                    }
                }else{
                    //设定列表为可写时的参数值
                    if(tableViewSet.isIsWrite()){
                        //TODO:
                    }
                }
                col++;
            }
            data.add(getInTableValue);
        }
        tableViewCustom.setItems(data);
    }

    public TableView getTableViewCustom() {
        return tableViewCustom;
    }

    public void setTableViewCustom(TableView tableViewCustom) {
        this.tableViewCustom = tableViewCustom;
    }
}
