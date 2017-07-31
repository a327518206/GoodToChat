package com.xiaoluogo.goodtochat.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.adapter.base.BaseViewHolder;
import com.xiaoluogo.goodtochat.adapter.base.OnRecyclerViewListener;
import com.xiaoluogo.goodtochat.db.ChatMessage;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;

/**
 * Created by xiaoluogo on 2017/7/28.
 * Email: angel-lwl@126.com
 */
public class ReceiveViewHolder extends BaseViewHolder {

    private TextView tv_receive_time;
    private ImageView iv_f_headerImage;
    private TextView tv_receive_message;
    private ImageView iv_receive_image;
    private ImageView iv_receive_voice;
    private ImageView iv_receive_fail_resend;
    private TextView tv_receive_status;
    private ProgressBar progress_receive_load;
    private TextView tv_receive_voice_length;

    public ReceiveViewHolder(Context context, ViewGroup parent, View itemView, OnRecyclerViewListener listener) {
        super(context,itemView,parent);
        initView();
    }

    private void initView() {
        tv_receive_time = getView(R.id.tv_receive_time);
        iv_f_headerImage = getView(R.id.iv_f_headerImage);
        iv_receive_fail_resend = getView(R.id.iv_receive_fail_resend);
        tv_receive_status = getView(R.id.tv_receive_status);
        progress_receive_load = getView(R.id.progress_receive_load);
        tv_receive_message = getView(R.id.tv_receive_message);
        iv_receive_image = getView(R.id.iv_receive_image);
        iv_receive_voice = getView(R.id.iv_receive_voice);
        tv_receive_voice_length = getView(R.id.tv_receive_voice_length);
    }
    public void onBindData(int itemViewType, ChatMessage data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tv_receive_time.setText(dateFormat.format(Double.parseDouble(data.getMessageTime())));
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.head)
                .error(R.drawable.default_head);
        Glide.with(mContext).load(data.getHeaderImage())
                .apply(requestOptions)
                .into(iv_f_headerImage);
        if (itemViewType == 2) {
            tv_receive_message.setVisibility(View.VISIBLE);
            iv_receive_image.setVisibility(View.GONE);
            iv_receive_voice.setVisibility(View.GONE);
            tv_receive_message.setText(data.getMessage());
        } else if (itemViewType == 4) {
            tv_receive_message.setVisibility(View.GONE);
            iv_receive_voice.setVisibility(View.GONE);
            iv_receive_image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(data.getMessage())
                    .into(iv_receive_image);
        } else if (itemViewType == 6) {
        } else if (itemViewType == 8) {
            tv_receive_message.setVisibility(View.GONE);
            iv_receive_image.setVisibility(View.GONE);
            iv_receive_voice.setVisibility(View.VISIBLE);
            tv_receive_voice_length.setText(data.getDuration());
            iv_receive_voice.setOnClickListener(new NewRecordPlayClickListener(mContext,data,iv_receive_voice));
        } else if (itemViewType == 10) {
        }

        int status =data.getSendOrReceive();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus()) {
            iv_receive_fail_resend.setVisibility(View.VISIBLE);
            progress_receive_load.setVisibility(View.GONE);
        } else if (status== BmobIMSendStatus.SENDING.getStatus()) {
            iv_receive_fail_resend.setVisibility(View.GONE);
            progress_receive_load.setVisibility(View.VISIBLE);
        } else {
            iv_receive_fail_resend.setVisibility(View.GONE);
            progress_receive_load.setVisibility(View.GONE);
        }
    }
}
