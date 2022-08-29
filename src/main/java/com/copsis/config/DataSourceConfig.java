package com.copsis.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	
	@Value("${tenants.default-instance}")
	private String defaultInstance;
	
	@Value("${tenants.default-db}")
	private String defaultDB;


    private final DataSourceProperties dataSourceProperties;

    public DataSourceConfig(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    public DataSource dataSource() {
        TenantRoutingDataSource customDataSource = new TenantRoutingDataSource(defaultInstance, defaultDB);
        customDataSource.setTargetDataSources(dataSourceProperties.getDataSources());
        return customDataSource;
    }
}