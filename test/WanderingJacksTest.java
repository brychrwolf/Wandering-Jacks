import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class WanderingJacksTest{
	Card aJoker = new Card(0, 0);
	Card anAce = new Card(1, 1);
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

	/*
	 * Deep Cloning
	 */
	@Test
	public void cloningGame_CreatesNew_game(){
		WanderingJacks clone = new WanderingJacks(wj);
		assertNotSame(wj, clone);
	}

	@Test
	public void cloningGame_OnlyCopies_activePlayer(){
		WanderingJacks clone = new WanderingJacks(wj);
		assertSame(wj.activePlayer, clone.activePlayer);
	}

	@Test
	public void cloningGame_OnlyCopies_onFirstMoveOfTurn(){
		WanderingJacks clone = new WanderingJacks(wj);
		assertSame(wj.onFirstMoveOfTurn, clone.onFirstMoveOfTurn);
	}

	@Test
	public void cloningGame_CreatesNew_deck(){
		WanderingJacks clone = new WanderingJacks(wj);
		assertNotSame(wj.deck, clone.deck);
	}

	@Test
	public void cloningGame_CreatesNew_Players(){
		WanderingJacks clone = new WanderingJacks(wj);
		for(int i = 0; i < 2; i++)
			assertNotSame(wj.player[i], clone.player[i]);
	}

	@Test
	public void cloningGame_CreatesNewButEqual_Retainers(){
		WanderingJacks clone = new WanderingJacks(wj);
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 4; j++){
				assertNotSame(wj.retainer[i][j], clone.retainer[i][j]);
				for(int k = 0; k < wj.retainer[i][j].size(); k++)
					assertTrue(wj.retainer[i][j].get(k).equals(clone.retainer[i][j].get(k)));
			}
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
		assertEquals(wj.player[0].handSize(), 3);
		assertEquals(wj.player[1].handSize(), 3);
	}

	/**
	 * This test ensures that at the beginning of the game, after the
	 * environment has been set up, that there are no Jokers in either players
	 * hand.
	 */
	@Test
	public void testInitialPlayersHandsHaveNoJokers(){
		wj.setUpGameEnvironment();
		assertFalse(wj.player[0].handContains("Joker"));
		assertFalse(wj.player[1].handContains("Joker"));
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
		wj.threeOfAKindPlusAce(anAce, 0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void exceptionThrown_ORcontainsAJack(){
		wj.retainer[0][0] = three9s;
		wj.retainer[1][0].add(aJack);
		wj.threeOfAKindPlusAce(anAce, 0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_AceIsNotAnAce(){
		wj.retainer[0][0] = three9s;
		wj.threeOfAKindPlusAce(a9, 0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_PIndexOutOfBounds(){
		wj.retainer[0][0] = three9s;
		wj.threeOfAKindPlusAce(anAce, 5, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void exceptionThrown_OIndexOutOfBounds(){
		wj.retainer[0][0] = three9s;
		wj.threeOfAKindPlusAce(anAce, 0, 5);
	}

	@Test
	public void threeOfAKindSwapsRetainers(){
		wj.retainer[0][0] = three9s;
		wj.retainer[1][0] = twoQueens;
		wj.threeOfAKindPlusAce(anAce, 0, 0);
		assertTrue(wj.retainer[0][0].size() == 2);
		assertTrue(wj.retainer[0][0].get(0).equals(aQueen));
		assertTrue(wj.retainer[0][0].get(1).equals(aQueen));
		assertTrue(wj.retainer[1][0].size() == 1);
		assertTrue(wj.retainer[1][0].get(0).equals(a9));
		assertTrue(wj.discardPile.peekAtTopCard().equals(anAce));
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
	public void requestedPlay_ToHand_IsPlayedWhenValid(){
		wj.setUpGameEnvironment();
		Card cardFromDiscardPile = wj.discardPile.peekAtTopCard();
		int originalHandSize = wj.player[wj.activePlayer].handSize();
		int[] pr = {2, 3, -1}; // discard pile, to my hand, with invalid handIndex
		assertTrue(wj.requestPlay(pr));
		int newHandSize = wj.player[wj.activePlayer].handSize();
		assertEquals(newHandSize, originalHandSize + 1);
		assertEquals(cardFromDiscardPile, wj.player[wj.activePlayer].getFromHand(originalHandSize));
	}

	@Test
	public void requestedPlay_FromHand_ToDiscardPile_IsPlayedWhenValid(){
		wj.setUpGameEnvironment();
		Card cardFromHand = wj.player[wj.activePlayer].getFromHand(0);
		int originalHandSize = wj.player[wj.activePlayer].handSize();
		int originalDiscardPileSize = wj.discardPile.size();
		int[] pr = {3, 2, 0}; // from my hand, to discard pile, with valid handIndex
		assertTrue(wj.requestPlay(pr));
		int newHandSize = wj.player[wj.activePlayer].handSize();
		int newDiscardPileSize = wj.discardPile.size();
		assertEquals(newHandSize, originalHandSize - 1);
		assertEquals(newDiscardPileSize, originalDiscardPileSize + 1);
		assertEquals(cardFromHand, wj.discardPile.peekAtTopCard());
	}

	@Test
	public void requestedPlay_FromHand_ToFirstRetainer_IsPlayedWhenValid(){
		wj.retainer[0][0].add(a9);
		wj.player[0].addToHand(a9);
		int[] pr = {3, 4, 0}; // from my hand, to My 1st Retainer, with valid handIndex
		assertTrue(wj.requestPlay(pr));
		assertEquals(wj.player[0].handSize(), 0);
		assertEquals(wj.retainer[0][0].size(), 2);
		assertEquals(wj.retainer[0][0].get(1), a9);
	}

	@Test(expected=IllegalStateException.class)
	public void requestedPlayIsNotPlayedWhenInvalid(){
		int[] pr = {2, 3, 0}; // empty discard pile, to my hand
		wj.requestPlay(pr);
	}

	@Test
	public void invalidHandIndex_Valid_IfHandIsNotChosenAsOrigin(){
		requestedPlay_ToHand_IsPlayedWhenValid();
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void invalidHandIndex_Invalid_IfHandIsChosenAsOrigin(){
		wj.setUpGameEnvironment();
		int[] pr = {3, 2, -1}; // from hand, to discard pile, with invalid handIndex
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
		int[] pr = {1, 3, -1}; // deck to hand
		wj.requestPlay(pr);
		assertFalse(wj.onFirstMoveOfTurn());
	}
	@Test
	public void onFirstMoveOfTurnTrueAfterEndOfTurn(){
		int[] pr = {1, 3, -1}; // deck to hand
		wj.requestPlay(pr);
		wj.endTurn();
		assertTrue(wj.onFirstMoveOfTurn());
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
		assertTrue(wj.getPossibleDestinations(aQueen).containsValue("My Hand"));
	}

	@Test
	public void possibleDestinations_DontInclude_MyHand_IfNotOnFirstMove(){
		wj.onFirstMoveOfTurn = false;
		assertFalse(wj.onFirstMoveOfTurn());
		assertFalse(wj.getPossibleDestinations(aQueen).containsValue("My Hand"));
	}

	@Test
	public void possibleDestinations_Includes_DiscardPile_IfNotOnFirstMove(){
		wj.onFirstMoveOfTurn = false;
		assertFalse(wj.onFirstMoveOfTurn());
		assertTrue(wj.getPossibleDestinations(aQueen).containsValue("The Discard Pile"));
	}

	@Test
	public void possibleDestinations_DontInclude_DiscardPile_OnFirstMove(){
		wj.setUpGameEnvironment();
		assertTrue(wj.onFirstMoveOfTurn());
		assertFalse(wj.getPossibleDestinations(aQueen).containsValue("The Discard Pile"));
	}

	@Test
	public void possibleDestinations_Include_RetainersWithSameValues_IfNotOnFirstMove(){
		wj.onFirstMoveOfTurn = false;
		wj.retainer[0][0].add(aQueen);
		wj.retainer[0][1].add(aJack);
		wj.retainer[0][2].add(a10);
		wj.retainer[0][3].add(a9);
		assertFalse(wj.onFirstMoveOfTurn());
		HashMap<Integer, String> pd = wj.getPossibleDestinations(aJack);
		assertTrue(pd.containsKey(4));
		assertFalse(pd.containsKey(5));
		assertTrue(pd.containsKey(6));
		assertFalse(pd.containsKey(7));
	}

	@Test
	public void possibleDestinations_Include_GoBack_WhenPromptingWhereToPlayFromHand(){
		wj.setUpGameEnvironment();
		wj.onFirstMoveOfTurn = false;
		assertTrue(wj.getPossibleDestinations(aQueen).containsValue("Go Back"));
	}

	@Test
	public void possibleDestinations_DontInclude_DiscardPile_WhenPlayingAJoker(){
		wj.setUpGameEnvironment();
		wj.onFirstMoveOfTurn = false;
		assertFalse(wj.getPossibleDestinations(aJoker).containsValue("The Discard Pile"));
	}

	/*
	 * validPlayFor()
	 */
	// Jacks
	@Test
	public void vPF_WhenPlayingAJack_Include_RsWithQueens(){
		wj.retainer[0][0].add(aQueen);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aJack);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAJack_Include_RsWith10s(){
		wj.retainer[0][0].add(a10);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aJack);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAJack_DontInclude_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aJack);
		assertFalse(vp[0]);
	}

	// Queens
	@Test
	public void vPF_WhenPlayingAQueen_Include_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aQueen);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAQueen_Include_RsWithQueens(){
		wj.retainer[0][0].add(aQueen);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aQueen);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAQueen_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aQueen);
		assertFalse(vp[0]);
	}

	// 10s
	@Test
	public void vPF_WhenPlayingA10_Include_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], a10);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingA10_Include_RsWith10s(){
		wj.retainer[0][0].add(a10);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], a10);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingA10_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], a10);
		assertFalse(vp[0]);
	}

	// Aces
	@Test
	public void vPF_WhenPlayingAnAce_DontInclude_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], anAce);
		assertFalse(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAnAce_Include_RsWith3ofaKinds(){
		wj.retainer[0][0].add(a9);
		wj.retainer[0][0].add(a9);
		wj.retainer[0][0].add(a9);
		assertTrue(wj.retainer[0][0].size() == 3);
		assertTrue(wj.retainer[0][0].get(0).equals(wj.retainer[0][0].get(1)));
		assertTrue(wj.retainer[0][0].get(0).equals(wj.retainer[0][0].get(2)));
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], anAce);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAnAce_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], anAce);
		assertFalse(vp[0]);
	}

	// Jokers
	@Test
	public void vPF_WhenPlayingAJoker_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aJoker);
		assertFalse(vp[0]);
	}

	// Kings
	@Test
	public void vPF_WhenPlayingAKing_DontInclude_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aKing);
		assertFalse(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAKing_Include_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aKing);
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAKing_DontInclude_RsWithNoJacks(){
		wj.retainer[0][0].add(a9);
		assertFalse(wj.retainer[0][0].retainsJack());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aKing);
		assertFalse(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAKing_DontInclude_RsWithKings(){
		wj.retainer[0][0].add(aKing);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], aKing);
		assertFalse(vp[0]);
	}

	// 2-9s
	@Test
	public void vPF_WhenPlayingA2thru9_Include_EmptyRs(){
		for(int value = 2; value <= 9; value++){
			wj.retainer[0][0].empty();
			boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], new Card(value, 1));
			assertTrue(vp[0]);
		}
	}
	@Test
	public void vPF_WhenPlayingA2thru9_Include_RsWithSameValue(){
		for(int value = 2; value <= 9; value++){
			wj.retainer[0][0].empty();
			wj.retainer[0][0].add(new Card(value, 1));
			boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], new Card(value, 1));
			assertTrue(vp[0]);
		}
	}

}
