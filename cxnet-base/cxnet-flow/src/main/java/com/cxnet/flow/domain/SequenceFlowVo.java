package com.cxnet.flow.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @program: cxnet-internal-control
 * @description: 箭头流向vo
 * @author: Mr.Cai
 * @create: 2020-12-10 16:08
 **/
@Data
public class SequenceFlowVo extends NodeBaseEntity {

    private String targetNodeId;
    private String sourceNodeId;
    private List<Map<String, Integer>> waypoints;

}
