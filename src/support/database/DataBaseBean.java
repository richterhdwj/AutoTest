/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import support.DataBaseSet;
import support.database.databaseModel.UserInfo;

/**
 * 一切新增、删除、修改、查询的方法都存在这里
 *
 * @author hdwjy
 */
public class DataBaseBean extends DataBaseSet {

    String table_name;

    /**
     * sql是以where语句为主，切记要注意这不但是sqllite,也没有什么函数可以用，一切用简单方式（order by 应该还是可以用的）
     *
     * @param obj
     * @param sql
     * @return
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List selectObject(Object obj) throws SQLException, Exception {
        connect();
        String tableName = getTableName(obj);
        ResultSet rs = this.getStat().executeQuery("select * from " + tableName);
        List list = new ArrayList();
        while (rs.next()) {
            Object newTableModel = getAnyTableModel(obj);
            String[][] getParamets = getTableAllColnum(newTableModel);
            for (String[] valueParamet : getParamets) {
                if (!valueParamet[0].equals("tableName")) {
                    this.setNameValue(newTableModel, valueParamet[0], new Object[]{rs.getObject(valueParamet[1])});
                }
            }
            list.add(newTableModel);
        }
        close();
        return list;
    }
    
    /**
     * 通过简单的命令（sql）来过滤list中符合条件（obj）的内容
     * @param list
     * @param sql
     * @param obj
     * @return
     * @throws Exception 
     */
    public List checkObject(List list,String sql,Object obj) throws Exception{
        //TODO:
        return list;
    }

    /**
     * 根据一个结构获取一个新结构
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Object getAnyTableModel(Object obj) throws Exception {
        Class newObj = Class.forName(obj.getClass().getName());
        Constructor ct = newObj.getConstructor();
        Object ret = ct.newInstance();
        return ret;
    }

    /**
     * 获取结构中赋予的表名
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private String getTableName(Object obj) throws Exception {
        table_name = null;
        try {
            table_name = (String) getNameValue(obj, "tableName");
        } catch (Exception e) {
            System.out.println("名字获取失败" + e.getMessage());
        }

        return table_name;
    }

    /**
     * 获取结构中赋予的表值（包括内部设置的表名，表字段名和表字段类型）
     *
     * @param obj
     * @return
     * @throws Exception
     */
    private String[][] getTableAllColnum(Object obj) throws Exception {
        String[][] tableSet = null;
        try {
            tableSet = (String[][]) getNameValue(obj, "TableModelSet");
        } catch (Exception e) {
            System.out.println("名字获取失败" + e.getMessage());
        }

        return tableSet;
    }

    /**
     * 传入的getName为model结构中的定义名，只要有方法就可以取出值
     *
     * @param obj
     * @param getName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Object getNameValue(Object obj, String getName) throws Exception {
        try {
            String namePath = "get" + getName.substring(0, 1).toUpperCase() + getName.substring(1);

            Class paramets[] = new Class[0];
            Method getNameMethod = obj.getClass().getMethod(namePath, paramets);

            Object getRet = getNameMethod.invoke(obj);
            return getRet;
        } catch (Exception e) {
            System.out.println(obj.getClass().getName() + "类中没有get"
                    + getName.substring(0, 1).toUpperCase() + getName.substring(1)
                    + "的方法");
            return null;
        }
    }

    /**
     * 对相应表（obj）的相应的字段（mothodname）进行插入值（nameValue）的方法
     *
     * @param obj
     * @param mothodName
     * @param nameValue
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void setNameValue(Object obj, String mothodName, Object[] nameValue) throws Exception {
        try {
            String namePath = "set" + mothodName.substring(0, 1).toUpperCase() + mothodName.substring(1);

            Method[] allMethod = obj.getClass().getMethods();

            boolean noMethod = true;

            for (Method method : allMethod) {
                if (namePath.equals(method.getName())) {
                    Object[] paramets = new Object[method.getParameterTypes().length];
                    int i = 0;
                    for (Class classType : method.getParameterTypes()) {
                        if (classType.isInstance(new String())) {
                            paramets[i] = nameValue[i].toString();
                        } else if (classType.isInstance(Integer.TYPE)) {
                            paramets[i] = Integer.parseInt(nameValue[i].toString());
                        } else if (classType.isInstance(Double.TYPE)) {
                            paramets[i] = Double.parseDouble(nameValue[i].toString());
                        }
                    }
                    noMethod = false;
                    method.invoke(obj, paramets);
                }
            }

            if (noMethod) {
                throw new Exception();
            }

        } catch (Exception e) {
            System.out.println(obj.getClass().getName() + "类中没有set"
                    + mothodName.substring(0, 1).toUpperCase() + mothodName.substring(1)
                    + "的方法");
        }
    }

//    @SuppressWarnings("unchecked")
    public static void main(String[] arg0) throws Exception {
        DataBaseBean dataBaseBean = new DataBaseBean();
        UserInfo userInfo = new UserInfo();
        List list = dataBaseBean.selectObject(userInfo);
        for (Object get : list) {
            UserInfo getUser=(UserInfo)get;
            System.out.println(getUser.getPid());
            System.out.println(getUser.getName());
            System.out.println(getUser.getCode());
            System.out.println(getUser.getPassword());
            System.out.println(getUser.getSysFlag());
            System.out.println(getUser.getCreateTime());
            System.out.println(getUser.getTableName());
        }
    }
}
