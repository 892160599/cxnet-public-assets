package com.cxnet.common.cmd;

import com.cxnet.common.config.BaseConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 命令管理
 */
@Slf4j
public class CommandMgr {


    /**
     * 登录平台校验
     */
    public int loginPlatformVerify(String platformId, String filePath) {
        log.debug("==========>>>>>>>>>>开始执行登录平台校验脚本！<<<<<<<<<<==========");
        String cmd = BaseConfig.getPythonPath() + " " + BaseConfig.getBinPath() + File.separator
                + "sync.py -a login -i " + platformId + " -c " + filePath;
        return runShellCommand(cmd);
    }

    /**
     * 同步单个平台
     */
    public int syncSinglePlatform(String platformId, String filePath) {
        log.debug("==========>>>>>>>>>>开始执行同步单个平台脚本！<<<<<<<<<<==========");
        String cmd = BaseConfig.getPythonPath() + " " + BaseConfig.getBinPath() + File.separator
                + "sync.py -a sync -i " + platformId + " -c " + filePath;
        return runShellCommand(cmd);
    }

    /**
     * 同步所有平台
     */
    public int syncAllPlatform(String filePath) {
        log.debug("==========>>>>>>>>>>开始执行同步所有平台脚本！<<<<<<<<<<==========");
        String cmd = BaseConfig.getPythonPath() + " " + BaseConfig.getBinPath() + File.separator
                + "sync.py -a sync" + " -c " + filePath;
        return runShellCommand(cmd);
    }

    /**
     * 执行shell命令
     */
    private int runShellCommand(String cmd) {
        log.debug("执行命令：" + cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            ProcessWithTimeout processWithTimeout = new ProcessWithTimeout(process);
            int ret = processWithTimeout.waitForProcess(5 * 1000);
            if (ret == Integer.MIN_VALUE) {
                // Timeout
                processWithTimeout.interrupt();
                log.error(cmd + " , timeout");
            } else if (ret != 0) {
                log.error(cmd + " , exit code: " + ret);
            }
            process.destroy();
            return ret;
        } catch (Exception e) {
            return -1;
        }
    }

//    private int runShellCommand(String cmd, int timeout_sec) {
//        try {
//            Process process = Runtime.getRuntime().exec(cmd);
//            ProcessWithTimeout processWithTimeout = new ProcessWithTimeout(process);
//            int ret = processWithTimeout.waitForProcess(timeout_sec * 1000);
//            if (ret == Integer.MIN_VALUE) {
//                // Timeout
//                processWithTimeout.interrupt();
//                logger.error(cmd + " , timeout");
//            } else if (ret != 0) {
//                logger.error(cmd + " , exit code: " + ret);
//            }
//            process.destroy();
//            return ret;
//        } catch (Exception e) {
//            return -1;
//        }
//    }

    private class ProcessWithTimeout extends Thread {
        private Process mProcess;
        private int mExitCode = Integer.MIN_VALUE;

        public ProcessWithTimeout(Process pProcess) {
            mProcess = pProcess;
        }

        public int waitForProcess(int pTimeoutMilliseconds) {
            this.interrupt();
            this.start();
            try {
                this.join(pTimeoutMilliseconds);
            } catch (InterruptedException e) {
                this.interrupt();
            }
            return mExitCode;
        }

        @Override
        public void run() {
            try {
                mExitCode = mProcess.waitFor();
            } catch (InterruptedException ignore) {
                // Do nothing
            } catch (Exception ex) {
                // Unexpected exception
            }
        }
    }
}
