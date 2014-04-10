import java.util.ArrayList;
import java.util.List;


public class Player {
	private int bankroll;
	private List<Card> hand;

	Player(){
		bankroll = 100;
		hand = new ArrayList<Card>();
	}

	public int getBankroll() {
		return bankroll;
	}

	public void setBankroll(int bankroll) {
		this.bankroll = bankroll;
	}

	public List<Card> getHand() {
		return hand;
	}

	public void addToHand(Card c){
		hand.add(c);
	}

	public Card playFromHand(Card c){
		if(hand.contains(c))
			hand.remove(c);
		else throw new NullPointerException("It is illeagal to attempt playing a card that is not in hand.");
		return c;
	}
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
