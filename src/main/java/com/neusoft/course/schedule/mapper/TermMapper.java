package com.neusoft.course.schedule.mapper;

import com.neusoft.course.schedule.dto.OpenTermApplyDTO;
import com.neusoft.course.schedule.dto.SearchTermDTO;
import com.neusoft.course.schedule.entity.Term;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
public interface TermMapper {

    /**
     * 获取指定院系的学年列表
     * @param fcId
     * @return
     */
    List<Term> getTermDataByFcId(@Param(value = "fcId") Integer fcId);

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
    Integer batchDeleteTerms(@Param("idList") List<Integer> idList);

    /**
     * 模糊搜索对应院系学年
     * @param searchTermDTO
     * @return
     */
    List<Term> searchTerm(SearchTermDTO searchTermDTO);

    /**
     * 通过院系ID搜索对应所有学年
     * @param fcId
     * @param name
     * @return
     */
    Term selectTermByName(@Param(value = "fcId") Integer fcId, @Param(value = "name") String name);

    /**
     * 保存一个学年
     * @param term
     * @return
     */
    Integer saveTerm(Term term);

    /**
     * 获取学期申请状态
     * @param id 主键ID
     * @return
     */
    Integer getStateByTermId(@Param("id") Integer id);

    /**
     * 修改学期申请状态
     * @param openTermApplyDTO
     * @return
     */
    Integer updateStateByTermId(OpenTermApplyDTO openTermApplyDTO);
}
