package com.jkr.utils;

import com.jkr.common.constant.CommonConstant;
import lombok.Data;

/**
 * @author zhangnawien
 * 常量类
 */
@Data
public class Constant {


    /**分隔符 */
    public static final String COMMA_SYMBOL = ",";
    public static final String SPLIT_SYMBOL = "_";

    /**初始化密码 */
    public static final String INITIALIZATION_PASSWORD = "000000";

    /**企业角色 */
    public static final String ENTERPRISE_ROLE_KEY = "ROLE_ENTERPRISE";

    /**一级网络角色 */
    public static final String FIRST_GRID_ROLE_KEY = "ROLE_FIRST_GRID";

    /**二级网络角色 */
    public static final String SECOND_GRID_ROLE_KEY = "ROLE_SECOND_GRID";


    /**属地监管参数  */
    public static final String  POSITION_SUPERVISE = "unit_type_1";

    /**行业类别参数 */
    public static final String  BUSINESS_SUPERVISE = "unit_type_2";

    /**管理员标识 */
    public static final String  ADMIN = "admin";


}
