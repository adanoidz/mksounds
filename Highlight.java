package com.adanoidz.surfaceviewtest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.adanoidz.surfaceviewtest.Graphics.Render;
import com.adanoidz.surfaceviewtest.Graphics.Renderable;

public class Highlight implements Renderable{

	private Rect bounds;
	private int color;
	private boolean isAnimating;
	private Paint paint;
	private int frameCounter;
	private boolean grow, shrink;

	private Rect[] edgeBounds = new Rect[4];

	public Highlight(int color){
		this.color = color;
		paint = new Paint();
		paint.setColor(color);

		grow = false;
		shrink = false;
	}

	public void setBounds(Rect bounds){
		endAnimation();
		this.bounds = bounds;
		calculateBounds();
		startAnimation();
	}

	private void calculateBounds(){
		int stroke = bounds.width() / 15;
		edgeBounds[0] = new Rect(bounds.left, bounds.top, bounds.left + stroke, bounds.top + bounds.height());
		edgeBounds[1] = new Rect(bounds.left + stroke, bounds.top, bounds.left + bounds.width() - stroke, bounds.top + stroke);
		edgeBounds[2] = new Rect(bounds.right - stroke, bounds.top, (bounds.right - stroke) + stroke, bounds.top + bounds.height());
		edgeBounds[3] = new Rect(bounds.left + stroke, bounds.bottom - stroke, bounds.left + bounds.width() - stroke, (bounds.bottom - stroke) + stroke);
	}

	@Override
	public void setVisible(boolean visible) {

	}

	public void update(){
		if(isAnimating){
			animate();
		}

	}

	private void animate(){
		if(grow){
			frameCounter += 2;
		}else if(!grow){
			frameCounter -= 2;
		}

		if(frameCounter >= 60)grow = false;
		if(frameCounter <= 0)grow = true;

		if(frameCounter % 5 == 0){
			int percent = (frameCounter / 5) * 10;
			paint.setAlpha(percent);
		}
	}

	@Override
	public void render(Canvas c) {
		if(!isAnimating) return;
		for(int i = 0; i < edgeBounds.length; i++){
			c.drawRect(edgeBounds[i], paint);
		}
	}

	private void startAnimation(){
		isAnimating = true;
		frameCounter = 49;
		grow = true;
	}

	private void endAnimation(){
		isAnimating = false;
		frameCounter = 0;
		grow = false;
	}
}
