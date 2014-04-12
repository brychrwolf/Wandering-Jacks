import java.util.Stack;
/**
 *
 * @author Initia7_B
 *
 */
public class DiscardPile {
	/**
	 *
	 */
	private Stack<Card> discardPile = new Stack<Card>();

	/**
	 *
	 * @return
	 */
	public Card takeCard(){
		if(discardPile.size() <= 0)
			throw new IllegalStateException("No cards to take in the discard pile.");
		return discardPile.pop();
	}

	/**
	 *
	 * @param c
	 */
	public void discard(Card c){
		discardPile.push(c);
	}

	/**
	 *
	 * @return
	 */
	public Card peekAtTopCard(){
		return discardPile.peek();
	}

	/**
	 *
	 * @return
	 */
	public Stack<Card> getDiscardPile(){
		return discardPile;
	}

	/**
	 *
	 */
	public void empty(){
		discardPile = new Stack<Card>();
	}

	/**
	 *
	 * @return
	 */
	public boolean isEmpty(){
		return discardPile.isEmpty();
	}
}
