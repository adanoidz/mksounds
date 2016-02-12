package com.adanoidz.surfaceviewtest.Graphics;

import android.content.Context;

public class Render {

	private final int WIDTH;
	private final int HEIGHT;
	private Context context;
	private int[] pixels;

	private int xOffset;
	private int yOffset;

	public Render(Context context, int width, int height){
		this.context = context;
		WIDTH = width;
		HEIGHT = height;

		pixels = new int[WIDTH * HEIGHT];
	}

	public void renderSprite(int x, int y){
		//TODO add sprite here

	}
}
