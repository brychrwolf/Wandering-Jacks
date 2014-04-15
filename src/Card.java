/**
 * A Card is one of the 53 unique cards found in a standard deck of 54 cards.
 * It has a suit and a value, (for Jokers, both are set to Joker).
 * <ul>
 *  <li>Suits
 *   <ul>
 * 	  <li>Spades</li>
 * 	  <li>Hearts</li>
 * 	  <li>Diamonds</li>
 * 	  <li>Clubs</li>
 * 	  <li>Jokers</li>
 *   </ul>
 * 	</li>
 * 	<li>Values
 * 	 <ul>
 * 	  <li>Aces</li>
 * 	  <li>2s-10s</li>
 * 	  <li>Jacks</li>
 * 	  <li>Queens</li>
 * 	  <li>Kings</li>
 * 	  <li>Jokers</li>
 *   </ul>
 * 	</li>
 * </ul>
 *
 * @author Bryan Wolfford
 * @version %I%, %G%
 */
public class Card {
	/**
	 * An arbitrary integer code for the suit and value of Jokers.
	 */
   public final static int JOKER = 0;   // Codes for the 4 suits, plus Joker.
   /**
    * An arbitrary integer code for the suit, Hearts.
    */
   public final static int HEARTS = 1;
   /**
    * An arbitrary integer code for the suit, Diamonds.
    */
   public final static int DIAMONDS = 2;
   /**
    * An arbitrary integer code for the suit, Clubs.
    */
   public final static int CLUBS = 3;
   /**
    * An arbitrary integer code for the suit, Spades.
    */
   public final static int SPADES = 4;

   /**
    * An integer code for the value of Aces.
    */
   public final static int ACE = 1;      // Codes for the non-numeric cards.
   /**
    * An integer code for the value of Jacks.
    */
   public final static int JACK = 11;    //   Cards 2 through 10 have their
   /**
    * An integer code for the value of Queens.
    */
   public final static int QUEEN = 12;   //   numerical values for their codes.
   /**
    * An integer code for the value of Kings.
    */
   public final static int KING = 13;

   /**
    * This card's suit, one of the constants SPADES, HEARTS, DIAMONDS,
    * CLUBS, or JOKER.  The suit cannot be changed after the card is
    * constructed.
    */
   private final int suit;

   /**
    * The card's value.  For a normal card, this is one of the values
    * 1 through 13, with 1 representing ACE.  For a JOKER, the value
    * can be anything.  The value cannot be changed after the card
    * is constructed.
    */
   private final int value;

   /**
    * Creates a Joker, with 1 as the associated value.  (Note that
    * "new Card()" is equivalent to "new Card(1,Card.JOKER)".)
    */
   public Card() {
      suit = JOKER;
      value = JOKER;
   }

   /**
    * Creates a card with a specified suit and value.
    * @param value the value of the new card.  For a regular card,
    * the value must be in the range 0 through 13, with 1 representing an Ace,
    * and 0 representing a joker.
    * You can use the constants Card.ACE, Card.JACK, Card.QUEEN, Card.KING, and
    * Card.JOKER.
    * @param suit the suit of the new card.  This must be one of the values
    * Card.SPADES, Card.HEARTS, Card.DIAMONDS, Card.CLUBS, or Card.JOKER.
    * @throws IllegalArgumentException if the parameter values are not in the
    * permissible ranges or if value Joker unless it also has a suit of Joker,
    * and vice versa
    */
   public Card(int value, int suit) {
      if(suit != JOKER && suit != SPADES && suit != HEARTS && suit != DIAMONDS && suit != CLUBS)
         throw new IllegalArgumentException("Illegal playing card suit");
      if(value != JOKER && value != ACE && value != JACK && value != QUEEN && value != KING && (value < 2 || value > 10))
         throw new IllegalArgumentException("Illegal playing card value");
      if((value == JOKER && suit != JOKER) || (value != JOKER && suit == JOKER))
          throw new IllegalArgumentException("Illegal playing card value");

      this.value = value;
      this.suit = suit;
   }

   /**
    * Returns the suit of this card.
    * @returns the suit, which is one of the constants Card.SPADES,
    * Card.HEARTS, Card.DIAMONDS, Card.CLUBS, or Card.JOKER
    */
   public int getSuit() {
      return suit;
   }

   /**
    * Returns the value of this card.
    * @return the value, which is one of the numbers 1 through 13, inclusive for
    * a regular card, and which can be any value for a Joker.
    */
   public int getValue() {
      return value;
   }

   /**
    * Returns a String representation of the card's suit.
    * @return one of the strings "Spades", "Hearts", "Diamonds", "Clubs"
    * or "Joker".
    * @throws IllegalStateException is an invalid suit exists
    */
   public String getSuitAsString(){
      switch(suit){
      	case SPADES:   return "Spades";
      	case HEARTS:   return "Hearts";
      	case DIAMONDS: return "Diamonds";
      	case CLUBS:    return "Clubs";
      	case JOKER:    return "Joker";
      	default:
      		throw new IllegalStateException(suit+" is not a valid suit.");
      }
   }

   /**
    * Returns a String representation of the card's value.
    * @return for a regular card, one of the strings "Ace", "2",
    * "3", ..., "10", "Jack", "Queen", or "King".  For a Joker, the
    * string is always numerical.
    * @throws IllegalStateException is an invalid value exists
    */
   public String getValueAsString() {
     switch(value){
	     case 0:   return "Joker";
	     case 1:   return "Ace";
	     case 2:   return "2";
	     case 3:   return "3";
	     case 4:   return "4";
	     case 5:   return "5";
	     case 6:   return "6";
	     case 7:   return "7";
	     case 8:   return "8";
	     case 9:   return "9";
	     case 10:  return "10";
	     case 11:  return "Jack";
	     case 12:  return "Queen";
	     case 13:  return "King";
	     default:
      		throw new IllegalStateException(value+" is not a valid value.");
     }
   }

   /**
    * Returns a string representation of this card
    * @return a string representation of this card including both its suit and
    * its value (except that for a Joker which will return just "Joker").
    * Sample return values are: "Queen of Hearts", "10 of Diamonds",
    * "Ace of Spades", "Joker"
    */
   public String toString(){
      if(suit == JOKER) return "Joker";
      else return getValueAsString() + " of " + getSuitAsString();
   }

   /**
    * Returns true if the card value and suit are the same as this
    * @param card
    * @return true if the card value and suit are the same as this
    */
   public Boolean equals(Card card){
	   if(this.value == card.getValue() && this.suit == card.getSuit())
		   return true;
	   else return false;
   }
}