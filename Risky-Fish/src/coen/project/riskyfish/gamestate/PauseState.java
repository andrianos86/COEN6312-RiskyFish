package coen.project.riskyfish.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import coen.project.riskyfish.GamePanel;

public class PauseState extends GameState {

	private Font font;

	public PauseState(GameStateManager gsm) {
		super(gsm);

		font = new Font("Century Gothic", Font.PLAIN, 14);
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("G A M E  P A U S E D!", 90, 90);
		g.drawString("Press 'ESC' to Quit Game and return to Main Menu \r\n"
		    + "Press 'R' to resume the current Game ", 90, 115);
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ESCAPE) {
          gsm.setPaused(false);
          gsm.setState(GameStateManager.MENUSTATE);
		}
		if (k == KeyEvent.VK_R) {
	       gsm.setPaused(false);
		}

		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}
}
