import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
		assertTrue(wj.house instanceof Player);
		assertTrue(wj.playerRetainer instanceof Retainer[]);
		assertTrue(wj.houseRetainer instanceof Retainer[]);
		for(int i = 0; i < 4; i++){
			assertTrue(wj.playerRetainer[i] instanceof Retainer);
			assertTrue(wj.houseRetainer[i] instanceof Retainer);
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
				try{pr.get(i);}
				catch(IllegalStateException e){
					assertTrue(e instanceof IllegalStateException);}
			}
		}
		for(Retainer dr : wj.houseRetainer){
			assertTrue(dr.get(0) != null);
			assertTrue(dr.get(0) instanceof Card);
			for(int i = 1; i < dr.size(); i++){
				try{dr.get(i);}
				catch(IllegalStateException e){
					assertTrue(e instanceof IllegalStateException);}
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


	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, that there are exactly 3 cards in each
	 * players' hand.
	 */
	@Test
	public void testInitialPlayersHandsHave3Cards(){
		WanderingJacks wj = new WanderingJacks();
		wj.setUpGameEnvironment();
		assertEquals(wj.player.getHand().size(), 3);
		assertEquals(wj.house.getHand().size(), 3);
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, that there are no Jokers in either players
	 * hand.
	 */
	@Test
	public void testInitialPlayersHandsHaveNoJokers(){
		WanderingJacks wj = new WanderingJacks();
		wj.setUpGameEnvironment();
		for(Card c : wj.player.getHand())
			assertTrue(c.getValue() != Card.JOKER);
		for(Card c : wj.house.getHand())
			assertTrue(c.getValue() != Card.JOKER);
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, the discard pile only has one card.
	 */
	@Test
	public void testInitialDiscardPileOnlyHasOneCard(){
		WanderingJacks wj = new WanderingJacks();
		wj.setUpGameEnvironment();
		assertEquals(wj.discardPile.size(), 1);
	}

	/**
	 * This test ensures that the referee can detect when the game is over by
	 * detecting when player1 has a jack in all four registers
	 * detecting when player2 has a jack in all four registers
	 * detecting when player1's bankroll
	 */
	@Test
	public void testDetectingGameOver(){
		WanderingJacks wj = new WanderingJacks();
		Card aJack = new Card(Card.JACK, 1);
		//test with initial retainers and bankroll
		assertFalse(wj.isGameOver());
		//test with player 1 having 4 jacks
		for(int i = 0; i < 4; i++)
			wj.playerRetainer[i].add(aJack);
		assertTrue(wj.isGameOver());
		//test with player 2 having 4 jacks
		wj = new WanderingJacks();
		for(int i = 0; i < 4; i++)
			wj.houseRetainer[i].add(aJack);
		assertTrue(wj.isGameOver());
		//test with player 1 running out of money
		wj = new WanderingJacks();
		wj.player.setBankroll(0);
		assertTrue(wj.isGameOver());
		wj.player.setBankroll(-10);
		assertTrue(wj.isGameOver());
	}

	/**
	 * Test that when a player discards their entire register,
	 * that register no has size of 0,
	 * and that the discard pile size increases by that retainer size
	 */
	@Test
	public void testDiscardingEntireRegister(){
		WanderingJacks wj = new WanderingJacks();
		Card a10 = new Card(10, 1);
		Card aJoker = new Card(0, 0);
		Card cardRemoved = new Card();
		wj.playerRetainer[0].add(a10);
		wj.playerRetainer[0].add(a10);
		wj.playerRetainer[0].add(aJoker);
		int dpSizeBefore = wj.discardPile.size();
		while(wj.playerRetainer[0].size() > 0){
			cardRemoved = wj.playerRetainer[0].remove(0);
			wj.discardPile.discard(cardRemoved);
		}
		int dpSizeAfter = wj.discardPile.size();
		assertTrue(wj.playerRetainer[0].size() == 0);
		assertTrue(dpSizeAfter == dpSizeBefore + 3);
		assertTrue(wj.discardPile.peekAtTopCard() == cardRemoved);
	}
}
