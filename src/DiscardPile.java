import java.util.Stack;
/**
 * A discard pile is a stack of cards, facing up, typically found next to a
 * Deck of cards (which faces down). A discard pile begins with zero cards, but
 * can grow to any size as more cards are "discarded" (added to the pile).
 * Cards may also be taken from the top, removing it from the pile completely.
 * @author Bryan Wolfford
 * @version %I%, %G%
 * @see Card
 */
public class DiscardPile {
	private Stack<Card> discardPile = new Stack<Card>();

	/**
	 * Returns a single card from the top of the pile.
	 * @return a single card from the top of the pile. Actually removes the
	 * card from the pile so that the card will no longer exist in the pile.
	 * @throws IllegalStateException if there are now cards in the pile to take
	 * @see Card
	 */
	public Card takeTopCard(){
		if(discardPile.size() <= 0)
			throw new IllegalStateException("No cards to take in the discard pile.");
		return discardPile.pop();
	}

	/**
	 * Adds a single card to the top of the pile.
	 * @param c Card that was added to the top of the pile.
	 * @see Card
	 */
	public void discard(Card c){
		discardPile.push(c);
	}

	/**
	 * Returns the card that is on the top of the pile.
	 * @return the card that is on the top of the pile. Does NOT remove card
	 * from the pile.
	 * @See Card
	 */
	public Card peekAtTopCard(){
		return discardPile.peek();
	}

	/**
	 * Returns the entire discard pile.
	 * @return the entire pile of discarded cards.
	 * @see Card
	 */
	public Stack<Card> getDiscardPile(){
		return discardPile;
	}

	/**
	 * Removes every card from the discard pile resulting in an empty pile.
	 */
	public void empty(){
		discardPile = new Stack<Card>();
	}

	/**
	 * Returns true is the discard pile is empty.
	 * @return true if the discard pile is empty, and false otherwise.
	 */
	public boolean isEmpty(){
		return discardPile.isEmpty();
	}

	/**
	 * Returns the size of the discard pile.
	 * @return the size of the discard pile.
	 */
	public int size(){
		return discardPile.size();
	}

}
