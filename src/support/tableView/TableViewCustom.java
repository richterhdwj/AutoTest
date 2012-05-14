/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.tableView;

import java.util.HashMap;
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
public class TableViewCustom{

    public static String READ = "read";
    public static String WRITE = "write";
    private TableView tableViewCustom;  //返回用的值
    private boolean isRead = true;      //判断是否建立的为查看表还是编辑表
    private TableViewColumnSet[] getTableViewSet;   //通过结构得到表格建立说明
    private Object getTableClass;                   //结构预存值。
    private int nameNum = 0;                //表示表格内有多少个字符展示格
    private int doubleNum = 0;              //表示表格内有多少个数字展示格
    private int objNum = 0;                 //表示表格内有多少个其他类型展示格
    private HashMap tempMap;                //存储临时空间中的字段对应关系
    private HashMap tempModelMap;           //用于存储临时空间与模型之间的字段对应关系

    /**
     * 当flag为read的时候，取tableObject的read()方法的数据， 而为write的时候，则取write()方法的数据
     *
     * @param tableObject
     * @param node
     * @param flag
     */
    public TableViewCustom(Object tableObject,String flag) throws Exception {
        getTableClass = tableObject;
        tableViewCustom = new TableView();
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
     * 首先设置表格所需要的列,也是用于重置数据的第一步
     */
    @SuppressWarnings({"unchecked","fallthrough"})
    public void setTableViewColumn() {
        /**
         * 清空已有的数据
         */
        nameNum = 0;
        objNum = 0;
        doubleNum = 0;
        tableViewCustom.getColumns().clear();
        tableViewCustom.getItems().clear();
        /**
         * 获取每列数据的宽度
         */
        tempMap = new HashMap();
        tempModelMap = new HashMap();
        int maxCol = 0;
        //设置表头属性
        for (TableViewColumnSet tableViewSet : getTableViewSet) {
            if(!isRead){
                if(!tableViewSet.isIsWrite())
                    continue;
            };
            /**
             * 首先画出表格列
             */
            TableColumn newSomeObjectCol = new TableColumn();
            newSomeObjectCol.setText(tableViewSet.getColName());
            if (tableViewSet.getTypeName().equals("checkBox")) {
                newSomeObjectCol.setMaxWidth(20);
                newSomeObjectCol.setMinWidth(20);
                newSomeObjectCol.setResizable(true);
            } else {
                if (tableViewSet.getColWidth() != null){
                    if (tableViewSet.getColWidth() > 0) {
                        newSomeObjectCol.setPrefWidth(tableViewSet.getColWidth());
                    }
                }
            }

            String nowColName = null;
            if (isRead) {
                if(tableViewSet.isIsRead()){
                    nowColName = getParametTypeNameRead(tableViewSet.getTypeName());
                    tempMap.put(maxCol++, nowColName);
                }
            } else {
                if(tableViewSet.isIsWrite()){
                    nowColName = getParametTypeNameWrite(tableViewSet.getValueType(), tableViewSet.getValue());
                    tempMap.put(maxCol++, nowColName);
                }
            }
            
            //保存下模型名与临时名之间的关系，方便取值
            tempModelMap.put(tableViewSet.getColName(),nowColName);

            newSomeObjectCol.setCellValueFactory(new PropertyValueFactory(nowColName));
            /**
             * 将生成的列塞入表结构中
             */
            tableViewCustom.getColumns().add(newSomeObjectCol);
        }
    }

    private String getParametTypeNameRead(String parametType) {
        String ret = null;
        if (parametType == null) {
            ret = TableViewCenterNames.stringName[nameNum];
            nameNum++;
        } else if (parametType.equals("Node")) {
            ret = TableViewCenterNames.objectName[objNum];
            objNum++;
        } else if (parametType.equals("checkbox")) {
            ret = TableViewCenterNames.objectName[objNum];
            objNum++;
        } else {
            ret = TableViewCenterNames.stringName[nameNum];
            nameNum++;
        }
        return ret;
    }

    private String getParametTypeNameWrite(Node parametType, Object value) {
        String ret = null;
        if (parametType == null) {
            if (value != null) {
                if (value.getClass().isInstance(new String())) {
                    ret = TableViewCenterNames.stringName[nameNum];
                    nameNum++;
                } else if (value.getClass().isInstance(Double.TYPE)) {
                    ret = TableViewCenterNames.doubleName[nameNum];
                    doubleNum++;
                } else {
                    ret = TableViewCenterNames.stringName[nameNum];
                    nameNum++;
                }
            } else {
                ret = TableViewCenterNames.stringName[nameNum];
                nameNum++;
            }
        } else {
            ret = TableViewCenterNames.objectName[objNum];
            objNum++;
        }
        return ret;
    }

//    /**
//     * 获取默认列宽，如果每列剩余宽度不足80的话，则设为80
//     *
//     * @return
//     */
//    private Double getTableViewColumnWidth() {
//        int col = 0;
//        for (TableViewColumnSet tableViewSet : getTableViewSet) {
//            if(isRead){
//                if(tableViewSet.isIsRead())
//                    if (tableViewSet.getTypeName().equals("checkBox")) {
//                        widthThis = widthThis - 20;
//                    } else if (tableViewSet.getColWidth() != null && tableViewSet.getColWidth() > 0) {
//                        widthThis = widthThis - tableViewSet.getColWidth();
//                    } else {
//                        col++;
//                    }
//            }else{
//                if(tableViewSet.isIsWrite())
//                    if (tableViewSet.getTypeName().equals("checkBox")) {
//                        widthThis = widthThis - 20;
//                    } else if (tableViewSet.getColWidth() != null && tableViewSet.getColWidth() > 0) {
//                        widthThis = widthThis - tableViewSet.getColWidth();
//                    } else {
//                        col++;
//                    }
//            }
//        }
//        widthThis = widthThis / col;
//        if (widthThis < 80) {
//            widthThis = 80.0;
//        }
//        return widthThis;
//    }

    /**
     * 然后塞入所需要的数据
     */
    @SuppressWarnings({"unchecked","fallthrough"})
    public TableView setTableViewData(List list) throws Exception {
        ObservableList data = FXCollections.observableArrayList();
        /**
         * 设定object类型参数tablevalue，循环获取list内的值
         */
        for (Object tableValue : list) {
            //判断塞入的列表值是否和表头一样
            if (!tableValue.getClass().isInstance(getTableClass)) {
                data.clear();
                throw new Exception("塞入的列表值与列表表头设定的不一致。");
            }
            //成功的话依据当前结构值重新获取一次列表设置
            Class paramets[] = new Class[0];
            Object obj[] = new Object[0];
            if (isRead) {
                getTableViewSet = (TableViewColumnSet[]) tableValue.getClass().getMethod("read", paramets).invoke(tableValue, obj);
            } else {
                getTableViewSet = (TableViewColumnSet[]) tableValue.getClass().getMethod("read", paramets).invoke(tableValue, obj);
            }
            //所有表都通过的TableViewCenter类来展示
            TableViewCenter tableViewcenter = new TableViewCenter();

            //循环之前在设定表头时获取到的列表参数设置值
            int col = 0;
            for (TableViewColumnSet tableViewSet : getTableViewSet) {
                if (isRead) {
                    //设定列表为阅读时的参数值
                    if (tableViewSet.isIsRead()) {
                        if (tableViewSet.getTypeName().equals("checkbox") || tableViewSet.getTypeName().equals("Node")) {
                            tableViewcenter.setAnyObject((String) this.tempMap.get(col), tableViewSet.getValueType());
                        } else {
                            try{
                                tableViewcenter.setAnyObject((String) this.tempMap.get(col), tableViewSet.getValue().toString());
                            }catch(Exception e){
                                
                            }
                        }
                        col++;
                    }
                } else {
                    //设定列表为可写时的参数值
                    if (tableViewSet.isIsWrite()) {
                        if (tableViewSet.getValueType() == null) {
                            tableViewcenter.setAnyObject((String) this.tempMap.get(col), tableViewSet.getValue().toString());
                        } else {
                            tableViewcenter.setAnyObject((String) this.tempMap.get(col), tableViewSet.getValueType());
                        }
                        col++;
                    }
                }
            }
            tableViewcenter.setObj(tableValue);
            
            data.add(tableViewcenter);
        }
        tableViewCustom.setItems(data);
        
        return tableViewCustom;
    }

    /**
     *获取临时结构中的值
     */
    public Object getCenterValue(Object obj, String getName) {
        Object ret = null;
        try {
            ret = obj.getClass().getMethod(getName+"Property",(Class<?>) null);
        } catch (Exception e) {
            System.out.println(obj.getClass().getName() + "里面没有" +getName+"Property"+ "方法");
        }
        return ret;
    }

    public TableView getTableViewCustom() {
        return tableViewCustom;
    }

    public void setTableViewCustom(TableView tableViewCustom) {
        this.tableViewCustom = tableViewCustom;
    }

    private static class TableViewCenterNames {

        static final String[] stringName = new String[]{
            "name1", "name2", "name3", "name4", "name5", "name6", "name7", "name8", "name9", "name10",
            "name11", "name12", "name13", "name14", "name15", "name16", "name17", "name18", "name19", "name20"
        };
        static final String[] doubleName = new String[]{
            "double1", "double2", "double3", "double4", "double5", "double6", "double7", "double8", "double9", "double10",
            "double11", "double12", "double13", "double14", "double15", "double16", "double17", "double18", "double19", "double20"
        };
        static final String[] objectName = new String[]{
            "obj1", "obj2", "obj3", "obj4", "obj5", "obj6", "obj7", "obj8", "obj9", "obj10",
            "obj11", "obj12", "obj13", "obj14", "obj15", "obj16", "obj17", "obj18", "obj19", "obj20",};
    }

    public class TableViewCenter {

        private StringProperty name1;
        private StringProperty name2;
        private StringProperty name3;
        private StringProperty name4;
        private StringProperty name5;
        private StringProperty name6;
        private StringProperty name7;
        private StringProperty name8;
        private StringProperty name9;
        private StringProperty name10;
        private StringProperty name11;
        private StringProperty name12;
        private StringProperty name13;
        private StringProperty name14;
        private StringProperty name15;
        private StringProperty name16;
        private StringProperty name17;
        private StringProperty name18;
        private StringProperty name19;
        private StringProperty name20;
        private DoubleProperty double1;
        private DoubleProperty double2;
        private DoubleProperty double3;
        private DoubleProperty double4;
        private DoubleProperty double5;
        private DoubleProperty double6;
        private DoubleProperty double7;
        private DoubleProperty double8;
        private DoubleProperty double9;
        private DoubleProperty double10;
        private DoubleProperty double11;
        private DoubleProperty double12;
        private DoubleProperty double13;
        private DoubleProperty double14;
        private DoubleProperty double15;
        private DoubleProperty double16;
        private DoubleProperty double17;
        private DoubleProperty double18;
        private DoubleProperty double19;
        private DoubleProperty double20;
        private ObjectProperty obj1;
        private ObjectProperty obj2;
        private ObjectProperty obj3;
        private ObjectProperty obj4;
        private ObjectProperty obj5;
        private ObjectProperty obj6;
        private ObjectProperty obj7;
        private ObjectProperty obj8;
        private ObjectProperty obj9;
        private ObjectProperty obj10;
        private ObjectProperty obj11;
        private ObjectProperty obj12;
        private ObjectProperty obj13;
        private ObjectProperty obj14;
        private ObjectProperty obj15;
        private ObjectProperty obj16;
        private ObjectProperty obj17;
        private ObjectProperty obj18;
        private ObjectProperty obj19;
        private ObjectProperty obj20;
        private Object obj;

        public StringProperty name1Property() {
            return name1;
        }

        public StringProperty name2Property() {
            return name2;
        }

        public StringProperty name3Property() {
            return name3;
        }

        public StringProperty name4Property() {
            return name4;
        }

        public StringProperty name5Property() {
            return name5;
        }

        public StringProperty name6Property() {
            return name6;
        }

        public StringProperty name7Property() {
            return name7;
        }

        public StringProperty name8Property() {
            return name8;
        }

        public StringProperty name9Property() {
            return name9;
        }

        public StringProperty name10Property() {
            return name10;
        }

        public StringProperty name11Property() {
            return name11;
        }

        public StringProperty name12Property() {
            return name12;
        }

        public StringProperty name13Property() {
            return name13;
        }

        public StringProperty name14Property() {
            return name14;
        }

        public StringProperty name15Property() {
            return name15;
        }

        public StringProperty name16Property() {
            return name16;
        }

        public StringProperty name17Property() {
            return name17;
        }

        public StringProperty name18Property() {
            return name18;
        }

        public StringProperty name19Property() {
            return name19;
        }

        public StringProperty name20Property() {
            return name20;
        }

        public DoubleProperty double1Property() {
            return double1;
        }

        public DoubleProperty double2Property() {
            return double2;
        }

        public DoubleProperty double3Property() {
            return double3;
        }

        public DoubleProperty double4Property() {
            return double4;
        }

        public DoubleProperty double5Property() {
            return double5;
        }

        public DoubleProperty double6Property() {
            return double6;
        }

        public DoubleProperty double7Property() {
            return double7;
        }

        public DoubleProperty double8Property() {
            return double8;
        }

        public DoubleProperty double9Property() {
            return double9;
        }

        public DoubleProperty double10Property() {
            return double10;
        }

        public DoubleProperty double11Property() {
            return double11;
        }

        public DoubleProperty double12Property() {
            return double12;
        }

        public DoubleProperty double13Property() {
            return double13;
        }

        public DoubleProperty double14Property() {
            return double14;
        }

        public DoubleProperty double15Property() {
            return double15;
        }

        public DoubleProperty double16Property() {
            return double16;
        }

        public DoubleProperty double17Property() {
            return double17;
        }

        public DoubleProperty double18Property() {
            return double18;
        }

        public DoubleProperty double19Property() {
            return double19;
        }

        public DoubleProperty double20Property() {
            return double20;
        }

        public ObjectProperty obj1Property() {
            return obj1;
        }

        public ObjectProperty obj2Property() {
            return obj2;
        }

        public ObjectProperty obj3Property() {
            return obj3;
        }

        public ObjectProperty obj4Property() {
            return obj4;
        }

        public ObjectProperty obj5Property() {
            return obj5;
        }

        public ObjectProperty obj6Property() {
            return obj6;
        }

        public ObjectProperty obj7Property() {
            return obj7;
        }

        public ObjectProperty obj8Property() {
            return obj8;
        }

        public ObjectProperty obj9Property() {
            return obj9;
        }

        public ObjectProperty obj10Property() {
            return obj10;
        }

        public ObjectProperty obj11Property() {
            return obj11;
        }

        public ObjectProperty obj12Property() {
            return obj12;
        }

        public ObjectProperty obj13Property() {
            return obj13;
        }

        public ObjectProperty obj14Property() {
            return obj14;
        }

        public ObjectProperty obj15Property() {
            return obj15;
        }

        public ObjectProperty obj16Property() {
            return obj16;
        }

        public ObjectProperty obj17Property() {
            return obj17;
        }

        public ObjectProperty obj18Property() {
            return obj18;
        }

        public ObjectProperty obj19Property() {
            return obj19;
        }

        public ObjectProperty obj20Property() {
            return obj20;
        }

        public void setDouble1(Double double1) {
            this.double1 = new SimpleDoubleProperty(double1);
        }

        public void setDouble10(Double double10) {
            this.double10 = new SimpleDoubleProperty(double10);
        }

        public void setDouble11(Double double11) {
            this.double11 = new SimpleDoubleProperty(double11);
        }

        public void setDouble12(Double double12) {
            this.double12 = new SimpleDoubleProperty(double12);
        }

        public void setDouble13(Double double13) {
            this.double13 = new SimpleDoubleProperty(double13);
        }

        public void setDouble14(Double double14) {
            this.double14 = new SimpleDoubleProperty(double14);
        }

        public void setDouble15(Double double15) {
            this.double15 = new SimpleDoubleProperty(double15);
        }

        public void setDouble16(Double double16) {
            this.double16 = new SimpleDoubleProperty(double16);
        }

        public void setDouble17(Double double17) {
            this.double17 = new SimpleDoubleProperty(double17);
        }

        public void setDouble18(Double double18) {
            this.double18 = new SimpleDoubleProperty(double18);
        }

        public void setDouble19(Double double19) {
            this.double19 = new SimpleDoubleProperty(double19);
        }

        public void setDouble2(Double double2) {
            this.double2 = new SimpleDoubleProperty(double2);
        }

        public void setDouble20(Double double20) {
            this.double20 = new SimpleDoubleProperty(double20);
        }

        public void setDouble3(Double double3) {
            this.double3 = new SimpleDoubleProperty(double3);
        }

        public void setDouble4(Double double4) {
            this.double4 = new SimpleDoubleProperty(double4);
        }

        public void setDouble5(Double double5) {
            this.double5 = new SimpleDoubleProperty(double5);
        }

        public void setDouble6(Double double6) {
            this.double6 = new SimpleDoubleProperty(double6);
        }

        public void setDouble7(Double double7) {
            this.double7 = new SimpleDoubleProperty(double7);
        }

        public void setDouble8(Double double8) {
            this.double8 = new SimpleDoubleProperty(double8);
        }

        public void setDouble9(Double double9) {
            this.double9 = new SimpleDoubleProperty(double9);
        }

        /**
         * ***********************************************************************
         */
        public void setName1(String name1) {
            this.name1 = new SimpleStringProperty(name1);
        }

        public void setName10(String name10) {
            this.name10 = new SimpleStringProperty(name10);
        }

        public void setName11(String name11) {
            this.name11 = new SimpleStringProperty(name11);
        }

        public void setName12(String name12) {
            this.name12 = new SimpleStringProperty(name12);
        }

        public void setName13(String name13) {
            this.name13 = new SimpleStringProperty(name13);
        }

        public void setName14(String name14) {
            this.name14 = new SimpleStringProperty(name14);
        }

        public void setName15(String name15) {
            this.name15 = new SimpleStringProperty(name15);
        }

        public void setName16(String name16) {
            this.name16 = new SimpleStringProperty(name16);
        }

        public void setName17(String name17) {
            this.name17 = new SimpleStringProperty(name17);
        }

        public void setName18(String name18) {
            this.name18 = new SimpleStringProperty(name18);
        }

        public void setName19(String name19) {
            this.name19 = new SimpleStringProperty(name19);
        }

        public void setName2(String name2) {
            this.name2 = new SimpleStringProperty(name2);
        }

        public void setName20(String name20) {
            this.name20 = new SimpleStringProperty(name20);
        }

        public void setName3(String name3) {
            this.name3 = new SimpleStringProperty(name3);
        }

        public void setName4(String name4) {
            this.name4 = new SimpleStringProperty(name4);
        }

        public void setName5(String name5) {
            this.name5 = new SimpleStringProperty(name5);
        }

        public void setName6(String name6) {
            this.name6 = new SimpleStringProperty(name6);
        }

        public void setName7(String name7) {
            this.name7 = new SimpleStringProperty(name7);
        }

        public void setName8(String name8) {
            this.name8 = new SimpleStringProperty(name8);
        }

        public void setName9(String name9) {
            this.name9 = new SimpleStringProperty(name9);
        }

        /**
         * *********************************************************************
         */
        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj1(Object obj1) {
            this.obj1 = new SimpleObjectProperty(obj1);
        }
        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj10(Object obj10) {
            this.obj10 = new SimpleObjectProperty(obj10);
        }
        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj11(Object obj11) {
            this.obj11 = new SimpleObjectProperty(obj11);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj12(Object obj12) {
            this.obj12 = new SimpleObjectProperty(obj12);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj13(Object obj13) {
            this.obj13 = new SimpleObjectProperty(obj13);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj14(Object obj14) {
            this.obj14 = new SimpleObjectProperty(obj14);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj15(Object obj15) {
            this.obj15 = new SimpleObjectProperty(obj15);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj16(Object obj16) {
            this.obj16 = new SimpleObjectProperty(obj16);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj17(Object obj17) {
            this.obj17 = new SimpleObjectProperty(obj17);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj18(Object obj18) {
            this.obj18 = new SimpleObjectProperty(obj18);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj19(Object obj19) {
            this.obj19 = new SimpleObjectProperty(obj19);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj2(Object obj2) {
            this.obj2 = new SimpleObjectProperty(obj2);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj20(Object obj20) {
            this.obj20 = new SimpleObjectProperty(obj20);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj3(Object obj3) {
            this.obj3 = new SimpleObjectProperty(obj3);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj4(Object obj4) {
            this.obj4 = new SimpleObjectProperty(obj4);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj5(Object obj5) {
            this.obj5 = new SimpleObjectProperty(obj5);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj6(Object obj6) {
            this.obj6 = new SimpleObjectProperty(obj6);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj7(Object obj7) {
            this.obj7 = new SimpleObjectProperty(obj7);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj8(Object obj8) {
            this.obj8 = new SimpleObjectProperty(obj8);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public void setObj9(Object obj9) {
            this.obj9 = new SimpleObjectProperty(obj9);
        }

        @SuppressWarnings({"unchecked","fallthrough"})
        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public DoubleProperty getDouble1() {
            return double1;
        }

        public DoubleProperty getDouble10() {
            return double10;
        }

        public DoubleProperty getDouble11() {
            return double11;
        }

        public DoubleProperty getDouble12() {
            return double12;
        }

        public DoubleProperty getDouble13() {
            return double13;
        }

        public DoubleProperty getDouble14() {
            return double14;
        }

        public DoubleProperty getDouble15() {
            return double15;
        }

        public DoubleProperty getDouble16() {
            return double16;
        }

        public DoubleProperty getDouble17() {
            return double17;
        }

        public DoubleProperty getDouble18() {
            return double18;
        }

        public DoubleProperty getDouble19() {
            return double19;
        }

        public DoubleProperty getDouble2() {
            return double2;
        }

        public DoubleProperty getDouble20() {
            return double20;
        }

        public DoubleProperty getDouble3() {
            return double3;
        }

        public DoubleProperty getDouble4() {
            return double4;
        }

        public DoubleProperty getDouble5() {
            return double5;
        }

        public DoubleProperty getDouble6() {
            return double6;
        }

        public DoubleProperty getDouble7() {
            return double7;
        }

        public DoubleProperty getDouble8() {
            return double8;
        }

        public DoubleProperty getDouble9() {
            return double9;
        }

        public StringProperty getName1() {
            return name1;
        }

        public StringProperty getName10() {
            return name10;
        }

        public StringProperty getName11() {
            return name11;
        }

        public StringProperty getName12() {
            return name12;
        }

        public StringProperty getName13() {
            return name13;
        }

        public StringProperty getName14() {
            return name14;
        }

        public StringProperty getName15() {
            return name15;
        }

        public StringProperty getName16() {
            return name16;
        }

        public StringProperty getName17() {
            return name17;
        }

        public StringProperty getName18() {
            return name18;
        }

        public StringProperty getName19() {
            return name19;
        }

        public StringProperty getName2() {
            return name2;
        }

        public StringProperty getName20() {
            return name20;
        }

        public StringProperty getName3() {
            return name3;
        }

        public StringProperty getName4() {
            return name4;
        }

        public StringProperty getName5() {
            return name5;
        }

        public StringProperty getName6() {
            return name6;
        }

        public StringProperty getName7() {
            return name7;
        }

        public StringProperty getName8() {
            return name8;
        }

        public StringProperty getName9() {
            return name9;
        }

        public ObjectProperty getObj1() {
            return obj1;
        }

        public ObjectProperty getObj10() {
            return obj10;
        }

        public ObjectProperty getObj11() {
            return obj11;
        }

        public ObjectProperty getObj12() {
            return obj12;
        }

        public ObjectProperty getObj13() {
            return obj13;
        }

        public ObjectProperty getObj14() {
            return obj14;
        }

        public ObjectProperty getObj15() {
            return obj15;
        }

        public ObjectProperty getObj16() {
            return obj16;
        }

        public ObjectProperty getObj17() {
            return obj17;
        }

        public ObjectProperty getObj18() {
            return obj18;
        }

        public ObjectProperty getObj19() {
            return obj19;
        }

        public ObjectProperty getObj2() {
            return obj2;
        }

        public ObjectProperty getObj20() {
            return obj20;
        }

        public ObjectProperty getObj3() {
            return obj3;
        }

        public ObjectProperty getObj4() {
            return obj4;
        }

        public ObjectProperty getObj5() {
            return obj5;
        }

        public ObjectProperty getObj6() {
            return obj6;
        }

        public ObjectProperty getObj7() {
            return obj7;
        }

        public ObjectProperty getObj8() {
            return obj8;
        }

        public ObjectProperty getObj9() {
            return obj9;
        }

        public void setAnyObject(String parametName, Object parametValue) throws Exception {
            if (parametName.contains("name")) {
                this.getClass().getMethod("set" + parametName.substring(0, 1).toUpperCase() + parametName.substring(1), String.class).invoke(this, parametValue);
            } else if (parametName.contains("double")) {
                this.getClass().getMethod("set" + parametName.substring(0, 1).toUpperCase() + parametName.substring(1), Double.class).invoke(this, parametValue);
            } else if (parametName.contains("obj")) {
                this.getClass().getMethod("set" + parametName.substring(0, 1).toUpperCase() + parametName.substring(1), Object.class).invoke(this, parametValue);
            }
        }
        
    }
}
