package com.cxnet.asset.inventory.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhangyl
 * @since 2021-04-19 14:14:44
 */

@Data
public class AstInventoryListVo {
    private String billNo;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date acquisitionStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date acquisitionEndDate;

    private List<String> assetsTypeCode;

    private List<String> applyDeptCode;

    private List<String> empCode;

    private List<String> placeCode;

    private List<String> applyStatus;
}
