package com.example.bitsbids;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer {

    public static void resizeImage(File originalFile, File resizedFile, int newWidth, int newHeight)
            throws IOException {
        // Read the original image
        BufferedImage originalImage = ImageIO.read(originalFile);

        // Create a new BufferedImage with the desired size
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        // Create a Graphics2D object to perform the resize operation
        Graphics2D graphics2D = resizedImage.createGraphics();

        // Use AffineTransform to scale the original image to the new size
        AffineTransform transform = AffineTransform.getScaleInstance(
                (double) newWidth / originalImage.getWidth(),
                (double) newHeight / originalImage.getHeight()
        );

        // Draw the scaled image onto the new BufferedImage
        graphics2D.drawImage(originalImage, transform, null);

        // Dispose of the Graphics2D object to free up resources
        graphics2D.dispose();

        // Write the resized image to the specified file
        ImageIO.write(resizedImage, "jpg", resizedFile);
    }

    public static void main(String[] args) {
        try {
            File originalFile = new File("path/to/original/image.jpg");
            File resizedFile = new File("path/to/resized/image.jpg");

            int newWidth = 100; // Set your desired width
            int newHeight = 100; // Set your desired height

            resizeImage(originalFile, resizedFile, newWidth, newHeight);

            System.out.println("Image resized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
