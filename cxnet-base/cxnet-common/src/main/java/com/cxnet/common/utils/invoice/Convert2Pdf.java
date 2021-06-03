package com.cxnet.common.utils.invoice;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: rw
 * Date: 2021/4/9
 * Description:文件转为二进制
 */
public class Convert2Pdf {

    /**
     * @param jpgPath 图片的地址
     * @param pdfPath 转换pdf的地址
     */
    public static void toPdf(String jpgPath, String pdfPath) {
        Document document = new Document();
        FileOutputStream fos = null;
        try {
            //设置pdf的输出地址
            fos = new FileOutputStream(pdfPath);
            //设置document的行间距
            document.setMargins(0, 0, 0, 0);
            //将pdf写到对应的地址
            PdfWriter.getInstance(document, fos);
            // 打开文档
            document.open();
            //读取图片
            Image image = Image.getInstance(jpgPath);
            //行宽
            float scaledWidth = image.getScaledWidth();
            //行高
            float scaledHeight = image.getScaledHeight();
            //创建一个矩形
            Rectangle rectangle = new Rectangle(scaledHeight, scaledWidth);
            //设置文档对象页面大小
            document.setPageSize(rectangle);
            //设置图片位置
            image.setAlignment(Image.ALIGN_BASELINE);
            //创建一页
            document.newPage();
            //将图片添加到document对象中
            document.add(image);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关流
            document.close();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        String jpgPath = "C:\\Users\\Administrator\\Desktop\\增值税普票（卷票）.jpg";
        String pdfPath = "C:\\Users\\Administrator\\Desktop\\增值税普票（卷票）.pdf";
        Convert2Pdf.toPdf(jpgPath, pdfPath);
    }
}


