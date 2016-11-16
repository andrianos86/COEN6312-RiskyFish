package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import coen.project.riskyfish.gfx.ImageLoader;
import coen.project.riskyfish.gfx.SpriteSheet;

/*
 * Description:
 * Obstacle objects do not move (they only move at the speed of the ocean world).
 * Obstacle objects do not allow the player to pass through them.
 * Obstacle objects have a dedicated y coordinate where they are located on the ocean world.
 * Instances of the obstacle class can have different heights..
 */
/**
 * @author Andrianos
 *
 */
public class Obstacle extends OnScreenObject {

	private static final String SEAWEED_SPRITE_SHEET = "/textures/seaweed.png";
	private static final String NETS_SPRITE_SHEET = "/textures/nets.png";

	private int numframes = 1;
	private int framewidth = 150;
	private int frameheight;
	private int sritedelay = 1;

	// Types of obstacles
	public static final int NETS = 0;
	public static final int SEEWEED = 1;
	public static final int TOKEN = 2;

	// identifier
	protected int obstacleType;
	protected double speed;

	protected BufferedImage[] sprites;
	protected String spritePath;

	// points earned for avoiding obstacle
	protected int pointsToAward;

	// Determine if obstacle will be displayed and interact with player
	public boolean active = true;
	boolean visible = false;

	// type of event when colliding with object
	protected boolean isDeadly;
	protected boolean isPoisonous;
	protected boolean isCollectable;
	

	/**
	 * @param ocean
	 * @param type
	 */
	public Obstacle(World ocean, int type) {
		super(ocean);

		this.obstacleType = type;
		this.setSpeed(ocean.getScrollingSpeed());

		init(obstacleType);

	}

	private void init(int obstacleType) {
		if (obstacleType == Obstacle.SEEWEED || obstacleType == Obstacle.NETS) {
			if (obstacleType == Obstacle.SEEWEED) {
				this.isDeadly = false;
				this.isPoisonous = true;
				this.pointsToAward = 10;
				this.isCollectable = false;
				spritePath = SEAWEED_SPRITE_SHEET;
				frameheight = 150;
			} else if (obstacleType == Obstacle.NETS) {
				this.isDeadly = true;
				this.isPoisonous = false;
				this.isCollectable = false;

				spritePath = NETS_SPRITE_SHEET;
				this.pointsToAward = 20;
				frameheight = 215;
			}
			try {
				SpriteSheet spritesSheet = new SpriteSheet(ImageLoader.loadImage(spritePath));

				sprites = new BufferedImage[numframes];
				for (int i = 0; i < sprites.length; i++) {
					sprites[i] = spritesSheet.crop(i * numframes, 0, framewidth, frameheight);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			animation.setFrames(sprites);
			animation.setDelay(sritedelay);

			setAnimation();
			setBounds();
			// Collision box dimensions
			cwidth = width;
			cheight = height;

			// Spawn first seaweed at random location outside right edge of the
			// screen

			this.spawnRandomly();

		} else {
			// If token set basic obstacle properties
			this.isDeadly = false;
			this.isPoisonous = false;
			this.isCollectable = true;

		}
	}

	/**
	 * 
	 */
	public void spawnRandomly() {
		if (this.obstacleType == SEEWEED) {
			y = maxY;
		}
		if (this.obstacleType == NETS) {
			y = minY;
		}
		Random generator = new Random();

		// Start at any location starting outside right edge up to 10 times the
		// screen size
		x = generator.nextInt(2 * maxX) + maxX;

		int flipCoin = generator.nextInt(3);

		if (flipCoin == 0) {
			setActive(false);
			setVisible(false);
		} else {
			setActive(true);
			setVisible(false);
		}

	}

	/**
	 * @return
	 */
	public boolean isPastWindowEdge() {
		int rightEdgeX = (int) (x + width);
		return rightEdgeX < 0;
	}

	/**
	 * @param d
	 */
	public void setSpeed(double d) {
		speed = d;
	}

	/**
	 * @return
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @return
	 */
	public int getPoints() {
		return this.pointsToAward;
	}

	/**
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * 
	 */
	protected void setAnimation() {
		currentAction = 0; // only one action implemented 'idle'
		animation.setFrames(sprites);
		animation.setDelay(sritedelay);
		width = framewidth;
		height = frameheight;
	}

	/**
	 * 
	 */
	public void update() {
		// Move to the left
		setSpeed(ocean.getScrollingSpeed());

		if (this.isActive()) {
			if (this.notOnScreen()) {
				this.setVisible(false);
			} else {
				this.setVisible(true);
			}
		} else {
			// if it is not active it is not visible either
			this.setVisible(false);
		}
		x += speed;
		// System.out.println("Y: "+y+", X: "+x+", Active: "+isActive()+",
		// Visible: "+isVisible());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see coen.project.riskyfish.OnScreenObject#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {

		if (this.isVisible()) {
			super.draw(g);

		}
	}

}
