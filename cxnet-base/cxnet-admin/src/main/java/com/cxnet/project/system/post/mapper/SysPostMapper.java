package com.cxnet.project.system.post.mapper;

import java.util.List;

import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.post.domain.SysPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

/**
 * 岗位信息 数据层
 *
 * @author cxnet
 */
public interface SysPostMapper {
    /**
     * 查询岗位数据集合
     *
     * @param post 岗位信息
     * @return 岗位数据集合
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
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    public List<SysPost> selectPostsByUserName(String userName);

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public int deletePostById(@Param("postId") String postId, @Param("affilUnitCode") String affilUnitCode);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    public int deletePostByIds(String[] postIds);

    /**
     * 修改岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int updatePost(SysPost post);

    /**
     * 新增岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int insertPost(SysPost post);

    /**
     * 校验岗位名称
     *
     * @param postName 岗位名称
     * @return 结果
     */
    public SysPost checkPostNameUnique(String postName);

    /**
     * 校验岗位编码
     *
     * @param postCode 岗位编码
     * @return 结果
     */
    public SysPost checkPostCodeUnique(String postCode);


    /**
     * 批量新增岗位信息
     *
     * @param posts 岗位信息
     * @return 结果
     */
    public int insertBatchPost(List<SysPost> posts);


    Integer checkPostCodeUniqueByDeptCode(@Param("postCode") String postCode, @Param("postId") String postId, @Param("deptCode") String deptCode, @Param("affilUnitCode") String affilUnitCode);

    /**
     * 查询岗位
     *
     * @param postCode      岗位编码
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     */
    SysPost selectOnePost(@Param("postCode") String postCode, @Param("deptCode") String deptCode, @Param("affilUnitCode") String affilUnitCode);
}
