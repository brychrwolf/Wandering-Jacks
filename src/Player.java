import java.util.ArrayList;
import java.util.List;

/**
 * A Player is a person playing a card game, that has secret cards in their
 * "hand" and a "bankroll" of money from which they may make bets.
 * @author Bryan Wolfford
 * @version %I%, %G%
 * @see Card
 */
public class Player {
	private int bankroll;
	private List<Card> hand;

	/**
	 * Creates a player with an empty hand, and a bankroll of 100.
	 */
	Player(){
		bankroll = 100;
		hand = new ArrayList<Card>();
	}

	/**
	 * Returns the player's current bankroll.
	 * @return the integer value of the player's bankroll.
	 */
	public int getBankroll() {
		return bankroll;
	}

	/**
	 * Sets the Players bankroll to the given value.
	 * @param bankroll Integer value to which the Player's bankroll is set.
	 */
	public void setBankroll(int bankroll) {
		this.bankroll = bankroll;
	}

	/**
	 * Returns the list of cards held in the Player's hand.
	 * @return the entire List<Card> of the Player's hand.
	 * @see Card
	 */
	public List<Card> getHand(){
		return hand;
	}

	/**
	 * Returns true only if the target card is found within the hand.
	 * @param target is the card that is searched for in the hand.
	 * @return true only if the target card is found within the hand, and
	 * false if otherwise.
	 * @see Card
	 */
	public boolean handContains(Card target){
		boolean found = false;
		for(Card card : hand)
			if(card.equals(target))
				found = true;
		return found;
	}

	/**
	 * Returns true only if any of the cards in the hand have a value that
	 * matches the given target value string.
	 * @param targetValue the string to search for among all the cards' values
	 * in the hand
	 * @return true only if the target value is found within the hand, and
	 * false if otherwise.
	 * @see Card
	 */
	public boolean handContains(String targetValue){
		boolean found = false;
		for(Card card : hand)
			if(card.getValueAsString() == targetValue)
				found = true;
		return found;
	}

	/**
	 * Adds a card to the secret list of cards in the Player's hand.
	 * @param c the card to be added to the Player's hand.
	 * @see Card
	 */
	public void addToHand(Card c){
		hand.add(c);
	}

	/**
	 * Returns the size of the hand.
	 */
	public int handSize(){
		return hand.size();
	}

	/**
	 * Removes and returns the given card from the Player's hand, if it exists.
	 * @param c the card to be removed and returned.
	 * @return the card that was removed from the Player's hand (if it exists).
	 * @throws NullPointerException if the card does not exists in hand.
	 * @see Card
	 */
	public Card playFromHand(Card c){
		if(this.handContains(c))
			hand.remove(c);
		else throw new IllegalStateException("There is no "+c.toString()+ " to play from hand.");
		return c;
	}

	/**
	 * Removes and returns the card at the given index from the Player's hand,
	 * if it exists.
	 * @param i the index of the card to be removed and returned.
	 * @return the card that was removed from the Player's hand (if it exists).
	 * @throws IndexOutOfBoundsException if the index does not exist.
	 * @see Card
	 */
	public Card playFromHand(int i){
		Card c;
		try{
			c = hand.get(i);
			hand.remove(i);
		}catch(IndexOutOfBoundsException e){
			System.err.println("IndexOutOfBoundsException: " + e.getMessage());
			throw new IndexOutOfBoundsException("Index "+i+" is not within bounds: "+hand.size());
		}
		return c;
	}
}
