package com.jkr.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author jikeruan
 */
public class ExcelUtil {

    private static Workbook workbook;
    private static String xls = "xls";
    private static String xlsx = "xlsx";
    private static String xlsm = "xlsm";

    public ExcelUtil(Workbook workboook) {
        this.workbook = workboook;
    }

    public static List<List<String>> readExcel(String path,int index) {
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        // return a list contains many list
        List<List<String>> lists = new ArrayList<List<String>>();
        //读取excel文件
        InputStream is = null;


        try {
            is = new FileInputStream(path);
            //获取工作薄
            if (xls.equals(fileType)) {
                workbook = new HSSFWorkbook(is);
            } else if (xlsx.equals(fileType)) {
                workbook = new XSSFWorkbook(is);
            }else if (xlsm.equals(fileType)) {
                workbook = new XSSFWorkbook(is);

            } else {
                return null;
            }

            //读取第一个工作页sheet
            Sheet sheet = workbook.getSheetAt(index);
            //第一行为标题
            for (Row row : sheet) {
                ArrayList<String> list = new ArrayList<String>();
                for (Cell cell : row) {
                    //根据不同类型转化成字符串
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    list.add(cell.getStringCellValue());
                }
                lists.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }

    /**
     * 导入企业信息
     * @param multipartFile
     * @return
     */
    public static List<List<String>> readExcelsGetList(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        List<List<String>> lists = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        //读取excel文件
        InputStream is = null;

        try {
            is = multipartFile.getInputStream();
            //获取工作薄
            if (xls.equals(fileType)) {
                workbook = new HSSFWorkbook(is);
            } else if (xlsx.equals(fileType)) {
                workbook = new XSSFWorkbook(is);
            }else if (xlsm.equals(fileType)) {
                workbook = new XSSFWorkbook(is);
            } else {
                lists.add(msgList(201,0,0));
                return lists;
            }
            //读取第一个工作页sheet
            Sheet sheet = workbook.getSheetAt(0);
            //第一行为标题
            for (Row row : sheet) {
                short lastCellNum = row.getLastCellNum();
                int isNullNum = CheckRowNull(row);
                ArrayList<String> list = new ArrayList<>();
                if(row.getRowNum()>1 && isNullNum!=lastCellNum ){
                    if(lastCellNum == 22 && isNullNum != -1){
                        for (Cell cell : row) {
                            //空值校验
                            if(cell==null||cell.equals("")||cell.getCellType() ==Cell.CELL_TYPE_BLANK){
                                //序号、X，y坐标、详细地址、业务范围、备注不做非空校验
                                if(cell.getColumnIndex() == 0 ||cell.getColumnIndex() == 18 ||  cell.getColumnIndex() == 19 ||cell.getColumnIndex() == 20){
                                    cell.setCellType(CellType.STRING);
                                    list.add(cell.getStringCellValue());
                                    continue;
                                    //属地、行业监管必选一种
                                }else if(cell.getColumnIndex() == 13){
                                    if(row.getCell(14).toString().equals("")){
                                        lists.clear();
                                        lists.add(msgList(203,row.getRowNum(),cell.getColumnIndex()));
                                        return lists;
                                    }else {
                                        cell.setCellType(CellType.STRING);
                                        list.add(cell.getStringCellValue());
                                        continue;
                                    }
                                }else if(cell.getColumnIndex() == 14){
                                    if(row.getCell(13).toString().equals("")){
                                        lists.clear();
                                        lists.add(msgList(203,row.getRowNum(),cell.getColumnIndex()));
                                        return lists;
                                    }else {
                                        cell.setCellType(CellType.STRING);
                                        list.add(cell.getStringCellValue());
                                        continue;
                                    }
                                    //安全员与手机号填写校验
                                }else if(cell.getColumnIndex() == 9){
                                    if(row.getCell(10).toString().equals("")){
                                        cell.setCellType(CellType.STRING);
                                        list.add(cell.getStringCellValue());
                                        continue;
                                    }else {
                                        lists.clear();
                                        lists.add(msgList(209,row.getRowNum(),cell.getColumnIndex()));
                                        return lists;
                                    }
                                }else if(cell.getColumnIndex() == 10){
                                    if(row.getCell(9).toString().equals("")){
                                        cell.setCellType(CellType.STRING);
                                        list.add(cell.getStringCellValue());
                                        continue;
                                    }else {
                                        lists.clear();
                                        lists.add(msgList(210,row.getRowNum(),cell.getColumnIndex()));
                                        return lists;
                                    }
                                }else {
                                    lists.clear();
                                    lists.add(msgList(203,row.getRowNum(),cell.getColumnIndex()));
                                    return lists;
                                }

                            }
                            //数据格式校验
                            //手机号座机号格式
                            if(cell.getColumnIndex() == 6 || cell.getColumnIndex() == 8||cell.getColumnIndex() == 10){
                                if(isPhone(row.getCell(cell.getColumnIndex()).toString()) || isTelPhone(row.getCell(cell.getColumnIndex()).toString())){
                                    cell.setCellType(CellType.STRING);
                                    list.add(cell.getStringCellValue());
                                    continue;
                                }else {
                                    lists.clear();
                                    lists.add(msgList(206,row.getRowNum(),cell.getColumnIndex()));
                                    return lists;
                                }
                            }
                            if(cell.getColumnIndex() == 21 ){
                                if(isPhone(row.getCell(cell.getColumnIndex()).toString())){
                                    cell.setCellType(CellType.STRING);
                                    list.add(cell.getStringCellValue());
                                    continue;
                                }else {
                                    lists.clear();
                                    lists.add(msgList(206,row.getRowNum(),cell.getColumnIndex()));
                                    return lists;
                                }
                            }
                            //社会统一信用代码
                            if(cell.getColumnIndex() == 3) {
                                if(CreditCodeUtils.isValidSocialCreditCode(row.getCell(cell.getColumnIndex()).toString())){
                                    cell.setCellType(CellType.STRING);
                                    list.add(cell.getStringCellValue());
                                    continue;
                                }else {
                                    lists.clear();
                                    lists.add(msgList(207,row.getRowNum(),cell.getColumnIndex()));
                                    return lists;
                                }
                            }
                            //备注业务范围500字限制
                            if(cell.getColumnIndex() == 19 || cell.getColumnIndex() == 20) {
                                if(row.getCell(cell.getColumnIndex()).toString().length()<500){
                                    cell.setCellType(CellType.STRING);
                                    list.add(cell.getStringCellValue());
                                    continue;
                                }else {
                                    lists.clear();
                                    lists.add(msgList(208,row.getRowNum(),cell.getColumnIndex()));
                                    return lists;
                                }
                            }
                            //企业名称限制
                            if(cell.getColumnIndex() == 1) {
                                if(row.getCell(cell.getColumnIndex()).toString().length()<20){
                                    cell.setCellType(CellType.STRING);
                                    list.add(cell.getStringCellValue());
                                    continue;
                                }else {
                                    lists.clear();
                                    lists.add(msgList(211,row.getRowNum(),cell.getColumnIndex()));
                                    return lists;
                                }
                            }
                            cell.setCellType(CellType.STRING);
                            list.add(cell.getStringCellValue());
                        }

                        lists.add(list);
                    } else{
                        lists.clear();
                        lists.add(msgList(202,0,0));
                        return lists;
                    }

                }else if(row.getRowNum()==2 && isNullNum == lastCellNum){
                    //空行数据
                    lists.clear();
                    lists.add(msgList(212,row.getRowNum(),0));
                    return lists;
                }

            }
        } catch (IOException e) {
            lists.clear();
            lists.add(msgList(202,0,0));
            return lists;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }


    /**
     * 判断行有多少个空值
     * @param row
     * @return
     */
    private static int CheckRowNull(Row row){
        int num = 0;
        Iterator<Cell> cellItr =row.iterator();
        while(cellItr.hasNext()){
            Cell c =cellItr.next();
            if(c.getCellType() == Cell.CELL_TYPE_BLANK){
                num++;
            }
        }
        return num;
    }

    /**
     * 错误信息
     * @param code
     * @param rowNum
     * @param columnIndex
     * @return
     */
    public static List<String> msgList(int code,int rowNum,int columnIndex){
        List<String> errorList = new ArrayList<String>();
        if(code == 201){
            errorList.add("error");
            errorList.add("导入失败！上传格式不正确，请参考导入模板上传后重新导入");
        }else if(code == 202) {
            errorList.add("error");
            errorList.add("导入失败！请按导入模板导入并检查导入数据内容是否正确后重新导入");
        }else if(code == 203) {
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"没有数据，建议您检查导入数据！");
        }else if(code == 204) {
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"请您填写正确数据格式，建议您检查导入数据！");
        }else if(code == 205) {
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写的手机号已被使用，建议您更换手机号！");
        }else if(code == 206) {
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写的手机号或座机号格式不正确，请重新填写！");
        }else if(code == 207) {
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写的统一信用代码格式不正确，请重新填写！");
        }else if(code == 208) {
            errorList.add("error");
            if((columnIndex+1) == 19){
                errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写业务范围超过了500字，请重新填写！");
            }else {
                errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写备注超过了500字，请重新填写！");
            }
        }else if(code == 209){
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写安全员没有填写联系方式，请重新填写！");
        }else if(code == 210){
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写填写联系方式没有填写安全员名称，请重新填写！");
        }else if(code == 211){
            errorList.add("error");
            errorList.add("导入失败！请检查第"+(rowNum+1)+"行第"+(columnIndex+1)+"列，"+"您填写企业名称超过了20字，请重新填写！");
        }else if(code == 212){
            errorList.add("error");
            errorList.add("导入失败！"+"导入数据为空，请检查数据，重新填写！");
        }



        return errorList;
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }


    /**
     * @param phone 字符串类型的手机号
     * 传入手机号,判断后返回
     * true为手机号,false相反
     * */
    public static boolean isPhone(String phone) {
        String regex = "^1[34578]\\d{9}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }


    /**
     * 座机号码验证
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isTelPhone(String str) {
        Pattern p1 = null,p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        if(str.length() >9) {
            m = p1.matcher(str);
            b = m.matches();
        }
        return b;
    }
}