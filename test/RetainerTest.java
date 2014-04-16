import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class RetainerTest {
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
		retainer.remove(hJack);
		retainer.empty();
		assertFalse(retainer.retainsJack());

	}
}
