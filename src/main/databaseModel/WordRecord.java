/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.databaseModel;

import support.ModelBase;

/**
 *
 * @author hdwjy
 */
public class WordRecord {
    private String tableName="T_WORD_RECORD";
    
    private String pid;
    private String type;
    private String contect;
    private String trans;
    private String example;
    private String createTime;
    private String sysFlag;
    
    public String[][] getTableModelSet(){
        return new String[][]{
            {"tableName",tableName,"TableName"},
            {"pid","F_PID","mainKey"},
            {"type","F_TYPE","String"},
            {"contect","F_CONTECT","String"},
            {"trans","F_TRANS","String"},
            {"example","F_EXAMPLE","String"},
            {"createTime","F_CREATTIME","String"},
            {"sysFlag","F_SYS_FLAG","String"}
        };
    }

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSysFlag() {
        return sysFlag;
    }

    public void setSysFlag(String sysFlag) {
        this.sysFlag = sysFlag;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
