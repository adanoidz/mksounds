package com.adanoidz.mortalkombatsounds.Graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Portrait implements Renderable{


	Bitmap bitmap;
	int width, height;
	boolean visible;
	private double scale;
	Rect bounds;

	public Portrait(Bitmap b, double scale){
		//TODO Remove this scaling when you can stretch an image that is rendered
		bitmap = Bitmap.createScaledBitmap(b, (int) (b.getWidth() * scale), (int) (b.getHeight() * scale), false);
		width = bitmap.getWidth();
		height = bitmap.getHeight();

		this.scale = scale;

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
