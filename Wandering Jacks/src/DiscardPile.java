import java.util.Stack;

public class DiscardPile {
	private Stack<Card> discardPile = new Stack<Card>();

	public Card takeCard(){
		if(discardPile.size() <= 0)
			throw new IllegalStateException("No cards to take in the discard pile.");
		return discardPile.pop();
	}

	public void discard(Card c){
		discardPile.push(c);
	}

	public Card peekAtTopCard(){
		return discardPile.peek();
	}

	public Stack<Card> getDiscardPile(){
		return discardPile;
	}

	public void empty(){
		discardPile = new Stack<Card>();
	}

	public boolean isEmpty(){
		return discardPile.isEmpty();
	}
}
