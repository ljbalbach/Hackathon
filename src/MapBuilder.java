import java.io.*;

import java.util.*;

import javax.imageio.*;
import javax.imageio.stream.*;

import java.awt.*;
import java.awt.image.*;

/**
 * MapBuilder
 */
public class MapBuilder {

    // WHERE : is the fist character and ~ is the last character

    private String texturePath;
    private BufferedImage map;
    private final int BLOCK_SIZE = 160;
    private final int DIMENSIONS = 14;

    public MapBuilder(String texturePath) {
        this.texturePath = texturePath;
    }

    public String rleToString(String filePath) throws FileNotFoundException {

        // this is the file we are reading from
        File file = new File(filePath);
        // the string that is to be returned
        String output = "";
        // read from the file line by line
        Scanner inputStream = new Scanner(file);
        while (inputStream.hasNext()) {
            // data is the current line of the file being read
            String data = inputStream.next();

            // count represents the number of times a character appears
            int count = 0;
            String result = "";

            // iterate through each character in the line
            for (int i = 0; i < data.length(); i++) {
                // if the character is a digit it need to be incorporated into count
                char c = data.charAt(i);
                if (Character.isDigit(c)) {
                    // to update count: multiply count by 10, and add the current digit to count
                    count = count * 10 + Character.getNumericValue(c);
                } else {
                    // if count is 0, then that no number preceeds a symbol
                    // which means the symbol appears once
                    if (count == 0) {
                        count = 1;
                    }
                    // add the number of symbols that appear to the result
                    while (count > 0) {
                        result = result + String.valueOf(c);
                        // decrement count
                        count--;
                    }
                }
            }
            // add a newline after each line in the file
            output = output + result + "\n";
        }
        inputStream.close();
        // error check in case the file path is invalid

        return output;
    }

    public BufferedImage extractTexture(int x, int y, int w, int h) {
        Rectangle sourceRegion = new Rectangle(x, y, w, h); // The region you want to extract

        BufferedImage block = null;
        File file = new File(texturePath);
        try (ImageInputStream stream = ImageIO.createImageInputStream(file);) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(stream);

                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(sourceRegion); // Set region

                block = reader.read(0, param); // Will read only the region specified

            }
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

        return block;
    }

    public void createMap(String[] textMap, String mapName) {
        map = new BufferedImage(BLOCK_SIZE * 16, BLOCK_SIZE * 8, BufferedImage.TYPE_INT_RGB);
        Graphics2D paint = map.createGraphics();
        for (int y = 0; y < textMap.length; y++) {
            for (int x = 0; x < textMap[y].length(); x++) {
                int xBlock;

                if (textMap[y].charAt(x) <= '/' && textMap[y].charAt(x) >= '!') {
                    xBlock = textMap[y].charAt(x) - '!' + '' - '';
                }
                else if((textMap[y].charAt(x) <= '' && textMap[y].charAt(x) >= '')) {
                    xBlock = textMap[y].charAt(x) - '';
                }
                else {
                    xBlock = textMap[y].charAt(x) - ':' + (1 + '/' - '!' + '' - '');
                }
                // int yBlock = xBlock > DIMENSIONS ? xBlock / DIMENSIONS : 0;
                int yBlock = xBlock / DIMENSIONS;
                if (yBlock != 0) {
                    xBlock = xBlock % DIMENSIONS;
                    // xBlock -= DIMENSIONS * (xBlock / DIMENSIONS);
                }
                // System.out.println(xBlock);
                // System.out.println(yBlock);

                BufferedImage img = extractTexture(BLOCK_SIZE*xBlock, BLOCK_SIZE*yBlock, BLOCK_SIZE, BLOCK_SIZE);
                paint.drawImage(img, x * BLOCK_SIZE, y * BLOCK_SIZE, null);
            }
        }

        // Write the map as an image to a png file
        try {
            if (!ImageIO.write(map, "png", new File(mapName))) {
                System.out.println("WARNING! No appropriate writer was found...");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        for (int index = -32;index < 126; index ++) {
            if (index <= '/' && index >= '!') {
                System.out.print(index - '!' + 1 + '' - '');
                System.out.println(" " + (char) index);
            }
            else if((index <= '' && index >= '')) {
                System.out.print(index - '');
                System.out.println(" " + (char) index);
            }
            else if (index >= ':'){
                System.out.print(index - ':' + (1 + '/' - '!' + '' - ''));
                System.out.println(" " + (char) index);
            }
        }
        // TODO Change to get dynamic pathfinding
        MapBuilder builder = new MapBuilder(args[0]); // Texture pack
        String path = args[1]; // The rle file path --> THE FILE TO BE CREATED FOR THE MAP
        String[] textMap = null;

        try {
            textMap = builder.rleToString(path).split("\n");
        } catch(FileNotFoundException e) {
            System.out.println("The file at: " + path + " couldn't be found.");
            System.exit(0);
        }

        builder.createMap(textMap, args[2]); // The actual map name (PNG)

        // builder.extractTexture(builder.BLOCK_SIZE*19, builder.BLOCK_SIZE*5, 32, 32);

        KeyMoving screen = new KeyMoving(args[2]); // The actual map name (PNG)
    }
}