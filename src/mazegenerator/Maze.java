package mazegenerator;

import java.util.Random;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * @author Devan Carlson, devansc@gmail.com
 */
public class Maze {

    private int mazeSize = 30;

    private MazeCell[][] maze;
    private LinkedList<MazeCell> queue;

    private Random randomGenerator;
    
    /**
     * Sets the size of the maze to a square dimension of size x size. 
     * @param size the new width and height of the maze
     */
    public void SetMazeSize(int size) {
        mazeSize = size;
    }

    /**
     * Builds a maze of size 30x30 unless setMazeSize was called beforehand.
     */
    public void Build() {
        randomGenerator = new Random();

        maze = new MazeCell[mazeSize][mazeSize];
        for (int row = 0; row < mazeSize; row++) {
            for (int col = 0; col < mazeSize; col++) {
                maze[row][col] = new MazeCell(row, col);
            }
        }
        queue = new LinkedList<>();
        StdDraw.filledSquare(.5, .5, .5);
        StdDraw.setPenColor(StdDraw.WHITE);
        buildMaze();
    }

    /**
     * Draws a solution from the start to the finish of the maze. The start is 
     * in the bottom left corner, and the finish is in the top right. 
     */
    public void ShowSolution() {
        MazeCell end = maze[mazeSize - 1][mazeSize - 1];
        MazeCell start = maze[0][0];
        MazeCell cur = end;
        
        StdDraw.setPenColor(StdDraw.CYAN);
        drawSquare(end.row, end.col);
        while(cur != start) {
            drawPath(cur, cur.parent);
            cur = cur.parent;
        }
        drawStartEnd();
    }

    /**
     * Draws a single square or cell in the maze in the current pen color. 
     * @param row the row to draw the square in
     * @param col the column to draw the square in
     */
    private void drawSquare(double row, double col) {
        double sizeSquare = (double) 1 / mazeSize;
        double offset = sizeSquare / 2;
        sizeSquare /= 4;
        StdDraw.filledSquare(offset + row / mazeSize, offset + col / mazeSize, sizeSquare);
    }

    /**
     * Draws a path from the current cell to the neighbor cell in the maze. 
     * @param cur the current cell the path is on
     * @param nbr the neighbor cell the path is going to
     */
    private void drawPath(MazeCell cur, MazeCell nbr) {
        drawSquare(nbr.row, nbr.col);
        double row = (double) (cur.row + nbr.row) / 2;
        double col = (double) (cur.col + nbr.col) / 2;
        drawSquare(row, col);
    }

    /**
     * Builds the maze using a Depth First Search Algorithm. 
     */
    private void buildMaze() {
        MazeCell current = maze[0][0];
        drawSquare(0, 0);

        // Depth first search iteratively using a queue
        while (current != null) {
            queue.add(current);
            current.visited = true;
            MazeCell neighbor = getRandomNonVisitedNeighbor(current);
            if (neighbor == null) {
                current = findNonVisitedNeighbor();
                continue;
            }
            neighbor.setParent(current);
            drawPath(current, neighbor);
            current = neighbor;
        }
        drawStartEnd();
    }
    
    /**
     * Draws a picture of myself at the start, and coffee the end of the maze.
     */
    private void drawStartEnd() {
        double sizeSquare = (double) 1 / (2*mazeSize);
        StdDraw.picture(sizeSquare, sizeSquare, "Me.jpg", sizeSquare, sizeSquare);
        StdDraw.picture(1-sizeSquare, 1-sizeSquare, "Coffee.png", sizeSquare, sizeSquare);
    }

    /**
     * Recursively pulls cells off of the queue looking for a cell with a 
     * neighbor that has not been visited yet.
     * @return the first cell in the queue that has a non-visited neighbor
     */
    private MazeCell findNonVisitedNeighbor() {
        MazeCell cur = queue.poll();
        if (cur != null && getRandomNonVisitedNeighbor(cur) != null) {
            return cur;
        } else if (cur != null) {
            return findNonVisitedNeighbor();
        } else {
            return null;
        }
    }

    /**
     * Gets a random neighbor to this cell that has not been marked as visited.
     * @param cell the cell we want to find a neighbor of
     * @return a random non-visited neighbor or null if there is no such 
     * neighbor 
    */
    private MazeCell getRandomNonVisitedNeighbor(MazeCell cell) {
        int row = cell.row;
        int col = cell.col;
        ArrayList<MazeCell> possibleNeighbors = new ArrayList<>();
        if (row > 0) {
            if (!maze[row - 1][col].visited) {
                possibleNeighbors.add(maze[row - 1][col]);
            }
        }
        if (row < mazeSize - 1) {
            if (!maze[row + 1][col].visited) {
                possibleNeighbors.add(maze[row + 1][col]);
            }
        }
        if (col > 0) {
            if (!maze[row][col - 1].visited) {
                possibleNeighbors.add(maze[row][col - 1]);
            }
        }
        if (col < mazeSize - 1) {
            if (!maze[row][col + 1].visited) {
                possibleNeighbors.add(maze[row][col + 1]);
            }
        }

        if (possibleNeighbors.size() > 0) {
            int ndx = randomGenerator.nextInt(possibleNeighbors.size());
            return possibleNeighbors.get(ndx);
        }
        return null;
    }

    /**
     * Private class that represents a cell in the maze.
     */
    private class MazeCell {

        MazeCell parent;
        boolean visited;
        int row, col;

        MazeCell(int row, int col) {
            visited = false;
            this.row = row;
            this.col = col;
        }

        void setParent(MazeCell p) {
            parent = p;
        }
    }
}
