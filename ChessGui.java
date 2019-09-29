import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.Timer;
import javax.imageio.*;

public class ChessGui extends JFrame
{

        public Board gameBoard = new Board();

        private JPanel mainBoard = new JPanel();
        private JPanel leftPanel = new JPanel();
        private JPanel bottomPanel = new JPanel();
        private JPanel topPanel = new JPanel();

        ChessSquare[][] squares = new ChessSquare[8][8];
        public static BufferedImage pieces[][] = new BufferedImage[2][6];


        public ChessGui()
        {
            gameBoard.standardChess();
            setUpImages();
            setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getSize().getWidth()*.75), (int)(Toolkit.getDefaultToolkit().getScreenSize().getSize().getHeight()*.75));
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.15), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.15));
            setLayout(new BorderLayout());
            topPanel.setLayout(new BorderLayout());
            mainBoard.setLayout(new GridLayout(8, 8));
            leftPanel.setLayout(new GridLayout(8,0));
            bottomPanel.setLayout(new GridLayout(0,8));

            boardSetUp();
            sidesSetup();
            updateBoard(gameBoard);
            add(mainBoard);
            add(leftPanel, BorderLayout.WEST);
            add(bottomPanel, BorderLayout.SOUTH);
            setVisible(true);
        }
        /*
        DEPRECATED
        private void layPieces()
        {
            for(int i = 0; i < 8; i ++)
            {
                squares[1][i].setIcon(new ImageIcon(pieces[0][5]));
                squares[6][i].setIcon(new ImageIcon(pieces[1][5]));
            }
            //Top Side
            squares[0][0].setIcon(new ImageIcon(pieces[0][2]));
            squares[0][1].setIcon(new ImageIcon(pieces[0][3]));
            squares[0][2].setIcon(new ImageIcon(pieces[0][4]));
            squares[0][3].setIcon(new ImageIcon(pieces[0][1]));
            squares[0][4].setIcon(new ImageIcon(pieces[0][0]));
            squares[0][5].setIcon(new ImageIcon(pieces[0][4]));
            squares[0][6].setIcon(new ImageIcon(pieces[0][3]));
            squares[0][7].setIcon(new ImageIcon(pieces[0][2]));

            //Bottom Side
            squares[7][0].setIcon(new ImageIcon(pieces[1][2]));
            squares[7][1].setIcon(new ImageIcon(pieces[1][3]));
            squares[7][2].setIcon(new ImageIcon(pieces[1][4]));
            squares[7][3].setIcon(new ImageIcon(pieces[1][1]));
            squares[7][4].setIcon(new ImageIcon(pieces[1][0]));
            squares[7][5].setIcon(new ImageIcon(pieces[1][4]));
            squares[7][6].setIcon(new ImageIcon(pieces[1][3]));
            squares[7][7].setIcon(new ImageIcon(pieces[1][2]));
        }
        */
        //BASIC SETUP HELPER METHODS
        private void sidesSetup()
        {
            for(int i = 8; i > 0; i--)
            {
                JLabel newLabel = new JLabel(i + "", SwingConstants.CENTER);
                newLabel.setFont(new Font(newLabel.getFont().getName(), Font.BOLD, 25));

                leftPanel.add(newLabel);
            }
            for(int i = 65; i < 73; i ++) //Char values for A-H
            {
                String letter = Character.toString((char)i);
                JLabel newLabel = new JLabel(letter, SwingConstants.CENTER);
                newLabel.setFont(new Font(newLabel.getFont().getName(), Font.BOLD, 20));

                bottomPanel.add(newLabel);
            }

        }
        private void boardSetUp()
        {
            int counter = 0;
            for(int x = 0;x < 8; x ++) //Adding tiles to board
            {
                counter = Math.abs(counter-1);
                for(int y = 0; y < 8; y ++)
                {
                    ChessSquare newSquare = new ChessSquare(counter, x, y);
                    newSquare.addActionListener(new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent)
                        {
                            ChessSquare temp = (ChessSquare) actionEvent.getSource();
                            temp.Highlight();
                            System.out.println(actionEvent.getActionCommand()); //Action Command is set to coordinates on board of where you clicked.
                        }                                                       //Note: These coordinates don't correspond to the ones shown in the GUI, and instead
                    });                                                         //      show their indexes in the array squares
                    mainBoard.add(newSquare);
                    counter = Math.abs(counter - 1);
                    squares[x][y] = newSquare;

                }
            }
        }
        private void setUpImages()
        {
            try
            {
                BufferedImage biPieces = ImageIO.read(new File("img/Pieces.png"));
                for(int i = 0; i < 2; i ++)
                {
                    for(int j = 0; j < 6; j++)
                    {
                        pieces[i][j] = biPieces.getSubimage(j*64, i*64, 64, 64);
                    }
                }
            }
            catch(Exception e){}
        }
        //END BASE SETUP


        //Updates images on the mainBoard JPanel
        public void updateBoard(Board board)
        {
            //Remove icons
            for(int x = 0; x < 8; x ++)
            {
                for(int y = 0; y < 8; y++)
                {
                    squares[x][y].setIcon(null);
                }
            }
            //Setting up Icons in shape of new board using Roy's code for displaying the array but setting icons instead.
            for(int x = 0; x < 8; x ++)
            {
                for(int y = 0; y < 8; y ++)
                {
                            //white pieces
                            if(((board.whiteKing>>(x*8)+y)&1) == 1)
                            {
                                squares[x][y].setIcon(new ImageIcon(pieces[1][0]));         //"wK";
                                squares[x][y].setActionCommand("White King " + x + " " + y);
                            }
                            else if(((board.whiteQueens>>(x*8)+y)&1) == 1)
                            {
                                squares[x][y].setIcon(new ImageIcon(pieces[1][1]));         //"wq";
                                squares[x][y].setActionCommand("White Queen " + x + " " + y);
                            }
                            else if(((board.whiteRooks>>(x*8)+y)&1) == 1)
                            {
                                squares[x][y].setIcon(new ImageIcon(pieces[1][2]));         //"wr";
                                squares[x][y].setActionCommand("White Rook " + x + " " + y);
                            }
                            else if(((board.whiteKnights>>(x*8)+y)&1) == 1)
                            {
                                squares[x][y].setIcon(new ImageIcon(pieces[1][3]));         //"wk";
                                squares[x][y].setActionCommand("White Knight " + x + " " + y);
                            }
                            else if(((board.whiteBishops>>(x*8)+y)&1) == 1) {
                                squares[x][y].setIcon(new ImageIcon(pieces[1][4]));         //"wb";
                                squares[x][y].setActionCommand("White Bishop " + x + " " + y);
                            }
                            else if(((board.whitePawns>>(x*8)+y)&1) == 1) {
                                squares[x][y].setIcon(new ImageIcon(pieces[1][5]));         //"wp";
                                squares[x][y].setActionCommand("White Pawn " + x + " " + y);
                            }

                                //black pieces
                            else if(((board.blackKing>>(x*8)+y)&1) == 1) {
                                squares[x][y].setIcon(new ImageIcon(pieces[0][0]));           //"bK";
                                squares[x][y].setActionCommand("Black King " + x + " " + y);
                            }
                            else if(((board.blackQueens>>(x*8)+y)&1) == 1) {
                                squares[x][y].setIcon(new ImageIcon(pieces[0][1]));           //"bq";
                                squares[x][y].setActionCommand("Black Queen " + x + " " + y);
                            }
                            else if(((board.blackRooks>>(x*8)+y)&1) == 1) {
                                squares[x][y].setIcon(new ImageIcon(pieces[0][2]));           //"br";
                                squares[x][y].setActionCommand("Black Rook " + x + " " + y);
                            }
                            else if(((board.blackKnights>>(x*8)+y)&1) == 1){
                                squares[x][y].setIcon(new ImageIcon(pieces[0][3]));           //"bk";
                                squares[x][y].setActionCommand("Black Knight " + x + " " + y);
                            }
                            else if(((board.blackBishops>>(x*8)+y)&1) == 1) {
                                squares[x][y].setIcon(new ImageIcon(pieces[0][4]));           //"bb";
                                squares[x][y].setActionCommand("Black Bishop " + x + " " + y);
                            }
                            else if(((board.blackPawns>>(x*8)+y)&1) == 1) {
                                squares[x][y].setIcon(new ImageIcon(pieces[0][5]));           //"bp";
                                squares[x][y].setActionCommand("Black Pawn " + x + " " + y);
                            }
                }
            }
            mainBoard.revalidate();
            mainBoard.repaint();
        }
}
