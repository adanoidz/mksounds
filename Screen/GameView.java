package com.adanoidz.mortalkombatsounds.Screen;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.adanoidz.mortalkombatsounds.ActivityLifeCycle;
import com.adanoidz.mortalkombatsounds.Audio.Audio;

public class GameView extends SurfaceView implements ActivityLifeCycle, View.OnTouchListener, Runnable{

	protected final Activity activity;
	protected final Context context;
	protected final Audio audio;

	//Fields
	// array of gameObjects
	// array of mobs
	// array of sound effects?
	// music files

	public GameView(Activity activity, Context context, Audio audioIn){
		super(context);
		this.activity = activity;
		this.context = context;
		this.audio = audioIn;
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onStop(boolean isFinishing) {

	}

	@Override
	public void onResume() {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public void run() {

	}

	public void onBackPressed(){
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
