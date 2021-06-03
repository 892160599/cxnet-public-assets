package com.cxnet.asset.businessSet.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.businessSet.mapper.AstDeprMethodMapper;
import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.cxnet.asset.businessSet.service.AstDeprMethodService;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产折旧方法表(AstDeprMethod)表服务实现类
 *
 * @author zhangyl
 * @since 2021-03-25 10:00:10
 */
@Service
public class AstDeprMethodServiceImpl extends ServiceImpl<AstDeprMethodMapper, AstDeprMethod> implements AstDeprMethodService {

    @Autowired(required = false)
    private AstDeprMethodMapper astDeprMethodMapper;

}

