import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class WanderingJacksTest{
	private final String newLine = System.getProperty("line.separator");

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
		assertSame(wj.onFirstPlayFromHandOfTurn, clone.onFirstPlayFromHandOfTurn);
	}

	@Test
	public void cloningGame_CreatesNewButEqual_hasAnEmptyRetainer(){
		WanderingJacks clone = new WanderingJacks(wj);
		assertNotSame(wj.hasAnEmptyRetainer, clone.hasAnEmptyRetainer);
		for(int i = 0; i < 2; i++){
			assertEquals(wj.hasAnEmptyRetainer[i], clone.hasAnEmptyRetainer[i]);
		}
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

	/*
	 * playNewGame()
	 */
	@Test
	public void playNewGame(){
		fail("not implemented");
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
		assertTrue(wj.onFirstPlayFromHandOfTurn == (boolean)wj.onFirstPlayFromHandOfTurn);
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
	public void afterSettingUpGame_FirstCardInRegisterIsNotAForbiddenCard(){
		wj.setUpGameEnvironment();
		HashSet<Integer> forbiddenValues = new HashSet<Integer>();
		forbiddenValues.add(Card.JACK);
		forbiddenValues.add(Card.ACE);
		forbiddenValues.add(Card.JOKER);
		forbiddenValues.add(Card.KING);
		for(int i = 0; i < 2; i++)
			for(Retainer r : wj.retainer[i])
				assertFalse(forbiddenValues.contains(r.get(0).getValue()));
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
	public void threeOakPA_exThrown_PlayerHasNo3oak(){
		wj.move_3oakPlusAce(0, 0);
	}

	@Test(expected=IllegalStateException.class)
	public void threeOakPA_exThrown_ORcontainsAJack(){
		wj.retainer[0][0] = three9s;
		wj.retainer[1][0].add(aJack);
		wj.move_3oakPlusAce(0, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void threeOakPA_exThrown_PIndexOutOfBounds(){
		wj.retainer[0][0] = three9s;
		wj.move_3oakPlusAce(5, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void threeOakPA_exThrown_OIndexOutOfBounds(){
		wj.retainer[0][0] = three9s;
		wj.move_3oakPlusAce(0, 5);
	}

	@Test
	public void threeOakPA_SwapsRetainers(){
		wj.retainer[0][0] = three9s;
		wj.retainer[1][0] = twoQueens;
		wj.move_3oakPlusAce(0, 0);
		assertTrue(wj.retainer[0][0].size() == 2);
		assertTrue(wj.retainer[0][0].get(0).equals(aQueen));
		assertTrue(wj.retainer[0][0].get(1).equals(aQueen));
		assertTrue(wj.retainer[1][0].size() == 1);
		assertTrue(wj.retainer[1][0].get(0).equals(a9));
		assertTrue(wj.discardPile.peekAtTopCard().equals(a9));
	}
	@Test
	public void threeOakPA_turnsOn_needToCoverDiscardedCard(){
		wj.retainer[0][0] = three9s;
		wj.retainer[1][0] = twoQueens;
		assertFalse(wj.needToCoverDiscardedCard);
		wj.move_3oakPlusAce(0, 0);
		assertTrue(wj.needToCoverDiscardedCard);
	}

	/*
	 * moveJoker
	 */
	@Test(expected=IllegalArgumentException.class)
	public void moveJoker_exThrown_PIndexOutOfBounds(){
		wj.moveJoker(5, 0);
	}
	@Test(expected=IllegalArgumentException.class)
	public void moveJoker_exThrown_OIndexOutOfBounds(){
		wj.moveJoker(0, 5);
	}
	@Test
	public void moveJoker_DiscardEntirePlayerRetainer(){
		wj.retainer[0][0].add(a9);
		wj.retainer[0][0].add(a9);
		wj.moveJoker(0, 0);
		assertTrue(wj.retainer[0][0].isEmpty());
	}
	@Test
	public void moveJoker_DiscardOpJack(){
		wj.retainer[1][0].add(aJack);
		assertTrue(wj.retainer[1][0].contains(Card.JACK));
		wj.moveJoker(0, 0);
		assertFalse(wj.retainer[1][0].contains(Card.JACK));
	}
	@Test
	public void moveJoker_UpdatesRetainsJack_WhenDiscardOpJack(){
		wj.retainer[1][0].add(aJack);
		assertTrue(wj.retainer[1][0].contains(Card.JACK));
		wj.moveJoker(0, 0);
		assertFalse(wj.retainer[1][0].contains(Card.JACK));
		assertFalse(wj.retainer[1][0].retainsJack());
	}
	@Test
	public void moveJoker_DiscardOpKing(){
		wj.retainer[1][0].add(aKing);
		int origSize = wj.retainer[1][0].size();
		wj.moveJoker(0, 0);
		assertEquals(origSize - 1, wj.retainer[1][0].size());
	}
	@Test
	public void moveJoker_DiscardOpKing_WhenJackAndKingPresent(){
		wj.retainer[1][0].add(aJack);
		wj.retainer[1][0].add(aKing);
		wj.retainer[1][1].add(aKing);
		wj.retainer[1][1].add(aJack);
		int origSizeA = wj.retainer[1][0].size();
		int origSizeB = wj.retainer[1][1].size();
		assertTrue(wj.retainer[1][0].contains(Card.JACK));
		assertTrue(wj.retainer[1][1].contains(Card.JACK));
		wj.moveJoker(0, 0);
		wj.moveJoker(0, 1);
		assertEquals(origSizeA - 1, wj.retainer[1][0].size());
		assertEquals(origSizeB - 1, wj.retainer[1][1].size());
		assertTrue(wj.retainer[1][0].contains(Card.JACK));
		assertTrue(wj.retainer[1][1].contains(Card.JACK));
	}
	@Test
	public void moveJoker_DiscardOpAces_WhenDiscardingJack(){
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(anAce);
		wj.retainer[1][0].add(anAce);
		wj.retainer[1][0].add(aJack);
		wj.moveJoker(0, 0);
		assertEquals(wj.retainer[1][0].size(), 1);
		assertFalse(wj.retainer[1][0].contains(Card.JACK));
		assertFalse(wj.retainer[1][0].contains(Card.ACE));
	}
	@Test
	public void moveJoker_DontDiscardOpAces_WhenDiscardingKing(){
		wj.retainer[1][0].add(a10);
		wj.retainer[1][0].add(anAce);
		wj.retainer[1][0].add(aKing);
		wj.retainer[1][0].add(aJack);
		wj.moveJoker(0, 0);
		assertEquals(wj.retainer[1][0].size(), 3);
		assertTrue(wj.retainer[1][0].contains(Card.JACK));
		assertTrue(wj.retainer[1][0].contains(Card.ACE));
		assertFalse(wj.retainer[1][0].contains(Card.KING));
	}
	@Test
	public void moveJoker_turnsOn_needToCoverDiscardedCard(){
		assertFalse(wj.needToCoverDiscardedCard);
		wj.moveJoker(0, 0);
		assertTrue(wj.needToCoverDiscardedCard);
	}
	@Test
	public void moveJoker_acceptsNeg1ForOpIndex(){
		wj.moveJoker(0, -1);
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

	@Test
	public void endingTurnDiscardsIfPileIsEmpty(){
		assertTrue(wj.discardPile.isEmpty());
		wj.endTurn();
		assertTrue(wj.discardPile.size() == 1);
	}

	@Test
	public void endingTurnDiscardsIf_NtCDC_IsTrue(){
		wj.discardPile.discard(new Card(Card.JACK, 1));
		int origDpSize = wj.discardPile.size();
		wj.needToCoverDiscardedCard = true;
		wj.endTurn();
		assertEquals(wj.discardPile.size(), origDpSize + 1);
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
		int originalDiscardPileSize = wj.discardPile.size();
		int[] pr = {3, 2, 0}; // from my hand, to discard pile, with valid handIndex
		assertTrue(wj.requestPlay(pr));
		int newDiscardPileSize = wj.discardPile.size();
		assertEquals(newDiscardPileSize, originalDiscardPileSize + 1);
		assertEquals(cardFromHand, wj.discardPile.peekAtTopCard());
	}

	@Test
	public void requestedPlay_FromHand_ToFirstRetainer_IsPlayedWhenValid(){
		wj.retainer[0][0].add(a9);
		wj.player[0].addToHand(a9);
		int[] pr = {3, 4, 0}; // from my hand, to My 1st Retainer, with valid handIndex
		assertTrue(wj.requestPlay(pr));
		assertEquals(wj.retainer[0][0].size(), 2);
		assertEquals(wj.retainer[0][0].get(1), a9);
	}

	@Test(expected=IllegalStateException.class)
	public void requestedPlayIsNotPlayedWhenInvalid(){
		int[] pr = {2, 3, 0}; // empty discard pile, to my hand
		wj.requestPlay(pr);
	}

	@Test
	public void requestedPlay_invalidHandIndex_Valid_IfHandIsNotChosenAsOrigin(){
		requestedPlay_ToHand_IsPlayedWhenValid();
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void requestedPlay_invalidHandIndex_Invalid_IfHandIsChosenAsOrigin(){
		wj.setUpGameEnvironment();
		int[] pr = {3, 2, -9}; // from hand, to discard pile, with invalid handIndex
		wj.requestPlay(pr);
	}

	@Test
	public void requestedPlay_isDenOfThieves_IsPlayedWhenValid(){
		wj.retainer[0][0].add(aQueen);
		wj.player[0].addToHand(anAce);
		wj.player[0].addToHand(anAce);
		wj.player[0].addToHand(anAce);
		int[] pr = {3, 4, 100}; // from hand, to 1st retainer, with DenOfThieves
		assertTrue(wj.requestPlay(pr));
		assertEquals(wj.retainer[0][0].size(), 4);
		assertTrue(wj.retainer[0][0].get(0).equals(aQueen));
		assertTrue(wj.retainer[0][0].get(3).equals(anAce));
	}

	@Test
	public void requestedPlayFromHand_always_addsCardsToEnsure3inHandAtAllTimes(){
		wj.retainer[0][0].add(a9);
		wj.player[0].addToHand(a9);
		assertEquals(wj.player[0].handSize(), 1);
		int origDeckSize = wj.deck.cardsLeft();
		int[] pr = {3, 4, 0}; // from hand, to 1st retainer, my 1 card
		assertTrue(wj.requestPlay(pr));
		assertEquals(wj.player[0].handSize(), 3);
		assertEquals(wj.deck.cardsLeft(), origDeckSize - 3); // -3 because players hand was 1, played 1, so 0 left is 3 away from target 3
	}

	@Test
	public void requestedPlayFromHand_shufflesDeck_whenNotEnoughToEnsure3inHandAtAllTimes(){
		wj.retainer[0][0].add(a9);
		wj.player[0].addToHand(a9);
		while(wj.deck.cardsLeft() > 0)
			wj.discardPile.discard(wj.deck.dealCard());
		int origDeckSize = wj.deck.cardsLeft();
		int origDiscardPileSize = wj.discardPile.size();
		int[] pr = {3, 4, 0}; // from hand, to 1st retainer, my 1 card
		assertTrue(wj.requestPlay(pr));
		assertEquals(wj.player[0].handSize(), 3);
		assertEquals(wj.deck.cardsLeft(), origDeckSize + origDiscardPileSize - 4); // -3 for hand, -1 for discard pile
		assertEquals(wj.discardPile.size(), 1);
	}

	@Test
	public void requestedPlay_resultingInEmptyRetainer_forcesPlayerToPlayToRetainer(){
		fail("not implemented");
	}

	/*
	 * onFirstMoveOfTurn tracker
	 */
	@Test
	public void onFirstMoveOfTurnTrueAtConstuction(){
		assertTrue(wj.onFirstPlayFromHandOfTurn);
	}
	@Test
	public void onFirstMoveOfTurnTrueAfterEndOfTurn(){
		wj.onFirstPlayFromHandOfTurn = false;
		wj.endTurn();
		assertTrue(wj.onFirstPlayFromHandOfTurn);
	}

	/*
	 * Possible Destinations
	 */
	@Test
	public void pDs_Always_Returns_ANonEmptyResult(){
		wj.setUpGameEnvironment();
		wj.onFirstPlayFromHandOfTurn = true;
		assertTrue(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Queen").size() > 0);
		wj.onFirstPlayFromHandOfTurn = false;
		assertTrue(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Queen").size() > 0);
	}

	@Test
	public void pDs_Always_Include_GoBack(){
		wj.setUpGameEnvironment();
		wj.onFirstPlayFromHandOfTurn = true;
		assertTrue(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Queen").containsValue("Go Back"));
		wj.onFirstPlayFromHandOfTurn = false;
		assertTrue(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Queen").containsValue("Go Back"));
	}

	@Test
	public void pDs_OnFirstMove_Include_DiscardPile(){
		wj.setUpGameEnvironment();
		wj.onFirstPlayFromHandOfTurn = true;
		assertTrue(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Queen").containsValue("The Discard Pile"));
	}

	@Test
	public void pDs_NotOnFirstMove_DontInclude_DiscardPile(){
		wj.setUpGameEnvironment();
		wj.onFirstPlayFromHandOfTurn = false;
		assertFalse(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Queen").containsValue("The Discard Pile"));
	}

	@Test
	public void pDs_WhenPlayingAJoker_DontInclude_DiscardPile(){
		wj.setUpGameEnvironment();
		wj.onFirstPlayFromHandOfTurn = true;
		assertFalse(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Joker").containsValue("The Discard Pile"));
		wj.onFirstPlayFromHandOfTurn = false;
		assertFalse(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Joker").containsValue("The Discard Pile"));
	}

	@Test
	public void pDs_WhenPlayingADoT_DontInclude_DiscardPile(){
		wj.setUpGameEnvironment();
		wj.onFirstPlayFromHandOfTurn = true;
		assertFalse(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Den of Thieves").containsValue("The Discard Pile"));
		wj.onFirstPlayFromHandOfTurn = false;
		assertFalse(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, "Den of Thieves").containsValue("The Discard Pile"));
	}

	@Test
	public void pDs_Always_Include_ValidPlays(){
		wj.setUpGameEnvironment();
		for(int value = 1; value <= Card.KING; value++){
			Card aCard = new Card(value, 1);
			HashMap<Integer, String> pd = WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], wj.onFirstPlayFromHandOfTurn, aCard.getValueAsString());
			boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[wj.activePlayer], aCard.getValueAsString());
			assertEquals(vp[0], pd.containsKey(4));
			assertEquals(vp[1], pd.containsKey(5));
			assertEquals(vp[2], pd.containsKey(6));
			assertEquals(vp[3], pd.containsKey(7));
		}
	}

	@Test
	public void pDs_WhenPlayingADoT_Include_ValidRetainers(){
		Retainer[] rg = new Retainer[4];
		for(int i = 0; i < 4; i++)
			rg[i] = new Retainer();
		rg[0].add(aQueen);
		rg[1].add(aQueen); rg[1].add(aQueen);
		rg[2].add(a10);
		rg[3].add(a9);
		HashMap<Integer, String> pd = WanderingJacks.getPossibleDestinations(rg, true, "Den of Thieves");
		boolean[] vp = WanderingJacks.validPlayFor(rg, "Den of Thieves");
		assertTrue(vp[0]);
		assertFalse(vp[1]);
		assertTrue(vp[2]);
		assertFalse(vp[3]);
		assertEquals(vp[0], pd.containsKey(4));
		assertEquals(vp[1], pd.containsKey(5));
		assertEquals(vp[2], pd.containsKey(6));
		assertEquals(vp[3], pd.containsKey(7));
	}

	@Test
	public void pDs_whenPlayerHasAnEmptyRetainer_Include_OnlyEmptyRetainers(){
		wj.retainer[0][2].add(a9);
		wj.retainer[0][3].add(a9);
		HashMap<Integer, String> pd = WanderingJacks.getPossibleDestinations(wj.retainer[0], true, "Queen");
		assertTrue(pd.containsKey(4));
		assertTrue(pd.containsKey(5));
		assertFalse(pd.containsKey(6));
		assertFalse(pd.containsKey(7));
	}

	@Test
	public void pDs_whenPlayerHasAnEmptyRetainer_ButNoValidCard_DontInclude_EmptyRetainers(){
		wj.retainer[0][2].add(a9);
		wj.retainer[0][3].add(a9);
		HashMap<Integer, String> pd = WanderingJacks.getPossibleDestinations(wj.retainer[0], true, "aJack");
		assertFalse(pd.containsKey(4));
		assertFalse(pd.containsKey(5));
		assertFalse(pd.containsKey(6));
		assertFalse(pd.containsKey(7));
	}

	@Test
	public void pDs_whenPlayerHasAnEmptyRetainer_DontInclude_DiscardPile(){
		assertFalse(WanderingJacks.getPossibleDestinations(wj.retainer[wj.activePlayer], false, "9").containsValue("The Discard Pile"));
	}

	/*
	 * validPlayFor()
	 */
	// Jacks
	@Test
	public void vPF_WhenPlayingAJack_Include_RsWithQueens(){
		wj.retainer[0][0].add(aQueen);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Jack");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAJack_Include_RsWith10s(){
		wj.retainer[0][0].add(a10);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Jack");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAJack_DontIncludee_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Jack");
		assertFalse(vp[0]);
	}

	// Queens
	@Test
	public void vPF_WhenPlayingAQueen_Include_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Queen");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAQueen_Include_RsWithQueens(){
		wj.retainer[0][0].add(aQueen);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Queen");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAQueen_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Queen");
		assertFalse(vp[0]);
	}

	// 10s
	@Test
	public void vPF_WhenPlayingA10_Include_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "10");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingA10_Include_RsWith10s(){
		wj.retainer[0][0].add(a10);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "10");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingA10_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "10");
		assertFalse(vp[0]);
	}

	// Aces
	@Test
	public void vPF_WhenPlayingAnAce_DontInclude_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Ace");
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
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Ace");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAnAce_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Ace");
		assertFalse(vp[0]);
	}

	// Jokers
	@Test
	public void vPF_WhenPlayingAJoker_DontInclude_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Joker");
		assertFalse(vp[0]);
	}

	// Kings
	@Test
	public void vPF_WhenPlayingAKing_DontInclude_EmptyRs(){
		assertTrue(wj.retainer[0][0].isEmpty());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "King");
		assertFalse(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAKing_Include_RsWithJacks(){
		wj.retainer[0][0].add(aJack);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "King");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAKing_DontInclude_RsWithNoJacks(){
		wj.retainer[0][0].add(a9);
		assertFalse(wj.retainer[0][0].retainsJack());
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "King");
		assertFalse(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingAKing_DontInclude_RsWithKings(){
		wj.retainer[0][0].add(aKing);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "King");
		assertFalse(vp[0]);
	}

	// 2-9s
	@Test
	public void vPF_WhenPlayingA2thru9_Include_EmptyRs(){
		for(int value = 2; value <= 9; value++){
			wj.retainer[0][0].empty();
			boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], ""+value);
			assertTrue(vp[0]);
		}
	}
	@Test
	public void vPF_WhenPlayingA2thru9_Include_RsWithSameValue(){
		for(int value = 2; value <= 9; value++){
			wj.retainer[0][0].empty();
			wj.retainer[0][0].add(new Card(value, 1));
			boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], ""+value);
			assertTrue(vp[0]);
		}
	}

	// Den of Thieves
	@Test
	public void vPF_WhenPlayingADoT_DontInclude_EmptyRs(){
		wj.retainer[0][0].empty();
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Den of Thieves");
		assertFalse(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingADoT_DontInclude_RsWithMoreThanOneCard(){
		wj.retainer[0][0].add(aQueen);
		wj.retainer[0][0].add(aQueen);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Den of Thieves");
		assertFalse(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingADoT_Include_RsWithQueens(){
		wj.retainer[0][0].add(aQueen);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Den of Thieves");
		assertTrue(vp[0]);
	}
	@Test
	public void vPF_WhenPlayingADoT_Include_RsWith10s(){
		wj.retainer[0][0].add(a10);
		boolean[] vp = WanderingJacks.validPlayFor(wj.retainer[0], "Den of Thieves");
		assertTrue(vp[0]);
	}

	// Has Empty Retainer
	@Test
	public void vPF_WhenHasEmptyRetainer_Include_OnlyEmptyRs(){
		fail("not implemented");
	}

	/*
	 * ensureActivePlayerHasCardToPlayInEmptyRetainer()
	 */
	@Test
	public void eAPHCtPiER_playerHasFourCards() throws IOException{
		for(int i = 0; i < 3; i++)
			wj.player[0].addToHand(a9);
		wj.ensureActivePlayerHasCardToPlayInEmptyRetainer();
		assertEquals(wj.player[0].handSize(), 4);
	}

	@Test
	public void eAPHCtPiER_playerGetsAValidCardForRetainerBottoms() throws IOException{
		for(int i = 0; i < 3; i++)
			wj.player[0].addToHand(aJack);
		String mockUserInput = "1"+newLine;
		for(int i = 0; i < 25; i++)
			mockUserInput += "1"+newLine;

		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		wj.ensureActivePlayerHasCardToPlayInEmptyRetainer();
		System.setIn(System.in);

		boolean hasValidCard = false;
		for(int i = 0; i < wj.player[0].handSize(); i++)
			if(WanderingJacks.cardValuesValidForRetainerBottom.contains(wj.player[wj.activePlayer].getFromHand(i).getValue()))
				hasValidCard = true;
		assertTrue(hasValidCard);
	}

	/*
	 * checkForEmptyRetainers()
	 */
	@Test
	public void cfER_True_whenEmptyRs_Exists(){
		wj.retainer[0][0].empty();
		wj.checkForEmptyRetainers();
		assertTrue(wj.hasAnEmptyRetainer[0]);
	}

	@Test
	public void cfER_False_whenEmptyRs_DontExists(){
		for(int i = 0; i < wj.retainer[0].length; i++)
			wj.retainer[0][i].add(a9);
		wj.checkForEmptyRetainers();
		assertFalse(wj.hasAnEmptyRetainer[0]);
	}

	/*
	 * activateSpecialMoves
	 */
	@Test
	public void aSM_3oakPlusA(){
		fail("not implemented");
	}
	@Test
	public void aSM_joker(){
		fail("not implemented");
	}
	@Test
	public void aSM_4oak(){
		fail("not implemented");
	}

}
