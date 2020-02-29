package com.neusoft.course.schedule.utils;

import com.github.pagehelper.PageInfo;
import com.neusoft.course.schedule.entity.PageEntity;
import com.neusoft.course.schedule.entity.PageResult;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/15.
 */
public class PageUtils {

    /**
     * 将分页信息封装到统一的接口
     * @param pageInfo
     * @return
     */
    public static PageResult getPageResult(PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setData(pageInfo.getList());
        return pageResult;
    }

}
