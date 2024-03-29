package Engine;

import Pieces.*;

public class PawnPromote {
	
	public static boolean promotion = false;
	public static long coord = 0L;
	
	public static void pawnPromotion(Board board) {
		
		int p = 0;
		long promote = 0L;
		if(board.pieceList.get(0).containsKey('p'))
			promote = board.pieceList.get(0).get('p').piece & Board.row8;
		if(board.pieceList.get(1).containsKey('p'))
			promote |= board.pieceList.get(1).get('p').piece & Board.row1;
		p = Long.numberOfTrailingZeros(promote);
		coord = 1L<<p;

		promotion = true;
	}
	
	public static void promotePawn(int team, long coord, Board board, char ID) {
		
		for(Piece piece : board.pieceList.get(team).values()) {
			if(piece.ID == ID) {
				board.removePromote(coord, team, piece.value);
				board.pawnsBB[team] &= ~coord;
				piece.piece |= coord;

				break;
			}
		}
		
		promotion = false;
		if(!board.cpuTurn) {
			board.switchTeamTurn();
			board.currentState();
		}
	}
}