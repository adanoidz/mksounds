package com.adanoidz.surfaceviewtest.Graphics;


import android.graphics.Canvas;

public interface Renderable {

	boolean visible = true;

	public void setVisible(boolean visible);
	public void render (Canvas c);

}
