package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Description: Obstacle objects do not move (they only move at the speed of the
 * ocean world). Obstacle objects have a dedicated y coordinate where they are
 * located on the ocean world (y does not change once object is created).
 *
 */
public class Obstacle extends OnScreenObject {

	// identifier
	protected objType obstacleType;

	/**
	 * An array of images containing the sprite images of the token
	 */
	private BufferedImage[] spriteImages;

	/**
	 * Points earned for avoiding obstacle
	 */
	protected int pointsToAward;

	/**
	 * Type of event trigger when colliding with object
	 */
	protected boolean isDeadly;
	protected boolean isPoisonous;

	/**
	 * Specifies the delay between animation frames. Default value for token is
	 * 10 corresponding to 10 animation update calls before changing frames.
	 */
	private int animationDelay;

	/**
	 * @param oceanPanel
	 * @param obsticleType
	 */
	public Obstacle(World oceanPanel, objType type) {
		super(oceanPanel);
		this.setActive(false);
		this.obstacleType = type;
		this.setSpriteImages(type);
		this.setAnimation(spriteImages, animationDelay);
		this.setMovingBounds();
		this.setImmuneToOthers(true);

		// Obstacles only move towards the playerFish
		this.setOffScreenPolicy(EXIT_POLICY_BOUNCE);
		this.setVelocityVector(oceanPanel.getSpeed(), 0);
		//this.spawn(this.getParent().getYmin(), this.getParent().getYmax() - this.getHeight(),
		//		this.getParent().getXmax(), 20 * this.getParent().getXmax());

	}
	
	public void spawn(double mimimumY, double maximumY, double minimumX, double maximumX){
		switch(obstacleType){
		case SEAWEED:
			super.spawn(ocean.getYmax()-height, ocean.getYmax()-height,minimumX, 3*maximumX);
			this.setPointsToAward(10);

			//seaweed appear at the bottom of the ocean
			break;
		case NETS:
			super.spawn(ocean.getYmin(),ocean.getYmin(),minimumX, 6*maximumX);
			this.setPointsToAward(20);

			//nets appear on the surface of the ocean
		}
	}

	/**
	 * Loads the token sprite images to be used for the animation, and sets the
	 * token's dimensions as per it's image.
	 */
	private void setSpriteImages(objType obstacleType) {

		switch (obstacleType) {
		case SEAWEED:
			this.spriteImages = SpriteContent.seaweed[0];
			this.setHeight(150);
			this.setWidth(150);
			this.setCHeight(150);
			this.setCWidth(150);
			animationDelay = 10;
			break;
		case NETS:
			this.spriteImages = SpriteContent.nets[0];
			this.setHeight(215);
			this.setWidth(150);
			this.setCHeight(215);
			this.setCWidth(150);
			animationDelay = 10;
			break;

		default:
			System.out.println("Wrong object type enum for obstacle");
			this.setHeight(50);
			this.setWidth(50);
			this.setCHeight(50);
			this.setCWidth(50);
			animationDelay = -1;
			break;
		}

	}

	public int getPoints() {
		return this.pointsToAward;
	}

	/**
	 * Updates the obstacles velocity according to the worlds current scrolling
	 * speed. Also updates it's animation.
	 */
	public void update() {
		if (isActive()) {
			this.setVelocityVector(this.getParent().getSpeed(), 0);
			super.update();
		}
	}

	/**
	 * Draws object on screen
	 */
	public void draw(Graphics g, Boolean debug) {
		if (isActive() & isVisible()) {
			super.draw(g, debug);
		}
	}

}
