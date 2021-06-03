package com.cxnet.project.system.post.service;

import java.util.ArrayList;
import java.util.List;

import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.mapper.SysDeptMapper;
import com.cxnet.project.system.user.domain.SysUserPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.post.domain.SysPost;
import com.cxnet.project.system.post.mapper.SysPostMapper;
import com.cxnet.project.system.user.mapper.SysUserPostMapper;
import org.springframework.transaction.annotation.Transactional;

import static java.awt.SystemColor.info;

/**
 * 岗位信息 服务层处理
 *
 * @author cxnet
 */
@Service
public class SysPostServiceIImpl implements SysPostServiceI {
    @Autowired(required = false)
    private SysPostMapper postMapper;

    @Autowired(required = false)
    private SysUserPostMapper userPostMapper;

    @Autowired(required = false)
    private SysDeptMapper sysDeptMapper;


    /**
     * 新增用户岗位信息
     *
     * @param post 岗位对象
     */
    public void insertUserPost(SysPost post) {
        userPostMapper.deleteUserPostByPostId(post.getPostId());
        String[] userIds = post.getUserIds();
        if (StringUtils.isNotNull(userIds)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (String userId : userIds) {
                SysUserPost up = new SysUserPost();
                up.setUserId(userId);
                up.setPostId(post.getPostId());
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll() {
        return postMapper.selectPostAll();
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(String postId) {
        return postMapper.selectPostById(postId);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> selectPostListByUserId(String userId) {
        return postMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(SysPost post) {
        String postId = StringUtils.isNull(post.getPostId()) ? "-1" : post.getPostId();
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && !info.getPostId().equals(postId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(SysPost post) {
        String postId = StringUtils.isNull(post.getPostId()) ? "-1" : post.getPostId();
        Integer count = postMapper.checkPostCodeUniqueByDeptCode(post.getPostCode(), postId, post.getDeptCode(), post.getAffilUnitCode());
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public Long countUserPostById(String postId) {
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deletePostById(String postId, String affilUnitCode) {
        return postMapper.deletePostById(postId, affilUnitCode);
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public int deletePostByIds(String[] postIds) {
        for (String postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return postMapper.deletePostByIds(postIds);
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertPost(SysPost post) {
        postMapper.insertPost(post);
        insertUserPost(post);

        return post.getPostId();
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int updatePost(SysPost post) {
        insertUserPost(post);
        return postMapper.updatePost(post);
    }

    /**
     * 批量新增岗位信息
     *
     * @param posts 岗位信息
     * @return 结果
     */
    @Override
    public int insertBatchPost(List<SysPost> posts) {
        return postMapper.insertBatchPost(posts);
    }

    /**
     * 删除岗位人员信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deleteUserPostByPostId(String postId) {
        return userPostMapper.deleteUserPostByPostId(postId);
    }

    @Override
    public int batchUserPost(List<SysUserPost> userPostList) {
        return userPostMapper.batchUserPost(userPostList);
    }

    /**
     * 查询岗位
     *
     * @param postCode      岗位编码
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     */
    @Override
    public SysPost selectOnePost(String postCode, String deptCode, String affilUnitCode) {
        return postMapper.selectOnePost(postCode, deptCode, affilUnitCode);
    }

}
