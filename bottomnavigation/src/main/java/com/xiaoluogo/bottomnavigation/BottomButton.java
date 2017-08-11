package com.xiaoluogo.bottomnavigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 一个底部button
 * Created by xiaoluogo on 2017/8/2.
 * Email: angel-lwl@126.com
 */
public class BottomButton extends RelativeLayout {
    private static final int VISIBILITY = 0x00000000;
    private static final int GONE = 0x00000008;

    public ImageButton image_button;
    public TextView hint_num;
    public TextView text_title;
    /**
     * 选中后的文字颜色
     */
    private int checkedColor;
    /**
     * 未选中的文字颜色
     */
    private int normalColor;

    public BottomButton(Context context) {
        this(context, null);
    }

    public BottomButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.bottom_button, this);
        image_button = (ImageButton) findViewById(R.id.image_button);
        hint_num = (TextView) findViewById(R.id.hint_num);
        text_title = (TextView) findViewById(R.id.text_title);
        initAttrs(context, attrs);
        initListener();
    }

    private void initListener() {
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                setChecked(true);
            }
        });
    }

    /**
     * 拦截触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.BottomButton);
            //设置底部按钮的文字
            String text = tintTypedArray.getString(R.styleable.BottomButton_text);
            setText(text);
//            //设置底部按钮文字颜色..默认颜色
//            normalColor = tintTypedArray.getColor(R.styleable.BottomButton_normalColor, Color.BLACK);
//            setNormalColor(normalColor);
//            //设置底部按钮文字颜色..选中颜色
//            checkedColor = tintTypedArray.getColor(R.styleable.BottomButton_checkedColor, Color.RED);
//            setCheckedColor(checkedColor);
            //设置hint_visibility,红色提醒原点隐藏显示状态
            int hint_visibility = tintTypedArray.getInteger(R.styleable.BottomButton_hint_visibility, 1);
            setHintVisibility(hint_visibility);
            //设置hint_num,提醒数量
            String num = tintTypedArray.getString(R.styleable.BottomButton_hint_num);
            setHintNum(num);
            //设置图片的选择器
            Drawable imgbackground = tintTypedArray.getDrawable(R.styleable.BottomButton_imgbackground);
            setImgBackground(imgbackground);
//            getResources().getDrawable(R.drawable.bg,null);
            //设置bottom_button的选中状态
            boolean checked = tintTypedArray.getBoolean(R.styleable.BottomButton_checked, false);
            setChecked(checked);
            //回收
            tintTypedArray.recycle();
        }
    }

    public interface OnClickListener {
        void onClick(View v);
    }

    public OnClickListener listener;


    public void setOnIClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置默认的文字颜色
     */
    public void setCheckedColor(int checkedColor) {
        this.checkedColor = checkedColor;
        if (isEnabled()) {
            text_title.setTextColor(checkedColor);
        }
    }

    /**
     * 设置默认的文字颜色
     */

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        text_title.setTextColor(normalColor);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setImgBackground(Drawable imgbackground) {
        if (imgbackground != null) {
            image_button.setBackground(imgbackground);
        }
    }

    /**
     * 设置是否选中
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (checked) {
            setCheckedColor(checkedColor);
            text_title.setTextSize(17);
        } else {
            setNormalColor(normalColor);
            text_title.setTextSize(15);
        }
        image_button.setEnabled(!checked);
        text_title.setEnabled(!checked);
        this.setEnabled(!checked);
    }

    /**
     * 是否选中
     */
    public boolean isChecked() {
        return image_button.isEnabled();
    }

    /**
     * 设置提醒数
     *
     * @param num
     */
    public void setHintNum(String num) {
        if (num != null) {
            hint_num.setText(num);
        }
    }

    /**
     * 得到提醒数
     *
     * @return
     */
    public String getHintNum() {
        return hint_num.getText().toString();
    }

    public void setHintVisibility(int hint_visibility) {
        if (hint_visibility == VISIBILITY) {
            hint_num.setVisibility(VISIBLE);
        } else if (hint_visibility == GONE) {
            hint_num.setVisibility(View.GONE);
        }
    }

    /**
     * 提醒是否显示
     *
     * @return
     */
    public boolean hintIsVisibility() {
        return hint_num.isShown();
    }


    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        if (text != null) {
            text_title.setText(text);
        }
    }

    /**
     * 得到设置的文字
     */
    public String getText() {
        return text_title.getText().toString();
    }
}
