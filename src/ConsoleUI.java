
public class ConsoleUI {
	private final static String newLine = System.getProperty("line.separator");

	public static void draw(WanderingJacks wj){
		System.out.print("Discard Pile: ");
		drawDiscardPile(wj.discardPile);
		System.out.println("Player's Hand:");
		drawPlayersHand(wj.player[wj.activePlayer]);
		System.out.println("Player's Retainers:");
		drawRetainerGroup(wj.retainer[wj.activePlayer]);
		System.out.println("Opponent's Retainers:");
		drawRetainerGroup(wj.retainer[(wj.activePlayer == 0 ? 1 : 0)]);
	}

	/**
	 * This draws any given retainer group.
	 * @param retainer the group of retainers to be drawn.
	 * @see Retainer
	 */
	public static void drawRetainerGroup(Retainer[] retainer){
		String outString = "";
		for(int i = 0; i < retainer.length; i++){
			for(int j = 0; j < retainer[i].size(); j++)
				outString += "["+i+"]["+j+"] = "+retainer[i].get(j).toString()+" ";
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
			outString += "["+i+"] = "+player.getFromHand(i) .toString()+" ";
		}
		outString += newLine;
		System.out.print(outString);
	}

	/**
	 * This any discard pile.
	 * @param discardPile the discard pile which is to be drawn.
	 * @see DiscardPile
	 */
	public static void drawDiscardPile(DiscardPile discardPile){
		String outString = discardPile.peekAtTopCard().toString();
		outString += newLine;
		System.out.print(outString);
	}

}
