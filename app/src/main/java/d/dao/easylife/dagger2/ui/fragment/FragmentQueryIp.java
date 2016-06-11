package d.dao.easylife.dagger2.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import d.dao.easylife.dagger2.R;
import d.dao.easylife.dagger2.adapter.SearchResultsListAdapter;
import d.dao.easylife.dagger2.app.EasyLifeApplication;
import d.dao.easylife.dagger2.model.bean.ip.BaseIpData;
import d.dao.easylife.dagger2.components.DaggerFragmentQueryIpComponent;
import d.dao.easylife.dagger2.constants.BaseUrl;
import d.dao.easylife.dagger2.modules.QueryIpFragmentModule;
import d.dao.easylife.dagger2.presenter.impl.QueryIpPresenter;
import d.dao.easylife.dagger2.ui.view.IQueryIpView;
import d.dao.easylife.dagger2.utils.ToastUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentQueryIp extends Fragment implements IQueryIpView {

    private final String TAG = "MainActivity";

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private FloatingSearchView mSearchView;

    private RecyclerView mSearchResultsList;
    private SearchResultsListAdapter mSearchResultsAdapter;


    private Context mContext;


    @Bind(R.id.et_ip)
    EditText et_ip;
    @Bind(R.id.tv_query)
    TextView tv_query;
    @Bind(R.id.tv_area)
    TextView tv_area;
    @Bind(R.id.tv_location)
    TextView tv_location;

    @Inject
    QueryIpPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_ip, container, false);
        ButterKnife.bind(this, view);

        DaggerFragmentQueryIpComponent.builder().
                appComponent(EasyLifeApplication.get(this.getContext()).getAppComponent())
                .queryIpFragmentModule(new QueryIpFragmentModule(this))
                .build().inject(this);

        tv_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et_ip.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    mPresenter.queryIp(text, BaseUrl.IP_KEY);
                } else {
                    ToastUtils.show(mContext, "IP地址不能为空", 0);
                }
            }
        });


        return view;
    }

    /**
     * 查询成功
     *
     * @param data 返回的IP信息
     */

    @Override
    public void onQueryIpSuccess(BaseIpData data) {

        Log.e("data", data.toString());


        String area = data.getArea();
        String location = data.getLocation();

        tv_area.setText(area);
        tv_location.setText(location);

    }

    /**
     * 查询失败
     *
     * @param e
     */
    @Override
    public void onQueryIpFailure(Throwable e) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
