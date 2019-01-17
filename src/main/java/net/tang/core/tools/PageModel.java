package net.tang.core.tools;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.Data;

/**
 * 分页
 * @author service1
 *
 */
@Data
public class PageModel implements Serializable {
	
	private static final long serialVersionUID = 3699815864487040479L;

	/**
	 * total records
	 */
	@TableField(exist=false)
	private Integer totals; 
	
	/**
	 * every records
	 */
	@TableField(exist=false)
	private Integer limit; 	 
	
	/**
	 * total pages
	 */
	@TableField(exist=false)
	private Integer pages; 	
	
	/**
	 * current page
	 */
	@TableField(exist=false)
	private Integer current;
	
	/**
	 * offset
	 */
	@TableField(exist=false)
	private Integer offset;

	/**
	 * 设置查询的偏移量
	 */
	public void setOffset() {
		if(current <= 0)
			current = 1;
		
		if(limit <= 0)
			limit = 10;
		
		offset = (current - 1) * limit;
		if(offset < 0)
			offset = 0;
	}
	
	/**
	 * 设置分页
	 * @param pageModel
	 * @param rows
	 * @param page
	 */
	public void assembling(Integer rows, Integer page) {
		if(rows != null && page != null) {
			this.setLimit(rows);
			this.setCurrent(page);
			this.setOffset();
		}
	}
	
	/**
	 * 审核人条件查询时设置分页
	 * @param pages 分页对象
	 * @param list 数据集合
	 * @param count 总记录数
	 */
	public <T> void setPages(IPage<T> pages, List<T> list, Integer count) {
		if(pages != null) {
			pages.setRecords(list);
			pages.setTotal(count);
			pages.setSize(this.getLimit());
			pages.setCurrent(this.getCurrent());
		}
	}
}
