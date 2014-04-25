import java.io.IOException;
import java.util.HashMap;

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

	/**
	 * Creates the game environment with two players, their array of 4
	 * retainers each, the deck, the discard pile, and activePlayer, the
	 * record of whose turn it is.
	 */
	WanderingJacks(){
		activePlayer = 0;
		onFirstPlayFromHandOfTurn = true;
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
		wj.setUpGameEnvironment();
		int[] playRequest = new int[3];
		//while the game is not over
		while(!wj.isGameOver()){
			// https://www.pivotaltracker.com/story/show/69883240
			// 1. Display game state
			ConsoleUI.draw(wj);
			// 2. ask discard or deck
			playRequest[0] = ConsoleUI.promptPlayerToDrawInitialCard();
			// 3. move card from origin to hand
			playRequest[1] = ConsoleUI.cardLocation("My Hand");
			playRequest[2] = -1;
			wj.requestPlay(playRequest);
			// 4. Display game state
			ConsoleUI.draw(wj);
			boolean endThisTurn = false;
			while(endThisTurn == false){
				boolean commitThisPlay = false;
				while(commitThisPlay == false){
					playRequest = new int[3];
					// 5. ask which card-from-hand to play
					// 	5.1 list all cards in hand
					// 	5.2 If drew a joker, only option is the Joker
					//  5.3 if not first move, show end turn as option
					playRequest[0] = ConsoleUI.cardLocation("My Hand");
					int handIndex = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], wj.onFirstPlayFromHandOfTurn) - 1; // -1 to translate from displayed option to actual hand index
					if(handIndex == ConsoleUI.cardLocation("End Turn")){
						commitThisPlay = true;
						endThisTurn = true;
					}else{
						String cardFromHand = ""; // Should never be used
						String prompt = "Enter *to* where to play your ";
						if(handIndex <= wj.player[wj.activePlayer].handSize() + 4){ // Single Card Play
							cardFromHand = wj.player[wj.activePlayer].getFromHand(handIndex).getValueAsString();
							// 6. ask which of available destinations to go
							//	6.1 first time only, show discard pile
							//	6.2 show -back- as destination
							//	6.3 check validation rules for which retainer is valid
							//  6.4 If playing Joker, cannot discard
							prompt += cardFromHand.toString()+":";
						}else if(handIndex == 101){ // Den of Thieves
							cardFromHand = "Den of Thieves";
							prompt += cardFromHand+":";
						}
						playRequest[1] = ConsoleUI.getPlayerInput(prompt, wj.getPossibleDestinations(cardFromHand));
						playRequest[2] = handIndex;
						//  6.5 if go back chosen, loop to 5 (ask which card from hand to play)
						if(commitThisPlay = (playRequest[1] != ConsoleUI.cardLocation("Go Back") ? true : false));
					}
				}
				if(endThisTurn == false){
					// 7. move card from hand to destination
					wj.requestPlay(playRequest);
					//  7.1 No longer be the first play-from-hand of turn
					wj.onFirstPlayFromHandOfTurn = false;
					// 8. perform any fancy moves like 3oak+A
					//	8.1 prompt user for opponent's retainer if necessary
					// 9. display game state
					ConsoleUI.draw(wj);
					// 10. ask to end turn or loop to 5 (ask which card from hand to play)
					//  10.1 choosing discard pile as destination ends turn automatically
					if(playRequest[1] == ConsoleUI.cardLocation("The Discard Pile"))
						endThisTurn = true;
					else endThisTurn = ConsoleUI.promptPlayerToLoopOrEndTurn();
				}
			}
			//11. END TURN
			wj.endTurn();
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
		if(discardPile.isEmpty())
			discardPile.discard(deck.dealCard());
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
	public void threeOfAKindPlusAce(Card ace, int prIndex, int orIndex){
		// Check if both indexes are within bounds
		if(prIndex > 3 || prIndex < 0) throw new IllegalArgumentException(prIndex + " is not within the range of valid retainers, 0-3");
		if(orIndex > 3 || orIndex < 0) throw new IllegalArgumentException(orIndex + " is not within the range of valid retainers, 0-3");
		// Check if the ace is an ace
		if(!ace.getValueAsString().equals("Ace")) throw new IllegalArgumentException("The ace is not an ace");
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
		// Play the Ace
		r1.add(ace);
		// Discard all but the bottom card
		while(r1.size() > 1)
			discardPile.discard(r1.remove(1));
		// Swap retainers
		retainer[activePlayer][prIndex] = r2;
		retainer[(activePlayer == 0 ? 1 : 0)][orIndex] = r1;
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

		WanderingJacks.makePlay(this, fromHere, toThere, handIndex);
		return true;
	}

	public HashMap<Integer, String> getPossibleDestinations(String cardToPlay){
		HashMap<Integer, String> pd = new HashMap<Integer, String>();
		pd.put(ConsoleUI.cardLocation("Go Back"), "Go Back");
		//  6.1 first time only, show discard pile
		//  6.4 If playing Joker, cannot discard
		if(onFirstPlayFromHandOfTurn
		&& !cardToPlay.equals("Joker"))
			pd.put(ConsoleUI.cardLocation("The Discard Pile"), "The Discard Pile");
		String output;
		boolean[] isValidDestination = WanderingJacks.validPlayFor(retainer[activePlayer], cardToPlay);
		for(int i = 0; i < retainer[activePlayer].length; i++){
			if(isValidDestination[i]){
				output = "Retainer:";
				for(int j = 0; j < retainer[activePlayer][i].size(); j++)
					output += " ["+i+"]["+j+"] = "+retainer[activePlayer][i].get(j).toString();
				pd.put(i+4, output);
			}
		}
		return pd;
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

		if(fromHere == 3 && handIndex == 101){ // Den of Thieves
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
		while(p.handSize() > 0 && numAces <= 3)
			if(p.getFromHand(index).getValue() == Card.ACE){
				game.retainer[game.activePlayer][ri].add(p.playFromHand(index));
				numAces++;
			}else index++;
	}
}
