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
	 * @see Card
	 */
	public boolean contains(Card target){
		return retainer.contains(target);
	}

	/**
	 * Returns the index of the card first card in the retainer to have a value
	 * that matches the given target value string.
	 * @param targetValue the string to search for among all the cards' values
	 * in the retainer
	 * @return the index of the first matched card, or -1
	 * @see Card
	 */
	public int indexOf(String targetValue){
		int index = -1;
		for(Card card : retainer)
			if(card.getValueAsString() == targetValue)
				index = retainer.indexOf(card);
		return index;
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
	 * @see Card
	 */
	public void add(Card card){
		retainer.add(card);
		if(card.getValueAsString() == "Jack")
			retainsJack = true;
	}

	/**
	 * Returns and removes the given card from the retainer.
	 * @param card the card to remove from the retainer.
	 * @return card the card that was removed
	 * @throws IllegalStateException if no cards exist
	 * @throws IllegalStateException target card does not exist
	 * @see Card
	 */
	public Card remove(Card card){
		if(this.size() <= 0)
			throw new IllegalStateException("No cards in the retainer to remove.");
		if(!retainer.remove(card))
			throw new IllegalStateException("There was no "+card.toString()+" in the retainer to remove.");
		if(card.getValueAsString() == "Jack")
			retainsJack = false;
		return card;
	}

	/**
	 * Returns and removes the given card from the retainer at index.
	 * @param index the index from which to remove from a card
	 * @return the card
	 * @throws IllegalStateException if no cards exist
	 * @throws IllegalStateException target index is out of bounds
	 */
	public Card remove(int index){
		Card c;
		try{
			c = retainer.get(index);
			retainer.remove(c);
		}catch(IndexOutOfBoundsException e){
			System.err.println("IndexOutOfBoundsException: " + e.getMessage());
			throw new IndexOutOfBoundsException("Index "+index+" is not within bounds: "+retainer.size());
		}
		return c;
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
		if(retainsJack == true) retainsJack = false;
		retainer = new ArrayList<Card>();
	}

	/**
	 * Returns the card at given index
	 * @param index the integer index to get from the Retainer, 0 is the bottom
	 * of the retainer
	 * @return the Card found at the given index
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 * @see Card
	 */
	public Card get(int index){
		try{
			return retainer.get(index);
		}catch (IndexOutOfBoundsException e){
		    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
		    System.err.println("Index " + index + " not within {0 - " + (retainer.size() - 1) + "}");
			throw new IndexOutOfBoundsException(e + "Index " + index + " not within {0 - " + (retainer.size() - 1) + "}");
		}
	}

	/**
	 * Returns the size of the retainer.
	 */
	public int size(){
		return retainer.size();
	}
}
