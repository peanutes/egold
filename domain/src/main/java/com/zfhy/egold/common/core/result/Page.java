package com.zfhy.egold.common.core.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
public class Page<T> implements Serializable {
    
    
    private int pageNum;
    
    
    private int pageSize;
    
    
    private int size;
    
    
    private long total;
    
    
    private int pages;
    
    
    private List<T> list;
    
    
    private int prePage;
    
    
    private int nextPage;
    
    
    private boolean hasPreviousPage = false;
    
    
    private boolean hasNextPage = false;


    public static <K> Page<K> newPage(List<K> list) {
        Page<K> page = new Page<>();
        page.setList(list);

        return page;
    }


}
