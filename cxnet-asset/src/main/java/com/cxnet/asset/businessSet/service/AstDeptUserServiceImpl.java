package com.cxnet.asset.businessSet.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.businessSet.domain.AstDeptUser;
import com.cxnet.asset.businessSet.mapper.AstDeptUserMapper;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (AstDeptUser)表服务实现类
 *
 * @author zhangyl
 * @since 2021-03-29 10:39:24
 */
@Service
public class AstDeptUserServiceImpl extends ServiceImpl<AstDeptUserMapper, AstDeptUser> implements AstDeptUserService {

    @Autowired(required = false)
    private AstDeptUserMapper astDeptUserMapper;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelRpcServiceI;

    @Override
    public List<Zone> selectAstDeptTree(AstDeptUser astDeptUser) {
        List<Zone> zones = astDeptUserMapper.selectAstDeptTree(astDeptUser);
        List<Zone> zoneList = ZoneUtils.buildTree(zones);
        return zoneList;
    }

    @Override
    public Map<String, List<String>> getMap() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 根据登录用户的id查询部门人员的信息
        QueryWrapper<BdPersonnel> bdwapper = new QueryWrapper<>();
        bdwapper.lambda().eq(BdPersonnel::getUserId, user.getUserId()).eq(BdPersonnel::getDelFlag, "0");
        List<BdPersonnel> personnels = bdPersonnelRpcServiceI.list(bdwapper);
        Map<String, List<String>> map = this.getMap(personnels);
        return map;
    }

    @Override
    public Map<String, List<String>> getPerMap(String personnelId) {
        QueryWrapper<BdPersonnel> bdwapper = new QueryWrapper<>();
        bdwapper.lambda().eq(BdPersonnel::getPersonnelId, personnelId).eq(BdPersonnel::getDelFlag, "0");
        List<BdPersonnel> personnels = bdPersonnelRpcServiceI.list(bdwapper);
        Map<String, List<String>> map = this.getMap(personnels);
        return map;
    }

    private Map<String, List<String>> getMap(List<BdPersonnel> personnels) {
        Map<String, List<String>> map = new HashMap();
        // 根据部门人员的姓名查询数据
        if (CollectionUtils.isNotEmpty(personnels)) {
            QueryWrapper<AstDeptUser> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(AstDeptUser::getUserName, personnels.get(0).getUserName())
                    .eq(AstDeptUser::getStatus, "0").eq(AstDeptUser::getDelFlag, "0");
            List<AstDeptUser> list = this.list(wrapper);

            if (CollectionUtils.isNotEmpty(list)) {
                // 获取单位id
                List<String> unitList = list.stream().filter(v -> "0".equals(v.getIsUnitAdmin())).filter(v -> StringUtils.isNotBlank(v.getUnitId()))
                        .map(AstDeptUser::getUnitId).collect(Collectors.toList());

                // 获取部门id
                List<String> deptList = list.stream().filter(v -> "0".equals(v.getIsDeptAdmin())).filter(v -> StringUtils.isNotBlank(v.getDeptId()))
                        .map(AstDeptUser::getDeptId).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(unitList)) {
                    map.put("unit", unitList);
                } else if (CollectionUtils.isNotEmpty(deptList)) {
                    map.put("dept", deptList);
                }
            }
        }
        return map;
    }
}
