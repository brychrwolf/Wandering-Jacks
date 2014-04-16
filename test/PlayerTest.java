import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;


public class PlayerTest {

	/**
	 * This test ensures that all players are constructed with an empty hand.
	 */
	@Test
	public void testBeginWithEmptyHand() {
		Player p = new Player();
		assertTrue(p.getHand().size() == 0);
	}

	/**
	 * This test ensures that the player's hand is returned, empty or not.
	 */
	@Test
	public void testGettingHand() {
		Player p = new Player();
		assertTrue(p.getHand() instanceof ArrayList<?>);
		p.addToHand(new Card(0,0));
		assertTrue(p.getHand() instanceof ArrayList<?>);
		p.playFromHand(new Card(0,0));
		assertTrue(p.getHand() instanceof ArrayList<?>);
	}

	/**
	 * This test ensures that contains() returns true if a card with
	 * the same value and suit is found in the deck
	 */
	@Test
	public void testHandContains(){
		Card aJoker = new Card(0,0);
		Player p = new Player();
		//test contains(Card)
		assertFalse(p.handContains(aJoker));
		p.addToHand(aJoker);
		assertTrue(p.handContains(aJoker));
		//test contains(String)
		p = new Player();
		assertFalse(p.handContains("Joker"));
		p.addToHand(aJoker);
		assertTrue(p.handContains("Joker"));
	}


	/**
	 * This test ensures that addToHand(Card) adds the correct card.
	 */
	/*@Test
	public void testAdd(){
		Card aJoker = new Card(0, 0);
		Card aJack = new Card(Card.JACK, Card.CLUBS);
		Player p = new Player();
		//test adding to initial retainer
		int sizeBefore = p.size();
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
	}*/

	/**
	 * This test ensures that size() is accurate
	 */
	@Test
	public void testHandSize(){
		Card aJoker = new Card(0,0);
		Player p = new Player();
		//test initial size is empty
		assertTrue(p.handSize() == 0);
		//test after adding card
		p.addToHand(aJoker);
		assertTrue(p.handSize() == 1);
		//test after removing a card
		p.playFromHand(aJoker);
		assertTrue(p.handSize() == 0);
	}


}
