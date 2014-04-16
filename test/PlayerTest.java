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


}
