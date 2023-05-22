package com.jkr.utils;

import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Locale;
import java.util.UUID;


/**
 * <p>Title: PdfUtils.java</p>
 * <p>Description: PDF生成工具类</p>
 * <p>Company:  </p>
 * @author ZY
 * @date 2018年3月1日 下午8:44:18
 * @version v1.0
 */

@SuppressWarnings("all")
public class PDFUtil {

    /**
     * method_name: generateToFile
     * create_user: zhaosongming
     * create_date: 2020-07-03
     * create_time: 09:46:58
     * describe: TODO 生成PDF到文件
     * param : htmlFile 生成的html文件
     * Param : outputFile 目标文件（全路径名称）
     * throws : Exception 异常对象
     */
    public static void generateToFile(File htmlFile, String outputFile) throws Exception {
        ITextRenderer renderer = new ITextRenderer();
        OutputStream os = null;

        os = new FileOutputStream(outputFile);
        ITextRenderer iTextRenderer = new ITextRenderer();
        iTextRenderer.setDocument(htmlFile);
        // 解决中文编码
        ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
        // 判断当前系统，获取字体文件
        if ("linux".equals(getCurrentOperationSystem())) {
            fontResolver.addFont("/usr/share/fonts/chinese/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } else {
            fontResolver.addFont("c:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }

        iTextRenderer.layout();
        iTextRenderer.createPDF(os);
        os.close();
    }

    /**
     * method_name: getCurrentOperationSystem
     * create_user: zhaosongming
     * create_date: 2020-06-16
     * create_time: 02:31:19
     * describe: TODO 获取当前操作系统类型
     * return : String 返回当前操作系统
     */
    private static String getCurrentOperationSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return os;
    }

    /**
     * method_name: createTempPath
     * create_user: zhaosongming
     * create_date: 2020-06-16
     * create_time: 02:31:39
     * describe: TODO 在当前系统临时目录下生成临时文件夹，使用UUID生成文件夹名称
     * param : []
     * return : java.lang.String
     */
    public static String createTempPath() {
        StringBuffer directory = new StringBuffer(System.getProperty("java.io.tmpdir"));
        // 获取当前操作系统类型
        String os = getCurrentOperationSystem();
        // 判断系统类型
        if ("linux".equals(os)) {
            directory.append("/").append(UUID.randomUUID().toString()).append("/");
        } else {
            directory.append(UUID.randomUUID().toString()).append("\\");
        }
        return directory.toString();
    }

    /**
     * <p>Description: 获取数据，使用freemarker生成html</p>
     * <p>Company: </p>
     * @author ZY
     * @param ftlFile  模板文件
     * @param ftlName  模板文件（不含路径）
     * @param imageDiskPath 图片的磁盘路径
     * @param data 数据 （填到模板上的数据）
     * @param tempPath 临时文件生成目录
     * @throws Exception
     */
    public static File getPdfContent(File ftlFile, String ftlName, String tempName, Object data, String tempPath) throws Exception {

        FileOutputStream fos = null;
        Writer out = null;
        Template tpl = null;
        OutputStreamWriter oWriter = null;
        File outFile = null;

        // 获取模板路径及名称
        tpl = getFreemarkerConfig(ftlFile, tempPath).getTemplate(ftlName);
        outFile = new File(tempPath + tempName +".html");
        // 创建目录
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        // 创建文件
        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        fos = new FileOutputStream(outFile);
        oWriter = new OutputStreamWriter(fos, "UTF-8");
        out = new BufferedWriter(oWriter);
        tpl.process(data, out);

        out.flush();
        oWriter.flush();
        fos.flush();

        if (null != out) {
            out.close();
        }
        if (null != oWriter) {
            oWriter.close();
        }
        if (null != fos) {
            fos.close();
        }

        return outFile;
    }


    /**
     * 获取Freemarker配置
     *
     * @param templatePath
     * @return
     * @throws IOException
     */

    private static Configuration getFreemarkerConfig(File file, String tempPath) throws IOException {
        Configuration config = new Configuration();
        config.setDirectoryForTemplateLoading(new File(tempPath));
        config.setEncoding(Locale.CHINA, "utf-8");
        return config;
    }


    /**
     * 获取类项目根路径
     *
     * @return
     */
    public static String getPath() {
        //返回项目根路径（编译之后的根路径）
        return PDFUtil.class.getResource("/").getPath().substring(1);
    }

    /**
     * method_name: FileToMultipartFile
     * create_user: zhaosongming
     * create_date: 2020-06-12
     * create_time: 09:58:51
     * describe: TODO 将File转换为MultipartFile
     * param : [filePath]
     * return : org.springframework.web.multipart.MultipartFile
     */
    public static MultipartFile FileToMultipartFile(String filePath, String contentType) throws Exception {

        FileInputStream input = null;
        MultipartFile multipartFile = null;

        File file = new File(filePath);
        input = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", file.getName(), contentType, IOUtils.toByteArray(input));

        if (input != null) {
            input.close();
        }
        return multipartFile;
    }
}