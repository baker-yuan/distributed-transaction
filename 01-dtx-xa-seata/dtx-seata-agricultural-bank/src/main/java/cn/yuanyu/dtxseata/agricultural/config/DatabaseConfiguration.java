package cn.yuanyu.dtxseata.agricultural.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author yuanyu
 */
@Configuration
public class DatabaseConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.ds0")
    public DruidDataSource ds0() {
        return new DruidDataSource();
    }

    /**
     * TODO 数据源代理
     */
    @Bean
    @Primary
    public DataSource dataSource(DruidDataSource ds0)  {
        return new DataSourceProxy(ds0);
    }

}
