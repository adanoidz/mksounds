package com.adanoidz.surfaceviewtest.Graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Portrait implements Renderable{


	Bitmap bitmap;
	int width, height;
	boolean visible;
	Rect bounds;

	public Portrait(Bitmap b){
		//TODO Remove this scaling when you can stretch an image that is rendered
		bitmap = Bitmap.createScaledBitmap(b, b.getWidth() * 3, b.getHeight() * 3, false);
		width = bitmap.getWidth();
		height = bitmap.getHeight();


		visible = true;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void render(Canvas canvas) {
		canvas.drawBitmap(bitmap, bounds.left, bounds.top, null);
	}

	public void setRect(int x, int y){
		bounds = new Rect();
		bounds.set(x, y, x + width, y + height);
	}

	public Rect getRect(){
		return bounds;
	}
}
