package com.isoftstone.myprovider.esproc;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;

@Controller
public class QueryTxtFile {

    @ResponseBody
    @RequestMapping("/txt")
    public  String runSPL() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement st;
        ResultSet set;
        //建立连接
        Class.forName("com.esproc.jdbc.InternalDriver");
        con = DriverManager.getConnection("jdbc:esproc:local://");
        //直接执行SPL语句，返回结果集
        st = (PreparedStatement) con.createStatement();
        ResultSet rs= (ResultSet) st.executeQuery("$()select * from Employees.txt");
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
        StringBuilder builder = new StringBuilder();
        while (rs.next()) {
            for (int c = 1; c <= colCount; c++) {
                if (c > 1) {
                    System.out.print("\t");
                } else {
                    System.out.print("\n");
                }
                Object o = rs.getObject(c);
                System.out.print(o.toString());
                builder.append(o.toString());
            }
        }
        //关闭连接
        if (con != null) {
            con.close();
        }
        return builder.toString();
    }




        public static void main(String[] args) throws SQLException, ClassNotFoundException {
        QueryTxtFile test = new QueryTxtFile();
        test.runSPL();
    }
}
