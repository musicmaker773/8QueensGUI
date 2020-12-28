import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.ActionEvent;
import java.util.Stack;


public class ChessBoard {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private JPanel chessBoard;
    private static final String COLS = "12345678";

    private Stack<RowCol> optimalSol = new Stack<RowCol>();

    private char[][] board = new char[8][8];

    ChessBoard() {
        initializeGui();

    }

    public final void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("Execute 8 Queens") {
            // clears board before printing the queens
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j<8; j++) {
                        chessBoardSquares[i][j].setIcon(new ImageIcon(""));
                    }
                }
                // runs code for 8 queens then prints out the 8 queens
                execute(optimalSol, board);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j<8; j++) {
                        if(board[i][j] == 'X') {
                            chessBoardSquares[i][j].setIcon(new ImageIcon("src/kindpng_971447.png"));
                        }
                    }
                }

            }
        };
        tools.add(newGameAction);

        gui.add(new JLabel(""), BorderLayout.LINE_START);

        chessBoard = new JPanel(new GridLayout(0, 9));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);

        // create the chess board squares
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                b.setPreferredSize(new Dimension(64, 64));
                chessBoardSquares[jj][ii] = b;
            }
        }

        //fill the chess board
        chessBoard.add(new JLabel(""));
        // fill the top row
        for (int ii = 0; ii < 8; ii++) {
            chessBoard.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                            SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                switch (jj) {
                    case 0:
                        chessBoard.add(new JLabel("" + (ii + 1),
                                SwingConstants.CENTER));
                    default:
                        chessBoard.add(chessBoardSquares[jj][ii]);
                }
            }
        }
    }

    public final JComponent getChessBoard() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                ChessBoard cb =
                        new ChessBoard();

                JFrame f = new JFrame("8 Queens");
                f.add(cb.getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
    public static void execute(Stack<RowCol> optimalSol, char board[][]){


        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j<8; j++) {
                board[i][j] = '-';
            }
        }
        solve(optimalSol, 0, count);

        while (!optimalSol.isEmpty()) {
            RowCol copy = optimalSol.pop();
            board[copy.getRow()][copy.getCol()] = 'X';
        }

    }
    public static boolean solve(Stack<RowCol> optimalSol, int row, int count) {

        // base case
        if (row >= 8) {
            return true;
        }
        for (int i = 0; i < 8; i++) {

            RowCol temp = new RowCol(row, i);

            boolean result = optimal(optimalSol, row, i);

            if (result) {
                optimalSol.push(temp);
                count++;

                // recursive return
                if (solve(optimalSol, row + 1, count)) {
                    return true;
                }

                // backtracking
                if (!optimalSol.isEmpty()) {
                    optimalSol.pop();
                    count--;
                }
            }
        }
        return false;
    }
    public static boolean optimal(Stack<RowCol> optimalSol, int checkRow, int checkCol) {

        // makes a copy of the stack to empty out whats in it
        Stack<RowCol> optimalSolCopy = (Stack<RowCol>)optimalSol.clone();
        while (!optimalSolCopy.isEmpty()) {

            // each item in a stack gets checked if it collides with the current position
            RowCol toCheck = optimalSolCopy.pop();
            if (toCheck.getCol() == checkCol) {
                return false;
            }
            if (toCheck.getRow() == checkRow) {
                return false;
            }
            int sub1 = Math.abs(toCheck.getCol() - checkCol);
            int sub2 = Math.abs(toCheck.getRow() - checkRow);
            if (sub1 == sub2) {
                return false;
            }
        }
        return true;

    }

    public static class RowCol {

        private int row;
        private int col;

        public RowCol(int Trow, int Tcol) {

            row = Trow;
            col = Tcol;
        }

        public int getRow(){
            return row;
        }

        public int getCol(){
            return col;
        }


    }
}