package com.cxnet.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 读取项目相关配置
 *
 * @author cxnet
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "cxnet")
public class BaseConfig {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private boolean demoEnabled;

    /**
     * 上传路径
     */
    private static String profile;

    /**
     * python路径
     */
    private static String pythonPath;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled() {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled) {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        BaseConfig.profile = profile;
    }

    public static String getPythonPath() {
        return pythonPath;
    }

    public void setPythonPath(String pythonPath) {
        BaseConfig.pythonPath = pythonPath;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        BaseConfig.addressEnabled = addressEnabled;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + File.separator + "avatar";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + File.separator + "upload";
    }

    /**
     * 获取公务之家上传路径
     */
    public static String getReimUploadPath() {
        return getProfile() + File.separator + "upload" + File.separator + "reim";
    }

    /**
     * 获取配置路径
     */
    public static String getConfigPath() {
        return getProfile() + File.separator + "conf";
    }

    /**
     * 获取nginx配置路径
     */
    public static String getBootClusterPath() {
        return getProfile() + File.separator + "nginx" + File.separator + "conf";
    }

    /**
     * 获取数据路径
     */
    public static String getDataPath() {
        return getProfile() + File.separator + "data";
    }

    /**
     * 获取平台数据路径
     */
    public static String getDataPlatformPath() {
        return getProfile() + File.separator + "data" + File.separator + "platform";
    }

    /**
     * 获取日志路径
     */
    public static String getLogPath() {
        return getProfile() + File.separator + "log";
    }

    /**
     * 获取报表路径
     */
    public static String getReportPath() {
        return getProfile() + File.separator + "report";
    }

    public static String getPatternPath() {
        return getProfile() + File.separator + "pat";
    }

    public static String getPatchPath() {
        return getProfile() + File.separator + "patch";
    }

    public static String getTempLogPath() {
        return getProfile() + File.separator + "temp";
    }

    public static String getBinPath() {
        return getProfile() + File.separator + "bin";
    }

    public static String getUpdatePath() {
        return getProfile() + File.separator + "update";
    }

    public static String getMailPath() {
        return getProfile() + File.separator + "mail";
    }

    public static String getVirtualDataPath() {
        return getDataPath() + File.separator + "virtual";
    }

    public static String getDefaultDataPath() {
        return getDataPath() + File.separator + "default";
    }

    public static String getTemplateDataPath() {
        return getDataPath() + File.separator + "templates";
    }

    public static String getPolicyDataPath() {
        return getDataPath() + File.separator + "policies";
    }

    public static String getSystemEventPath() {
        return getDataPath() + File.separator + "system_event";
    }

    public static String getWidgetDataPath() {
        return getDataPath() + File.separator + "widget";
    }

    public static String getTomcatRootPath() {
        return getProfile() + File.separator + "tomcat" + File.separator + "webapps" + File.separator + "ROOT";
    }

    public static String getDeployPath() {
        return getTomcatRootPath() + File.separator + "download" + File.separator + "deploy" + File.separator + "ROOT";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + File.separator + "download";
    }

    public static String getDownloadsPath() {
        return getProfile() + File.separator;
    }

    public static String getAgentPkgPath() {
        return getTomcatRootPath() + File.separator + "download" + File.separator + "deploy" + File.separator + "ROOT" + File.separator + "agent";
    }

    public static String getAgentlessPkgPath() {
        return getTomcatRootPath() + File.separator + "download" + File.separator + "deploy" + File.separator + "ROOT" + File.separator + "agentless";
    }

    public static String getResourcePath() {
        return getTomcatRootPath() + File.separator + "download" + File.separator + "resource";
    }

    public static String getDefaultResourcePath() {
        return getTomcatRootPath() + File.separator + "download" + File.separator + "resource-default";
    }

    public static boolean isShowNetworkFunc() {
        boolean bShow = true;
        String sigpath = getTomcatRootPath() + File.separator + "hide_net_function";
        File file = new File(sigpath);
        if (file.exists()) {
            bShow = false;
        }
        return bShow;
    }

    /**
     * 获取主机名称
     */
    public static String getHostname() {
        String hostname = null;
        BufferedReader br = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("hostname");
            process.waitFor();
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            hostname = br.readLine().trim();
            br.close();
            process.destroy();
        } catch (Exception e) {

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                log.error("流关闭异常：{}", e);
            }
            if (process != null) {
                process.destroy();
            }
        }
        return hostname;
    }

    /**
     * 获取删除路径
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < (children != null ? children.length : 0); i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public synchronized static boolean deleteConfigDirectory(String tenant) {
        boolean ret = true;
        if (tenant != null) {
            String fileName = getPolicyDataPath() + File.separator + tenant;
            try {
                File file = new File(fileName);
                if (file.exists() && file.isDirectory()) {
                    ret = deleteDir(file);
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
                log.error("Failed to delete file directory " + fileName);
                ret = false;
            }
        }
        return ret;
    }

    public synchronized static boolean deleteConfigDirectories(List<String> tenantIds) {
        boolean ret = true;
        for (String tenantId : tenantIds) {
            String fileName = getPolicyDataPath() + File.separator + tenantId;
            try {
                File file = new File(fileName);
                if (file.exists() && file.isDirectory()) {
                    ret = deleteDir(file);
                }
            } catch (Exception e) {
                log.debug(e.getMessage());
                log.error("Failed to delete file directory " + fileName);
                ret = false;
            }
        }

        return ret;
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream outStream = new FileOutputStream(newPath);
                byte[] buffer = new byte[2048];
                while ((byteread = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, byteread);
                }
                inStream.close();
                outStream.close();
            }
        } catch (Exception e) {
            log.error("错误原因:{}", e.getMessage(), e);
        }
    }

    public static List<String> getFilesList(String filePath) {
        List<String> filelist = new ArrayList<String>();
        File root = new File(filePath);
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filelist.add(file.getName());
                }
            }
        }
        return filelist;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim());
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int strLength(String s) {
        if (s == null) {
            return 0;
        }
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static String getStrLength(String s, int l) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        String ret = "";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            if (valueLength > l) {
                break;
            }
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
            ret += temp;
        }
        return ret;
    }

    public static Set<String> getMacList() {
        Set<String> macList = new HashSet<String>();
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                StringBuffer stringBuffer = new StringBuffer();
                NetworkInterface networkInterface = enumeration.nextElement();
                if (networkInterface != null && !"lo".equals(networkInterface.getName())) {
                    byte[] bytes = networkInterface.getHardwareAddress();
                    if (bytes != null) {
                        for (int i = 0; i < bytes.length; i++) {
                            if (i != 0) {
                                stringBuffer.append(":");
                            }
                            int tmp = bytes[i] & 0xff; // 字节转换为整数
                            String str = Integer.toHexString(tmp);
                            if (str.length() == 1) {
                                stringBuffer.append("0" + str);
                            } else {
                                stringBuffer.append(str);
                            }
                        }
                        String mac = stringBuffer.toString().toLowerCase();
                        if (mac.length() > 0) {
                            macList.add(mac);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("错误原因:{}", e.getMessage(), e);
            log.error("Get management center mac list error!");
        }
        return macList;

    }

    public static Set<String> getIpList() {
        Set<String> ipList = new HashSet<String>();
        Enumeration<NetworkInterface> allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (java.net.SocketException e) {
            log.error("错误原因:{}", e.getMessage(), e);
        }
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            String name = netInterface.getName();
            log.debug("name = " + name);
            if (netInterface != null && !"lo".equals(name)) {
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        log.debug("iplist " + name + "-" + ip.getHostAddress());
                        ipList.add(name + "-" + ip.getHostAddress());
                    }
                }
            }
        }
        return ipList;
    }


    /**
     * 转换文件大小
     */
    public static String formatFileSize(Long fileS) {
        DecimalFormat df = new DecimalFormat("#.0000");
        String fileSizeString = "";
        String wrongSize = "0";
        if (fileS == 0) {
            return wrongSize;
        } else {
            fileSizeString = df.format((double) fileS / 1048576);
        }

        return fileSizeString;
    }

}