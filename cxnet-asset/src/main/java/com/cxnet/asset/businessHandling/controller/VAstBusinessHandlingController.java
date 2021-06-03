package com.cxnet.asset.businessHandling.controller;

import com.cxnet.asset.businessHandling.domain.VAstBusinessHandling;
import com.cxnet.asset.businessHandling.service.VAstBusinessHandlingService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.flow.model.domain.SysModel;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产业务办理(VAstBusinessHandling)表控制层
 *
 * @author caixx
 * @since 2021-03-31 16:04:18
 */
@Slf4j
@Api(tags = "资产业务办理")
@RestController
@RequestMapping("/vAstBusinessHandling")
public class VAstBusinessHandlingController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private VAstBusinessHandlingService vAstBusinessHandlingService;

    @ApiOperation("查询单据下拉列表")
    @GetMapping("/astModelList")
    public AjaxResult selectAll() {
        List<SysModel> list = vAstBusinessHandlingService.getAstModel();
        return success(list);
    }

    /**
     * 分页查询所有数据
     *
     * @param vAstBusinessHandling 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据(我的事项)")
    @GetMapping
    public AjaxResult selectAssetMatter(VAstBusinessHandling vAstBusinessHandling) {
        startPage();
        List<VAstBusinessHandling> list = vAstBusinessHandlingService.selectAssetMatter(vAstBusinessHandling);
        return success(getDataTable(list));
    }

    /**
     * 分页查询所有数据
     *
     * @param vAstBusinessHandling 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据(业务审批)")
    @GetMapping("/assetAudit")
    public AjaxResult selectAssetAudit(VAstBusinessHandling vAstBusinessHandling) {
        startPage();
        List<VAstBusinessHandling> list = vAstBusinessHandlingService.selectAssetAudit(vAstBusinessHandling);
        return success(getDataTable(list));
    }

}