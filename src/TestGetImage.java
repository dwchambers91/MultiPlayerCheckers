import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestGetImage {


    private BufferedImage r_piece;
    private BufferedImage b_piece;
    private BufferedImage r_piece_king;
    private BufferedImage b_piece_king;

    public static void main (String[] args){

        System.out.println(new TestGetImage());
    }
    public TestGetImage(){
        System.out.println(CheckersPanel());
    }

    public String CheckersPanel() {
        try {
            this.r_piece = ImageIO.read(getClass().getResource("/Images/RPiece.jpg"));
            this.b_piece = ImageIO.read(getClass().getResource("/Images/BPiece.jpg"));
            this.r_piece_king = ImageIO.read(getClass().getResource("/Images/RKing.jpg"));
            this.b_piece_king = ImageIO.read(getClass().getResource("/Images/BKing.jpg"));
            String test = "success";
            return test;
        } catch (IOException e) {
            System.out.println("could not open file");
        }
        return "uuuuu";
    }

}
