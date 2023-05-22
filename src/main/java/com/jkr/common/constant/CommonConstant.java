package com.jkr.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @Author：jikeruan
 * @Description: 全局公共常量
 * @Date: 2019/9/6 13:36
 */
public interface CommonConstant {

    String ENCRYPT_PREFIX = "{bcrypt}";
    /**
     * token请求头名称
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * The access token issued by the authorization server. This value is REQUIRED.
     */
    String ACCESS_TOKEN = "access_token";

    String BEARER_TYPE = "Bearer ";
    /**
     * ROLE_ANONYMOUS 匿名权限
     */
    String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * 公共父级id
     */
    String DEFAULT_PARENT_VAL = "0";
    /**
     * 删除
     */
    String STATUS_DEL = "1";

    /**
     * 删除
     */
    Integer STATUS_DEL_NUMBER = 1;

    /**
     * 删除
     */
    Integer STATUS_NUMBER = 0;

    /**
     * 正常
     */
    String STATUS_NORMAL = "0";
    /**
     * 正常
     */
    Integer STATUS_NORMAL_NUMBER = 0;

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 目录
     */
    Integer CATALOG = -1;

    /**
     * 菜单
     */
    Integer MENU = 1;

    /**
     * 权限
     */
    Integer PERMISSION = 2;


    // auth-server 常量
    String SIGNING_KEY = "jikeruan!#$&*(!FGE";
    String CONTEXT_KEY_USER_ID = "currentUserId";
    String CONTEXT_KEY_UNIT_ID = "currentUnitId";
    String USER_INFO = "user_info";
    String CONTEXT_KEY_USERNAME = "currentUserName";
    /**
     * 存储用户权限
     */
    String REDIS_USER_MODULE_LIST_KEY = "user:module:resources:list:";

    String REDIS_USER_KEY = "user_";

    String REDIS_USER_TOKEN_KEY = "user_token_";

    String REDIS_USER_LOGIN_KEY = "user_login_";


    /**
     * 日志链路追踪id信息头
     */
    String TRACE_ID_HEADER = "x-traceId-header";
    /**
     * 日志链路追踪id日志标志
     */
    String LOG_TRACE_ID = "traceId";

    /**
     * 租户id参数
     */
    String TENANT_ID_PARAM = "tenant_id";
    String CONTEXT_KEY_TENANT_ID = "currentTenantId";

    Integer MINUS_ONE_NUMBER= -1;
    Integer ZERO_NUMBER = 0;
    Integer ONE_NUMBER = 1;
    Integer TWO_NUMBER = 2;
    Integer THREE_NUMBER = 3;

    String MINUS_ONE= "-1";
    String ZERO = "0";
    String ONE = "1";

    String TWO = "2";

    String THREE = "3";

    /**
     *  行业监管 first_check
     */
    String BUSINESS_RANGE ="business_range";

    /**
     * 属地监管：first_check参数
     */
    String FIRST_CHECK ="first_check";

    /**
     * 领导主要产业type
     */
    String LEADER_PROJECT = "leader_project";

    List<String> ADMIN_FLAG = Arrays.asList("ROLE_ADMIN");

    /**
     * 秘书端角色LIST
     */
    List<String> COMMON_ROLE_LIST = Arrays.asList("ROLE_ADMIN","ROLE_SECRETAIRE", "ROLE_CENTER_HEAD","ROLE_UNIT_HEAD","ROLE_KEY_WORK_HEAD","ROLE_LEAD_UNIT","ROLE_COMMON_CENTER_HEAD");

    /**
     * 领导端角色LIST
     */
    List<String> LEADER_ROLE_LIST = Arrays.asList("ROLE_ADMIN","ROLE_SECRETARY", "ROLE_MAYOR","ROLE_VICE_MAYOR","ROLE_GENERAL","ROLE_COUNTY","ROLE_WARDEN");

    /**
     * 网格类型：网格长
     */
    String GRID_LABEL_TYPE_1 = "grid_label_type_1";

    /**
     * 网格类型：副网格长
     */
    String GRID_LABEL_TYPE_2 = "grid_label_type_2";

    /**
     * 网格类型：网格员
     */
    String GRID_LABEL_TYPE_3 = "grid_label_type_3";

    /**
     * 网格类型：班组长
     */
    String GRID_LABEL_TYPE_4 = "grid_label_type_4";

    /**
     * 网格类型：联防员
     */
    String GRID_LABEL_TYPE_5 = "grid_label_type_5";

    /**
     *单位类型：安监站
     * */
    String UNIT_TYPE_3 = "unit_type_3";

    /**
     *单位类型：属地政府
     * */
    String UNIT_TYPE_1 = "unit_type_1";

    /**
     *单位类型：行业监管
     * */
    String UNIT_TYPE_2 = "unit_type_2";

    /**
     * 网格回显顺序：网格长、班组长、联防员通用
     */
    String[] LEADER_COLLECTIONS_ARR = {"personType"};

    /**
     * 网格回显顺序：副网格长
     */
    String[] DEPUTY_COLLECTIONS_ARR = {"personType", "position"};

    /**
     * 网格回显顺序：网格员
     */
    String[] GRID_COLLECTIONS_ARR = {"personType", "dept"};

    /**
     * 查询类型：列表
     */
    String QUERY_TYPE_LIST = "list";

    /**
     * 查询类型：网格
     */
    String QUERY_TYPE_GRID = "grid";

    /**
     * 监管类别
     */
    String TYPE = "type";

    /**
     * 项目发布角色
     */
    String ROLE_RELEASE = "ROLE_RELEASE";
    /**
     * 进度上报角色
     */
    String ROLE_PROGRESS = "ROLE_PROGRESS";
    /**
     * 单位管理员
     */
    String ROLE_UNIT_ADMIN = "ROLE_UNIT_ADMIN";
    /**
     * 超级管理员
     */
    String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 游客
     */
    String ROLE_TOURIST = "ROLE_TOURIST";
    /**
     * 领导
     */
    String ROLE_LEADER = "ROLE_LEADER";



}

