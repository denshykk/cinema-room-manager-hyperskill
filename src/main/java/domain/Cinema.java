package domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Class that represents a cinema.
 */
@Getter
public class Cinema {

  private final int rows;
  private final int columns;
  @Setter
  private       int soldTickets;
  @Setter
  private       int currentIncome;

  @Setter
  private String[][] seats;

  public Cinema(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
  }

  /**
   * A simple record that represents a Seat in the Cinema.
   */
  public record Seat(int row, int column) {}

}
