package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class OnScreenObject {


	// Restrict objects within screen
	protected int maxY;
	protected int minY;
	protected int maxX;
	protected int minX;

	// world
	protected World ocean;

	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;

	// dimensions (sprite)
	protected int width;
	protected int height;

	// collision box (object size)
	protected int cwidth;
	protected int cheight;

	// collision
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;

	// Animation
	protected Animation animation;
	protected int currentAction;
	protected boolean facingRight;

	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double minSpeed;
	protected double gravity;

	// constructor
	public OnScreenObject(World ocean) {
		this.ocean = ocean;
		animation = new Animation();
		facingRight = true;

	}
	
	protected void setBounds(){
		//must be called after setting height (end of constructor)
		maxY = GamePanel.HEIGHT - this.height;
		minY = 0;
		maxX = GamePanel.WIDTH;
		minX = 0;
	}

	public boolean intersects(OnScreenObject other) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = other.getRectangle();
		return r1.intersects(r2);
	}

	public boolean intersects(Rectangle r) {
		return getRectangle().intersects(r);
	}

	public boolean contains(OnScreenObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}

	public boolean contains(Rectangle r) {
		return getRectangle().contains(r);
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) x, (int) y, cwidth, cheight);
	}

	public void checkTopBottomCollision() {
		xdest = x + dx;
		ydest = y + dy;

		xtemp = x;
		ytemp = y;
	}

	public int getx() {
		return (int) x;
	}

	public int gety() {
		return (int) y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCWidth() {
		return cwidth;
	}

	public int getCHeight() {
		return cheight;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public boolean notOnScreen() {
		return x + width < 0 || x - width > GamePanel.WIDTH || y + height < 0 || y - height > GamePanel.HEIGHT;
	}

	public void draw(Graphics g) {
		if (facingRight) {
			g.drawImage(animation.getImage(), (int) (x), (int) (y), null);
		} else {
			g.drawImage(animation.getImage(), (int) (x + width), (int) (y ), -width, height,
					null);
		}
		Rectangle r = getRectangle();
		g.drawRect(r.x, r.y, r.width, r.height);
	}

}
