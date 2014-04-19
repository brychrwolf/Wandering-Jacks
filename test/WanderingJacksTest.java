import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class WanderingJacksTest{
	Card aJoker = new Card(0, 0);
	Card aAce = new Card(1, 1);
	Card aQueen = new Card(12, 1);
	Card aJack = new Card(11, 1);
	Card a10 = new Card(10, 1);
	Card a9 = new Card(9, 1);
	Retainer three9s;
	Retainer twoQueens;
	WanderingJacks wj;

	@Before
	public void init(){
		wj = new WanderingJacks();
		three9s = new Retainer();
		three9s.add(a9);
		three9s.add(a9);
		three9s.add(a9);
		twoQueens = new Retainer();
		twoQueens.add(aQueen);
		twoQueens.add(aQueen);
	}

	/**
	 * This test ensures that at the beginning of the game, the deck has 54
	 * cards (the standard 52 + 2 Jokers).
	 */
	@Test
	public void testInitialDeckSizeIs54(){
		assertTrue(wj.deck.cardsLeft() == 54);
	}

	/**
	 * This test ensures that at the beginning of the game, the deck contains
	 * exactly two 2 Jokers.
	 */
	@Test
	public void testInitialDeckHasTwoJokers(){
		int numJokers = 0;
		while(wj.deck.cardsLeft() > 0)
			if(wj.deck.dealCard().getSuit() == Card.JOKER)
				numJokers++;
		assertTrue(numJokers == 2);
	}

	@Test
	public void playerGoesFirstInNewGame(){
		assertTrue(wj.activePlayer == 0);
	}

	/**
	 * This test ensures that at the beginning of the game, the appropriate
	 * objects are created.
	 */
	@Test
	public void testInitialGameObjectsAreCreated(){
		assertTrue(wj.deck instanceof Deck);
		assertTrue(wj.discardPile instanceof DiscardPile);
		assertTrue(wj.player[0] instanceof Player);
		assertTrue(wj.player[1] instanceof Player);
		assertTrue(wj.retainerGroup[0] instanceof Retainer[]);
		assertTrue(wj.retainerGroup[1] instanceof Retainer[]);
		for(int i = 0; i < 4; i++){
			assertTrue(wj.retainerGroup[0][i] instanceof Retainer);
			assertTrue(wj.retainerGroup[1][i] instanceof Retainer);
		}
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, all registers have exactly one card.
	 */
	@Test
	public void testAllRegistersHaveOneCard(){
		wj.setUpGameEnvironment();
		for(Retainer pr : wj.retainerGroup[0]){
			assertTrue(pr.get(0) != null);
			assertTrue(pr.get(0) instanceof Card);
			for(int i = 1; i < pr.size(); i++){
				try{pr.get(i);}
				catch(IllegalStateException e){
					assertTrue(e instanceof IllegalStateException);}
			}
		}
		for(Retainer dr : wj.retainerGroup[1]){
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
		wj.setUpGameEnvironment();
		for(Retainer r : wj.retainerGroup[0])
			assertTrue(!forbiddenValues.contains(r.get(0).getValue()));
	}


	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, that there are exactly 3 cards in each
	 * players' hand.
	 */
	@Test
	public void testInitialPlayersHandsHave3Cards(){
		wj.setUpGameEnvironment();
		assertEquals(wj.player[0].getHand().size(), 3);
		assertEquals(wj.player[1].getHand().size(), 3);
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, that there are no Jokers in either players
	 * hand.
	 */
	@Test
	public void testInitialPlayersHandsHaveNoJokers(){
		wj.setUpGameEnvironment();
		for(Card c : wj.player[0].getHand())
			assertTrue(c.getValue() != Card.JOKER);
		for(Card c : wj.player[1].getHand())
			assertTrue(c.getValue() != Card.JOKER);
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, the discard pile only has one card.
	 */
	@Test
	public void testInitialDiscardPileOnlyHasOneCard(){
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
		//test with initial retainers and bankroll
		assertFalse(wj.isGameOver());
		//test with player 1 having 4 jacks
		for(int i = 0; i < 4; i++)
			wj.retainerGroup[0][i].add(aJack);
		assertTrue(wj.isGameOver());
		//test with player 2 having 4 jacks
		wj = new WanderingJacks();
		for(int i = 0; i < 4; i++)
			wj.retainerGroup[1][i].add(aJack);
		assertTrue(wj.isGameOver());
		//test with player 1 running out of money
		wj = new WanderingJacks();
		wj.player[0].setBankroll(0);
		assertTrue(wj.isGameOver());
		wj.player[0].setBankroll(-10);
		assertTrue(wj.isGameOver());
	}

	/**
	 * Test that when a player discards their entire register,
	 * that register no has size of 0,
	 * and that the discard pile size increases by that retainer size
	 */
	@Test
	public void testDiscardingEntireRegister(){
		Card cardRemoved = new Card();
		wj.retainerGroup[0][0] = twoQueens;
		wj.retainerGroup[0][0].add(aJoker);
		int dpSizeBefore = wj.discardPile.size();
		while(wj.retainerGroup[0][0].size() > 0){
			cardRemoved = wj.retainerGroup[0][0].remove(0);
			wj.discardPile.discard(cardRemoved);
		}
		int dpSizeAfter = wj.discardPile.size();
		assertTrue(wj.retainerGroup[0][0].size() == 0);
		assertTrue(dpSizeAfter == dpSizeBefore + 3);
		assertTrue(wj.discardPile.peekAtTopCard() == cardRemoved);
	}

	/**
	 * Test that a player can take a specific card from their register,
	 * and put it elsewhere
	 */
	@Test
	public void testPlayerTakeingCardFromRegisterPuttingElsehere(){
		wj.retainerGroup[0][0].add(a10);
		wj.retainerGroup[0][0].add(a9);
		wj.retainerGroup[0][0].add(aJoker);
		Card cardRemoved = wj.retainerGroup[0][0].remove(a9);
		//test that cardRemoved is no longer in retainer
		assertFalse(wj.retainerGroup[0][0].contains(a9));
		//test that cardRemoved is an actual card that can be placed elsewhere
		//   and is the same card that was removed
		assertTrue(cardRemoved instanceof Card);
		assertTrue(cardRemoved.equals(a9));
	}

	/**
	 * Test that a player can discard from their hand
	 */
	@Test
	public void testPlayerPlayingCardFromHandToOwnedRetainer(){
		wj.player[0].addToHand(aJoker);
		Card cardPlayed = wj.player[0].playFromHand(aJoker);
		assertTrue(wj.player[0].handSize() == 0);
		wj.discardPile.discard(cardPlayed);
		assertTrue(wj.discardPile.peekAtTopCard().equals(cardPlayed));
	}

	/**
	 * Test that a player can take a card from the discard pile and put it in
	 * their hand
	 */
	@Test
	public void testPlayerTakingCardFromDiscardPileToHand(){
		wj.discardPile.discard(aJoker);
		Card cardTaken = wj.discardPile.takeTopCard();
		wj.player[0].addToHand(cardTaken);
		assertTrue(wj.player[0].handSize() == 1);
		assertTrue(wj.player[0].handContains(aJoker));
		assertTrue(wj.discardPile.size() == 0);
	}

	/**
	 * Test that a player can swap a retainer with 3oak+A
	 */
	@Test(expected=IllegalStateException.class)
	public void exceptionThrown_PlayerHasNo3oak(){
		wj.threeOfAKind('p', aAce, 0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void exceptionThrown_ORcontainsAJack(){
		wj.retainerGroup[0][0] = three9s;
		wj.retainerGroup[1][0].add(aJack);
		wj.threeOfAKind('p', aAce, 0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_PHisNotPorH(){
		wj.retainerGroup[0][0] = three9s;
		wj.threeOfAKind('f', aAce, 0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_AceIsNotAnAce(){
		wj.retainerGroup[0][0] = three9s;
		wj.threeOfAKind('p', a9, 0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_PIndexOutOfBounds(){
		wj.retainerGroup[0][0] = three9s;
		wj.threeOfAKind('p', aAce, 5, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_OIndexOutOfBounds(){
		wj.retainerGroup[0][0] = three9s;
		wj.threeOfAKind('p', aAce, 0, 5);
	}

	@Test
	public void threeOfAKindSwapsRetainers(){
		wj.retainerGroup[0][0] = three9s;
		wj.retainerGroup[1][0] = twoQueens;
		wj.threeOfAKind('p', aAce, 0, 0);
		assertTrue(wj.retainerGroup[0][0].size() == 2);
		assertTrue(wj.retainerGroup[0][0].get(0).equals(aQueen));
		assertTrue(wj.retainerGroup[0][0].get(1).equals(aQueen));
		assertTrue(wj.retainerGroup[1][0].size() == 1);
		assertTrue(wj.retainerGroup[1][0].get(0).equals(a9));
		assertTrue(wj.discardPile.peekAtTopCard().equals(aAce));
	}


	/**
	 * Test that a player can attempt to steal a Jack from opponent
	 */
	@Test
	public void stealingUnprotectedJackGainsJackForPlayer(){
		wj.retainerGroup[0][0] = twoQueens;
		wj.retainerGroup[1][0].add(a10);
		wj.retainerGroup[1][0].add(aJack);
		wj.stealJack(0, 0);
		assertTrue(wj.retainerGroup[0][0].size() == 3);
		assertTrue(wj.retainerGroup[0][0].get(2).equals(aJack));
		assertTrue(wj.retainerGroup[1][0].size() == 1);
	}

	@Test
	public void endingTurnTogglesActivePlayer(){
		// test 0 to 1
		int previousPlayer = wj.activePlayer;
		wj.endTurn();
		assertTrue(previousPlayer == 0 && wj.activePlayer == 1);
		// test 1 to 0
		previousPlayer = wj.activePlayer;
		wj.endTurn();
		assertTrue(previousPlayer == 1 && wj.activePlayer == 0);
	}
}
