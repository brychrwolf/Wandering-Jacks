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
public class DiscardPile{
	private Stack<Card> discardPile = new Stack<Card>();

	public DiscardPile() {
		// TODO Auto-generated constructor stub
	}

	DiscardPile(DiscardPile cloned){
		for(int i = 0; i < cloned.discardPile.size(); i++)
			this.discardPile.push(new Card(cloned.discardPile.get(i)));
	}

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
	 * @throws IllegalStateException if there are no cards to peek
	 * @See Card
	 */
	public Card peekAtTopCard(){
		if(discardPile.size() <= 0)
			throw new IllegalStateException("No cards to peek at in the discard pile.");
		return discardPile.peek();
	}

	/**
	 * Returns and removes every card from the discard pile.
	 * @return every card that was in the pile
	 * @throws IllegalStateException if there are no cards to return/empty
	 */
	public Stack<Card> takeAllCards(){
		if(discardPile.size() <= 0)
			throw new IllegalStateException("No cards to return/empty in the discard pile.");
		Stack<Card> tempPile = discardPile;
		discardPile = new Stack<Card>();
		return tempPile;
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
