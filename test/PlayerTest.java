import static org.junit.Assert.assertTrue;

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

}
