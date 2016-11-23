package coen.project.riskyfish.gfx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import coen.project.riskyfish.GamePanel;

public class Background {

	public static final String BG_RES = "/textures/bg_underwater.jpg";

	private BufferedImage bgImage;

	private double x;
	private double y;
	private double dx;
	private double dy;
	private double moveScale;

	public Background(String s, double ms) {
		SpriteSheet bgSheet = new SpriteSheet(ImageLoader.loadImage(BG_RES));
		bgImage = bgSheet.crop(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		this.moveScale = ms;
	}
	public Background(String s, double ms,int width,int height) {
		SpriteSheet bgSheet = new SpriteSheet(ImageLoader.loadImage(BG_RES));
		bgImage = bgSheet.crop(0, 0, width, height);
		this.moveScale = ms;
	}

	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void update() {
		x += dx;
		while (x <= -GamePanel.WIDTH)
			x += GamePanel.WIDTH;
		while (x >= GamePanel.WIDTH)
			x -= GamePanel.WIDTH;
		y += dy;
		while (y <= -GamePanel.HEIGHT)
			y += GamePanel.HEIGHT;
		while (y >= GamePanel.HEIGHT)
			y -= GamePanel.HEIGHT;
	}

	public void draw(Graphics g) {
		g.drawImage(bgImage, (int) x, (int) y, null);

		if (x < 0) {
			g.drawImage(bgImage, (int) x + GamePanel.WIDTH, (int) y, null);
		}
		/*
		 * if (x > 0) { g.drawImage(bgImage, (int) x - GamePanel.WIDTH, (int) y,
		 * null); } if (y < 0) { g.drawImage(bgImage, (int) x, (int) y +
		 * GamePanel.HEIGHT, null); } if (y > 0) { g.drawImage(bgImage, (int) x,
		 * (int) y - GamePanel.HEIGHT, null); }
		 */
	}
}
