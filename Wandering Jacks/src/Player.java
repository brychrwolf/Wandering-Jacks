import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Initia7_B
 *
 */
public class Player {
	/**
	 *
	 */
	private int bankroll;
	/**
	 *
	 */
	private List<Card> hand;

	/**
	 *
	 */
	Player(){
		bankroll = 100;
		hand = new ArrayList<Card>();
	}

	/**
	 *
	 * @return
	 */
	public int getBankroll() {
		return bankroll;
	}

	/**
	 *
	 * @param bankroll
	 */
	public void setBankroll(int bankroll) {
		this.bankroll = bankroll;
	}

	/**
	 *
	 * @return
	 */
	public List<Card> getHand() {
		return hand;
	}

	/**
	 *
	 * @param c
	 */
	public void addToHand(Card c){
		hand.add(c);
	}

	/**
	 *
	 * @param c
	 * @return
	 */
	public Card playFromHand(Card c){
		if(hand.contains(c))
			hand.remove(c);
		else throw new NullPointerException("It is illeagal to attempt playing a card that is not in hand.");
		return c;
	}
	/**
	 *
	 * @param i
	 * @return
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
