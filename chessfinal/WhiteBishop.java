//Roy Grady, bishop

package chessfinal;

public class WhiteBishop implements Piece
{
	
	private long moves;
	
	public long possibleMoves(Board board, long coord) {
		
		moves = 0L;
		
		if((board.whiteBishops&coord) != 0) {	
			int trail = Long.numberOfTrailingZeros(coord);
			
			long bltr = ((~board.empty&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * coord)) ^ Long.reverse(Long.reverse(~board.empty&Board.bltrMasks[(trail / 8) + (trail % 8)]) - (2 * Long.reverse(coord)));
	        long tlbr = ((~board.empty&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * coord)) ^ Long.reverse(Long.reverse(~board.empty&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) - (2 * Long.reverse(coord)));
			moves = (bltr&Board.bltrMasks[(trail / 8) + (trail % 8)] | tlbr&Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)]) & board.notWhite;
		}	
	
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
			board.whiteBishops^= change;
			return true;
		}
		return false;
	}
	
	public long getAllPM(Board board) {
		
		long allMoves = 0L;
		long bishops = board.whiteBishops;
		long coord = 0L;
		int b = 0;
		
		while(bishops != 0) {
			b = Long.numberOfTrailingZeros(bishops);
			coord = 1L<<b;
			bishops &= ~coord;
			
			allMoves |= possibleMoves(board, coord);
		}
		
		return allMoves;
	}
}