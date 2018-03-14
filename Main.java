import java.util.Scanner;
import java.lang.Math;

public class Main {

	static class Move {
		public int row;
		public int col;
	}

	private static char player = 'X';
	private static char opponent = 'O';
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		playGame(player);	
	}

	private static void showBoard(char[][] board) {
		System.out.printf("\n\n");
		System.out.printf("\t\t\t %c | %c | %c \n", board[0][0], board[0][1], board[0][2]);
		System.out.printf("\t\t\t-----------\n");
		System.out.printf("\t\t\t %c | %c | %c \n", board[1][0], board[1][1], board[1][2]);
		System.out.printf("\t\t\t-----------\n");
		System.out.printf("\t\t\t %c | %c | %c \n\n", board[2][0], board[2][1], board[2][2]);
	}

	private static void showInstructions() {
		System.out.printf("\t\t\tTic-Tac-Toe\n\n");
		System.out.printf("Choose a cell numbered from 1 to 9 as below and play\n\n");
		System.out.printf("\t\t\t 1 | 2 | 3 \n");
		System.out.printf("\t\t\t-----------\n");
		System.out.printf("\t\t\t 4 | 5 | 6 \n");
		System.out.printf("\t\t\t-----------\n");
		System.out.printf("\t\t\t 7 | 8 | 9 \n");

		System.out.printf("-\t-\t-\t-\t-\t-\t-\t-\t-\t-\n\n");
	}

	private static int evaluate(char[][] board) {
		// Check Rows for X or O victory
		for (int row = 0; row < 3; ++row) {
			if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
				if (board[row][0] == player)
					return +10;
				else if (board[row][0] == opponent)
					return -10;
			}
		}

		// Check Columns for X or O victory
		for (int col = 0; col < 3; ++col) {
			if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
				if (board[0][col] == player)
					return +10;
				else if (board[0][col] == opponent)
					return -10;
			}
		}

		// Check Diagonals for X or O victory
		if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
			if (board[0][0] == player)
				return +10;
			else if (board[0][0] == opponent)
				return -10;
		}

		if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
			if (board[0][2] == player)
				return +10;
			else if (board[0][2] == opponent)
				return -10;
		}

		// Else if none of them have won return 0
		return 0;
	}

	private static boolean isMovesLeft(char[][] board) {
		for (int i = 0; i < 3; ++i) 
			for (int j = 0; j < 3; ++j) 
				if (board[i][j] == ' ')
					return true;
		return false;
	}

	private static int minimax(char[][] board, int depth, boolean isMax) {
		int score = evaluate(board);

		// If maximizer has won the game, return his/her evaluated score
		if (score == 10)
			return score - depth;			// minus depth to win fastest

		// If minimizer has won the game, return his//her evaluated score
		if (score == -10)
			return score + depth;			// add depth to prolong defeat

		// If there are no more moves and no winner then, it is a tie
		if (isMovesLeft(board) == false)
			return 0;

		// If it is mazimizer's move
		if (isMax) {
			int best = -1000;

			// Traverse all cells
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					// Check if cell is empty
					if (board[i][j] == ' ') {
						// Make the move
						board[i][j] = player;

						// Call minimax recursively and choose the maximum value
						best = Math.max(best, minimax(board, depth + 1, !isMax));

						// Undo the move
						board[i][j] = ' ';
					}
				}
			}
			return best;
		}

		// If it is minimizer's move
		else {
			int best = 1000;

			// Traverse all cells 
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					// Check if cell is empty
					if (board[i][j] == ' ') {
						// Make the move
						board[i][j] = opponent;

						// Call minmax recursively and choose the minimum value
						best = Math.min(best, minimax(board, depth + 1, !isMax));

						// Undo the move
						board[i][j] = ' ';
					}
				}
			}
			return best;
		}
	}
	
	// Find the best possible move for the computer (Minimizer)
	private static Move findBestMove (char[][] board) {
		int bestVal = 1000;
		Move bestMove = new Move();
		bestMove.row = -1;
		bestMove.col = -1;

		// Traverse all cells, evaluate minimax function for all empty cells.
		// And return the cell with the optimal value.

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				// Check if the cell is empty
				if (board[i][j] == ' ') {

					// Make the move
					board[i][j] = opponent;

					// Compute the evaluation function for this move
					int moveVal = minimax(board, 0, true);

					// Undo the move
					board[i][j] = ' ';

					// If the value of the current move is less than the best value,
					// then update bestVal and bestMove
					if (moveVal < bestVal) {
						bestMove.row = i;
						bestMove.col = j;
						bestVal = moveVal;
					}
				}
			}
		}
		return bestMove;
	}

	private static void clearBoard(char[][] board) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				board[i][j] = ' ';
			}
		}
	}

	private static boolean gameOver(char[][] board) {
		int boardVal = evaluate(board);

		if (boardVal == +10 || boardVal == -10 || isMovesLeft(board) == false)
			return true;

		return false;
	}

	private static boolean invalidPosition(char[][] board, int position) {
		if (position < 1 || position > 9)
			return true;

		int index = position - 1;
		if (board[index / 3][index % 3] != ' ')
			return true;

		return false;
	}

	private static void playGame(char whoseTurn) {
		// A 3 * 3 Tic Tac Toe board for playing
		char[][] board = new char[3][3];

		// Clear the board
		clearBoard(board);

		// Show the Instructions
		showInstructions();

		// Show the board
		showBoard(board);

		while (!gameOver(board)) {
			if (whoseTurn == opponent) {
				// Find the best move for the Computer
				Move bestMove = findBestMove(board);

				// Make the move
				board[bestMove.row][bestMove.col] = opponent;

				System.out.printf("Computer has put %c in cell %d\n", opponent, ((bestMove.row * 3) + bestMove.col + 1));

				showBoard(board);

				// End the computer's turn
				whoseTurn = player;


			} else if (whoseTurn == player) {
				// Get the player's move
				int position;
				do {
					System.out.print("Enter the position that you want to play in: ");
					position = scanner.nextInt();
				} while (invalidPosition(board, position));

				position -= 1;		// To map position to index

				Move playerMove = new Move();
				playerMove.row = position / 3;
				playerMove.col = position % 3;

				// Make the move
				board[playerMove.row][playerMove.col] = player;

				System.out.printf("You have put %c in cell %d\n", player, position + 1);

				showBoard(board);

				// End the player's turn
				whoseTurn = opponent;

			}
		}

		// If the game is over check who has won
		int boardVal = evaluate(board);

		if (boardVal == +10)
			System.out.println("Congratulation you won!!!");
		else if (boardVal == -10)
			System.out.println("Sorry you lost :(... Try again.");
		else
			System.out.println("It's a draw!");
	}
}
