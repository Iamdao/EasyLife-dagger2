package d.dao.easylife.dagger2.ui.view;

import d.dao.easylife.dagger2.model.bean.ip.BaseIpData;

/**
 * Created by dao on 6/4/16.
 * ip查询
 */
public interface IQueryIpView {

    /**
     * 查询IP成功
     * @param data 返回的IP信息
     */
    void onQueryIpSuccess(BaseIpData data);

    /**
     * 查询IP失败
     * @param e
     */
    void onQueryIpFailure(Throwable e);
}
