package com.copsis.config;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {
	
	private String defaultInstance;
	private String defaultDB;
	
	public TenantRoutingDataSource(String defaultInstance, String defaultDB) {
		this.defaultInstance = defaultInstance;
		this.defaultDB = defaultDB;
	}

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantStorage.getCurrentTenantInstance();
    }
    
    @Override
    public Connection getConnection() throws SQLException {
    	Object obj = determineCurrentLookupKey();
		if(obj == null) {
			TenantStorage.setCurrentTenantDb(defaultDB);
			TenantStorage.setCurrentTenantInstance(defaultInstance);
		}
    	// All Auto-generated method stub
    	Connection connection = super.getConnection();
    	connection.setCatalog(TenantStorage.getCurrentTenantDb());
    	return connection;
    }
}