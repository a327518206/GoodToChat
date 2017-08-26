package com.xiaoluogo.waveview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xiaoluogo.waveview.util.BitmapUtils;
import com.xiaoluogo.waveview.util.DensityUtil;
import com.xiaoluogo.waveview.util.bitmap_cache.BitmapCacheUtils;
import com.xiaoluogo.waveview.util.bitmap_cache.OnBitmapCacheListener;


/**
 * Created by xiaoluogo on 2017/8/21.
 * Email: angel-lwl@126.com
 */
public class WaveView extends View {

    private Path path;
    private Paint paint;
    private int dX;
    private int dY;

    private int width;
    private int height;
    private int color = 0x6600FFFF;

    private int wave_bitmap;
    private int wave_length;
    private int wave_height;
    private int wave_color;
    private int wave_duration;
    private int originY;
    private Bitmap header_image;

    private ValueAnimator animator;
    //用来做个矩形框
    private Region region;

    private BitmapCacheUtils utils;

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    private void init() {
        utils = new BitmapCacheUtils();

        paint = new Paint();
        paint.setColor(wave_color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);//填充

        path = new Path();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.WaveView);
        //波长
        wave_length = (int) a.getDimension(R.styleable.WaveView_wave_length, 400);
        //波高
        wave_height = (int) a.getDimension(R.styleable.WaveView_wave_height, 50);
        //起点Y轴上的位置
        originY = (int) a.getDimension(R.styleable.WaveView_originY, 50);
        //波浪的颜色
        wave_color = a.getColor(R.styleable.WaveView_wave_color, color);
        //时长
        wave_duration = a.getInt(R.styleable.WaveView_wave_duration, 3000);
        //图片
        Drawable drawable = a.getDrawable(R.styleable.WaveView_header_image);
        a.recycle();

        if (drawable != null) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            header_image = bd.getBitmap();

            header_image = BitmapUtils.makeRoundCorner(header_image, DensityUtil.dip2px(getContext(), 40));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(viewWidth, viewHeight);

        width = viewWidth;
        height = viewHeight;

        if (originY == 0) {
            originY = height;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPathData();
        paint.setColor(wave_color);
        canvas.drawPath(path, paint);
        if (header_image != null) {
            drawHeader(canvas);
        }
    }

    private void drawHeader(Canvas canvas) {
        paint.setColor(Color.WHITE);
        Rect rect = region.getBounds();
        if (rect.top < originY) {
            canvas.drawBitmap(header_image, rect.left - header_image.getWidth() / 2, rect.top - header_image.getHeight(), paint);
        } else {
            canvas.drawBitmap(header_image, rect.left - header_image.getWidth() / 2, rect.bottom - header_image.getHeight(), paint);
        }
    }

    private void setPathData() {
        path.reset();
        int halfWaveLength = wave_length / 2;
        path.rMoveTo(-wave_length + dX, originY );
        for (int i = -wave_length; i < width + wave_length; i += wave_length) {
            path.rQuadTo(halfWaveLength / 2, -wave_height, halfWaveLength, 0);
            path.rQuadTo(halfWaveLength / 2, wave_height, halfWaveLength, 0);
        }

        float x = width / 2;
        region = new Region();
        Region clip = new Region((int) (x - 0.1), 0, (int) x, height);
        region.setPath(path, clip);


        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }

    public void setAnimation() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(wave_duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        //线性插值器
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //执行的百分比
                float fraction = (float) animation.getAnimatedValue();
                dX = (int) (fraction * wave_length);
                postInvalidate();
            }
        });
        animator.start();
    }

    public void setWaveBitmap(String url) {
        utils.getBitmap(url, 0, new OnBitmapCacheListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                header_image = BitmapUtils.changeSize(getContext(), bitmap);
                header_image = BitmapUtils.makeRoundCorner(header_image, DensityUtil.dip2px(getContext(), 40));
                postInvalidate();
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
