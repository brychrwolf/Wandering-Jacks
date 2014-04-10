import java.util.ArrayList;
import java.util.List;


public class Retainer {
	private List<Card> retainer;
	private boolean retainsJack;

	Retainer(){
		retainer = new ArrayList<Card>();
		retainsJack = false;
	}

	public boolean contains(Card target){
		return retainer.contains(target);
	}
	public boolean contains(String targetValue){
		boolean found = false;
		for(Card card : retainer)
			if(card.getValueAsString() == targetValue)
				found = true;
		return found;
	}

	public boolean retainsJack(){
		return retainsJack;
	}

	public void add(Card card){
		retainer.add(card);
		if(card.getValueAsString() == "Jack")
			retainsJack = true;
	}

	public void remove(Card card){
		retainer.remove(card);
		if(card.getValueAsString() == "Jack")
			retainsJack = false;
	}

	public boolean isEmpty(){
		return (retainer.size() <= 0 ? true : false);
	}

	public void empty(){
		retainer = new ArrayList<Card>();
	}
}
