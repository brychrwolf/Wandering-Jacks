import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;



public class ConsoleUI {
	private final static String newLine = System.getProperty("line.separator");

	public static void draw(WanderingJacks wj){
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

	public static int[] getPlayRequest(HashMap<Integer, String> possibleOrigins, HashMap<Integer, String> possibleDestinations) throws IOException{
		int[] rp = new int[2];
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println();
		System.out.println("Enter *from* where to play a card:");
		for(int origins : possibleOrigins.keySet())
			System.out.println("["+origins+"] "+possibleOrigins.get(origins));
		// Get user input, avoiding errors
		boolean loop = true;
		while(loop == true && possibleOrigins.containsKey(rp[1])){
			try{
				rp[0] = Integer.parseInt(br.readLine());
				loop = false;
			}catch(NumberFormatException nfe){
				System.err.println("Invalid Format!");
			}
		}
		System.out.println();
		System.out.println("Enter *to* where to play a card:");
		for(int destination : possibleDestinations.keySet())
			System.out.println("["+destination+"] "+possibleDestinations.get(destination));
		// Get user input, avoiding errors
		loop = true;
		while(loop == true && possibleDestinations.containsKey(rp[1])){
			try{
				rp[1] = Integer.parseInt(br.readLine());
				loop = false;
			}catch(NumberFormatException nfe){
				System.err.println("Please enter a valid integer!");
			}
		}

		/*Scanner sc = new Scanner(System.in);
		System.out.println();
		System.out.println("To make a play:");
		System.out.println("First enter *from* where to play a card:");
		System.out.println("[0] deck");
		System.out.println("[1] discard pile");
		System.out.println();
		rp[0] = sc.nextInt();
		sc.nextLine();
		System.out.println("Next enter *to* where to play a card:");
		System.out.println("[0] my hand");
		rp[1] = sc.nextInt();
		sc.nextLine();
		sc.close();*/
		return rp;
	}
}
