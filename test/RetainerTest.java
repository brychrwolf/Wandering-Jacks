import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class RetainerTest {
	/**
	 * This test ensures that add(Card) adds the correct card to the retainer.
	 */
	@Test
	public void testRetainerAdd(){
		Card aJoker = new Card(0, 0);
		Card aJack = new Card(Card.JACK, Card.CLUBS);
		Retainer retainer = new Retainer();
		//test adding to initial retainer
		int sizeBefore = retainer.size();
		retainer.add(aJoker);
		int sizeAfter = retainer.size();
		assertTrue(sizeBefore + 1 == sizeAfter);
		//test adding to retainer with same card
		sizeBefore = retainer.size();
		retainer.add(aJoker);
		sizeAfter = retainer.size();
		assertTrue(sizeBefore + 1 == sizeAfter);
		//test adding to retainer with different cards
		sizeBefore = retainer.size();
		retainer.add(aJack);
		sizeAfter = retainer.size();
		assertTrue(sizeBefore + 1 == sizeAfter);
	}

	/**
	 * This test ensures that remove(Card) removes the correct card from the
	 * retainer, and throws an error if there are no cards or if that card
	 * didn't exist in the retainer.
	 */
	@Test
	public void testRetainerRemove(){
		Card aJoker = new Card(0, 0);
		Card aJack = new Card(Card.JACK, Card.CLUBS);
		Retainer retainer = new Retainer();
		//test removing from initial retainer
		try{retainer.remove(aJoker);}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
		//test removing after adding that card
		retainer = new Retainer();
		retainer.add(aJoker);
		assertTrue(retainer.remove(aJoker).equals(aJoker));
		//test removing after adding a different card
		retainer = new Retainer();
		retainer.add(aJack);
		try{retainer.remove(aJoker);}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
		//test removing after adding two cards, one different
		retainer = new Retainer();
		retainer.add(aJoker);
		retainer.add(aJack);
		assertTrue(retainer.remove(aJoker).equals(aJoker));
		//test removing after adding two cards, one different (differnt order)
		retainer = new Retainer();
		retainer.add(aJack);
		retainer.add(aJoker);
		assertTrue(retainer.remove(aJoker).equals(aJoker));
		//test removing after adding, then emptying
		retainer = new Retainer();
		retainer.add(aJoker);
		retainer.empty();
		try{retainer.remove(aJoker);}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
	}

	/**
	 * This test ensures that Retainer.empty() reports true when there are
	 * no cards in the retainer, and never at any other time
	 */
	@Test
	public void testRetainerEmpty(){
		Card aJoker = new Card(0,0);
		Retainer retainer = new Retainer();
		//test initial size is empty
		assertTrue(retainer.isEmpty());
		//test after adding card
		retainer.add(aJoker);
		assertFalse(retainer.isEmpty());
		//test after removing a card
		retainer.remove(aJoker);
		assertTrue(retainer.isEmpty());
		//test after emptying a register with cards in it
		retainer.add(aJoker);
		retainer.empty();
		assertTrue(retainer.isEmpty());
		//test after emptying a empty register
		retainer.empty();
		assertTrue(retainer.isEmpty());
	}


	/**
	 * This test ensures that Retainer.size() is accurate
	 */
	@Test
	public void testRetainerSize(){
		Card aJoker = new Card(0,0);
		Retainer retainer = new Retainer();
		//test initial size is empty
		assertTrue(retainer.size() == 0);
		//test after adding card
		retainer.add(aJoker);
		assertTrue(retainer.size() == 1);
		//test after removing a card
		retainer.remove(aJoker);
		assertTrue(retainer.size() == 0);
		//test after emptying a register with cards in it
		retainer.add(aJoker);
		retainer.empty();
		assertTrue(retainer.size() == 0);
		//test after emptying a empty register
		retainer.empty();
		assertTrue(retainer.size() == 0);
	}

	/**
	 * This test ensures that Retainer.contains() returns true if a card with
	 * the same value and suit is found in the deck
	 */
	@Test
	public void testRetainerContains(){
		Card aJoker = new Card(0,0);
		//test contains(Card)
		Retainer retainer = new Retainer();
		assertFalse(retainer.contains(aJoker));
		retainer.add(aJoker);
		assertTrue(retainer.contains(aJoker));
		//test contains(String)
		retainer = new Retainer();
		assertFalse(retainer.contains("Joker"));
		retainer.add(aJoker);
		assertTrue(retainer.contains("Joker"));
	}

	/**
	 * This tests if retainsJack() returns true if this retainer retains a Jack,
	 * false if otherwise.
	 */
	@Test
	public void testRetainsJack(){
		Card sJack = new Card(Card.JACK, Card.SPADES);
		Card cJack = new Card(Card.JACK, Card.CLUBS);
		Card dJack = new Card(Card.JACK, Card.DIAMONDS);
		Card hJack = new Card(Card.JACK, Card.HEARTS);
		//test no Jack
		Retainer retainer = new Retainer();
		assertFalse(retainer.retainsJack());
		//test with Jack of Spades
		retainer.add(sJack);
		assertTrue(retainer.retainsJack());
		//test with Jack of CLUBS
		retainer = new Retainer();
		retainer.add(cJack);
		assertTrue(retainer.retainsJack());
		//test with Jack of DIAMONDS
		retainer = new Retainer();
		retainer.add(dJack);
		assertTrue(retainer.retainsJack());
		//test with Jack of HEARTS
		retainer = new Retainer();
		retainer.add(hJack);
		assertTrue(retainer.retainsJack());
		//test after Jack is removed
		retainer.remove(hJack);
		assertFalse(retainer.retainsJack());
		//test after retainer is emptied
		retainer = new Retainer();
		retainer.add(hJack);
		retainer.empty();
		assertFalse(retainer.retainsJack());

	}
}
