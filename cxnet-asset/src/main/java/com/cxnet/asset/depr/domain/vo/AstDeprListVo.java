package com.cxnet.asset.depr.domain.vo;

import com.cxnet.asset.depr.domain.AstDeprList;
import lombok.Data;

import java.util.List;

/**
 * @program: cxnet-internal-control
 * @description:
 * @author: Mr.Cai
 * @create: 2021-04-09 14:55
 **/
@Data
public class AstDeprListVo {

    private List<AstDeprList> classCodeList;

    private List<AstDeprList> deptList;

}
