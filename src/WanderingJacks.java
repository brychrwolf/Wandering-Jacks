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

public class WanderingJacks {
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
	 * One of the two players in the game. Has a hand of cards, and a
	 * bankroll with which to bet. Plays the part of the gambler.
	 * @see Player
	 */
	public Player player;
	/**
	 * One of the two players in the game. Has a hand of cards, and a
	 * bankroll with which to bet. Plays the part of the house.
	 * @see Player
	 */
	public Player house;
	/**
	 * An array of the player's 4 retainers.
	 * @see Retainer
	 */
	public Retainer[] playerRetainer;
	/**
	 * An array of the dealer's 4 retainers.
	 * @see Retainer
	 */
	public Retainer[] houseRetainer;

	/**
	 * Creates the game environment with two players, their array of 4
	 * retainers each, the deck, and discard pile.
	 */
	WanderingJacks(){
		boolean includeJokers = true;
		deck = new Deck(includeJokers);
		discardPile = new DiscardPile();
		player = new Player();
		house = new Player();
		playerRetainer = new Retainer[4];
		houseRetainer = new Retainer[4];
		for(int i = 0; i < 4; i++){
			playerRetainer[i] = new Retainer();
			houseRetainer[i] = new Retainer();
		}
	}

	public static void main(String[] args) {
		WanderingJacks wj = new WanderingJacks();
		wj.setUpGameEnvironment();
		//while the game is not over
		while(!wj.isGameOver()){
		//	player decides if he wants a card from the deck or the discard pile
			boolean takeFromDeck = true;
		//	player is dealt a card from the deck or discard pile
			if(takeFromDeck)
				wj.player.addToHand(wj.deck.dealCard());
			else wj.player.addToHand(wj.discardPile.takeTopCard());
		//	if Joker is drawn, it must be played immediately
		//	player decides how to play, discard 1 or play at least 1 to retainers
			int cardIndex = 0;
			int retainerIndex = 0;
		//	player plays
			wj.playerRetainer[retainerIndex].add(wj.player.playFromHand(cardIndex));
		//	End of Turn maintenance
			wj.endTurn();
			wj.player.setBankroll(0);
		}System.out.println("Game Over.");
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
		if(playerRetainer[0].retainsJack()
				&& playerRetainer[1].retainsJack()
				&& playerRetainer[2].retainsJack()
				&& playerRetainer[3].retainsJack()){
			return true;
		// Dealer wins
		}else if(houseRetainer[0].retainsJack()
					&& houseRetainer[1].retainsJack()
					&& houseRetainer[2].retainsJack()
					&& houseRetainer[3].retainsJack()){
			return true;
		// Player loses by running out of money
		}else if(player.getBankroll() <= 0){
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
			while(playerRetainer[i].isEmpty() == true){
				rcard = deck.dealCard();
				if(rcard.getValue() == Card.JACK || rcard.getValue() == Card.ACE || rcard.getValue() == Card.JOKER || rcard.getValue() == Card.KING)
					discardPile.discard(rcard);
				else playerRetainer[i].add(rcard);}
			while(houseRetainer[i].isEmpty() == true){
				rcard = deck.dealCard();
				if(rcard.getValue() == Card.JACK || rcard.getValue() == Card.ACE || rcard.getValue() == Card.JOKER || rcard.getValue() == Card.KING)
					discardPile.discard(rcard);
				else houseRetainer[i].add(rcard);
			}
		}
		// deal 3 cards one player's hand, then the other, discarding Jokers
		while(player.getHand().size() < 3){
			rcard = deck.dealCard();
			if(rcard.getSuit() == Card.JOKER)
				discardPile.discard(rcard);
			else player.getHand().add(rcard);}
		while(house.getHand().size() < 3){
			rcard = deck.dealCard();
			if(rcard.getSuit() == Card.JOKER)
				discardPile.discard(rcard);
			else house.getHand().add(rcard);}
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
	public void validMoves(Card card){
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
	}

	/**
	 * This cleans up the state of the game environment at the end of every turn
	 * as provided by the rules of the game.
	 */
	public void endTurn(){
		//multiple queens steal Jacks (or Kings) from stacks built on 10s
		//multiple 10s steal Jacks (or Kings) from stacks built on Queens
		//den of thieves steal Jacks (or Kings)
		//Jacks on Q,10,DoT stacks, burn stack
		//ensure that player's hand has 3 cards
		while(player.getHand().size() < 3)
			player.addToHand(deck.dealCard());
		//If discardPile is empty, discard one from deck
		if(discardPile.isEmpty())
			discardPile.discard(deck.dealCard());
	}

	/**
	 * This swaps completes the 3oak+A play by discarding all but the bottom
	 * card in the player's retainer, then swapping that retainer with any of
	 * the opponent's (that does not contain a Jack)
	 * @param i is the player's first initial, either (p)layer or (h)ouse
	 * @param ace is the card to play on the player's retainer
	 * @param prIndex is the index for which player's retainer to swap
	 * @param orIndex is the index for which opponent's retainer to swap
	 * @throws IllegalArgumentException if ace is not an ace
	 * @throws IllegalArgumentException if the player's initial is not either 'p' or 'h'
	 * @throws IllegalStateException if the opponent's retainer contains a Jack
	 * @throws IllegalStateException if the player's retainer does not contain a 3-of-a-kind
	 */
	public void threeOfAKind(char i, Card ace, int prIndex, int orIndex){
		// Check if both indexes are within bounds
		if(prIndex > 3 || prIndex < 0) throw new IllegalArgumentException(prIndex + " is not within the range of valid retainers, 0-3");
		if(orIndex > 3 || orIndex < 0) throw new IllegalArgumentException(orIndex + " is not within the range of valid retainers, 0-3");
		// Check if the ace is an ace
		if(!ace.getValueAsString().equals("Ace")) throw new IllegalArgumentException("The ace is not an ace");
		// normalize retainer references
		Retainer r1, r2;
		if(i == 'p' || i == 'P'){
			r1 = playerRetainer[prIndex];
			r2 = houseRetainer[orIndex];
		}else if(i == 'p' || i == 'P'){
			r1 = houseRetainer[orIndex];
			r2 = playerRetainer[prIndex];
		}else throw new IllegalArgumentException("The player's first initial must be either 'p' or 'h', not '"+i+"'");
		// Check if player has a three a kind in that retainer
		if(r1.get(0).getValueAsString() != r1.get(1).getValueAsString() ||
			r1.get(0).getValueAsString() != r1.get(2).getValueAsString())
			throw new IllegalStateException("The player's retainer must not contain a 3-of-a-kind.");
		// Check for a Jack in the opponent's retainer
		if(r2.retainsJack()) throw new IllegalStateException("The opponent's retainer must not contain a Jack");
		// Play the Ace
		r1.add(ace);
		// Discard all but the bottom card
		while(r1.size() > 1)
			discardPile.discard(r1.remove(1));
		// Swap retainers
		Retainer rt = r1;
		r1 = r2;
		r2 = rt;
		if(i == 'p' || i == 'P'){
			playerRetainer[prIndex] = r1;
			houseRetainer[orIndex]= r2;
		}else if(i == 'p' || i == 'P'){
			houseRetainer[orIndex] = r1;
			playerRetainer[prIndex] = r2;
		}else throw new IllegalArgumentException("The player's first initial must be either 'p' or 'h', not '"+i+"'");
	}

	/*public Double probabilityOfDrawingA(char pOrD, String target){
		//total cards = 54
		int tc = 54;
		//known cards = 0-3 my hand, 0-13 my retainers, 0-13 opponant's retainers, 0-54 discard pile
		int kc = 0;
		int st = 0;
		for(Card[] i : pr){
			for(Card j : i){
				if(j != null){
					kc++;
					if(j.getValueAsString() == target) st++;
				}
			}
		}
		for(Card[] i : dr){
			for(Card j : i){
				if(j != null){
					kc++;
					if(j.getValueAsString() == target) st++;
				}
			}
		}
		if(pOrD == 'p'){
			for(Card i : p.getHand()){
				kc++;
				if(i.getValueAsString() == target) st++;
			}
		}else if(pOrD == 'd'){
			for(Card i : d.getHand()){
				kc++;
				if(i.getValueAsString() == target) st++;
			}
		}else{
			throw new IllegalArgumentException("Invalid (p)layer or (d)ealer parameter.");
		}
		for(Card i : discard_pile){
			kc++;
			if(i.getValueAsString() == target) st++;
		}
		//unknown cards = total cards - known cards
		int uc = tc - kc;
		//total targets = 4 (unless joker)
		int tt = (target == "Joker" ? 2 : 4);
		//seen targets = targets within known cards
		//hidden targets = total targets - seen targets
		System.out.println("Seen "+target+"s = "+st);
		int ht = tt - st;
		//Probability of drawing target = hidden targets / unknown cards
		return (double) ht / uc;
	}*/


}
