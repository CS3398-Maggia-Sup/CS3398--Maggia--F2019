//Roy Grady
package Engine;

public class Check {														//special check restrictions and pin calculations
	
	static public void addCheck(Board board, int team) {			//increment check of a team
		
		if(!(board.pieceList.get(team).containsKey('P') && board.pieceList.get(team).get('P').piece != 0))
			board.check[team]++;
	}
	
	static public long checkRestrict(Board board, int team, long coord, long pinned) { 
																	//restricts piece movement when team king has one check
		long interfere, kingThreats;

		interfere = board.interfereBB[team];
		kingThreats = board.kThreatsBB[team];
		
		if((coord & pinned) != 0)
			return 0L;
		else
			return interfere|kingThreats;
	}
	
	static public long pinRestrict(Board board, long coord, int team) {		//method for a piece to call if pinned
		
		long pinners, protect;

		pinners = board.pinnersBB[Math.abs(team-1)];
		protect = board.kingBB[team];
		
		return pinMove(coord, pinners, protect);
	}
	
	static public long pinMove(long pin, long pinners, long protect) {		//calculates the moves a pinned piece can make
		
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
	
	static public long obstruct(long coord1, long coord2) {					//gets the cardinal/ordinal bits between two coordinates 
		
		if(coord1-1 > coord2-1) {
			long temp = coord1;
			coord1 = coord2;
			coord2 = temp;
		}
		long coords = coord1 | coord2;
		
		int trail1 = Long.numberOfTrailingZeros(coord1);
		int trail2 = Long.numberOfTrailingZeros(coord2);
		
		if((trail1 / 8) == (trail2 / 8)) {													//horizontal
			return (coord2 - coord1) & Board.rowMasks[trail1 / 8] | coords;
		}
		else if((trail1 % 8) == (trail2 % 8)) {												//vertical
			return (coord2 - coord1) & Board.colMasks[trail1 % 8] | coords;
		}
		else if((trail1 / 8) + (trail1 % 8) == (trail2 / 8) + (trail2 % 8)) {				//bottom left to top right
			return (coord2 - coord1) & Board.bltrMasks[(trail1 / 8) + (trail1 % 8)] | coords;
		}
		else if((trail1 / 8) + 7 - (trail1 % 8) == (trail2 / 8) + 7 - (trail2 % 8)) {		//top left to bottom right
			return (coord2 - coord1) & Board.tlbrMasks[(trail1 / 8) + 7 - (trail1 % 8)] | coords;
		}
		
		
		return 0L;
	}
	
	static public void slideThreats(Board board, int team) {                     //computes sliding pieces that threaten the king so that the king can't move backwards parallel to the pieces
		
		long king = 0;
		long cardinals = 0;
		long ordinals = 0;
		int notTeam = Math.abs(team-1);
		
		king = board.kingBB[team];
		cardinals = board.cardinalsBB[notTeam];
		ordinals = board.ordinalsBB[notTeam];
		
		int trail = Long.numberOfTrailingZeros(king);
		long occ = ~board.empty;
		long threats = 0L;
		long temp = 0L;
		long coord = 0L;
		
		long horizontal = Moves.horizontal(occ, king);
		long vertical = Moves.vertical(occ, king);
		long bltr = Moves.diagonalBLTR(occ, king);
		long tlbr = Moves.diagonalTLBR(occ, king);
		
        if((horizontal&cardinals) != 0) {
        	threats |= Board.rowMasks[trail / 8];
        	temp |= horizontal&cardinals;
        }
        if((vertical&cardinals) != 0) {
        	threats |= Board.colMasks[trail % 8];
        	temp |= vertical&cardinals;
        }
        if((bltr&ordinals) != 0) {
        	threats |= Board.bltrMasks[(trail / 8) + (trail % 8)];
        	temp |= bltr&ordinals;
        }
        if((tlbr&ordinals) != 0) {
        	threats |= Board.tlbrMasks[(trail / 8) + 7 - (trail % 8)];
        	temp |= tlbr&ordinals;
        }
        
        long interfere = 0L;
        
        if(board.pieceList.get(notTeam).get('a') != null) {
        	long archers = board.pieceList.get(notTeam).get('a').piece;
        	if((((king>>8|king<<8|king>>16|king<<16) & vertical) & archers) != 0)
        		threats |= Board.colMasks[trail % 8] & ~(archers>>8|archers<<8|archers);
        	if((king>>1 & archers) != 0)
        		threats |= king<<1;
        	if((king<<1 & archers) != 0)
        		threats |= king>>1;
			temp |= archers & Moves.archerPseudoCaptures(king) & (horizontal|vertical);
        }
        
        while(temp != 0) {
        	trail = Long.numberOfTrailingZeros(temp);
        	coord = 1L<<trail;
        	
        	interfere |= Check.obstruct(coord, king);
        	
        	temp &= temp - 1;
        }
        interfere &= ~king;
        
        if(board.check[team] < 2)
        threats &= ~((cardinals|ordinals)&board.kThreatsBB[team]);
        
		board.interfereBB[team] = interfere;
		board.slideThreatsBB[team] = threats;
		
	}
	
	static public void slideThreats(Board board) {
		slideThreats(board, 0);
		slideThreats(board, 1);
	}
	
	static public void getPinned(Board board) {
		
		int noti;
		long xrayHV, xrayDX;
		for(int i = 0; i < board.teamNum; i++) {
			noti = Math.abs(i-1);
			board.pinnedBB[i] = 0L;
			
			xrayHV = Moves.xrayHV(~board.empty, board.kingBB[noti], board.teamBB[noti]);
			xrayDX = Moves.xrayDX(~board.empty, board.kingBB[noti], board.teamBB[noti]);
			
			board.pinnersBB[i] = xrayHV & board.cardinalsBB[i] | xrayDX & board.ordinalsBB[i];
			if(board.pieceList.get(i).get('a') != null)
				board.pinnersBB[i] |= xrayHV & board.pieceList.get(i).get('a').piece & Moves.archerPseudoCaptures(board.kingBB[noti]);
			board.interfereBB[i] = 0L;
		}

		long coord = 0L;
		long temp = 0L;
		int pinPos = 0;
		
		for(int i = 0; i < board.teamNum; i++) {
			noti = Math.abs(i-1);
			
			temp = board.pinnersBB[i];
			while(temp != 0) {
				
				pinPos = Long.numberOfTrailingZeros(temp);
				coord = 1L<<pinPos;
				
				board.pinnedBB[noti] |= Check.obstruct(coord, board.kingBB[noti]) & (board.teamBB[noti]&~board.kingBB[noti]);
				
				temp &= temp - 1;
			}
		}
	}
	
}