package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class EnemyFish extends OnScreenObject {

	// identifier
	private objType enemyType;

	// sprite path
	protected String spritePath;

	// frames and properties of each player fish animation
	private BufferedImage[] spriteImages;

	// Damage it causes
	protected int damageCaused;

	protected double speed;

	public EnemyFish(World oceanPanel, objType type) {
		super(oceanPanel);
		this.setOffScreenPolicy(EXIT_POLICY_BOUNCE);
		this.enemyType = type;
		// this.setVelocityVector(oceanPanel.getSpeed(), oceanPanel.getSpeed());
		this.setSpriteImages(type);

		int animationDelay = 5;
		this.setAnimation(spriteImages, animationDelay);

		this.setMovingBounds();

		this.setImmuneToOthers(true);
	}

	public void spawn(double minimumY, double maximumY, double minimumX, double maximumX) {
		switch (enemyType) {
		case PREDATOR:
			dy = 2.0;
			int offset = 150;
			this.minY = this.getParent().getYmin() + 150;
			this.maxY = this.getParent().getYmax() - this.height - offset;
			super.spawn(minY, maxY, 8.0 * maximumX, 9.0 * maximumX);
			this.setPointsToAward(100);
			break;
		default:
			super.spawn(minimumY, maximumY, 8.0 * maximumX, 9.0 * maximumX);
			this.setPointsToAward(50);
		}
	}

	/**
	 * Loads the token sprite images to be used for the animation, and sets the
	 * token's dimensions as per it's image.
	 */
	private void setSpriteImages(objType type) {

		switch (type) {
		case PREDATOR:
			this.spriteImages = SpriteContent.predator[0];
			this.setHeight(79);
			this.setWidth(200);
			this.setCHeight(79);
			this.setCWidth(200);
			break;
		case JELLYFISH:
			this.spriteImages = SpriteContent.jellyfish[0];
			this.setHeight(125);
			this.setWidth(89);
			this.setCHeight(125);
			this.setCWidth(89);

			break;
		default:
			System.out.println("Wrong object type enum for enemyFish");
			break;
		}
	}

	public void update() {

		if (this.isActive()) {
			if (enemyType == objType.PREDATOR) {
				this.setVelocityVector(this.getParent().getSpeed() - 1.0, this.dy);
			} else if (enemyType == objType.JELLYFISH) {
				this.setVelocityVector(this.getParent().getSpeed(), 0.0);
			}
			super.update();
		}
	}

	public void draw(Graphics g, boolean mbr) {
		if (this.isVisible() && this.isActive()) {
			super.draw(g, mbr);
		}
	}

}
