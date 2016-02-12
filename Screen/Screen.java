//
//package com.adanoidz.surfaceviewtest.Screen;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.Typeface;
//import android.media.MediaPlayer;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.View;
//
//import com.adanoidz.surfaceviewtest.ActivityLifeCycle;
//import com.adanoidz.surfaceviewtest.Audio.Audio;
//
//public class Screen extends GameView implements Runnable, View.OnTouchListener, ActivityLifeCycle {
//	Thread gameLoop;
//	SurfaceHolder holder;
//	Canvas canvas;
//	volatile boolean running;
//	final double fps = 60.0;
//	double averageFPS = 0.0;
//	double frameLength = 1000 / fps;
//	Context context;
//	Paint paint = new Paint();
//	Paint boxPaint = new Paint();
//	int width, height;
//	Rect buttonBounds;
//	Color boxColor;
//	Audio audio;
//	MediaPlayer mediaPlayer;
//
//
//	public Screen(Context context, Audio audio) {
//		super(context);
//		this.context = context;
//		this.audio = audio;
//		setOnTouchListener(this);
//		holder = getHolder();
//
//		paint.setColor(Color.rgb(255, 255, 255));
//		paint.setTypeface(Typeface.DEFAULT);
//		boxPaint.setColor(Color.rgb(118, 255, 3));
//
//		width = getWidth();
//		height = getHeight();
//		buttonBounds = new Rect();
//	}
//
//	public void run() {
//		long deltaTime;
//		long lastFrame = System.currentTimeMillis();
//		int counter = 0;
//		long deltaElapsed = 0;
//
//		mediaPlayer = audio.createMusic("sounds/media1.ogg", false, false);
//		mediaPlayer.start();
//		Log.e("Media", "MP is " +mediaPlayer.toString());
//
//
//		while (running) {
//			deltaTime = System.currentTimeMillis() - lastFrame;
//			lastFrame = System.currentTimeMillis();
//
//			if (!holder.getSurface().isValid()) {
//				continue;
//			}
//
//			update();
//			render();
//			/*counter++;
//			deltaElapsed += deltaTime;
//			if(counter >= 60){
//				averageFPS = deltaElapsed / frameLength;
//				counter = 0;
//				deltaElapsed = 0;
//			} */
//		}
//	}
//
//	private void update() {
//	}
//
//	private void render() {
//		canvas = holder.lockCanvas();
//		width = canvas.getWidth();
//		height = canvas.getHeight();
//		buttonBounds.set(5, 5, width - 5, height - 5);
//
//		if (audio.isMusicPaused()) {
//			canvas.drawRGB(125, 125, 125);
//		} else {
//			canvas.drawRGB(0, 0, 0);
//		}
//		canvas.drawRect(buttonBounds, paint);
//		//canvas.drawText("Set FPS: " + fps, 10, 10, paint);
//		//canvas.drawText("Current FPS: " + averageFPS, 10, 30, paint);
//		holder.unlockCanvasAndPost(canvas);
//	}
//
//	@Override
//	public void onResume() {
//		running = true;
//		gameLoop = new Thread(this);
//		audio.onResume();
//		gameLoop.start();
//	}
//
//	@Override
//	public void onPause() {
//		running = false;
//		audio.onPause();
//		if (gameLoop != null) {
//			try {
//				gameLoop.join();
//			} catch (Exception e) {
//				// Retry
//			}
//		}
//	}
//
//
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		int action = event.getAction();
//
//		switch (action) {
//			case MotionEvent.ACTION_DOWN:
//				break;
//			case MotionEvent.ACTION_UP:
//				if(buttonBounds.contains((int)event.getX(), (int) event.getY())){
//					if (!audio.isMusicPaused()) {
//						audio.setMediaPaused(true);
//					} else {
//						audio.setMediaPaused(false);
//					}
//				}
//				break;
//		}
//		return true;
//	}
//
//	@Override
//	public void onStop(boolean isFinishing) {
//
//		audio.onStop(isFinishing);
//	}
//}
