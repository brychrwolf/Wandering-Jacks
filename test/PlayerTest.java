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


}
