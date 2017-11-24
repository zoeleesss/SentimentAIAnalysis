/**
 * Created by sss on 2017/5/22.
 */
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.*;
import java.util.*;

public class InsertCorpusToSQL {

    public static void main(String []args){


        try {

            FileInputStream file = new FileInputStream("/Library/正面情感词语（中文）.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(file, "gbk"));

            Map<String,String> check=new HashMap<String,String>();
            String dburl = "jdbc:mysql://localhost:3306/corpus?useUnicode=true&characterEncoding=UTF-8";
            String driver = "com.mysql.jdbc.Driver";
            Connection dbconn = null;


            String sql = "";
            Class.forName(driver);
            dbconn = DriverManager.getConnection(dburl, "root", "root");
            Statement statement = dbconn.createStatement();


            String strLine = null;
            char first_char = '0';

            int sort = 1;

            while ((strLine = br.readLine()) != null) {

                //statement.executeUpdate(sql);
                char t_first_char = strLine.charAt(0);
                if (t_first_char != first_char) {
                    sort++;
                    first_char = t_first_char;
                }


                sql = "insert into chinese_corpus_divide(sort,word,sentiment) " +
                        "values('" + sort + "','" + strLine + "','" + "0" + "')";

                if (!check.containsKey(strLine)) {statement.executeUpdate(sql);check.put(strLine,"1");}
                System.out.println(sql);
                //System.out.println(strLine);

            }


            FileInputStream file2 = new FileInputStream("/Library/负面情感词语（中文）.txt");
            BufferedReader br2 = new BufferedReader(new InputStreamReader(file2, "gbk"));


            while ((strLine = br2.readLine()) != null) {

                //statement.executeUpdate(sql);
                char t_first_char = strLine.charAt(0);
                if (t_first_char != first_char) {
                    sort++;
                    first_char = t_first_char;
                }


                sql = "insert into chinese_corpus_divide(sort,word,sentiment) " +
                        "values('" + sort + "','" + strLine + "','" + "-1" + "')";

                System.out.println(sql);
                //System.out.println(strLine);
                if (!check.containsKey(strLine)) {statement.executeUpdate(sql);check.put(strLine,"1");}

            }



        }
        catch (Exception e){
            e.printStackTrace();

        }




    }

}
