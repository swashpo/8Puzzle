/* *****************************************************************************
 *  Name:    Richard Ma
 *  NetID:   rm44
 *  Precept: P05
 *
 *
 *  Description:  Creates Board object and methods for computing neighbors,
 *  hamming, and manhattan distances.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int length; // board size
    private final int[][] board; // 8 puzzle array representation
    private int zeroX; // x coordinate of blank tile
    private int zeroY; // y coordinate of blank tile
    private int ham; // hamming distance
    private int manhattan; // manhattan distance


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        length = tiles.length; // dimension

        board = new int[length][length];

        // copy tiles into board
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j] = tiles[i][j];

                // keep track of location of zero
                if (tiles[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                }
            }
        }

        // below code calculates manhattan distance ***********************
        manhattan = 0;

        // check each tile and sum differences between it and supposed location
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int current = board[i][j];

                if (current != 0) {
                    int row = (current - 1) / length;
                    int col = (current - 1) % length;

                    manhattan += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        // *****************************************************************

        // calculate hamming distance **************************************
        int tileNum = 1;
        ham = 0;

        // check each tile to see if it matches the location
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j] != tileNum && board[i][j] != 0)
                    ham++;
                tileNum++;
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("" + length + "\n");

        // append each tile to string
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                sb.append(String.format("%2d ", tileAt(i, j)));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    // return tile at row and column
    public int tileAt(int row, int col) {
        if (row < 0 || row > length - 1 || col < 0 || col > length - 1)
            throw new IllegalArgumentException("Bad location");

        return board[row][col];
    }

    // board size n
    public int size() {
        return length;
    }

    // number of tiles out of place
    public int hamming() {
        return ham;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (this.hamming() == 0)
            return true;
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // null object
        if (y == null)
            return false;
        // not a board object
        if (this.getClass() != y.getClass())
            return false;

        Board that = (Board) y;
        int thisSize = this.size();

        // if dimensions are different
        if (thisSize != that.size())
            return false;

        // check each tile
        for (int i = 0; i < thisSize; i++) {
            for (int j = 0; j < thisSize; j++) {
                if (this.tileAt(i, j) != that.tileAt(i, j))
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();

        // array representation of board
        int[][] newBoard = new int[length][length];

        // copy all tiles into array
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }

        if (zeroX - 1 >= 0) {
            // top
            newBoard[zeroX][zeroY] = newBoard[zeroX - 1][zeroY];
            newBoard[zeroX - 1][zeroY] = 0;

            // create and add neighbor above to stack
            Board neighbor = new Board(newBoard);
            stack.push(neighbor);

            // reset newBoard for later loops
            newBoard[zeroX - 1][zeroY] = newBoard[zeroX][zeroY];
            newBoard[zeroX][zeroY] = 0;

        }

        if (zeroX + 1 < length) {
            // bottom
            newBoard[zeroX][zeroY] = newBoard[zeroX + 1][zeroY];
            newBoard[zeroX + 1][zeroY] = 0;

            // create and add neighbor above to stack
            Board neighbor = new Board(newBoard);
            stack.push(neighbor);

            // reset newBoard for later loops
            newBoard[zeroX + 1][zeroY] = newBoard[zeroX][zeroY];
            newBoard[zeroX][zeroY] = 0;
        }

        if (zeroY - 1 >= 0) {
            // left
            newBoard[zeroX][zeroY] = newBoard[zeroX][zeroY - 1];
            newBoard[zeroX][zeroY - 1] = 0;

            // create and add neighbor above to stack
            Board neighbor = new Board(newBoard);
            stack.push(neighbor);

            // reset newBoard for later loops
            newBoard[zeroX][zeroY - 1] = newBoard[zeroX][zeroY];
            newBoard[zeroX][zeroY] = 0;
        }

        if (zeroY + 1 < length) {
            // right
            newBoard[zeroX][zeroY] = newBoard[zeroX][zeroY + 1];
            newBoard[zeroX][zeroY + 1] = 0;

            // create and add neighbor above to stack
            Board neighbor = new Board(newBoard);
            stack.push(neighbor);

            // reset newBoard for later loops
            newBoard[zeroX][zeroY + 1] = newBoard[zeroX][zeroY];
            newBoard[zeroX][zeroY] = 0;
        }

        return stack;
    }

    // is this board solvable?
    public boolean isSolvable() {
        /*
        Use double for loop to go through array and store into 1D array.
        Now loop through 1D array, and if we skip a number, inversions++
         */

        // transfer to 1D array
        int[] oneD = new int[length * length - 1];
        int marker = 0; // 1D array marker
        int zeroRow = 0; // row for blank

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (board[i][j] != 0) {
                    oneD[marker] = board[i][j];
                    marker++;
                }
                else
                    zeroRow = i; // mark row location of blank
            }
        }

        int inversions = 0;

        // count inversions
        for (int i = 0; i < oneD.length; i++) {
            for (int j = i + 1; j < oneD.length; j++) {
                if (oneD[i] > oneD[j])
                    inversions++;
            }
        }

        // odd sized array
        if (length % 2 == 1) {
            if (inversions % 2 == 0)
                return true;
            return false;
        }
        else { // even sized array
            if ((inversions + zeroRow) % 2 != 0)
                return true;
            return false;
        }
    }

    // tester
    public static void main(String[] args) {
        int[][] board = new int[3][3];

        board[0][0] = 2;
        board[0][1] = 6;
        board[0][2] = 0;
        board[1][0] = 1;
        board[1][1] = 4;
        board[1][2] = 7;
        board[2][0] = 3;
        board[2][1] = 5;
        board[2][2] = 8;

        Board bo = new Board(board);
        StdOut.println(bo.toString());

        StdOut.println("Size:" + bo.size());
        int tileAt = bo.tileAt(2, 2);

        if (bo.isSolvable())
            StdOut.println("This is solvable");

        StdOut.println("Tile at (2, 2): " + tileAt);

        StdOut.println(bo.hamming());
        StdOut.println(bo.manhattan());

        if (bo.isGoal())
            StdOut.println("This is it!");
        else
            StdOut.println("This isn't the goal");

        Board bo2 = new Board(board);

        if (bo.equals(bo2))
            StdOut.println("Bo equals bo2");

        for (Board neighbors : bo.neighbors())
            StdOut.println(neighbors);


    }
}
