package com.taco.backend_demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long total, Integer pageNum, Integer pageSize) {
        int pages = (int) Math.ceil((double) total / pageSize);
        return new PageResult<>(
            records,
            total,
            pageNum,
            pageSize,
            pages,
            pageNum > 1,
            pageNum < pages
        );
    }
}
