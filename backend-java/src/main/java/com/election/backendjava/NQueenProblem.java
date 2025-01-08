package com.election.backendjava;

public class NQueenProblem {

    public static void main(String[] args) {
        // Test boards: 4, 8, 12
        int[] boardSizes = {4, 12, 18, 24};

        System.out.println("===== NAIVE APPROACH =====");
        for (int size : boardSizes) {
            NaiveNQueens naive = new NaiveNQueens(size);
            naive.solve();
            System.out.println("--------------------------");
        }

        System.out.println("===== PRUNED APPROACH =====");
        for (int size : boardSizes) {
            PrunedNQueens pruned = new PrunedNQueens(size);
            pruned.solve();
            System.out.println("---------------------------");
        }
    }
}

/**
 * NAIVE APPROACH
 * - Checks row, col, and diagonals by scanning the board each time.
 * - Increments exploredStates every time we try to place a queen in a row/col.
 */
class NaiveNQueens {
    private int N;
    private int[][] board;
    private long exploredStates;

    public NaiveNQueens(int size) {
        this.N = size;
        board = new int[N][N];
        exploredStates = 0;
    }

    public void solve() {
        long startTime = System.currentTimeMillis();
        boolean success = solveNQUtil(0);
        long endTime = System.currentTimeMillis();

        System.out.println("Naive approach for " + N + "x" + N);
        if (!success) {
            System.out.println("No solution found.");
        } else {
            printBoard();
        }
        System.out.println("Explored States: " + exploredStates);
        System.out.println("Time taken (ms): " + (endTime - startTime));
    }

    // Try to place queens column by column
    private boolean solveNQUtil(int col) {
        if (col >= N) {
            return true;
        }
        for (int row = 0; row < N; row++) {
            exploredStates++; // We "attempt" placing here
            if (isSafe(row, col)) {
                board[row][col] = 1;
                if (solveNQUtil(col + 1)) {
                    return true;
                }
                // backtrack
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int row, int col) {
        // Check the row to the left
        for (int i = 0; i < col; i++) {
            if (board[row][i] == 1) return false;
        }
        // Check upper diagonal
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) return false;
        }
        // Check lower diagonal
        for (int i = row, j = col; j >= 0 && i < N; i++, j--) {
            if (board[i][j] == 1) return false;
        }
        return true;
    }

    private void printBoard() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                System.out.print(board[r][c] == 1 ? "Q " : ". ");
            }
            System.out.println();
        }
    }
}

/**
 * PRUNED APPROACH
 * - We keep track of columns, diag1, and diag2 usage in boolean arrays.
 * - This means we can skip a row/col immediately if it's invalid.
 */
class PrunedNQueens {
    private int N;
    private int[] queens;       // queens[row] = col for that row
    private boolean[] usedCols; // usedCols[c] = true if col c is taken
    private boolean[] diag1;    // diag1[r+c] = true if a diag is taken
    private boolean[] diag2;    // diag2[r-c+N-1] = true if a diag is taken
    private long exploredStates;

    public PrunedNQueens(int size) {
        this.N = size;
        queens = new int[N];
        usedCols = new boolean[N];
        diag1 = new boolean[2 * N - 1]; // for r+c
        diag2 = new boolean[2 * N - 1]; // for r-c + (N-1)
        exploredStates = 0;
    }

    public void solve() {
        long startTime = System.currentTimeMillis();
        boolean success = backtrack(0);
        long endTime = System.currentTimeMillis();

        System.out.println("Pruned approach for " + N + "x" + N);
        if (!success) {
            System.out.println("No solution found.");
        } else {
            printBoard();
        }
        System.out.println("Explored States: " + exploredStates);
        System.out.println("Time taken (ms): " + (endTime - startTime));
    }

    private boolean backtrack(int row) {
        if (row == N) {
            return true;
        }
        for (int col = 0; col < N; col++) {
            exploredStates++; // Attempting to place queen here
            if (!usedCols[col] && !diag1[row + col] && !diag2[row - col + (N - 1)]) {
                // Place the queen
                queens[row] = col;
                usedCols[col] = true;
                diag1[row + col] = true;
                diag2[row - col + (N - 1)] = true;

                if (backtrack(row + 1)) {
                    return true;
                }

                // backtrack
                usedCols[col] = false;
                diag1[row + col] = false;
                diag2[row - col + (N - 1)] = false;
            }
        }
        return false;
    }

    private void printBoard() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (queens[r] == c) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
