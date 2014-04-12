import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Initia7_B
 * @version %I%, %G%
 *
 */
public class Retainer {
	/**
	 *
	 */
	private List<Card> retainer;
	/**
	 *
	 */
	private boolean retainsJack;

	/**
	 *
	 */
	Retainer(){
		retainer = new ArrayList<Card>();
		retainsJack = false;
	}

	/**
	 *
	 * @param target
	 * @return
	 */
	public boolean contains(Card target){
		return retainer.contains(target);
	}

	/**
	 *
	 * @param targetValue
	 * @return
	 */
	public boolean contains(String targetValue){
		boolean found = false;
		for(Card card : retainer)
			if(card.getValueAsString() == targetValue)
				found = true;
		return found;
	}

	/**
	 *
	 * @return
	 */
	public boolean retainsJack(){
		return retainsJack;
	}

	/**
	 *
	 * @param card
	 */
	public void add(Card card){
		retainer.add(card);
		if(card.getValueAsString() == "Jack")
			retainsJack = true;
	}

	/**
	 *
	 * @param card
	 */
	public void remove(Card card){
		retainer.remove(card);
		if(card.getValueAsString() == "Jack")
			retainsJack = false;
	}

	/**
	 *
	 * @return
	 */
	public boolean isEmpty(){
		return (retainer.size() <= 0 ? true : false);
	}

	/**
	 *
	 */
	public void empty(){
		retainer = new ArrayList<Card>();
	}
}
