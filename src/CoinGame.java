public class CoinGame {

	public static void main(String[] args) {

		int coinsCount = 0;
		if (args.length == 1) {
			try {
				coinsCount = Integer.parseInt(args[0]);
				if (coinsCount < 1) {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				System.err.println("Argument \"" + args[0]
						+ "\" must be a positive integer.");
				System.exit(1);
			}
		} else {
			System.out
					.println("You should input one integer as argument for number of coins");
			System.exit(2);
		}

		int[] startingCoins = new int[coinsCount];
		for (int j = 0; j < coinsCount; j++) {
			startingCoins[j] = 1;
		}
		Node root = new Node(startingCoins);
		int pointsWon = alphabeta(root, coinsCount, Integer.MIN_VALUE,
				Integer.MAX_VALUE, true, 0);

		int index = root.calculateBestChildCoinsTakenIndex();
		int coinsDifference = root.calculateBestChildCoinsDifference();
		System.out.println("{" + index + ", " + coinsDifference + ", "
				+ pointsWon + "}");

	}

	// @node is the currently explored node(state)
	// @coinsAmount is the number of coins at the start of the game
	// @alpha is the maximum value
	// @beta is the minimum value
	// @maximizingPlayerTurn shows who's turn is in the current call of the
	// function
	// @moves is the number of moves made before the game is over
	public static int alphabeta(Node node, int coinsAmount, int alpha,
			int beta, boolean maximazingPlayerTurn, int moves) {

		// if the current node is a terminal node
		// meaning it has all coins taken
		if (node.isTerminal()) {

			// this means that on the previous move the player has won
			// return the amount of coins that the game starts with minus the
			// number of moves that take to go to this terminal node
			return maximazingPlayerTurn ? (-1) * (coinsAmount - moves)
					: coinsAmount - moves;
		}

		if (maximazingPlayerTurn == true) {

			int value = Integer.MIN_VALUE;

			// used for tracking the best child node of the currently explored
			// node
			Node bestChild = null;
			int tempBestValue = value;

			for (Node child : node.getChildren()) {

				value = Math.max(
						value,
						alphabeta(child, coinsAmount, alpha, beta, false,
								moves + 1));

				if (value > tempBestValue) {
					tempBestValue = value;
					bestChild = child;
				}

				alpha = Math.max(alpha, value);
				if (alpha >= beta) {
					break;
				}
			}
			node.setBestChild(bestChild);
			return value;

		} else {

			int value = Integer.MAX_VALUE;

			// used for tracking the best child node of the currently explored
			// node
			Node bestChild = null;
			int tempBestValue = value;

			for (Node child : node.getChildren()) {
				value = Math.min(
						value,
						alphabeta(child, coinsAmount, alpha, beta, true,
								moves + 1));
				beta = Math.min(beta, value);

				if (value < tempBestValue) {
					tempBestValue = value;
					bestChild = child;
				}
				if (alpha >= beta) {
					break;
				}
			}
			node.setBestChild(bestChild);
			return value;
		}
	}
}
