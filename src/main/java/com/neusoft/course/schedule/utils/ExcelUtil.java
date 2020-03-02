package com.neusoft.course.schedule.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.course.schedule.constants.ServiceConstants;
import com.neusoft.course.schedule.dto.UploadExcelImportCourseDTO;
import com.neusoft.course.schedule.entity.Course;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.mapper.FacultyMapper;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 导入导出工具
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/19.
 */
@Component
public class ExcelUtil {

    @Autowired
    FacultyMapper facultyMapper;

    /**
     * 排除对象需要过滤的属性下标集合
     */
    private ArrayList<Integer> courseExcludeFieldIndex = new ArrayList<Integer>(5){{
        add(0);
        add(1);
        add(2);
        add(13);
        add(19);
        add(25);
        add(26);
    }};

    /**
     * 将list中的data写入表格中
     *
     * @param keyNames   导出文件的sheet列表值
     * @param list       数据库查询出的Bean集合
     * @param fieldNames 查询出的字段list
     * @param clazz 自定义的Excel可显示字段的枚举的class
     * @param <T>
     * @return
     */
    public <T> SXSSFWorkbook export(
            List<String> keyNames, List<T> list,
            List<String> fieldNames, Class<?> clazz) {
        // 内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
        SXSSFWorkbook wb = new SXSSFWorkbook(2000);
        for (String keyName : keyNames){
            // 创建一个Excel的sheet
            Sheet sheet = wb.createSheet(keyName);
            for (int j = 0; j <= list.size(); j++) {
                Row row = sheet.createRow(j);
                JSONObject jsonObj = null;
                if (j != 0) {
                    T t = list.get(j - 1);
                    jsonObj = (JSONObject) JSON.toJSON(t);
                }
                for (int k = 0; k < fieldNames.size(); k++) {
                    String detailByFieldName = reflectEnumGetDetailByFieldName(clazz, fieldNames.get(k)).toString();
                    if (detailByFieldName == null){
                        continue;
                    }
                    // 第一列单元格
                    Cell cell = row.createCell(k);
                    // 第一行
                    if (j == 0) {
                        CellStyle style = getColumnTopStyle(wb);
                        cell.setCellStyle(style);
                        // 第一列数据
                        cell.setCellValue(detailByFieldName);
                    } else {
                        String fieldKey = fieldNames.get(k);
                        // 将bean对象转换成JSON对象, key为 column_name 去除第一个下划线
                        String content = jsonObj.getString(StringUtils.lineToHump(fieldKey.substring(fieldKey.indexOf("_") + 1, fieldKey.length())));
                        if (content == null) {
                            content = "";
                        } else {
                            if (fieldKey.equals("tc_faculty_id")){
                                content = facultyMapper.selectFacultyById(Integer.parseInt(content)).getName();
                            }
                        }

                        CellStyle style = getBodyStyle(wb);
                        cell.setCellStyle(style);
                        cell.setCellValue(content);
                    }
                }

            }
            setSizeColumn(sheet, fieldNames.size());
        }

        return wb;
    }

    /**
     * 调整列宽,兼容中文
     *
     * @param sheet
     * @param size
     */
    private static void setSizeColumn(Sheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                // 当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }

            // Excel的长度为字节码长度*256,*1.3为了处理数字格式
            columnWidth = (int) Math.floor(columnWidth * 256 * 1.3);
            //单元格长度大于20000的话也不美观,设置个最大长度
            columnWidth = columnWidth >= 20000 ? 20000 : columnWidth;
            //设置每列长度
            sheet.setColumnWidth(columnNum, columnWidth);
        }
    }

    /**
     * 通过浏览器workbook以流的形式输出,为了处理中文表名识别不了的问题.
     *
     * @param workbook 文件对象
     * @param request
     * @param response
     * @param fileName 文件名
     */
    public static void writeToResponse(SXSSFWorkbook workbook, HttpServletRequest request,
                                       HttpServletResponse response, String fileName) {
        try {
            String userAgent = request.getHeader("User-Agent");
            // 解决中文乱码问题
            String fileName1 = "Excel-" + fileName + ".xlsx";
            String newFilename = URLEncoder.encode(fileName1, "UTF8");
            // 如果没有userAgent，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            String rtn = "filename=\"" + newFilename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.indexOf("IE") != -1) {
                    rtn = "filename=\"" + newFilename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.indexOf("OPERA") != -1) {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.indexOf("SAFARI") != -1) {
                    rtn = "filename=\"" + new String(fileName1.getBytes("UTF-8"), "ISO8859-1")
                            + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.indexOf("FIREFOX") != -1) {
                    rtn = "filename*=UTF-8''" + newFilename;
                }
            }

            String headStr = "attachment;  " + rtn;
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", headStr);
            // 响应到客户端
            OutputStream os = response.getOutputStream();
            workbook.write(os);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToResponse(SXSSFWorkbook workbook, HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            String userAgent = request.getHeader("User-Agent");
            // 解决中文乱码问题
            // 如果没有userAgent，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            String rtn = "filename=\"" + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.indexOf("IE") != -1) {
                    rtn = "filename=\"" + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.indexOf("OPERA") != -1) {
                    rtn = "filename*=UTF-8''";
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.indexOf("SAFARI") != -1) {
                    rtn = "filename=\"" + new String("".getBytes("UTF-8"), "ISO8859-1")
                            + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.indexOf("FIREFOX") != -1) {
                    rtn = "filename*=UTF-8''";
                }
            }

            String headStr = "attachment;  " + rtn;
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", headStr);
            // 响应到客户端
            OutputStream os = response.getOutputStream();
            workbook.write(os);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 列头单元格样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle getColumnTopStyle(SXSSFWorkbook workbook) {

        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 1、设置表体的单元格样式
     *
     * @param workbook
     * @return
     */
    private static CellStyle getBodyStyle(SXSSFWorkbook workbook) {
        // 创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置单元格居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 创建单元格内容不显示自动换行
        cellStyle.setWrapText(false);
        // 设置单元格字体样式
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("Courier New");// 设置字体
        font.setFontHeight(11);// 设置字体的大小
        cellStyle.setFont(font);// 将字体添加到表格中去
        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        return cellStyle;
    }

    /**
     * 按造项目枚举结构，反射调用枚举方法
     * @param clazz
     * @param fieldName
     * @return
     */
    private static Object reflectEnumGetDetailByFieldName(Class<?> clazz, String fieldName){
        Method getMeaning = null;
        try {
            getMeaning = clazz.getDeclaredMethod("getDetailByFieldName", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        // 错误的方式，枚举对应的class没有newInstance方法，会报NoSuchMethodException，应该使用getEnumConstants方法
        Object[] oo = clazz.getEnumConstants();
        Object invoke = null;
        try {
            invoke = getMeaning.invoke(oo[0], fieldName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return invoke;
    }

    /**
     * Excel 转对象集合
     * @param file 文件
     * @param aimClass 目标类
     * @param extendObj 拓展参数，可以根据业务包装新构建出来的对象
     * @param <T>
     * @param <S>
     * @return
     */
    public <T, S> List<T> parseObjectFromExcel(File file, Class<T> aimClass, S extendObj) {
        List<T> result = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            //对excel文档的第一页,即sheet1进行操作
            if(workbook.getNumberOfSheets() > 1){
                throw new RuntimeException(ResultCode.EXCEL_SHEET_SIZE_ERROR.getMessage());
            }
            Sheet sheet = workbook.getSheetAt(0);
            int lastRaw = sheet.getLastRowNum();
            for (int i = 1; i <= lastRaw; i++) {
                // 第i行
                Row row = sheet.getRow(i);
                T parseObject = aimClass.newInstance();
                // 课程业务信息设置
                courseServiceExtend(parseObject, aimClass, extendObj);
                Field[] fields = aimClass.getDeclaredFields();
                for (int j = 2, z = 0; j < fields.length; j++, z++) {
                    while(courseExcludeFieldIndex.contains(z)){
                        z ++;
                    }
                    if (z >= fields.length){
                        break;
                    }
                    Field field = fields[z];
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    //第j列
                    Cell cell = row.getCell(j);
                    if (cell == null)
                        continue;
                    cell.setCellType(CellType.STRING);
                    String cellContent = cell.getStringCellValue();
                    cellContent = ObjectUtils.isEmpty(cellContent) ? null : cellContent;
                    if (z <= 11 && ObjectUtils.isEmpty(cellContent)){
                        throw new RuntimeException("第" + j + "列有数据缺失，请检查");
                    }
                    if (type.equals(String.class)) {
                        field.set(parseObject, cellContent);
                    } else if (type.equals(char.class) || type.equals(Character.class)) {
                        field.set(parseObject, cellContent.charAt(0));
                    } else if (type.equals(int.class) || type.equals(Integer.class)) {
                        field.set(parseObject, Integer.parseInt(cellContent));
                    } else if (type.equals(long.class) || type.equals(Long.class)) {
                        field.set(parseObject, Long.parseLong(cellContent));
                    } else if (type.equals(float.class) || type.equals(Float.class)) {
                        field.set(parseObject, Float.parseFloat(cellContent));
                    } else if (type.equals(double.class) || type.equals(Double.class)) {
                        field.set(parseObject, Double.parseDouble(cellContent));
                    } else if (type.equals(short.class) || type.equals(Short.class)) {
                        field.set(parseObject, Short.parseShort(cellContent));
                    } else if (type.equals(byte.class) || type.equals(Byte.class)) {
                        field.set(parseObject, Byte.parseByte(cellContent));
                    } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
                        field.set(parseObject, Boolean.parseBoolean(cellContent));
                    }
                }
                result.add(parseObject);
            }
            return result;
        } catch (Exception e) {
            System.err.println("An error occured when parsing object from Excel. at " + this.getClass());
            e.printStackTrace();
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file.exists()){
                file.delete();
            }
        }
        return result;
    }

    private void courseServiceExtend(Object parseObject, Object aimClass, Object extendObj){
        if (aimClass.equals(Course.class)){
            if (!org.springframework.util.ObjectUtils.isEmpty(extendObj)){
                UploadExcelImportCourseDTO courseDTO = (UploadExcelImportCourseDTO) extendObj;
                ((Course)parseObject).setFacultyId(courseDTO.getFcId());
                ((Course)parseObject).setSemesterId(courseDTO.getTermId());
            }
        }
    }

}
