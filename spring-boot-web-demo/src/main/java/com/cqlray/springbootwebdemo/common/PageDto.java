package com.cqlray.springbootwebdemo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PageDto<T> {
    private Long page;
    private Long total;
    private List<T> rows;

    public static <T> PageDto<T> of(Long page, Long total, List<T> rows){
        return new PageDto<T>(page, total, rows);
    }
}
