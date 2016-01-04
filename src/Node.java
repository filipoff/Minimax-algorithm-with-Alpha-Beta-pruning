import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Node {

	// the current state of the game is represented by
	// array of 1s and 0, where 1 means there is a coin at this position
	// and 0 means the coin is taken at this position

	private int[] state;

	// the child of the current node that has the best minimum or maximum score
	private Node bestChild;

	// Constructor that takes integer array for state of the node

	public Node(int[] state) {
		this.state = state;
		this.bestChild = null;
	}

	public Node getBestChild() {
		return bestChild;
	}

	public void setBestChild(Node bestChild) {
		this.bestChild = bestChild;
	}

	// state getter
	public int[] getState() {
		return state;
	}

	// generates a list of the children nodes of the current node
	public List<Node> getChildren() {

		// using set to ban adding duplicated child nodes
		Set<Node> children = new LinkedHashSet<Node>();

		// can be optimized for state length less than 3
		// but still works this way
		for (int i = 0, j = i + 1, k = i + 2; i < this.state.length; i++, j++, k++) {

			// if this index goes out of bounds it is reset to
			// the first index, because the first and the last coins of the
			// state are neighbors
			if (k >= this.state.length) {
				k = 0;
			}

			// if this index goes out of bounds it is reset to
			// the first index, because the first and the last coins of the
			// state are neighbors
			if (j >= this.state.length) {
				j = 0;
			}

			// generate child which has 1 coin taken from the current node
			// if it has a coin at the current index of the state
			if (this.state[i] == 1) {

				// make a copy of the current node
				Node child = new Node(Arrays.copyOf(this.state,
						this.state.length));

				// set the value at the current index of the child state to 0
				// which means the coin has been taken
				child.getState()[i] = 0;
				children.add(child);
			}

			// generate child which has 2 coins taken from the current node
			// if it has 2 neighbor coins at the current and next indices of the
			// state
			if (this.state[i] == 1 && this.state[j] == 1) {

				// make a copy of the current node
				Node child = new Node(Arrays.copyOf(this.state,
						this.state.length));

				// set the values at the current and the next indices of the
				// child state to 0
				// which means 2 neighbor coins has been taken
				child.getState()[i] = 0;
				child.getState()[j] = 0;
				children.add(child);
			}

			// generate child which has 3 coins taken from the current node
			// if it has 3 neighbor coins at the current and the 2 next indices
			// of the state
			if (this.state[i] == 1 && this.state[j] == 1 && this.state[k] == 1) {

				// make a copy of the current node
				Node child = new Node(Arrays.copyOf(this.state,
						this.state.length));

				// set the values at the current and next 2 indices of the child
				// state to 0
				// which means 3 neighbor coins has been taken
				child.getState()[i] = 0;
				child.getState()[j] = 0;
				child.getState()[k] = 0;
				children.add(child);
			}
		}

		// convert the set to list and return it
		List<Node> result = new ArrayList<>();
		result.addAll(children);
		return result;
	}

	// checks if this node is a terminal node
	// which happens when all coins of the current state have been taken
	// thus all integers in the state are 0s
	public boolean isTerminal() {

		for (int value : this.state) {

			if (value == 1) {
				return false;
			}
		}
		return true;
	}

	// calculates how many coins been taken from the current
	// state to transit to the best child's state
	public int calculateBestChildCoinsDifference() {

		int result = 0;

		for (int i = 0; i < this.state.length; i++) {

			// subtract the integer from the child state from the
			// current state at given index i
			// and this difference gives us how many coins have been taken
			result -= (this.bestChild.getState()[i] - this.state[i]);
		}

		// return the coin difference
		return result;
	}

	// calculates the first index of which the coins have been taken
	// from the current state to the best child state
	public int calculateBestChildCoinsTakenIndex() {

		// get how many coins have been taken
		// this is pre-calculated earlier in the alpha-beta procedure
		// it should not have default value
		int count = calculateBestChildCoinsDifference();

		for (int i = 0, j = i + 1, k = j + 1; i < this.state.length; i++, j++, k++) {

			// if this index goes out of bounds it is reset to
			// the first index, because the first and the last coins of the
			// state are neighbors
			if (k >= this.state.length) {
				k = 0;
			}

			// if this index goes out of bounds it is reset to
			// the first index, because the first and the last coins of the
			// state are neighbors
			if (j >= this.state.length) {
				j = 0;
			}

			// if the amount of taken coins in 1
			if (count == 1) {

				if (this.state[i] == 1 && this.bestChild.getState()[i] == 0) {
					// get the first index at which there is 1 in the current
					// state and 0 in the child state
					return i;
				}
			}

			// if the amount of taken coins in 2
			if (count == 2) {

				if (this.state[i] == 1 && this.bestChild.getState()[i] == 0
						&& this.state[j] == 1
						&& this.bestChild.getState()[j] == 0) {

					// get the first index at which there are 2 neighbor 1s in
					// the current state and there are 2 neighbor 0s in the
					// child state
					return i;
				}

			}

			// if the amount of taken coins in 2
			if (count == 3) {

				if (this.state[i] == 1 && this.bestChild.getState()[i] == 0
						&& this.state[j] == 1
						&& this.bestChild.getState()[j] == 0
						&& this.state[k] == 1
						&& this.bestChild.getState()[k] == 0) {

					// get the first index at which there are 3 neighbor 1s in
					// the current state and there are 3 neighbor 0s in the
					// child state
					return i;
				}
			}
		}
		// something went wrong
		// shouldn't happen
		return -1;
	}

	@Override
	public String toString() {
		return Arrays.toString(this.state);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(state);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		if (!Arrays.equals(state, other.state))
			return false;
		return true;
	}
}
