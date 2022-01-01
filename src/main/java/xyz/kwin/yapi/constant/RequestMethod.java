package xyz.kwin.yapi.constant;

/**
 * YApi支持的请求类型枚举
 *
 * @author kwin
 * @since 2021/12/30 11:15 上午
 */
public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PATCH("PATCH"),
    ;

    /**
     * 请求类型
     */
    private final String method;

    RequestMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static RequestMethod byMethod(String method) {
        for (RequestMethod value : values()) {
            if (value.getMethod().equals(method)) {
                return value;
            }
        }
        return GET;
    }
}
