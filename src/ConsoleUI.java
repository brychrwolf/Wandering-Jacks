import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;



public class ConsoleUI {
	private final static String newLine = System.getProperty("line.separator");
	private final static HashMap<Integer, String> cardLocations = new HashMap<Integer, String>();
	static{
		cardLocations.put(0, "The Deck");
		cardLocations.put(1, "The Discard Pile");
		cardLocations.put(2, "My Hand");
		cardLocations.put(3, "My 1st Retainer");
		cardLocations.put(4, "My 2nd Retainer");
		cardLocations.put(5, "My 3rd Retainer");
		cardLocations.put(6, "My 4th Retainer");
	};

	public static String cardLocation(int location){
		if(cardLocations.containsKey(location))
			return cardLocations.get(location);
		else throw new NullPointerException("That location does not exist.");
	}

	public static int cardLocation(String location){
		int loc = -1; // should never return as -1
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
		System.out.print("Discard Pile: ");
		drawDiscardPile(wj.discardPile);

		System.out.print("Player's Bankroll: ");
		drawPlayersBankroll(wj.player[wj.activePlayer]);

		System.out.println("Player's Hand:");
		drawPlayersHand(wj.player[wj.activePlayer]);

		System.out.println("Player's Retainers:");
		drawRetainerGroup(wj.retainer[wj.activePlayer]);

		System.out.println("Opponent's Retainers:");
		drawRetainerGroup(wj.retainer[(wj.activePlayer == 0 ? 1 : 0)]);
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
			outString += " ["+i+"] = "+player.getFromHand(i) .toString()+" ";
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

	public static int promptPlayerToChooseCardFromHand(Player player){
		HashMap<Integer, String> options = new HashMap<Integer, String>();
		for(int i = 0; i < player.handSize(); i++)
			options.put(i, player.getFromHand(i).toString());
		int selection = -1; // Should never return as -1
		try{ selection = ConsoleUI.getPlayerInput("Please select which card from your hand:", options);
		}catch(IOException e){
			System.err.println("Failed to accept player input.");}
		return selection;
	}

	public static boolean promptPlayerToLoopOrEndTurn(){
		HashMap<Integer, String> options = new HashMap<Integer, String>();
			options.put(0, "Yes.");
			options.put(1, "No, end my turn now.");
		boolean endTurn = false;
		int selection = -1; // Should never return as -1
		try{
			selection = ConsoleUI.getPlayerInput("Would you like to continue playing?", options);
			endTurn = (selection != 0 ? true : false);
		}catch(IOException e){
			System.err.println("Failed to accept player input.");}
		return endTurn;
	}

}
