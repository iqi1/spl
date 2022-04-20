package com.isoftstone.myprovider.esproc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import static com.sun.webkit.network.URLs.newURL;

@Controller
public class HttpDfx {

    @ResponseBody
    @RequestMapping("/http")
    public String runSPL() {
        try {

            URL url = newURL("http://127.0.0.1:8503/emp.dfx"); //传入URL串

            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();

            urlcon.connect(); //获取连接

            InputStream is = urlcon.getInputStream();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

            StringBuffer bs = new StringBuffer();

            String l = null;

            while ((l = buffer.readLine()) != null) {

                bs.append(l).append("\n");

            }

            System.out.println(bs.toString());
            return bs.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        HttpDfx test = new HttpDfx();
        test.runSPL();
    }

}
