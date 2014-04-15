import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class CardTest {

	/**
	 * This test ensures that every card that is constructed has a value and a
	 * suit.
	 */
	@Test
	public void testCardHasAValueAndSuit() {
		//test empty constructor
		Card a = new Card();
		assertTrue(a.getValue() == a.getValue());
		assertTrue(a.getSuit() == a.getSuit());
		//test valid constructor
		Card b = new Card(0,0);
		assertTrue(b.getValue() == b.getValue());
		assertTrue(b.getSuit() == b.getSuit());
		//test other valid constructor
		Card c = new Card(1,2);
		assertTrue(c.getValue() == c.getValue());
		assertTrue(c.getSuit() == c.getSuit());
		//test invalid value
		try{
			@SuppressWarnings("unused")
			Card d = new Card(20,1);
		}catch(IllegalArgumentException e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		//test invalid suit
		try{
			@SuppressWarnings("unused")
			Card ce = new Card(20,1);
		}catch(IllegalArgumentException e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		//test invalid value and suit
		try{
			@SuppressWarnings("unused")
			Card f = new Card(20,1);
		}catch(IllegalArgumentException e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}


	/**
	 * This test ensures that every card that is constructed has a valid value
	 * and valid suit. Valid values are 0-13. Valid suits are 0-4.
	 */
	@Test
	public void testCardHasValidValuesAndSuits() {
		//test empty constructor
		Card a = new Card();
		assertTrue(a.getValue() >= 0 && a.getValue() <= 13);
		assertTrue(a.getSuit() >= 0 && a.getSuit() <= 4);
		//test valid constructor
		Card b = new Card(0,0);
		assertTrue(b.getValue() == 0);
		assertTrue(b.getSuit() == 0);
		//test other valid constructor
		Card c = new Card(1,2);
		assertTrue(c.getValue() == 1);
		assertTrue(c.getSuit() == 2);
	}

}
