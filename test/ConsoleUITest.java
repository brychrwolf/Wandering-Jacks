import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
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
	}

	@After
	public void cleanUpStreams(){
	    System.setOut(null);
	    System.setErr(null);
	}

	@Test
	public void canDrawPlayersRetainers(){
		Retainer[] pRets = wj.retainer[wj.activePlayer];
		String outString = "";
		for(int i = 0; i < pRets.length; i++){
			for(int j = 0; j < pRets[i].size(); j++)
				outString += "["+i+"]["+j+"] = "+pRets[i].get(j).toString()+" ";
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
				outString += "["+i+"]["+j+"] = "+oRets[i].get(j).toString()+" ";
			outString += newLine;
		}
		ConsoleUI.drawRetainerGroup(oRets);
		assertEquals(outString, outContent.toString());
	}

}