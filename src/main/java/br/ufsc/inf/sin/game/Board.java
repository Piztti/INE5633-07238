package br.ufsc.inf.sin.game;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Integer> board = new ArrayList<>();

    public Board(Integer... pieces) throws Exception {
        addFromArray(pieces);
    }

    public Board(String pieces) throws Exception {
        String[] piecesString = pieces.split(",");
        Integer[] piecesInteger = new Integer[piecesString.length];
        for (int i = piecesString.length-1; i>=0; i--){
            piecesInteger[i] = Integer.parseInt(piecesString[i]);
        }
        addFromArray(piecesInteger);
    }

    private void addFromArray(Integer[] pieces) throws Exception {
        for (int i = 0; i<pieces.length; i++){
            if(board.contains(pieces[i])){
                throw new Exception("Preciso de peças diferentes");
            }
            board.add(pieces[i]);
        }
    }

    public List<Integer> getBoard(){
        return this.board;
    }

    @Override
    public String toString(){
        Integer nLines = ((Double) Math.sqrt(board.size())).intValue();
        String outString = "";
        Integer lastIndex = 0;
        for (int i = 0; i<board.size(); i++){
            if((i+1)%nLines == 0){
                outString = outString + board.subList(lastIndex, i+1).toString();
                lastIndex = i+1;
                if(lastIndex<board.size()){
                    outString = outString + System.lineSeparator();
                }
            }
        }
        return outString;
    }

    @Override
    public boolean equals(Object board){
        return this.getBoard().equals(((Board)board).getBoard());
    }

    @Override
    public int hashCode(){
        return this.getBoard().hashCode();
    }

}
