package com.cxnet.asset.card.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.domain.dto.AstCardDTO;
import com.cxnet.asset.card.domain.vo.AstCardVO;
import com.cxnet.project.system.dept.domain.SysDept;
import com.google.zxing.NotFoundException;

import javassist.CannotCompileException;

/**
 * 资产主表(AstCard)表服务接口
 *
 * @author guks
 * @since 2021-03-29 10:23:01
 */
public interface AstCardService extends IService<AstCard> {

    /**
     * 保存资产对象
     *
     * @param astCardVO 资产对象
     * @return
     */
    String saveAstCardVo(AstCardVO astCardVO);


    /**
     * 修改资产对象
     *
     * @param astCardVO 资产对象
     * @return
     */
    String updateAstCardVo(AstCardVO astCardVO);


    /**
     * 根据id查询资产信息
     *
     * @param id 主键id
     * @return
     */
    AstCardVO getAstCardVO(String id);


    /**
     * 获取使用部门列表
     */
    List<SysDept> getDepartmentList();


    /**
     * 获取资产列表
     */
    List<AstCard> getAstCardList(AstCardDTO astCardDTO);

}

