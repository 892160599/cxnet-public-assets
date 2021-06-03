package com.cxnet.asset.annex.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.mapper.AstAnnexMapper;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产附件关联表(AstAnnex)表服务实现类
 *
 * @author makejava
 * @since 2021-03-25 10:55:12
 */
@Service
public class AstAnnexServiceImpl extends ServiceImpl<AstAnnexMapper, AstAnnex> implements AstAnnexService {


    @Override
    public List<AstAnnex> listByAstIdAndAnnexType(String astId, String annexType) {
        return this.baseMapper.listByAstIdAndAnnexType(astId, annexType);
    }
}

