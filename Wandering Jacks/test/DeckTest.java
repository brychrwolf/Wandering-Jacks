import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DeckTest {

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
		seedDeck.shuffleAllCards();
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
