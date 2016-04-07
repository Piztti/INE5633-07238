package br.ufsc.inf.sin.game;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Heuristic {

    private final Float playCost = 1f;
    private Rules rules;
    private String report = "";

    Integer timesManhattanUsed = 0;
    Integer timesCartesianUsed = 0;

    public Heuristic(Rules rules) {
        this.rules = rules;
    }

    public Boolean solve(Board initialBoard) throws Exception {

        Instant start = Instant.now();

        timesManhattanUsed = 0;
        timesCartesianUsed = 0;

        Node rootNode = new Node(initialBoard, null, boardCost(initialBoard), 0f);
        Set<Board> generatedStates = new HashSet<>();
        Set<Node> closed = new HashSet<>();
        Queue<Node> open = new PriorityQueue<>();

        generatedStates.add(initialBoard);
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
                        if (!generatedStates.contains(board)
                                && (boardCost(currentNode.getBoardRef()) + currentNode.getPathCost()) >= (boardCost(board))) {
                            generatedStates.add(board);
                            open.add(new Node(board, currentNode, boardCost(board), currentNode.getPathCost() + playCost));
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

    public Float boardCost(Board board) {
        Float cost = 0f;

        cost = board.getBoard()
                .stream()
                .map(piece -> {
                    Float cartesianCost = estimateCartesianMoveCost(piece, board.getBoard().indexOf(piece));
                    Float manhattanCost = estimateManhattanMoveCost(piece, board.getBoard().indexOf(piece));
                    Float estimate = 0f;
                    if(cartesianCost == manhattanCost){
                        estimate = cartesianCost;
                        this.timesCartesianUsed++;
                        this.timesManhattanUsed++;
                    }
                    if(cartesianCost > manhattanCost){
                        estimate = cartesianCost;
                        this.timesCartesianUsed++;
                    } else {
                        estimate = manhattanCost;
                        this.timesManhattanUsed++;
                    }
                    return estimate;
                })
                .reduce((c1, c2) -> c1 + c2).get();

        return cost;
    }

    private Float estimateCartesianMoveCost(Integer piece, Integer index) {
        Float estimate = 0f;

        Position currentPosition = rules.boardCoordinates().get(index);
        Position finalPosition = rules.boardCoordinates().get(rules.getFinalPosition().getBoard().indexOf(piece));

        Double a = Math.pow(currentPosition.getX() - finalPosition.getX(), 2);
        Double b = Math.pow(currentPosition.getY() - finalPosition.getY(), 2);

        estimate = (new Double(Math.sqrt(a + b))).floatValue();

        return estimate;
    }

    private Float estimateManhattanMoveCost(Integer piece, Integer index) {
        Integer estimate = 0;

        Position currentPosition = rules.boardCoordinates().get(index);
        Position finalPosition = rules.boardCoordinates().get(rules.getFinalPosition().getBoard().indexOf(piece));

        Integer a = Math.abs(currentPosition.getX() - finalPosition.getX());
        Integer b = Math.abs(currentPosition.getY() - finalPosition.getY());

        estimate = a+b;

        return estimate.floatValue();
    }

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
                .append(System.lineSeparator())
                .append("Uso Manhatan: ").append(new Float(timesManhattanUsed))
                .append(System.lineSeparator())
                .append("Uso Cartesiano: ").append(new Float(timesCartesianUsed))
                .append(System.lineSeparator());

        report = sb.toString();

    }

    public String getReport() {
        return report;
    }
}
