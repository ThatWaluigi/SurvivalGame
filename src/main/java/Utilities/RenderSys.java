package Utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class RenderSys {
     /**
     * Draws the image at the corner instead of the center
     * @param g The graphics
     * @param ImageFile The image you want to draw
     * @param Scale The scale of the image
     * @param X X location
     * @param Y Y Location
     * */
    public void DrawImage(Graphics g, int X, int Y, Image ImageFile, int Scale){
        g.drawImage(ImageFile, X, Y, Scale, Scale, null);
    }
    /**
     * Draws the image in the center instead of the corner
     * @param g The graphics
     * @param ImageFile The image you want to draw
     * @param Scale The scale of the image
     * @param X X location
     * @param Y Y location
     * */
    public void DrawCenteredImage(Graphics g, int X, int Y, Image ImageFile, int Scale){
        g.drawImage(ImageFile, X + ImageFile.getWidth(null) / 2,
                Y + ImageFile.getHeight(null) / 2, Scale, Scale, null);
    }
    /**
     * Used to get an image from a specified place
     * @param Path This is the path to the image
     * @return Returns the Image received as a java.awt.Image
     * */
    public Image getImage(String Path){
        URL url = this.getClass().getClassLoader().getResource(Path);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }else{
            System.out.println("ERROR: (" + Path + ")");
            return null;
        }
    }
    /**
     * Used to stack multiple images on top of each other
     * @param Path The path to the Image
     * @param ImageFiles A list of images to stack (note: First image defines size of the image)
     * @return Returns the stacked image
     */
    public Image StackImages(String Path, String[] ImageFiles) {
        Image[] Images = new Image[ImageFiles.length];
        int a = 0;
        for (String File : ImageFiles) {
            Images[a] = getImage(Path + File);
            a++;
        }
        BufferedImage image = new BufferedImage(Images[0].getWidth(null), Images[0].getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        for (Image value : Images) {
            g.drawImage(value, 0, 0, null);
        }
        return image;
    }

    public void DrawGrid(Graphics g, Dimension GridSize, int GridTileSize, Dimension ScreenSize, Color color){
        g.setColor(color);
        for(int x = 0; x < GridSize.width; ++x) {
            g.drawLine(x * GridTileSize, 0, x * GridTileSize, ScreenSize.height);
            g.drawLine(0, x * GridTileSize, ScreenSize.width, x * GridTileSize);
        }
    }
}