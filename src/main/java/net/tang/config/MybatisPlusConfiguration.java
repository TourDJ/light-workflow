package net.tang.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("net.tang.**.mapper.**")
public class MybatisPlusConfiguration {

}
