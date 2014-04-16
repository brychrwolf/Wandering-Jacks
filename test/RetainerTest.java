import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class RetainerTest {

	/**
	 * This test ensures that Retainer.contains() returns true if a card with
	 * the same value and suit is found in the deck
	 */
	@Test
	public void testRetainerContains(){
		Card aJoker = new Card(0,0);
		Retainer retainer = new Retainer();
		assertFalse(retainer.contains(aJoker));
		retainer.add(aJoker);
		assertTrue(retainer.contains(aJoker));
	}



}
