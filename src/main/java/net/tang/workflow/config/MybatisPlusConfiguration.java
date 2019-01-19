package net.tang.workflow.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;

@Configuration
@MapperScan("net.tang.**.mapper.**")
public class MybatisPlusConfiguration {

	/**
	 * 
	 * @return
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({ConfigConstant.ENV_DEV, ConfigConstant.ENV_TEST})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}
