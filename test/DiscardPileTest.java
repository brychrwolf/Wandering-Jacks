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

	/**
	 * This test ensures that when discarded cards are added to the pile, that
	 * the size of the pile increases,
	 * and the top card becomes the discarded card.
	 */
	@Test
	public void testDiscarding(){
		Card aJoker = new Card(0,0);
		Card aJack = new Card(Card.JACK, Card.CLUBS);
		DiscardPile dp = new DiscardPile();
		//test discarding to the initial pile
		int sizeBefore = dp.size();
		dp.discard(aJoker);
		int sizeAfter = dp.size();
		assertTrue(sizeAfter == sizeBefore + 1);
		assertTrue(dp.peekAtTopCard().equals(aJoker));
		//test taking after adding two cards
		sizeBefore = dp.size();
		dp.discard(aJack);
		dp.discard(aJoker);
		sizeAfter = dp.size();
		assertTrue(sizeAfter == sizeBefore + 2);
		assertTrue(dp.peekAtTopCard().equals(aJoker));
	}

	/**
	 * This test ensures that when peeking at the top card,
	 * the top card is returned
	 * but is not removed
	 * and that an error is thrown when there are no cards
	 */
	@Test
	public void testPeeking(){
		Card aJoker = new Card(0,0);
		Card aJack = new Card(Card.JACK, Card.CLUBS);
		Card cardPeeked;
		DiscardPile dp = new DiscardPile();
		//test peeking at the initial pile
		try{cardPeeked = dp.peekAtTopCard();}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
		//test peeking after adding one card
		dp.discard(aJoker);
		int sizeBefore = dp.size();
		cardPeeked = dp.peekAtTopCard();
		int sizeAfter = dp.size();
		assertTrue(sizeAfter == sizeBefore);
		assertTrue(cardPeeked.equals(aJoker));
		//test taking after adding two cards
		dp.discard(aJack);
		dp.discard(aJoker);
		sizeBefore = dp.size();
		cardPeeked = dp.peekAtTopCard();
		sizeAfter = dp.size();
		assertTrue(sizeAfter == sizeBefore);
		assertTrue(cardPeeked.equals(aJoker));
	}



}
