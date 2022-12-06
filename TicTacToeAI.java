import java.util.Scanner;
import java.util.Random;

public class TicTacToeAI {

    static Board board = new Board();
    static int turns = 0;
    static char currentTurn = 'X';
    // Difficulty is 0 - easy, 1 - medium, 2 - hard
    static int difficulty = 1;
    // For easy and medium AI
    static Random random = new Random();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        board.showBoard();

        while (true) {
            int position = playerInput(input);
            if (!checkIfValid(position)) {
                continue;
            }

            setPositionToCurrentTurn(position);
            currentTurn = currentTurn == 'X' ? 'O' : 'X';

            board.showBoard();
            if (checkIfALetterWon()) {
                System.out.println(String.format("%s WON!", board.winningPlayer));
                break;
            } else if (board.checkIfDraw()) {
                System.out.println("DRAWWW!!!");
                break;
            }

            if(difficulty == 0) {
                RandomInput(currentTurn);
            } else if(difficulty == 1) {
                MediumAIInput(currentTurn);
            } else {
                AIInput(currentTurn);
            }
            board.showBoard();
            currentTurn = currentTurn == 'X' ? 'O' : 'X';

            if (checkIfALetterWon()) {
                System.out.println(String.format("%s WON!", board.winningPlayer));
                break;
            } else if (board.checkIfDraw()) {
                System.out.println("DRAWWW!!!");
                break;
            }
        }
    }

    public static int playerInput(Scanner input) {
        while (true) {
            if (input.hasNextInt()) {
                int position = input.nextInt();
                if (position < 1) {
                    System.out.println("INPUT MUST BE GREATER THAN 0!");
                } else if (position > 9) {
                    System.out.println("INPUT MUST BE LESS THAN 9!");
                }

                return position - 1;
            } else {
                input.next();
            }
        }
    }

    public static boolean checkIfValid(int position) {
        int row = position / 3;
        int col = position % 3;

        if (board.board[row][col] != ' ') {
            displayOccupiedSpace(row, col);
            return false;
        }

        return true;
    }

    public static boolean checkIfValidAI(int position) {
        int row = position / 3;
        int col = position % 3;

        if (board.board[row][col] != ' ') {
            return false;
        }

        return true;
    }

    public static void displayOccupiedSpace(int row, int col) {
        System.out.println(String.format("THAT POSITION IS OCCUPIED BY %s!", board.board[row][col]));
    }

    public static void setPositionToCurrentTurn(int position) {
        int row = position / 3;
        int col = position % 3;

        board.board[row][col] = currentTurn;
    }

    private static boolean checkIfALetterWon() {
        if (board.checkIfRowWon()) {
            return true;
        } else if (board.checkIfColumnWon()) {
            return true;
        } else if (board.checkIfDiagonalWon()) {
            return true;
        }

        return false;
    }

    public static void RandomInput(char currentTurn) {
        while (true) {
            int position = random.nextInt(9);
            if (checkIfValidAI(position)) {
                setPositionToCurrentTurn(position);
                break;
            }
        }
    }

    public static void MediumAIInput(char currentTurn) {
        int smartOrDumb = random.nextInt(2);
        if(smartOrDumb == 0) {
            RandomInput(currentTurn);
        } else {
            AIInput(currentTurn);
        }
    }

    public static void AIInput(char currentTurn) {
        int bestMove = 0;
        int bestScore = -100000;

        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                if(board.board[row][col] != ' ') {
                    continue;
                }

                board.board[row][col] = currentTurn;
                int score = minimax(board, currentTurn, false);
                board.board[row][col] = ' ';
                if(score > bestScore) {
                    bestScore = score;
                    // We will be running this in position /3 and %3
                    bestMove = row * 3 + col;
                }
            }
        }

        setPositionToCurrentTurn(bestMove);
    }



    private static int minimax(Board board, char AILetter, boolean isMaximizing) {
        char playerLetter = currentTurn=='X' ? 'O' : 'X';

        if(checkIfLetterWonMinimax(board, AILetter)) {
            return 100000;
        } else if(checkIfLetterWonMinimax(board, playerLetter)) {
            return -100000;
        } else if(board.checkIfDraw()) {
            return 0;
        }

        if(isMaximizing) {
            int bestScore = -1000;

            for(int row = 0; row < 3; row++) {
                for(int col = 0; col < 3; col++) {
                    if(board.board[row][col] != ' ') {
                        continue;
                    }

                    board.board[row][col] = AILetter;
                    int score = minimax(board, AILetter, false);
                    board.board[row][col] = ' ';
                    if(score > bestScore) {
                        bestScore = score;
                        // We will be running this in position /3 and %3
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = 1000;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board.board[row][col] != ' ') {
                        continue;
                    }

                    board.board[row][col] = playerLetter;
                    int score = minimax(board, AILetter, true);
                    board.board[row][col] = ' ';
                    if (score < bestScore) {
                        bestScore = score;
                        // We will be running this in position /3 and %3
                    }
                }
            }

            return bestScore;
        }
    }

    private static boolean checkIfLetterWonMinimax(Board board, char letter) {
        if(checkIfRowWonMinimax(board, letter)) {
            return true;
        } else if(checkIfColumnWonMinimax(board, letter)) {
            return true;
        } else if(checkIfDiagonalWonMinimax(board, letter)) {
            return true;
        }

        return false;
    }

    public static boolean checkIfRowWonMinimax(Board board, char letter) {
        for(int row = 0; row < 3; row++) {
            if(board.board[row][0] == board.board[row][1] && board.board[row][1] == board.board[row][2] && board.board[row][2] == letter) {
                return true;
            }
        }
        
        return false;
    }

    public static boolean checkIfColumnWonMinimax(Board board, char letter) {
        for(int col = 0; col < 3; col++) {
            if(board.board[0][col] == board.board[1][col] && board.board[1][col] == board.board[2][col] && board.board[2][col] == letter){
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfDiagonalWonMinimax(Board board, char letter) {
        if(board.board[0][0] == board.board[1][1] && board.board[1][1] == board.board[2][2] && board.board[2][2] == letter) {
            return true;
        } else if(board.board[0][2] == board.board[1][1] && board.board[1][1] == board.board[2][0] && board.board[2][0] == letter) {
            return true;
        }
        
        return false;
    }

    
}


class Board {
    char[][] board;
    char winningPlayer = ' ';

    public Board() {
        this.board = createBoard();
    }

    public char[][] createBoard() {
        char[][] board = new char[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = ' ';
            }
        }

        return board;
    }

    public boolean checkIfDraw() {
        if (board[0][0] != ' ' && board[0][1] != ' ' && board[0][2] != ' ' && board[1][0] != ' ' && board[1][1] != ' '
                && board[1][2] != ' ' && board[2][0] != ' ' && board[2][1] != ' ' && board[2][2] != ' ') {
            return true;
        }

        return false;
    }

    public boolean checkIfRowWon() {
        if (board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][0] != ' ') {
            winningPlayer = board[0][0];
            return true;
        } else if (board[1][0] == board[0][1] && board[1][1] == board[1][2] && board[1][0] != ' ') {
            winningPlayer = board[1][0];
            return true;
        } else if (board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][0] != ' ') {
            winningPlayer = board[2][0];
            return true;
        }

        return false;
    }

    public boolean checkIfColumnWon() {
        if (board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[0][0] != ' ') {
            winningPlayer = board[0][0];
            return true;
        } else if (board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[0][1] != ' ') {
            winningPlayer = board[0][1];
            return true;
        } else if (board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[0][2] != ' ') {
            winningPlayer = board[0][2];
            return true;
        }

        return false;
    }

    public boolean checkIfDiagonalWon() {
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            winningPlayer = board[0][0];
            return true;
        } else if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            winningPlayer = board[0][2];
            return true;
        }

        return false;
    }

    public void showBoard() {
        String visibleBoard = "";

        for (int row = 2; row >= 0; row--) {
            for (int col = 0; col < 3; col++) {
                visibleBoard += Character.toString(board[row][col]);

                if (col != 2) {
                    visibleBoard += " |";
                } else {
                    visibleBoard += "\n";
                }
            }
        }

        System.out.println(visibleBoard);
    }
}

