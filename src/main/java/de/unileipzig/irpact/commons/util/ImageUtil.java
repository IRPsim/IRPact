package de.unileipzig.irpact.commons.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public final class ImageUtil {

    public static final BiConsumer<RenderedImage, Graphics2D> DEFAULT_SETUP = (img, g) -> {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.BLACK);
    };
    public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    protected ImageUtil() {
    }

    public static String getFormatName(Path path) {
        if(path == null) throw new NullPointerException("path is null");

        String fileName = path.getFileName().toString().toLowerCase();
        if(fileName.endsWith("png")) {
            return "png";
        }
        //currently only png supported
        return null;
    }

    public static BufferedImage createFittingImage(List<String> lines, Font font, int imageType) {
        if(lines == null) throw new NullPointerException("lines is null");
        if(lines.isEmpty()) throw new IllegalArgumentException("lines is empty");
        if(font == null) throw new NullPointerException("font is null");

        BufferedImage temp = new BufferedImage(1, 1, imageType);
        Graphics2D g = (Graphics2D) temp.getGraphics();

        double width = Double.NaN;
        double height = 0;

        try {
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics();
            for(String line: lines) {
                Rectangle2D lineRect = metrics.getStringBounds(line, g);
                if(Double.isNaN(width) || width < lineRect.getWidth()) {
                    width = lineRect.getWidth();
                }
                height += metrics.getHeight();
            }
            height += metrics.getHeight(); //with extra empty line
        } finally {
            g.dispose();
        }

        return new BufferedImage(
                (int) Math.ceil(width),
                (int) Math.ceil(height),
                imageType
        );
    }

    public static void writeLines(List<String> lines, Font font, BufferedImage image, BiConsumer<? super RenderedImage, ? super Graphics2D> setup) {
        if(lines == null) throw new NullPointerException("lines is null");
        if(lines.isEmpty()) throw new IllegalArgumentException("lines is empty");
        if(font == null) throw new NullPointerException("font is null");
        if(image == null) throw new NullPointerException("image is null");

        Graphics2D g = (Graphics2D) image.getGraphics();
        try {
            if(setup != null) {
                setup.accept(image, g);
            }
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics();

            int y = metrics.getHeight();
            for(String line: lines) {
                g.drawString(line, 0, y);
                y += metrics.getHeight();
            }
        } finally {
            g.dispose();
        }
    }

    public static void storeImage(RenderedImage image, Path target) throws IOException {
        if(image == null) throw new NullPointerException("image is null");
        if(target == null) throw new NullPointerException("target is null");

        String formatName = getFormatName(target);
        if(formatName == null) {
            throw new IllegalArgumentException("unsupported format: " + target);
        }
        ImageIO.write(image, formatName, target.toFile());
    }

    public static void writeText(CharSequence text, Path target) throws IOException {
        writeText(text, DEFAULT_FONT, DEFAULT_SETUP, target);
    }

    public static void writeText(CharSequence text, BiConsumer<? super RenderedImage, ? super Graphics2D> setup, Path target) throws IOException {
        writeText(text, DEFAULT_FONT, setup, target);
    }

    public static void writeText(CharSequence text, Font font, Path target) throws IOException {
        writeText(text, font, DEFAULT_SETUP, target);
    }

    public static void writeText(CharSequence text, Font font, BiConsumer<? super RenderedImage, ? super Graphics2D> setup, Path target) throws IOException {
        if(text == null) throw new NullPointerException("text is null");
        if(font == null) throw new NullPointerException("font is null");
        if(target == null) throw new NullPointerException("target is null");

        List<String> lines = StringUtil.getLines(text);

        BufferedImage image = createFittingImage(lines, font, BufferedImage.TYPE_4BYTE_ABGR);
        writeLines(lines, font, image, setup);
        storeImage(image, target);
    }
}
