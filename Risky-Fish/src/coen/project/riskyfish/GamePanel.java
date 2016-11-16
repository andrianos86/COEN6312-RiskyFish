package coen.project.riskyfish;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

import coen.project.riskyfish.gamestate.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	private Timer timer;

	// game state manager
	private GameStateManager gsm;

	public GamePanel() {
		super();
		this.setFocusable(true);
		this.requestFocusInWindow();

		// listeners
		this.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				gsm.keyReleased(e.getKeyCode());
			}

			public void keyTyped(KeyEvent e) {
				// use this for entering names
				// gsm.keyTyped(e.getKeyCode());
			}

			public void keyPressed(KeyEvent e) {
				gsm.keyPressed(e.getKeyCode());

			}
		});

		// timer
		timer = new Timer(17, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				timedAction();
			}
		});

		gsm = new GameStateManager();
		timer.start();

	}

	public Dimension getPreferredSize() {
		Dimension size = new Dimension(WIDTH, HEIGHT);

		return size;
	}

	private void timedAction() {

		gsm.update();
		this.repaint();
	}

	public void paintComponent(Graphics g) {
		gsm.draw(g);
	}

}
