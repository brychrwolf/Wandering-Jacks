import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class PlayerTest {
	Player p = new Player();
	Card aJoker = new Card(0,0);
	Card aJack = new Card(Card.JACK, Card.CLUBS);

	@Before
	public void init(){
	}

	/*
	 * Test constructors
	 */
	@Test
	public void constructedWithEmptyHand(){
		assertTrue(p.getHand().size() == 0);
	}

	@Test
	public void constructedBankrollDefaultGreaterThan0(){
		assertTrue(p.getBankroll() > 0);
	}

	@Test
	public void constructedBankrollSetsBankroll(){
		int bValue = 999;
		p = new Player(bValue);
		assertTrue(p.getBankroll() == bValue);
	}

	/*
	 * Deep Cloning
	 */
	@Test
	public void cloningPlayer_CreatesNew_Player(){
		Player clone = new Player(p);
		assertNotSame(clone, p);
	}
	@Test
	public void cloningPlayer_CreatesNewButEqual_Hand(){
		Player clone = new Player(p);
		while(p.handSize() > 0){
			Card origCard = p.playFromHand(0);
			Card cloneCard = clone.playFromHand(0);
			assertNotSame(origCard, cloneCard);
			assertTrue(origCard.equals(cloneCard));
		}
	}
	@Test
	public void cloningPlayer_OnlyCopies_Bankroll(){
		Player clone = new Player(p);
		assertSame(p.getBankroll(), clone.getBankroll());
	}


	/**
	 * This test ensures that the player's hand is returned, empty or not.
	 */
	@Test
	public void testGettingHand() {
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
	@Test
	public void testAddToHand(){
		//test adding to initial retainer
		int sizeBefore = p.handSize();
		p.addToHand(aJoker);
		int sizeAfter = p.handSize();
		assertTrue(sizeBefore + 1 == sizeAfter);
		//test adding to retainer with same card
		sizeBefore = p.handSize();
		p.addToHand(aJoker);
		sizeAfter = p.handSize();
		assertTrue(sizeBefore + 1 == sizeAfter);
		//test adding to retainer with different cards
		sizeBefore = p.handSize();
		p.addToHand(aJack);
		sizeAfter = p.handSize();
		assertTrue(sizeBefore + 1 == sizeAfter);
	}

	/**
	 * This test ensures that size() is accurate
	 */
	@Test
	public void testHandSize(){
		//test initial size is empty
		assertTrue(p.handSize() == 0);
		//test after adding card
		p.addToHand(aJoker);
		assertTrue(p.handSize() == 1);
		//test after removing a card
		p.playFromHand(aJoker);
		assertTrue(p.handSize() == 0);
	}

	/**
	 * This test ensures that playingFromHand(Card) and (int),
	 * removes the correct card from the hand, decrementing its size,
	 * and throws an error if there are no cards or if that card didn't exist
	 */
	@Test
	public void testPlayFromHand(){
		//test removing from initial hand
		try{p.playFromHand(aJoker);}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
		try{p.playFromHand(0);}
		catch(IndexOutOfBoundsException e){
			assertTrue(e instanceof IndexOutOfBoundsException);}
		//test playFromHand by Card after adding one card
		p = new Player();
		p.addToHand(aJoker);
		assertTrue(p.playFromHand(aJoker).equals(aJoker));
		//test playFromHand by index after adding one card
		p = new Player();
		p.addToHand(aJoker);
		assertTrue(p.playFromHand(0).equals(aJoker));
		//test playFromHand by card after adding a different card
		p = new Player();
		p.addToHand(aJack);
		try{p.playFromHand(aJoker);}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
		//test playFromHand by card after adding two cards, one different
		p = new Player();
		p.addToHand(aJoker);
		p.addToHand(aJack);
		assertTrue(p.playFromHand(aJoker).equals(aJoker));
		//test playFromHand by index after adding two cards, one different
		p = new Player();
		p.addToHand(aJoker);
		p.addToHand(aJack);
		assertTrue(p.playFromHand(0).equals(aJoker));
		//test playFromHand by card after adding two cards, one different (different order)
		p = new Player();
		p.addToHand(aJack);
		p.addToHand(aJoker);
		assertTrue(p.playFromHand(aJoker).equals(aJoker));
		//test playFromHand by index after adding two cards, one different (different order)
		p = new Player();
		p.addToHand(aJack);
		p.addToHand(aJoker);
		assertTrue(p.playFromHand(0).equals(aJack));
	}

	/**
	 * Test getFromHand(index) returns a card if one exists in that
	 * index, and throws and exception otherwise
	 */
	@Test
	public void getFromHandReturnsCardFromIndex(){
		p.addToHand(aJack);
		p.addToHand(aJoker);
		assertTrue(p.getFromHand(0).equals(aJack));
		assertTrue(p.getFromHand(1).equals(aJoker));
	}
	@Test
	public void getFromHandThrowsIndexOoBException(){
		try{p.getFromHand(0);}
		catch(IndexOutOfBoundsException e){
			assertTrue(e instanceof IndexOutOfBoundsException);}
	}

}
