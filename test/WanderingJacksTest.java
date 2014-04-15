import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

public class WanderingJacksTest{

	/**
	 * This test ensures that at the beginning of the game, the deck has 54
	 * cards (the standard 52 + 2 Jokers).
	 */
	@Test
	public void testInitialDeckSizeIs54(){
		WanderingJacks wj = new WanderingJacks();
		assertTrue(wj.deck.cardsLeft() == 54);
	}

	/**
	 * This test ensures that at the beginning of the game, the deck contains
	 * exactly two 2 Jokers.
	 */
	@Test
	public void testInitialDeckHasTwoJokers(){
		WanderingJacks wj = new WanderingJacks();
		int numJokers = 0;
		while(wj.deck.cardsLeft() > 0)
			if(wj.deck.dealCard().getSuit() == Card.JOKER)
				numJokers++;
		assertTrue(numJokers == 2);
	}

	/**
	 * This test ensures that at the beginning of the game, the appropriate
	 * objects are created.
	 */
	@Test
	public void testInitialGameObjectsAreCreated(){
		WanderingJacks wj = new WanderingJacks();
		assertTrue(wj.deck instanceof Deck);
		assertTrue(wj.discardPile instanceof DiscardPile);
		assertTrue(wj.player instanceof Player);
		assertTrue(wj.dealer instanceof Player);
		assertTrue(wj.playerRetainer instanceof Retainer[]);
		assertTrue(wj.dealerRetainer instanceof Retainer[]);
		for(int i = 0; i < 4; i++){
			assertTrue(wj.playerRetainer[i] instanceof Retainer);
			assertTrue(wj.dealerRetainer[i] instanceof Retainer);
		}
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, all registers have exactly one card.
	 */
	@Test
	public void testAllRegistersHaveOneCard(){
		WanderingJacks wj = new WanderingJacks();
		wj.setUpGameEnvironment();
		for(Retainer pr : wj.playerRetainer){
			assertTrue(pr.get(0) != null);
			assertTrue(pr.get(0) instanceof Card);
			for(int i = 1; i < pr.size(); i++){
				assertTrue(pr.get(i) == null);
			}
		}
		for(Retainer dr : wj.dealerRetainer){
			assertTrue(dr.get(0) != null);
			assertTrue(dr.get(0) instanceof Card);
			for(int i = 1; i < dr.size(); i++){
				assertTrue(dr.get(i) == null);
			}
		}
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, that the first card in any register is not
	 * found within the forbidden list.
	 */
	@Test
	public void testFirstCardInRegisterIsNotAForbiddenCard(){
		HashSet<Integer> forbiddenValues = new HashSet<Integer>();
		forbiddenValues.add(Card.JACK);
		forbiddenValues.add(Card.ACE);
		forbiddenValues.add(Card.JOKER);
		forbiddenValues.add(Card.KING);
		WanderingJacks wj = new WanderingJacks();
		wj.setUpGameEnvironment();
		for(Retainer r : wj.playerRetainer)
			assertTrue(!forbiddenValues.contains(r.get(0).getValue()));
	}

}
