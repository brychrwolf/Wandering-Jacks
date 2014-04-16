import static org.junit.Assert.assertTrue;

import java.util.Stack;

import org.junit.Test;


public class DeckTest {
	/**
	 * This test ensures that a deck reports the correct number of cards
	 * <p>
	 * This test relies on dealCard decrementing a card from the deck, and
	 * throwing a IllegalStateException when there are no cards left to deal.
	 */
	@Test
	public void testDeckCardsLeft(){
		Deck seedDeck = new Deck();
		int initialCardsLeft = seedDeck.cardsLeft();
		int countedCards = 0;
		while(true){
			try{
				seedDeck.dealCard();
				countedCards++;
			}catch(IllegalStateException e){
				assertTrue(initialCardsLeft == countedCards);
				break;
			}
		}
	}

	/**
	 * This test ensures that new decks are constructed with either 52
	 * (standard) or 54 (Standard+Jokers) cards
	 */
	@Test
	public void testNewDecksHave52or54CorrectCards(){
		//test standard constructors
		Deck ds1 = new Deck();
		Deck ds2 = new Deck(false);
		assertTrue(ds1.cardsLeft() == 52);
		assertTrue(ds2.cardsLeft() == 52);
		//test standard + jokers constructor
		Deck dj = new Deck(true);
		assertTrue(dj.cardsLeft() == 54);
		//ensure all standard cards exists
		for(int suit = 1; suit <= 4; suit++){
			for(int value = 1; value <= 13; value++){
				assertTrue(ds1.contains(new Card(value, suit)));
				assertTrue(ds2.contains(new Card(value, suit)));
				assertTrue(dj.contains(new Card(value, suit)));
			}
		}
		//ensure jokers exists
		assertTrue(dj.contains(new Card(Card.JOKER, Card.JOKER)));
	}

	/**
	 * This test ensures that shuffling a deck, returns a deck with the same size.
	 * <p>
	 * Test is achieved by recording the deck's size, shuffling the deck,
	 * asserting that the deck size hasn't changed, then popping a card off
	 * the deck and repeating.
	 */
	@Test
	public void testShuffleSizeStaysConstant(){
		Deck seedDeck = new Deck(true);
		int sizeBefore;
		int sizeAfter;
		while(seedDeck.cardsLeft() > 0){
			sizeBefore = seedDeck.cardsLeft();
			seedDeck.shuffle();
			sizeAfter = seedDeck.cardsLeft();
			assertTrue(sizeBefore == sizeAfter);
			seedDeck.dealCard();
		}
	}

	/**
	 * This test ensures that shuffling a deck, returns a deck with the same contents.
	 */
	@Test
	public void testShuffleContentStaysConstant(){
		// get two initial contents
		Deck initialDeck1 = new Deck(true);
		Deck initialDeck2 = new Deck(true);
		// get another copy of initial contents and shuffle it
		Deck shuffledDeck = new Deck(true);
		shuffledDeck.shuffle();
		// assert that all the cards in the initial1 set are in the shuffled set
		while(initialDeck1.cardsLeft() > 0)
			assertTrue(shuffledDeck.contains(initialDeck1.dealCard()));
		// assert that all the cards in the shuffled set are in the initial2 set
		while(shuffledDeck.cardsLeft() > 0)
			assertTrue(initialDeck2.contains(shuffledDeck.dealCard()));
	}

	/**
	 * This test relies on these facts:
	 * <ul>
	 * <li>the Deck constructor always makes the
	 * exact same deck, with the same number of cards, all in the same
	 * order.</li>
	 * <li>the constructed Deck size is greater much than 1</li>
	 * </ul>
	 */
	@Test
	public void testShuffleRandomizesCardOrder(){
		// Store the initial deck in an array
		Deck seedDeck = new Deck(true);
		int deckSize = seedDeck.cardsLeft();
		Card[] initialDeck = new Card[deckSize];
		for(int i = 0; i < deckSize; i++)
			initialDeck[i] = seedDeck.dealCard();
		// Create a new deck, shuffle it, then put that in a new array
		seedDeck = new Deck(true);
		Card[] shuffledDeck = new Card[deckSize];
		seedDeck.shuffle();
		for(int i = 0; i < deckSize; i++)
			shuffledDeck[i] = seedDeck.dealCard();
		// count the number of differences between the two arrays
		int numCardsDifferent = 0;
		for(int i = 0; i < deckSize; i++)
			if(initialDeck[i] != shuffledDeck[i])
				numCardsDifferent++;
		assertTrue(numCardsDifferent / deckSize >= .5);
	}


	/**
	 * This test ensures that shuffling a deck with a stack of other cards,
	 * returns a deck with the correct size.
	 * <p>
	 * Test is achieved by recording the deck's size, shuffling the deck with
	 * two additional jokers, asserting that the deck size hasn't changed, then
	 * popping a card off the deck and repeating.
	 */
	@Test
	public void testShuffleWithOtherCardsSizeStaysConstant(){
		//Make a new deck
		Deck d = new Deck(true);
		//Get a stack of two jokers
		Stack<Card> otherCards = new Stack<Card>();
		otherCards.push(new Card(0,0));
		otherCards.push(new Card(0,0));
		//records of sizes
		int sizeBefore;
		int sizeAfter;
		while(d.cardsLeft() > 0){
			sizeBefore = d.cardsLeft();
			d.shuffleWithOtherCards(otherCards);
			sizeAfter = d.cardsLeft();
			assertTrue(sizeBefore + otherCards.size() == sizeAfter);
			d.dealCard();
			d.dealCard();
			d.dealCard();
		}
	}

	/**
	 * This test ensures that shuffling a deck with a stack of other cards,
	 * returns a deck with the same correct contents.
	 */
	@Test
	public void testShuffleWithOtherCardsContentStaysConstant(){
		// put two new Jokers in a stack of otherCards
		Card aJoker = new Card(0,0);
		Stack<Card> twoJokers = new Stack<Card>();
		twoJokers.push(aJoker);
		twoJokers.push(aJoker);
		// get two initial contents
		Deck initialDeck1 = new Deck(true);
		Deck initialDeck2 = new Deck(true);
		// get another copy of initial contents and shuffle it with otherCards
		Deck shuffledDeck = new Deck(true);
		shuffledDeck.shuffleWithOtherCards(twoJokers);
		// assert that all the cards in the initial set plus two jokers (total 4) are in the shuffled set
		while(initialDeck1.cardsLeft() > 0)
			assertTrue(shuffledDeck.contains(initialDeck1.dealCard()));
		int jokerCount = 0;
		for(int i = 0; i < 4; i++)
			if(shuffledDeck.contains(new Card(Card.JOKER, Card.JOKER)))
				jokerCount++;
		assertTrue(jokerCount == 4);
		// assert that all the cards in the shuffled set are in the initial set or are the four total jokers
		Card c;
		jokerCount = 0;
		while(shuffledDeck.cardsLeft() > 0){
			c = shuffledDeck.dealCard();
			if(c.equals(aJoker))
				jokerCount++;
			assertTrue(initialDeck2.contains(c));
		}
		assertTrue(jokerCount == 4);
	}

	/**
	 * This test relies on these facts:
	 * <ul>
	 * <li>the Deck constructor always makes the
	 * exact same deck, with the same number of cards, all in the same
	 * order.</li>
	 * <li>the constructed Deck size is greater much than 1</li>
	 * </ul>
	 */
	@Test
	public void testShuffleWithOtherCardsRandomizesCardOrder(){
		// Store the initial deck + two more jokers in an array
		Card aJoker = new Card(0,0);
		Deck seedDeck = new Deck(true);
		int initDeckSizePlus2 = seedDeck.cardsLeft() + 2; //+2 to anticipate adding two Jokers
		Card[] initialDeck = new Card[initDeckSizePlus2];
		initialDeck[0] = aJoker;
		initialDeck[1] = aJoker;
		for(int i = 2; i < initDeckSizePlus2; i++)
			initialDeck[i] = seedDeck.dealCard();
		// Create a new deck, shuffle it with two Jokers, then put that in a new array
		seedDeck = new Deck(true);
		Stack<Card> twoJokers = new Stack<Card>();
		twoJokers.push(aJoker);
		twoJokers.push(aJoker);
		Card[] shuffledDeck = new Card[initDeckSizePlus2];
		seedDeck.shuffleWithOtherCards(twoJokers);
		for(int i = 0; i < initDeckSizePlus2; i++)
			shuffledDeck[i] = seedDeck.dealCard();
		// count the number of differences between the two arrays
		int numCardsDifferent = 0;
		for(int i = 0; i < initDeckSizePlus2; i++)
			if(initialDeck[i] != shuffledDeck[i])
				numCardsDifferent++;
		assertTrue(numCardsDifferent / initDeckSizePlus2 >= .5);
	}
}
