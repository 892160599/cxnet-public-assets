package com.cxnet.asset.astchange.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.astchange.domain.vo.AstAstchangeVo;
import com.cxnet.asset.astchange.domain.vo.SelectAstAstChangeListVo;
import com.cxnet.asset.astchange.mapper.AstAstchangeBillMapper;
import com.cxnet.asset.astchange.domain.AstAstchangeBill;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.mapper.AstCardMapper;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.empchange.domain.AstEmpchangeBill;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
import com.cxnet.asset.empchange.service.AstEmpchangeBillService;
import com.cxnet.asset.empchange.service.AstEmpchangeListService;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.service.BdAssetTypeService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.SpringContextUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.flow.utils.BillUtils;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.common.service.CheckStatus;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.service.SysUserServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.task.Task;
import org.omg.CORBA.CurrentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产变动主表(AstAstchangeBill)表服务实现类
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:14
 */
@Service
public class AstAstchangeBillServiceImpl extends ServiceImpl<AstAstchangeBillMapper, AstAstchangeBill> implements AstAstchangeBillService {

    @Autowired(required = false)
    private AstAstchangeBillMapper astAstchangeBillMapper;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private SysUserServiceI sysUserServiceI;
    @Autowired(required = false)
    private AstCardMapper astCardMapper;
    @Autowired(required = false)
    private AstAstchangeListService astAstchangeListService;
    @Autowired(required = false)
    private AstCardService astCardService;
    @Autowired(required = false)
    private AstAnnexService astAnnexService;
    @Autowired(required = false)
    private AstDeptUserService astDeptUserService;
    @Autowired(required = false)
    private AstEmpchangeListService astEmpchangeListService;
    @Autowired(required = false)
    private AstEmpchangeBillService astEmpchangeBillService;
    @Autowired(required = false)
    private BdAssetTypeService bdAssetTypeService;
    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelServiceIRpc;

    /**
     * 判断该明细是否已保存
     *
     * @param astAstchangeLists
     * @author zhaoyi
     */
    public void ifExist(List<AstAstchangeList> astAstchangeLists) {
        astAstchangeLists.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            if (("0").equals(astCard.getInitialState())) {
                throw new CustomException("卡片" + astCard.getAstCode() + "已在操作中");
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAstAstchangeVo(AstAstchangeVo astAstchangeVo) {
        AstAstchangeBill astAstchangeBill = astAstchangeVo.getAstAstchangeBill();
        List<AstAstchangeList> astAstchangeLists = astAstchangeVo.getAstAstchangeLists();
        List<AstAnnex> astAnnexes = astAstchangeVo.getAstAnnexes();
        this.ifExist(astAstchangeLists);
        //编号器生成单据号
        astAstchangeBill.setCreateTime(new Date());
        String astchangeCode = ruleServiceI.nextNumber(BillUtils.getRuleCode(BillTypeCodeConstant.AST_ASTCHANGE_CODE), astAstchangeBill);
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(astAstchangeBill.getOperatorId());
        astAstchangeBill.setOperatorCode(bdPersonnel.getUserCode());
        astAstchangeBill.setOperatorName(bdPersonnel.getUserName());
        astAstchangeBill.setAstchangeCode(astchangeCode);
        astAstchangeBill.setStatus("0");
        astAstchangeBill.setDelFlag("0");
        int i = astAstchangeBillMapper.insert(astAstchangeBill);
        //主表保存成功
        if (i > 0) {
            //保存明细
            List<AstAstchangeList> astchangeLists = new ArrayList<>();
            List<AstCard> astCardList = new ArrayList<>();
            astAstchangeLists.forEach(a -> {
                a.setBillId(astAstchangeBill.getId());
                a.setId("");
                astchangeLists.add(a);
                AstCard astCard = astCardMapper.selectById(a.getAstId());
                //回写卡片状态
                astCard.setInitialState("0");
                astCardList.add(astCard);
            });
            astAstchangeListService.saveBatch(astchangeLists);
            astCardService.updateBatchById(astCardList);
            //保存附件信息
            List<AstAnnex> astAnnexes1 = new ArrayList<>();
            astAnnexes.forEach(b -> {
                b.setAstId(astAstchangeBill.getId());
                b.setAnnexType("11");
                astAnnexes1.add(b);
            });
            astAnnexService.saveBatch(astAnnexes1);
        }
        return astAstchangeBill.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAstAstchangeVo(AstAstchangeVo astAstchangeVo) {
        AstAstchangeBill astAstchangeBill = astAstchangeVo.getAstAstchangeBill();
        List<AstAstchangeList> astAstchangeLists = astAstchangeVo.getAstAstchangeLists();
        List<AstAnnex> astAnnexes = astAstchangeVo.getAstAnnexes();
        String id = astAstchangeBill.getId();
        AstAstchangeBill selectById = this.getById(id);
        if (ObjectUtil.isNull(selectById) || "2".equals(selectById.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!"0".equals(selectById.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(astAstchangeBill.getOperatorId());
        astAstchangeBill.setOperatorCode(bdPersonnel.getUserCode());
        astAstchangeBill.setOperatorName(bdPersonnel.getUserName());
        //修改主表
        this.updateById(astAstchangeBill);
        //修改明细表
        QueryWrapper<AstAstchangeList> astAstchangeListQueryWrapper = new QueryWrapper<>();
        //查询当前单据下的明细信息
        astAstchangeListQueryWrapper.lambda().eq(AstAstchangeList::getBillId, id);
        List<AstAstchangeList> astchangeLists = astAstchangeListService.list(astAstchangeListQueryWrapper);
        List<AstCard> astCardList = new ArrayList<>();
        astchangeLists.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            //回写资产卡片,解除锁定
            astCard.setInitialState("1");
            astCardList.add(astCard);
        });
        astCardService.updateBatchById(astCardList);
        astAstchangeListService.remove(astAstchangeListQueryWrapper);
        List<AstCard> astCardList1 = new ArrayList<>();
        astAstchangeLists.forEach(b -> {
            b.setId("");
            b.setBillId(id);
            AstCard astCard = astCardMapper.selectById(b.getAstId());
            //回写资产卡片,锁定资产
            astCard.setInitialState("0");
            astCardList1.add(astCard);
        });
        astAstchangeListService.saveBatch(astAstchangeLists);
        astCardService.updateBatchById(astCardList1);
        //修改附件表
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        astAnnexService.remove(astAnnexQueryWrapper);
        astAnnexes.forEach(b -> {
            b.setAstId(id);
            b.setAnnexType("6");
        });
        astAnnexService.saveBatch(astAnnexes);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(List<String> ids) {
        List<AstAstchangeBill> astAstchangeBills = this.listByIds(ids);
        List<AstCard> astCardList = new ArrayList<>();
        astAstchangeBills.forEach(a -> {
            a.setDelFlag("2");
            QueryWrapper<AstAstchangeList> astAstchangeListQueryWrapper = new QueryWrapper<>();
            astAstchangeListQueryWrapper.lambda().eq(AstAstchangeList::getBillId, a.getId());
            List<AstAstchangeList> astchangeLists = astAstchangeListService.list(astAstchangeListQueryWrapper);
            astchangeLists.forEach(b -> {
                AstCard astCard = astCardMapper.selectById(b.getAstId());
                //回写卡片锁定状态
                astCard.setInitialState("1");
                astCardList.add(astCard);
            });
        });
        astCardService.updateBatchById(astCardList);
        this.updateBatchById(astAstchangeBills);
        return "success";
    }

    /**
     * 单据编号
     *
     * @param id
     */
    public void checkId(String[] id) {
        if (ArrayUtil.isEmpty(id)) {
            throw new CustomException("单据编号不能为空");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String astSubmit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAstchangeBill> astAstchangeBills = this.listByIds(Arrays.asList(ids));
        if (CollectionUtil.isEmpty(astAstchangeBills)) {
            throw new CustomException("未查询到变更单");
        }
        List<String> billStatusList = astAstchangeBills.stream().map(AstAstchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astAstchangeBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_ASTCHANGE_CODE, processInstanceId);
            v.setProcessinstid(processInstanceId);
            v.setStatus(ProcessConstant.STATUS_1);
            // 设置流程变量
            Map<String, Object> startVariables = TaskUtils.getStartVariables(commonProcessRequest, v);
            // 执行任务
            List<Task> complete = TaskUtils.complete(processInstanceId, startVariables);
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
        });
        this.updateBatchById(astAstchangeBills);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String astAudit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstAstchangeBill> astAstchangeBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtils.isEmpty(astAstchangeBills)) {
            throw new CustomException("未查询到变更单");
        }
        // 校验单据状态
        List<String> billStatusList = astAstchangeBills.stream().map(AstAstchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        astAstchangeBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            // 执行任务
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(), TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // 判断流程是否结束
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_2);
                v.setApprovalPost(ProcessConstant.END);
                v.setVerificationDate(new Date());
                List<AstCard> astCardList = new ArrayList<>();
                astAstchangeBills.forEach(a -> {
                    a.setVerificationDate(new Date());
                    QueryWrapper<AstAstchangeList> astAstchangeListQueryWrapper = new QueryWrapper<>();
                    //查询明细信息
                    astAstchangeListQueryWrapper.lambda().eq(AstAstchangeList::getBillId, a.getId());
                    List<AstAstchangeList> astchangeLists = astAstchangeListService.list(astAstchangeListQueryWrapper);
                    //回写资产卡片
                    astchangeLists.forEach(b -> {
                        AstCard astCard = astCardMapper.selectById(b.getAstId());
                        astCard.setInitialState("1");
                        //原值
                        if (ObjectUtil.isNotNull(b.getCostAfter())) {
                            astCard.setCost(b.getCostAfter());
                        }
                        //累计折旧
                        if (ObjectUtil.isNotNull(b.getDepAfterTotal())) {
                            astCard.setDepTotal(b.getDepAfterTotal());
                        }
                        //取得日期
                        if (ObjectUtil.isNotNull(b.getAddAfterDate())) {
                            astCard.setAddDate(b.getAddAfterDate());
                        }
                        //折旧方法
                        if (StringUtils.isNotEmpty(b.getDepAfterMethod())) {
                            astCard.setDepMethod(b.getDepAfterMethod());
                        }
                        //设计使用月份
                        if (ObjectUtil.isNotNull(b.getUseAfterLife())) {
                            astCard.setUseLife(b.getUseAfterLife());
                        }
                        //资产类别
                        if (StringUtils.isNotEmpty(b.getCategoryAfterId())) {
                            astCard.setCategoryId(b.getCategoryAfterId());
                            astCard.setCategoryCode(b.getCategoryAfterCode());
                            astCard.setCategoryName(b.getCategoryAfterName());
                            //查询资产归类
                            BdAssetType bdAssetType = bdAssetTypeService.getById(b.getCategoryAfterId());
                            //查询资产大类
                            SysDictData sysDictData = sysDictDataServiceI.selectDictDataById(bdAssetType.getAssetType());
                            astCard.setTypeId(sysDictData.getDictCode());
                            astCard.setTypeCode(sysDictData.getDictValue());
                            astCard.setTypeName(sysDictData.getDictLabel());
                            //查询资产门类
                            SysDictData sysDictData1 = sysDictDataServiceI.selectDictDataById(bdAssetType.getClassification());
                            astCard.setClassCode(sysDictData1.getDictCode());
                            astCard.setClassName(sysDictData1.getDictLabel());
                        }
                        //存放地点
                        if (StringUtils.isNotEmpty(b.getPlaceAfterId())) {
                            astCard.setPlaceId(b.getPlaceAfterId());
                            astCard.setPlaceCode(b.getPlaceAfterCode());
                            astCard.setPlaceName(b.getPlaceAfterName());
                        }
                        astCardList.add(astCard);
                    });
                });
                astCardService.updateBatchById(astCardList);
            }
        });
        this.updateBatchById(astAstchangeBills);
        return "success";
    }

    @Override
    public String astBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstAstchangeBill> astAstchangeBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astAstchangeBills.stream().map(AstAstchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astAstchangeBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astAstchangeBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astAstchangeBills);
        return "success";
    }

    @Override
    public String taskBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstAstchangeBill> astAstchangeBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astAstchangeBills.stream().map(AstAstchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astAstchangeBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astAstchangeBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astAstchangeBills);
        return "success";
    }

    @Override
    public List<AstAstchangeBill> selectAll(AstAstchangeBill astAstchangeBill) {
        String status = StringUtils.isEmpty(astAstchangeBill.getStatus()) ? "" : astAstchangeBill.getStatus();
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astAstchangeBill.setDeptIds(idsMap.get("dept"));
        }
        astAstchangeBill.setDelFlag("0");
        List<AstAstchangeBill> astAstchangeBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            //草稿
            case "0":
                astAstchangeBill.setCreateBy(SecurityUtils.getUsername());
                astAstchangeBills = this.baseMapper.selectAll(astAstchangeBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astAstchangeBill.setPiids(runTaskByUserNameAndStatus);
                    astAstchangeBills = this.baseMapper.selectAll(astAstchangeBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astAstchangeBill.setPiids(runTaskByUserNameAndStatus);
                    astAstchangeBills = this.baseMapper.selectAll(astAstchangeBill);
                }
                break;
            // 终审
            case "3":
                astAstchangeBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astAstchangeBills = this.baseMapper.selectAll(astAstchangeBill);
                break;
            // 全部
            case "":
                astAstchangeBill.setCreateBy(SecurityUtils.getUsername());
                astAstchangeBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astAstchangeBills = this.baseMapper.selectAll(astAstchangeBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astAstchangeBills;
    }

    @Override
    public List<SelectAstAstChangeListVo> selectAstAstChangeList(String astId) {
        List<SelectAstAstChangeListVo> selectAstAstChangeListVos = new ArrayList<>();
        if (StringUtils.isNotEmpty(astId)) {
            //判断是否有使用人变更信息
            QueryWrapper<AstEmpchangeList> astEmpchangeListQueryWrapper = new QueryWrapper<>();
            astEmpchangeListQueryWrapper.lambda().eq(AstEmpchangeList::getAstId, astId);
            List<AstEmpchangeList> astEmpchangeLists = astEmpchangeListService.list(astEmpchangeListQueryWrapper);
            if (ObjectUtil.isNotNull(astEmpchangeLists)) {
                astEmpchangeLists.forEach(a -> {
                    //查询使用人变更主表信息
                    AstEmpchangeBill astEmpchangeBill = astEmpchangeBillService.getById(a.getBillId());
                    //判断是否有使用部门变动
                    if (StringUtils.isNotEmpty(a.getDepartmentAfterId())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astEmpchangeBill.getEmpchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("使用部门");
                        selectAstAstChangeListVo.setAstchangeBefore(a.getDepartmentBeforeName());
                        selectAstAstChangeListVo.setAstchangeAfter(a.getDepartmentAfterName());
                        selectAstAstChangeListVo.setVerificationDate(astEmpchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astEmpchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astEmpchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                    //判断是否有使用人变动
                    if (StringUtils.isNotEmpty(a.getEmpAfterId())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astEmpchangeBill.getEmpchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("使用人");
                        selectAstAstChangeListVo.setAstchangeBefore(a.getEmpBeforeName());
                        selectAstAstChangeListVo.setAstchangeAfter(a.getEmpAfterName());
                        selectAstAstChangeListVo.setVerificationDate(astEmpchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astEmpchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astEmpchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                });
            }
            //判断是否有资产变动信息
            QueryWrapper<AstAstchangeList> astAstchangeListQueryWrapper = new QueryWrapper<>();
            astAstchangeListQueryWrapper.lambda().eq(AstAstchangeList::getAstId, astId);
            List<AstAstchangeList> astchangeLists = astAstchangeListService.list(astAstchangeListQueryWrapper);
            if (ObjectUtil.isNotNull(astchangeLists)) {
                astchangeLists.forEach(b -> {
                    //查询主表信息
                    AstAstchangeBill astAstchangeBill = this.getById(b.getBillId());
                    //判断是否有原值变动
                    if (ObjectUtil.isNotNull(b.getCostAfter())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astAstchangeBill.getAstchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("原值");
                        selectAstAstChangeListVo.setAstchangeBefore(b.getCostBefore().toString());
                        selectAstAstChangeListVo.setAstchangeAfter(b.getCostAfter().toString());
                        selectAstAstChangeListVo.setVerificationDate(astAstchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astAstchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astAstchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                    //判断是否有累计折旧变动
                    if (ObjectUtil.isNotNull(b.getDepAfterTotal())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astAstchangeBill.getAstchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("累计折旧");
                        selectAstAstChangeListVo.setAstchangeBefore(b.getDepBeforeTotal().toString());
                        selectAstAstChangeListVo.setAstchangeAfter(b.getDepAfterTotal().toString());
                        selectAstAstChangeListVo.setVerificationDate(astAstchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astAstchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astAstchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                    //判断是否有取得日期变动
                    if (ObjectUtil.isNotNull(b.getAddAfterDate())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astAstchangeBill.getAstchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("取得日期");
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                        if (null != b.getAddBeforeDate()) {
                            selectAstAstChangeListVo.setAstchangeBefore(sf.format(b.getAddBeforeDate()));
                        }
                        selectAstAstChangeListVo.setAstchangeAfter(sf.format(b.getAddAfterDate()));
                        selectAstAstChangeListVo.setVerificationDate(astAstchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astAstchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astAstchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                    //判断是否有折旧方法变动
                    if (StringUtils.isNotEmpty(b.getDepAfterMethod())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astAstchangeBill.getAstchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("折旧方法");
                        selectAstAstChangeListVo.setAstchangeBefore(b.getDepBeforeMethod());
                        selectAstAstChangeListVo.setAstchangeAfter(b.getDepAfterMethod());
                        selectAstAstChangeListVo.setVerificationDate(astAstchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astAstchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astAstchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                    //判断是否有设计使用月份变动
                    if (ObjectUtil.isNotNull(b.getUseAfterLife())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astAstchangeBill.getAstchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("预计使用月份");
                        selectAstAstChangeListVo.setAstchangeBefore(b.getUseBeforeLife().toString());
                        selectAstAstChangeListVo.setAstchangeAfter(b.getUseAfterLife().toString());
                        selectAstAstChangeListVo.setVerificationDate(astAstchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astAstchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astAstchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                    //判断是否有资产类别变动
                    if (ObjectUtil.isNotNull(b.getCategoryAfterId())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astAstchangeBill.getAstchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("资产类别");
                        selectAstAstChangeListVo.setAstchangeBefore(b.getCategoryBeforeName());
                        selectAstAstChangeListVo.setAstchangeAfter(b.getCategoryAfterName());
                        selectAstAstChangeListVo.setVerificationDate(astAstchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astAstchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astAstchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                    //判断是否有存放地点变动
                    if (ObjectUtil.isNotNull(b.getPlaceAfterId())) {
                        SelectAstAstChangeListVo selectAstAstChangeListVo = new SelectAstAstChangeListVo();
                        selectAstAstChangeListVo.setAstchangeCode(astAstchangeBill.getAstchangeCode());
                        selectAstAstChangeListVo.setAstchangeType("存放地点");
                        selectAstAstChangeListVo.setAstchangeBefore(b.getPlaceBeforeName());
                        selectAstAstChangeListVo.setAstchangeAfter(b.getPlaceAfterName());
                        selectAstAstChangeListVo.setVerificationDate(astAstchangeBill.getVerificationDate());
                        selectAstAstChangeListVo.setVerificationReason(astAstchangeBill.getVerificationReason());
                        selectAstAstChangeListVo.setCreateBy(astAstchangeBill.getCreateName());
                        selectAstAstChangeListVos.add(selectAstAstChangeListVo);
                    }
                });

            }
        }
        return selectAstAstChangeListVos;
    }


}

