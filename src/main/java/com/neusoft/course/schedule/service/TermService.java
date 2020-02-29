package com.neusoft.course.schedule.service;

import com.neusoft.course.schedule.dto.OpenTermApplyDTO;
import com.neusoft.course.schedule.dto.SearchTermDTO;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.entity.Term;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/20.
 */
public interface TermService {

    /**
     * 分页获取学年信息
     * @param pageEntity 为null则返回全部数据
     * @return
     */
    PageResult<Term> getTermData(PageEntity pageEntity, Integer fcId);

    /**
     * 获取指定院系全部学年数据
     * @return
     */
    List<Term> getTermDataByFcId(Integer fcId);

    /**
     * 删除指定学年
     * @param term
     * @return
     */
    Integer deleteTerm(Term term);

    /**
     * 批量删除指定学年
     * @param idList
     * @return
     */
    Integer batchDeleteTerms(List<Integer> idList);

    /**
     * 模糊搜索key相关的学年
     * @param searchTermDTO
     * @return
     */
    PageResult<Term> searchTerm(SearchTermDTO searchTermDTO);

    /**
     * 验证学年名称是否重复
     * @param fcId
     * @param name
     * @return
     */
    Term checkTermNameIsRepeat(Integer fcId, String name);

    /**
     * 保存学年
     * @param term
     * @return
     */
    Integer saveTerm(Term term);

    /**
     * 根据学期主键ID修改申请状态
     * @param openTermApplyDTO
     * @return
     */
    Integer openTermApply(OpenTermApplyDTO openTermApplyDTO);

    /**
     * 根据主键获取学期状态
     * @param id
     * @return
     */
    Integer getTermApplyState(Integer id);

}
