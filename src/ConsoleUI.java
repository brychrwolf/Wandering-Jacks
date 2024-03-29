import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;



public class ConsoleUI {
	public final static String newLine = System.getProperty("line.separator");
	private final static HashMap<Integer, String> cardLocations = new HashMap<Integer, String>();
	static{
		cardLocations.put(-1, "End Turn");
		cardLocations.put(0, "Go Back");
		cardLocations.put(1, "The Deck");
		cardLocations.put(2, "The Discard Pile");
		cardLocations.put(3, "My Hand");
		cardLocations.put(4, "My 1st Retainer");
		cardLocations.put(5, "My 2nd Retainer");
		cardLocations.put(6, "My 3rd Retainer");
		cardLocations.put(7, "My 4th Retainer");
	};

	public static String cardLocation(int location){
		if(cardLocations.containsKey(location))
			return cardLocations.get(location);
		else throw new NullPointerException("That location does not exist.");
	}

	public static int cardLocation(String location){
		int loc = -9; // should never return as -1
		if(cardLocations.containsValue(location)){
			for(int l : cardLocations.keySet()){
				if(cardLocations.get(l).equals(location)){
					loc = l;
				}
			}
			return loc;
		}else{
			throw new NullPointerException("That location does not exist.");
		}
	}

	public static boolean cardLocationsInclude(int location){
		return cardLocations.containsKey(location);
	}

	public static boolean cardLocationsInclude(String location){
		return cardLocations.containsValue(location);
	}

	public static void draw(WanderingJacks wj) throws IOException{
		System.out.println(newLine+"------------------------------");

		System.out.print("Active Player: ");
		drawActivePlayer(wj.activePlayer);

		System.out.print("Player's Bankroll: ");
		drawPlayersBankroll(wj.player[wj.activePlayer]);

		System.out.print("Discard Pile: ");
		drawDiscardPile(wj.discardPile);

		System.out.println("Player's Hand:");
		drawPlayersHand(wj.player[wj.activePlayer]);

		System.out.println("Player's Retainers:");
		drawRetainerGroup(wj.retainer[wj.activePlayer]);

		System.out.println("Opponent's Retainers:");
		drawRetainerGroup(wj.retainer[(wj.activePlayer == 0 ? 1 : 0)]);

		System.out.println("------------------------------");
	}

	/**
	 * This draws all retainers in a given retainer group.
	 * @param retainer the group of retainers to be drawn.
	 * @see Retainer
	 */
	public static void drawRetainerGroup(Retainer[] retainer){
		String outString = "";
		for(int i = 0; i < retainer.length; i++){
			if(retainer[i].isEmpty())
				outString += " ["+i+"][0] = empty";
			else for(int j = 0; j < retainer[i].size(); j++)
				outString += " ["+i+"]["+j+"] = "+retainer[i].get(j).toString()+" ";
			outString += newLine;
		}
		System.out.print(outString);
	}

	/**
	 * This draws any player's hand.
	 * @param player the player whose hand is to be drawn.
	 * @see Player
	 */
	public static void drawPlayersHand(Player player){
		String outString = "";
		for(int i = 0; i < player.handSize(); i++){
			outString += " ["+(i+1)+"] = "+player.getFromHand(i) .toString()+" ";
		}
		outString += newLine;
		System.out.print(outString);
	}

	/**
	 * This draws any discard pile.
	 * @param discardPile the discard pile which is to be drawn.
	 * @see DiscardPile
	 */
	public static void drawDiscardPile(DiscardPile discardPile){
		String outString = (discardPile.isEmpty() ? "empty" : discardPile.peekAtTopCard().toString());
		outString += newLine;
		System.out.print(outString);
	}

	public static void drawActivePlayer(int id){
		String outString = String.valueOf(id);
		outString += newLine;
		System.out.print(outString);
	}

	/**
	 * This draws any Player's bankroll.
	 * @param player the player whose bankroll is to be drawn.
	 * @see Player
	 */
	public static void drawPlayersBankroll(Player player){
		String outString = ""+player.getBankroll();
		outString += newLine;
		System.out.print(outString);
	}

	public static int getPlayerInput(String prompt, HashMap<Integer, String> option) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println();
		System.out.println(prompt);
		for(int fromHere : option.keySet())
			System.out.println("-"+fromHere+"- "+option.get(fromHere));
		// Get user input, avoiding errors
		while(true){
			try{
				int input = Integer.parseInt(br.readLine());
				if(option.containsKey(input)){
					return input;
				}else System.err.println("Invalid Selection!");
			}catch(NumberFormatException nfe){
				System.err.println("Not an integer!");
			}
		}
	}

	public static int promptPlayerToDrawInitialCard(){
		int deck = ConsoleUI.cardLocation("The Deck");
		int discardPile = ConsoleUI.cardLocation("The Discard Pile");
		HashMap<Integer, String> options = new HashMap<Integer, String>();
			options.put(deck, cardLocations.get(deck));
			options.put(discardPile, cardLocations.get(discardPile));
		int selection = -1; // Should never return as -1
		try{ selection = ConsoleUI.getPlayerInput("Would you prefer a card from the Deck or the Discard Pile?", options);
		}catch(IOException e){
			System.err.println("Failed to accept player input.");}
		return selection;
	}

	public static int promptPlayerToChooseCardFromHand(Player player, boolean hasAnEmptyRetainer, boolean onFirstPlayFromHandOfTurn){
		ConsoleUI.drawPlayersHand(player);
		HashMap<Integer, String> options = new HashMap<Integer, String>();
		// If joker in hand, only option to play is the Joker
		if(player.handContains("Joker")){
			for(int i = 0; i < player.handSize(); i++)
				if(player.getFromHand(i).getValue() == Card.JOKER)
					options.put(i+1, player.getFromHand(i).toString());
		}else{
			int numbAces = 0;
			for(int i = 0; i < player.handSize(); i++){
				Card c = player.getFromHand(i);
				options.put(i+1, c.toString());
				if(c.getValue() == Card.ACE)
					numbAces++;
			}
			// if not first move and no empty retainers, show end turn as option
			if(!onFirstPlayFromHandOfTurn && !hasAnEmptyRetainer)
				options.put(ConsoleUI.cardLocation("End Turn")+1, "End Turn"); // +1 to translate to displayed option from actual hand index
			//If have three aces, add "Den of Thieves" as an option.
			if(numbAces >= 3)
				options.put(101, "Den of Thieves");
		}
		int selection = -1; // Should never return as -1
		try{ selection = ConsoleUI.getPlayerInput("Please select a card from your hand to play:", options);
		}catch(IOException e){
			System.err.println("Failed to accept player input.");}
		return selection;
	}

	public static boolean promptPlayerToLoopOrEndTurn(){
		HashMap<Integer, String> options = new HashMap<Integer, String>();
			options.put(1, "Yes.");
			options.put(2, "No, end my turn now.");
		boolean endTurn = false;
		int selection = -1; // Should never return as -1
		try{
			selection = ConsoleUI.getPlayerInput("Would you like to continue playing?", options);
			endTurn = (selection != 1 ? true : false);
		}catch(IOException e){
			System.err.println("Failed to accept player input.");}
		return endTurn;
	}

	public static int promptPlayerToChooseCardToFillEmptyRetainer(Player player){
		String output = "You do not have a valid card to play to your empty register."+newLine;
		output += "Please select a card to discard.";
		HashMap<Integer, String> options = new HashMap<Integer, String>();
		for(int i = 0; i < player.handSize(); i++){
			Card c = player.getFromHand(i);
			options.put(i+1, c.toString());
		}

		int selection = -1; // Should never return as -1
		try{ selection = ConsoleUI.getPlayerInput(output, options);
		}catch(IOException e){
			System.err.println("Failed to accept player input.");}
		return selection;
	}

	public static int promptPlayerToChooseOpponantsRetainer(Retainer[] oRetainers, String moveType){
		String output = "Please choose which of the opponent's retainers to affect.";
		HashMap<Integer, String> options = new HashMap<Integer, String>();

		switch(moveType){
		case "3oak+A":
			// any retainer without a Jack
			for(int i = 0; i < oRetainers.length; i++){
				if(!oRetainers[i].retainsJack()){
					String outString = "";
					for(int j = 0; j < oRetainers[i].size(); j++)
						outString += " ["+i+"]["+j+"] = "+oRetainers[i].get(j).toString()+" ";
					options.put(i, outString);
				}
			}break;
		case "joker":
			// any retainer with a Jack or a King or both
			for(int i = 0; i < oRetainers.length; i++){
				if(oRetainers[i].retainsJack() || oRetainers[i].contains(Card.KING)){
					String outString = "";
					for(int j = 0; j < oRetainers[i].size(); j++)
						outString += " ["+i+"]["+j+"] = "+oRetainers[i].get(j).toString()+" ";
					options.put(i, outString);
				}
			}break;
		default:
			throw new IllegalArgumentException(moveType+" is not a valid move type.");
		}

		int selection = -1; // Should never return as -1
		try{ selection = ConsoleUI.getPlayerInput(output, options);
		}catch(IOException e){
			System.err.println("Failed to accept player input.");}
		return selection;
	}

}
