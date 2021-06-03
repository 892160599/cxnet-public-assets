package com.cxnet.flow.model.domain;

import com.cxnet.project.system.dict.domain.SysDictData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: cxnet
 * @description:
 * @author: Mr.Cai
 * @create: 2020-08-14 17:51
 **/
@Data
@ApiModel("单据配置信息")
public class SysBillConf {

    @ApiModelProperty("值集字典code")
    private String dictType;

    @ApiModelProperty("附件文件类别")
    private List<SysDictData> sysDictData;

    @ApiModelProperty("系统模块")
    private SysModel sysModel;


}
