//Roy Grady

import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Board {
	
	//white pieces
	long whiteKing;
	long whiteQueens;
	long whiteRooks;
	long whiteBishops;
	long whiteKnights;
	long whitePawns;	
	long whitePieces;
	
	//black pieces
	long blackKing;
	long blackQueens;
	long blackRooks;
	long blackBishops;
	long blackKnights;
	long blackPawns;	
	long blackPieces;
	
	//king and sliders
	long pinnersW;
	long pinnedB;
	long interfereB;
	long pinnersB;
	long pinnedW;
	long interfereW;
	
	//misc
	long notWhite;
	long notBlack;
	long empty;
	long whiteThreaten;
	long blackThreaten;
	long wKThreats;
	long bKThreats;
	long wGetAllPM;
	long bGetAllPM;
	int whiteCheck;
	int blackCheck;
	int promoteWhite;
	int teamWon = 2;
	
	static long row1 = -72057594037927936L;
	static long row2 = 71776119061217280L;
	static long row4 = 1095216660480L;
	static long row5 = 4278190080L;
	static long row7 = 65280L;
	static long row8 = 255L;
	static long colA = 72340172838076673L;
	static long colB = 144680345676153346L;
	static long colG = 4629771061636907072L;
	static long colH = -9187201950435737472L;
	
	static long kingMoves = 460039L;
	
	static long rowMasks[] = {
			255L, 65280L, 16711680L, 4278190080L, 1095216660480L, 280375465082880L, 71776119061217280L, -72057594037927936L
	};
	
	static long colMasks[] = {
			72340172838076673L, 144680345676153346L, 289360691352306692L, 578721382704613384L,
			1157442765409226768L, 2314885530818453536L, 4629771061636907072L, -9187201950435737472L
	};
	
	static long bltrMasks[] = {
		0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
		0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
		0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
	};
	    static long tlbrMasks[] = {
		0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
		0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
		0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
	};
	
	WhitePawn pawnW = new WhitePawn();
	BlackPawn pawnB = new BlackPawn();
	
	WhiteKnight knightW = new WhiteKnight();
	BlackKnight knightB = new BlackKnight();
	
	WhiteBishop bishopW = new WhiteBishop();
	BlackBishop bishopB = new BlackBishop();
	
	WhiteRook rookW = new WhiteRook();
	BlackRook rookB = new BlackRook();
	
	WhiteQueen queenW = new WhiteQueen();
	BlackQueen queenB = new BlackQueen();
	
	WhiteKing kingW = new WhiteKing();
	BlackKing kingB = new BlackKing();
	
	public Board() {                                                   //initializes all of the bitboards
		
		this.whiteKing = 0;
		this.whiteQueens = 0;
		this.whiteRooks = 0;
		this.whiteBishops = 0;
		this.whiteKnights = 0;
		this.whitePawns = 0;
		this.whitePieces = 0;
		
		this.blackKing = 0;
		this.blackQueens = 0;
		this.blackRooks = 0;
		this.blackBishops = 0;
		this.blackKnights = 0;
		this.blackPawns = 0;
		this.blackPieces = 0;
		
		this.pinnersW = 0;
		this.pinnedB = 0;
		this.interfereB = 0;
		this.pinnersB = 0;
		this.pinnedW = 0;
		this.interfereW = 0;
		
		this.notWhite = 0;
		this.notBlack = 0;
		this.empty = 0;
		this.whiteThreaten = 0;
		this.blackThreaten = 0;
		this.wGetAllPM = 0;
		this.bGetAllPM = 0;
		this.wKThreats = 0;
		this.bKThreats = 0;
		this.whiteCheck = 0;
		this.blackCheck = 0;
		
		//currentState();
	}
	
	public void standardChess() {                                           //sets up the positions of a standard chess game
		
		String standardChessBoard[][] = {
				{"br","bk","bb","bq","bK","bb","bk","br"},
				{"bp","bp","bp","bp","bp","bp","bp","bp"},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"wp","wp","wp","wp","wp","wp","wp","wp"},
				{"wr","wk","wb","wK","wq","wb","wk","wr"}};
		
		arrayToBitboards(standardChessBoard);
	}
	
	public void arrayToBitboards(String[][] chessBoard) {                   //turns an array representation of a chessboard into bitboards for each piece
		
        String Binary;
        for (int i = 0; i < 64; i++) {
            Binary="0000000000000000000000000000000000000000000000000000000000000000";
            Binary=Binary.substring(i+1)+"1"+Binary.substring(0, i);
            switch (chessBoard[i/8][i%8]) {
                case "wp": this.whitePawns += convertStringToBitboard(Binary);
                    break;
                case "wk": this.whiteKnights += convertStringToBitboard(Binary);
                    break;
                case "wb": this.whiteBishops += convertStringToBitboard(Binary);
                    break;
                case "wr": this.whiteRooks += convertStringToBitboard(Binary);
                    break;
                case "wq": this.whiteQueens += convertStringToBitboard(Binary);
                    break;
                case "wK": this.whiteKing += convertStringToBitboard(Binary);
                    break;
                case "bp": this.blackPawns += convertStringToBitboard(Binary);
                    break;
                case "bk": this.blackKnights += convertStringToBitboard(Binary);
                    break;
                case "bb": this.blackBishops += convertStringToBitboard(Binary);
                    break;
                case "br": this.blackRooks += convertStringToBitboard(Binary);
                    break;
                case "bq": this.blackQueens += convertStringToBitboard(Binary);
                    break;
                case "bK": this.blackKing += convertStringToBitboard(Binary);
                    break;
            }
        }
        
        currentState();
    }
	
	public long convertStringToBitboard(String Binary) {                    //converts the string of binary numbers into actual binary fit for bitboards
		
        if (Binary.charAt(0)=='0') {                                        //not negative
            return Long.parseLong(Binary, 2);
        } else {
            return Long.parseLong("1"+Binary.substring(2), 2)*2;
        }
    }
	
	public void currentState() {
		
		whiteCheck = 0;
		blackCheck = 0;
		promoteWhite = 0;
		
		this.whitePieces = whiteQueens|whiteRooks|whiteKnights|whiteBishops|whitePawns|whiteKing;
		this.blackPieces = blackQueens|blackRooks|blackKnights|blackBishops|blackPawns|blackKing;
		
		this.notWhite = ~(whitePieces);
		this.notBlack = ~(blackPieces);
		
		this.empty = ~(whitePieces|blackPieces|whiteKing|blackKing);
		
		getThreaten();
		
		checkmate();
		
		//code for making a bitboard of all of the pinnedB and pinning pieces
		getpinned();
		
		kingB.slideThreats(this);
		kingW.slideThreats(this);
		
		if((this.whitePawns&row8) != 0) {
			pawnPromote.pawnPromotion(0, this);
		}
		if((this.blackPawns&row1) != 0) {

		}
	}
	
	public void getpinned() {
		
		pinnedB = 0L;
		pinnersW = 0L;
		interfereB = 0L;
		pinnedW = 0L;
		pinnersB = 0L;
		interfereW = 0L;
		long coord = 0L;
		long temp = 0L;
		int pinPos = 0;

		pinnersW |= kingB.xrayHV(this, this.blackPieces) & (this.whiteRooks | this.whiteQueens);
		pinnersW |= kingB.xrayD(this, this.blackPieces) & (this.whiteBishops | this.whiteQueens);
		pinnersB |= kingW.xrayHV(this, this.whitePieces) & (this.blackRooks | this.blackQueens);
		pinnersB |= kingW.xrayD(this, this.whitePieces) & (this.blackBishops | this.blackQueens);
		
		temp = pinnersW;
		
		while(temp != 0) {
			
			pinPos = Long.numberOfTrailingZeros(temp);
			coord = 1L<<pinPos;
			
			pinnedB |= obstruct(coord, this.blackKing) & (this.blackPieces&~this.blackKing);
			
			temp &= temp - 1;
		}

		temp = pinnersB;
		
		while(temp != 0) {
			
			pinPos = Long.numberOfTrailingZeros(temp);
			coord = 1L<<pinPos;
			
			pinnedW |= obstruct(coord, this.whiteKing) & (this.whitePieces&~this.whiteKing);
			
			temp &= temp - 1;
		}
		
	}
	
	public long pinMove(long pin, long pinners, long protect) {
		
		long pinPos = 0L;
		long coord = 0L;
		
		while(pinners != 0) {
			
			pinPos = Long.numberOfTrailingZeros(pinners);
			coord = 1L<<pinPos;
			
			if((pin & obstruct(coord, protect)) != 0) {
				return obstruct(coord, protect);
			}
			
			pinners &= pinners - 1;
		}
		return 0L;
	}
	
	public long obstruct(long coord1, long coord2) {
		
		if(coord1 > coord2) {
			long temp = coord1;
			coord1 = coord2;
			coord2 = temp;
		}
		long coords = coord1 | coord2;
		
		int trail1 = Long.numberOfTrailingZeros(coord1);
		int trail2 = Long.numberOfTrailingZeros(coord2);
		
		if((trail1 / 8) == (trail2 / 8)) {			//horizontal
			return (coord2 - coord1) & rowMasks[trail1 / 8] | coords;
		}
		else if((trail1 % 8) == (trail2 % 8)) {		//vertical
			return (coord2 - coord1) & colMasks[trail1 % 8] | coords;
		}
		else if((trail1 / 8) + (trail1 % 8) == (trail2 / 8) + (trail2 % 8)) {		//bottom left to top right
			return (coord2 - coord1) & Board.bltrMasks[(trail1 / 8) + (trail1 % 8)] | coords;
		}
		else if((trail1 / 8) + 7 - (trail1 % 8) == (trail2 / 8) + 7 - (trail2 % 8)) {		//top left to bottom right
			return (coord2 - coord1) & Board.tlbrMasks[(trail1 / 8) + 7 - (trail1 % 8)] | coords;
		}
		
		
		return 0L;
	}

	
	public void checkmate() {
		wGetAllPM = 
				pawnW.getAllPM(this)|knightW.getAllPM(this)|bishopW.getAllPM(this)|
				rookW.getAllPM(this)|queenW.getAllPM(this)|kingW.getAllPM(this);
		
		if(wGetAllPM == 0) {
			teamWon = 1;
		}
		
		bGetAllPM = 
				pawnB.getAllPM(this)|knightB.getAllPM(this)|bishopB.getAllPM(this)|
				rookB.getAllPM(this)|queenB.getAllPM(this)|kingB.getAllPM(this);
		
		if(bGetAllPM == 0) {
			teamWon = 0;
		}
	}
	
	public void getThreaten() {
		
		whiteThreaten = 
				pawnW.threaten(this)|knightW.threaten(this)|bishopW.threaten(this)|
				rookW.threaten(this)|queenW.threaten(this)|kingW.threaten(this);
		
		if((whiteThreaten&this.blackKing) != 0) {
			bKThreats = 0;
			if((this.blackKing&pawnW.threaten(this)) != 0) {
				bKThreats |= pawnW.threatPos(this, this.blackKing);
			}
			if((this.blackKing&knightW.threaten(this)) != 0) {
				bKThreats |= knightW.threatPos(this, this.blackKing);
			}
			if((this.blackKing&bishopW.threaten(this)) != 0) {
				bKThreats |= bishopW.threatPos(this, this.blackKing);
			}
			if((this.blackKing&rookW.threaten(this)) != 0) {
				bKThreats |= rookW.threatPos(this, this.blackKing);
			}
			if((this.blackKing&queenW.threaten(this)) != 0) {
				bKThreats |= queenW.threatPos(this, this.blackKing);
			}
			if((this.blackKing&kingW.threaten(this)) != 0) {
				bKThreats |= kingW.threatPos(this, this.blackKing);
			}
		}
		else
			blackCheck = 0;
		
		blackThreaten = 
				pawnB.threaten(this)|knightB.threaten(this)|bishopB.threaten(this)|
				rookB.threaten(this)|queenB.threaten(this)|kingB.threaten(this);

		if((blackThreaten&this.whiteKing) != 0) {
			wKThreats = 0;
			if((this.whiteKing&pawnB.threaten(this)) != 0) {
				wKThreats |= pawnB.threatPos(this, this.whiteKing);
			}
			if((this.whiteKing&knightB.threaten(this)) != 0) {
				wKThreats |= knightB.threatPos(this, this.whiteKing);
			}
			if((this.whiteKing&bishopB.threaten(this)) != 0) {
				wKThreats |= bishopB.threatPos(this, this.whiteKing);
			}
			if((this.whiteKing&rookB.threaten(this)) != 0) {
				wKThreats |= rookB.threatPos(this, this.whiteKing);
			}
			if((this.whiteKing&queenB.threaten(this)) != 0) {
				wKThreats |= queenB.threatPos(this, this.whiteKing);
			}
			if((this.whiteKing&kingB.threaten(this)) != 0) {
				wKThreats |= kingB.threatPos(this, this.whiteKing);
			}
		}
		else
			whiteCheck = 0;
	}
	
	public void displayArray() {
		
		String displayChessBoard[][] = {
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "}};
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				//white pieces
				if(((this.whiteKing>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wK";
				else if(((this.whiteQueens>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wq";
				else if(((this.whiteRooks>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wr";
				else if(((this.whiteKnights>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wk";
				else if(((this.whiteBishops>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wb";
				else if(((this.whitePawns>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wp";
				
				//black pieces
				else if(((this.blackKing>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bK";
				else if(((this.blackQueens>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bq";
				else if(((this.blackRooks>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "br";
				else if(((this.blackKnights>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bk";
				else if(((this.blackBishops>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bb";
				else if(((this.blackPawns>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bp";
			}
		}
		
		int rows = 9;
		
		for(int i = 0; i < 8; i++) {
			System.out.print(--rows + " ");
			for(int j = 0; j < 8; j++) {
				System.out.print("[" + displayChessBoard[i][j] + "]");
			}
			System.out.println();
		}
		System.out.println("   a   b   c   d   e   f   g   h");
		System.out.println();
	}
	
public void displayArray(long bitboard) {
		
		String displayChessBoard[][] = {
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"  ","  ","  ","  ","  ","  ","  ","  "}};
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				//white pieces
				if(((this.whiteKing>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wK";
				else if(((this.whiteQueens>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wq";
				else if(((this.whiteRooks>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wr";
				else if(((this.whiteKnights>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wk";
				else if(((this.whiteBishops>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wb";
				else if(((this.whitePawns>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "wp";
				
				//black pieces
				else if(((this.blackKing>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bK";
				else if(((this.blackQueens>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bq";
				else if(((this.blackRooks>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "br";
				else if(((this.blackKnights>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bk";
				else if(((this.blackBishops>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bb";
				else if(((this.blackPawns>>(i*8)+j)&1) == 1)
					displayChessBoard[i][j] = "bp";
			}
		}
		
		int rows = 9;
		
		for(int i = 0; i < 8; i++) {
			System.out.print(--rows + " ");
			for(int j = 0; j < 8; j++) {
				if(((bitboard>>(i*8)+j)&1) == 0)
					System.out.print("[" + displayChessBoard[i][j] + "]");
				else
					System.out.print("<" + displayChessBoard[i][j] + ">");
			}
			System.out.println();
		}
		System.out.println("   a   b   c   d   e   f   g   h");
		System.out.println();
	}
	
	public void removePiece(long coord) {
		if((this.whitePieces&coord) != 0) {
			if((this.whitePawns&coord) != 0) {
				this.whitePawns = this.whitePawns^coord;
			}
			else if((this.whiteBishops&coord) != 0) {
				this.whiteBishops = this.whiteBishops^coord;
			}
			else if((this.whiteKnights&coord) != 0) {
				this.whiteKnights = this.whiteKnights^coord;
			}
			else if((this.whiteRooks&coord) != 0) {
				this.whiteRooks = this.whiteRooks^coord;
			}
			else if((this.whiteQueens&coord) != 0) {
				this.whiteQueens = this.whiteQueens^coord;
			}
		}
		else if((this.blackPieces&coord) != 0) {
			if((this.blackPawns&coord) != 0) {
				this.blackPawns = this.blackPawns^coord;
			}
			else if((this.blackBishops&coord) != 0) {
				this.blackBishops = this.blackBishops^coord;
			}
			else if((this.blackKnights&coord) != 0) {
				this.blackKnights = this.blackKnights^coord;
			}
			else if((this.blackRooks&coord) != 0) {
				this.blackRooks = this.blackRooks^coord;
			}
			else if((this.blackQueens&coord) != 0) {
				this.blackQueens = this.blackQueens^coord;
			}
		}
	}
	
	public void displayBitboard(long bitBoard) {
		
		String display = Long.toBinaryString(bitBoard);
		
		while(display.length() <= 64) {
			display = "0" + display;
		}
		
		for(int i = 7; i >= 0; i--) {
			for(int j = 8; j > 0; j--) {
				System.out.print(display.charAt(8*i+j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static long convertToCoord(String pos) {
		
		int x = pos.charAt(0) - 97;
		int y = 8 - Integer.parseInt(pos.substring(1,2));
		long coord = 1L<<((y*8)+x);
		
		return coord;
	}
	
	public static long convertToCoord(int x, int y) {

		long coord = 1L<<((y*8)+x);
		
		return coord;
	}
	
	public boolean makeMove(int team, String pos, boolean checked) {
		
		long coord1 = convertToCoord(pos.substring(0,2));
		long coord2 = convertToCoord(pos.substring(2,4));
		
		boolean valid = false;
		
		if(team == 0 && (coord1&(this.whitePieces|this.whiteKing)) != 0) {
			if((coord1&this.whitePawns) != 0) {
				valid = pawnW.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.whiteKnights) != 0) {
				valid = knightW.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.whiteBishops) != 0) {
				valid = bishopW.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.whiteRooks) != 0) {
				valid = rookW.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.whiteQueens) != 0) {
				valid = queenW.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.whiteKing) != 0) {
				valid = kingW.movePiece(this, coord1, coord2, checked);
			}
		}
		else if(team == 1 && (coord1&(this.blackPieces|this.blackKing)) != 0) {
			if((coord1&this.blackPawns) != 0) {
				valid = pawnB.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.blackKnights) != 0) {
				valid = knightB.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.blackBishops) != 0) {
				valid = bishopB.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.blackRooks) != 0) {
				valid = rookB.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.blackQueens) != 0) {
				valid = queenB.movePiece(this, coord1, coord2, checked);
			}
			else if((coord1&this.blackKing) != 0) {
				valid = kingB.movePiece(this, coord1, coord2, checked);
			}
		}
		if(valid)
			currentState();
		
		return valid;
	}
	
	public long showMoves(String pos) {
		
		long coord = convertToCoord(pos);
		long moves = 0L;
		
		if((coord&(this.whitePieces|this.whiteKing)) != 0) {
			if((coord&this.whitePawns) != 0) {
				moves = pawnW.possibleMoves(this, coord);
			}
			else if((coord&this.whiteKnights) != 0) {
				moves = knightW.possibleMoves(this, coord);
			}
			else if((coord&this.whiteBishops) != 0) {
				moves = bishopW.possibleMoves(this, coord);
			}
			else if((coord&this.whiteRooks) != 0) {
				moves = rookW.possibleMoves(this, coord);
			}
			else if((coord&this.whiteQueens) != 0) {
				moves = queenW.possibleMoves(this, coord);
			}
			else if((coord&this.whiteKing) != 0) {
				moves = kingW.possibleMoves(this, coord);
			}
		}
		else if((coord&(this.blackPieces|this.blackKing)) != 0) {
			if((coord&this.blackPawns) != 0) {
				moves = pawnB.possibleMoves(this, coord);
			}
			else if((coord&this.blackKnights) != 0) {
				moves = knightB.possibleMoves(this, coord);
			}
			else if((coord&this.blackBishops) != 0) {
				moves = bishopB.possibleMoves(this, coord);
			}
			else if((coord&this.blackRooks) != 0) {
				moves = rookB.possibleMoves(this, coord);
			}
			else if((coord&this.blackQueens) != 0) {
				moves = queenB.possibleMoves(this, coord);
			}
			else if((coord&this.blackKing) != 0) {
				moves = kingB.possibleMoves(this, coord);
			}
		}
		
		return moves;
	}
	
	public long showAllMoves(String type) {
		
		long moves = 0L;
		
		switch(type) {
		case "wA":
			moves |= pawnW.getAllPM(this);
			moves |= knightW.getAllPM(this);
			moves |= bishopW.getAllPM(this);
			moves |= rookW.getAllPM(this);
			moves |= queenW.getAllPM(this);
			moves |= kingW.getAllPM(this);
			break;
		case "wp":
			moves = pawnW.getAllPM(this);
			break;
		case "wk":
			moves = knightW.getAllPM(this);
			break;
		case "wb":
			moves = bishopW.getAllPM(this);
			break;
		case "wr":
			moves = rookW.getAllPM(this);
			break;
		case "wq":
			moves = queenW.getAllPM(this);
			break;
		case "wK":
			moves = kingW.getAllPM(this);
			break;
			
		case "bA":
			moves |= pawnB.getAllPM(this);
			moves |= knightB.getAllPM(this);
			moves |= bishopB.getAllPM(this);
			moves |= rookB.getAllPM(this);
			moves |= queenB.getAllPM(this);
			moves |= kingB.getAllPM(this);
			break;
		case "bp":
			moves = pawnB.getAllPM(this);
			break;
		case "bk":
			moves = knightB.getAllPM(this);
			break;
		case "bb":
			moves = bishopB.getAllPM(this);
			break;
		case "br":
			moves = rookB.getAllPM(this);
			break;
		case "bq":
			moves = queenB.getAllPM(this);
			break;
		case "bK":
			moves = kingB.getAllPM(this);
			break;
		default:
			System.out.println("Invalid syntax");
		}
		
		return moves;
	}
	
	/*public void pawnPromotion(int team, ChessGui gui) {
		
		long coord = 0L;
		int p = 0;
		int newP = 4;
		long promote = 0L;
		
		if(team == 0) {
			coord = 0L;
			p = 0;
			newP = 1;
			promote = this.whitePawns&row8;
			
			//while(promote != 0) {
				newP = gui.showPawnPromotion();
				p = Long.numberOfTrailingZeros(promote);
				coord = 1L<<p;
				promote &= ~coord;
				
				switch(newP) {
				  case 3: //knight
					this.removePiece(coord);
					this.whiteKnights |= coord;
				    break;
				  case 4: //bishop
					this.removePiece(coord);
					this.whiteBishops |= coord;
				    break;
				  case 2: //rook
					this.removePiece(coord);
					this.whiteRooks |= coord;
				    break;
				  case 1: //queen
					this.removePiece(coord);
					this.whiteQueens |= coord;
				    break;
				  default:
				    System.out.println("error");
				}
			}
		//}	
		/*else if(team == 1) {
			coord = 0L;
			p = 0;
			newP = 4;
			promote = this.blackPawns&row1;
			
			while(promote != 0) {
				//newP = GUI
				p = Long.numberOfTrailingZeros(promote);
				coord = 1L<<p;
				promote &= ~coord;
				
				switch(newP) {
				  case 1: //knight
					this.removePiece(coord);
					this.blackKnights |= coord;
				    break;
				  case 2: //bishop
					this.removePiece(coord);
					this.blackBishops |= coord;
				    break;
				  case 3: //rook
					this.removePiece(coord);
					this.blackRooks |= coord;
				    break;
				  case 4: //queen
					this.removePiece(coord);
					this.blackQueens |= coord;
				    break;
				  default:
				    System.out.println("error");
				}
			}
		}
	}*/
}