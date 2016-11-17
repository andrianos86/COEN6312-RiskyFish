package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Token extends OnScreenObject {

	/**
	 * identifies the type of token to one of: SHELL,FOOD, FEMALE
	 */
	private objType tokenType;
	/**
	 * Specifies the delay between animation frames. Default value for token is
	 * 10 corresponding to 10 animation update calls before changing frames.
	 */
	private int animationDelay = 10;

	/**
	 * The reward awarded if player collects the token.
	 */
	private RewardType reward;

	/**
	 * Identifies if token has been collected by the player.
	 */
	private boolean collected = false;

	/**
	 * An array of images containing the sprite images of the token
	 */
	private BufferedImage[] spriteImages;

	public Token(World oceanPanel, objType type) {
		// initialize token off screen.
		super(oceanPanel);

		this.setVelocityVector(0, oceanPanel.getSpeed());

		this.tokenType = type;
		this.reward = new RewardType(this.tokenType);

		this.setSpriteImages(type);

		this.setAnimation(spriteImages, animationDelay);

		this.setMovingBounds();
		this.setOffScreenPolicy(EXIT_POLICY_BOUNCE);

		this.setImmuneToOthers(true);

		// place token at random depth outside viewable part of the world
		this.spawn(this.getParent().getYmin(), this.getParent().getYmax(), this.getParent().getXmin(),
				this.getParent().getXmax());
		// token ready to be drawn
		this.setActive(true);

	}

	/**
	 * Loads the token sprite images to be used for the animation, and sets the
	 * token's dimensions as per it's image.
	 */
	private void setSpriteImages(objType tokenType) {

		switch (tokenType) {
		case FOOD_TKN:
			this.spriteImages = SpriteContent.token[0];
			break;
		case FEMALE_TKN:
			this.spriteImages = SpriteContent.token[1];
			break;
		case SHELL_TKN:
			this.spriteImages = SpriteContent.token[2];
			break;
		default:
			System.out.println("Wrong object type enum for token");
			break;
		}

		this.setHeight(55);
		this.setWidth(55);
		this.setCHeight(55);
		this.setCWidth(55);

	}

	public RewardType collectToken() {
		if (this.collected) {
			// if already collected don't give extra reward
			return new RewardType(0, 0, false);
		} else {
			// stop drawing token
			this.setActive(false);
			this.collected = true;
			// remove token from world
			this.discard();

			return reward;
		}
	}

	public boolean isCollected() {
		return collected;
	}

	public void update() {
		this.setVelocityVector(this.getParent().getSpeed(), 0);
		super.update();
	}

	public void draw(Graphics g, boolean debug) {

		if (this.isVisible() && this.isActive()) {
			super.draw(g, debug);
		}
	}

}
