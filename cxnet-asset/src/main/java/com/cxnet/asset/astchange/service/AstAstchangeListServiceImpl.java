package com.cxnet.asset.astchange.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.astchange.mapper.AstAstchangeListMapper;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.astchange.service.AstAstchangeListService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产变动明细表(AstAstchangeList)表服务实现类
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:55
 */
@Service
public class AstAstchangeListServiceImpl extends ServiceImpl<AstAstchangeListMapper, AstAstchangeList> implements AstAstchangeListService {

    @Override
    public List<AstAstchangeList> getThisMoAstchange(String date, List<String> astCardIds) {
        return this.baseMapper.getThisMoAstchange(date, astCardIds);
    }
}

