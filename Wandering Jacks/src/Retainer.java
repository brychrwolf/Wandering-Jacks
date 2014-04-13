import java.util.ArrayList;
import java.util.List;

/**
 * A retainer is a special stack of cards for use in the game Wandering Jacks.
 * <p>
 * Each of the two players have 4 retainers. The goal in Wandering Jacks is to
 * retain all 4 Jacks, one each in the player's retainers. The first card is
 * forbidden from being one of few values, cards may be added to retainers,
 * retainers may be emptied or individual cards may be removed from anywhere
 * within the stack as according to the rules of the game.
 * @author Bryan Wolfford
 * @version %I%, %G%
 * @see WanderingJacks
 * @see Player
 * @see Card
 */
public class Retainer {
	private List<Card> retainer;
	private boolean retainsJack;

	/**
	 * Creates a new empty retainer, which by definition, does not retain a Jack.
	 */
	Retainer(){
		retainer = new ArrayList<Card>();
		retainsJack = false;
	}

	/**
	 * Returns true only if the target card is found within the retainer.
	 * @param target is the card that is searched for in the retainer.
	 * @return true only if the target card is found within the retainer, and
	 * false if otherwise.
	 */
	public boolean contains(Card target){
		return retainer.contains(target);
	}

	/**
	 * Returns true only if any of the cards in the retainer have a value that
	 * matches the given target value string.
	 * @param targetValue the string to search for among all the cards' values
	 * in the retainer
	 * @return true only if the target value is found within the retainer, and
	 * false if otherwise.
	 */
	public boolean contains(String targetValue){
		boolean found = false;
		for(Card card : retainer)
			if(card.getValueAsString() == targetValue)
				found = true;
		return found;
	}

	/**
	 * Returns true if this retainer retains a Jack.
	 * @return true if this retainer retains a Jack, false if otherwise.
	 */
	public boolean retainsJack(){
		return retainsJack;
	}

	/**
	 * Adds a Card to the top of the retainer. Restrictions about which cards
	 * may be put on which retainers are defined in the rules of the game.
	 * @param card the Card that is to be put in the retainer.
	 */
	public void add(Card card){
		retainer.add(card);
		if(card.getValueAsString() == "Jack")
			retainsJack = true;
	}

	/**
	 * Removes the given card from the retainer.
	 * @param card the card to remove from the retainer.
	 */
	public void remove(Card card){
		retainer.remove(card);
		if(card.getValueAsString() == "Jack")
			retainsJack = false;
	}

	/**
	 * Returns true if the retainer is empty.
	 * @return true id the retainer is empty, false if otherwise.
	 */
	public boolean isEmpty(){
		return (retainer.size() <= 0 ? true : false);
	}

	/**
	 * Removes all the cards from the retainer, resulting in a empty retainer.
	 */
	public void empty(){
		retainer = new ArrayList<Card>();
	}
}
