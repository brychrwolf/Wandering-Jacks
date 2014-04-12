import java.util.ArrayList;
import java.util.List;

/**
 * A Player is a person playing a card game, that has secret cards in their
 * "hand" and a "bankroll" of money from which they may make bets.
 * @author Bryan Wolfford
 * @version %I%, %G%
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
	 */
	public List<Card> getHand() {
		return hand;
	}

	/**
	 * Adds a card to the secret list of cards in the Player's hand.
	 * @param c the card to be added to the Player's hand.
	 */
	public void addToHand(Card c){
		hand.add(c);
	}

	/**
	 * Removes and returns the given card from the Player's hand, if it exists.
	 * @param c the card to be removed and returned.
	 * @return the card that was removed from the Player's hand (if it exists).
	 * @throws NullPointerException if the card does not exists in hand.
	 */
	public Card playFromHand(Card c){
		if(hand.contains(c))
			hand.remove(c);
		else throw new NullPointerException("It is illeagal to attempt playing a card that is not in hand.");
		return c;
	}
	/**
	 * Removes and returns the card at the given index from the Player's hand,
	 * if it exists.
	 * @param i the index of the card to be removed and returned.
	 * @return the card that was removed from the Player's hand (if it exists).
	 * @throws IndexOutOfBoundsException if the index does not exist.
	 */
	public Card playFromHand(int i){
		Card c;
		try{
			c = hand.get(i);
			hand.remove(i);
		}catch(IndexOutOfBoundsException e){
			System.err.println("IndexOutOfBoundsException: " + e.getMessage());
			throw new IndexOutOfBoundsException("It is illeagal to attempt playing a card that is not in hand.");
		}
		return c;
	}
}
