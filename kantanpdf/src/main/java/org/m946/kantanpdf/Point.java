package org.m946.kantanpdf;

public class Point {
	private float x;
	private float y;
	
	public Point(float x, float y){
		this.x = x;
		this.y = y;
	}

	public Point(){
		x = 0f;
		y = 0f;
	}
	
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}
