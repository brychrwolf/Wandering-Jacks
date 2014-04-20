import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ConsoleUITest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final String newLine = System.getProperty("line.separator");

	WanderingJacks wj;

	@Before
	public void init(){
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
		wj = new WanderingJacks();
		wj.setUpGameEnvironment();
	}

	@After
	public void cleanUpStreams(){
	    System.setOut(null);
	    System.setErr(null);
	}

	@Test
	public void canDrawDiscardPile(){
		String outString = wj.discardPile.peekAtTopCard().toString();
		outString += newLine;
		ConsoleUI.drawDiscardPile(wj.discardPile);
	    assertEquals(outString, outContent.toString());
	}

	@Test
	public void canDrawPlayersBankroll(){
		String outString = ""+wj.player[wj.activePlayer].getBankroll();
		outString += newLine;
		ConsoleUI.drawPlayersBankroll(wj.player[wj.activePlayer]);
	    assertEquals(outString, outContent.toString());
	}

	@Test
	public void canDrawPlayersHand(){
		Player p = wj.player[wj.activePlayer];

		String outString = "";
		for(int i = 0; i < p.handSize(); i++){
			outString += " ["+i+"] = "+p.getFromHand(i) .toString()+" ";
		}
		outString += newLine;
		ConsoleUI.drawPlayersHand(p);
	    assertEquals(outString, outContent.toString());
	}

	@Test
	public void canDrawPlayersRetainers(){
		Retainer[] pRets = wj.retainer[wj.activePlayer];
		String outString = "";
		for(int i = 0; i < pRets.length; i++){
			for(int j = 0; j < pRets[i].size(); j++)
				outString += " ["+i+"]["+j+"] = "+pRets[i].get(j).toString()+" ";
			outString += newLine;
		}
		ConsoleUI.drawRetainerGroup(pRets);
	    assertEquals(outString, outContent.toString());
	}

	@Test
	public void canDrawOpponentsRetainers(){
		Retainer[] oRets = wj.retainer[(wj.activePlayer == 0 ? 1 : 0)];
		String outString = "";
		for(int i = 0; i < oRets.length; i++){
			for(int j = 0; j < oRets[i].size(); j++)
				outString += " ["+i+"]["+j+"] = "+oRets[i].get(j).toString()+" ";
			outString += newLine;
		}
		ConsoleUI.drawRetainerGroup(oRets);
		assertEquals(outString, outContent.toString());
	}

	@Test
	public void canDrawEmptyRetainers(){
		Retainer[] retainer = wj.retainer[wj.activePlayer];
		retainer[0].remove(0);

		String outString = "";
		for(int i = 0; i < retainer.length; i++){
			if(retainer[i].isEmpty())
				outString += " ["+i+"][0] = empty";
			else for(int j = 0; j < retainer[i].size(); j++)
				outString += " ["+i+"]["+j+"] = "+retainer[i].get(j).toString()+" ";
			outString += newLine;
		}
		ConsoleUI.drawRetainerGroup(retainer);
		assertEquals(outString, outContent.toString());
	}

	@Test
	public void canDrawEmptyDiscardPile(){
		wj.discardPile.takeTopCard();
		ConsoleUI.drawDiscardPile(wj.discardPile);
		assertEquals("empty"+newLine, outContent.toString());
	}

	/**
	 * User prompt
	 * @throws IOException
	 */
	@Test
	public void requestPlayFromDeckToHandGainsCard() throws IOException{
		String mockUserInput = "0"+newLine+"0";
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int[] rp = ConsoleUI.getPlayRequest();
		System.setIn(System.in);
		assertEquals(rp[0], 0);
		assertEquals(rp[1], 0);
	}
}