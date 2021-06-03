package com.cxnet.project.system.post.service;

import java.util.List;

import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.post.domain.SysPost;
import com.cxnet.project.system.user.domain.SysUserPost;

/**
 * 岗位信息 服务层
 *
 * @author cxnet
 */
public interface SysPostServiceI {
    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位列表
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    public List<SysPost> selectPostAll();

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    public SysPost selectPostById(String postId);

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    public List<Long> selectPostListByUserId(String userId);

    /**
     * 校验岗位名称
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostNameUnique(SysPost post);

    /**
     * 校验岗位编码
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostCodeUnique(SysPost post);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public Long countUserPostById(String postId);

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public int deletePostById(String postId, String affilUnitCode);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deletePostByIds(String[] postIds);

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String insertPost(SysPost post);

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int updatePost(SysPost post);

    /**
     * 批量新增岗位信息
     *
     * @param posts 岗位信息
     * @return 结果
     */
    public int insertBatchPost(List<SysPost> posts);

    /**
     * 删除岗位人员信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public int deleteUserPostByPostId(String postId);

//    /**
//     * 新增岗位人员信息
//     *
//     * @param sysUserPost 岗位信息
//     * @return 结果
//     */
//    public int addUserPost(SysUserPost sysUserPost);

    /**
     * 批量新增用户岗位信息
     *
     * @param userPostList 用户角色列表
     * @return 结果
     */
    public int batchUserPost(List<SysUserPost> userPostList);


    /**
     * 查询岗位
     *
     * @param postCode      岗位编码
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     */
    SysPost selectOnePost(String postCode, String deptCode, String affilUnitCode);
}
