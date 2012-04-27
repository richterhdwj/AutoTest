/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.tableView;

import javafx.scene.Node;

/**
 *
 * @author Richter
 */
public class TableViewColumnSet {
    public static String checkbox="checkbox";
    
    private String typeName;
    private String colName;
    private Double colWidth;
    private Object value;
    private boolean isRead=true;
    private boolean isWrite=false;
    private Node valueType;
    private Object columnModel;

    public TableViewColumnSet(String typeName, String colName, Double colWidth, Node valueType,Object value) {
        this.typeName = typeName;
        this.colName = colName;
        this.colWidth = colWidth;
        this.valueType = valueType;
        this.value=value;
    }
    
    
    public TableViewColumnSet(String typeName, String colName, Double colWidth, Node valueType,Object value,Boolean isRead,Boolean isWrite) {
        this.typeName = typeName;
        this.colName = colName;
        this.colWidth = colWidth;
        this.valueType = valueType;
        this.value=value;
        if(isRead==null){
            this.isRead=true;
        }else{
            this.isRead=isRead;
        }
        
        if(isWrite==null){
            this.isWrite=false;
        }else{
            this.isWrite=isWrite;
        }
    }
    
    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Double getColWidth() {
        return colWidth;
    }

    public void setColWidth(Double colWidth) {
        this.colWidth = colWidth;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Node getValueType() {
        return valueType;
    }

    public void setValueType(Node valueType) {
        this.valueType = valueType;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isIsWrite() {
        return isWrite;
    }

    public void setIsWrite(boolean isWrite) {
        this.isWrite = isWrite;
    }

    public Object getColumnModel() {
        return columnModel;
    }

    public void setColumnModel(Object columnModel) {
        this.columnModel = columnModel;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
