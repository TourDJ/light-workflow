package net.tang.workflow.page;

import java.io.Serializable;

import lombok.Data;

@Data
public class PageQuery implements Serializable {

	private static final long serialVersionUID = 3863967310591896916L;

	/**
     * 当前页
     */
    private Integer current = 1;
    
    /**
     * 每页的数量
     */
    private Integer size = 10;

}
