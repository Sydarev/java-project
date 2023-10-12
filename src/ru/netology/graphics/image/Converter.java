package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    protected int maxWidth;
    protected int maxHeight;
    protected double maxRatio;
    protected TextColorSchema schema = new Schema();

    public Converter(int maxHeight, int maxWidth, double maxRatio) {
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.maxRatio = maxRatio;
    }


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        //блок, масштабирующий размеры изображения
        if ((double) (newWidth / newHeight) > maxRatio) {
            throw new BadImageSizeException(newWidth / newHeight, maxRatio);
        } else if ((double) (newHeight / newWidth) > maxRatio) {
            throw new BadImageSizeException(newHeight / newWidth, maxRatio);
        }
        double inc1 = 0, inc2 = 0;
        if (newHeight > maxHeight || newWidth > maxWidth) {
            inc1 = newHeight / maxHeight;
            inc2 = newWidth / maxWidth;

            newWidth = newWidth / ((int) Math.max(inc1, inc2));
            newHeight = newHeight / ((int) Math.max(inc1, inc2));
        }

        //блок, преобразующий изображение
        ImageIO.write(img, "png", new File("outSource.png"));
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        ImageIO.write(bwImg, "png", new File("outSource2.png"));

        //блок, конвертирующий изображение
        char[][] image = new char[newHeight][newWidth];
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                image[h][w] = c;
            }
        }

        //блок, собирающий изображение в 1 строку
        StringBuilder imageStr = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                imageStr.append(image[h][w]);
                imageStr.append(image[h][w]);
            }
            imageStr.append('\n');
        }
        return imageStr.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = maxWidth;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = maxHeight;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
