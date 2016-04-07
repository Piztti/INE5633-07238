package br.ufsc.inf.sin.game;

import java.util.List;

public class Game {
    Board initialBoard = new Board();
    Board finalBoard = new Board(1, 2, 3, 4, 0, 5, 6, 7, 8);
    Rules rules = new Rules(finalBoard);
    Heuristic heuristic = new Heuristic(rules);
    Boolean solved = false;

    public Game() throws Exception {
    }

    public void playDefault8Game() throws Exception {
        finalBoard = new Board(1, 2, 3, 4, 0, 5, 6, 7, 8);
        updateRulesHeuristics();
        initialBoard = scramble(finalBoard, 10, 40);
        solved = false;

        printInitialBoard();

        printFinalBoard();

        printNextStates(initialBoard);

        solve();

        if(solved){
            printStatistics();
        }
    }

    public void playDefault15Game() throws Exception {
        finalBoard = new Board(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0);
        updateRulesHeuristics();
        initialBoard = scramble(finalBoard, 10, 40);
        solved = false;

        printInitialBoard();

        printFinalBoard();

        printNextStates(initialBoard);

        solve();

        if(solved){
            printStatistics();
        }
    }

    public void setFinalBoard(Integer... pieces) throws Exception {
        finalBoard = new Board(pieces);
        updateRulesHeuristics();
    }

    public void setFinalBoard(String pieces) throws Exception {
        finalBoard = new Board(pieces);
        updateRulesHeuristics();
    }

    public void setInitialAsFinalScrambledBoard(Integer minScrambles, Integer maxScrambles) throws Exception {
        initialBoard = scramble(finalBoard, minScrambles,maxScrambles);
    }

    public void setInitialBoard(Integer... pieces) throws Exception {
        initialBoard = new Board(pieces);
    }

    public void setInitialBoard(String pieces) throws Exception {
        initialBoard = new Board(pieces);
    }

    public void printInitialBoard(){
        println("Tabuleiro inicial:");
        if(!initialBoard.toString().isEmpty()) {
            println(initialBoard.toString());
            println("Custo: " + heuristic.boardCost(initialBoard));
            println("");
        }
    }

    public void printFinalBoard(){
        println("Tabuleiro final:");
        if(!finalBoard.toString().isEmpty()) {
            println(finalBoard.toString());
            println("Custo: " + heuristic.boardCost(finalBoard));
            println("");
        }
    }

    public void printNextStates(Board board) throws Exception {
        println("Exemplo de possíveis expansões do estado: ");
        println(board.toString());
        println("Custo: " + heuristic.boardCost(board));
        println("");
        List<Board> validMoves = rules.validMoves(board);
        validMoves.forEach(board1 -> {
            println(board);
            println("Custo: " + heuristic.boardCost(board1));
            println("");
        });
    }

    public void printStatistics(){
        println(heuristic.getReport());
    }

    private void updateRulesHeuristics() throws Exception {
        rules = new Rules(finalBoard);
        heuristic = new Heuristic(rules);
    }

    public Boolean solve() throws Exception {
        solved = heuristic.solve(initialBoard);
        return solved;
    }

    private Board scramble(Board board, Integer min, Integer max) throws Exception {
        if(min>max){
            throw new Exception("O número máximo de embaralhamentos deve ser maior que o mínimo");
        }

        Integer maxScramble = ((Double)((Math.random()*(max-min))+min)).intValue();

        Board scrambled = board;
        for (int i = maxScramble; i>=0; i--){
            scrambled = rules.validMoves(scrambled)
                    .stream()
                    .max((o1, o2) ->
                            heuristic.boardCost(o1).compareTo(heuristic.boardCost(o2)))
                    .get();
        }

        return scrambled;
    }

    private void println(Object msg){
        System.out.println(msg.toString());
    }

    private void print(Object msg){
        System.out.print(msg.toString());
    }

}
