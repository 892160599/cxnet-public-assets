package com.cxnet.flow.model.mapper;

import com.cxnet.common.utils.tree.Zone;
import com.cxnet.rpc.domain.SysDbYb;
import com.cxnet.flow.model.domain.SysModel;
import com.cxnet.flow.model.domain.SysModelBill;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.rpc.domain.system.deptrpc.SysDeptRpc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 系统模块管理Mapper接口
 *
 * @author caixx
 * @date 2020-08-12
 */
public interface SysModelMapper {
    /**
     * 查询系统模块管理
     *
     * @param modelId 系统模块管理ID
     * @return 系统模块管理
     */
    public SysModel selectSysModelById(String modelId);

    /**
     * 查询系统模块管理集合
     *
     * @param sysModel 系统模块管理
     * @return 系统模块管理集合
     */
    public List<SysModel> selectSysModelList(SysModel sysModel);

    /**
     * 查询系统模块业务单据集合
     *
     * @param modelId 系统模块管理
     * @return 系统业务单据集合
     */
    public List<SysModelBill> selectModelBill(String modelId);

    /**
     * 查询系统模块管理tree
     *
     * @param sysModel 查询系统模块管理tree
     * @return 查询系统模块管理tree
     */
    public List<Zone> selectSysModelTree(SysModel sysModel);

    /**
     * 新增系统模块管理
     *
     * @param sysModel 系统模块管理
     * @return 结果
     */
    public int insertSysModel(SysModel sysModel);

    /**
     * 批量新增系统模块管理
     *
     * @param sysModels 系统模块管理
     * @return 结果
     */
    public int insertBatchSysModel(List<SysModel> sysModels);

    /**
     * 修改系统模块管理
     *
     * @param sysModel 系统模块管理
     * @return 结果
     */
    public int updateSysModel(SysModel sysModel);

    /**
     * 批量修改系统模块管理
     *
     * @param sysModels 系统模块管理
     * @return 结果
     */
    public int updateBatchSysModel(List<SysModel> sysModels);

    /**
     * 删除系统模块管理
     *
     * @param modelId 系统模块管理ID
     * @return 结果
     */
    public int deleteSysModelById(String modelId);

    /**
     * 批量删除系统模块管理
     *
     * @param modelIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysModelByIds(String[] modelIds);

    /**
     * 查询单据绑定的流程key
     *
     * @param billTypeCode
     * @return
     */
    String selectProcessKeyByModelCode(String billTypeCode);

    /**
     * 根据单据类型和单位id查询单据绑定的流程key
     *
     * @param billTypeCode 单据类型
     * @param unitId       单位id
     * @return 流程key
     */
    String selectProcessKeyByModelCodeAndUnitId(@Param("billTypeCode") String billTypeCode, @Param("unitId") String unitId);


    /**
     * 查询单据编号器编码
     *
     * @param billTypeCode
     * @return
     */
    String selectRuleCodeByModelCode(String billTypeCode);

    /**
     * 查询单据附件类型值集编码
     *
     * @param billTypeCode
     * @return
     */
    String selectDictTypeCodeByModelCode(String billTypeCode);

    /**
     * 根据编码查询
     *
     * @param billTypeCode
     * @return
     */
    SysModel selectSysModelByBillTypeCode(String billTypeCode);

    /**
     * 查询同单位用户
     *
     * @param unitId 单位id
     * @return list
     */
    List<String> selectUserNamesByUnitId(String unitId);

    /**
     * 查询同部门用户
     *
     * @param unitId 单位id
     * @param deptId 部门id
     * @return list
     */
    List<String> getUsersByUnitIdAndDeptId(@Param("deptId") String deptId);

    /**
     * 查询任务组
     *
     * @param tasks
     * @return
     */
    @Select("select group_id_ from ACT_RU_IDENTITYLINK where task_id_ in ('${taskIds}') and group_id_ is not null group by group_id_")
    List<String> selectGroupIdList(@Param("taskIds") String tasks);

    /**
     * 查询任务所有候选人及待办人
     *
     * @param tasks
     * @return
     */
    @Select("select t0.* from SYS_USER t0\n" +
            "            inner join (select user_id_ from ACT_RU_IDENTITYLINK where task_id_ in ('${taskIds}') and user_id_ is not null group by\n" +
            " user_id_\n" +
            " UNION \n" +
            " select ASSIGNEE_ from act_ru_task where id_ in ('${taskIds}') and ASSIGNEE_ is not null group by ASSIGNEE_\n" +
            " ) t1\n" +
            "            on t0.user_name = t1.user_id_\n" +
            "            where t0.DEL_FLAG = '0'")
    List<SysUser> selectUserCandidateList(@Param("taskIds") String tasks);

    /**
     * 查询已办
     *
     * @param userAccount
     * @return
     */
    List<SysDbYb> selectActiviDone(@Param("userAccount") String userAccount,
                                   @Param("modelName") String modelName,
                                   @Param("startTime") Date startTime,
                                   @Param("endTime") Date endTime,
                                   @Param("searchValue") String searchValue,
                                   @Param("deptCode") String deptCode,
                                   @Param("billNo") String billNo,
                                   @Param("minAmt") BigDecimal minAmt,
                                   @Param("maxAmt") BigDecimal maxAmt,
                                   @Param("status") String status,
                                   @Param("parentPath") String parentPath);

    /**
     * 查询任务所有待办人
     *
     * @param tasks
     * @return
     */
    @Select("select t0.* from SYS_USER t0\n" +
            "            inner join (select ASSIGNEE_ from act_ru_task where id_ in ('${taskIds}') and ASSIGNEE_ is not null group by ASSIGNEE_) t1\n" +
            "            on t0.user_name = t1.ASSIGNEE_\n" +
            "            where t0.DEL_FLAG = '0'")
    List<SysUser> selectUserList(@Param("taskIds") String tasks);

    /**
     * 根据modelCode查找路径
     *
     * @param modeCode
     * @return
     */
    @Select("select t.path from sys_menu t where t.model_code=#{modeCode}")
    String selectPatchByModeCode(@Param("modeCode") String modeCode);

    /**
     * 查询角色用户
     *
     * @param roles
     * @return
     */
    @Select("select user_name from sys_user \n" +
            "where user_id in (\n" +
            "\tSELECT user_id FROM SYS_USER_ROLE\n" +
            "\twhere role_id in (\n" +
            "\t\tselect role_id from sys_role\n" +
            "\t\twhere role_key in ('${roles}')\n" +
            "\t\tand del_flag = 0\n" +
            "\t\tand status = 0\n" +
            "\t)\n" +
            ")\n" +
            "group by user_name")
    List<String> getUsersByRoles(@Param("roles") String roles);

    /**
     * 查询退回类型
     *
     * @return
     */
    @Select("SELECT value FROM EXP_BASIC_CONTROL \n" +
            "where code = 'doc_back_method'")
    String selectByCode();

    /**
     * 查询流程历史节点
     *
     * @param piid
     * @return
     */
    @Select("select TASK_DEF_KEY from (\n" +
            "SELECT TASK_DEF_KEY_ TASK_DEF_KEY, min(START_TIME_) START_TIME FROM ACT_HI_TASKINST\n" +
            "where PROC_INST_ID_ = #{piid}\n" +
            "GROUP BY TASK_DEF_KEY_)\n" +
            "order by START_TIME")
    List<String> selectTaskDefKeyOrder(@Param("piid") String piid);

    /**
     * 查询待办
     *
     * @param userAccount
     * @param modelName
     * @param startTime
     * @param endTime
     * @return
     */
    List<SysDbYb> selectAcivitiTodo(@Param("userAccount") String userAccount,
                                    @Param("modelName") String modelName, @Param("startTime") Date startTime,
                                    @Param("endTime") Date endTime,
                                    @Param("searchValue") String searchValue,
                                    @Param("deptCode") String deptCode,
                                    @Param("billNo") String billNo,
                                    @Param("minAmt") BigDecimal minAmt,
                                    @Param("maxAmt") BigDecimal maxAmt,
                                    @Param("status") String status,
                                    @Param("parentPath") String parentPath);


    @Select("\n" +
            "SELECT RES.NAME_\n" +
            "                  FROM ACT_RU_TASK RES\n" +
            "                   where RES.PROC_INST_ID_ = #{processinstid}")
    String selectAssigerPostbyZb(@Param("processinstid") String processinstid);

    /**
     * 查询用户nick_name
     *
     * @param assignee
     * @return
     */
    @Select("select nick_name from SYS_USER where user_name = #{assignee} and DEL_FLAG = '0'")
    String getNickNameByUserName(@Param("assignee") String assignee);

    /**
     * 查询单位
     *
     * @param actDeptId
     * @return
     */
    @Select("select * from sys_dept where dept_id = #{actDeptId}")
    SysDeptRpc selectDeptById(String actDeptId);

    /**
     * 移动端查看我的发起
     *
     * @return
     */
    List<SysDbYb> mobileSelectMyselfStart(@Param("userName") String userName,
                                          @Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime,
                                          @Param("modelName") String modelName,
                                          @Param("deptCode") String deptCode,
                                          @Param("billNo") String billNo,
                                          @Param("minAmt") BigDecimal minAmt,
                                          @Param("maxAmt") BigDecimal maxAmt,
                                          @Param("status") String status,
                                          @Param("parentPath") String parentPath);

}
