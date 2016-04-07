package br.ufsc.inf.sin.game;

public class Node implements Comparable {
    private Board boardRef;
    private Node parent;
    private Float heuristicCost;
    private Float pathCost;

    public Node() {
    }

    public Node(Board boardRef, Node parent, Float heuristicCost, Float pathCost) {
        this.boardRef = boardRef;
        this.parent = parent;
        this.heuristicCost = heuristicCost;
        this.pathCost = pathCost;
    }

    public Board getBoardRef() {
        return boardRef;
    }

    public void setBoardRef(Board boardRef) {
        this.boardRef = boardRef;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Float getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(Float heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public Float getPathCost() {
        return pathCost;
    }

    public void setPathCost(Float pathCost) {
        this.pathCost = pathCost;
    }

    @Override
    public int compareTo(Object node) {
        Node toCompare = (Node) node;
        Boolean bigger = (pathCost + heuristicCost) > toCompare.getPathCost() + toCompare.getHeuristicCost();
        Boolean smaller = (pathCost + heuristicCost) < toCompare.getPathCost() + toCompare.getHeuristicCost();
        if(bigger){
            return 1;
        }
        if(smaller){
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object node){
        return this.getBoardRef().equals(((Node)node).getBoardRef());
    }
}
