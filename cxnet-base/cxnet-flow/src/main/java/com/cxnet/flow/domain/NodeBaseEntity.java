package com.cxnet.flow.domain;

import com.cxnet.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @program: cxnet-internal-control
 * @description:
 * @author: Mr.Cai
 * @create: 2020-12-10 16:09
 **/
@Data
public class NodeBaseEntity {

    private String id;
    private String name;
    private String users;
    private String type;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    public NodeBaseEntity() {
    }

    public NodeBaseEntity(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getStatus() {
        if (StringUtils.isEmpty(this.status)) {
            this.status = "0";
        }
        return this.status;
    }
}
