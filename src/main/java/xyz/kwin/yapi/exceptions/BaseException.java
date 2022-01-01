package xyz.kwin.yapi.exceptions;

/**
 * 基础报错
 *
 * @author kwin
 * @since 2021/12/13 5:18 下午
 */
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public static BaseException throwSelf(String message) {
        throw new BaseException(message);
    }
}
