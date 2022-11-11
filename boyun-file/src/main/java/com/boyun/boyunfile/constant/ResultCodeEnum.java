package com.boyun.boyunfile.constant;

import lombok.Getter;

/**
 * 结果类枚举
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(true, 0, "成功"),
    UNKNOWN_ERROR(false, 999999, "未知错误"),
    DAO_INSERT_ERROR(false, 100000, "插入数据异常"),
    DAO_SELECT_ERROR(false, 100001, "查询数据异常"),
    DAO_UPDATE_ERROR(false, 100002, "更新数据异常"),
    DAO_DELETE_ERROR(false, 100003, "删除数据异常"),
    NULL_POINT(false, 100004, "空指针异常"),
    INDEX_OUT_OF_BOUNDS(false, 100005, "下标越界异常"),
    REQUEST_TIMEOUT(false, 100006, "请求超时"),
    PARAM_ERROR(false, 100007, "参数错误"),
    NOT_INIT_DATA(false, 100008, "数据未初始化"),
    CUSTOM_ERROR(false, 200000, "自定义错误"),
    USER_FORBIDDEN(false, 200001, "用户被禁用"),
    NOT_LOGIN_ERROR(false, 200002, "未登录");

    // 响应是否成功
    private Boolean success;
    // 响应状态码
    private Integer code;
    // 响应信息
    private String message;

    ResultCodeEnum(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}