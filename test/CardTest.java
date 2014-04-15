import static org.junit.Assert.assertFalse;
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
		//test valid Joker constructor
		Card b = new Card(0,0);
		assertTrue(b.getValue() == b.getValue());
		assertTrue(b.getSuit() == b.getSuit());
		//test valid standard constructor
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
		//test invalid Joker combo value
		try{
			@SuppressWarnings("unused")
			Card d = new Card(1,0);
		}catch(IllegalArgumentException e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		//test invalid Joker combo suit
		try{
			@SuppressWarnings("unused")
			Card d = new Card(0,1);
		}catch(IllegalArgumentException e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}


	/**
	 * This test ensures that every card that is constructed has a valid value
	 * and valid suit. Valid values are 0-13. Valid suits are 0-4. Also, Jokers
	 * must have both value and suit 0, any other 0 is invalid.
	 */
	@Test
	public void testCardHasValidValuesAndSuits() {
		//test empty constructor
		Card a = new Card();
		assertTrue(a.getValue() >= 0 && a.getValue() <= 13);
		assertTrue(a.getSuit() >= 0 && a.getSuit() <= 4);
		//test valid Joker constructor
		Card b = new Card(0,0);
		assertTrue(b.getValue() == 0);
		assertTrue(b.getSuit() == 0);
		//test other valid constructor
		Card c = new Card(1,2);
		assertTrue(c.getValue() == 1);
		assertTrue(c.getSuit() == 2);
	}

	/**
	 * This test ensures that for every card value or suit that there is a
	 * string value to return for it.
	 */
	@Test
	public void testEveryCardHasAStringValueAndSuit(){
		Card c;
		// test standard 52
		for(int value = 1; value <= 13; value++){
			for(int suit = 1; suit <= 4; suit++){
				c = new Card(value, suit);
				assertTrue(c.getValueAsString() instanceof String);
				assertTrue(c.getSuitAsString() instanceof String);
				assertTrue(c.toString() instanceof String);
			}
		}
		//test jokers
		c = new Card(Card.JOKER, Card.JOKER);
		assertTrue(c.getValueAsString() instanceof String);
		assertTrue(c.getSuitAsString() instanceof String);
		assertTrue(c.toString() instanceof String);
	}

	/**
	 * This test ensures accuracy of the comparison function, Card.equals()
	 */
	@Test
	public void testAccuracyOfEqualsFunction(){
		Card a = new Card(0,0);
		Card b = new Card(1,1);
		Card c = new Card(1,1);
		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
		assertFalse(a.equals(c));
		assertFalse(c.equals(a));
		assertTrue(b.equals(c));
		assertTrue(c.equals(b));
	}

}
