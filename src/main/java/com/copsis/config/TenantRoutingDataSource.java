package com.copsis.config;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantStorage.getCurrentTenantInstance();
    }
    
    @Override
    public Connection getConnection() throws SQLException {
    	// All Auto-generated method stub
    	Connection connection = super.getConnection();
    	connection.setCatalog(TenantStorage.getCurrentTenantDb());
    	return connection;
    }
}