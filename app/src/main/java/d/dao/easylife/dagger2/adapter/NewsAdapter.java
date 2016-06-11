package d.dao.easylife.dagger2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import d.dao.easylife.dagger2.R;
import d.dao.easylife.dagger2.model.bean.news.BaseNewsData;
import d.dao.easylife.dagger2.utils.DateUtils;

/**
 * Created by dao on 5/30/16.
 * 新闻
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BaseNewsData> mList = new ArrayList<>();
    private LayoutInflater mInflater;
    //onClickListener
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    //onItemLongClickListener
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener;

    public NewsAdapter(Context context, List<BaseNewsData> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(mContext);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_news, parent, false);
        AutoUtils.autoSize(view);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }


        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
        ((NewsHolder) holder).tv_title.setText(mList.get(position).getTitle());
        ((NewsHolder) holder).tv_source.setText(mList.get(position).getSource());
        long time = mList.get(position).getBehot_time();
        String result = DateUtils.formatDate("yyyy-MM-dd HH:mm:ss", time);
        ((NewsHolder) holder).tv_time.setText(result);
        ((NewsHolder) holder).tv_praise.setText("赞 " + mList.get(position).getDigg_count());
        ((NewsHolder) holder).tv_step.setText("踩 " + mList.get(position).getBury_count());
        ((NewsHolder) holder).tv_collect.setText("收藏 " + mList.get(position).getRepin_count());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_source;
        TextView tv_time;
        TextView tv_praise;
        TextView tv_step;
        TextView tv_collect;


        public NewsHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_source = (TextView) itemView.findViewById(R.id.tv_source);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_praise = (TextView) itemView.findViewById(R.id.tv_praise);
            tv_step = (TextView) itemView.findViewById(R.id.tv_step);
            tv_collect = (TextView) itemView.findViewById(R.id.tv_collect);
        }
    }

    public void setList(List<BaseNewsData> list){
        this.mList.clear();
        this.mList.addAll(list);
    }

}
