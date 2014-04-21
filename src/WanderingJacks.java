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
	public boolean onFirstMoveOfTurn;
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
		boolean includeJokers = true;
		activePlayer = 0;
		onFirstMoveOfTurn = true;
		deck = new Deck(includeJokers);
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
		this.onFirstMoveOfTurn 	= cloned.onFirstMoveOfTurn;
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
		int[] playRequest = new int[2];
		//while the game is not over
		while(!wj.isGameOver()){
			// Display game state
			ConsoleUI.draw(wj);
			// player takes card to his hand from the deck or the discard pile
			playRequest[0] = ConsoleUI.promptPlayerToDrawInitialCard();
			playRequest[1] = ConsoleUI.cardLocation("My Hand");
			wj.requestPlay(playRequest);
			// Display game state
			ConsoleUI.draw(wj);
			// player plays a card from his hand to the discard pile or one of his registers
			playRequest[0] = ConsoleUI.cardLocation("My Hand");
			int handIndex = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer]);
			Card cardFromHand = wj.player[wj.activePlayer].getFromHand(handIndex);
			playRequest[1] = ConsoleUI.getPlayerInput("Enter *to* where to play your "+cardFromHand.toString()+":", wj.getPossibleDestinations(cardFromHand));
			playRequest[2] = handIndex;
			wj.requestPlay(playRequest);
			//int[] rp = new int[2];
			//rp[0] = ConsoleUI.getPlayerInput("Enter *from* where to play a card:", wj.getPossibleOrigins());
			// TODO how do I get card?
			//rp[1] = ConsoleUI.getPlayerInput("Enter *to* where to play a card:", wj.getPossibleDestinations(wj.retainer[wj.activePlayer], ));
			//wj.requestPlay(rp);
			// TODO if Joker is drawn, it must be played immediately (not yet implemented)
			// TODO player decides how to play (discard 1 or play at least 1 to retainers)
				//int cardIndex = 0;
				//int retainerIndex = 0;
			// player plays
				//wj.retainer[0][retainerIndex].add(wj.player[0].playFromHand(cardIndex));
			// End of Turn maintenance
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
		while(player[0].getHand().size() < 3){
			rcard = deck.dealCard();
			if(rcard.getSuit() == Card.JOKER)
				discardPile.discard(rcard);
			else player[0].getHand().add(rcard);}
		while(player[1].getHand().size() < 3){
			rcard = deck.dealCard();
			if(rcard.getSuit() == Card.JOKER)
				discardPile.discard(rcard);
			else player[1].getHand().add(rcard);}
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
	public static boolean[] validPlayFor(Retainer[] retainer, Card card){
		boolean[] vr = {false, false, false, false};
		switch(card.getValueAsString()){
		case "Jack":
			//if not empty
			//if a retainer contains a Queen and no Jacks
			//if a retainer contains a 10 and no Jacks
			for(int i = 0; i < retainer.length; i++){
				if(!retainer[i].isEmpty())
					vr[i] = true;
				else if(!retainer[i].contains(Card.JACK)){
					if(retainer[i].get(0).getValue() == Card.QUEEN
					|| retainer[i].get(0).getValue() == 10){
						vr[i] = true;
					}
				}
			}

			break;
		case "Queen":
			//If empty
			//If a retainer is empty
			//If a retainer contains a Queen and not a Jack
			for(int i = 0; i < retainer.length; i++){
				if(retainer[i].isEmpty())
					vr[i] = true;
				else if(!retainer[i].contains(Card.JACK)){
					if(retainer[i].get(0).getValue() == Card.QUEEN){
						vr[i] = true;
					}
				}
			}
			break;
		case "10":
			//If empty
			//If a retainer is empty
			//If a retainer contains a Queen and not a Jack
			for(int i = 0; i < retainer.length; i++){
				if(retainer[i].isEmpty())
					vr[i] = true;
				else if(!retainer[i].contains(Card.JACK)){
					if(retainer[i].get(0).getValue() == 10){
						vr[i] = true;
					}
				}
			}
			break;
		case "Ace":
			//If not empty
			//If a retainer contains three of a kind and no Jack
			for(int i = 0; i < retainer.length; i++){
				if(!retainer[i].isEmpty())
					vr[i] = true;
				else if(!retainer[i].contains(Card.JACK)
				&& retainer[i].size() == 3){
					if(retainer[i].get(0).getValue() == retainer[i].get(1).getValue()
					&& retainer[i].get(0).getValue() == retainer[i].get(2).getValue()){
						vr[i] = true;
					}
				}
				/*/If player's hand has three aces, and a retainer contains only one Queen or one 10
				if(retainer[i].size() == 1
				&& (retainer[i].get(0).getValue() == Card.QUEEN
				|| retainer[i].get(0).getValue() == 10)){
					vr[i] = true;
				}*/
			}
			break;
		case "Joker":
			//If empty
			//If the retainer does not contain a Jack
			for(int i = 0; i < retainer.length; i++){
				if(retainer[i].isEmpty())
					vr[i] = true;
				else if(!retainer[i].contains(Card.JACK)){
					vr[i] = true;
				}
			}
			break;
		case "King":
			//If not empty
			//if a retainer contains a Jack and no King
			for(int i = 0; i < retainer.length; i++){
				if(!retainer[i].isEmpty())
					vr[i] = true;
				else if(retainer[i].contains(Card.JACK)
				&& !retainer[i].contains(Card.KING))
					vr[i] = true;
			}
			break;
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
				&& retainer[i].get(0).getValue() == card.getValue())
					vr[i] = true;
			}
			break;
		}
		return vr;
	}
	/*public void validMoves(Card card){
		switch(card.getValueAsString()){
		case "Jack":
			//discard()
			//if a retainer contains a Queen and no Jacks
			//	play Jack to that retainer
			//if a retainer contains a 10 and no Jacks
			//	play Jack to that retainer
			break;
		case "Queen":
			//discard()
			//If a retainer is empty
			//	play a Queen to that retainer
			//If a retainer contains a Queen and not a Jack
			//	play a Queen to that retainer
			break;
		case "10":
			//discard()
			//If a retainer is empty
			//	play a 10 to that retainer
			//If a retainer contains a 10 and not a Jack
			//	play a 10 to that retainer
			break;
		case "Ace":
			//discard()
			//If a retainer contains three of a kind
			//	play Ace to that retainer,
			//	then discard retainer with Ace, except bottom-most card
			//		switch that retainer with any one of the opponant's retainer which does not contain a Jack
			//	dealer discards one card from the deck
			//If player's hand has three aces, and a retainer contains only a Queen or a 10
			//	play all three Aces on that retainer
			break;
		case "Joker":
			//If the player's hand contains a Joker,
			//	must play Joker to any retainer which does not contain a Jack (empty retainer is valid)
			//	discard that entire retainer, including the Joker
			//		discard any one of the opponant's Jacks (or Kings)
			//	draw from deck until hand has 4 cards
			//	play a valid card so that hand has 3 cards, and all retainers have at least one card
			//		if player has no valid card in hand (non Jacks, Aces and Jokers),
			//			discard one card, then draw another from the deck
			//	dealer shuffles discard pile into deck, then discards top card from deck
			break;
		case "King":
			//discard()
			//if a retainer contains a Jack and no King
			//	play King to that retainer
			break;
		case "2":
			//if retainer contains a 2
			//	play card to that retainer
		case "3":
			//if retainer contains a 3
			//	play card to that retainer
		case "4":
			//if retainer contains a 4
			//	play card to that retainer
		case "5":
			//if retainer contains a 5
			//	play card to that retainer
		case "6":
			//if retainer contains a 6
			//	play card to that retainer
		case "7":
			//if retainer contains a 7
			//	play card to that retainer
		case "8":
			//if retainer contains a 8
			//	play card to that retainer
		case "9":
			//if retainer contains a 9
			//	play card to that retainer

			//discard()
			//if retainer is empty
			//	play card to that retainer

			//if retainer contains three of a kind (same as card)
			//	play card to that retainer
			//	discard entire retainer
			//		discard any one of the opponant's Kings
			//	draw from deck until hand has 4 cards
			//	play a valid card so that hand has 3 cards, and all retainers have at least one card
			//		if player has no valid card in hand (non Jacks, Aces and Jokers),
			//			discard one card, then draw another from the deck
			//	dealer then discards top card from deck

			break;
		}
	}*/

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
		onFirstMoveOfTurn = true;
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
		int handIndex = -1;
		if(request.length == 3)
			handIndex = request[2];
		else if(fromHere == ConsoleUI.cardLocation("My Hand"))
			handIndex = ConsoleUI.promptPlayerToChooseCardFromHand(player[activePlayer]);
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

	private static void makePlay(WanderingJacks game, int fromHere, int toThere, int handIndex){
		/*
		 * Translate string to card:
		 * cardLocations.put(0, "The Deck");
		 * cardLocations.put(1, "The Discard Pile");
		 * cardLocations.put(2, "My Hand");
		 * cardLocations.put(3, "My 1st Retainer");
		 * cardLocations.put(4, "My 2nd Retainer");
		 * cardLocations.put(5, "My 3rd Retainer");
		 * cardLocations.put(6, "My 4th Retainer");
		 */
		Card cardFromHere = new Card(); // Should never return as this value
		switch(fromHere){
			case 0:
				cardFromHere = game.deck.dealCard();
				break;
			case 1:
				cardFromHere = game.discardPile.takeTopCard();
				break;
			case 2:
				if(handIndex >= 0 && game.player[game.activePlayer].handSize() > handIndex)
					cardFromHere = game.player[game.activePlayer].getFromHand(handIndex);
				else throw new IndexOutOfBoundsException("Hand Index Out of Bounds");
				break;
			default:
				throw new IllegalStateException("That play would be illegal.");
		}
		// Move card to location
		switch(toThere){
			case 1:
				game.discardPile.discard(cardFromHere);
				break;
			case 2:
				game.player[0].addToHand(cardFromHere);
				break;
			case 3:
				game.retainer[game.activePlayer][0].add(cardFromHere);
				break;
			case 4:
				game.retainer[game.activePlayer][1].add(cardFromHere);
				break;
			case 5:
				game.retainer[game.activePlayer][2].add(cardFromHere);
				break;
			case 6:
				game.retainer[game.activePlayer][3].add(cardFromHere);
				break;
			default:
				throw new IllegalStateException("That play would be illegal.");
		}
		// Can no longer be the first move of turn
		game.onFirstMoveOfTurn = false;
	}

	public HashMap<Integer, String> getPossibleOrigins(){
		HashMap<Integer, String> po = new HashMap<Integer, String>();
		int optNum = 0;
		if(onFirstMoveOfTurn) po.put(optNum++, "deck");
		if(onFirstMoveOfTurn) po.put(optNum++, "discard pile");
		if(!onFirstMoveOfTurn)
			for(int i = 0; i < player[activePlayer].handSize(); i++)
				po.put(optNum++, "from hand: "+player[activePlayer].getFromHand(i).toString());
		return po;
	}

	public HashMap<Integer, String> getPossibleDestinations(Card cardToPlay){
		HashMap<Integer, String> pd = new HashMap<Integer, String>();
		int optNum = 0;
		if(onFirstMoveOfTurn) pd.put(optNum++, "my hand");
		if(!onFirstMoveOfTurn) pd.put(optNum++, "discard pile & end turn");
		if(!onFirstMoveOfTurn){
			// Check every retainer
			String output;
			boolean[] isValidDestination = WanderingJacks.validPlayFor(retainer[activePlayer], cardToPlay);
			WanderingJacks.validPlayFor(retainer[activePlayer], cardToPlay);
			for(int i = 0; i < retainer[activePlayer].length; i++){
				// Add to options if the cardToPlay matches the bottom-most in retainer
				// or if cardToPlay is a Jack and Retainer is a Queen
				if(isValidDestination[i]){
					output = "retainer: ";
					for(int j = 0; j < retainer[activePlayer][i].size(); j++){
						output += retainer[activePlayer][i].get(j).toString()+" ";
					}
					pd.put(optNum++, output);
				}
			}
		}
		return pd;
	}

	public boolean onFirstMoveOfTurn(){
		return onFirstMoveOfTurn;
	}

}
