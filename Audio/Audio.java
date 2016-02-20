package com.adanoidz.mortalkombatsounds.Audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class Audio {

	private Context context;
	private AssetManager am;
	private SoundPool soundPool;
	private MediaPlayer mediaPlayer;
	private boolean mediaPaused = false;

	public Audio(Context context) {
		this.context = context;
		am = context.getAssets();
	}

	/**
	 * Create a new music player from a filepath.
	 * MediaPlayer object returned is already prepared to be played
	 *
	 * @param file        The filepath for the sound file to be streamed
	 * @param newInstance is this a separate instance of music to the core audio music player
	 * @return reference to the Audio class MediaPlayer object
	 */
	public MediaPlayer createMusic(String file, boolean newInstance, boolean looping) {
		MediaPlayer temp;

		if (newInstance) {
			temp = new MediaPlayer();
		} else {
			// Load new file onto old reference
			unloadMusic();
			mediaPlayer = new MediaPlayer();
			temp = mediaPlayer;
		}

		try {
			AssetFileDescriptor asset = am.openFd(file);
			temp.setDataSource(asset.getFileDescriptor(), asset.getStartOffset(), asset.getLength());
			temp.setLooping(looping);
			temp.prepare();

		} catch (IOException e) {
			Log.e("AudioClass", "createMusic: Unable to open media from String. File not available");
		}
		return temp;
	}

	// Release and nullify the media player to free up resources
	public void unloadMusic() {
		if (mediaPlayer == null) {
			return;
		}
		if (mediaPlayer.isPlaying() || mediaPaused) {
			mediaPlayer.stop();
			mediaPaused = false;
		}
		mediaPlayer.reset();
		mediaPlayer.release();
		mediaPlayer = null;
	}

	public boolean isMusicPaused() {
		return mediaPaused;
	}

	public void createSoundPool(int numStreams) {
		if (soundPool == null) {
			soundPool = new SoundPool(numStreams, AudioManager.STREAM_MUSIC, 1);
		}
	}

	public int loadSoundFx(String path) {
		AssetFileDescriptor assetFileDescriptor;
		int idNum = -1;

		try {
			assetFileDescriptor = am.openFd(path);
			idNum = soundPool.load(assetFileDescriptor, 1);
		} catch (IOException e) {
			Log.e("Audio", "SoundPool: Cannot load file from path: " + path);
		}

		return idNum;
	}

	public void playSound(int id, float volume) {
		soundPool.play(id, volume, volume, 0, 0, 1);
	}

	public void stopSound(int id){
		soundPool.stop(id);
	}

	/**
	 * General use pause method to safely pause and resume all media types at once,
	 * for instance when a popup or ad is displayed
	 */
	public void setMediaPaused(boolean pause) {
		if (pause) {
			onPause();
		} else {
			onResume();
		}
	}

	// ----------------------- Activity Lifecycle Methods ----------------------- //

	public void onStop(boolean isFinishing) {
		// TODO Close all instanced Audio objects in Audio.onStop()
		if (mediaPlayer != null && (mediaPlayer.isPlaying() || mediaPaused)) {
			mediaPlayer.pause();
			if (isFinishing) {
				unloadMusic();
			}
		}

		if (soundPool != null && isFinishing) {

			soundPool.release();
			soundPool = null;
		}
	}

	public void onPause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPaused = true;
			mediaPlayer.pause();
		}
	}

	public void onResume() {
		if (mediaPlayer != null && mediaPaused) {
			mediaPlayer.start();
			mediaPaused = false;
		}
	}
}