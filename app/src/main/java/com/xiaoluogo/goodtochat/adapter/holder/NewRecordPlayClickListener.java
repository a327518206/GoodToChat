package com.xiaoluogo.goodtochat.adapter.holder;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.widget.ImageView;

import com.xiaoluogo.goodtochat.R;
import com.xiaoluogo.goodtochat.db.ChatMessage;

import java.io.File;
import java.io.FileInputStream;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.v3.BmobUser;

public class NewRecordPlayClickListener implements View.OnClickListener {

	private final String mFilePath;
	private final ChatMessage mdata;
	ImageView iv_voice;
	private AnimationDrawable anim = null;
	Context mContext;
	MediaPlayer mediaPlayer = null;
	public static boolean isPlaying = false;
	public static NewRecordPlayClickListener currentPlayListener = null;

	public NewRecordPlayClickListener(Context context, ChatMessage data, ImageView voice) {
		this.iv_voice = voice;
		this.mdata = data;
		this.mFilePath = mdata.getMessage();
		this.mContext = context.getApplicationContext();
		currentPlayListener = this;
	}

	@SuppressWarnings("resource")
	public void startPlayRecord(String filePath, boolean isUseSpeaker) {
		if (!(new File(filePath).exists())) {
			return;
		}
		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = new MediaPlayer();
		if (isUseSpeaker) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		}

		try {
			mediaPlayer.reset();
			// 单独使用此方法会报错播放错误:setDataSourceFD failed.: status=0x80000000
			// mediaPlayer.setDataSource(filePath);
			// 因此采用此方式会避免这种错误
			FileInputStream fis = new FileInputStream(new File(filePath));
			mediaPlayer.setDataSource(fis.getFD());
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer arg0) {
					isPlaying = true;
					arg0.start();
					startRecordAnimation();
				}
			});
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							stopPlayRecord();
						}

					});
			currentPlayListener = this;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopPlayRecord() {
		stopRecordAnimation();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
	}

	private void startRecordAnimation() {
		if (mdata.getMessageType() == 7) {
			iv_voice.setImageResource(R.drawable.anim_chat_voice_right);
		} else {
			iv_voice.setImageResource(R.drawable.anim_chat_voice_left);
		}
		anim = (AnimationDrawable) iv_voice.getDrawable();
		anim.start();
	}

	private void stopRecordAnimation() {
		if (mdata.getMessageType() == 7) {
			iv_voice.setImageResource(R.drawable.voice_left3);
		} else {
			iv_voice.setImageResource(R.drawable.voice_right3);
		}
		if (anim != null) {
			anim.stop();
		}
	}

	@Override
	public void onClick(View arg0) {
		if (isPlaying) {
			currentPlayListener.stopPlayRecord();
		}
		startPlayRecord(mFilePath, true);
	}

}