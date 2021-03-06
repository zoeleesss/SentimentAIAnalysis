

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by sss on 2017/5/22.
 */
public class ProcessPS
{

    public static void print(int[] matrix)
    {

	for (int i = 0; i < matrix.length; i++)
	{
	    if (matrix[i] != 0)
	    {
		System.out.println("not 0: " + i + "num is: " + matrix[i]);
	    }
	}
    }

    public static void print_float(float[] matrix)
    {

	for (int i = 0; i < matrix.length; i++)
	{
	    if (matrix[i] != 0)
	    {
		System.out.println("not 0: " + i + "num is: " + matrix[i]);
	    }
	}

    }

    public static void toFloat(int[] matrix, float count, float[] float_matrix)
    {

	for (int i = 0; i < matrix.length; i++)
	{
	    if (matrix[i] != 0)
	    {

		float_matrix[i] = matrix[i] / count;
	    } else
	    {
		float_matrix[i] = 0;
	    }
	}

    }

    public static void addToExcel(float[] matrix, String file, double xls_index, double sentiment)
    {
	try
	{

	    InputStream is = new FileInputStream(file);
	    XSSFWorkbook wb = new XSSFWorkbook(is);
	    // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	    XSSFSheet sheet1 = wb.getSheetAt(0);
	    // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	    int x_row = sheet1.getLastRowNum();
	    XSSFRow row1 = sheet1.getRow(0);
	    int x_col = row1.getPhysicalNumberOfCells();
	    sheet1.createRow(x_row + 1);
	    row1 = sheet1.getRow(x_row + 1);
	    // 第四步，创建单元格，并设置值表头 设置表头居中
	    XSSFCellStyle style1 = wb.createCellStyle();
	    XSSFCell cell1 = null;

	    row1.createCell(0);
	    row1.createCell(1);
	    row1.createCell(2);
	    row1.createCell(3);
	    row1.createCell(4);
	    row1.createCell(5);
	    cell1 = row1.getCell(0);
	    cell1.setCellValue(xls_index);
	    cell1 = row1.getCell(1);
	    cell1.setCellValue(sentiment);
	    for (int i = 0; i <= x_col; i++)
	    {
		row1.createCell(i + 6);
		cell1 = row1.getCell(i + 6);
		if (i <= matrix.length - 1)
		    cell1.setCellValue(matrix[i]);
		else
		    cell1.setCellValue(0);
	    }

	    FileOutputStream fout = new FileOutputStream(file);
	    wb.write(fout);
	    fout.flush();
	    fout.close();

	} catch (Exception e)
	{
	    e.printStackTrace();
	}

    }

    public static void main(String[] args)
    {

	try
	{
	    String dburl = "jdbc:mysql://localhost:3306/corpus?useUnicode=true&characterEncoding=UTF-8";
	    String driver = "com.mysql.jdbc.Driver";
	    Connection dbconn = null;

	    String sql = "";
	    Class.forName(driver);
	    dbconn = DriverManager.getConnection(dburl, "root", "");
	    Statement statement = dbconn.createStatement();

	    int matrix[] = new int[2943];
	    float float_matrix[] = new float[2943];

	    String str = "";
	    String old_str = "";
	    char first_char = '0';

	    double sentiment_value = 0;
	    double xls_index = 0;

	    String words = "努力了这么久，但凡有点儿天赋，也该有些成功迹象了。";
	    float count_all = 0;

	    first_char = words.charAt(0);
	    str += first_char;
	    // System.out.println(str);

	    ResultSet resultSet = null;
	    boolean isFound = false;
	    int i = 0;
	    int old_serial_num = 0;
	    int new_serial_num = 0;
	    while (true)
	    {

		if (i >= words.length())
		    break;

		// System.out.println("curr_str: " + str + " old_str: " +
		// old_str);

		sql = "select * from chinese_corpus_divide where word like'" + str + "%'";
		resultSet = statement.executeQuery(sql);

		while (resultSet.next())
		{

		    if (i == 0)
		    {
			old_serial_num = resultSet.getInt("sort");
			new_serial_num = old_serial_num;
		    } else
			new_serial_num = resultSet.getInt("sort");
		    isFound = true;

		}

		// situation 1: find it in database
		if (isFound && i < words.length() - 1)
		{
		    old_str = str;
		    str += words.charAt(++i);
		    old_serial_num = new_serial_num;
		    isFound = false;

		    continue;
		}
		// can't find it due to it deosnt exist :

		// old_str is not empty
		if (!isFound && old_str.length() >= 1)
		{
		    count_all = count_all + 1;
		    matrix[old_serial_num]++;
		    // System.out.println("str: " + old_str + " and index in
		    // matrix:" + old_serial_num);
		    str = "" + words.charAt(i);
		    old_str = "";
		    isFound = false;

		}
		// old_str is empty
		else
		{
		    if (i >= words.length() - 1)
			break;
		    str = "" + words.charAt(++i);
		    isFound = false;

		}

	    }
	    // print(matrix);
	    // System.out.println("count all :" + count_all);
	    toFloat(matrix, count_all, float_matrix);
	    System.out.println("----------------");
	    // print_float(float_matrix);
	    System.out.println("----------------");
	    System.out.println("index " + xls_index);
	    addToExcel(float_matrix, "C:/Users/Administrator/Desktop/sampleData.xlsx", xls_index,
		    sentiment_value);

	} catch (Exception e)
	{
	    e.printStackTrace();

	}

    }
}
