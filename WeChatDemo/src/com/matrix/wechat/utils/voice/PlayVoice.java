package com.matrix.wechat.utils.voice;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

public class PlayVoice {
	private static MediaPlayer mPlayer;

	/*
	 * public PlayVoice (MediaPlayer mPlayer) { this.mPlayer=mPlayer; }
	 */

	public static void startPlaying(String url) {

		try {
			mPlayer = new MediaPlayer();
			// 设置要播放的文件
			mPlayer.setDataSource(url);

			mPlayer.prepare();
			// 播放之
			System.out.println("准备好了");
			mPlayer.start();
			System.out.println("开始播放");
		} catch (IOException e) {
			Log.e("PlayVoice", "prepare() failed");
		}
	}
}
