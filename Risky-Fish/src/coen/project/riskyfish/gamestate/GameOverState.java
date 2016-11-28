package coen.project.riskyfish.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import coen.project.riskyfish.GamePanel;

public class GameOverState extends GameState {

    private Font font;

    public GameOverState(GameStateManager gsm) {
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
        g.drawString("G A M E  O V E R!", 90, 90);
        g.drawString("Press 'ESC' to return to Main Menu or 'V' to view your ranking. ", 90, 115);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {
          gsm.setState(GameStateManager.MENUSTATE);
        }
        if (k == KeyEvent.VK_V) {
           // gsm.setPaused(false);
          //  gsm.setState(GameStateManager.MENUSTATE);
        }

        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(int k) {
        // TODO Auto-generated method stub

    }
}
