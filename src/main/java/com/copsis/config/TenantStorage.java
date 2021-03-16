package com.copsis.config;

public class TenantStorage {
    private static ThreadLocal<String> currentTenantInstance = new ThreadLocal<>();
    private static ThreadLocal<String> currentTenantDb = new ThreadLocal<>();

    private TenantStorage() {
    	throw new IllegalStateException("Utility class");
    }
    
    public static void setCurrentTenantInstance(String instance) {
		currentTenantInstance.set(instance);
	}
    
    public static String getCurrentTenantInstance() {
		return currentTenantInstance.get();
	}
    
    public static void setCurrentTenantDb(String db) {
		currentTenantDb.set(db);
	}

	public static String getCurrentTenantDb() {
		return currentTenantDb.get();
	}

	public static void clear() {
        currentTenantInstance.remove();
        currentTenantDb.remove();
    }
}
