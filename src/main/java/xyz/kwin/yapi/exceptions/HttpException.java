package xyz.kwin.yapi.exceptions;

/**
 * Http请求异常
 *
 * @author kwin
 * @since 2021/12/13 5:17 下午
 */
public class HttpException extends BaseException {

    public static final String HTTP_ERROR = "http请求异常";
    public static final String HTTP_ENTITY_EMPTY = "http entity is null";

    public HttpException() {
        super(HTTP_ERROR);
    }

    public HttpException(String message) {
        super(message);
    }

}
