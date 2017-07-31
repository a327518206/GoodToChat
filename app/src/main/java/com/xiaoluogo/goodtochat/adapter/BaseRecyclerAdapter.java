package com.xiaoluogo.goodtochat.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 可添加header和footer的RecyclerView适配器
 * Created by xiaoluogo on 2017/7/20.
 * Email: angel-lwl@126.com
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;

    public Context context;

    public List<T> mDatas;

    public BaseRecyclerAdapter(Context context, List<T> datas) {
        this.context = context;
        this.mDatas = datas;
    }

    private View mHeaderView;
    public View mFooterView;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 只可以添加一次headerView
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 只可以添加一次footView
     * @param footerView
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount());
    }

    public View getFooterView() {
        return mFooterView;
    }

//    public void addDatas(List<T> datas) {
//        mDatas.addAll(datas);
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        if (mFooterView == null) return TYPE_NORMAL;
        if (position == getItemCount() - 1) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) return new ViewHolder(mFooterView);
        if (mHeaderView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        return onCreate(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        //防止出现ClassCastException
        if (getItemViewType(position) == TYPE_HEADER) return;
        if (getItemViewType(position) == TYPE_FOOTER) return;

        int pos = getRealPosition(viewHolder);
        T data = null;
        //对数据进行判空,以免出现异常
        if (mDatas != null && mDatas.size() > 0) {
//            if(mFooterView != null && pos == mDatas.size()){
//                pos -= 1;
//            }
            data = mDatas.get(pos);
            onBind(viewHolder, pos, data);
        }
        if (mListener != null) {
            final T finalData = data;
            final int finalPos = pos;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(finalPos, finalData);
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        super.onViewAttachedToWindow(viewHolder);
        ViewGroup.LayoutParams lp = viewHolder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(viewHolder.getLayoutPosition() == 0);
        }
    }

    /**
     * 得到真正的位置position
     * @param holder
     * @return
     */
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
//        if (mHeaderView != null ) {
//            position -- ;
//        }
//        if (mFooterView != null) {
//            position --;
//        }
//        return position;
        return mHeaderView == null ? position : position - 1;
    }
    /**
     * 得到recyclerView中的item总数
     */
    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        if (mHeaderView != null ) {
            itemCount ++ ;
        }
        if (mFooterView != null) {
            itemCount ++;
        }
        return itemCount;
    }
    /**
     * 得到recyclerView中的真正数据总数
     */
    public int getRealItemCount(){
        return mDatas.size();
    }

    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int viewType);

    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, T data);

    public abstract void bindDatas(List<T> allNewFriend);

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }
}