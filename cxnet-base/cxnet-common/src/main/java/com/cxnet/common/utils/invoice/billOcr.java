package com.cxnet.common.utils.invoice;

import com.baidu.aip.contentcensor.EImgType;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.util.HashMap;

public class billOcr {
    //设置APPID/AK/SK
    public static final String APP_ID = "23922492";
    public static final String API_KEY = "wf9FtutKN2K7AkhmIxAkRKja";
    public static final String SECRET_KEY = "Tt1DS7Z330AOf4Ub5SkrZO6zltGBB3Yh";


    //    public void sample(AipOcr client) {
    public static void main(String[] args) {
        //初始化AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        //设置链接超时时间毫秒
        client.setConnectionTimeoutInMillis(10000);
        client.setSocketTimeoutInMillis(20000);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();

        // 参数为本地图片路径
        //String image = "test.jpg";
        String pdfPath = "C:\\Users\\Administrator\\Desktop\\顺丰电子发票1.pdf";
//        PdfToPng.pdf2png("C:\\Users\\Administrator\\Desktop","顺丰电子发票1",0,1,"png");
//        String pngPath = "C:\\Users\\Administrator\\Desktop\\顺丰电子发票1_1.png";
//        JSONObject res = client.webimageLoc(pngPath, EImgType.FILE, options);
//        System.out.println(res.toString(2));
        // 参数为url
//        String image = "http://test.jpg";
//        JSONObject res = client.vatInvoice( image, EImgType.URL, options);
//        System.out.println(res.toString(2));
        // 参数为本地pdf
        //String image = "test.pdf";
        JSONObject res = client.vatInvoice(pdfPath, EImgType.PDF, options);
        System.out.println(res.toString(2));
//        参数为本地图片二进制数组
//        byte[] file = readImageFile(image);
//        res = client.vatInvoice(file, options);
//        System.out.println(res.toString(2));
    }
}
