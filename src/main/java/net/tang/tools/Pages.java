package net.tang.tools;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class Pages {

    public static <T> IPage<T> getPage() {
		Page<T> page = new Page<>(1, 10);
		return page;
    }
}
