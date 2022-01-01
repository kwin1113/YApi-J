package xyz.kwin.yapi.entity.yapi;

/**
 * @author kwin
 * @since 2021/12/28 10:54 下午
 */
public class Response<T> {
    private Integer errcode;
    private String errmsg;
    private T data;

    public Response() {
    }

    public Response(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public boolean isSuccess() {
        return errcode == 0;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
