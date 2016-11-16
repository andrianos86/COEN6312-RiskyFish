package coen.project.riskyfish;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class RiskyFishGame extends JFrame {


	private GamePanel gamePanel; 

	public static void main(String[] args) {

		try {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(className);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Put a new Runnable object on the event dispatch thread.

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new RiskyFishGame();
			}
		});

	}

	public RiskyFishGame() {
		
		initGUI();
		this.setTitle("Risky-Fish");
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initGUI() {
		// main game panel
		gamePanel = new GamePanel();
		this.add(gamePanel);

 

	}

}
