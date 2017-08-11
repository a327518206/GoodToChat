/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xiaoluogo.goodtochat.other;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.doman.UserBean;
import com.xiaoluogo.goodtochat.utils.Constants;
import com.xiaoluogo.goodtochat.utils.L;

import java.io.File;
import java.io.IOException;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 查看头像,更改头像
 */
public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 显示按钮
     */
    private static final int SHOW_BUTTON = 1;
    /**
     * 隐藏按钮
     */
    private static final int HIDE_BUTTON = 2;
    /**
     * 更新进度
     */
    private static final int UPDATE_PROGRESS = 3;
//    /**
//     * 打开相机
//     */
//    private static final int TAKE_PHOTO = 1;
//    /**
//     * 相册中选择图片
//     */
//    public static final int CHOOSE_PHOTO = 2;
    private Toolbar toolbarPhoto;
    private TextView tvPhotoTitle;
    private Button btnChangeHeader;
    private PhotoView headerPhotoView;
    private RelativeLayout rlChangePhoto;
    private Button btnPhotosChoose;
    private Button btnPhotograph;
    private Button btnPhotoCancel;
    private ProgressBar progress_upload;
    private TextView tv_upload;
    /**
     * 是不是本人信息
     */
    private boolean isMyInfo;
    private String photo_title;
    private String headerUri;
    /**
     * 控件的高
     */
    private int rlChangePhotoHeight;
    /**
     * 当前控件的高
     */
    private double currentHeight;

    private boolean isFinish;
    /**
     * 修改后的uri
     */
    private String currentUri;
    private UserBean user;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMyInfo = getIntent().getBooleanExtra("isMyInfo", false);
        photo_title = getIntent().getStringExtra("photo_title");
        headerUri = getIntent().getStringExtra("photoUrl");
        findViews();
        initData();
    }

    private void initData() {
        tvPhotoTitle.setText(photo_title);
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        setContentView(R.layout.activity_photo);

        toolbarPhoto = (Toolbar) findViewById(R.id.toolbar_photo);

        setSupportActionBar(toolbarPhoto);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_home);
        }

        tvPhotoTitle = (TextView) findViewById(R.id.tv_photo_title);
        btnChangeHeader = (Button) findViewById(R.id.btn_change_header);
        headerPhotoView = (PhotoView) findViewById(R.id.header_photo_view);
        rlChangePhoto = (RelativeLayout) findViewById(R.id.rl_change_photo);
        btnPhotosChoose = (Button) findViewById(R.id.btn_photos_choose);
        btnPhotograph = (Button) findViewById(R.id.btn_photograph);
        btnPhotoCancel = (Button) findViewById(R.id.btn_photo_cancel);
        progress_upload = (ProgressBar) findViewById(R.id.progress_upload);
        tv_upload = (TextView) findViewById(R.id.tv_upload);

        progress_upload.setVisibility(View.GONE);
        tv_upload.setVisibility(View.GONE);

        if (isMyInfo) {
            btnChangeHeader.setVisibility(View.VISIBLE);
            rlChangePhoto.measure(0, 0);
            rlChangePhotoHeight = rlChangePhoto.getMeasuredHeight();
            rlChangePhoto.setPadding(0, rlChangePhotoHeight, 0, 0);
            isFinish = false;
            user = BmobUser.getCurrentUser(UserBean.class);
        } else {
            btnChangeHeader.setVisibility(View.GONE);
            rlChangePhoto.setVisibility(View.GONE);
        }

        loadPhoto(headerUri);

        btnChangeHeader.setOnClickListener(this);
        btnPhotosChoose.setOnClickListener(this);
        btnPhotograph.setOnClickListener(this);
        btnPhotoCancel.setOnClickListener(this);
    }

    private void loadPhoto(String url) {
        final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(headerPhotoView);

        Glide.with(this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(PhotoActivity.this, "加载头像失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                L.e(e.getMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                photoViewAttacher.update();
                return false;
            }
        })
                .into(headerPhotoView);
    }

    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        if (v == btnChangeHeader) {
            // Handle clicks for btnChangeHeader
            if (!isFinish) {
                isFinish = true;
                btnChangeHeader.setText("完成");
                handler.sendEmptyMessage(SHOW_BUTTON);
                currentHeight = 0;
            } else {
                hideButton();
                if (currentUri != null) {
                    updateImage();
                }
            }
        } else if (v == btnPhotosChoose) {
            // Handle clicks for btnPhotosChoose
            if (ContextCompat.checkSelfPermission(PhotoActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PhotoActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openAlbum();
            }
        } else if (v == btnPhotograph) {
            // Handle clicks for btnPhotograph
            openCamera();
        } else if (v == btnPhotoCancel) {
            // Handle clicks for btnPhotoCancel
            hideButton();
        }
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        try {
            File outputImage = new File(getExternalCacheDir(), ""+System.currentTimeMillis());
            outputImage.createNewFile();
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(PhotoActivity.this, "com.xiaoluogo.goodtochat.fileprovider", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, Constants.TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void updateImage() {
        progress_upload.setVisibility(View.VISIBLE);
        tv_upload.setVisibility(View.VISIBLE);
        String fileName = user.getNickName() + System.currentTimeMillis();
        File localFile = new File(currentUri);
        final BmobFile file = new BmobFile(localFile);
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    user.setHeaderImage(file.getFileUrl());
                    user.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                loadPhoto(user.getHeaderImage());
                                tv_upload.setText("上传成功");
                                BmobIM.getInstance().getUserInfo(user.getObjectId());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress_upload.setVisibility(View.GONE);
                                        tv_upload.setVisibility(View.GONE);
                                    }
                                }, 500);
                            } else {
                                uploadFail();
                            }
                        }
                    });
                } else {
                    uploadFail();
                }
            }

            @Override
            public void onProgress(final Integer value) {
                if (value < 100) {
                    tv_upload.setText("上传中..." + value + "%");
                }
            }
        });


    }

    private void uploadFail() {
        loadPhoto(headerUri);
        tv_upload.setText("上传失败");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_upload.setVisibility(View.GONE);
                tv_upload.setVisibility(View.GONE);
                currentUri = null;
            }
        }, 1000);
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, Constants.CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "请打开相册权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    currentUri = imageUri.getPath();
                    loadPhoto(currentUri);
                }
                break;
            case Constants.CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4版本以上用这个
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri,则通过document id 处理;
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接截取图片路径即可
            imagePath = uri.getPath();
        }
        currentUri = imagePath;
        loadPhoto(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        currentUri = imagePath;
        loadPhoto(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 隐藏按钮
     */
    private void hideButton() {
        currentHeight = rlChangePhotoHeight;
        isFinish = false;
        btnChangeHeader.setText("修改");
        handler.sendEmptyMessage(HIDE_BUTTON);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_BUTTON:
                    removeMessages(SHOW_BUTTON);
                    currentHeight += rlChangePhotoHeight / 20;
                    rlChangePhoto.setPadding(0, (int) (rlChangePhotoHeight - currentHeight), 0, 0);
                    if (currentHeight < rlChangePhotoHeight) {
                        sendEmptyMessageDelayed(SHOW_BUTTON, 10);
                    }
                    break;
                case HIDE_BUTTON:
                    removeMessages(HIDE_BUTTON);
                    currentHeight -= rlChangePhotoHeight / 20;
                    rlChangePhoto.setPadding(0, (int) (rlChangePhotoHeight - currentHeight), 0, 0);
                    if (currentHeight > 0) {
                        sendEmptyMessageDelayed(HIDE_BUTTON, 10);
                    }
                    break;
            }
        }
    };

    /**
     * 左上角返回按钮点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
