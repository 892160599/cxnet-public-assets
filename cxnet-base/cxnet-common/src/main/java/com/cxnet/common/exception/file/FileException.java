package com.cxnet.common.exception.file;

import com.cxnet.common.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @author cxnet
 */
public class FileException extends BaseException {
    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
