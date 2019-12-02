//THIS CODE WAS BROUGHT TO YOU BY MAGGIA GANG
import javax.swing.JFrame;
import Menu.*;
import Visuals.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main extends JFrame {
    public Main() {
        setTitle("Chess++");

        setSize(640, 480);
        setResizable(false);
        Menu menu = new Menu(this);
        add(menu);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) {
    	Images.setUpImages();
    	Images.readMenuIcons();
        new Main();
    }
}
