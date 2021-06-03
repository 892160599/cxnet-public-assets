package com.cxnet.project.system.user.mapper;

import java.util.List;

import com.cxnet.project.system.user.domain.SysUserPost;

/**
 * 用户与岗位关联表 数据层
 *
 * @author cxnet
 */
public interface SysUserPostMapper {
    /**
     * 通过用户ID删除用户和岗位关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserPostByUserId(String userId);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public Long countUserPostById(String postId);

    /**
     * 批量删除用户和岗位关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserPost(String[] ids);

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户角色列表
     * @return 结果
     */
    public int batchUserPost(List<SysUserPost> userPostList);

    /**
     * 删除岗位人员信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public int deleteUserPostByPostId(String postId);
}
