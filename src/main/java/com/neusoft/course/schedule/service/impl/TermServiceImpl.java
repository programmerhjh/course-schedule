package com.neusoft.course.schedule.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neusoft.course.schedule.dto.OpenTermApplyDTO;
import com.neusoft.course.schedule.dto.SearchTermDTO;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;
import com.neusoft.course.schedule.entity.Term;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.mapper.TermMapper;
import com.neusoft.course.schedule.service.TermService;
import com.neusoft.course.schedule.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/20.
 */
@Service
public class TermServiceImpl implements TermService {

    @Autowired
    private TermMapper termMapper;

    @Override
    public PageResult<Term> getTermData(PageEntity pageEntity, Integer fcId) {
        if (ObjectUtils.isEmpty(pageEntity.getPage())){
            PageResult<Term> termPageResult = new PageResult<>();
            List<Term> termData = termMapper.getTermDataByFcId(fcId);
            termPageResult.setData(termData);
            return termPageResult;
        }
        return PageUtils.getPageResult(getPageInfo(pageEntity, fcId));
    }

    @Override
    public List<Term> getTermDataByFcId(Integer fcId) {
        return termMapper.getTermDataByFcId(fcId);
    }

    @Override
    public Integer deleteTerm(Term term) {
        Integer row = termMapper.deleteTerm(term);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public Integer batchDeleteTerms(List<Integer> idList) {
        Integer row = termMapper.batchDeleteTerms(idList);
        // mybatis 批量事务操作的 BATCH 执行器无法获取事务操作条数
        return row;
    }

    @Override
    public PageResult<Term> searchTerm(SearchTermDTO searchTermDTO) {
        PageResult pageResult = PageUtils.getPageResult(getPageLikeInfo(searchTermDTO));
        return pageResult;
    }

    @Override
    public Term checkTermNameIsRepeat(Integer fcId, String name) {
        return termMapper.selectTermByName(fcId, name);
    }

    @Override
    public Integer saveTerm(Term term) {
        Integer row = termMapper.saveTerm(term);
        if (row <= 0){
            throw new RuntimeException(ResultCode.COMMON_FAIL.getMessage());
        }
        return row;
    }

    @Override
    public Integer openTermApply(OpenTermApplyDTO openTermApplyDTO) {
        return termMapper.updateStateByTermId(openTermApplyDTO);
    }

    @Override
    public Integer getTermApplyState(Integer id) {
        return termMapper.getStateByTermId(id);
    }

    /**
     * 调用分页插件完成分页
     * @param pageEntity
     * @return
     */
    private PageInfo<Term> getPageInfo(PageEntity pageEntity, Integer fcId) {
        int pageNum = pageEntity.getPage();
        int pageSize = pageEntity.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<Term> termList = termMapper.getTermDataByFcId(fcId);
        return new PageInfo<>(termList);
    }

    /**
     * 调用分页插件完成模糊搜索分页
     * @param searchTermDTO
     * @return
     */
    private PageInfo<Term> getPageLikeInfo(SearchTermDTO searchTermDTO) {
        int pageNum = searchTermDTO.getPage();
        int pageSize = searchTermDTO.getLimit();
        PageHelper.startPage(pageNum, pageSize);
        List<Term> terms = termMapper.searchTerm(searchTermDTO);
        return new PageInfo<>(terms);
    }
}
