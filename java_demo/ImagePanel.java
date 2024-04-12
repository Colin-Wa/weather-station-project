package com.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImagePanel extends JPanel {
        private BufferedImage image;

        // Class constructor
        public ImagePanel() {
            try {
                // Set the background image of the ImagePanel
                URL imgUrl = new URL("https://pixnio.com/free-images/2021/07/31/2021-07-31-08-08-30-925x1350.jpg"); //替换为实际图片链接
                // Initialize the image class variable
                image = ImageIO.read(imgUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Description: Paints the background image
        // Precondition: The image class variable is initialized with the background image
        // Postcondition: The background image is drawn on the imagepanel
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        }
}
