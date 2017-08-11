package com.xiaoluogo.bottomnavigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoluogo on 2017/8/3.
 * Email: angel-lwl@126.com
 */
public class BottomNavigation extends LinearLayout {

    private int threeButton = 3;
    private int fourButton = 4;
    private int fiveButton = 5;
    private int button_sum;

    private BottomButton bottombutton0;
    private BottomButton bottombutton1;
    private BottomButton bottombutton2;
    private BottomButton bottombutton3;
    private BottomButton bottombutton4;



    private final List<BottomButton> list;

    public BottomNavigation(Context context) {
        this(context, null);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.bottom_navigation, this);
        bottombutton0 = (BottomButton) findViewById(R.id.bottombutton0);
        bottombutton1 = (BottomButton) findViewById(R.id.bottombutton1);
        bottombutton2 = (BottomButton) findViewById(R.id.bottombutton2);
        bottombutton3 = (BottomButton) findViewById(R.id.bottombutton3);
        bottombutton4 = (BottomButton) findViewById(R.id.bottombutton4);

        list = new ArrayList<>();
        list.add(bottombutton0);
        list.add(bottombutton1);
        list.add(bottombutton2);
        list.add(bottombutton3);
        list.add(bottombutton4);
        initAttrs(context, attrs);
        visibileButtonNum();
        listener();
    }

    private void listener() {
        bottombutton0.setOnIClickListener(new MyOnClickListener());
        bottombutton1.setOnIClickListener(new MyOnClickListener());
        bottombutton2.setOnIClickListener(new MyOnClickListener());
        bottombutton3.setOnIClickListener(new MyOnClickListener());
        bottombutton4.setOnIClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements BottomButton.OnClickListener {
        @Override
        public void onClick(View v) {
            int position;
            switch (v.getId()) {
//                case R.id.bottombutton0:
                default:
//                    bottombutton1.setChecked(false);
//                    bottombutton2.setChecked(false);
//                    bottombutton3.setChecked(false);
//                    bottombutton4.setChecked(false);
                    position = 0;
                    break;
                case R.id.bottombutton1:
//                    bottombutton0.setChecked(false);
//                    bottombutton2.setChecked(false);
//                    bottombutton3.setChecked(false);
//                    bottombutton4.setChecked(false);
                    position = 1;
                    break;
                case R.id.bottombutton2:
//                    bottombutton0.setChecked(false);
//                    bottombutton1.setChecked(false);
//                    bottombutton3.setChecked(false);
//                    bottombutton4.setChecked(false);
                    position = 2;
                    break;
                case R.id.bottombutton3:
//                    bottombutton0.setChecked(false);
//                    bottombutton1.setChecked(false);
//                    bottombutton2.setChecked(false);
//                    bottombutton4.setChecked(false);
                    position = 3;
                    break;
                case R.id.bottombutton4:
//                    bottombutton0.setChecked(false);
//                    bottombutton1.setChecked(false);
//                    bottombutton2.setChecked(false);
//                    bottombutton3.setChecked(false);
                    position = 4;
                    break;
            }
            //简化后的代码
            checkedPosition(position);

            if (onBtnClickListener != null) {
                onBtnClickListener.onClick(position);
            }
        }
    }

    /**
     * 选中的位置
     * @param position
     */
    private void checkedPosition(int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                continue;
            }
            getBtn(i).setChecked(false);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.BottomNavigation);
            button_sum = typedArray.getInt(R.styleable.BottomNavigation_button_sum, 3);
            //设置文字默认颜色
            int normalColor = typedArray.getColor(R.styleable.BottomNavigation_normal_text_color, Color.BLACK);
            setTextNormalColor(normalColor);
            //设置文字选中颜色
            int checkedColor = typedArray.getColor(R.styleable.BottomNavigation_checked_text_color, Color.RED);
            setTextCheckedColor(checkedColor);
            //回收
            typedArray.recycle();
        }
    }

    /**
     * 显示按钮总数
     */
    private void visibileButtonNum() {
        if (button_sum == threeButton) {
            bottombutton3.setVisibility(GONE);
            bottombutton4.setVisibility(GONE);
        } else if (button_sum == fourButton) {
            bottombutton4.setVisibility(GONE);
        } else if (button_sum == fiveButton) {
            return;
        }
    }

    /**
     * 设置每个按钮的image
     *
     * @param res
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setImgBackground(int... res) {
        Drawable drawable;
        if (res.length > 0) {
            for (int i = 0; i < res.length; i++) {
                drawable = getResources().getDrawable(res[i], null);
                if (drawable != null) {
                    list.get(i).setImgBackground(drawable);
                }
            }
        }
    }

    /**
     * 设置每个按钮的文字
     *
     * @param titles
     */
    public void setBottomTitle(String... titles) {
        String title;
        if (titles.length > 0) {
            for (int i = 0; i < titles.length; i++) {
                title = titles[i];
                if (title != null) {
                    list.get(i).setText(title);
                }
            }
        }
    }

    /**
     * 设置文字正常颜色
     *
     * @param normalColor
     */
    public void setTextNormalColor(int normalColor) {
        for (int i = 0; i < list.size(); i++) {

            list.get(i).setNormalColor(normalColor);

        }
    }

    /**
     * 设置文字选中颜色
     *
     * @param checkedColor
     */
    public void setTextCheckedColor(int checkedColor) {
        for (int i = 0; i < list.size(); i++) {

            list.get(i).setCheckedColor(checkedColor);

        }
    }

    /**
     * 设置默认选中的位置
     *
     * @param position
     */
    public void setClickedPosition(int position) {
        checkedPosition(position);
        getBtn(position).setChecked(true);
    }

//    public int getCurrentPosition() {
//        if (!bottombutton0.isChecked()) {
//            bottombutton1.setChecked(false);
//            bottombutton2.setChecked(false);
//            bottombutton3.setChecked(false);
//            bottombutton4.setChecked(false);
//            return 0;
//        } else if (!bottombutton1.isChecked()) {
//            bottombutton0.setChecked(false);
//            bottombutton2.setChecked(false);
//            bottombutton3.setChecked(false);
//            bottombutton4.setChecked(false);
//            return 1;
//        } else if (!bottombutton2.isChecked()) {
//            bottombutton0.setChecked(false);
//            bottombutton1.setChecked(false);
//            bottombutton3.setChecked(false);
//            bottombutton4.setChecked(false);
//            return 2;
//        } else if (!bottombutton3.isChecked()) {
//            bottombutton0.setChecked(false);
//            bottombutton1.setChecked(false);
//            bottombutton2.setChecked(false);
//            bottombutton4.setChecked(false);
//            return 3;
//
//        } else if (!bottombutton4.isChecked()) {
//            bottombutton0.setChecked(false);
//            bottombutton1.setChecked(false);
//            bottombutton2.setChecked(false);
//            bottombutton3.setChecked(false);
//            return 4;
//        } else {
////        for(int i = 0 ; i < list.size(); i++){
////            if(list.get(i).isChecked()){
////
////            }
////        }
//            return 0;
//        }
//    }

    public BottomButton getBtn(int position) {
        return list.get(position);
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    private OnBtnClickListener onBtnClickListener;

    public interface OnBtnClickListener {
        void onClick(int position);
    }
}
