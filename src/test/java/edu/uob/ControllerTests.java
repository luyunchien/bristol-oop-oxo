package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class ControllerTests {
  OXOModel model;
  OXOController controller;
  OXOModel model5;
  OXOController controller5;

  // create your standard 3*3 OXO board (where three of the same symbol in a line wins) with the X
  // and O player
  private static OXOModel createStandardModel() {
    OXOModel model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    return model;
  }

  private static OXOModel createStandardModel5() {
    OXOModel model = new OXOModel(5, 5, 5);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    model.addPlayer(new OXOPlayer('*'));
    return model;
  }

  // we make a new board for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
    model = createStandardModel();
    controller = new OXOController(model);
    model5 = createStandardModel5();
    controller5 = new OXOController(model5);
  }

  // here's a basic test for the `controller.handleIncomingCommand` method
  @Test
  void testHandleIncomingCommand() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");

    // A move has been made for A1 (i.e. the [0,0] cell on the board), let's see if that cell is
    // indeed owned by the player
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0));
  }

  // here's a complete game where we find out if someone won
  @Test
  void testBasicWinWithA1A2A3() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a3");

    // OK, so A1, A2, A3 is a win and that last A3 move is made by the first player (players
    // alternative between moves) let's make an assertion to see whether the first moving player is
    // the winner here
    assertEquals(
        firstMovingPlayer,
        model.getWinner(),
        "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testDiagonalWinWithA3B2C1() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("a3");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testDiagonalWinWithA1B2C3() throws OXOMoveException{
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a3");
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("c3");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void  testVerticalwithA2B2C2D2() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.addColumn();
    controller.addRow();
    controller.increaseWinThreshold();
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("d1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("d2");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void  testDecreaseThreshold() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("c2");
    controller.decreaseWinThreshold();

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void  testWinThresholdEqualsTo0() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a1");
    controller.decreaseWinThreshold();
    controller.decreaseWinThreshold();

    assertEquals(
            true,
            model.isGameDrawn(),
            "Winner was expected to be %s but wasn't".formatted(model.getWinner()));
  }

  @Test
  void  testThreePlayersDrawn() throws OXOMoveException {
    model.addPlayer(new OXOPlayer('*'));
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("b3");
    controller.handleIncomingCommand("c3");
    controller.decreaseWinThreshold();
    controller.decreaseWinThreshold();

    assertEquals(
            true,
            model.isGameDrawn(),
            "Winner was expected to be %s but wasn't".formatted(model.getWinner()));
  }

    @Test
    void testDecreaseWinThresholdTo2() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a2");
    controller.decreaseWinThreshold();

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testDrawWithFullBoard() throws OXOMoveException {
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("b3");
    controller.handleIncomingCommand("c3");

    assertEquals(
            true,
            model.isGameDrawn(),
            "Winner was expected to be %s but wasn't".formatted(model.getWinner()));
  }

  @Test
  void  test5X5WinDiagonalWithThreshold4() throws OXOMoveException {
    controller5.handleIncomingCommand("c3");
    controller5.handleIncomingCommand("b3");
    controller5.handleIncomingCommand("a3");
    controller5.handleIncomingCommand("d2");
    controller5.handleIncomingCommand("c4");
    controller5.handleIncomingCommand("a4");
    controller5.handleIncomingCommand("b4");
    controller5.handleIncomingCommand("d5");
    controller5.handleIncomingCommand("a1");
    controller5.decreaseWinThreshold();
    controller5.decreaseWinThreshold();

    assertEquals(
            true,
            model5.isGameDrawn(),
            "Winner was expected to be %s but wasn't".formatted(model5.getWinner()));
  }

  @Test
  void testException() throws OXOMoveException {
    assertThrows(OXOMoveException.InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("aa1"));
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("@2"));
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("b$"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("d3"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("c4"));

    controller.handleIncomingCommand("c3");
    assertThrows(OXOMoveException.CellAlreadyTakenException.class, ()-> controller.handleIncomingCommand("c3"));
  }
}
