/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 一切新增、删除、修改、查询的方法都存在这里
 *
 * @author hdwjy
 */
public class DataBaseBean extends DataBaseSet {

    String table_name;

    /**
     * 简单查询结构(obj)所属的所有数据
     *
     * @param obj
     * @param sql
     * @return
     * @throws SQLException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List selectObject(Class tableClass, String sql) throws SQLException, Exception {
        connect();
        Class newObj = Class.forName(tableClass.getName());
        Constructor ct = newObj.getConstructor();
        Object obj = ct.newInstance();

        String tableName = getTableName(obj);
        ResultSet rs = this.getStat().executeQuery("select t.* from " + tableName + " as t " + (sql==null?"":sql));
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
     * 通过简单的命令（sql）来过滤list中符合条件（obj）的内容 flag为0时采用解析SQL，为1时采用标准sql
     *
     * @param list
     * @param sql
     * @param obj
     * @return
     * @throws Exception
     */
    public List<Object[]> selectObject(String sql) throws Exception {
        connect();
        List<Object[]> retList = new ArrayList<>();
        ResultSet rs = this.getStat().executeQuery(sql);
        int colmax = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            Object[] obj = new Object[colmax];
            for (int col = 1; col <= colmax; col++) {
                obj[col-1] = rs.getObject(col);
            }
            retList.add(obj);
        }
        close();
        return retList;
    }

    /**
     * 保存方法，如果对象的mainKey有值，则计算为修改，否则计算为新增
     *
     * @param obj
     * @throws Exception
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public void save(Object obj) throws Exception {
        connect();
        String keyName = null;//设置主键名
        String keyValue = null;//设置主键值

        String tableName = getTableName(obj);

        Object newTableModel = getAnyTableModel(obj);

        String[][] getParamets = getTableAllColnum(newTableModel);
        String inster = null;
        String values = null;
        for (String[] para : getParamets) {
            if (!para[0].equals("tableName") && !para[2].equals("mainKey")) {
                if (inster == null) {
                    inster = para[1];
                    values = "?";
                } else {
                    inster = inster + "," + para[1];
                    values = values + ",?";
                }
            }
            if (para[2].equals("mainKey")) {
                Object getValue = this.getNameValue(obj, para[0]);
                if (getValue != null) {
                    keyName = para[1];
                    keyValue = getValue.toString();
                }
            }
        }

        //如果找不到主键则插入
        if (keyValue == null) {
            System.out.println("insert into " + tableName + "(" + inster + ")" + " values (" + values + ");");
            PreparedStatement prep = this.getConn().prepareStatement(
                    "insert into " + tableName + "(" + inster + ")" + " values (" + values + ");");

            int col = 1;
            for (String[] para : getParamets) {
                if (para[2].equals("String")) {
                    Object getValue = this.getNameValue(obj, para[0]);
                    if (getValue != null) {
                        prep.setString(col, getValue.toString());
                    }
                    col++;
                } else if (para[2].equals("Double")) {
                    Object getValue = this.getNameValue(obj, para[0]);
                    if (getValue != null) {
                        prep.setDouble(col, Double.parseDouble(getValue.toString()));
                    }
                    col++;
                }
            }
            prep.addBatch();
            this.getConn().setAutoCommit(false);
            prep.executeBatch();
            this.getConn().setAutoCommit(true);
        } else {
            //拥有主键则更新
            List getList = this.selectObject(obj.getClass(), " where " + keyName + "=" + keyValue);
            Object oldObj = getList.get(0);

            List<Object[]> list = new ArrayList<Object[]>();
            for (String[] para : getParamets) {
                Object oldValue = this.getNameValue(oldObj, para[0]);
                if (oldValue == null) {
                    oldValue = "";
                }
                Object newValue = this.getNameValue(obj, para[0]);
                if (newValue == null) {
                    newValue = "";
                }
                if (!oldValue.equals(newValue)) {
                    Object[] objectValue = new Object[]{
                        para[1],
                        para[2],
                        newValue
                    };
                    list.add(objectValue);
                }
            }
            if (list.isEmpty()) {
                System.out.println("没有修改，放弃保存");
                return;
            }
            try {
                String updateStringBefore = "update " + tableName + " set ";
                String updateStringLast = null;
                if (list.size() > 0) {
                    for (Object[] getObjectValues : list) {
                        if (updateStringLast == null) {
                            updateStringLast = getObjectValues[0].toString() + " = " + getUpdate(getObjectValues[2], getObjectValues[1].toString());
                        } else {
                            updateStringLast = updateStringLast + " , " + getObjectValues[0].toString() + " = " + getUpdate(getObjectValues[2], getObjectValues[1].toString());
                        }
                    }
                }
                String updateStringEnd = " where " + keyName + "=" + keyValue;
                close();//关闭连接准备好储存
                connect();//重新打开连接
                System.out.println(updateStringBefore + updateStringLast + updateStringEnd);
                this.getConn().setAutoCommit(false);
                this.getStat().executeUpdate(updateStringBefore + updateStringLast + updateStringEnd);
                this.getStat().executeBatch();
                this.getConn().setAutoCommit(true);
            } catch (Exception e) {
                System.out.println("保存失败!");
                e.printStackTrace();
            }
        }
        close();
    }

    /**
     * 修改时的属性值的判断
     *
     * @param paramets
     * @param paramet
     * @return
     */
    private Object getUpdate(Object paramets, String paramet) {
        if (paramet.equals("String")) {
            return "'" + paramets + "'";
        } else {
            return paramets;
        }
    }

    /**
     * 删除表（obj）通过obj中设置的mainKey值
     *
     * @param obj
     * @return
     */
    public void delete(Object obj) throws Exception {
        String tableName = getTableName(obj);
        String keyName = null;
        String keyValue = null;
        String[][] getParamets = getTableAllColnum(obj);
        String inster = null;
        String values = null;
        for (String[] para : getParamets) {
            if (!para[0].equals("tableName") && !para[2].equals("mainKey")) {
                if (inster == null) {
                    inster = para[1];
                    values = "?";
                } else {
                    inster = inster + "," + para[1];
                    values = values + ",?";
                }
            }
            if (para[2].equals("mainKey")) {
                Object getValue = this.getNameValue(obj, para[0]);
                if (getValue != null) {
                    keyName = para[1];
                    keyValue = getValue.toString();
                }
            }
        }
        List list = this.selectObject(obj.getClass(), " where t." + keyName + " = " + keyValue);
        if (list.isEmpty()) {
            System.out.println("要删除的数据不存在");
            return;
        } else {
            String sql="delete from "+tableName+" where "+keyName+" = "+keyValue;
            System.out.println(sql);
            connect();
            this.getConn().setAutoCommit(false);
            this.getStat().executeUpdate(sql);
            this.getStat().executeBatch();
            this.getConn().setAutoCommit(true);
            close();
        }
        System.out.println("删除成功");
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
    public String[][] getTableAllColnum(Object obj) throws Exception {
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
                            try{
                                paramets[i] = nameValue[i].toString();
                            }catch(Exception e){
                                paramets[i] = null;
                            }
                        } else if (classType.isInstance(Integer.TYPE)) {
                            try{
                                paramets[i] = Integer.parseInt(nameValue[i].toString());
                            }catch(Exception e){
                                paramets[i] = null;
                            }
                        } else if (classType.isInstance(Double.TYPE)) {
                            try{
                                paramets[i] = Double.parseDouble(nameValue[i].toString());
                            }catch(Exception e){
                                paramets[i] = null;
                            }
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
}
