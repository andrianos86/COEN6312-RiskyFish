package coen.project.riskyfish;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import coen.project.riskyfish.gfx.ImageLoader;
import coen.project.riskyfish.gfx.SpriteSheet;

public class PlayerFish extends OnScreenObject {

	// Spreadsheet resource location
	private static final String FISH_SPRITE_SHEET = "/textures/fish_sprite1.png";

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

	// frames and properties of each player fish animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = { 7, 7, 0 };
	private final int[] FRAMEWIDTHS = { 57, 57, 57 };
	private final int[] FRAMEHEIGHTS = { 57, 57, 57 };
	private final int[] SPRITEDELAYS = { 5, 5, 3 };

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
		this.type = fish_type;
		// Collision box dimensions
		cwidth = 57;
		cheight = 57;

		lives = 3;

		setGravityFactor(type);


		try {
			SpriteSheet spritesSheet = new SpriteSheet(ImageLoader.loadImage(FISH_SPRITE_SHEET));

			int count = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < NUMFRAMES.length; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
				for (int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesSheet.crop(j * FRAMEWIDTHS[i], count, FRAMEWIDTHS[i], FRAMEHEIGHTS[i]);
				}
				sprites.add(bi);
				count += FRAMEHEIGHTS[i];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		setAnimation(IDLE);
		setBounds();
	}

	public PlayerFish(World ocean) {
		this(ocean, PlayerFish.TYPE_II);
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

	private void setAnimation(int i) {
		currentAnimation = i;
		animation.setFrames(sprites.get(currentAnimation));
		animation.setDelay(SPRITEDELAYS[currentAnimation]);
		width = FRAMEWIDTHS[currentAnimation];
		height = FRAMEHEIGHTS[currentAnimation];
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
		// checkTopBottomCollision();
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
				setAnimation(EATING);
			}
		} else if (isDead) {
			if (currentAnimation != DEAD) {
				setAnimation(DEAD);
			}
		} else if (currentAnimation != IDLE) {
			setAnimation(IDLE);
		}
		animation.update();

	}

	private void getNextPosition() {
		float gravity = this.gravityFactor * GRAVITY;

		if (y + (int) dy > maxY) {
			dy = 0;
			y = maxY;
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

	public void draw(Graphics g) {
		// draw player
		if (isFlinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;

			if (elapsed / 100 % 2 == 0) {
				return; // blink every 100ms
			}
		}
		super.draw(g);
	}

}
