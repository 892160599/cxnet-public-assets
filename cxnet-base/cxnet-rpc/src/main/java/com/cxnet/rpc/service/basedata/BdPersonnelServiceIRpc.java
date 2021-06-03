package com.cxnet.rpc.service.basedata;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门人员Service接口
 *
 * @author sunsw
 * @date 2020-08-18
 */
@Service
public interface BdPersonnelServiceIRpc extends IService<BdPersonnel> {
    /**
     * 查询部门人员
     *
     * @param bdPersonnel 部门人员对象
     * @return
     */
    BdPersonnel selectBdPersonnelByPer(BdPersonnel bdPersonnel);

    /**
     * 查询部门人员
     *
     * @param personnelId 部门人员ID
     * @return 部门人员
     */
    BdPersonnel selectBdPersonnelById(String personnelId);

    /**
     * 查询部门人员集合
     *
     * @param bdPersonnel 部门人员
     * @return 部门人员集合
     */
    List<BdPersonnel> selectBdPersonnelList(BdPersonnel bdPersonnel);

    /**
     * 新增部门人员
     *
     * @param bdPersonnel 部门人员
     * @return 结果
     */
    int insertBdPersonnel(BdPersonnel bdPersonnel);

    /**
     * 批量新增部门人员
     *
     * @param bdPersonnels 部门人员
     * @return 结果
     */
    int insertBatchBdPersonnel(List<BdPersonnel> bdPersonnels);

    /**
     * 修改部门人员
     *
     * @param bdPersonnel 部门人员
     * @return 结果
     */
    int updateBdPersonnel(BdPersonnel bdPersonnel);

    /**
     * 批量修改部门人员
     *
     * @param bdPersonnels 部门人员
     * @return 结果
     */
    int updateBatchBdPersonnel(List<BdPersonnel> bdPersonnels);

    /**
     * 批量删除部门人员
     *
     * @param personnelIds 需要删除的部门人员ID
     * @return 结果
     */
    int deleteBdPersonnelByIds(String[] personnelIds);

    /**
     * 删除部门人员信息
     *
     * @param personnelId 部门人员ID
     * @return 结果
     */
    int deleteBdPersonnelById(String personnelId);

    /**
     * 删除部门人员信息 （逻辑删除）
     *
     * @param personnelId
     * @return
     */
    int deleteBdPersonnel(String personnelId);


    /**
     * 批量查询是否有相同人员编码
     *
     * @param bdPersonnels 部门人员集合
     * @return 结果
     */
    String checkImportByUserCode(List<BdPersonnel> bdPersonnels);

    /**
     * 引入部门人员
     *
     * @param bdPersonnels@return
     */
    int importPerson(List<BdPersonnel> bdPersonnels);

    /**
     * 停用部门人员
     *
     * @param bdPersonnel
     * @return
     */
    int stopPerson(BdPersonnel bdPersonnel);
}
