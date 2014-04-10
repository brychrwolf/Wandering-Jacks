import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *  An object of type Deck represents a deck of playing cards.  The deck
 *  is a regular poker deck that contains 52 regular cards and that can
 *  also optionally include two Jokers.
 */
public class Old_Deck {

   /**
    * An array of 52 or 54 cards.  A 54-card deck contains two Jokers,
    * in addition to the 52 cards of a regular poker deck.
    */
   private Stack<Card> deck = new Stack<Card>();

   /**
    * Keeps track of if the deck uses Jokers
    */
   private boolean includesJokers;

   /**
    * Constructs a regular 52-card poker deck.  Initially, the cards
    * are in a sorted order.  The shuffle() method can be called to
    * randomize the order.  (Note that "new Deck()" is equivalent
    * to "new Deck(false)".)
    */
   public Old_Deck() {
      this(false);  // Just call the other constructor in this class.
   }

   /**
    * Constructs a poker deck of playing cards, The deck contains
    * the usual 52 cards and can optionally contain two Jokers
    * in addition, for a total of 54 cards.   Initially the cards
    * are in a sorted order.  The shuffle() method can be called to
    * randomize the order.
    * @param includeJokers if true, two Jokers are included in the deck; if false,
    * there are no Jokers in the deck.
    */
   public Old_Deck(boolean includeJokers) {
      if(includeJokers) includesJokers = true;
      else includesJokers = false;

      for(int suit = 0; suit <= 3; suit++){
         for(int value = 1; value <= 13; value++){
            deck.push(new Card(value,suit));
         }
      }
      if(includeJokers){
    	  deck.push(new Card(0, Card.JOKER));
    	  deck.push(new Card(0, Card.JOKER));
      }
   }

   /**
    * Put all the used cards back into the deck (if any), and
    * shuffle the deck into a random order.
    */
   public void shuffleAll(){
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

   /**
    * Put all only the discard cards back into the deck (if any), and
    * shuffle the deck into a random order.
    */
   public void shuffleCardsIntoDeck(Stack<Card> discard_stack){
	   Stack<Card> tempDeck = deck;
	   for(Card card : discard_stack) tempDeck.push(card);
	   deck = new Stack<Card>();
	   for(int i = tempDeck.size()-1; i > 0; i--){
		   int rand = (int)(Math.random()*(i+1));
		   deck.push(tempDeck.get(rand));
		   tempDeck.remove(rand);
	   }
   }

   /**
    * As cards are dealt from the deck, the number of cards left
    * decreases.  This function returns the number of cards that
    * are still left in the deck.  The return value would be
    * 52 or 54 (depending on whether the deck includes Jokers)
    * when the deck is first created or after the deck has been
    * shuffled.  It decreases by 1 each time the dealCard() method
    * is called.
    */
   public int cardsLeft() {
      return deck.size();
   }

   /**
    * Removes the next card from the deck and return it.  It is illegal
    * to call this method if there are no more cards in the deck.  You can
    * check the number of cards remaining by calling the cardsLeft() function.
    * @return the card which is removed from the deck.
    * @throws IllegalStateException if there are no cards left in the deck
    */
   public Card dealCard() {
      if (deck.size() <= 0)
         throw new IllegalStateException("No cards are left in the deck.");
      return deck.pop();
   }

   /**
    * Check whether the deck contains Jokers.
    * @return true, if this is a 54-card deck containing two jokers, or false if
    * this is a 52 card deck that contains no jokers.
    */
   public boolean hasJokers() {
      return includesJokers;
   }

} // end class Deck