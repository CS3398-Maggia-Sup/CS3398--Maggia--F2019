//Kieran Hsieh
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.imageio.*;
import javax.swing.border.Border;

public class ChessGui3 extends JFrame
{
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private String message = null;
	
        public int pawnID = 0;
        public int teamNum = 0;
        public boolean locked = false;
        public Board gameBoard = new Board();
        public int promoButtonClicked = 0;
        public ChessGui3 thisGui = this;
        private String serverIP;
        private int port;

        private JPanel mainPanel = new JPanel();
        private JPanel mainBoard = new JPanel();
        private JPanel leftPanel = new JPanel();
        private JPanel bottomPanel = new JPanel();
        private JLabel topLabel = new JLabel();
        private PieceHistory historyPanel = new PieceHistory();
        private JPanel rightPanel = new JPanel();
        private JPanel rightPawnChoice = new JPanel();

        ChessSquare[][] squares = new ChessSquare[8][8];
        public static BufferedImage pieces[][] = new BufferedImage[2][6];
        public static BufferedImage colors[][] = new BufferedImage[3][6];

        private int[] lastClicked = new int[2];
        ArrayList<ChessSquare> highlighted = new ArrayList<ChessSquare>();
        public ChessGui3(String address, int p)   //Basic Setup
        {
        	
        	serverIP = address;
        	port = p;
            gameBoard.standardChess();

            setUpImages();
            setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getSize().getWidth()*.7), (int)(Toolkit.getDefaultToolkit().getScreenSize().getSize().getHeight()*.75));
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.15), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.15));
            setLayout(new BorderLayout());

            topLabel.setText("White Turn");
            topLabel.setFont(new Font("Serif", Font.BOLD, 20));
            topLabel.setHorizontalAlignment(JLabel.CENTER);

            mainPanel.setLayout(new BorderLayout());
            mainBoard.setLayout(new GridLayout(8, 8));
            leftPanel.setLayout(new GridLayout(8,0));
            rightPanel.setLayout(new GridLayout(2, 1));
            bottomPanel.setLayout(new GridLayout(0,8));
            rightPawnChoice.setLayout(new GridLayout(2, 2));

            boardSetUp();
            sidesSetup();
            updateBoard(gameBoard);

            mainPanel.add(mainBoard);
            mainPanel.add(leftPanel, BorderLayout.WEST);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);
            mainPanel.add(topLabel, BorderLayout.NORTH);
            add(rightPanel, BorderLayout.EAST);
            add(mainPanel, BorderLayout.CENTER);
            setVisible(true);
            
           startRunning();
        }
        
        public void startRunning() {
    		try {
    				try {
    					connection = new Socket(InetAddress.getByName(serverIP), port);
    			            try {
    			            	output = new ObjectOutputStream(connection.getOutputStream());
    			            	output.flush();
    							input = new ObjectInputStream(connection.getInputStream());
    			            } catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    			            readin();
    				/*} catch(EOFException eofException) {
    					System.out.println("FDFDDF");*/
    				}
    				finally {
    					System.out.println(connection.getInputStream());
    				}
    			
    		} catch(IOException ioException) {
    			ioException.printStackTrace();
    		}
    	}
        
        private void readin() {
        	while(true) {
	    		try {
					try {
						message = (String) input.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		if(teamNum == 0 && Board.convertToCoord(message) != -1L) {
	    			if(gameBoard.makeMove(teamNum, message, false)) {
	    				teamNum = Math.abs(teamNum - 1);
	    				updateBoard(gameBoard);
	    			}
	    		}
        	}
    	}
        
        //BASIC SETUP HELPER METHODS
        private void sidesSetup()
        {
            //Left Side number labels
            for(int i = 8; i > 0; i--)
            {
                JLabel newLabel = new JLabel(i + "", SwingConstants.CENTER);
                newLabel.setFont(new Font(newLabel.getFont().getName(), Font.BOLD, 25));

                leftPanel.add(newLabel);
            }
            //Bottom side letters
            for(int i = 65; i < 73; i ++) //Char values for A-H
            {
                String letter = Character.toString((char)i);
                JLabel newLabel = new JLabel(letter, SwingConstants.CENTER);
                newLabel.setFont(new Font(newLabel.getFont().getName(), Font.BOLD, 20));

                bottomPanel.add(newLabel);
            }
            //Right side Panels
            //Dimension d = new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getSize().getWidth()*.155), (int)(Toolkit.getDefaultToolkit().getScreenSize().getSize().getHeight()*.375));
            rightPanel.add(historyPanel);
            rightPanel.add(rightPawnChoice);
        }
        public void showPawnPromotion(int team, long coord)
        {
            locked = true;
            int teamIcon = Math.abs(team-1);
            for(int i = 1; i < 5; i ++)
            {
                JButton choiceButton = new JButton();
                choiceButton.setIcon(new ImageIcon(pieces[teamIcon][i]));
                choiceButton.setActionCommand(i + "");
                choiceButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e)
                    {
                        locked = false;
                        JButton temp = (JButton)  e.getSource();
                        promoButtonClicked = Integer.parseInt(temp.getActionCommand());
                        pawnID = promoButtonClicked;
                        PawnPromote.promotePawn(team, coord, gameBoard, pawnID);

                        temp.getParent().removeAll();
                        rightPawnChoice.repaint();
                        updateBoard(gameBoard);
                    }
                });
                rightPawnChoice.add(choiceButton);
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
                    {                                                                       //When tiles are clicked
                        @Override
                        public void actionPerformed(ActionEvent actionEvent)
                        {
                            if(!locked)
                            {
                                boolean moveMade = false;
                                String moveString = "";

                                moveString += convertStrings(lastClicked[0], lastClicked[1]);      //Converts coordinates to a usable form
                                ChessSquare temp = (ChessSquare) actionEvent.getSource();
                                temp.Highlight();
                                squares[lastClicked[0]][lastClicked[1]].unHighlight();
                                int[] pieceMovedPos = new int[2];
                                pieceMovedPos[0] = lastClicked[0];
                                pieceMovedPos[1] = lastClicked[1];

                                for (int i = 0; i < highlighted.size(); i++)                //Unhighlight previously highlighted tiles
                                {
                                    highlighted.get(i).unHighlight();
                                }
                                for (int i = 6; i < temp.getActionCommand().length(); i++)       //Find coordinates for current button being pressed
                                {
                                    int count = 0;
                                    if (temp.getActionCommand().substring(i, i + 1).equals(" ")) {
                                        lastClicked[0] = Integer.parseInt(temp.getActionCommand().substring(i + 1, i + 2));
                                        lastClicked[1] = Integer.parseInt(temp.getActionCommand().substring(i + 3));
                                        break;
                                    }
                                }
                                
                                if(teamNum == 1) {
	                                moveString += convertStrings(lastClicked[0], lastClicked[1]);                                   //Convert current coordinates into usable form
	
	                                moveMade = gameBoard.makeMove(teamNum, moveString, true);                           //Check to see if a move has been made
                                }
                                //else if(Board.convertToCoord(message) != -1L) {
                                	//moveMade = gameBoard.makeMove(teamNum, message, false);
                                //}
                                if (moveMade) {
                                    teamNum = Math.abs(teamNum - 1);
                                    String tempString = "";
                                    String actionCommandString = squares[pieceMovedPos[0]][pieceMovedPos[1]].getActionCommand();
                                    tempString += actionCommandString.substring(0, 5);
                                    int spaceIndex = actionCommandString.substring(6).indexOf(' ') + 6;
                                    tempString += " " + actionCommandString.substring(6, spaceIndex);
                                    tempString += " has moved from " + moveString.substring(0, 2) + " to " + moveString.substring(2);
                                    historyPanel.addMove(tempString);
                                    try {
                            			output.writeObject(moveString);
                            			output.flush();
                            		} catch(IOException ioException) {
                            			System.out.println("wut");
                            		}
                                } else {
                                    String teamString = "";
                                    if (teamNum == 0) {
                                        teamString = "White";
                                    } else {
                                        teamString = "Black";
                                    }
                                    if (squares[lastClicked[0]][lastClicked[1]].getActionCommand().substring(0, 5).equals(teamString)) {
                                        highlighted = displayPossibleMoves(gameBoard.showMoves(convertStrings(lastClicked[0], lastClicked[1])));     //Display the possible moves of piece that has been clicked
                                    } else {
                                        highlighted = new ArrayList<ChessSquare>();
                                    }
                                }
                                updateBoard(gameBoard);                             //Action Command is set to coordinates on board of where you clicked.
                            }
                        }

                            //Note: These coordinates don't correspond to the ones shown in the GUI, and instead
                    });                                                         //      show their indexes in the array squares
                    mainBoard.add(newSquare);
                    counter = Math.abs(counter - 1);
                    squares[x][y] = newSquare;
                }
            }
        }
        private ArrayList<ChessSquare> displayPossibleMoves(long board)                 //Highlights possible moves with board method
        {
            ArrayList<ChessSquare> retList = new ArrayList<ChessSquare>();
            for(int x = 0; x < 8; x ++)
            {
                for(int y = 0; y < 8;y ++)
                {
                    if(((board>>(x*8)+y)&1) == 1)
                    {
                        squares[x][y].Highlight();
                        retList.add(squares[x][y]);
                    }
                }
            }
            return retList;
        }
        private void setUpImages()                                                                                      //Separates consolidated image file into different images
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
                BufferedImage biColors = ImageIO.read(new File("img/colors-1.jpg"));
                for(int i = 0; i < 3; i++)
                {
                    for(int j = 0; j < 6; j ++)
                    {
                        colors[i][j] = biColors.getSubimage((j*212)+30, (i*204)+23, 20, 20);
                        if(i == 2 && j == 0){ break; }
                    }
                }
            }
            catch(Exception e){}
        }
        //END BASE SETUP

        private String convertStrings(int x, int y)                                                                     //Converts coordinates to a different form
        {
            String ret = "";
            ret+= (char)(y + 97);
            ret+= 8-x;
            return ret;
        }
        //Updates images on the mainBoard JPanel
        public void updateBoard(Board board)
        {
            if(PawnPromote.promotion == true) {
            	if((board.pawnsBB[0]&Board.row8) != 0) {
            		showPawnPromotion(0, PawnPromote.coord);
        		}
        		if((board.pawnsBB[1]&Board.row1) != 0) {
        			showPawnPromotion(1, PawnPromote.coord);
        		}
            }
            if(teamNum == 0)
            {
                topLabel.setText("White Turn");
            }
            else
            {
                topLabel.setText("Black Turn");
            }
            if(gameBoard.check[0] == 1 || gameBoard.check[1] == 1)
            {
                topLabel.setText("Check! " + topLabel.getText());
            }
            if(gameBoard.teamWon == 0)
            {
                topLabel.setText("White team wins!");
            }
            else if(gameBoard.teamWon == 1)
            {
                topLabel.setText("Black team wins!");
            }
            else if(gameBoard.teamWon == -1)
            {
                topLabel.setText("Stalemate!");
            }
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
	                if(((board.kingBB[0]>>(x*8)+y)&1) == 1)
	                {
	                    squares[x][y].setIcon(new ImageIcon(pieces[1][0]));         //"wK";
	                    squares[x][y].setActionCommand("White King " + x + " " + y);
	                }
	                else if(((board.queensBB[0]>>(x*8)+y)&1) == 1)
	                {
	                    squares[x][y].setIcon(new ImageIcon(pieces[1][1]));         //"wq";
	                    squares[x][y].setActionCommand("White Queen " + x + " " + y);
	                }
	                else if(((board.rooksBB[0]>>(x*8)+y)&1) == 1)
	                {
	                    squares[x][y].setIcon(new ImageIcon(pieces[1][2]));         //"wr";
	                    squares[x][y].setActionCommand("White Rook " + x + " " + y);
	                }
	                else if(((board.knightsBB[0]>>(x*8)+y)&1) == 1)
	                {
	                    squares[x][y].setIcon(new ImageIcon(pieces[1][3]));         //"wk";
	                    squares[x][y].setActionCommand("White Knight " + x + " " + y);
	                }
	                else if(((board.bishopsBB[0]>>(x*8)+y)&1) == 1) {
	                    squares[x][y].setIcon(new ImageIcon(pieces[1][4]));         //"wb";
	                    squares[x][y].setActionCommand("White Bishop " + x + " " + y);
	                }
	                else if(((board.pawnsBB[0]>>(x*8)+y)&1) == 1) {
	                    squares[x][y].setIcon(new ImageIcon(pieces[1][5]));         //"wp";
	                    squares[x][y].setActionCommand("White Pawn " + x + " " + y);
	                }
	
	                    //black pieces
	                else if(((board.kingBB[1]>>(x*8)+y)&1) == 1) {
	                    squares[x][y].setIcon(new ImageIcon(pieces[0][0]));           //"bK";
	                    squares[x][y].setActionCommand("Black King " + x + " " + y);
	                }
	                else if(((board.queensBB[1]>>(x*8)+y)&1) == 1) {
	                    squares[x][y].setIcon(new ImageIcon(pieces[0][1]));           //"bq";
	                    squares[x][y].setActionCommand("Black Queen " + x + " " + y);
	                }
	                else if(((board.rooksBB[1]>>(x*8)+y)&1) == 1) {
	                    squares[x][y].setIcon(new ImageIcon(pieces[0][2]));           //"br";
	                    squares[x][y].setActionCommand("Black Rook " + x + " " + y);
	                }
	                else if(((board.knightsBB[1]>>(x*8)+y)&1) == 1){
	                    squares[x][y].setIcon(new ImageIcon(pieces[0][3]));           //"bk";
	                    squares[x][y].setActionCommand("Black Knight " + x + " " + y);
	                }
	                else if(((board.bishopsBB[1]>>(x*8)+y)&1) == 1) {
	                    squares[x][y].setIcon(new ImageIcon(pieces[0][4]));           //"bb";
	                    squares[x][y].setActionCommand("Black Bishop " + x + " " + y);
	                }
	                else if(((board.pawnsBB[1]>>(x*8)+y)&1) == 1) {
	                    squares[x][y].setIcon(new ImageIcon(pieces[0][5]));           //"bp";
	                    squares[x][y].setActionCommand("Black Pawn " + x + " " + y);
	                }
	                else
	                {
	                    squares[x][y].setActionCommand("White  " + x + " " + y);
	                }
                }
            }
            mainBoard.revalidate();
            mainBoard.repaint();
        }
}