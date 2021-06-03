package com.cxnet.project.common.domain;

import lombok.Data;

/**
 * 缓存请求数据类
 *
 * @author Aowen
 * @date 2021/5/12
 */
@Data
public class CacheReqData {

    private String key;
    private String value;

}
