package view.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class HudRenderer {

    public void render(Graphics2D g2, int score, int highScore, int lives) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + score, 10, 20);
        g2.drawString("High Score: " + highScore, 200, 20);
        g2.drawString("Lives: " + lives, 400, 20);
    }
}
