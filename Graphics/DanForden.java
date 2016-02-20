package com.adanoidz.mortalkombatsounds.Graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class DanForden extends Portrait {

	private boolean isAnimating;
	private int x, y, animCounter, interval, pauseCounter;
	private boolean grow, pauseActive;

	public DanForden(Bitmap b, double scale) {
		super(b, scale);
		interval = b.getWidth() / 3;
	}

	public void update() {
		if (isAnimating) {
			animCounter++;
			if (x <= bounds.left - bounds.width()) {
				grow = false;
				pauseActive = true;
			}

			if (x > bounds.left) {
				isAnimating = false;
				return;
			}

			if (pauseActive) {
				pauseCounter--;
				if (pauseCounter == 0) {
					pauseActive = false;
				} else {
					return;
				}
			}

			if (grow) {
				x -= interval;
			} else {
				x += interval;
			}


			}

		}

	public void render(Canvas c) {
		if (!isAnimating) return;
		c.drawBitmap(bitmap, x, bounds.top, null);
	}

	public void getToasty() {
		isAnimating = true;
		x = bounds.left;
		y = bounds.top;
		grow = true;
		pauseCounter = 15;
	}

}
