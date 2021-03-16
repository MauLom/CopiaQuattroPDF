package com.copsis.config;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "tenants", ignoreInvalidFields=true, ignoreUnknownFields=true)
@Component
public class DataSourceProperties {

    private Map<Object, Object> dataSources = new LinkedHashMap<>();
    //[tenant1: datasource1
    // tenant2: darasource2
    //]

    public Map<Object, Object> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, Map<String, String>> datasources) {
        datasources.forEach((key, value) -> this.dataSources.put(key, convert(value)));
    }

    public DataSource convert(Map<String, String> source) {
    	return DataSourceBuilder.create()
                .url(source.get("jdbcUrl"))
                .driverClassName(source.get("driverClassName"))
                .username(source.get("username"))
                .password(source.get("password"))
                .build();
    }
}