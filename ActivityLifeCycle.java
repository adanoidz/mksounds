package com.adanoidz.surfaceviewtest;


public interface ActivityLifeCycle {

	void onPause();
	void onStop(boolean isFinishing);
	void onResume();
}
