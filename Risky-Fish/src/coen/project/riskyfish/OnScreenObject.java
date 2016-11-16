package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class OnScreenObject {

	// world
	protected World ocean;

	// Restrict objects within screen
	protected int maxY;
	protected int minY;
	protected int maxX;
	protected int minX;

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

	// Animation
	protected Animation animation;
	protected int currentAnimation=0;

	// Set to true if object should be drawn.
	protected boolean isActive = false;

	// When set to true, the sprite is removed from its world panel.
	private boolean discarded = false;

	// if true, object does not collide with other object except from the player
	// fish.
	private boolean immuneToOthers;

	// Describes what should happen when hitting the top or bottom of the screen
	/**
	 * A value for the exitPolicy property, indicating that the sprite should
	 * bounce off the edge of the SpritePanel when it hits it. The sprite will
	 * not move outside the panel. This is the default behavior for a sprite.
	 */
	public static final int EXIT_POLICY_BOUNCE = 0;
	/**
	 * A value for the exitPolicy property, indicating that the sprite should
	 * die when it moves outside the SpritePanel. This is the default policy for
	 * the player fish.
	 */
	public static final int EXIT_POLICY_DIE = 1;
	/**
	 * Determines what happens to the sprite when it hits the top or bottom of
	 * the screen
	 */
	private int exitPolicy = EXIT_POLICY_BOUNCE;

	// constructor
	public OnScreenObject(World ocean) {
		this.ocean = ocean;
		this.immuneToOthers = false;
		this.minX = ocean.getXmin();
		this.minY = ocean.getYmin();
		this.maxX = ocean.getXmax();
		this.maxY = ocean.getXmax();
		animation = new Animation();

	}

	protected void setActive(boolean active) {
		this.isActive = active;
	}

	protected void setBounds() {
		// must be called after setting height (end of constructor)
		maxY = this.getParent().getYmax() - this.height;
		minY = this.getParent().getYmin();
		maxX = this.getParent().getXmax() - this.width;
		minX = this.getParent().getXmin();
	}

	public World getParent() {
		return ocean;
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

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
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

	public void worldBoundsCollision() {

	}

	public void discard() {
		this.discarded = true;
		onDeath();
	}

	private void onDeath() {
		// TODO Auto-generated method stub

	}

	public void update() {
		if (isActive) {
			// update objects position
			x += dx;
			y += dy;

			double left = x;
			double top = y;
			double right = x + width;
			double bottom = y + height;

			if (dx == 0 && dy == 0) {
				return;
			} else {
				switch (exitPolicy) {
				case EXIT_POLICY_BOUNCE:
					if (bottom > ocean.getHeight()) {
						dy = -Math.abs(dy);
						y = y - (bottom - ocean.getHeight());
					}
					if (top < 0) {
						dy = Math.abs(dy);
						y = y - top;
					}
					if (right <= 0) {
						discard();
					}
					break;
				case EXIT_POLICY_DIE:
					if (left <= ocean.getXmin() || bottom >= ocean.getYmax() || top <= ocean.getYmin())
						discard();
					break;
				}
			}
			// update it's animation to the next image
			this.animation.update();
		}
	}

	public void draw(Graphics g) {
		if (isActive) {
			g.drawImage(animation.getImage(), (int) (x), (int) (y), null);
		}
		Rectangle r = getRectangle();
		g.drawRect(r.x, r.y, r.width, r.height);
	}

}
