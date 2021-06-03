package com.cxnet.asset.empchange.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.empchange.mapper.AstEmpchangeListMapper;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
import com.cxnet.asset.empchange.service.AstEmpchangeListService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产使用人变动明细表(AstEmpchangeList)表服务实现类
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:30
 */
@Service
public class AstEmpchangeListServiceImpl extends ServiceImpl<AstEmpchangeListMapper, AstEmpchangeList> implements AstEmpchangeListService {

    @Override
    public List<AstEmpchangeList> getThisMoAstchange(String date, List<String> astCardIds) {
        return this.baseMapper.getThisMoAstchange(date, astCardIds);
    }
}

