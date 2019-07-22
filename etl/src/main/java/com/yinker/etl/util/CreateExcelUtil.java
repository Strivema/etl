package com.yinker.etl.util;

import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <b>类描述:</b>HrEmployeeAttendance表对应的业务处理类。
 * <b>作者:</b>李玉强
 * <b>创建日期:</b>2016-08-31 10:24:05
 * </pre>
 */
public class CreateExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateExcelUtil.class);


    /**
     * 创建PDF
     *
     * @param fileLocation excel 生成路径
     * @param attendance
     * @throws Exception
     */
    public static void createExcel (String fileLocation, List<Map<String, Object>> attendance, String FileName, List<String> headName, Map<String, String> keyvalue) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(FileName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);

        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();
        // 居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        int i = 0;
        int l = headName.size();
        for(i = 0; i < l; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(headName.get(i));
            cell.setCellStyle(style);
        }

        for(int j = 0; j < attendance.size(); j++) {
            row = sheet.createRow((int) j + 1);
            Map<String, Object> bean = (Map<String, Object>) attendance.get(j);
            // 第四步，创建单元格，并设置值  
            int m = 0;
            for(String name : headName) {
                Object object = bean.get(keyvalue.get(name));
                if (object != null && ((object instanceof Double) || (object instanceof Float) ||
                        (object instanceof Integer) || (object instanceof String)
                        || (object instanceof Long) || (object instanceof BigDecimal))) {
                    row.createCell(m++).setCellValue(object.toString());
                } else if (object != null && object instanceof Date) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String datestr = format.format((Date) object);
                    row.createCell(m++).setCellValue(datestr);
                } else if (object != null) {
                    row.createCell(m++).setCellValue("未支持的类型：" + object.getClass());
                }
            }
        }

        if (attendance != null && attendance.size() > 0) {
            for(int j = 0; j < attendance.get(0).size(); j++) {
                sheet.setColumnWidth(j, 35 * 100);
            }
        }


        // 第六步，将文件存到指定位置  
        try {
            LOGGER.debug("文件路径为 ：" + fileLocation);
            File file = new File(fileLocation);
            if (!file.exists()) {
                LOGGER.debug("文件不存在，新创建文件！");
                file.createNewFile();
            }
            FileOutputStream fout = new FileOutputStream(fileLocation);
            wb.write(fout);
            fout.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
