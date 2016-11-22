package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import coen.project.riskyfish.gfx.ImageLoader;
import coen.project.riskyfish.gfx.SpriteSheet;

public class PlayerFish extends OnScreenObject {

	// Limit the bounds of the fish vertical speed
	private final int MIN_DY = 0;
	private final int MAX_DY = 10;

	// fish types associated with different gravity factors
	public static final int TYPE_I = 0;
	public static final int TYPE_II = 1;
	public static final int TYPE_III = 2;

	// Gravity constant
	protected final int GRAVITY = 12;
	// Gravity factor depending on fish type
	private float gravityFactor;

	// fish properties
	private int lives;
	private int score;
	private int distanceTravelled = 0;

	// setting default fish type
	private int type = TYPE_II;

	// flicking effect for animation when loosing a life.
	private boolean isFlinching = false;
	private long flinchTimer;

	// poisonedTimer used when poisoned
	private int poisonedTime = (int) 10;
	private long time;

	// how far up fish moves with each user input
	private double swimForce = -8.0;

	// Player events
	// player is swimmingUp
	private boolean isSwimmingUp;
	// collecting food-token
	private boolean isEating;
	// collecting female-fish-token
	private boolean isSlowingDown;
	// Lost final life
	private boolean isDead;
	// Lost one life
	private boolean isBitten;
	// player is poisoned from seaweed
	private boolean isPoisoned;

	// Animation frames each player fish action
	private BufferedImage[] sprites;

	// animated actions
	private static final int IDLE = 0;
	private static final int EATING = 1;
	private static final int DEAD = 2;
	private static final int POISONED = 3;

	public PlayerFish(World ocean, int fish_type) {
		super(ocean);
		isSwimmingUp = false;
		isEating = false;
		isSlowingDown = false;
		isDead = false;
		isBitten = false;
		isPoisoned = false;
		this.setActive(true);

		this.type = fish_type;
		// Collision box dimensions
		// Collision box dimensions
		this.setCWidth(57);
		this.setCHeight(57);
		this.setWidth(57);
		this.setHeight(57);

		this.setLives(3);
		setImmuneToOthers(false);
		setGravityFactor(type);

		sprites = SpriteContent.playerFish[IDLE];

		setAnimation(sprites, 5);
		setMovingBounds();
	}

	public PlayerFish(World ocean) {
		this(ocean, PlayerFish.TYPE_II);
	}
	
	private void setSprites(int currentState){
		switch (currentState) {
		case IDLE:
			this.sprites = SpriteContent.playerFish[IDLE];
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

	private void setGravityFactor(int fish_type) {
		switch (fish_type) {
		case TYPE_I:
			this.gravityFactor = 0.01f;
			break;
		case TYPE_III:
			this.gravityFactor = 0.055f;
			break;
		default:
			this.gravityFactor = 0.03f;
		}
	}

	public void setLives(int i) {
		lives = i;
		if (lives < 0) {
			lives = 0;
			this.isDead = true;
		}
	}

	public void gainLife() {
		lives++;
	}

	public void loseLife() {
		lives--;
		if (lives < 0) {
			lives = 0;
			this.isDead = true;
		}
	}

	public int getLives() {
		return lives;
	}

	public void increaseScore(int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long t) {
		time = t;
	}

	private void updateDistanceTravelled() {
		distanceTravelled += (-this.getParent().getSpeed());
	}

	public void update() {
		time++;
		updateDistanceTravelled();
		// updateScore();

		// update speedX
		if (time % 100 == 0) {
			this.getParent().setSpeed(this.getParent().getSpeed() - 0.1);
		}

		// update position
		getNextPosition();
		this.worldBoundsCollision();
		setPosition(x, y);

		// check if player should flicker
		if (isFlinching) {
			flinchTimer++;
			if (flinchTimer > 120) {
				isFlinching = false;
			}
		}

		// set Animation by priority
		if (isEating) {
			if (currentAnimation != EATING) {
				setAnimation(sprites, 1);
			}
		} else if (isDead) {
			if (currentAnimation != DEAD) {
				setAnimation(sprites, 1);
			}
		} else if (currentAnimation != IDLE) {
			setAnimation(sprites, 1);
		}
		animation.update();

	}

	private void getNextPosition() {
		float gravity = this.gravityFactor * GRAVITY;

		if (y + (int) dy > maxY - height) {
			dy = 0;
			y = maxY - height;
		} else if (y < minY) {
			y = minY;
		} else {
			dy += gravity;
			if (dy > MAX_DY) {
				dy = MAX_DY;
			}
			y += dy;
			if (dy > this.MIN_DY) {
				isSwimmingUp = false;
			}
		}

	}

	public void startSwimming() {
		isSwimmingUp = true;
		dy = swimForce;
	}

	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}

	public void reset() {
		isFlinching = false;
		currentAnimation = -1;
	}

	public void draw(Graphics g, boolean mbr) {
		// draw player
		if (isFlinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;

			if (elapsed / 100 % 2 == 0) {
				return; // blink every 100ms
			}
		}
		super.draw(g, mbr);
	}

}
