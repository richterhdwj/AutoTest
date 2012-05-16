/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.databaseModel;

/**
 *
 * @author hdwjy
 */
public class WordTopicAnswer {
    private String tableName="T_WORD_TOPIC_ANSWER";
    
    private String pid;
    private String parentId;
    private String answer; //答案内容
    private String istrue; //是否为正确答案
    private String type;
    private String createTime;
    private String sysFlag;
    
    public String[][] getTableModelSet(){
        return new String[][]{
            {"tableName",tableName,"TableName"},
            {"pid","F_PID","mainKey"},
            {"parentId","F_PARENT_ID","String"},
            {"answer","F_ANSWER","String"},
            {"istrue","F_ISTRUE","String"},
            {"type","F_TYPE","String"},
            {"createTime","F_CREATTIME","String"},
            {"sysFlag","F_SYS_FLAG","String"}
        };
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIstrue() {
        return istrue;
    }

    public void setIstrue(String istrue) {
        this.istrue = istrue;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
