package com.example.testoptimizationstudytest.config.db_seperate;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class DataSourceConfiguration {

    private final JpaProperties jpaProperties;

    public DataSourceConfiguration(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Primary
    @Bean
    public DataSource testDataSource(
        @Qualifier("testRoutingDataSource") DataSource testRoutingDataSource) {
        return new LazyConnectionDataSourceProxy(testRoutingDataSource);
    }

    @Bean
    public DataSource testRoutingDataSource(
        @Qualifier("readDataSource") DataSource readDataSource,
        @Qualifier("writeDataSource") DataSource writeDataSource,
        DataSourceSelector dataSourceSelector
    ) {
        ReplicationRoutingDataSource testRoutingDataSource
            = new ReplicationRoutingDataSource(dataSourceSelector);

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", writeDataSource);
        dataSourceMap.put("read", readDataSource);

        testRoutingDataSource.setTargetDataSources(dataSourceMap);
        testRoutingDataSource.setDefaultTargetDataSource(writeDataSource);

        return testRoutingDataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-read")
    public DataSource readDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().build();
    }
}