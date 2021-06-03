package com.cxnet.common.utils.invoice;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.HashMap;
import java.util.Map;

/**
 * 极光推送demo
 */
public class JpushUtils {
    // app_key和masterSecret
    private static String APP_KEY = "0db4f6a29762ad21cfd42f9c";
    private static String MASTER_SECRET = "e85ce5a032e61a6fa9e3e277";

    //极光推送>>All所有平台
    public static void jpushAll(Map<String, String> parm) {
        //创建JPushClient
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        //创建option
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())  //所有平台的用户
                //.setAudience(Audience.alias(parm.get("id")))
                .setAudience(Audience.registrationId(parm.get("id")))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder() //发送ios
                                .setAlert(parm.get("msg")) //消息体
                                .setBadge(+1)
                                .setSound("happy") //ios提示音
                                .addExtras(parm) //附加参数
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder() //发送android
                                .addExtras(parm) //附加参数
                                .setAlert(parm.get("msg")) //消息体
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(false).build())//指定开发环境 true为生产模式 false 为测试模式 (android不区分模式,ios区分模式)
                //.setMessage(Message.newBuilder().setMsgContent(parm.get("msg")).addExtras(parm).build())//自定义信息
                .build();
        try {
            PushResult pu = jpushClient.sendPush(payload);
            System.out.println(pu.toString());
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {


        //设置推送参数
        //这里就可以自定义推送参数了
        Map<String, String> parm = new HashMap<String, String>();
        //设备id,指定设备推送id
        parm.put("id", "100d855909e98c4caaf");
        //设置提示信息,内容是文章标题
        parm.put("msg", "rw测试极光推送");
        //附加参数
//            parm.put("name","任伟");
        jpushAll(parm);

    }


}