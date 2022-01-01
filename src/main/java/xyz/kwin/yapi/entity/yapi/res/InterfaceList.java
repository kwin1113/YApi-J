package xyz.kwin.yapi.entity.yapi.res;

import java.util.List;

/**
 * @author kwin
 * @since 2021/12/29 10:40 上午
 */
public class InterfaceList {
    private Integer count;
    private Integer total;
    private List<InterfaceInfo> list;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<InterfaceInfo> getList() {
        return list;
    }

    public void setList(List<InterfaceInfo> list) {
        this.list = list;
    }
}
