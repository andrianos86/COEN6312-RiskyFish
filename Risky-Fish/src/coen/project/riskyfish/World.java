package coen.project.riskyfish;

import java.awt.Graphics;

import coen.project.riskyfish.gfx.Background;

public class World {

	// viewable size of the world
	private int pWidth;
	private int pHeight;

	// total world size
	private int worldWidth;
	private int worldHeight;

	// viewable bounding corner coordinates
	private int xmin;
	private int xmax;
	private int ymin;
	private int ymax;

	// speed the world is moving at
	private double scrollSpeed;

	// background animation
	private Background oceanBg;
	//
	private String bgName = Background.BG_RES;

	private static final int MIN_SEPERATION = 40; // minimum separation between
													// obstacles

	public World(int viewWidth, int viewHeight) {
		this.scrollSpeed = 0;

		this.loadBackground(bgName);

		this.pWidth = viewWidth;
		this.pHeight = viewHeight;
		
		this.worldHeight = pHeight;
		this.worldWidth = 2 * pWidth;

		xmin = 0;
		ymin = 0;
		ymax = ymin + this.pHeight;
		xmax = xmin + this.pWidth;
	}
	
	
	
	
	public int getWorldWidth(){
		return this.worldWidth;
	}
	
	public int getWorldHeight(){
		return this.worldHeight;
	}

	public void loadBackground(String bgName) {
		try {
			oceanBg = new Background(bgName, 1);
			oceanBg.setVector(scrollSpeed, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int minSeperation() {
		return this.MIN_SEPERATION;
	}

	public Background getBackground() {
		return oceanBg;
	}

	public double getSpeed() {
		return this.scrollSpeed;
	}

	public void setSpeed(double speed) {
		// world should always move to the right
		if (speed > 0) {
			this.scrollSpeed = -speed;
		}
		this.scrollSpeed = speed;
	}

	public int getWidth() {
		return pWidth;
	}

	public void setWidth(int width) {
		this.pWidth = width;
	}

	public int getHeight() {
		return pHeight;
	}

	public void setHeight(int height) {
		this.pHeight = height;
	}

	public int getXmin() {
		return xmin;
	}

	private void setXmin(int xmin) {
		this.xmin = xmin;
	}

	public int getXmax() {
		return xmax;
	}

	private void setXmax(int xmax) {
		this.xmax = xmax;
	}

	public int getYmin() {
		return ymin;
	}

	private void setYmin(int ymin) {
		this.ymin = ymin;
	}

	public int getYmax() {
		return ymax;
	}

	private void setYmax(int ymax) {
		this.ymax = ymax;
	}

	public void update() {
		// update background speed
		oceanBg.setVector(scrollSpeed, 0);
		oceanBg.update();
	}

	public void draw(Graphics g) {
		oceanBg.draw(g);
	}
}
