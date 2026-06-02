package view.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class MenuScreenRenderer {

    public void render(Graphics2D g2, int menuSelection, int screenWidth, int screenHeight) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.drawString("PACMAN", screenWidth / 2 - 80, screenHeight / 2 - 80);

        g2.setFont(new Font("Arial", Font.PLAIN, 24));

        String[] options = {"Start Game", "Exit"};
        for (int i = 0; i < options.length; i++) {
            if (i == menuSelection) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> " + options[i], screenWidth / 2 - 70, screenHeight / 2 + i * 40);
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString(options[i], screenWidth / 2 - 50, screenHeight / 2 + i * 40);
            }
        }
    }
}
