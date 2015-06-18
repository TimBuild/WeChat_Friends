package com.matrix.wechat.utils.voice;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class RecordVoice {

	private static String mFileName = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/test.3gp";
	private MediaRecorder mRecorder = null;

	private static long start = 0;
	private static long end = 0;
	private static long endtime = 0;

	public void startRecording() {
		Log.d("RecordVoice", mFileName);
		mRecorder = new MediaRecorder();

		// 设置音源为Micphone
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置封装格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		// 设置编码格式
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e("VoiceRecord", "prepare() failed");
		}

		start = System.currentTimeMillis();
		mRecorder.start();
	}

	public boolean stopRecording() {
		end = System.currentTimeMillis();
		boolean result = false;
		if (end - start <= 1000) {
			if (mRecorder != null)
				mRecorder.release();
			return result;
		}
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		result = true;
		return result;
	}
	
	public Long calcuteVoice(){
		long endtim = System.currentTimeMillis();
		endtime = endtim - start;
		return endtime;
	}

}
