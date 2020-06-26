/* *****************************************************************************
 *  Name:    Richard Ma
 *  NetID:   rm44
 *  Precept: P05
 *
 *
 *  Description:  Given a Board, processes through to find the winning combo
 *  to get to the target board.
 *
 **************************************************************************** */


import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final int count; // number of steps taken so far
    private final SearchNode last; // solution board with traceback to original

    private static class SearchNode implements Comparable<SearchNode> {
        private final int func; // priority function num
        private final Board bo; // board in current step
        private SearchNode previous; // marker to previous
        private final int stepCount; // steps taken o far for each node


        // stores info of manhattan distance for board
        public SearchNode(Board board, int count) {
            func = board.manhattan() + count;
            bo = board;
            stepCount = count;
        }

        // compares to SearchNodes
        public int compareTo(SearchNode other) {
            if (this.func > other.func)
                return 1;
            else if (this.func < other.func)
                return -1;
            else return 0;
        }

    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null || !initial.isSolvable())
            throw new IllegalArgumentException("Unsolvable board");

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        // count = 0;

        // create first search node
        SearchNode first = new SearchNode(initial, 0);
        first.previous = null;

        // add searchnode
        pq.insert(first);


        // continue solving until solution is dequeued
        while (!pq.min().bo.isGoal()) {
            SearchNode min = pq.delMin();

            // count++;

            // add neighbors
            for (Board neigh : min.bo.neighbors()) {
                SearchNode neighbor = new SearchNode(neigh, min.stepCount + 1);
                neighbor.previous = min;

                // compare to grandfather
                if (neighbor.previous.previous == null) // specific first case
                    pq.insert(neighbor);
                else if (!neigh.equals(min.previous.bo))
                    pq.insert(neighbor);
            }
        }
        last = pq.min();
        count = last.stepCount;
    }

    // min number of moves to solve initial board
    public int moves() {
        return count;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> stack = new Stack<Board>();
        SearchNode tracer = last;
        while (tracer != null) {
            stack.push(tracer.bo);
            tracer = tracer.previous;
        }
        return stack;
    }

    // tester
    public static void main(String[] args) {
        int[][] board = new int[3][3];

        board[0][0] = 1;
        board[0][1] = 2;
        board[0][2] = 3;
        board[1][0] = 0;
        board[1][1] = 7;
        board[1][2] = 6;
        board[2][0] = 5;
        board[2][1] = 4;
        board[2][2] = 8;

        Board bo = new Board(board);

        Solver test = new Solver(bo);
        StdOut.println(test.moves());

        for (Board steps : test.solution()) {
            StdOut.println(steps);
        }
    }
}
