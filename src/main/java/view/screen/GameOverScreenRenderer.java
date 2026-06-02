package view.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GameOverScreenRenderer {

    public void render(Graphics2D g2, int lastScore, int highScore, int screenWidth, int screenHeight) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.drawString("GAME OVER", screenWidth / 2 - 120, screenHeight / 2 - 40);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + lastScore, screenWidth / 2 - 70, screenHeight / 2 + 10);
        g2.drawString("High Score: " + highScore, screenWidth / 2 - 85, screenHeight / 2 + 40);
        g2.drawString("Press ENTER to return to menu", screenWidth / 2 - 160, screenHeight / 2 + 80);
    }
}
