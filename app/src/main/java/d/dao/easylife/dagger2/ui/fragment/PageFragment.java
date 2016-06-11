package d.dao.easylife.dagger2.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.camnter.easyrecyclerview.widget.decorator.EasyBorderDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import d.dao.easylife.dagger2.R;
import d.dao.easylife.dagger2.adapter.JokeFirstAdapter;
import d.dao.easylife.dagger2.app.EasyLifeApplication;
import d.dao.easylife.dagger2.model.bean.joke.BaseJokeFirstData;
import d.dao.easylife.dagger2.components.DaggerJokeFirstFragmentComponent;
import d.dao.easylife.dagger2.modules.JokeFirstFragmentModule;
import d.dao.easylife.dagger2.presenter.impl.JokeFirstPresenter;
import d.dao.easylife.dagger2.ui.view.IJokeFirstView;
import d.dao.easylife.dagger2.utils.ReservoirUtils;
import d.dao.easylife.dagger2.utils.ToastUtil;

/**
 * Created by dao on 5/31/16.
 * 刷新得,先返回更新的数据,也就是比maxXid大的数据,如果是3条,那么就返回3条更新的和其他的,其他的不需要关注,只要找到刚好衔接的点即可
 * 加载更多,与刷新类似,多观察返回的数据即可
 * 注意事情:笑话ID已经到44334,再增多后可能需要更改bean类型
 */
public class PageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IJokeFirstView,View.OnClickListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;//实例化时传递的参数

    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    private EasyBorderDividerItemDecoration dataDecoration;//

    private boolean isRefreshing = false;
    private List<BaseJokeFirstData> mList = new ArrayList<>();//储存数据
    private static int pageSize = 10;//每次请求十条数据
    private Context mContext;
    private JokeFirstAdapter mAdapter;


    @Inject
    JokeFirstPresenter mJokeFirstPresenter;
    @Inject
    ToastUtil mToastUtil;
    @Inject
    ReservoirUtils reservoirUtils;//缓存管理

    @Bind(R.id.error_root)
    LinearLayout ll_error_root;//错误界面

    @Bind(R.id.tv_load_once_more)
    TextView tv_loadOnceMore;//加载更多



    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        DaggerJokeFirstFragmentComponent.builder().
                appComponent(EasyLifeApplication.get(this.getContext()).getAppComponent())
                .jokeFirstFragmentModule(new JokeFirstFragmentModule(this))
                .build().inject(this);
        mContext = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        ButterKnife.bind(this,view);

        tv_loadOnceMore.setOnClickListener(this);

        mSwipe.setOnRefreshListener(this);
        mSwipe.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mAdapter = new JokeFirstAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //EasyRecyclerView
        this.dataDecoration = new EasyBorderDividerItemDecoration(
                this.getResources().getDimensionPixelOffset(R.dimen.data_border_divider_height),
                this.getResources()
                        .getDimensionPixelOffset(R.dimen.data_border_padding_infra_spans));
        mRecyclerView.addItemDecoration(dataDecoration);

        mRecyclerView.addOnScrollListener(this.getRecyclerViewOnScrollListener());

                mSwipe.measure(0, 0);
                mSwipe.setRefreshing(true);
                isRefreshing = true;
                mJokeFirstPresenter.loadFirstJoke(0, 0, pageSize);
        return view;
    }

    /**
     * LinearLayoutManager 时的滚动监听
     *
     * @return RecyclerView.OnScrollListener
     */
    public RecyclerView.OnScrollListener getRecyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {
            private boolean toLast = false;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // 正在向下滑动
                    this.toLast = true;
                } else {
                    // 停止滑动或者向上滑动
                    this.toLast = false;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    // 不滚动
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // 最后完成显示的item的position 正好是 最后一条数据的index
                        if (toLast && manager.findLastCompletelyVisibleItemPosition() ==
                                (manager.getItemCount() - 1)) {
                            Log.e("MainActivity", "加载更多");
                            PageFragment.this.loadMoreRequest();
                        }
                    }
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                    // 不滚动
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        /*
                         * 由于是StaggeredGridLayoutManager
                         * 取最底部数据可能有两个item，所以判断这之中有一个正好是 最后一条数据的index
                         * 就OK
                         */
                        int[] bottom = manager.findLastCompletelyVisibleItemPositions(new int[2]);
                        int lastItemCount = manager.getItemCount() - 1;
                        if (toLast && (bottom[0] == lastItemCount || bottom[1] == lastItemCount)) {
                            PageFragment.this.loadMoreRequest();
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        Log.e("onrefresh", "refresh");
        isRefreshing = true;
        //如果有缓存,缓存的数据最少有十条
        if (mList != null && mList.size() > 0) {
            Log.e("onrefresh", "maxid");
            int maxId = mList.get(0).getId();
            int minId = mList.get(mList.size() - 1).getId();
            this.mJokeFirstPresenter.loadFirstJoke(maxId, minId, pageSize);
        } else {
            Log.e("onrefresh", "0");

            this.mJokeFirstPresenter.loadFirstJoke(0, 0, pageSize);
        }
    }

    //加载更多
    private void loadMoreRequest(){
        if (!isRefreshing) {
            if (mList != null && mList.size() > 0) {
                //加载更多
                int maxId = mList.get(0).getId();
                int minId = mList.get(mList.size() - 1).getId();

                this.mJokeFirstPresenter.loadFirstJokeMore(maxId, minId, pageSize);
                isRefreshing = true;
                mSwipe.setRefreshing(true);
            }

        }
    }

    //获取笑话成功
    @Override
    public void onGetJokeSuccess(List<BaseJokeFirstData> list) {
        Log.e("success", "onGetJokeSuccess");
        isRefreshing = false;
        if (this.mSwipe.isRefreshing()) {
            this.mSwipe.setRefreshing(false);
        }
        if(list.size()==0){
            mToastUtil.showToast("没有得到数据");
            return;
        }

        //如果集合大小为空,直接赋值,不再进行任何处理即可
        if (this.mList.size() == 0) {
            this.mList = list;
            mAdapter.setList(mList);
            mAdapter.notifyDataSetChanged();
        } else {//如果不为空
            //如果获取到的数据的最后一条ID大于原有集合的第一条ID
            if (list.get(list.size() - 1).getId() > mList.get(0).getId()) {
                list.addAll(mList);//把原有的数据集合赋值给得到的数组,然后再赋值给 mList,目的就是把得到的数据查到原有集合数据的前面
                mList.clear();
                mList = list;
            } else if (list.get(0).getId() < mList.get(mList.size() - 1).getId()) {//如果得到的数据的第一条数据ID小于原有数据的最后一条ID
                //不处理,得到的数据没用,相当于没有得到数据
                mToastUtil.showToast("暂无更新的笑话");
                return;
            } else {//得到的数据有几条时刚刚更新的数据,另外几条不是
                //得到mList的最大ID
                int index = -1;
                int count = list.size();
                //得到到第几条刚好是更新的ID
                for (int i = 0; i < count; i++) {
                    if (list.get(i).getId() == mList.get(mList.size() - 1).getId() + 1) {
                        index = i;
                        break;
                    }
                }
                //截取
                List<BaseJokeFirstData> tempFather = list.subList(0, index + 1);
                tempFather.addAll(mList);
                mList.clear();
                mList.addAll(tempFather);

            }
            //刷新
            mAdapter.setList(mList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetJokeFailure(Throwable e) {
        isRefreshing = false;
        Log.e("error", e.toString());
        this.mSwipe.setRefreshing(false);
        mToastUtil.showToast("刷新失败");

        //根据mList的大小来判断,如果为0,说明网络请求失败,获取缓存失败
        if(mList == null || mList.size()==0){
            ll_error_root.setVisibility(View.VISIBLE);
        }else{//不为0,就不显示
            ll_error_root.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadJokeSuccess(List<BaseJokeFirstData> list) {
        Log.e("success", "onGetJokeSuccess");
        Log.e("result", list.toString());

        isRefreshing = false;
        if (this.mSwipe.isRefreshing()) {
            this.mSwipe.setRefreshing(false);
        }
        if(list.size()==0){
            mToastUtil.showToast("没有得到数据");
            return;
        }
        //如果集合大小为空,直接赋值,不再进行任何处理即可
        if (this.mList.size() == 0) {
            this.mList = list;
            mAdapter.setList(mList);
            mAdapter.notifyDataSetChanged();
        } else {//如果不为空
            //如果获取到的数据的第一条ID大于原有集合的最后一条ID
            if (list.get(0).getId() < mList.get(mList.size()-1).getId()) {
                mList.addAll(list);//把得到的数据集合赋值给mList
            } else if (list.get(list.size()-1).getId() > mList.get(0).getId()){//如果得到的数据的最后一条数据ID大于原有数据的最后一条ID
                //不处理,得到的数据没用,相当于没有得到数据
                mToastUtil.showToast("没有更多的笑话");
                return;
            } else {//得到的数据有几条时刚刚更新的数据,另外几条不是
                //得到mList的最大ID
                int index = -1;
                //得到到第几条刚好是更新的ID
                int count = list.size();
                for (int i = 0; i <count; i++) {
                    if (list.get(i).getId() == mList.get(mList.size() - 1).getId() - 1) {
                        index = i;
                        break;
                    }
                }
                //截取
                List<BaseJokeFirstData> tempFather = list.subList(0, index + 1);
                tempFather.addAll(mList);
                mList.clear();
                mList.addAll(tempFather);

            }
            //刷新
            mAdapter.setList(mList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadJokeFailure(Throwable e) {
        isRefreshing = false;
        Log.e("error", e.toString());
        this.mSwipe.setRefreshing(false);
        mToastUtil.showToast("加载失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //缓存20条
        if(mList.size()>20){
            List<BaseJokeFirstData> tempList = mList.subList(0, 20);
            reservoirUtils.refresh("jokefirst",tempList);
        }else{
            if(mList.size()>0){
                reservoirUtils.refresh("jokefirst",mList);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_load_once_more:
                mSwipe.setRefreshing(true);
                isRefreshing = true;
                this.mJokeFirstPresenter.loadFirstJoke(0,0,pageSize);
                ll_error_root.setVisibility(View.GONE);
                break;
        }
    }


}
