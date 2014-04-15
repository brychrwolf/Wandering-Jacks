import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DeckTest {
	@Test
	public void shuffleSizeTest(){
		/**
		 * This test ensures that shuffling a deck, returns a deck with the same size.
		 * <p>
		 * Test is achieved by recording the deck's size, shuffling the deck,
		 * asserting that the deck size hasn't changed, then popping a card off
		 * the deck repeating.
		 */
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

	@Test
	public void shuffleContentsTest(){
		/**
		 * This test ensures that shuffling a deck, returns a deck with the same contents.
		 * <p>
		 * Test is achieved by recording the deck's size, shuffling the deck,
		 * asserting that the deck size hasn't changed, then popping a card off
		 * the deck repeating.
		 */
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

	@Test
	public void shuffleTest() {
		/**
		 * This test relies on these facts:
		 * <ul>
		 * <li>the Deck constructor always makes the
		 * exact same deck, with the same number of cards, all in the same
		 * order.</li>
		 * <li>the constructed Deck size is greater much than 1</li>
		 * </ul>
		 */
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

}
