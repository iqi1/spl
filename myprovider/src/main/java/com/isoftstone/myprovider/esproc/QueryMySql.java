package com.isoftstone.myprovider.esproc;

import java.sql.*;

public class QueryMySql {

    public  void runSPL() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement st;
        ResultSet set;
        //建立连接
        Class.forName("com.esproc.jdbc.InternalDriver");
        con = DriverManager.getConnection("jdbc:esproc:local://");
        //直接执行SPL语句，返回结果集
        PreparedStatement pst = con.prepareStatement("$(my_company)select * from employees");
        //设置参数
        //获取结果集
        ResultSet rs = (ResultSet) pst.executeQuery();
        //简单处理结果集，将结果集中的字段名与数据输出
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        for (int c = 1; c <= colCount; c++) {
            String title = rsmd.getColumnName(c);
            if (c > 1) {
                System.out.print("\t");
            } else {
                System.out.print("\n");
            }
            System.out.print(title);
        }
        while (rs.next()) {
            for (int c = 1; c <= colCount; c++) {
                if (c > 1) {
                    System.out.print("\t");
                } else {
                    System.out.print("\n");
                }
                Object o = rs.getObject(c);
                System.out.print(o.toString());
            }
        }
        //关闭连接
        if (con != null) {
            con.close();
        }
    }




    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        QueryMySql test = new QueryMySql();
        test.runSPL();
    }

}
