import java.util.List;
import java.util.Stack;
/**
 * A Deck is a stack of cards, which begins with 52 standard cards or 54 if
 * Jokers are included. A deck may be drawn from (cards removed), added to,
 * or shuffled (card order randomized).
 * @author Bryan Wolfford
 * @version %I%, %G%
 * @see Card
 */
public class Deck{
	private Stack<Card> deck = new Stack<Card>();

	/**
	 * Alias of Deck(false);
	 */
	public Deck(){
		this(false);
	}

	/**
	 * Creates a standard 52 or 54 card deck (without or with Jokers).
	 * @param includeJokers True or False, whether to include or not include
	 * Jokers in the deck.
	 * @see Card
	 */
	public Deck(boolean includeJokers){

		for(int suit = 1; suit <= 4; suit++){
			for(int value = 1; value <= 13; value++){
				deck.push(new Card(value,suit));
			}
		}
		if(includeJokers){
			deck.push(new Card(Card.JOKER, Card.JOKER));
			deck.push(new Card(Card.JOKER, Card.JOKER));
		}
	}

	Deck(Deck cloned){
		for(int i = 0; i < cloned.deck.size(); i++)
			this.deck.push(new Card(cloned.deck.get(i)));
	}

	/**
	 * Randomize the order of (shuffle) the current deck, maintaining the
	 * current size and contents.
	 * @see Card
	 */
	public void shuffle(){
		List<Card> tempDeck = deck;
		deck = new Stack<Card>();
		for(int i = tempDeck.size()-1; i >= 0; i--){
			int rand = (int)(Math.random()*(i+1));
			deck.push(tempDeck.get(rand));
			tempDeck.remove(rand);
		}
	}

	/**
	 * Randomize the order of (shuffle) the deck, after adding a given stack of
	 * other cards to add to the deck.
	 * @param otherCards stack of cards to add to the deck before shuffling.
	 * @see Card
	 */
	public void shuffleWithOtherCards(Stack<Card> otherCards){
		Stack<Card> tempDeck = deck;
		for(Card card : otherCards)
			tempDeck.push(card);
		deck = new Stack<Card>();
		for(int i = tempDeck.size()-1; i >= 0; i--){
			int rand = (int)(Math.random()*(i+1));
			deck.push(tempDeck.get(rand));
			tempDeck.remove(rand);
		}
	}

	/**
	 * Returns the number of cards remaining in the deck.
	 * @return the number of cards remaining in the deck, will be either 52 or
	 * 54 for a new deck and 0 for an empty deck, but may grow larger if more
	 * cards have been shuffled into this deck.
	 */
	public int cardsLeft(){
		return deck.size();
	}

	/**
	 * Returns a single card, removing it from the deck.
	 * @return a single card from the deck. Actually removes the card from the
	 * deck so that the card will no longer exist in the deck and the deck size
	 * will decrement by 1.
	 * @throws IllegalStateException if there are now cards in the deck to give
	 * @see Card
	 */
	public Card dealCard(){
		if(this.cardsLeft() <= 0)
			throw new IllegalStateException("No cards are left in the deck.");
		return deck.pop();
	}


   /**
    * Returns true if a card with the same value and suit is found in the deck
    * @param card the card to search for
    * @return true if a card with the same value and suit is found in the deck,
    * uses the overloaded Card.equals() method for comparison.
    */
   public Boolean contains(Card card){
	   for(int i = 0; i < deck.size(); i++){
		   if(card.equals(deck.elementAt(i)))
			   return true;
	   }
	   return false;
   }
}