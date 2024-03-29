import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class RetainerTest {
	Card aJoker = new Card(0, 0);
	Card aAce = new Card(1, 1);
	Card aKing = new Card(13, 1);
	Card aQueen = new Card(12, 1);
	Card aJack = new Card(11, 1);
	Card a10 = new Card(10, 1);
	Card a9 = new Card(9, 1);
	Retainer r = new Retainer();
	/*Retainer three9s;
	Retainer twoQueens;
	WanderingJacks wj;*/

	@Before
	public void init(){
		/*wj = new WanderingJacks();
		three9s = new Retainer();
		three9s.add(a9);
		three9s.add(a9);
		three9s.add(a9);
		twoQueens = new Retainer();
		twoQueens.add(aQueen);
		twoQueens.add(aQueen);*/
	}

	/*
	 * Deep Cloning
	 */
	@Test
	public void cloningEmptyRetainer_CreatesNew_Retainer(){
		Retainer clone = new Retainer(r);
		assertNotSame(clone, r);
	}

	@Test
	public void cloningNonEmptyRetainer_CreatesNewButSameSize_Retainer(){
		r.add(a9);
		Retainer clone = new Retainer(r);
		assertNotSame(clone, r);
		assertEquals(clone.size(), r.size());
	}

	@Test
	public void cloningRetainer_CreatesNewButEqual_Cards(){
		r.add(a9);
		r.add(a10);
		Retainer clone = new Retainer(r);
		while(r.size() > 0){
			Card origCard = r.remove(0);
			Card cloneCard = clone.remove(0);
			assertNotSame(origCard, cloneCard);
			assertTrue(origCard.equals(cloneCard));
		}
	}

	@Test
	public void cloningRetainer_OnlyCopies_RetainsJack(){
		Retainer clone = new Retainer(r);
		assertSame(r.retainsJack(), clone.retainsJack());
	}

	/**
	 * This test ensures that add(Card) adds the correct card to the retainer.
	 */
	@Test
	public void addingTo_InitialRetainer_AddsCard(){
		r.add(a9);
		assertEquals(r.size(), 1);
		assertEquals(r.get(0), a9);
	}
	@Test
	public void addingTo_NonEmptyRetainer_AddsCard(){
		r.add(a9);
		r.add(a9);
		assertEquals(r.size(), 2);
		assertEquals(r.get(1), a9);
	}
	@Test
	public void addingto_RetainerSize10_AddsCard(){
		for(int i = 0; i < 10; i++)
			r.add(a9);
		r.add(a9);
		assertEquals(r.size(), 11);
		assertEquals(r.get(10), a9);
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
		try{retainer.remove(0);}
		catch(IndexOutOfBoundsException e){
			assertTrue(e instanceof IndexOutOfBoundsException);}
		//test removing by card after adding that card
		retainer = new Retainer();
		retainer.add(aJoker);
		assertTrue(retainer.remove(aJoker).equals(aJoker));
		//test removing by index after adding that card
		retainer = new Retainer();
		retainer.add(aJoker);
		assertTrue(retainer.remove(0).equals(aJoker));
		//test removing by card after adding a different card
		retainer = new Retainer();
		retainer.add(aJack);
		try{retainer.remove(aJoker);}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
		//test removing by card after adding two cards, one different
		retainer = new Retainer();
		retainer.add(aJoker);
		retainer.add(aJack);
		assertTrue(retainer.remove(aJoker).equals(aJoker));
		//test removing by index after adding two cards, one different
		retainer = new Retainer();
		retainer.add(aJoker);
		retainer.add(aJack);
		assertTrue(retainer.remove(0).equals(aJoker));
		//test removing by card after adding two cards, one different (differnt order)
		retainer = new Retainer();
		retainer.add(aJack);
		retainer.add(aJoker);
		assertTrue(retainer.remove(aJoker).equals(aJoker));
		//test removing by card after adding two cards, one different (differnt order)
		retainer = new Retainer();
		retainer.add(aJack);
		retainer.add(aJoker);
		assertTrue(retainer.remove(0).equals(aJack));

		//test removing after adding, then emptying
		retainer = new Retainer();
		retainer.add(aJoker);
		retainer.empty();
		try{retainer.remove(aJoker);}
		catch(IllegalStateException e){
			assertTrue(e instanceof IllegalStateException);}
	}

	/**
	 * This test ensures that empty() removes all cards from the retainer.
	 */
	@Test
	public void testRetainerEmpty(){
		Card aJoker = new Card(0,0);
		Card aJack = new Card(Card.JACK, Card.CLUBS);
		Retainer retainer = new Retainer();
		//test emptying the initial retainer
		retainer.empty();
		assertTrue(retainer.size() == 0);
		assertTrue(retainer.isEmpty());
		//test after adding to retainer
		retainer = new Retainer();
		retainer.add(aJoker);
		retainer.empty();
		assertTrue(retainer.size() == 0);
		assertTrue(retainer.isEmpty());
		//test after adding two different cards
		retainer = new Retainer();
		retainer.add(aJack);
		retainer.add(aJoker);
		retainer.empty();
		assertTrue(retainer.size() == 0);
		assertTrue(retainer.isEmpty());
		//test after emptying and empty retainer
		retainer = new Retainer();
		retainer.empty();
		retainer.empty();
		assertTrue(retainer.size() == 0);
		assertTrue(retainer.isEmpty());
	}

	/**
	 * This test ensures that isEmpty() reports true when there are
	 * no cards in the retainer, and never at any other time
	 */
	@Test
	public void testRetainerIsEmpty(){
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
	 * This test ensures that get(index) returns a card if one exists in that
	 * index, and throws and exception otherwise
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void getting_FromInitialRetainer_ThrowsException(){
		r.get(0);
	}

	@Test
	public void gettingCard_FromRetainerWith1Card_GetsCardInfo(){
		r.add(a9);
		Card cardGotten = r.get(0);
		assertEquals(r.size(), 1);
		assertEquals(cardGotten, a9);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void gettingHighIndex_FromSmallRetainer_ThrowsException(){
		r.add(a9);
		assertEquals(r.size(), 1);
		r.get(2);
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
		assertFalse(retainer.contains(Card.JOKER));
		retainer.add(aJoker);
		assertTrue(retainer.contains(Card.JOKER));
		//test indexOf(int)
		retainer = new Retainer();
		assertTrue(retainer.indexOf(Card.JOKER) == -1);
		retainer.add(aJoker);
		assertTrue(retainer.indexOf(Card.JOKER) == 0);
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
