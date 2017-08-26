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
import com.xiaoluogo.goodtochat.doman.UserBean;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.v3.BmobUser;

import java.text.SimpleDateFormat;

/**
 * Created by xiaoluogo on 2017/7/28.
 * Email: angel-lwl@126.com
 */
public class SendViewHolder extends BaseViewHolder {

    private TextView tv_send_time;
    private ImageView iv_headerImage;
    private TextView tv_send_message;
    private ImageView iv_send_image;
    private ImageView iv_send_voice;
    private ImageView iv_fail_resend;
    private TextView tv_send_status;
    private ProgressBar progress_send_load;
    private TextView tv_voice_length;

    public SendViewHolder(Context context, ViewGroup parent, View itemView, OnRecyclerViewListener listener) {
        super(context, itemView, parent);
        initView();
    }

    private void initView() {
        tv_send_time = getView(R.id.tv_send_time);
        tv_voice_length = getView(R.id.tv_voice_length);
        iv_headerImage = getView(R.id.iv_headerImage);
        iv_fail_resend = getView(R.id.iv_fail_resend);
        tv_send_status = getView(R.id.tv_send_status);
        progress_send_load = getView(R.id.progress_send_load);
        tv_send_message = getView(R.id.tv_send_message);
        iv_send_image = getView(R.id.iv_send_image);

        iv_send_voice = getView(R.id.iv_send_voice);
    }

    public void onBindData(int itemViewType, ChatMessage data, BmobIMConversation c) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tv_send_time.setText(dateFormat.format(Double.parseDouble(data.getMessageTime())));
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.head)
                .error(R.drawable.default_head);
        Glide.with(mContext).load(BmobUser.getCurrentUser(UserBean.class).getHeaderImage())
                .apply(requestOptions)
                .into(iv_headerImage);
        if (itemViewType == 1) {
            tv_send_message.setVisibility(View.VISIBLE);
            iv_send_voice.setVisibility(View.GONE);
            iv_send_image.setVisibility(View.GONE);
            tv_send_message.setText(data.getMessage());
        } else if (itemViewType == 3) {
            tv_send_message.setVisibility(View.GONE);
            iv_send_voice.setVisibility(View.GONE);
            iv_send_image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(data.getMessage())
                    .into(iv_send_image);
        } else if (itemViewType == 5) {
        } else if (itemViewType == 7) {
            tv_send_message.setVisibility(View.GONE);
            iv_send_image.setVisibility(View.GONE);
            iv_send_voice.setVisibility(View.VISIBLE);
            tv_voice_length.setText(data.getDuration());
            tv_voice_length.setVisibility(View.VISIBLE);
            iv_send_voice.setOnClickListener(new NewRecordPlayClickListener(mContext, data, iv_send_voice));
        } else if (itemViewType == 9) {
        }

        int status = data.getSendOrReceive();
        if (status == BmobIMSendStatus.SEND_FAILED.getStatus())

        {
            iv_fail_resend.setVisibility(View.VISIBLE);
            progress_send_load.setVisibility(View.GONE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus())

        {
            iv_fail_resend.setVisibility(View.GONE);
            progress_send_load.setVisibility(View.VISIBLE);
        } else

        {
            iv_fail_resend.setVisibility(View.GONE);
            progress_send_load.setVisibility(View.GONE);
        }
    }

    class MySendOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_send_voice:

                    break;
            }
        }
    }
}
