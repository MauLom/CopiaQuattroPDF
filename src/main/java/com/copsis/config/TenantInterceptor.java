package com.copsis.config;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
public class TenantInterceptor implements WebRequestInterceptor {
    private static final String INSTANCE_TENANT_HEADER = "X-Tenant-Instance";
    private static final String DB_TENANT_HEADER = "X-Tenant-Db";

    @Override
    public void preHandle(WebRequest request) {
    	if(request.getHeader(INSTANCE_TENANT_HEADER) != null) {
    		TenantStorage.setCurrentTenantInstance(request.getHeader(INSTANCE_TENANT_HEADER));	
    	}
    	if(request.getHeader(DB_TENANT_HEADER) != null) {
    		TenantStorage.setCurrentTenantDb(request.getHeader(DB_TENANT_HEADER));	
    	}
    }

    @Override
    public void postHandle(WebRequest webRequest, ModelMap modelMap) {
        TenantStorage.clear();
    }

    @Override
    public void afterCompletion(WebRequest webRequest, Exception e) {
    }

}