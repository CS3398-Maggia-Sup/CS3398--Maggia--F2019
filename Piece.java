import java.awt.*;
public interface Piece
{
    public long possibleMoves(Board board, long coord);
    public void movePiece(Board board, long coord1, long coord2, boolean checked);
    public long getAllPM(Board board);
}
