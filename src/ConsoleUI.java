import java.io.IOException;
import java.util.Scanner;



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
		String outString = discardPile.peekAtTopCard().toString();
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

	public static int[] requestPlay() throws IOException{
		int[] rp = new int[2];
		Scanner sc = new Scanner(System.in);
		System.out.println("To make a play:");
		System.out.println("First enter *from* where to play a card:");
		System.out.println("[0] deck");
		System.out.println("[1] discard pile");
		rp[0] = sc.nextInt();
		System.out.println("Next enter *to* where to play a card:");
		System.out.println("[0] my hand");
		rp[1] = sc.nextInt();
		sc.close();
		return rp;
	}
}
