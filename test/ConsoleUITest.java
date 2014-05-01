import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ConsoleUITest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final String newLine = System.getProperty("line.separator");

	WanderingJacks wj;
	Card aJoker = new Card(0, 0);
	Card anAce = new Card(1, 1);

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
	public void canDrawActivePlayer(){
		String outString = String.valueOf(wj.activePlayer);
		outString += newLine;
		ConsoleUI.drawActivePlayer(wj.activePlayer);
	    assertEquals(outString, outContent.toString());
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
	public void validPlayerInputReturnsThatInt() throws IOException{
		String mockUserInput = "1"+newLine;
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		HashMap<Integer, String> validOptions = new HashMap<Integer, String>();
		validOptions.put(1, "valid");
		int pr = ConsoleUI.getPlayerInput("", validOptions);
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	@Test
	public void invalidPlayRequestAreIgnoredUntilValidEntriesAreGiven() throws IOException{
		String mockUserInput = "f"+newLine; // ignored origins
		mockUserInput += "foo"+newLine;		//
		mockUserInput += "99"+newLine;		//
		mockUserInput += "-1"+newLine;		//
		mockUserInput += "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		HashMap<Integer, String> validOptions = new HashMap<Integer, String>();
		validOptions.put(1, "valid");
		int pr = ConsoleUI.getPlayerInput("", validOptions);
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	/*
	 * promptPlayerToLoopOrEndTurn()
	 */
	@Test
	public void pPtLoET_validEntriesReturnSelectionAsBoolean(){
		String mockUserInput = "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		boolean endTurn = ConsoleUI.promptPlayerToLoopOrEndTurn();
		System.setIn(System.in);
		assertTrue(endTurn == false);
	}

	@Test
	public void pPtLoET_invalidEntriesIgnoredUntilValidEntryIsGiven() throws IOException{
		String mockUserInput = "f"+newLine; // ignored inputs
		mockUserInput += "foo"+newLine;		//
		mockUserInput += "99"+newLine;		//
		mockUserInput += "-1"+newLine;		//
		mockUserInput += "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		boolean endTurn = ConsoleUI.promptPlayerToLoopOrEndTurn();
		System.setIn(System.in);
		assertTrue(endTurn == false);
	}

	/*
	 * promptPlayerToDrawInitialCard
	 */
	@Test
	public void pPtDIC_validEntriesReturnSelectionAsInt() throws IOException{
		String mockUserInput = "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToDrawInitialCard();
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	@Test
	public void pPtDIC_invalidEntriesIgnoredUntilValidEntryIsGiven() throws IOException{
		String mockUserInput = "f"+newLine; // ignored inputs
		mockUserInput += "foo"+newLine;		//
		mockUserInput += "99"+newLine;		//
		mockUserInput += "-1"+newLine;		//
		mockUserInput += "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToDrawInitialCard();
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	/*
	 * promptPlayerToChooseCardFromHand
	 */
	@Test
	public void ptCCFH_handWithJokerOnlyAllowsJoker() throws IOException{
		wj.player[wj.activePlayer].addToHand(aJoker);
		int handSize = wj.player[wj.activePlayer].handSize();
		assertTrue(wj.player[wj.activePlayer].getFromHand(handSize-1).equals(aJoker));
		String mockUserInput = "1"+newLine;						// ignored as invalid
		mockUserInput += "2"+newLine;							//
		mockUserInput += String.valueOf(handSize)+newLine;  	// only Joker index (+1 for output translation) accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		assertTrue(handSize > 1);
		int pr = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], true);
		System.setIn(System.in);
		assertEquals(pr, handSize);
	}

	@Test
	public void ptCCFH_validEntriesReturnSelectionAsInt() throws IOException{
		String mockUserInput = "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], true);
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	@Test
	public void ptCCFH_invalidEntriesIgnoredUntilValidEntryIsGiven() throws IOException{
		String mockUserInput = "f"+newLine; // ignored inputs
		mockUserInput += "foo"+newLine;		//
		mockUserInput += "99"+newLine;		//
		mockUserInput += "-1"+newLine;		//
		mockUserInput += "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], true);
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	@Test
	public void ptCCFH_onFirstPlay_NotInclude_EndTurn() throws IOException{
		String mockUserInput = "0"+newLine; // ignored inputs
		mockUserInput += "1"+newLine;		// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], true);
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	@Test
	public void ptCCFH_notOnFirstPlay_Include_EndTurn() throws IOException{
		String mockUserInput = "0"+newLine; // accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], false);
		System.setIn(System.in);
		assertEquals(pr, 0);
	}

	@Test
	public void ptCCFH_notHaveThreeAces_DontInclude_DenOfThieves() throws IOException{
		wj.player[wj.activePlayer].playFromHand(0);
		assertTrue(wj.player[wj.activePlayer].handSize() < 3);
		String mockUserInput = "101"+newLine;	// ignored inputs
		mockUserInput += "1"+newLine;			// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], false);
		System.setIn(System.in);
		assertEquals(pr, 1);
	}

	@Test
	public void ptCCFH_haveThreeAces_Include_DenOfThieves() throws IOException{
		wj.player[wj.activePlayer].addToHand(anAce);
		wj.player[wj.activePlayer].addToHand(anAce);
		wj.player[wj.activePlayer].addToHand(anAce);
		String mockUserInput = "101"+newLine;	// accepted
		ByteArrayInputStream mockIn = new ByteArrayInputStream(mockUserInput.getBytes());
		System.setIn(mockIn);
		int pr = ConsoleUI.promptPlayerToChooseCardFromHand(wj.player[wj.activePlayer], false);
		System.setIn(System.in);
		assertEquals(pr, 101);
	}



	/*
	 * cardLocation
	 */
	@Test
	public void validIntCardLocationReturnsString(){
		assertTrue(ConsoleUI.cardLocation(0) instanceof String);
	}

	@Test
	public void validStringCardLocationReturnsInt(){
		Object result = ConsoleUI.cardLocation("My Hand");
		assertTrue(result.equals((int)result));
	}

	@Test(expected=NullPointerException.class)
	public void invalidIntCardLocationThrowsException(){
		ConsoleUI.cardLocation(-9);
	}

	@Test(expected=NullPointerException.class)
	public void invalidStringCardLocationThrowsException(){
		ConsoleUI.cardLocation("The Sky");
	}

	@Test
	public void validIntCardLocationIncludesReturnsTrue(){
		assertTrue(ConsoleUI.cardLocationsInclude(0));
	}

	@Test
	public void validStringCardLocationIncludesReturnsTrue(){
		assertTrue(ConsoleUI.cardLocationsInclude("My Hand"));
	}

	@Test
	public void invalidIntCardLocationIncludesReturnsFalse(){
		assertFalse(ConsoleUI.cardLocationsInclude(-9));
	}

	@Test
	public void invalidStringCardLocationIncludesReturnsFalse(){
		assertFalse(ConsoleUI.cardLocationsInclude("The Sky"));
	}

	/*
	 * promptPLayerToChooseCardToFillEmptyRetainer
	 */

	@Test
	public void pPtCCtER_(){
		fail("not implemented");
	}

}