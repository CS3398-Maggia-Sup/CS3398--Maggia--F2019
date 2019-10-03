//Roy Grady, black knight
package chessfinal;
public class BlackKnight implements Piece
{
	
	private long moves;
	
	public long possibleMoves(Board board, long coord) {
		
		moves = 0L;
		
		if((board.blackKnights&coord) != 0) {	
			if((coord&(Board.row7|Board.row8)) == 0) {
				if((coord&Board.colA) == 0 && ((coord>>17)&(board.empty|board.whitePieces)) != 0) {     //up left
					moves |= coord>>17;
				}
				if((coord&Board.colH) == 0 && ((coord>>15)&(board.empty|board.whitePieces)) != 0) {     //up right
					moves |= coord>>15;
				}
			}
			if((coord&(Board.row1|Board.row2)) == 0) {
				if((coord&Board.colA) == 0 && ((coord<<15)&(board.empty|board.whitePieces)) != 0) {     //down left
					moves |= coord<<15;
				}
				if((coord&Board.colH) == 0 && ((coord<<17)&(board.empty|board.whitePieces)) != 0) {     //down right
					moves |= coord<<17;
				}
			}
			if((coord&(Board.colA|Board.colB)) == 0) {
				if((coord&Board.row8) == 0 && ((coord>>10)&(board.empty|board.whitePieces)) != 0) {     //left up
					moves |= coord>>10;
				}
				if((coord&Board.row1) == 0 && ((coord<<6)&(board.empty|board.whitePieces)) != 0) {     //left down
					moves |= coord<<6;
				}
			}
			if((coord&(Board.colG|Board.colH)) == 0) {
				if((coord&Board.row8) == 0 && ((coord>>6)&(board.empty|board.whitePieces)) != 0) {     //right up
					moves |= coord>>6;
				}
				if((coord&Board.row1) == 0 && ((coord<<10)&(board.empty|board.whitePieces)) != 0) {     //right down
					moves |= coord<<10;
				}
			}
		}	
	
		return moves;
	}
	
	public boolean movePiece(Board board, long coord1, long coord2, boolean checked) {
		
		if(checked == false)
			moves = possibleMoves(board, coord1);
		
		long change = coord1|coord2;
		
		if((coord2&moves) != 0) {
			if((board.whitePieces&coord2) != 0) {
				board.removePiece(coord2);
			}
			board.blackKnights^= change;
			return true;
		}
		return false;
	}
	
	public long getAllPM(Board board) {
		
		long allMoves = 0L;
		
		allMoves |= (board.blackKnights>>17)&(board.empty|board.whitePieces)&~(Board.row1|Board.row2)&~Board.colH;
		allMoves |= (board.blackKnights>>15)&(board.empty|board.whitePieces)&~(Board.row1|Board.row2)&~Board.colA;
		allMoves |= (board.blackKnights<<15)&(board.empty|board.whitePieces)&~(Board.row7|Board.row8)&~Board.colH;
		allMoves |= (board.blackKnights<<17)&(board.empty|board.whitePieces)&~(Board.row7|Board.row8)&~Board.colA;
		allMoves |= (board.blackKnights>>10)&(board.empty|board.whitePieces)&~(Board.colG|Board.colH)&~Board.row1;
		allMoves |= (board.blackKnights<<6)&(board.empty|board.whitePieces)&~(Board.colG|Board.colH)&~Board.row8;
		allMoves |= (board.blackKnights>>6)&(board.empty|board.whitePieces)&~(Board.colA|Board.colB)&~Board.row1;
		allMoves |= (board.blackKnights<<10)&(board.empty|board.whitePieces)&~(Board.colA|Board.colB)&~Board.row8;
		
		return allMoves;
	}
}
