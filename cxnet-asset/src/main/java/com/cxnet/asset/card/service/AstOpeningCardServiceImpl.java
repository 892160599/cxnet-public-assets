package com.cxnet.asset.card.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.domain.AstOpeningCard;
import com.cxnet.asset.card.domain.AstOpeningJournal;
import com.cxnet.asset.card.mapper.AstOpeningCardMapper;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.service.BdAssetTypeService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 期初卡片导入记录表(AstOpeningCard)表服务实现类
 *
 * @author zhangyl
 * @since 2021-04-22 16:29:00
 */
@Service
public class AstOpeningCardServiceImpl extends ServiceImpl<AstOpeningCardMapper, AstOpeningCard> implements AstOpeningCardService {

    @Autowired(required = false)
    private BdAssetTypeService bdAssetTypeService;
    @Autowired(required = false)
    private AstOpeningJournalService astOpeningJournalService;
    @Autowired(required = false)
    private AstCardService astCardService;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;


    @Override
    @Transactional
    public AstOpeningCard insertCardList(List<AstCard> astCardList) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        AstOpeningCard card = new AstOpeningCard();
        card.setId(uuid);
        card.setConfirmedCode(user.getUserName());
        card.setConfirmedName(user.getNickName());
        card.setConfirmedDate(new Date());
        card.setIsSucess("0");
        card.setDelFlag("0");
        int cardLine = 4;
        for (AstCard v : astCardList) {
            String id = UUID.randomUUID().toString().replace("-", "");
            v.setId(id);
            v.setAstStatus("0");
            v.setInitialState("1");
            v.setIsInitImport("1");
            v.setDelFlag("0");
            card.setUnitId(v.getUnitId());

            //把卡片id放到日志表中
            AstOpeningJournal journal = new AstOpeningJournal();
            journal.setOpeningId(uuid);
            journal.setAstId(id);
            journal.setStatus("1");
            journal.setUnitId(v.getUnitId());
            journal.setCardLine(Integer.toString(cardLine));
            cardLine = cardLine + 1;
            //判断资产类别是否存在
            QueryWrapper<BdAssetType> typeQueryWrapper = new QueryWrapper<>();
            typeQueryWrapper.lambda().eq(BdAssetType::getAssetCode, v.getCategoryCode())
                    .eq(BdAssetType::getUnitId, user.getDeptId());
            BdAssetType one = bdAssetTypeService.getOne(typeQueryWrapper);
            if (ObjectUtil.isNull(one)) {
                card.setIsSucess("2");
                journal.setAssetName(v.getAssetName());
                if (v.getQty() != null) {
                    journal.setQty(Integer.toString(v.getQty()));
                }
                journal.setCost(v.getCost());
                //卡片行号
                journal.setConfirmedCode(user.getUserName());
                journal.setConfirmedName(user.getNickName());
                journal.setStatus("2");
                journal.setRemark("资产类别编码不存在");
            }
            //保存卡片日志表
            astOpeningJournalService.save(journal);
            //生成卡片编号
            v.setCreateTime(new Date());
            v.setAstCode(ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_REGISTER, v));
        }
        if ("0".equals(card.getIsSucess())) {
            astCardService.saveBatch(astCardList);
        }
        this.save(card);
        return card;
    }

    @Override
    public List<AstOpeningJournal> getList(String id) {
        QueryWrapper<AstOpeningJournal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstOpeningJournal::getOpeningId, id).eq(AstOpeningJournal::getStatus, "2");
        List<AstOpeningJournal> list = astOpeningJournalService.list(wrapper);
        return list;
    }
}

