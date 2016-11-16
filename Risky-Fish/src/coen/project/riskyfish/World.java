package coen.project.riskyfish;

import java.awt.Graphics;

import coen.project.riskyfish.gfx.Background;

public class World {

	// size
	private int width;
	private int height;

	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;

	private double scrollSpeed;
	private Background oceanBg;

	private static final int MIN_SEPERATION = 40; // minimum separation between
													// obstacles

	public World(int viewWidth, int viewHeight, int rightOffset, double worldSpeed) {
		this.scrollSpeed = worldSpeed;
		try {
			oceanBg = new Background(Background.BG_RES,1);
			oceanBg.setVector(scrollSpeed, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.width = viewWidth;
		this.height = viewHeight;

		xmin = 0.0;
		ymin = 0.0;
		ymax = ymin + this.height;
		xmax = xmin + this.width + rightOffset * this.width;

	}
	
	public int requiredObjectSeperation(){
		return this.MIN_SEPERATION;
	}

	public Background getBackground() {
		return oceanBg;
	}

	public double getScrollingSpeed() {
		return this.scrollSpeed;
	}

	public void setScrollingSpeed(double speed) {
		if (speed > 0) {
			this.scrollSpeed = -speed;
		}
		this.scrollSpeed = speed;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getXmin() {
		return xmin;
	}

	public void setXmin(double xmin) {
		this.xmin = xmin;
	}

	public double getXmax() {
		return xmax;
	}

	public void setXmax(double xmax) {
		this.xmax = xmax;
	}

	public double getYmin() {
		return ymin;
	}

	public void setYmin(double ymin) {
		this.ymin = ymin;
	}

	public double getYmax() {
		return ymax;
	}

	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	
	public void update(){
		oceanBg.setVector(scrollSpeed, 0);
		oceanBg.update();
	}
	public void draw(Graphics g){
		oceanBg.draw(g);
	}
}
