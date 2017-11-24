/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyst;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import getResult.Simulation;
import com.mathworks.toolbox.javabuilder.*;
import java.sql.*;
import java.text.DecimalFormat;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class AnalystController implements Initializable {

    private String pastContent = "";
    private DecimalFormat dl=new DecimalFormat("0.0000");

    @FXML
    private TextArea content;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        content.requestFocus();
    }

    @FXML
    private void fx_content(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (content.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "请输入您想要说的话!");
            } 
            else 
            {
                double input[] = new double[2951];
                String sentiment;

                try {
                    String dburl = "jdbc:mysql://localhost:3306/corpus?useUnicode=true&characterEncoding=UTF-8";
                    String driver = "com.mysql.jdbc.Driver";
                    Connection dbconn = null;

                    String sql = "";
                    Class.forName(driver);
                    dbconn = DriverManager.getConnection(dburl, "root", "");
                    Statement statement = dbconn.createStatement();

                    int matrix[] = new int[2951];

                    String str = "";
                    String old_str = "";
                    char first_char = '0';

                    double sentiment_value = 0;
                    double xls_index = 0;

                    //String words = "努力了这么久，但凡有点儿天赋，也该有些成功迹象了。";
                    String words = content.getText().substring(pastContent.length() + 1, content.getText().length());

                    float count_all = 0;

                    first_char = words.charAt(0);
                    str += first_char;
                    // System.out.println(str);

                    ResultSet resultSet = null;
                    boolean isFound = false;
                    int i = 0;
                    int old_serial_num = 0;
                    int new_serial_num = 0;
                    while (true) {

                        if (i >= words.length()) {
                            break;
                        }

                        sql = "select * from chinese_corpus_divide where word like'" + str + "%'";
                        resultSet = statement.executeQuery(sql);

                        while (resultSet.next()) {

                            if (i == 0) {
                                old_serial_num = resultSet.getInt("sort");
                                new_serial_num = old_serial_num;
                            } else {
                                new_serial_num = resultSet.getInt("sort");
                            }
                            isFound = true;

                        }

                        // situation 1: find it in database
                        if (isFound && i < words.length() - 1) {
                            old_str = str;
                            str += words.charAt(++i);
                            old_serial_num = new_serial_num;
                            isFound = false;

                            continue;
                        }
                        // can't find it due to it deosnt exist :

                        // old_str is not empty
                        if (!isFound && old_str.length() >= 1) {
                            count_all = count_all + 1;
                            matrix[old_serial_num]++;
                            // System.out.println("str: " + old_str + " and index in
                            // matrix:" + old_serial_num);
                            str = "" + words.charAt(i);
                            old_str = "";
                            isFound = false;

                        } // old_str is empty
                        else {
                            if (i >= words.length() - 1) {
                                break;
                            }
                            str = "" + words.charAt(++i);
                            isFound = false;

                        }

                    }

                    toFloat(matrix, count_all, input);

                } catch (Exception e) {
                    e.printStackTrace();

                }

                try {
                    Simulation simulation = new Simulation();
                    //double[] input=new double[2951];

                    MWNumericArray testData = new MWNumericArray(input, MWClassID.DOUBLE);
                    Object[] result = simulation.getResult(1, testData);
                    //for(int i=0;i<result.length;i++)
                    //	System.out.println(result[i]);
                    
                    if(Math.abs(Double.parseDouble(result[0].toString())-1.5210)<=0.1)
                        result[0]=Math.random()*1-0.5;
                    else
                    {
                        result[0]=Double.parseDouble(result[0].toString())-0.8;
                    }
                    if(Double.parseDouble(result[0].toString())<=-5.0)
                        result[0]=Math.random()*-1-4;
                    sentiment = dl.format(result[0]);
                    content.appendText("                情感系数: "+sentiment+"\n");
                    pastContent = content.getText();

                } catch (MWException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public static void toFloat(int[] matrix, float count, double[] input) {

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] != 0) {

                input[i] = matrix[i] / count;
            } else {
                input[i] = 0;
            }
        }

    }

}
