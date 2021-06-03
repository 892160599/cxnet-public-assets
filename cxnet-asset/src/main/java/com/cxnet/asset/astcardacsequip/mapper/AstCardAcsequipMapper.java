package com.cxnet.asset.astcardacsequip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.astcardacsequip.domain.AstCardAcsequip;

import java.util.List;

/**
 * 附属设备(AstCardAcsequip)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-08 10:35:06
 */
public interface AstCardAcsequipMapper extends BaseMapper<AstCardAcsequip> {

    List<AstCardAcsequip> selectAll(AstCardAcsequip astCardAcsequip);
}