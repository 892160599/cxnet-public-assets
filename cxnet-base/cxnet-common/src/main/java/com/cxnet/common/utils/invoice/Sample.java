package com.cxnet.common.utils.invoice;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.util.HashMap;

public class Sample {

    //设置APPID/AK/SK
    public static final String APP_ID = "23922492";
    public static final String API_KEY = "wf9FtutKN2K7AkhmIxAkRKja";
    public static final String SECRET_KEY = "Tt1DS7Z330AOf4Ub5SkrZO6zltGBB3Yh";

    public static void main(String[] args) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(20000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", 11);  // 设置http代理
        //client.setSocketProxy("proxy_host", 58681);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("recognize_granularity", "big");
        options.put("detect_direction", "true");
        options.put("vertexes_location", "true");
        options.put("probability", "true");
        // 调用接口
        String pdfPath = "C:\\Users\\Administrator\\Desktop\\顺丰电子发票1.pdf";
        PdfToPng.pdf2png("C:\\Users\\Administrator\\Desktop", "顺丰电子发票1", 0, 1, "png");
        String pngPath = "C:\\Users\\Administrator\\Desktop\\顺丰电子发票1_1.png";
        JSONObject res = client.basicGeneral(pngPath, options);
        System.out.println(res.toString(2));

    }

}
