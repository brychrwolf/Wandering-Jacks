import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class DiscardPileTest {
	/**
	 * This test ensures that the top card of a discard pile can be taken, that
	 * it is actually the top card,
	 * that the size is decremented by one afterward,
	 * and that an error is thrown if there are no cards
	 */
	@Test
	public void testTakingTopCard() {
		Card aJoker = new Card(0,0);
		Card aJack = new Card(Card.JACK, Card.CLUBS);
		Card cardTaken;
		DiscardPile dp = new DiscardPile();
		//test taking from initial pile
		try{dp.takeTopCard();}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
		//test taking after adding one card
		int sizeBefore = dp.size();
		dp.discard(aJoker);
		cardTaken = dp.takeTopCard();
		int sizeAfter = dp.size();
		assertTrue(sizeAfter == sizeBefore);
		assertTrue(cardTaken.equals(aJoker));
		//test taking after adding two cards
		sizeBefore = dp.size();
		dp.discard(aJack);
		dp.discard(aJoker);
		cardTaken = dp.takeTopCard();
		sizeAfter = dp.size();
		assertTrue(sizeAfter == sizeBefore + 1);
		assertTrue(cardTaken.equals(aJoker));
	}

}
