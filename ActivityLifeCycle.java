package com.adanoidz.mortalkombatsounds;


public interface ActivityLifeCycle {

	void onPause();
	void onStop(boolean isFinishing);
	void onResume();
}
