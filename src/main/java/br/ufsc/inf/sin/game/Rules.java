package br.ufsc.inf.sin.game;

import java.util.ArrayList;
import java.util.List;

public class Rules {

    static final Integer blankPiece = 0;
    private Integer boardSize;
    private Integer maxXY;
    private Board finalPosition;

    private List<Position> positions;

    public Rules(Board finalPosition) throws Exception {

        boardSize = finalPosition.getBoard().size();
        maxXY = ((Double)Math.sqrt(boardSize)).intValue()-1;
        layoutPositions(finalPosition);

        this.finalPosition = finalPosition;
    }

    private void layoutPositions(Board finalPosition) throws Exception {
        Integer targetSize = finalPosition.getBoard().size();
        Double sizeSides = Math.sqrt(finalPosition.getBoard().size());
        Integer nLines = sizeSides.intValue();

        if(targetSize >= boardSize &&
                sizeSides/sizeSides.intValue() == 1){
            Integer currentLine = 0;
            positions = new ArrayList<>(targetSize);
            for (int i = 0; i<finalPosition.getBoard().size(); i++){
                positions.add(new Position(currentLine, i%nLines));
                if((i+1)%nLines == 0){
                    currentLine++;
                }
            }
        }else{
            throw new Exception("Preciso de um tabuleiro quadrado!");
        }
    }

    public List<Board> validMoves(Board board) throws Exception {
        List<Board> validMoves = new ArrayList<>();
        Integer boardBlankIndex = board.getBoard().indexOf(blankPiece);
        Position blankPosition = positions.get(boardBlankIndex);

        if(blankPosition.getX() + 1 <= maxXY){
            validMoves.add(swap(1, 0, blankPosition, boardBlankIndex, board));
        }

        if(blankPosition.getX() - 1 >= 0){
            validMoves.add(swap(-1, 0, blankPosition, boardBlankIndex, board));
        }

        if(blankPosition.getY() + 1 <= maxXY){
            validMoves.add(swap(0, 1, blankPosition, boardBlankIndex, board));
        }

        if(blankPosition.getY() - 1 >= 0){
            validMoves.add(swap(0, -1, blankPosition, boardBlankIndex, board));
        }

        return validMoves;
    }

    private Board swap(Integer x, Integer y, Position blankPosition, Integer boardBlankIndex, Board board) throws Exception {
        Integer[] targetBoard = new Integer[board.getBoard().size()];
        targetBoard = board.getBoard().toArray(targetBoard);

        Integer targetBlankIndex = positions.indexOf(new Position(blankPosition.getX()+x, blankPosition.getY()+y));

        Integer p1 = targetBoard[boardBlankIndex];
        Integer p2 = targetBoard[targetBlankIndex];


        targetBoard[boardBlankIndex] = p2;
        targetBoard[targetBlankIndex] = p1;

        return new Board(targetBoard);
    }

    public List<Position> boardCoordinates(){
        return positions;
    }

    public Board getFinalPosition() {
        return finalPosition;
    }
}
