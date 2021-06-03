package com.cxnet.asset.check.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.check.mapper.AstCheckListMapper;
import com.cxnet.asset.check.domain.AstCheckList;
import com.cxnet.asset.check.service.AstCheckListService;
import org.springframework.stereotype.Service;

/**
 * 资产验收明细表(AstCheckList)表服务实现类
 *
 * @author caixx
 * @since 2021-03-25 10:22:54
 */
@Service
public class AstCheckListServiceImpl extends ServiceImpl<AstCheckListMapper, AstCheckList> implements AstCheckListService {

}