package Visuals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;

public class Images {

	public static BufferedImage pieces[][] = new BufferedImage[2][6];
	public static List<BufferedImage[][]> pieceModels = new ArrayList<>();
	public static BufferedImage menuIcons[] = new BufferedImage[10];

	public static void setUpImages() {                                          //Separates consolidated image file into different images

		File modelPath = new File("img");
      File[] pathArray = modelPath.listFiles();
      Arrays.sort(pathArray);
      for(int m = 0; m < 5; m++)
      {
        try
        {
            BufferedImage biPieces = ImageIO.read(pathArray[m]);
						BufferedImage pieceSet[][] = new BufferedImage[2][6];
            for(int i = 0; i < 2; i++)
            {
                for(int j = 0; j < 6; j++)
                {
                    pieceSet[i][j] = biPieces.getSubimage(j*(biPieces.getWidth()/6), i*(biPieces.getWidth()/6), (biPieces.getWidth()/6), (biPieces.getWidth()/6));
                }
            }
						pieceModels.add(pieceSet);
        }
        catch(Exception e)
        {
          System.out.println("Problem reading icon image files.");
        }
      }
			pieces = pieceModels.get(2);
    }

    public static void readMenuIcons() {
      try
      {
        BufferedImage tempIcons = ImageIO.read(new File("img/SmallModels.png"));
        for(int i = 0; i < 10; i++)
        {
          menuIcons[i] = tempIcons.getSubimage(i*(tempIcons.getWidth()/10), 0, (tempIcons.getWidth()/10), 25);
        }
      }
      catch(Exception e)
      {
        System.out.println("Problem reading menu image files.");
      }
    }

		public static void newImages(int tm, int mdl) {
			BufferedImage[][] temp = new BufferedImage[2][6];
			temp = pieceModels.get(mdl/2);
			BufferedImage[] im = new BufferedImage[6];
			im = temp[mdl%2];
			pieces[tm] = im;
		}

		public static BufferedImage getBufferedImage(String tm, String pce) {
				Integer index1, index2;
				if(tm.equals("White"))
					index1 = 1;
				else
					index1 = 0;

				switch(pce){
					case "Pawn":
							index2 = 5;
							break;
					case "Bishop":
							index2 = 4;
							break;
					case "Knight":
							index2 = 3;
							break;
					case "Wall":
					case "Rook":
							index2 = 2;
							break;
					case "Archer":
					case "Queen":
							index2 = 1;
							break;
					case "King":
							index2 = 0;
							break;
					default:
							index2 = null;
							break;
				}
				if(index2 == null)
					return null;
				else
					return pieces[index1][index2];
		}
}
