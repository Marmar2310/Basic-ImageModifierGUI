
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class EdgeDetector {

	Image image = null;
	Color black = new Color(0, 0, 0, 1.0);
	Image newImage = null;

	public Image filterImage(Image image) {

		Color[][] pixelsData = applyFilter(applyGreyscale(getPixelDataExtended(image)), createFilter());
		Color[][] borderless = new Color[pixelsData.length - 2][pixelsData.length - 2];

		for (int h = 0; h < borderless.length; h++) {
			for (int w = 0; w < borderless.length; w++) {
				borderless[h][w] = pixelsData[h + 1][w + 1];
			}
		}

		saveImage(borderless);

		return newImage;
	}

	public float[][] createFilter() {
		float[][] edgefilter = new float[3][3];

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				edgefilter[y][x] = -1;
			}
		}
		edgefilter[1][1] = 8;
		return edgefilter;
	}

	public Color[][] getPixelDataExtended(Image image) {
		int borderheight = (int) image.getHeight() + 2;
		int borderwidth = (int) image.getWidth() + 2;
		Color[][] bordPixColor = new Color[borderheight][borderwidth];
		PixelReader readerext = image.getPixelReader();

		for (int i = 0; i < image.getHeight() + 2; i++) {
			bordPixColor[i][0] = black;
			bordPixColor[0][i] = black;
			bordPixColor[borderheight - 1][i] = black;
			bordPixColor[i][borderwidth - 1] = black;
			bordPixColor[0][0] = black;
			bordPixColor[borderwidth - 1][borderwidth - 1] = black;
		}

		for (int h = 0; h < image.getHeight(); h++) {
			for (int w = 0; w < image.getWidth(); w++) {
				bordPixColor[h + 1][w + 1] = readerext.getColor(h, w);
			}
		}
		return bordPixColor;
	}

	public Color[][] getPixelData(Image image) {
		Color[][] pixColor = new Color[(int) image.getHeight()][(int) image.getWidth()];
		PixelReader reader = image.getPixelReader();

		for (int h = 0; h < image.getHeight(); h++) {
			for (int w = 0; w < image.getWidth(); w++) {

				pixColor[h][w] = reader.getColor(h, w);

			}
		}
		return pixColor;
	}

	public Color[][] applyGreyscale(Color[][] pixels) {

		for (int h = 0; h < pixels.length; h++) {
			for (int w = 0; w < pixels.length; w++) {

				double newRed = (pixels[h][w].getRed() + pixels[h][w].getBlue() + pixels[h][w].getGreen()) / 3;
				double newGreen = newRed;
				double newBlue = newGreen;
				Color grey = new Color(newRed, newGreen, newBlue, 1.0);
				pixels[h][w] = grey;
			}
		}
		return pixels;
	}

	public Color[][] applyFilter(Color[][] pixels, float[][] filter) {
		filter = createFilter();
		Color[][] filterpixels = new Color[pixels.length][pixels.length];

		for (int i = 0; i < pixels.length; i++) {
			filterpixels[i][0] = black;
			filterpixels[0][i] = black;
			filterpixels[pixels.length - 1][i] = black;
			filterpixels[i][pixels.length - 1] = black;
			filterpixels[0][0] = black;
			filterpixels[pixels.length - 1][pixels.length - 1] = black;
		}

		for (int h = 1; h < pixels.length - 1; h++) {
			for (int w = 1; w < pixels.length - 1; w++) {

				double topleftR = filter[0][0] * pixels[h - 1][w - 1].getRed();
				double topmidR = filter[0][1] * pixels[h - 1][w].getRed();
				double toprightR = filter[0][2] * pixels[h - 1][w + 1].getRed();
				double midleftR = filter[1][0] * pixels[h][w - 1].getRed();
				double middleR = filter[1][1] * pixels[h][w].getRed();
				double midrightR = filter[1][2] * pixels[h][w + 1].getRed();
				double bottomleftR = filter[2][0] * pixels[h + 1][w - 1].getRed();
				double bottommidR = filter[2][1] * pixels[h + 1][w].getRed();
				double bottomrightR = filter[2][2] * pixels[h + 1][w + 1].getRed();

				double newRed = topleftR + topmidR + middleR + midleftR + bottommidR + midrightR + bottomleftR
						+ bottomrightR + toprightR;

				double topleftG = filter[0][0] * pixels[h - 1][w - 1].getGreen();
				double topmidG = filter[0][1] * pixels[h - 1][w].getGreen();
				double toprightG = filter[0][2] * pixels[h - 1][w + 1].getGreen();
				double midleftG = filter[1][0] * pixels[h][w - 1].getGreen();
				double middleG = filter[1][1] * pixels[h][w].getGreen();
				double midrightG = filter[1][2] * pixels[h][w + 1].getGreen();
				double bottomleftG = filter[2][0] * pixels[h + 1][w - 1].getGreen();
				double bottommidG = filter[2][1] * pixels[h + 1][w].getGreen();
				double bottomrightG = filter[2][2] * pixels[h + 1][w + 1].getGreen();

				double newGreen = topleftG + topmidG + middleG + midleftG + bottommidG + midrightG + bottomleftG
						+ bottomrightG + toprightG;

				double topleftB = filter[0][0] * pixels[h - 1][w - 1].getBlue();
				double topmidB = filter[0][1] * pixels[h - 1][w].getBlue();
				double toprightB = filter[0][2] * pixels[h - 1][w + 1].getBlue();
				double midleftB = filter[1][0] * pixels[h][w - 1].getBlue();
				double middleB = filter[1][1] * pixels[h][w].getBlue();
				double midrightB = filter[1][2] * pixels[h][w + 1].getBlue();
				double bottomleftB = filter[2][0] * pixels[h + 1][w - 1].getBlue();
				double bottommidB = filter[2][1] * pixels[h + 1][w].getBlue();
				double bottomrightB = filter[2][2] * pixels[h + 1][w + 1].getBlue();

				double newBlue = topleftB + topmidB + middleB + midleftB + bottomleftB + midrightB + bottommidB
						+ bottomrightB + toprightB;

				if (newRed > 1) {
					newRed = 1.0;
				}

				if (newRed < 0) {
					newRed = 0;
				}

				if (newGreen > 1) {
					newGreen = 1.0;
				}

				if (newGreen < 0) {
					newGreen = 0;
				}

				if (newBlue > 1) {
					newBlue = 1.0;
				}

				if (newBlue < 0) {
					newBlue = 0;
				}

				Color newPixel = new Color(newRed, newGreen, newBlue, 1.0);

				filterpixels[h][w] = newPixel;

			}

		}

		return filterpixels;
	}

	private void saveImage(Color[][] pixels) {

		WritableImage wimg = new WritableImage(pixels.length, pixels.length);
		PixelWriter pw = wimg.getPixelWriter();
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels.length; j++) {
				pw.setColor(i, j, pixels[i][j]);
			}
		}
		BufferedImage bImage = SwingFXUtils.fromFXImage(wimg, null);
		newImage = SwingFXUtils.toFXImage(bImage, null);
	}
}
