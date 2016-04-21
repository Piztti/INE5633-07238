package br.ufsc.inf.sin.game;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 *
 * Define a heuristica usada no algoritmo A*.
 *
 */
public class Heuristic {

    private final Float playCost = 1f;
    private Rules rules;
    private String report = "";

    /**
     *
     * Monta a heurística usando uma regra.
     *
     * @param rules
     */
    public Heuristic(Rules rules) {
        this.rules = rules;
    }


    /**
     *
     * Resolve o tabuleiro passado usando A*.
     * A estrutura de dados da fronteira é uma {@link PriorityQueue}.
     *
     * @param initialBoard
     * @return
     * @throws Exception
     */
    public Boolean solve(Board initialBoard) throws Exception {

        Instant start = Instant.now();

        Node rootNode = new Node(initialBoard, null, boardCost(initialBoard), 0f);
        Set<Node> closed = new HashSet<>();
        Queue<Node> open = new PriorityQueue<>();

        open.add(rootNode);

        Integer maxOpen = 0;
        Integer plays = 0;
        Integer maxPlays = 80;
        Integer maxIterations = 100000;
        Integer currentIteration = 0;
        Boolean found = false;
        Node finalNode = null;

        while (open.size() > 0 && currentIteration < maxIterations && plays < maxPlays) {
            Node currentNode = open.poll();

            if (currentNode.getBoardRef().equals(rules.getFinalPosition())) {
                found = true;
                finalNode = currentNode;
                break;
            }
            if (closed.contains(currentNode)) {
                continue;
            }

            closed.add(currentNode);

            rules.validMoves(currentNode.getBoardRef())
                    .stream()
                    .forEach(board -> {
                        Node node = new Node(board, currentNode, boardCost(board), currentNode.getPathCost() + playCost);
                        if (!closed.contains(node) && !open.contains(node)){
                            open.add(node);
                        }
                    });

            if (plays < currentNode.getPathCost().intValue()) {
                plays = currentNode.getPathCost().intValue();
            }
            if (maxOpen < open.size()) {
                maxOpen = open.size();
            }
            currentIteration++;
        }
        Instant end = Instant.now();

        if (!found) {
            System.out.println("I tried " + currentIteration + " times, gone " + plays
                    + " plays deep, visited " + closed.size() + " nodes and expanded "
                    + open.size() + " mode... Please try another initial board.");
        } else {
            printResult(maxOpen, currentIteration, closed.size(), Duration.between(start, end), finalNode);
        }

        return found;
    }

    /**
     *
     * Retorna o custo de um tabuleiro.
     *
     * @param board
     * @return
     */
    public Float boardCost(Board board) {
        Float cost = 0f;

        linePermutations(board);

        cost = board.getBoard()
                .stream()
                .map(piece -> {
                    Float manhattanCost = estimateManhattanMoveCost(piece, board.getBoard().indexOf(piece));
                    return manhattanCost;
                })
                .reduce((c1, c2) -> c1 + c2).get();

        return cost;
    }


    /**
     *
     * Estima o custo Manhattan da peça informada de acordo com sua posiçao atual.
     *
     * @param piece
     * @param index
     * @return
     */
    private Float estimateManhattanMoveCost(Integer piece, Integer index) {
        Integer estimate = 0;

        Position currentPosition = rules.boardCoordinates().get(index);
        Position finalPosition = rules.boardCoordinates().get(rules.getFinalPosition().getBoard().indexOf(piece));

        Integer a = Math.abs(currentPosition.getX() - finalPosition.getX());
        Integer b = Math.abs(currentPosition.getY() - finalPosition.getY());

        estimate = a+b;

        return estimate.floatValue();
    }

    /**
     *
     * Retorna o número de peças que tem posição final na sua
     * linha mas devem "pular" outra peça nas mesmas condições
     *
     * @param board
     * @return
     */
    private Integer linePermutations(Board board) {
        Integer n = 0;

        Map<Integer, Integer> conflictingPositinons = new HashMap<>();

        for (int lineNumber = 0; lineNumber <= rules.getMaxXY() ; lineNumber++) {
            Integer lineIndex = lineNumber + lineNumber * rules.getMaxXY();

            conflictingPositinons.clear();
            for (int pieceIndex = lineNumber + lineNumber * rules.getMaxXY(); pieceIndex < lineIndex + rules.getMaxXY()+1; pieceIndex++) {

                Integer piece = board.getBoard().get(pieceIndex);
                Integer pieceFinalIndex = rules.getFinalPosition().getBoard().indexOf(piece);

                Position currentPosition = rules.boardCoordinates().get(pieceIndex);
                Position finalPosition = rules.boardCoordinates().get(pieceFinalIndex);
                if(finalPosition.getX() == finalPosition.getX()){
                    conflictingPositinons.put(pieceIndex, pieceFinalIndex);
                }
            }

            if(conflictingPositinons.size()>1){
                conflictingPositinons.forEach((currentIndex0, finalIndex0) -> {
                    conflictingPositinons.forEach((currentIndexN, finalIndexN) -> {
                        if(currentIndexN!=currentIndex0){

                        }
                    });
                });
            }
        }

//        Position currentPosition = rules.boardCoordinates().get(index);
//        Position finalPosition = rules.boardCoordinates().get(rules.getFinalPosition().getBoard().indexOf(piece));
//
//        Integer a = Math.abs(currentPosition.getX() - finalPosition.getX());
//        Integer b = Math.abs(currentPosition.getY() - finalPosition.getY());
//
//        estimate = a+b;

        return n;
    }

    /**
     *
     * Mostra o relatório do jogo resolvido.
     *
     * @param maxElementsOpen
     * @param iterarions
     * @param visitedCount
     * @param execDuration
     * @param finalNode
     */
    private void printResult(Integer maxElementsOpen, Integer iterarions,
                             int visitedCount, Duration execDuration, Node finalNode) {

        Node currentNode = finalNode;
        List<Board> plays = new ArrayList<>();
        while(currentNode.getParent() !=null ){
            plays.add(currentNode.getBoardRef());
            currentNode = currentNode.getParent();
        }

        Collections.reverse(plays);

        StringBuilder sb = new StringBuilder();
        sb

                .append("Jogadas: ")
                .append(System.lineSeparator());
        plays.stream()
                .forEach(board-> {
                    sb.append(plays.indexOf(board)+1).append(":")
                            .append(System.lineSeparator())
                            .append(board.toString())
                            .append(System.lineSeparator())
                            .append(System.lineSeparator());
                });
                sb.append(System.lineSeparator())
                .append("Solução encontrada em ").append(execDuration)
                .append(" e ").append(iterarions).append(" ciclos ").append(execDuration.dividedBy(iterarions))
                .append("/ciclo")
                .append(System.lineSeparator())
                .append("Elementos máximos na fronteira: ").append(maxElementsOpen)
                .append(System.lineSeparator())
                .append("Total de elementos visitados: ").append(visitedCount)
                .append(System.lineSeparator())

                .append("Total de jogadas: ").append(plays.size())
                .append(System.lineSeparator());

        report = sb.toString();

    }

    /**
     *
     * Relatório do jogo resolvido.
     *
     * @return
     */
    public String getReport() {
        return report;
    }
}
