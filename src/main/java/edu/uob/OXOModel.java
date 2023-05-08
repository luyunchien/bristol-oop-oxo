package edu.uob;

import java.util.ArrayList;

class OXOModel {
  private final ArrayList<ArrayList<OXOPlayer>> cells;
  private final ArrayList<OXOPlayer> players;
  private int currentPlayerNumber;
  private OXOPlayer winner;
  private boolean gameDrawn;
  private int winThreshold;
  private int numberOfRows;
  private int numberOfColumns;

  public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
    winThreshold = winThresh;
    this.numberOfColumns = numberOfColumns;
    this.numberOfRows = numberOfRows;
    cells = new ArrayList<>(numberOfRows);
    for(int j=0; j<numberOfRows; j++){
      cells.add(new ArrayList<>(numberOfColumns));
      for(int i=0; i<numberOfColumns; i++){
        cells.get(j).add(i,new OXOPlayer(' '));
      }
    }
    players = new ArrayList<>();
  }

  public int getNumberOfPlayers() {
    return players.size();
  }

  public void addPlayer(OXOPlayer player) {
    players.add(player);
  }

  public OXOPlayer getPlayerByNumber(int number) {
    return players.get(number);
  }

  public OXOPlayer getWinner() {
    return winner;
  }

  public void setWinner(OXOPlayer player) {
    winner = player;
  }

  public int getCurrentPlayerNumber() {
    return currentPlayerNumber;
  }

  public void setCurrentPlayerNumber(int playerNumber) {
    currentPlayerNumber = playerNumber;
  }

  public int getNumberOfRows() {
    return cells.size();
  }

  public int getNumberOfColumns() {
    return cells.get(0).size();
  }

  public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
    return cells.get(rowNumber).get(colNumber);
  }

  public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
    cells.get(rowNumber).set(colNumber, player);
  }

  public void setWinThreshold(int winThresh) {
    winThreshold = winThresh;
  }

  public int getWinThreshold() {
    return winThreshold;
  }

  public void setGameDrawn() {
    gameDrawn = true;
  }

  public boolean isGameDrawn() {
    return gameDrawn;
  }

  public void addColumn() {
    this.numberOfColumns = numberOfColumns + 1;
    for (int j = 0; j < numberOfRows; j++) {
      cells.get(j).add(numberOfColumns - 1, new OXOPlayer(' '));
    }
  }

  public void addRow() {
    this.numberOfRows = numberOfRows + 1;
    cells.add(new ArrayList<>(numberOfColumns));
    for (int i = 0; i < numberOfColumns; i++) {
      cells.get(numberOfRows - 1).add(i, new OXOPlayer(' '));
    }
  }

  public void removeColumn() {
    this.numberOfColumns = numberOfColumns - 1;
    for(int j=0; j<numberOfRows; j++){
      cells.get(j).remove(numberOfColumns);
    }
  }

  public void removeRow() {
    this.numberOfRows = numberOfRows - 1;
    cells.remove(numberOfRows);
  }

}
