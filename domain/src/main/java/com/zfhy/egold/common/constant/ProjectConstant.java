package com.zfhy.egold.common.constant;


public final class ProjectConstant {


    
    public static final String BASE_PACKAGE = "com.zfhy.egold";

    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".domain.%s.entity";
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".domain.%s.dao";
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".domain.%s.service";
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".api.web.%s";
    public static final String DTO_PACKAGE = BASE_PACKAGE + ".domain.%s.dto";
    public static final String WEB_CONTROLLER_PACKAGE = BASE_PACKAGE + ".wap.web.%s";
    public static final String ADMIN_CONTROLLER_PACKAGE = BASE_PACKAGE + ".admin.web.%s";




    
    

    
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".common.core.Mapper";
    

    
    public final static String SESSION_VALID_CODE = "validCode";
    
    public final static String SESSION_MENUS= "sessionMenus";

}
