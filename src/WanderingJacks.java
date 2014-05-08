import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Wandering Jacks is a two-player card game.
 * <p>
 * Wandering Jacks is a two-player, competitive card game that uses a single,
 * standard deck of cards (including the Jokers) and was authored by Xawn
 * Alexander, a Mensan from the USA, in conjunction with a novel which is
 * currently unnamed and still in development.
 * <p>
 * Full game rules can be found at the private repo wiki:
 * https://bitbucket.org/brychrwolf/wandering-jacks/wiki/Home
 * @author Bryan Wolfford
 * @version %I%, %G%
 * @see Deck
 * @see DiscardPile
 * @see Player
 * @see Retainer
 */

public class WanderingJacks{
	/**
	 * The player whose turn is active during the game. 0 = gambler, 1 = house.
	 * Will toggle when player declares that they are finished.
	 * @see Player
	 */
	public int activePlayer;
	public boolean onFirstPlayFromHandOfTurn;
	public boolean[] hasAnEmptyRetainer;
	public boolean needToCoverDiscardedCard;
	/**
	 * A standard deck of 54 cards (52 + 2 Jokers)used in the game. Its
	 * contents are "random" and secret.
	 * @see Deck
	 */
	public Deck deck;
	/**
	 * The discard pile where discarded cards go. Its contents are known.
	 * @see DiscardPile
	 */
	public DiscardPile discardPile;
	/**
	 * The array of two players in the game. Each has a hand of cards and a
	 * bankroll with which to bet.
	 * <p>
	 * Player[0] = the gambler, Player[1] = the house.
	 * @see Player
	 */
	public Player[] player;
	/**
	 * The array of the two players' group of 4 retainers.
	 * <p>
	 * RetainerGroup[0] = the gambler's 4 retainers
	 * RetainerGroup[0][0] = the gambler's first retainer
	 * RetainerGroup[0][0].get(0) = the first card in the gambler's first retainer
	 * RetainerGroup[1] = the house's 4 retainers
	 * @see Retainer
	 */
	public Retainer[][] retainer;

	public final static HashSet<Integer> cardValuesValidForRetainerBottom = new HashSet<Integer>();
	static{
		for(int i = 0; i <= 13; i++)
			cardValuesValidForRetainerBottom.add(i);
		cardValuesValidForRetainerBottom.remove(Card.ACE);
		cardValuesValidForRetainerBottom.remove(Card.JACK);
		cardValuesValidForRetainerBottom.remove(Card.KING);
	}

	/**
	 * Creates the game environment with two players, their array of 4
	 * retainers each, the deck, the discard pile, and activePlayer, the
	 * record of whose turn it is.
	 */
	WanderingJacks(){
		activePlayer = 0;
		onFirstPlayFromHandOfTurn = true;
		needToCoverDiscardedCard = false;
		hasAnEmptyRetainer = new boolean[2];
			hasAnEmptyRetainer[0] = true;
			hasAnEmptyRetainer[1] = true;
		deck = new Deck(true);
		discardPile = new DiscardPile();
		player = new Player[2];
		retainer = new Retainer[2][4];
		for(int i = 0; i < 2; i++){
			player[i] = new Player();
			for(int j = 0; j < 4; j++)
				retainer[i][j] = new Retainer();
		}
	}

	WanderingJacks(WanderingJacks cloned){
		this.activePlayer 		= cloned.activePlayer;
		this.onFirstPlayFromHandOfTurn 	= cloned.onFirstPlayFromHandOfTurn;
		this.needToCoverDiscardedCard 	= cloned.needToCoverDiscardedCard;
		this.hasAnEmptyRetainer = new boolean[2];
			this.hasAnEmptyRetainer[0] = cloned.hasAnEmptyRetainer[0];
			this.hasAnEmptyRetainer[1] = cloned.hasAnEmptyRetainer[1];
		this.deck 				= new Deck(cloned.deck);
		this.discardPile 		= new DiscardPile(cloned.discardPile);
		player = new Player[2];
		retainer = new Retainer[2][4];
		for(int i = 0; i < 2; i++){
			player[i] = new Player(cloned.player[i]);
			for(int j = 0; j < 4; j++)
				retainer[i][j] = new Retainer(cloned.retainer[i][j]);
		}
	}

	public static void main(String[] args) throws IOException {
		WanderingJacks wj = new WanderingJacks();
		wj.playNewGame();
	}

	public void playNewGame() throws IOException{
		setUpGameEnvironment();
		//stageGameEnvironment();
		int[] playRequest = new int[3];
		while(!isGameOver()){
			// https://www.pivot1altracker.com/story/show/69883240
			// display game state
			ConsoleUI.draw(this);
			// ask discard or deck
			playRequest[0] = ConsoleUI.promptPlayerToDrawInitialCard();
			// move card from origin to hand
			playRequest[1] = ConsoleUI.cardLocation("My Hand");
			playRequest[2] = -1;
			requestPlay(playRequest);
			// display game state
			ConsoleUI.draw(this);
			boolean endThisTurn = false;
			while(endThisTurn == false){
				boolean commitThisPlay = false;
				while(commitThisPlay == false){
					// If an empty retainer exists, ensure that player has card to play in an empty retainer
					if(hasAnEmptyRetainer[activePlayer]){
						System.out.println("You have an empty retainer.");
						ensureActivePlayerHasCardToPlayInEmptyRetainer();
					}
					playRequest = new int[3];
					// ask which card-from-hand to play
					playRequest[0] = ConsoleUI.cardLocation("My Hand");
					int handIndex = ConsoleUI.promptPlayerToChooseCardFromHand(player[activePlayer], hasAnEmptyRetainer[activePlayer], onFirstPlayFromHandOfTurn) - 1;
					if(handIndex == ConsoleUI.cardLocation("End Turn")){
						commitThisPlay = true;
						endThisTurn = true;
					}else{
						String cardFromHand = ""; // Should never be used
						String prompt = "Enter *to* where to play your ";
						if(handIndex <= player[activePlayer].handSize() + 4){ // Single Card Play
							cardFromHand = player[activePlayer].getFromHand(handIndex).getValueAsString();
							// 6. ask which of available destinations to go
							//	6.1 first time only, show discard pile
							//	6.2 show -back- as destination
							//	6.3 check validation rules for which retainer is valid
							//  6.4 If playing Joker, cannot discard
							prompt += player[activePlayer].getFromHand(handIndex).toString()+":";
						}else if(handIndex == 100){ // Den of Thieves
							cardFromHand = "Den of Thieves";
							prompt += cardFromHand+":";
						}
						playRequest[1] = ConsoleUI.getPlayerInput(prompt, WanderingJacks.getPossibleDestinations(retainer[activePlayer], onFirstPlayFromHandOfTurn, cardFromHand));
						playRequest[2] = handIndex;
						//  6.5 if go back chosen, loop to 5 (ask which card from hand to play)
						if(commitThisPlay = (playRequest[1] != ConsoleUI.cardLocation("Go Back") ? true : false));
					}
				}
				if(endThisTurn == false){
					// move card from hand to destination
					requestPlay(playRequest);
					//  no longer on the first play-from-hand of turn
					onFirstPlayFromHandOfTurn = false;
					// activate moves: 3oak+A, 4oak, and jokers
					//	  prompt user for opponent's retainer if necessary
					activateSpecialMoves(playRequest);
					// display game state
					ConsoleUI.draw(this);
					// check for empty retainers
					checkForEmptyRetainers();
					// If have no empty retainers, ask to end turn or loop turn
					// else just loop
					//    choosing discard pile as destination ends turn automatically
					if(playRequest[1] == ConsoleUI.cardLocation("The Discard Pile"))
						endThisTurn = true;
					else if(!hasAnEmptyRetainer[activePlayer]
					&& !player[activePlayer].handContains("Joker"))
						endThisTurn = ConsoleUI.promptPlayerToLoopOrEndTurn();
				}
			}
			//11. END TURN
			this.endTurn();
		}System.out.println("Game Over.");
	}

	/**
	 * Attempts to steal Jack to player's retainer from an opponent's retainer
	 * <ul>
	 * <li>A opponent's King will be taken first.</li>
	 * <li>Having a King will prevent from taking a second king.</li>
	 * <li>There must be a Jack in the opponent's retainer to make attempt.</li>
	 * <li>Player must have no JAcks in the retainer to receive stolen card.</li>
	 *
	 *
	 * @param rIdx1
	 * @param rIdx2
	 */
	public void stealJack(int rIdx1, int rIdx2){
		Retainer r1 = retainer[activePlayer][rIdx1];
		Retainer r2 = retainer[(activePlayer == 1 ? 0 : 1)][rIdx2];
		// If player already has a Jack, throw exception
		if(r1.retainsJack())
			throw new IllegalStateException("It is illegal to steal to a retainer that already retains a Jack.");
		// If opponent does not have a Jack, throw exception
		if(!r2.retainsJack())
			throw new IllegalStateException("It is illegal to steal from a retainer that does not retain a Jack.");
		int indexOfCard;
		Card cardStolen;
		// if jack is king protected, take king instead
		if(r2.contains(Card.KING)){
			// if player already has a king, throw exception
			if(r1.contains(Card.KING))
				throw new IllegalStateException("Theft attempt would net a King, but it is illegal to have two Kings in a single register.");
			else indexOfCard = r2.indexOf(Card.KING);
		}else{
			indexOfCard = r2.indexOf(Card.JACK);
		}
		cardStolen = r2.remove(indexOfCard);
		r1.add(cardStolen);
	}

	/**
	 * Returns true if the game is over.
	 * <p>
	 * The game is over if the player or dealer ever has all 4 Jacks, one each in
	 * their retainers, or if the player's bankroll ever reaches or goes below 0.
	 * @return true is the game is over, false if otherwise.
	 */
	public boolean isGameOver(){
		// Player wins
		if(retainer[0][0].retainsJack()
				&& retainer[0][1].retainsJack()
				&& retainer[0][2].retainsJack()
				&& retainer[0][3].retainsJack()){
			return true;
		// Dealer wins
		}else if(retainer[1][0].retainsJack()
					&& retainer[1][1].retainsJack()
					&& retainer[1][2].retainsJack()
					&& retainer[1][3].retainsJack()){
			return true;
		// Player loses by running out of money
		}else if(player[0].getBankroll() <= 0){
			return true;
		}else return false;
	}

	/**
	 * Set up the game environment.
	 */
	public void setUpGameEnvironment(){
		// shuffle deck
		deck.shuffle();
		// deal a card to each players' retainers, alternating between each player, discarding Jacks, Aces, Jokers, and Kings
		Card rcard;
		for(int i = 0; i < 4; i++){
			while(retainer[0][i].isEmpty() == true){
				rcard = deck.dealCard();
				if(rcard.getValue() == Card.JACK || rcard.getValue() == Card.ACE || rcard.getValue() == Card.JOKER || rcard.getValue() == Card.KING)
					discardPile.discard(rcard);
				else retainer[0][i].add(rcard);}
			while(retainer[1][i].isEmpty() == true){
				rcard = deck.dealCard();
				if(rcard.getValue() == Card.JACK || rcard.getValue() == Card.ACE || rcard.getValue() == Card.JOKER || rcard.getValue() == Card.KING)
					discardPile.discard(rcard);
				else retainer[1][i].add(rcard);
			}
		}
		// deal 3 cards one player's hand, then the other, discarding Jokers
		while(player[0].handSize() < 3){
			rcard = deck.dealCard();
			if(rcard.getSuit() == Card.JOKER)
				discardPile.discard(rcard);
			else player[0].addToHand(rcard);}
		while(player[1].handSize() < 3){
			rcard = deck.dealCard();
			if(rcard.getSuit() == Card.JOKER)
				discardPile.discard(rcard);
			else player[1].addToHand(rcard);}
		// shuffle discard stack into deck, discard top card from the deck
		if(discardPile.size() > 0)
			deck.shuffleWithOtherCards(discardPile.takeAllCards());
		discardPile.discard(deck.dealCard());
	}



	/*
	 * Testing Environment Only!
	 */
	public void stageGameEnvironment(){
		deck.dealCard(); // Joker
		deck.dealCard(); // Joker

		// Den of thieves
		player[0].addToHand(new Card(Card.ACE, 1));
		player[0].addToHand(new Card(Card.ACE, 1));
		player[0].addToHand(new Card(Card.ACE, 1));
		retainer[0][0].add(new Card(8, 1));
		retainer[0][1].add(new Card(10, 1));
		retainer[0][2].add(new Card(9, 1));
		retainer[0][3].add(new Card(Card.QUEEN, 1));

		/*/3oak+A
		player[0].addToHand(new Card(Card.ACE, 1));
		retainer[0][0].add(new Card(3, 1));
		retainer[0][0].add(new Card(3, 1));
		retainer[0][0].add(new Card(3, 1));
		retainer[0][1].add(new Card(3, 1));
		retainer[0][2].add(new Card(3, 1));
		retainer[0][3].add(new Card(3, 1));
		retainer[1][0].add(new Card(Card.QUEEN, 1));
		retainer[1][0].add(new Card(Card.QUEEN, 1));
		retainer[1][1].add(new Card(7, 1));
		retainer[1][2].add(new Card(7, 1));
		retainer[1][3].add(new Card(7, 1));*/

		/*/moveJoker
		player[0].addToHand(new Card(Card.JOKER, Card.JOKER));
		retainer[0][0].add(new Card(3, 1));
		retainer[0][1].add(new Card(3, 1));
		retainer[0][2].add(new Card(3, 1));
		retainer[0][3].add(new Card(3, 1));
		retainer[1][0].add(new Card(Card.JACK, 1));
		retainer[1][1].add(new Card(7, 1));
		retainer[1][2].add(new Card(7, 1));
		retainer[1][3].add(new Card(7, 1));*/

		checkForEmptyRetainers();
	}

	/**
	 * This is a placeholder for a function that would either validate a given
	 * move or enumerate all the possible moves that a player may have, thus is
	 * currently unusable.
	 * @param card the Card from which the list of moves is evaluated against
	 */
	public static boolean[] validPlayFor(Retainer[] retainer, String playThis){
		boolean[] vr = {false, false, false, false};
		switch(playThis){
		case "Jack":
			//not if empty
			//not it contains a Jack
			//If contains a Queen
			//If contains a 10
			for(int i = 0; i < retainer.length; i++){
				if(!retainer[i].isEmpty()
				&& !retainer[i].retainsJack()
				&& (retainer[i].contains(Card.QUEEN)
				|| retainer[i].contains(10)))
					vr[i] = true;
			}break;
		case "Queen":
			//If is empty
			//If contains a queen
			//not if contains a Jack
			for(int i = 0; i < retainer.length; i++){
				if((retainer[i].isEmpty()
				|| retainer[i].contains(Card.QUEEN))
				&& !retainer[i].retainsJack())
					vr[i] = true;
			}break;
		case "10":
			//If is empty
			//If contains a 10
			//not if contains a Jack
			for(int i = 0; i < retainer.length; i++){
				if((retainer[i].isEmpty()
				|| retainer[i].contains(10))
				&& !retainer[i].retainsJack())
					vr[i] = true;
			}break;
		case "Ace":
			//not if empty
			//If contains three of a kind
			//not if contains a Jack
			for(int i = 0; i < retainer.length; i++){
				if(!retainer[i].isEmpty()
				&& !retainer[i].retainsJack()
				&& retainer[i].size() == 3
				&& retainer[i].get(0).getValue() == retainer[i].get(1).getValue()
				&& retainer[i].get(0).getValue() == retainer[i].get(2).getValue())
					vr[i] = true;
			/*/If player's hand has three aces, and a retainer contains only one Queen or one 10
			if(retainer[i].size() == 1
			&& (retainer[i].get(0).getValue() == Card.QUEEN
			|| retainer[i].get(0).getValue() == 10)){
				vr[i] = true;
			}*/
			}break;
		case "Joker":
			//If empty
			//not if contains a Jack
			for(int i = 0; i < retainer.length; i++){
				if(!retainer[i].retainsJack())
					vr[i] = true;
			}break;
		case "King":
			//if contains a Jack
			//not if contains a King
			for(int i = 0; i < retainer.length; i++){
				if(retainer[i].retainsJack()
				&& !retainer[i].contains(Card.KING))
					vr[i] = true;
			}break;
		case "2":
		case "3":
		case "4":
		case "5":
		case "6":
		case "7":
		case "8":
		case "9":
			//If empty
			//If bottom-most of retainer has same value
			for(int i = 0; i < retainer.length; i++){
				if(retainer[i].isEmpty())
					vr[i] = true;
				else if(!retainer[i].contains(Card.JACK)
				&& retainer[i].get(0).getValueAsString().equals(playThis))
					vr[i] = true;
			}
			break;
		case "Den of Thieves":
			//If has one and only one card
			//if that one is a Queen
			//if that one is a 10
			for(int i = 0; i < retainer.length; i++){
				if(retainer[i].size() == 1
				&& (retainer[i].get(0).getValueAsString().equals("Queen")
				|| retainer[i].get(0).getValueAsString().equals("10")))
					vr[i] = true;
			}
			break;
		}
		return vr;
	}

	/**
	 * This cleans up the state of the game environment at the end of every turn
	 * as provided by the rules of the game.
	 */
	public void endTurn(){
		// ensure that player's hand has 3 cards
		//
		// ensure that player's registers all have at least one card
		// Attempts to steal Jacks are made in order of Player's choice
		// 	* multiple queens steal Jacks (or Kings) from stacks built on 10s
		// 	* multiple 10s steal Jacks (or Kings) from stacks built on Queens
		//  * den of thieves steal Jacks (or Kings)
		// Jacks on Q, 10, DoT stacks, burn one from stack
		// If discardPile is empty, or if any card was discarded from any retainer,
		//	discard one from deck
		if(discardPile.isEmpty() || needToCoverDiscardedCard){
			Card drawnCard = deck.dealCard();
			System.out.println("The "+drawnCard.toString()+" was discarded.");
			discardPile.discard(drawnCard);
		}
		// toggle active player
		activePlayer = activePlayer == 1 ? 0 : 1;
		// make onFirstMoveOfTurn true
		onFirstPlayFromHandOfTurn = true;
	}

	/**
	 * This swaps completes the 3oak+A play by discarding all but the bottom
	 * card in the player's retainer, then swapping that retainer with any of
	 * the opponent's (that does not contain a Jack)
	 * @param ace is the card to play on the player's retainer
	 * @param prIndex is the index for which player's retainer to swap
	 * @param orIndex is the index for which opponent's retainer to swap
	 * @throws IllegalArgumentException if ace is not an ace
	 * @throws IllegalArgumentException if the player's initial is not either 'p' or 'h'
	 * @throws IllegalStateException if the opponent's retainer contains a Jack
	 * @throws IllegalStateException if the player's retainer does not contain a 3-of-a-kind
	 */
	public void move_3oakPlusAce(int prIndex, int orIndex){
		// Check if both indexes are within bounds
		if(prIndex > 3 || prIndex < 0) throw new IllegalArgumentException(prIndex + " is not within the range of valid retainers, 0-3");
		if(orIndex > 3 || orIndex < 0) throw new IllegalArgumentException(orIndex + " is not within the range of valid retainers, 0-3");
		// normalize retainer references
		Retainer r1 = retainer[activePlayer][prIndex];
		Retainer r2 = retainer[(activePlayer == 0 ? 1 : 0)][orIndex];
		// Check if player has a three a kind in that retainer
		try{
			if(r1.size() < 3)
				throw new IllegalStateException("The player's retainer must contain a 3-of-a-kind.");
			else if(r1.get(0).getValueAsString() != r1.get(1).getValueAsString() ||
					r1.get(0).getValueAsString() != r1.get(2).getValueAsString())
				throw new IllegalStateException("The player's retainer must contain a 3-of-a-kind.");
		}catch(IndexOutOfBoundsException e){
			throw new IllegalStateException("The player's retainer must contain a 3-of-a-kind.");}
		// Check for a Jack in the opponent's retainer
		if(r2.retainsJack()) throw new IllegalStateException("The opponent's retainer must not contain a Jack");
		// Discard all but the bottom card
		while(r1.size() > 1)
			discardPile.discard(r1.remove(1));
		// turn needToCoverDiscardedCard on
		needToCoverDiscardedCard = true;
		// Swap retainers
		retainer[activePlayer][prIndex] = r2;
		retainer[(activePlayer == 0 ? 1 : 0)][orIndex] = r1;
	}

	public void moveJoker(int prIndex, int orIndex){
		// Check if both indexes are within bounds
		if(prIndex > 3 || prIndex < 0) throw new IllegalArgumentException(prIndex + " is not within the range of valid retainers, 0-3");
		if(orIndex > 3 || orIndex < -1) throw new IllegalArgumentException(orIndex + " is not within the range of valid retainers, 0-3");
		// normalize retainer references
		Retainer r1 = retainer[activePlayer][prIndex];
		// Discard all pr's cards
		while(r1.size() >= 1)
			discardPile.discard(r1.remove(0));
		// turn needToCoverDiscardedCard on
		needToCoverDiscardedCard = true;
		if(orIndex != -1){
			// normalize retainer references
			Retainer r2 = retainer[(activePlayer == 0 ? 1 : 0)][orIndex];
			//check or for king, then jack, then aces to discard
			if(r2.contains(Card.KING)){
				discardPile.discard(r2.remove(r2.indexOf(Card.KING)));
			}else if(r2.contains(Card.JACK)){
				discardPile.discard(r2.remove(r2.indexOf(Card.JACK)));
				while(r2.contains(Card.ACE))
					discardPile.discard(r2.remove(r2.indexOf(Card.ACE)));
			}
		}
	}

	public void move4oak(int prIndex){
		while(!retainer[activePlayer][prIndex].isEmpty())
			retainer[activePlayer][prIndex].remove(0);
	}

	public boolean requestPlay(int[] request){
		int fromHere = request[0];
		int toThere = request[1];
		int handIndex =  request[2];
		// set up and play cloned environment for validation
		WanderingJacks inClone = new WanderingJacks(this);
		try{
			WanderingJacks.makePlay(inClone, fromHere, toThere, handIndex);
		}catch(IllegalStateException e){
			//e.printStackTrace();
			throw new IllegalStateException("That play would be illegal.");
		}
		// play in actual environment
		WanderingJacks.makePlay(this, fromHere, toThere, handIndex);
		// ensure players' hands stay at 3 cards after play
		if(fromHere == 3){
			while(player[activePlayer].handSize() < 3){
				if(deck.cardsLeft() == 0){
					deck.shuffleWithOtherCards(discardPile.takeAllCards());
					discardPile.discard(deck.dealCard());
				}
				Card drawnCard = deck.dealCard();
				player[activePlayer].addToHand(drawnCard);
				System.out.println("You drew the "+drawnCard.toString());
			}
		}
		return true;
	}

	public static HashMap<Integer, String> getPossibleDestinations(Retainer[] rg, boolean oFPfHoT, String cardToPlay){
		// determine if there is an empty retainer and which
		boolean anEmptyRetainerExists = false;
		boolean[] iIsEmpty = new boolean[rg.length];
		for(int i = 0; i < rg.length; i++){
			iIsEmpty[i] = rg[i].isEmpty();
			if(iIsEmpty[i] == true)
				anEmptyRetainerExists = true;
		}
		HashMap<Integer, String> pd = new HashMap<Integer, String>();
		pd.put(ConsoleUI.cardLocation("Go Back"), "Go Back");
		//  6.1 first time only, show discard pile
		//  6.4 If playing Joker, cannot discard
		if(oFPfHoT
		&& !cardToPlay.equals("Joker")
		&& !cardToPlay.equals("Den of Thieves"))
			pd.put(ConsoleUI.cardLocation("The Discard Pile"), "The Discard Pile");
		String output;
		boolean[] isValidDestination = WanderingJacks.validPlayFor(rg, cardToPlay);
		for(int i = 0; i < rg.length; i++){
			if(isValidDestination[i]
			&& (!anEmptyRetainerExists
			|| (anEmptyRetainerExists && iIsEmpty[i]))){
				output = "Retainer:";
				if(rg[i].size() == 0)
					output += " ["+i+"] empty";
				else for(int j = 0; j < rg[i].size(); j++)
					output += " ["+i+"]["+j+"] = "+rg[i].get(j).toString();
				pd.put(i+4, output);
			}
		}
		return pd;
	}

	public void ensureActivePlayerHasCardToPlayInEmptyRetainer(){
		// draw up to 4 cards in hand
		while(player[activePlayer].handSize() < 4){
			Card drawnCard = deck.dealCard();
			System.out.println("You drew the "+drawnCard.toString());
			player[activePlayer].addToHand(drawnCard);
		}

		int[] playRequest = new int[3];
		playRequest[0] = ConsoleUI.cardLocation("My Hand");
		// If have card to play in retainer, break
		boolean haveCardToPlay = false;
		while(!haveCardToPlay){
			for(int i = 0; i < player[activePlayer].handSize(); i++){
				if(WanderingJacks.cardValuesValidForRetainerBottom.contains(player[activePlayer].getFromHand(i).getValue()))
					haveCardToPlay = true;
			}
			if(!haveCardToPlay){
				int handIndex = ConsoleUI.promptPlayerToChooseCardToFillEmptyRetainer(player[activePlayer]) - 1;
				discardPile.discard(player[activePlayer].playFromHand(handIndex));
				Card drawnCard = deck.dealCard();
				System.out.println("You drew the "+drawnCard.toString());
				player[activePlayer].addToHand(drawnCard);
			}
		}
	}

	private static void makePlay(WanderingJacks game, int fromHere, int toThere, int handIndex){
		/*
		 * Translate string to card:
		 * cardLocations.put(1, "The Deck");
		 * cardLocations.put(2, "The Discard Pile");
		 * cardLocations.put(3, "My Hand");
		 * cardLocations.put(4, "My 1st Retainer");
		 * cardLocations.put(5, "My 2nd Retainer");
		 * cardLocations.put(6, "My 3rd Retainer");
		 * cardLocations.put(7, "My 4th Retainer");
		 */

		if(fromHere == 3 && handIndex == 100){ // Den of Thieves
			WanderingJacks.playDenOfThieves(game, toThere);
		}else{ // Single card play
			Card cardFromHere = new Card(); // Should never return as this value
			switch(fromHere){
				case 1:
					cardFromHere = game.deck.dealCard();
					break;
				case 2:
					cardFromHere = game.discardPile.takeTopCard();
					break;
				case 3:
					if(handIndex >= 0 && game.player[game.activePlayer].handSize() > handIndex)
						cardFromHere = game.player[game.activePlayer].playFromHand(handIndex);
					else throw new IndexOutOfBoundsException("Hand Index Out of Bounds");
					break;
				default:
					throw new IllegalStateException("That play would be illegal.");
			}
			// Move card to location
			switch(toThere){
				case 2:
					game.discardPile.discard(cardFromHere);
					break;
				case 3:
					game.player[game.activePlayer].addToHand(cardFromHere);
					break;
				case 4:
					game.retainer[game.activePlayer][0].add(cardFromHere);
					break;
				case 5:
					game.retainer[game.activePlayer][1].add(cardFromHere);
					break;
				case 6:
					game.retainer[game.activePlayer][2].add(cardFromHere);
					break;
				case 7:
					game.retainer[game.activePlayer][3].add(cardFromHere);
					break;
				default:
					throw new IllegalStateException("That play would be illegal.");
			}
		}
	}

	private static void playDenOfThieves(WanderingJacks game, int retainerIndex){
		Player p = game.player[game.activePlayer];
		int ri = retainerIndex -4; // -4 to translate from display to actual retainer index
		int numAces = 0;
		int index = 0;
		while(p.handSize() > 0 && numAces < 3)
			if(p.getFromHand(index).getValue() == Card.ACE){
				game.retainer[game.activePlayer][ri].add(p.playFromHand(index));
				numAces++;
			}else index++;
	}

	public void checkForEmptyRetainers(){
		for(int i = 0; i < 2; i++){
			hasAnEmptyRetainer[i] = false;
			for(int j = 0; j < retainer[i].length; j++)
				if(retainer[i][j].isEmpty())
					hasAnEmptyRetainer[i] = true;
		}
	}

	private void activateSpecialMoves(int[] playRequest){
		if(playRequest[1] >= 4 && playRequest[1] <= 7){
			int prIndex = playRequest[1] - 4;
			Retainer pr = retainer[activePlayer][prIndex];
			if(pr.size() == 4
			&& pr.get(0).getValue() == pr.get(1).getValue()
			&& pr.get(0).getValue() == pr.get(2).getValue()
			&& pr.get(3).getValue() == Card.ACE){
				// Move was a 3oak+A!
				int orIndex = ConsoleUI.promptPlayerToChooseOpponantsRetainer(retainer[(activePlayer == 1 ? 0 : 1)], "3oak+A");
				move_3oakPlusAce(prIndex, orIndex);
			}else if(pr.get(pr.size() - 1).getValue() == Card.JOKER){
				// Move was a Joker!
				// check if opponent has a jack or king first!
				boolean opHasCardsToBurn = false;
				for(int i = 0; i < 4; i++)
					if(retainer[activePlayer == 1 ? 0 : 1][i].contains(Card.JACK)
					|| retainer[activePlayer == 1 ? 0 : 1][i].contains(Card.KING))
						opHasCardsToBurn = true;
				int orIndex = -1;
				if(opHasCardsToBurn)
					orIndex = ConsoleUI.promptPlayerToChooseOpponantsRetainer(retainer[(activePlayer == 1 ? 0 : 1)], "joker");
				moveJoker(prIndex, orIndex);
			}else if(pr.size() == 4)
				if(pr.get(0).getValue() == pr.get(1).getValue()
				&& pr.get(0).getValue() == pr.get(2).getValue()
				&& pr.get(0).getValue() == pr.get(3).getValue()){
					// move was a 4oak!
					move4oak(prIndex);
				}
		}
	}
}
