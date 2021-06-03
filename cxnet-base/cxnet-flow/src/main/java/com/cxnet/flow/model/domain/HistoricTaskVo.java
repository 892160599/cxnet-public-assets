package com.cxnet.flow.model.domain;

import lombok.Data;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

/**
 * @program: cxnet-internal-control
 * @description:
 * @author: Mr.Cai
 * @create: 2020-11-19 09:47
 **/
@Data
public class HistoricTaskVo extends HistoricTaskInstanceEntity implements HistoricTaskInstance {

    private String diachronic;

}
