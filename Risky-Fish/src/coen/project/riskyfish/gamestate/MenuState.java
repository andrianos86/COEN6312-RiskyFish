package coen.project.riskyfish.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import coen.project.riskyfish.GamePanel;
import coen.project.riskyfish.gfx.Background;

public class MenuState extends GameState {

	private Background bg;

	private int currentChoice = 0;

	private Color titleColor;
	private Font titleFont;
	private Font smallFont;
	private Font smallerFont;

	private String[] options = { "Start Game", "Create PlayerFish", "Select Existing PlayerFish", "Help", "Quit" };

	public MenuState(GameStateManager gsm) {
		super(gsm);
		try {
			bg = new Background(Background.BG_RES, 1,GamePanel.WIDTH,GamePanel.HEIGHT-50);
			bg.setVector(0, 0);
			bg.setPosition(0, 50);

			titleColor = Color.WHITE;
			titleFont = new Font("Times New Roman", Font.BOLD, 28);

			smallFont = new Font("Arial", Font.PLAIN, 18);
			smallerFont = new Font("Arial", Font.PLAIN, 14);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {
		bg.update();
	}

	@Override
	public void draw(Graphics g) {
		//draw sky
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, GamePanel.WIDTH, 45);
		//draw sea boundary
		g.setColor(Color.WHITE);
		g.fillRect(0, 45, GamePanel.WIDTH, 5);
		g.setColor(Color.BLACK);
		// draw background
		bg.draw(g);

		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("R I S K Y - B I R D", 300, 100);

		// draw menu options
		g.setFont(smallFont);
		g.setColor(Color.WHITE);
		for (int i = 0; i < options.length; i++) {
			if (i == this.currentChoice) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 320, 190 + i * 24);
		}

		// draw credits
		g.setFont(smallerFont);
		g.setColor(Color.BLACK);
		g.drawString("COEN6312 - Fall 2016. Team: Now this.", 10, 30);
	}

	private void select() {
		if (currentChoice == 0) {
			// Start Game
			this.gsm.setState(GameStateManager.GAMEPLAYSTATE);
		}
		if (currentChoice == 2) {
			// Create New PlayerFish
			System.exit(0);
		}
		if (currentChoice == 3) {
			// Select Existing PlayerFish
			System.exit(0);
		}
		if (currentChoice == 4) {
			// Help
			System.exit(0);
		}
		if (currentChoice == 5) {
			// Quit
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}

		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}

		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}

	@Override
	public void keyReleased(int k) {

	}
}
