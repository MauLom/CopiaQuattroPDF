package com.copsis.models.Tabla;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtils {

//  public static Image readImage2(BufferedImage img) {
//   final BufferedImage bufferedImage = ImageIO.read(img);
//		return new Image(bufferedImage);
//  }
  public static Image readImage(String string) throws MalformedURLException, IOException {
      URL url = new URL(string);
      final BufferedImage bufferedImage = ImageIO.read(url);
      return new Image(bufferedImage);
  }

  // utility class, no instance needed
  private ImageUtils() {
  }

  /**
   * <p>
   * Simple reading image from file
   * </p>
   *
   * @param imageFile {@link File} from which image will be loaded
   * @return {@link Image}
   * @throws IOException if loading image fails
   */
  public static Image readImage(File imageFile) throws IOException {
      final BufferedImage bufferedImage = ImageIO.read(imageFile);
      return new Image(bufferedImage);
  }

  /**
   * <p>
   * Provide an ability to scale {@link Image} on desired {@link Dimension}
   * </p>
   *
   * @param imageWidth Original image width
   * @param imageHeight Original image height
   * @param boundWidth Desired image width
   * @param boundHeight Desired image height
   * @return {@code Array} with image dimension. First value is width and
   * second is height.
   */
  public static float[] getScaledDimension(float imageWidth, float imageHeight, float boundWidth, float boundHeight) {
      float newImageWidth = imageWidth;
      float newImageHeight = imageHeight;

      // first check if we need to scale width
      if (imageWidth > boundWidth) {
         
          newImageWidth = boundWidth;
          
          // scale height to maintain aspect ratio
          newImageHeight = (newImageWidth * imageHeight) / imageWidth;
        
         
      }

      // then check if the new height is also bigger than expected
      if (newImageHeight > boundHeight) {
          newImageHeight = boundHeight;
          // scale width to maintain aspect ratio
          newImageWidth = (newImageHeight * imageWidth) / imageHeight;
      }

      float[] imageDimension = {newImageWidth, newImageHeight};
      return imageDimension;
  }
}
