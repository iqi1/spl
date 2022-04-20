package com.isoftstone.myprovider.esproc;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.esproc.jdbc.ResultSet;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
@RestController
public class CommonMethod {

    @RequestMapping(path = "/callSPLCommonJson", method= RequestMethod.POST)
    public String callSPLCommonJson(@RequestParam("dfxName") String dfxName, @RequestParam("params") String params) throws ClassNotFoundException, SQLException {
        System.out.println("dfxName->"+dfxName);
        System.out.println("params->"+params);
        Connection con = null;
        java.sql.PreparedStatement st;
        int colCount = 0;
        int i = 0;
        ResultSet rs;
        String param = "";
        String resultStr;
        LinkedHashMap<String, String> jsonMap = null;
        String jsonResult=null;

        Class.forName("com.esproc.jdbc.InternalDriver");
        con = DriverManager.getConnection("jdbc:esproc:local://");
        if(null==params || "".equals(params)){
            st = con.prepareCall("call " + dfxName + "()");
        }else{
            jsonMap= JSONObject.parseObject(params, new TypeReference<LinkedHashMap<String, String>>(){});
            for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
                param += "?,";
            }
            if (param != null && !"".equals(param)) {
                int len = param.length();
                param = param.substring(0, len - 1);
            }
            //call dfxname(?,?,?)
            st = con.prepareCall("call " + dfxName + "(" + param + ")");

            for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
                i++;
                st.setObject(i, entry.getValue());
            }
        }
        st.execute();
        rs = (ResultSet) st.getResultSet();
        if (rs.next()){
            jsonResult = rs.getObject(1).toString();
        }
        if (con != null) {
            con.close();
        }
        return jsonResult;
    }


    @RequestMapping(value="callSPL/{code}", method= RequestMethod.GET)
    public String callSPL(@PathVariable("code") String code) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement st;
        java.sql.ResultSet rs;
        String jsonResult = null;
        Class.forName("com.esproc.jdbc.InternalDriver");
        con = DriverManager.getConnection("jdbc:esproc:local://");
        System.out.println("code:"+code);
        //call dfxname(?,?,?)
        st = con.prepareCall("call city(?)");
        st.setObject(1, code);
        st.execute();
        rs = st.getResultSet();
        if (rs.next()){
            jsonResult = rs.getObject(1).toString();
        }

        if (con != null) {
            con.close();
        }
        return jsonResult;
    }



    @RequestMapping("/callSPL2/{dfxName}")
    public String callSPL2(@PathVariable(value = "dfxName",required = false)  String dfxName) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement st;
        java.sql.ResultSet rs;
        String jsonResult = null;

        Class.forName("com.esproc.jdbc.InternalDriver");
        con = DriverManager.getConnection("jdbc:esproc:local://");
        st = con.prepareCall("call "+dfxName+"()");	//将dfx名字作为参数
        st.execute();
        rs = st.getResultSet();

        StringBuilder sb = new StringBuilder();
        if (rs.next()){
            jsonResult = rs.getObject(2).toString();
            sb.append(rs.getObject(1).toString()+",");
            sb.append(rs.getObject(2).toString()+",");
            sb.append(rs.getObject(3).toString()+",");
            sb.append(rs.getObject(4).toString()+",");
            sb.append(rs.getObject(5).toString());
        }

        if (con != null) {
            con.close();
        }
//        return jsonResult;
        return sb.toString();
    }
}
