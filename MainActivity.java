package com.adanoidz.surfaceviewtest;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.adanoidz.surfaceviewtest.Audio.Audio;
import com.adanoidz.surfaceviewtest.Screen.GameView;

public class MainActivity extends Activity {

	MortalKombat mk;
	GameView loadedView;
	Audio audio;
	//Graphics graphics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		audio = new Audio(this);
		//screen = new Screen(this);
		mk = new MortalKombat(this, this, audio);
		loadedView = mk;
		setContentView(mk);
	}

	public void switchView(View v) {
		setContentView(v);
	}

	public void onPause() {
		super.onPause();

		loadedView.onPause();
	}

	public void onResume() {
		super.onResume();

		loadedView.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		loadedView.onStop(isFinishing());

		if (isFinishing()) {
			//Shutdown any services running
		}
	}

	public void onBackPressed(){
		loadedView.onBackPressed();
	}

}