package com.adanoidz.mortalkombatsounds;

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

import com.adanoidz.mortalkombatsounds.Audio.Audio;
import com.adanoidz.mortalkombatsounds.Graphics.DanForden;
import com.adanoidz.mortalkombatsounds.Graphics.Portrait;
import com.adanoidz.mortalkombatsounds.Screen.GameView;

import java.io.IOException;
import java.util.Random;


public class MortalKombat extends GameView implements Runnable, SurfaceHolder.Callback {

	Random random;
	// Fields
	private int gridPosition;
	private String[] names;
	private Portrait[] sprites;

	private int[] nameSoundsId;
	private Thread thread;
	private boolean running;
	private SurfaceHolder holder;
	private MediaPlayer media;
	private Paint textPaint;
	private String lastCharacter;
	private int currentCharacterIndex, previousCharacterIndex;
	private boolean startScreen;
	private Highlight highlight;
	private boolean surfaceCreated;

	// Dan Forden Fields
	private DanForden danForden;
	private boolean isDanToasty;
	private int danCounter;
	private final int DAN_RANDOM = 25;
	private int danToastySound;


	// Surface size from display metrics
	private int WIDTH, HEIGHT;


	Portrait portrait;

	public MortalKombat(Activity activity, Context context, Audio audioIn) {
		super(activity, context, audioIn);
		audio.createSoundPool(20);

		holder = getHolder();
		holder.addCallback(this);
		random = new Random();
		setOnTouchListener(this);

		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);

		highlight = new Highlight(Color.GREEN);
		init();
	}

	@Override
	public void run() {
		populateGrid();
		while (running) {
			update();
			render();
		}
	}

	private void init() {
		names = context.getResources().getStringArray(R.array.mkCharacterNames);
		sprites = new Portrait[names.length];
		nameSoundsId = new int[names.length];
		AssetManager am = context.getAssets();
		currentCharacterIndex = -1;
		previousCharacterIndex = -1;

		int width = context.getResources().getDisplayMetrics().widthPixels;
		int height = context.getResources().getDisplayMetrics().heightPixels;
		int scaleY = (int) (height * 0.8) / 3;
		double scale = 0;

		for (int i = 0; i < names.length; i++) {
			// get value name of array item, hard-coded so language is not important!
			String fileName = context.getResources().getStringArray(R.array.mkSoundFileNames)[i];
			nameSoundsId[i] = audio.loadSoundFx("sounds/" + fileName + ".ogg");
			// Load graphics to Sprites
			try {
				Bitmap tempB = BitmapFactory.decodeStream(am.open("images/" + fileName + ".png"));
				if (scale == 0) {
					scale = scaleY / tempB.getHeight();
				}
				sprites[i] = new Portrait(tempB, scale);
			} catch (IOException e) {
				e.printStackTrace();
			}



		}


		// Load Dan Forden Graphics
		try {
			danForden = new DanForden(BitmapFactory.decodeStream(am.open("images/dan_forden.png")), scale);
		} catch (IOException e) {
		}

		media = audio.createMusic("sounds/playerSelect.wav", false, true);
		media.setVolume(0.3f, 0.3f);
		media.start();


		randomCharacter();
		setupDan();
	}

	private void setupDan() {
		danCounter = random.nextInt(DAN_RANDOM) + 1;
		danForden.setRect(WIDTH, HEIGHT - danForden.getBitmap().getHeight());
		isDanToasty = false;
		if(danToastySound == 0){
			danToastySound = audio.loadSoundFx("sounds/toasty.ogg");
		}
	}

	private void populateGrid() {
		WIDTH = holder.getSurfaceFrame().width();
		HEIGHT = holder.getSurfaceFrame().height();

		int spriteHeight = sprites[0].getBitmap().getHeight();
		int spriteWidth = sprites[0].getBitmap().getWidth();

		int leftX = (int) (WIDTH / 2 - (spriteWidth * 2.5));
		int topY = (int) (HEIGHT / 2 - (spriteHeight * 1.5));


		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 5; x++) {
				sprites[x + (y * 5)].setRect(leftX + (x * spriteWidth), topY + (y * spriteHeight));
			}
		}

		danForden.setRect(WIDTH, HEIGHT - danForden.getBitmap().getHeight());
	}

	private void update() {
		highlight.update();
		danForden.update();
	}

	private void render() {
		Canvas c = holder.lockCanvas();
		c.drawRGB(125, 125, 125);
		drawGrid(c);
		highlight.render(c);
		danForden.render(c);

		holder.unlockCanvasAndPost(c);

	}

	private void drawGrid(Canvas c) {
		for (int i = 0; i < sprites.length; i++) {
			sprites[i].render(c);
		}
	}

	@Override
	public void onPause() {
		running = false;
		if (thread != null) {
			try {
				thread.join();
				thread = null;
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
			if (surfaceCreated) {
				thread.start();
			}
		}
	}

	private int randomCharacter() {
		int num = random.nextInt(nameSoundsId.length);
		lastCharacter = names[num];
		currentCharacterIndex = num;
		return num;
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		int action = e.getAction();

		boolean faceTouched = false;

		switch (action) {
			case MotionEvent.ACTION_UP:
				// TODO Limit number of lookups to portraits by narrowing down search area
				for (int i = 0; i < names.length; i++) {
					if (sprites[i].getRect().contains((int) e.getX(), (int) e.getY())) {
						if (previousCharacterIndex == -1) previousCharacterIndex = i;
						previousCharacterIndex = currentCharacterIndex;

						lastCharacter = names[i];
						currentCharacterIndex = i;
						faceTouched = true;
						if (currentCharacterIndex != previousCharacterIndex) {
							highlight.setBounds(sprites[i].getRect());
						}


						break;
					}
				}

				// Handle Dan Forden event
				danCounter--;
				if (danCounter == 0) {
					danForden.getToasty();
					audio.playSound(danToastySound, 1); // TODO - Load this sound
					setupDan();
				}

				if (faceTouched) {
					audio.playSound(nameSoundsId[currentCharacterIndex], 1);
				}
				break;
		}

		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		running = true;
		surfaceCreated = true;
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}
}