package com.cxnet.framework.web.domain;

import java.io.Serializable;

import com.cxnet.rpc.domain.RpcBaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Entity基类
 *
 * @author cxnet
 */
@Data
@ApiModel("基础实体")
public class BaseEntity extends RpcBaseEntity implements Serializable {

}
