/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.databaseModel;

/**
 *
 * @author hdwjy
 */
public class UserInfo {
    private String tableName="T_USER_INFO";
    
    private String pid;
    private String name;
    private String code;
    private String password;
    private String createTime;
    private String sysFlag;
    
    public String[][] getTableModelSet(){
        return new String[][]{
            {"tableName",tableName,"TableName"},
            {"pid","F_PID","mainKey"},
            {"name","F_USER_NAME","String"},
            {"code","F_USER_CODE","String"},
            {"password","F_USER_PASSWORD","String"},
            {"createTime","F_CREATETIME","String"},
            {"sysFlag","F_SYS_FLAG","String"}
        };
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    
    
}
