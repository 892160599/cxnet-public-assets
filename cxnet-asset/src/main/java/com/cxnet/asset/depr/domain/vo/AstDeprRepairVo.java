package com.cxnet.asset.depr.domain.vo;

import com.cxnet.asset.depr.domain.AstDeprRepairBill;
import com.cxnet.asset.depr.domain.AstDeprRepairList;
import lombok.Data;

import java.util.List;

/**
 * @program: cxnet-internal-control
 * @description:
 * @author: Mr.Cai
 * @create: 2021-04-19 13:48
 **/
@Data
public class AstDeprRepairVo {

    private AstDeprRepairBill astDeprRepairBill;

    private List<AstDeprRepairList> astDeprRepairLists;

}
