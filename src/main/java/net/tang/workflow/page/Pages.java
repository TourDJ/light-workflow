/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tang.workflow.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * Page class
 * @author tang
 *
 */
public final class Pages {

	private Pages() {
		
	}
	
	/**
	 * 
	 * @return
	 */
    public static <T> IPage<T> getPage() {
		return getPage(1, 10);
    }
   
    /**
     * 
     * @param pageQuery
     * @return
     */
    public static <T> IPage<T> getPage(PageQuery pageQuery) {
    	Objects.requireNonNull(pageQuery);
    	
		return getPage(pageQuery.getCurrent(), pageQuery.getSize());
    }
    
    /**
     * 
     * @param current
     * @param size
     * @return
     */
    public static <T> IPage<T> getPage(int current, int size) {
		return new Page<>(current, size);
    }
     
    /**
     * 
     * @param pageQuery
     * @param datas
     * @return
     */
	public static <T> IPage<T> paging(PageQuery pageQuery, List<T> datas) {
		Objects.requireNonNull(pageQuery);
		Objects.requireNonNull(datas);
		
		int current = pageQuery.getCurrent();
		int size = pageQuery.getSize();
		
		IPage<T> pages = getPage(current, size);
		
		List<T> currentPageData = new ArrayList<>();
		
		datas.stream()
			 .skip((current - 1 ) * size)
			 .limit(size)
			 .forEach(e -> currentPageData.add(e));
		
		pages.setRecords(currentPageData);
		pages.setTotal(datas.size());
		pages.setSize(size);
		pages.setCurrent(current);
		
		return pages;
	}
}
