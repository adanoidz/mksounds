package com.adanoidz.surfaceviewtest;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.adanoidz.surfaceviewtest.Audio.Audio;
import com.adanoidz.surfaceviewtest.Graphics.Sprite;
import com.adanoidz.surfaceviewtest.Screen.GameView;

import java.io.IOException;
import java.util.Random;


public class MortalKombat extends GameView implements Runnable {

	Random random;
	// Fields
	private int gridPosition;
	private String[] names;
	private Sprite[] sprites;
	private int[] nameSoundsId;
	private Thread thread;
	private boolean running;
	private SurfaceHolder holder;
	private MediaPlayer media;
	private Paint textPaint;
	private String lastCharacter;
	private int currentCharacterIndex, previousCharacterIndex;
	private boolean startScreen;
	

	private int WIDTH, HEIGHT;


	Sprite portrait;

	public MortalKombat(Activity activity, Context context, Audio audioIn) {
		super(activity, context, audioIn);
		audio.createSoundPool(20);

		holder = getHolder();
		random = new Random();
		setOnTouchListener(this);

		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
	}

	@Override
	public void run() {
		init();
		while (running) {
			render();
		}
	}

	private void init() {
		names = context.getResources().getStringArray(R.array.mkCharacterNames);
		sprites = new Sprite[names.length];
		nameSoundsId = new int[names.length];
		AssetManager am = context.getAssets();
		currentCharacterIndex = -1;
		previousCharacterIndex = -1;

		for (int i = 0; i < names.length; i++) {
			// get value name of array item, hard-coded so language is not important!
			String fileName = context.getResources().getStringArray(R.array.mkSoundFileNames)[i];
			nameSoundsId[i] = audio.loadSoundFx("sounds/" + fileName + ".ogg");
			// Load graphics to Sprites
			try {
				Bitmap tempB = BitmapFactory.decodeStream(am.open("images/" + fileName + ".png"));
				sprites[i] = new Sprite(tempB);
			} catch (IOException e) {
				e.printStackTrace();
			}

			randomCharacter();
		}

		media = audio.createMusic("sounds/playerSelect.wav", false, true);
		media.setVolume(0.5f, 0.5f);
		media.start();

		WIDTH = holder.getSurfaceFrame().width();
		HEIGHT = holder.getSurfaceFrame().height();
	}

	private void render() {
		Canvas canvas = holder.lockCanvas();
		canvas.drawRGB(125, 125, 125);
		drawGrid(canvas);

		holder.unlockCanvasAndPost(canvas);

	}

	private void drawGrid(Canvas c){
		int spriteHeight = sprites[0].getBitmap().getHeight();
		int spriteWidth = sprites[0].getBitmap().getWidth();

		int leftX = (int) (c.getWidth()/2 - (spriteWidth * 2.5));
		int topY = (int) (c.getHeight()/2 - (spriteHeight * 1.5));


		for(int y = 0; y < 3; y++){
			for(int x = 0; x < 5; x++){
				sprites[x + (y * 5)].setRect(leftX + (x * spriteWidth), topY + (y * spriteHeight));
				c.drawBitmap(sprites[x + (y * 5)].getBitmap(), leftX + (x * spriteWidth),
						topY + (y * spriteHeight), null);
			}
		}
	}

	@Override
	public void onPause() {
		running = false;
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// retry
			}
		}
	}

	@Override
	public void onStop(boolean isFinishing) {
		audio.onStop(isFinishing);
	}

	@Override
	public void onResume() {
		running = true;

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	private int randomCharacter(){
		int num = random.nextInt(nameSoundsId.length);
		lastCharacter = names[num];
		currentCharacterIndex = num;
		return num;
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		int action = e.getAction();

		boolean faceTouched = false;
		for(int i = 0; i < names.length; i++){
			if(sprites[i].getRect().contains((int) e.getX(), (int) e.getY())){
				if(previousCharacterIndex == -1) previousCharacterIndex = i;
				previousCharacterIndex = currentCharacterIndex;

				lastCharacter = names[i];
				currentCharacterIndex = i;
				faceTouched = true;
				break;
			}
		}

		switch (action) {
			case MotionEvent.ACTION_UP:
				if(faceTouched){
					audio.playSound(nameSoundsId[currentCharacterIndex], 1);
				}
				break;
		}

		return true;
	}

}