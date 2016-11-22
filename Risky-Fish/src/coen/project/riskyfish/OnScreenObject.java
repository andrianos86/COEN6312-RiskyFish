package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * An on-screen object represents a small graphical element that can draw itself
 * in a ocean world panel. This class is not ordinarily used directly for
 * creating objects. Use subclasses instead. It does proved a large number of
 * methods and properties for working with on-screen objects. Note that this
 * class works closely with the World class to implement on-screen object
 * behavior. An on-screen object must be added to a world panel for it to do
 * anything useful.
 */
public abstract class OnScreenObject {

	/**
	 * A value for the offScreenPolicy property, indicating that the sprite
	 * should bounce off the edge of the SpritePanel when it hits it. The sprite
	 * will not move outside the panel. This is the default behavior for a
	 * sprite.
	 */
	public static final int EXIT_POLICY_BOUNCE = 0;
	/**
	 * A value for the offScreenPolicy property, indicating that the sprite
	 * should die when it moves outside the SpritePanel. This is the default
	 * policy for the player fish.
	 */
	public static final int EXIT_POLICY_DIE = 1;
	/**
	 * Determines what happens to the sprite when it hits the top or bottom of
	 * the screen
	 */

	/**
	 * The ocean world panel that contains the on-screen object, set
	 * automatically.
	 */
	protected World ocean;

	/**
	 * Identifier, specifying the type of on screen instance
	 */
	protected objType obstacleType;

	/**
	 * Represent the boundary coordinates of the viewable part of the world that
	 * an on-screen object can move in.
	 */
	protected int maxY, minY, maxX, minX;

	/**
	 * The position of the on-screen object, in pixels specifying it's top left
	 * coordinate.
	 */
	protected double x, y;
	/**
	 * The velocity of the on-screen object, in pixels per frame.
	 */
	protected double dx, dy;

	/**
	 * The image-sprite size of the on-screen object.
	 */
	protected int width, height;

	/**
	 * The actual size of the on-screen object, not guaranteed to match it's
	 * image size.
	 */
	protected int cwidth, cheight;

	/**
	 * Contains the animations of the on-screen object for all it's different
	 * actions.
	 */
	protected Animation animation;
	/**
	 * Determines the current animation action of the on-screen object.
	 */
	protected int currentAnimation = 0;

	/**
	 * A sprite is updated and drawn only when active. The isActive boolean
	 * allows a sprite to be (temporarily) removed from the game since the
	 * sprite won't be drawn or collide with the player when isActive is false.
	 * There are public isActive( ) and setActive( ) methods for manipulating
	 * the boolean.
	 */
	protected boolean isActive = false;

	/**
	 * When set to true, the on-screen object should be removed from its ocean
	 * world.
	 */
	private boolean discarded = false;

	/**
	 * When set to true, the on-screen object does not collide or die when
	 * interacting with other objects other than the player.
	 */
	private boolean immuneToOthers;

	/**
	 * Determines what happens to the on-screen object when it hits a boundary
	 * edge of the world.By default objects are discarded when exiting the
	 * screen.
	 */
	private int offScreenPolicy = EXIT_POLICY_BOUNCE;
	
	/**
	 * Determines the points to award for avoiding the on screen object.
	 */
	protected int pointsToAward=0;

	// constructor
	public OnScreenObject(World ocean) {
		this.ocean = ocean;
		dx = 0;
		dy = 0;
		this.immuneToOthers = false;
		// this.minX = ocean.getXmin();
		// this.minY = ocean.getYmin();
		// this.maxX = ocean.getXmax();
		// this.maxY = ocean.getXmax();
		animation = new Animation();

	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}
	
	public int getPointsToAward() {
		return pointsToAward;
	}

	public void setPointsToAward(int pointsToAward) {
		this.pointsToAward = pointsToAward;
	}

	/**
	 * Sets the world bounding coordinates of the on-screen object.
	 */
	public void setMovingBounds() {
		maxY = this.getParent().getYmax();
		minY = this.getParent().getYmin();
		maxX = this.getParent().getXmax();
		minX = this.getParent().getXmin();
	}

	public int getMaxY() {
		return this.maxY;
	}

	public int getMinY() {
		return this.minY;
	}

	public int getMaxX() {
		return this.maxX;
	}

	public int getMinX() {
		return this.minX;
	}

	/**
	 * Sets the location of the on-screen object at a random depth between
	 * surface and ocean floor just outside the right edge of the viewable ocean
	 * world.
	 * 
	 * @param minimumY
	 *            The minimum possible depth. Must be below surface.
	 * @param maximumY
	 *            The maximum possible depth. Must be above ocean floor.
	 */
	public void spawn(double minimumY, double maximumY, double minimumX, double maximumX) {

		double startingY = minimumY + (maximumY - minimumY) * Math.random();
		double startingX = minimumX + (maximumX - minimumX) * Math.random();

		y = startingY;
		x = startingX;

	}

	/**
	 * Gets the ocean panel that contains this on-screen object. That panel is
	 * called the "world" of the sprite. The value of world is set automatically
	 * when the object is added to a world.
	 * 
	 * @return the world, or null if this object is not in a world
	 */
	public World getParent() {
		return ocean;
	}

	void setWorld(World oceanPanel) {
		ocean = oceanPanel;
	}

	/**
	 * Loads the animation for a single action of the on-screen object. Should
	 * be called once before starting to draw the sprite.
	 * 
	 * @param sprites
	 *            The image frames composing the animation/
	 * @param delay
	 *            The delay between different images.
	 */
	protected void setAnimation(BufferedImage[] sprites, int delay) {
		animation.setFrames(sprites);
		animation.setDelay(delay);
	}

	/**
	 * Checks for collision with an other on-screen object.
	 * 
	 * @param other
	 *            The object with which to check if collision occurred.
	 * @return Returns true if on-screen object intersects an other on-screen
	 *         object.
	 */
	public boolean intersects(OnScreenObject other) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = other.getRectangle();
		return r1.intersects(r2);
	}

	/**
	 * Checks for collision with a bounding rectangle.
	 * 
	 * @param other
	 *            The rectangle with which to check if collision occurred.
	 * @return Returns true if on-screen object intersects the given rectangle.
	 */
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

	/**
	 * Returns the minimum boundary rectangle of the on-screen object.
	 */
	public Rectangle getRectangle() {
		return new Rectangle((int) x, (int) y, cwidth, cheight);
	}

	public double getX() {
		return x;
	}

	/**
	 * Sets the x-coordinate of the top left corner of the on-screen object.
	 * 
	 * @param newX
	 *            The new x-coordinate of the object.
	 */
	public void setX(double newX) {
		x = newX;
	}

	public double getY() {
		return y;
	}

	/**
	 * Sets the y-coordinate of the center of the on-screen object.
	 * 
	 * @param newY
	 *            The new y-coordinate of the object.
	 */
	public void setY(double newY) {
		y = newY;
	}

	/**
	 * Sets the location of the center of the on-screen object. Note that there
	 * is nothing to stop you from setting the location to be outside the panel.
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width < 0)
			throw new IllegalArgumentException("Width of a on-screen object can't be negative.");
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height < 0)
			throw new IllegalArgumentException("Height of a sprite can't be negative.");
		this.height = height;
	}

	public int getCWidth() {
		return cwidth;
	}

	public void setCWidth(int cWidth) {
		this.cwidth = cWidth;
	}

	public int getCHeight() {
		return cheight;
	}

	public void setCHeight(int cHeight) {
		this.cheight = cHeight;
	}

	public int getOffScreenPolicy() {
		return offScreenPolicy;
	}

	public void setOffScreenPolicy(int policyCode) {
		if (policyCode < 0 || policyCode > 1)
			throw new IllegalArgumentException("Illegal exit policy code");
		offScreenPolicy = policyCode;
	}

	/**
	 * Says whether this object should be immune to collisions. An object that
	 * is not immune to collisions interacts with other objects. For example
	 * player dies immediately when hitting nets or collects points when hitting
	 * a shell token.
	 */
	public void setImmuneToOthers(boolean immune) {
		immuneToOthers = immune;
	}

	public boolean isImmuneToOthers() {
		return immuneToOthers;
	}

	/**
	 * Sets the velocity of the on-screen object. The default velocity is zero.
	 * If the on-screen object is going to move by itself, a non-zero velocity
	 * must be assigned to it.
	 * 
	 * @param newDX
	 *            the horizontal component of the velocity
	 * @param newDY
	 *            the vertical component of the velocity
	 * 
	 */
	public void setVelocityVector(double newDX, double newDY) {
		this.dx = newDX;
		this.dy = newDY;
	}

	public double getDx() {
		return this.dx;
	}

	public double getDy() {
		return this.dy;
	}

	public void worldBoundsCollision() {

	}

	/**
	 * Calling this method kills the on-screen object. It is called automaticaly
	 * in some cases such as when a moving object moves outside the screen
	 * (ocean world). When an on-screen object gets discarded it is no longer
	 * drawn on the screen. A discarded on-screen object can not be brought
	 * back.
	 */
	public void discard() {
		discarded = true;
		onDiscard();
	}

	/**
	 * Tells whether the on-screen object is discarded.
	 * 
	 * @return A boolean indicating if the on-screen object is discarded.
	 */
	public boolean isDiscarded() {
		return discarded;
	}

	/**
	 * This method is called by the GamePlayState when the on-screen object gets
	 * discarded for any reason. It is not meant to be called directly. The
	 * method in this class does nothing. Subclasses that need to perform some
	 * action when the sprite dies can redefine this method.
	 */
	private void onDiscard() {
		// TODO Auto-generated method stub
		this.setActive(false);
	}

	/**
	 * Returns true if object is within viewable area of the ocean world.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return !((x + width) < ocean.getXmin() || x > ocean.getXmax() || (y + height) < ocean.getYmin()
				|| y > ocean.getYmax());
	}

	public void update() {
		if (isActive()) {

			if (dx == 0 && dy == 0)
				return;

			// update objects position
			x += dx;
			y += dy;
						
			double left = x;
			double top = y;
			double right = x + width;
			double bottom = y + height;
			
			
			switch (this.getOffScreenPolicy()) {
			case EXIT_POLICY_BOUNCE:
				if (bottom >= this.maxY) {
					dy = -dy;
					y = y - (bottom - this.maxY);
				}
				if (top < this.minY) {
					dy = -dy;
					y = y - (top - this.minY);
				}
				if (right <= this.minX) {
					discard();
				}
				break;
			case EXIT_POLICY_DIE:
				if (left <= ocean.getXmin() ){
					discard();
			}
				if (bottom >= ocean.getYmax()){
					y = y - (bottom - ocean.getYmax());
					discard();
				}
				if (top < this.ocean.getYmin()){
					y = y - (top - ocean.getYmin());
					discard();
				}
				break;
			default:
				if (right < ocean.getXmin()) {
					this.discard();
				}
			}

			// update it's animation to the next image
			this.animation.update();
		}
	}

	public void draw(Graphics g, boolean showMBR) {
		if (isActive() & isVisible()) {
			g.drawImage(animation.getImage(), (int) (x), (int) (y), null);
		}
		// For debugging collisions
		if (showMBR) {
			Rectangle r = getRectangle();
			g.drawRect(r.x, r.y, r.width, r.height);
		}
	}

}
