package edu.uob;

import edu.uob.OXOMoveException.*;

class OXOController {
  OXOModel gameModel;

  public OXOController(OXOModel model) {
    gameModel = model;
  }

  public void handleIncomingCommand(String command) throws OXOMoveException {
    validateCommand(command);
    int cols = command.charAt(1) - 49;
    char row = command.charAt(0);
    row = Character.toLowerCase(row);
    int rows = (int) row - 97;
    if(!gameHasWinner() && !gameModel.isGameDrawn()) {
      if(gameModel.getCellOwner(rows,cols).getPlayingLetter() == ' ') {
        gameModel.setCellOwner(rows, cols, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
        if(gameHasWinner()) {
          gameModel.setWinner(theWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber())));
        }
        int currentplayer = gameModel.getCurrentPlayerNumber() + 1;
        if (currentplayer == gameModel.getNumberOfPlayers()) {
          currentplayer = 0;
        }
        gameModel.setCurrentPlayerNumber(currentplayer);
      }
    }
    if(checkIfDrawn()){
      gameModel.setGameDrawn();
    }
  }

  private void validateCommand(String message) throws OXOMoveException {
    int rows;
    int cols;
    if(message.length() != 2){
      throw new InvalidIdentifierLengthException(message.length());
    }else {
      if((int)message.charAt(0) < 97){
        if((int)message.charAt(0) > 64 && (int)message.charAt(0) < 74){
          rows = (int)message.charAt(0) - 65;
        }else {
          throw new InvalidIdentifierCharacterException(RowOrColumn.ROW, message.charAt(0));
        }
      }else if((int)message.charAt(0) > 106){
        throw new InvalidIdentifierCharacterException(RowOrColumn.ROW, message.charAt(0));
      }else{
        rows = (int)message.charAt(0) - 97;
      }

      if((int)message.charAt(1) < 49 || (int)message.charAt(1) > 57){
        throw new InvalidIdentifierCharacterException(RowOrColumn.COLUMN, message.charAt(1));
      }else{
        cols = message.charAt(1) - 49;
      }

      if(rows >= gameModel.getNumberOfRows()){
        throw new OutsideCellRangeException(RowOrColumn.ROW, rows+1);
      }
      if(cols >= gameModel.getNumberOfColumns()){
        throw new OutsideCellRangeException(RowOrColumn.COLUMN, cols+1);
      }

      if(gameModel.getCellOwner(rows, cols).getPlayingLetter() != ' '){
        throw new CellAlreadyTakenException(rows, cols);
      }
    }
  }

  private boolean gameHasWinner() {
    return theNumOfWinners() == 1;
  }

  private OXOPlayer theWinner(OXOPlayer player) {
    if(gameHasWinner()){
      for(int i=0; i< gameModel.getNumberOfPlayers(); i++){
        char current = gameModel.getPlayerByNumber(i).getPlayingLetter();
        if(playerWins(current)){
          return gameModel.getPlayerByNumber(i);
        }
      }
    }
    return player;
  }

  private boolean isInsideBoard(int row, int col) {
    if(row < 0 || row >= gameModel.getNumberOfRows()){
      return false;
    }else return col >= 0 && col < gameModel.getNumberOfColumns();
  }

  private boolean winHorizontal(int row, int col, char currentcell){
    if(isInsideBoard(row, col + gameModel.getWinThreshold() - 1) && currentcell != ' '){
      for(int j=0; j<gameModel.getWinThreshold(); j++){
        if(gameModel.getCellOwner(row,col+j).getPlayingLetter() != currentcell){
          return false;
        }
      }
    }else {
      return false;
    }
    return true;
  }

  private boolean winVertical(int row, int col, char currentcell){
    if(isInsideBoard(row+ gameModel.getWinThreshold()-1, col) && currentcell != ' '){
      for(int i=0; i<gameModel.getWinThreshold(); i++){
        if(gameModel.getCellOwner(row+i, col).getPlayingLetter() != currentcell){
          return false;
        }
      }
    }else{
      return false;
    }
    return true;
  }

  private boolean winDiagonal(int row, int col, char currentcell){
    boolean win = true;
    if(isInsideBoard(row+gameModel.getWinThreshold()-1,col+gameModel.getWinThreshold()-1) && currentcell != ' '){
      for(int i=0; i<gameModel.getWinThreshold(); i++){
        if (gameModel.getCellOwner(row+i, col+i).getPlayingLetter() != currentcell) {
          win = false;
          break;
        }
      }
    }else {
      win = false;
    }
    if(win) {
      return true;
    }
    win = true;
    if(isInsideBoard(row+(gameModel.getWinThreshold()-1),col-(gameModel.getWinThreshold()-1)) && currentcell != ' '){
      for(int i=0; i<gameModel.getWinThreshold(); i++){
        if (gameModel.getCellOwner(row+i, col-i).getPlayingLetter() != currentcell) {
          win = false;
          break;
        }
      }
    }else {
      win = false;
    }
    return win;
  }

  private int theNumOfWinners() {
    int cnt = 0;
    for(int i=0; i<gameModel.getNumberOfPlayers(); i++){
      char current = gameModel.getPlayerByNumber(i).getPlayingLetter();
      if (playerWins(current)) {
        cnt++;
      }
    }
    return cnt;
  }

  private boolean playerWins(char current){
    for (int j = 0; j < gameModel.getNumberOfRows(); j++) {
      for (int i = 0; i < gameModel.getNumberOfColumns(); i++) {
        if (winHorizontal(j, i, current)) {
          return true;
        } else if (winVertical(j, i, current)) {
          return true;
        } else if (winDiagonal(j, i, current)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean checkIfDrawn() {
    if (theNumOfWinners() > 1) {
      return true;
    }
    if (!gameHasWinner()) {
      for (int j = 0; j < gameModel.getNumberOfRows(); j++) {
        for (int i = 0; i < gameModel.getNumberOfColumns(); i++) {
          if (gameModel.getCellOwner(j, i).getPlayingLetter() == ' ') {
            return false;
          }
        }
      }
    }
    return true;
  }

  public void addRow() {
    if(!gameHasWinner()) {
      if(!gameModel.isGameDrawn()) {
        gameModel.addRow();
      }
    }
  }
  public void removeRow() {
    if(!gameHasWinner()) {
      if(!gameModel.isGameDrawn()) {
        if (gameModel.getNumberOfRows() > 1) {
          gameModel.removeRow();
        }
      }
    }
  }
  public void addColumn() {
    if(!gameHasWinner()) {
      if(!gameModel.isGameDrawn()) {
        gameModel.addColumn();
      }
    }
  }
  public void removeColumn() {
    if(!gameHasWinner()) {
      if(!gameModel.isGameDrawn()) {
        if (gameModel.getNumberOfColumns() > 1) {
          gameModel.removeColumn();
        }
      }
    }
  }
  public void increaseWinThreshold() {
    int newthreshold = gameModel.getWinThreshold() + 1;
    gameModel.setWinThreshold(newthreshold);
  }
  public void decreaseWinThreshold() {
    if (gameModel.getWinThreshold() > 0) {
      int newthreshold = gameModel.getWinThreshold() - 1;
      gameModel.setWinThreshold(newthreshold);
      if (gameHasWinner()) {
        gameModel.setWinner(theWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber())));
      }else if(checkIfDrawn()) {
        gameModel.setGameDrawn();
      }
    }
  }
}
