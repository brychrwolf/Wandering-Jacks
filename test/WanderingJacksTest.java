import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class WanderingJacksTest{
	Card aJoker = new Card(0, 0);
	Card aAce = new Card(1, 1);
	Card aKing = new Card(13, 1);
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
		assertTrue(wj.activePlayer == (int)wj.activePlayer);
		assertTrue(wj.onFirstMoveOfTurn == (boolean)wj.onFirstMoveOfTurn);
		assertTrue(wj.deck instanceof Deck);
		assertTrue(wj.deck instanceof Deck);
		assertTrue(wj.discardPile instanceof DiscardPile);
		assertTrue(wj.player[0] instanceof Player);
		assertTrue(wj.player[1] instanceof Player);
		assertTrue(wj.retainer[0] instanceof Retainer[]);
		assertTrue(wj.retainer[1] instanceof Retainer[]);
		for(int i = 0; i < 4; i++){
			assertTrue(wj.retainer[0][i] instanceof Retainer);
			assertTrue(wj.retainer[1][i] instanceof Retainer);
		}
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, all registers have exactly one card.
	 */
	@Test
	public void testAllRegistersHaveOneCard(){
		wj.setUpGameEnvironment();
		for(Retainer pr : wj.retainer[0]){
			assertTrue(pr.get(0) != null);
			assertTrue(pr.get(0) instanceof Card);
			for(int i = 1; i < pr.size(); i++){
				try{pr.get(i);}
				catch(IllegalStateException e){
					assertTrue(e instanceof IllegalStateException);}
			}
		}
		for(Retainer dr : wj.retainer[1]){
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
		for(Retainer r : wj.retainer[0])
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
			wj.retainer[0][i].add(aJack);
		assertTrue(wj.isGameOver());
		//test with player 2 having 4 jacks
		wj = new WanderingJacks();
		for(int i = 0; i < 4; i++)
			wj.retainer[1][i].add(aJack);
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
		wj.retainer[0][0] = twoQueens;
		wj.retainer[0][0].add(aJoker);
		int dpSizeBefore = wj.discardPile.size();
		while(wj.retainer[0][0].size() > 0){
			cardRemoved = wj.retainer[0][0].remove(0);
			wj.discardPile.discard(cardRemoved);
		}
		int dpSizeAfter = wj.discardPile.size();
		assertTrue(wj.retainer[0][0].size() == 0);
		assertTrue(dpSizeAfter == dpSizeBefore + 3);
		assertTrue(wj.discardPile.peekAtTopCard() == cardRemoved);
	}

	/**
	 * Test that a player can take a specific card from their register,
	 * and put it elsewhere
	 */
	@Test
	public void testPlayerTakeingCardFromRegisterPuttingElsehere(){
		wj.retainer[0][0].add(a10);
		wj.retainer[0][0].add(a9);
		wj.retainer[0][0].add(aJoker);
		Card cardRemoved = wj.retainer[0][0].remove(a9);
		//test that cardRemoved is no longer in retainer
		assertFalse(wj.retainer[0][0].contains(9));
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

	/*
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

	@Test
	public void playerDealtCardAddsItToHand(){
		Card dealtCard = wj.deck.dealCard();
		wj.player[wj.activePlayer].addToHand(dealtCard);
		assertTrue(wj.player[wj.activePlayer].handContains(dealtCard));
	}

	@Test
	public void playerDealtCardToRetainerStaysInRetainer(){
		Card dealtCard = wj.deck.dealCard();
		wj.retainer[0][0].add(dealtCard);
		assertTrue(wj.retainer[0][0].contains(dealtCard.getValue()));
	}

	/*
	 * Test that a player can swap a retainer with 3oak+A
	 */
	@Test(expected=IllegalStateException.class)
	public void exceptionThrown_PlayerHasNo3oak(){
		wj.threeOfAKindPlusAce(aAce, 0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void exceptionThrown_ORcontainsAJack(){
		wj.retainer[0][0] = three9s;
		wj.retainer[1][0].add(aJack);
		wj.threeOfAKindPlusAce(aAce, 0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_AceIsNotAnAce(){
		wj.retainer[0][0] = three9s;
		wj.threeOfAKindPlusAce(a9, 0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_PIndexOutOfBounds(){
		wj.retainer[0][0] = three9s;
		wj.threeOfAKindPlusAce(aAce, 5, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_OIndexOutOfBounds(){
		wj.retainer[0][0] = three9s;
		wj.threeOfAKindPlusAce(aAce, 0, 5);
	}

	@Test
	public void threeOfAKindSwapsRetainers(){
		wj.retainer[0][0] = three9s;
		wj.retainer[1][0] = twoQueens;
		wj.threeOfAKindPlusAce(aAce, 0, 0);
		assertTrue(wj.retainer[0][0].size() == 2);
		assertTrue(wj.retainer[0][0].get(0).equals(aQueen));
		assertTrue(wj.retainer[0][0].get(1).equals(aQueen));
		assertTrue(wj.retainer[1][0].size() == 1);
		assertTrue(wj.retainer[1][0].get(0).equals(a9));
		assertTrue(wj.discardPile.peekAtTopCard().equals(aAce));
	}

	/*
	 * Jack Stealing
	 */

	@Test
	public void stealingUnprotectedJackGainsJack(){
		wj.retainer[0][0] = twoQueens;
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(aJack);
		wj.stealJack(0, 0);
		assertTrue(wj.retainer[0][0].size() == 3);
		assertTrue(wj.retainer[0][0].get(2).equals(aJack));
		assertTrue(wj.retainer[1][0].size() == 1);
	}

	@Test
	public void stealingUnprotectedJackWithKingGainsJack(){
		wj.retainer[0][0] = twoQueens;
		wj.retainer[0][0].add(aKing);
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(aJack);
		wj.stealJack(0, 0);
		assertTrue(wj.retainer[0][0].size() == 4);
		assertTrue(wj.retainer[0][0].get(2).equals(aKing));
		assertTrue(wj.retainer[0][0].get(3).equals(aJack));
		assertTrue(wj.retainer[1][0].size() == 1);
	}

	@Test
	public void stealingKingProtectedJackGainsKing(){
		wj.retainer[0][0] = twoQueens;
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(aJack);
		wj.retainer[1][0].add(aKing);
		wj.stealJack(0, 0);
		assertTrue(wj.retainer[0][0].size() == 3);
		assertTrue(wj.retainer[0][0].get(2).equals(aKing));
		assertTrue(wj.retainer[1][0].size() == 2);
		assertTrue(wj.retainer[1][0].size() == 2);
	}

	@Test(expected=IllegalStateException.class)
	public void exThrown_stealingKingProtectedJackWithKing(){
		wj.retainer[0][0] = twoQueens;
		wj.retainer[0][0].add(aKing);
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(aJack);
		wj.retainer[1][0].add(aKing);
		wj.stealJack(0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void exThrown_stealingUnprotectedJackWithJack(){
		wj.retainer[0][0] = twoQueens;
		wj.retainer[0][0].add(aJack);
		wj.retainer[0][0].add(aKing);
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(aJack);
		wj.stealJack(0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void exThrown_stealingKingProtectedJackWithJack(){
		wj.retainer[0][0] = twoQueens;
		wj.retainer[0][0].add(aJack);
		wj.retainer[0][0].add(aKing);
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(aJack);
		wj.retainer[1][0].add(aKing);
		wj.stealJack(0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void exThrown_stealingNoJackWithJack(){
		wj.retainer[0][0] = twoQueens;
		wj.retainer[0][0].add(aJack);
		wj.retainer[0][0].add(aKing);
		wj.stealJack(0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void exThrown_stealingNoJack(){
		wj.retainer[0][0] = twoQueens;
		wj.stealJack(0, 0);
	}

	/*
	 * End of turn
	 */
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

	/*
	 * Making a Play
	 * and
	 * Play Requests
	 */
	@Test
	public void requestedPlayToHandIsPlayedWhenValid(){
		wj.setUpGameEnvironment();
		Card cardFromDiscardPile = wj.discardPile.peekAtTopCard();
		int originalHandSize = wj.player[wj.activePlayer].handSize();
		int[] pr = {1, 2, -1}; // discard pile, to my hand, with invalid handIndex
		assertTrue(wj.requestPlay(pr));
		int newHandSize = wj.player[wj.activePlayer].handSize();
		assertEquals(newHandSize, originalHandSize + 1);
		assertEquals(cardFromDiscardPile, wj.player[wj.activePlayer].getFromHand(originalHandSize));
	}

	@Test
	public void requestedPlayFromHandIsPlayedWhenValid(){
		wj.setUpGameEnvironment();
		Card cardFromHand = wj.player[wj.activePlayer].getFromHand(0);
		int originalHandSize = wj.player[wj.activePlayer].handSize();
		int originalDiscardPileSize = wj.discardPile.size();
		int[] pr = {2, 1, 0}; // from my hand, to discard pile, with valid handIndex
		assertTrue(wj.requestPlay(pr));
		int newHandSize = wj.player[wj.activePlayer].handSize();
		int newDiscardPileSize = wj.discardPile.size();
		assertEquals(newHandSize, originalHandSize - 1);
		assertEquals(newDiscardPileSize, originalDiscardPileSize + 1);
		assertEquals(cardFromHand, wj.discardPile.peekAtTopCard());
	}

	@Test(expected=IllegalStateException.class)
	public void requestedPlayIsNotPlayedWhenInvalid(){
		int[] pr = {1, 2, 0}; // empty discard pile, to my hand
		wj.requestPlay(pr);
	}

	@Test
	public void invalidHandIndex_Valid_IfHandIsNotChosenAsOrigin(){
		requestedPlayToHandIsPlayedWhenValid();
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void invalidHandIndex_Invalid_IfHandIsChosenAsOrigin(){
		wj.setUpGameEnvironment();
		int[] pr = {2, 1, -1}; // from hand, to discard pile, with invalid handIndex
		wj.requestPlay(pr);
	}

	/*
	 * onFirstMoveOfTurn tracker
	 */
	@Test
	public void onFirstMoveOfTurnTrueAtConstuction(){
		assertTrue(wj.onFirstMoveOfTurn());
	}
	@Test
	public void onFirstMoveOfTurnFalseAfterOnePlay(){
		int[] pr = {0, 2, -1};
		wj.requestPlay(pr);
		assertFalse(wj.onFirstMoveOfTurn());
	}
	@Test
	public void onFirstMoveOfTurnTrueAfterEndOfTurn(){
		int[] pr = {0, 2, -1};
		wj.requestPlay(pr);
		wj.endTurn();
		assertTrue(wj.onFirstMoveOfTurn());
	}

	/*
	 * Possible Origins
	 */
	@Test
	public void possibleOriginsReturnsANonEmptyResult(){
		assertTrue(wj.getPossibleOrigins().size() > 0);
	}

	@Test
	public void possibleOriginsIncludesDeckOnFirstMove(){
		assertTrue(wj.onFirstMoveOfTurn());
		assertTrue(wj.getPossibleOrigins().containsValue("deck"));
	}

	@Test
	public void possibleOriginsIncludesDicardPileOnFirstMove(){
		assertTrue(wj.onFirstMoveOfTurn());
		assertTrue(wj.getPossibleOrigins().containsValue("discard pile"));
	}

	@Test
	public void possibleOrigins_DontInclude_Deck_IfNotOnFirstMove(){
		int[] pr = {0, 2, -1};
		wj.requestPlay(pr);
		assertFalse(wj.onFirstMoveOfTurn());
		assertFalse(wj.getPossibleOrigins().containsValue("deck"));
	}

	@Test
	public void possibleOrigins_DontInclude_DicardPile_IfNotOnFirstMove(){
		int[] pr = {0, 2, -1}; // from deck, to my hand
		wj.requestPlay(pr);
		assertFalse(wj.onFirstMoveOfTurn());
		assertFalse(wj.getPossibleOrigins().containsValue("discard pile"));
	}


	/*
	 * Possible Destinations
	 */
	@Test
	public void possibleDestinations_Returns_ANonEmptyResult(){
		wj.setUpGameEnvironment();
		assertTrue(wj.getPossibleDestinations(aQueen).size() > 0);
	}

	@Test
	public void possibleDestinations_Includes_MyHand_OnFirstMove(){
		wj.setUpGameEnvironment();
		assertTrue(wj.onFirstMoveOfTurn());
		assertTrue(wj.getPossibleDestinations(aQueen).containsValue("my hand"));
	}

	@Test
	public void possibleDestinations_DontInclude_MyHand_IfNotOnFirstMove(){
		wj.setUpGameEnvironment();
		int[] pr = {0, 2, -1};
		wj.requestPlay(pr);
		assertFalse(wj.onFirstMoveOfTurn());
		assertFalse(wj.getPossibleDestinations(aQueen).containsValue("my hand"));
	}

	@Test
	public void possibleDestinations_Includes_DiscardPile_IfNotOnFirstMove(){
		wj.setUpGameEnvironment();
		int[] pr = {0, 2, -1};
		wj.requestPlay(pr);
		assertFalse(wj.onFirstMoveOfTurn());
		assertTrue(wj.getPossibleDestinations(aQueen).containsValue("discard pile & end turn"));
	}

	@Test
	public void possibleDestinations_DontInclude_DiscardPile_OnFirstMove(){
		wj.setUpGameEnvironment();
		assertTrue(wj.onFirstMoveOfTurn());
		assertFalse(wj.getPossibleDestinations(aQueen).containsValue("discard pile & end turn"));
	}

	@Test
	public void possibleDestinations_Include_RetainersWithSameValues_IfNotOnFirstMove(){
		int[] pr = {0, 2, -1};
		wj.requestPlay(pr);
		wj.retainer[0][0].add(aQueen);
		wj.retainer[0][1].add(aJack);
		wj.retainer[0][2].add(a10);
		wj.retainer[0][3].add(a9);
		assertFalse(wj.onFirstMoveOfTurn());
		assertTrue(wj.getPossibleDestinations(aQueen).containsValue("retainer: "+aQueen.toString()+" "));
		assertFalse(wj.getPossibleDestinations(aQueen).containsValue("retainer: "+aJack.toString()+" "));
		assertFalse(wj.getPossibleDestinations(aQueen).containsValue("retainer: "+a10.toString()+" "));
		assertFalse(wj.getPossibleDestinations(aQueen).containsValue("retainer: "+a9.toString()+" "));
	}

	@Test
	public void possibleDestinations_Include_RetainersWithQueensOr10s_WhenPlayingAJack_IfNotOnFirstMove(){
		int[] pr = {0, 2, -1};
		wj.requestPlay(pr);
		wj.retainer[0][0].add(aQueen);
		wj.retainer[0][1].add(a10);
		wj.retainer[0][2].add(a9);
		wj.retainer[0][3].add(a9);
		wj.player[0].addToHand(aJack);
		assertFalse(wj.onFirstMoveOfTurn());
		assertTrue(wj.getPossibleDestinations(aJack).containsValue("retainer: "+aQueen.toString()+" "));
		assertTrue(wj.getPossibleDestinations(aJack).containsValue("retainer: "+a10.toString()+" "));
		assertFalse(wj.getPossibleDestinations(aJack).containsValue("retainer: "+a9.toString()+" "));
	}

	/*
	 * validMoves
	 */
	@Test
	public void testValidMoves(){
		fail("Not implemented");
	}
}
