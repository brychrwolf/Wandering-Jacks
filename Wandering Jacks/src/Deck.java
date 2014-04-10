import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Deck{
	private Stack<Card> deck = new Stack<Card>();
	private boolean includesJokers;

	public Deck(){
		this(false);
	}

	public Deck(boolean includeJokers){
		if(includeJokers) includesJokers = true;
		else includesJokers = false;

		for(int suit = 0; suit <= 3; suit++){
			for(int value = 1; value <= 13; value++){
				deck.push(new Card(value,suit));
			}
		}
		if(includeJokers){
			deck.push(new Card(Card.JOKER, Card.JOKER));
			deck.push(new Card(Card.JOKER, Card.JOKER));
		}
	}

	public void shuffleAllCards(){
		List<Card> tempDeck = new ArrayList<Card>();
		for(int suit = 0; suit <= 3; suit++){
			for(int value = 1; value <= 13; value++){
				tempDeck.add(new Card(value,suit));
			}
		}
		deck = new Stack<Card>();
		for(int i = tempDeck.size()-1; i > 0; i--){
			int rand = (int)(Math.random()*(i+1));
			deck.push(tempDeck.get(rand));
			tempDeck.remove(rand);
		}
	}

	public void shuffleWithOtherCards(Stack<Card> otherCards){
		Stack<Card> tempDeck = deck;
		for(Card card : otherCards) tempDeck.push(card);
		deck = new Stack<Card>();
		for(int i = tempDeck.size()-1; i > 0; i--){
			int rand = (int)(Math.random()*(i+1));
			deck.push(tempDeck.get(rand));
			tempDeck.remove(rand);
		}
	}

	public int cardsLeft(){
		return deck.size();
	}

	public Card dealCard(){
		if(deck.size() <= 0)
			throw new IllegalStateException("No cards are left in the deck.");
		return deck.pop();
	}

	public boolean hasJokers(){
		return includesJokers;
	}
}