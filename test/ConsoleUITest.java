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
	public void opponentsRetainersAreDrawn(){
		Retainer[] rets = wj.retainer[(wj.activePlayer == 0 ? 1 : 0)];
		String oRets = "Opponent's Retainers:"+newLine;

		for(int i = 0; i < rets.length; i++){
			for(int j = 0; j < rets[i].size(); j++)
				oRets += "[i][j] = "+rets[i].get(j).toString()+" ";
			oRets += newLine;
		}

		ConsoleUI.draw(wj);
	    assertEquals(oRets, outContent.toString());
	}

}

/*
private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

@Before
public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
}

@After
public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
}
sample test cases:

@Test
public void out() {
    System.out.print("hello");
    assertEquals("hello", outContent.toString());
}

@Test
public void err() {
    System.err.print("hello again");
    assertEquals("hello again", errContent.toString());
}
*/