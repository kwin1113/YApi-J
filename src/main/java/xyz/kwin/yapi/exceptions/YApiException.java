package xyz.kwin.yapi.exceptions;

/**
 * YApi异常
 *
 * @author kwin
 * @since 2021/12/29 11:05 上午
 */
public class YApiException extends BaseException {

    public static final String URL_NOT_FOUND = "未找到已配置的YApi Url，请前往配置页配置";
    public static final String YAPI_REQUEST_ERROR = "YApi请求失败";

    public YApiException() {
        super(URL_NOT_FOUND);
    }

    public YApiException(String message) {
        super(message);
    }
}
