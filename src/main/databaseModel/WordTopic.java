/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.databaseModel;

/**
 *
 * @author hdwjy
 */
public class WordTopic {
    private String tableName="T_WORD_TOPIC";
    
    private String pid;
    private String contect;
    private String answer;//答案的SEQ
    private String title; //词汇的SEQ
    private String type;
    private String attention;
    private String accept;
    private String createTime;
    private String sysFlag;
    
    public String[][] getTableModelSet(){
        return new String[][]{
            {"tableName",tableName,"TableName"},
            {"pid","F_PID","mainKey"},
            {"contect","F_CONTECT","String"},
            {"answer","F_TRANS","String"},
            {"title","F_EXAMPLE","String"},
            {"type","F_TYPE","String"},
            {"attention","F_ATTENTION","String"},
            {"accept","F_ACCEPT","String"},
            {"createTime","F_CREATTIME","String"},
            {"sysFlag","F_SYS_FLAG","String"}
        };
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
