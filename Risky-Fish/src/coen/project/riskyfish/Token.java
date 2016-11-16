package coen.project.riskyfish;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import coen.project.riskyfish.gfx.ImageLoader;
import coen.project.riskyfish.gfx.SpriteSheet;

public class Token extends Obstacle {

	private static final String TOKEN_SHEET = "/textures/token_sprites.png";

	// Type of animated token
	public static final int FOOD = 0;
	public static final int FEMALE = 1;
	public static final int SHELL = 2;
	// Identifier
	public int tokenType;

	// frames and properties of each player fish animation
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = { 2, 2, 2 };
	private final int[] FRAMEWIDTHS = { 55, 55, 55 };
	private final int[] FRAMEHEIGHTS = { 55, 55, 55 };
	private final int[] SPRITEDELAYS = { 5, 5, 5 };

	// Font for token award
	Font t_font = new Font("TimesRoman", Font.PLAIN, 18);

	// Random token generator
	Random generator = new Random();

	private int slowdownDuration;
	private int livesRewarded;

	public Token(World ocean) {
		super(ocean, Obstacle.TOKEN);

		tokenType = getNextToken();
		init(tokenType);

		spawnRandomly();
	}

	public int getNextToken() {

		// Start at any location starting outside right edge up to 10 times the
		// screen size
		tokenType = generator.nextInt(3);
		randomizeAward();

		return tokenType;
	}

	public void spawnRandomly() {
		setAnimation(tokenType);

		// generate random position
		// tokens are rarely generated once player travels 5x the screen size
		this.x = generator.nextInt(5 * this.maxX) + maxX;
		this.y = generator.nextInt(this.maxY);

		int flipCoin = generator.nextInt(5);

		if (flipCoin == 0) {
			setActive(false);
			setVisible(false);
		} else {
			setActive(true);
			setVisible(false);
		}

	}

	private void randomizeAward() {
		if (tokenType == Token.SHELL) {
			// shell tokens add from 1 to 100 points
			this.pointsToAward = generator.nextInt(100) + 1;
			this.slowdownDuration = 0;

		}
		if (tokenType == Token.FEMALE) {
			// Slow down for 5 to 20 seconds
			this.slowdownDuration = generator.nextInt(15) + 5;
			this.pointsToAward = 0;
		}
		if (tokenType == Token.FOOD) {
			// Award 1 to 5 lives.
			this.livesRewarded = generator.nextInt(4) + 1;
			this.pointsToAward = 0;
			this.slowdownDuration = 0;
		}
	}

	private void init(int tokenType) {
		if (obstacleType == Obstacle.TOKEN) {
			this.isDeadly = false;
			this.isPoisonous = false;
			this.isCollectable = true;
		}
		try {
			SpriteSheet spritesSheet = new SpriteSheet(ImageLoader.loadImage(TOKEN_SHEET));

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

		setAnimation(this.tokenType);
		setBounds();
		// Collision box dimensions
		cwidth = width;
		cheight = height;

		this.isDeadly = false;
		this.isPoisonous = false;
		this.isCollectable = true;
	}

	private void setAnimation(int tokenType) {
		animation.setFrames(sprites.get(tokenType));
		animation.setDelay(SPRITEDELAYS[tokenType]);
		width = FRAMEWIDTHS[tokenType];
		height = FRAMEHEIGHTS[tokenType];
	}

	public void collect() {
		this.setActive(false);
		this.setVisible(false);
		this.getNextToken();
		this.spawnRandomly();
	}

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

		animation.update();
	}

	public void draw(Graphics g) {
		super.draw(g);
		if (this.isActive() & this.isVisible()) {
			Font prevFont = g.getFont();
			Color prevColor = g.getColor();
			g.setFont(t_font);
			g.setColor(Color.WHITE);

			if (this.tokenType == Token.FEMALE) {
				g.drawString(this.slowdownDuration + " sec.", (int) this.x + this.width + 1, (int) this.y + this.height/2);
			}
			if (this.tokenType == Token.SHELL) {
				g.drawString(this.pointsToAward + " points", (int) this.x + this.width + 1, (int) this.y + this.height/2);
			}
			if (this.tokenType == Token.FOOD) {
				g.drawString(this.livesRewarded + " lives", (int) this.x + this.width + 1, (int) this.y + this.height/2);
			}
			g.setFont(prevFont);
			g.setColor(prevColor);
		}
	}

}
