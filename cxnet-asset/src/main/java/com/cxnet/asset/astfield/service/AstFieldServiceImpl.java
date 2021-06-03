package com.cxnet.asset.astfield.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.astfield.domain.AstField;
import com.cxnet.asset.astfield.mapper.AstFieldMapper;
import com.cxnet.asset.card.util.DynamicGenerationClassUtil;
import com.cxnet.common.constant.Constants;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 卡片字段表(AstField)表服务实现类
 *
 * @author guks
 * @since 2021-04-25 14:31:57
 */
@Service
public class AstFieldServiceImpl extends ServiceImpl<AstFieldMapper, AstField> implements AstFieldService {

    @Override
    public Class getDynamicClass(String exclude, String unitId) {
        //查询数据库中卡片字段列表
        QueryWrapper<AstField> queryWrapper = new QueryWrapper<AstField>();
        queryWrapper.lambda().eq(AstField::getUnitId, unitId).ne(AstField::getIsImport, exclude).eq(AstField::getIsEnable, Constants.YES).orderByAsc(AstField::getOrders);
        List<AstField> astFields = this.list(queryWrapper);
        Class classz = null;
        if (CollectionUtil.isNotEmpty(astFields)) {
            classz = DynamicGenerationClassUtil.generatePrototypeClass(astFields);
        }
        return classz;
    }

}

