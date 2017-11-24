/**
 * Created by sss on 2017/5/23.
 */

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.*;



public class CreateExcel {
    public static void main(String[] args) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet("sheet1");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        XSSFRow row= sheet.getRow(0);;
        // 第四步，创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style = wb.createCellStyle();


        try {
        XSSFCell cell = null;



            row = sheet.createRow(0);

            // 第四步，创建单元格，并设置值
            row.createCell(0);
            cell=row.getCell(0);
            cell.setCellValue("序号");
            row.createCell(1);
            cell=row.getCell(1);
            cell.setCellValue("情感");
            row.createCell(2);
            cell=row.getCell(2);
            cell.setCellValue("保留项1");
            row.createCell(3);
            cell=row.getCell(3);
            cell.setCellValue("保留项2");
            row.createCell(4);
            cell=row.getCell(4);
            cell.setCellValue("保留项3");
            row.createCell(5);
            cell=row.getCell(5);
            cell.setCellValue("保留项4");
            String data_info="数据项";
            for (int col=6;col<2950;col++)
            {
                int col_index=col-5;
                String col_info=data_info+col_index;

                row.createCell(col);
                cell=row.getCell(col);
                cell.setCellValue(col_info);

            }


            ClassLoader classloader =
                    org.apache.poi.poifs.filesystem.POIFSFileSystem.class.getClassLoader();
            URL res = classloader.getResource(
                    "org/apache/poi/poifs/filesystem/POIFSFileSystem.class");
            String path = res.getPath();
            System.out.println("POI Core came from " + path);

            classloader = org.apache.poi.POIXMLDocument.class.getClassLoader();
            res = classloader.getResource("org/apache/poi/POIXMLDocument.class");
            path = res.getPath();
            System.out.println("POI OOXML came from " + path);



        // 第六步，将文件存到指定位置
            File f=new File("/Library/Tomcat/bin/sampleData.xlsx");
            boolean isSuccess=false;
            if (!f.exists())
            {
                isSuccess=f.createNewFile();
            }
            System.out.println(isSuccess);
            FileOutputStream fout = new FileOutputStream(f);
            wb.write(fout);
            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
