package d.dao.easylife.dagger2.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import d.dao.easylife.dagger2.R;
import d.dao.easylife.dagger2.adapter.RobotAdapter;
import d.dao.easylife.dagger2.app.AppComponent;
import d.dao.easylife.dagger2.model.bean.robot.BaseRobotResponseData;
import d.dao.easylife.dagger2.model.bean.robot.RobotMessage;
import d.dao.easylife.dagger2.components.DaggerRobotActivityComponent;
import d.dao.easylife.dagger2.constants.BaseUrl;
import d.dao.easylife.dagger2.modules.RobotActivityModule;
import d.dao.easylife.dagger2.presenter.impl.RobotPresenter;
import d.dao.easylife.dagger2.ui.view.IRobotView;
import d.dao.easylife.dagger2.utils.ToastUtils;

/**
 * Created by dao on 6/3/16.
 */
public class RobotActivity extends BaseToolbarActivity implements View.OnClickListener, IRobotView {
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_send)
    TextView tv_send;
    @Bind(R.id.et_send)
    EditText et_send;

    @Inject
    RobotPresenter mPresenter;


    private Context mContext;
    private RobotAdapter mAdapter;
    private List<RobotMessage> mList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerRobotActivityComponent.builder().appComponent(appComponent)
                .robotActivityModule(new RobotActivityModule(this))
                .build().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_robot;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        super.setHomeTrue();//加上返回按钮
        mContext = RobotActivity.this;
        //RecyclerView


        mAdapter = new RobotAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new EasyBorderDividerItemDecoration(
//                this.getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
//                this.getResources().getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans)));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                String message = et_send.getText().toString();
                Log.e("msg", message);
                if (TextUtils.isEmpty(message)) {
                    et_send.findFocus();
                    return;
                }
                mList.add(new RobotMessage(message, 0));
                mAdapter = new RobotAdapter(mContext, mList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mList.size() - 1);
                mPresenter.loadRobot(message, BaseUrl.ROBOT_KEY);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //获取应答成功
    @Override
    public void onGetRobotResponseSuccess(BaseRobotResponseData data) {
        String result = data.getText();
        mList.add(new RobotMessage(result, 1));
        mAdapter = new RobotAdapter(mContext, mList);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mList.size() - 1);
    }


    //获取应答失败
    @Override
    public void onGetRobotResponseFailure(Throwable e) {
        ToastUtils.show(mContext, "应答失败", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
