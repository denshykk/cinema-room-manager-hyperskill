package service;

import domain.Cinema;
import exception.AlreadyTakenSeatException;
import exception.NonExistingSeatException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * It's a service class that provides a menu for the user to interact with the cinema
 */
public class CinemaService {

  private static final String AVAILABLE_SEAT = "S";
  private static final String SOLD_SEAT      = "B";

  private final Scanner scanner;
  private final Cinema  cinema;

  public CinemaService(final Scanner scanner, final Cinema cinema) {
    this.scanner = scanner;
    this.cinema = cinema;
    createSeats();
  }

  /**
   * It takes a seat, validates it, and if it's valid, it sets the price and prints it
   */
  public void showMenu(final String option) {
    switch (option) {
      case "1" -> showSeats();
      case "2" -> buyTicket();
      case "3" -> statistics();
      default -> System.out.printf("Unknown menu item - %s\n", option);
    }
  }

  /**
   * It's a method that creates the seats in the cinema
   */
  public void createSeats() {
    String[][] seats = new String[cinema.getRows()][];

    for (int i = 0; i < cinema.getRows(); i++) {
      String[] row = new String[cinema.getColumns()];
      Arrays.fill(row, AVAILABLE_SEAT);
      seats[i] = row;
    }

    cinema.setSeats(seats);
  }

  /**
   * It's a method that shows the seats in the cinema
   */
  public void showSeats() {
    System.out.print("Cinema:\n");
    IntStream.rangeClosed(1, cinema.getSeats()[0].length).forEach(i -> System.out.printf("\t%d", i));

    System.out.println();

    int counter = 1;
    for (String[] seat : cinema.getSeats()) {
      System.out.print(counter);
      for (String s : seat) {
        System.out.printf("\t%s", s);
      }
      System.out.println();
      counter++;
    }

    System.out.println();
  }

  /**
   * It's a method that gives an ability to buy a ticket in the cinema
   */
  public void buyTicket() {
    try {
      Cinema.Seat seat = reserveSeat();
      seatValidation(seat);
      int price = getPrice(seat);

      cinema.setCurrentIncome(cinema.getCurrentIncome() + price);
      cinema.setSoldTickets(cinema.getSoldTickets() + 1);

      printPrice(price);
    } catch (AlreadyTakenSeatException e) {
      System.out.println("That ticket has already been purchased!\n");
      buyTicket();
    } catch (NonExistingSeatException e) {
      System.out.println("Wrong input!\n");
      buyTicket();
    }
  }

  /**
   * It's a method that prints the statistics of the cinema
   */
  public void statistics() {
    System.out.printf("""
                      Number of purchased tickets: %d
                      Percentage: %s%%
                      Current income: $%d
                      Total income: $%d
                      """, cinema.getSoldTickets(), getPercentage(), cinema.getCurrentIncome(), getTotalIncome());
    System.out.println();
  }

  /**
   * It's a method that helps to reserve the seat
   */
  public Cinema.Seat reserveSeat() {
    System.out.println("Enter a row number:");
    int row = scanner.nextInt();

    System.out.println("Enter a seat number in that row:");
    int column = scanner.nextInt();

    System.out.println();
    return new Cinema.Seat(row, column);
  }

  /**
   * It's a method that validates the seat. <br/>
   * If the seat doesn't exist, it throws a `NonExistingSeatException`. <br/>
   * If the seat has already been taken, it throws an `AlreadyTakenSeatException`.
   */
  public void seatValidation(Cinema.Seat seat) throws NonExistingSeatException, AlreadyTakenSeatException {
    if (seat.row() < 1 || seat.row() > cinema.getRows() || seat.column() < 1 || seat.column() > cinema.getColumns()) {
      throw new NonExistingSeatException();
    }

    if (cinema.getSeats()[seat.row() - 1][seat.column() - 1].equals(SOLD_SEAT)) {
      throw new AlreadyTakenSeatException();
    } else {
      cinema.getSeats()[seat.row() - 1][seat.column() - 1] = SOLD_SEAT;
    }
  }

  private int getTotalSeats() {
    return cinema.getColumns() * cinema.getRows();
  }

  private int getPrice(Cinema.Seat seat) {
    int price;
    boolean isFrontSide = seat.row() <= cinema.getRows() / 2;

    price = getTotalSeats() <= 60 || isFrontSide ? 10 : 8;

    return price;
  }

  private void printPrice(int price) {
    System.out.printf("Ticket price: $%s\n\n", price);
  }

  private int getTotalIncome() {
    if (getTotalSeats() <= 60) {
      return cinema.getRows() * cinema.getColumns() * 10;
    } else {
      return (cinema.getRows() / 2) * cinema.getColumns() * 10 +
          (cinema.getRows() - cinema.getRows() / 2) * cinema.getColumns() * 8;
    }
  }

  private String getPercentage() {
    double percentage = (double) cinema.getSoldTickets() * 100 / getTotalSeats();
    return String.format("%.2f", percentage);
  }

}
