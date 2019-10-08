//Roy Grady, king
public class WhiteKing implements Piece
{
	
	private long moves;
	private boolean threat = false;
	
	public long possibleMoves(Board board, long coord) {
		moves = 0L;
		
		if((board.whiteKing&coord) != 0) {	
			int trail = Long.numberOfTrailingZeros(coord);
			
			if(trail == 9) {
				moves = Board.kingMoves;
			}
			else {
				if(trail > 9) {
					moves = Board.kingMoves<<(trail - 9);
				}
				else if(trail < 9) {
					moves = Board.kingMoves>>(9 - trail);
				}
				
				if((coord&Board.colA) != 0) {
					moves &= ~Board.colH;
				}
				if((coord&Board.colH) != 0) {
					moves &= ~Board.colA;
				}
			}
			
		}
		
		moves &= ~slideThreats(board);
		
		if(!threat)
			moves &= board.notWhite&~board.blackThreaten;
		
		threat = false;
		
		return moves;
	}
	
	public boolean movePiece(Board board, long coord1, long coord2, boolean checked) {
		
		if(checked == false)
			moves = possibleMoves(board, coord1);
		
		long change = coord1|coord2;
		
		if((coord2&moves) != 0) {
			if((board.blackPieces&coord2) != 0) {
				board.removePiece(coord2);
			}
			board.whiteKing^= change;
			return true;
		}
		return false;
	}
	
	public long getAllPM(Board board) {
		
		long allMoves = 0L;
		long king = board.whiteKing;
		long coord = 0L;
		int k = 0;
		
		while(king != 0) {
			k = Long.numberOfTrailingZeros(king);
			coord = 1L<<k;
			king &= ~coord;
			
			allMoves |= possibleMoves(board, coord);
		}
		
		return allMoves;
	}
	
	public long threaten(Board board) {
		
		long threatened = 0L;
		long king = board.whiteKing;
		long coord = 0L;
		int k = 0;
		
		while(king != 0) {
			threat = true;
			k = Long.numberOfTrailingZeros(king);
			coord = 1L<<k;
			king &= ~coord;
			
			threatened |= possibleMoves(board, coord);
		}
		
		return threatened;
	}
	
	public long threatPos(Board board, long pCoord) {
		
		long tPos = 0L;
		long unit = board.whiteKing;
		long coord = 0L;
		int u = 0;
		
		while(unit != 0) {
			u = Long.numberOfTrailingZeros(unit);
			coord = 1L<<u;
			unit &= ~coord;
			
			if((possibleMoves(board, coord)&pCoord) != 0) {
				tPos |= coord;
				board.blackCheck++;
			}
		}
		
		return tPos;
	}
	
	public long xrayHV(Board board, long blockers) {
		
		int trail = Long.numberOfTrailingZeros(board.whiteKing);
		long occ = ~board.empty;
		
		long horizontal = (occ - board.whiteKing * 2) ^ Long.reverse(Long.reverse(occ) - Long.reverse(board.whiteKing) * 2);
		long vertical = ((occ&Board.colMasks[trail % 8]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.colMasks[trail % 8]) - (2 * Long.reverse(board.whiteKing)));
		long attacks = (horizontal&Board.rowMasks[trail / 8] | vertical&Board.colMasks[trail % 8]);
		blockers &= attacks;
		if(blockers == 0) 
			return blockers;
		
		occ = ~board.empty ^ blockers;
		
		horizontal = (occ - board.whiteKing * 2) ^ Long.reverse(Long.reverse(occ) - Long.reverse(board.whiteKing) * 2);
		vertical = ((occ&Board.colMasks[trail % 8]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.colMasks[trail % 8]) - (2 * Long.reverse(board.whiteKing)));
		attacks ^= (horizontal&Board.rowMasks[trail / 8] | vertical&Board.colMasks[trail % 8]);
		
		return attacks;
		
	}
	
	public long xrayD(Board board, long blockers) {
		
		int trail = Long.numberOfTrailingZeros(board.whiteKing);
		long occ = ~board.empty;
		
		long bltr = ((occ&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * Long.reverse(board.whiteKing)));
        long tlbr = ((occ&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * Long.reverse(board.whiteKing)));
		long attacks = (bltr&Board.bltrMasks[(trail / 8) + (trail % 8)] | tlbr&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]);
		blockers &= attacks;
		if(blockers == 0) 
			return blockers;
		
		occ = ~board.empty ^ blockers;
		
		bltr = ((occ&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * Long.reverse(board.whiteKing)));
        tlbr = ((occ&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * Long.reverse(board.whiteKing)));
		attacks ^= (bltr&Board.bltrMasks[(trail / 8) + (trail % 8)] | tlbr&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]);
		
		return attacks;
		
	}
	
	public long slideThreats(Board board) {                     //computes sliding pieces that threaten the king so that i can't move backwards parallel to it
															    //as well as computes the interference bitboard.
		int trail = Long.numberOfTrailingZeros(board.whiteKing);
		long occ = ~board.empty;
		long threats = 0L;
		long temp = 0L;
		long coord = 0L;
		
		long horizontal = (occ - board.whiteKing * 2) ^ Long.reverse(Long.reverse(occ) - Long.reverse(board.whiteKing) * 2);
		horizontal &= Board.rowMasks[trail / 8];
		
		long vertical = ((occ&Board.colMasks[trail % 8]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.colMasks[trail % 8]) - (2 * Long.reverse(board.whiteKing)));
		vertical &= Board.colMasks[trail % 8];
		
		long bltr = ((occ&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * Long.reverse(board.whiteKing)));
        bltr &= Board.bltrMasks[(trail / 8) + (trail % 8)];
		
		long tlbr = ((occ&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * board.whiteKing)) ^ Long.reverse(Long.reverse(occ&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * Long.reverse(board.whiteKing)));
        tlbr &= Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)];
		
        if((horizontal&(board.blackRooks | board.blackQueens)) != 0) {
        	threats |= Board.rowMasks[trail / 8];
        	temp |= horizontal&(board.blackRooks | board.blackQueens);
        }
        if((vertical&(board.blackRooks | board.blackQueens)) != 0) {
        	threats |= Board.colMasks[trail % 8];
        	temp |= vertical&(board.blackRooks | board.blackQueens);
        }
        if((bltr&(board.blackBishops | board.blackQueens)) != 0) {
        	threats |= Board.bltrMasks[(trail / 8) + (trail % 8)];
        	temp |= bltr&(board.blackBishops | board.blackQueens);
        }
        if((tlbr&(board.blackBishops | board.blackQueens)) != 0) {
        	threats |= Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)];
        	temp |= tlbr&(board.blackBishops | board.blackQueens);
        }
        
        while(temp != 0) {
        	trail = Long.numberOfTrailingZeros(temp);
        	coord = 1L<<trail;
        	
        	board.interfereW |= board.obstruct(coord, board.whiteKing);
        	
        	temp &= temp - 1;
        }
        board.interfereW &= ~board.whiteKing;
        
        threats &= ~(board.blackRooks | board.blackBishops | board.blackQueens);
        
		return threats;
	}
}
