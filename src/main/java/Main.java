import java.util.Scanner;

import domain.Cinema;
import service.CinemaService;

public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter the number of rows:");
    int rows = scanner.nextInt();

    System.out.println("Enter the number of seats in each row:");
    int cols = scanner.nextInt();

    Cinema cinema = new Cinema(rows, cols);

    CinemaService cinemaService = new CinemaService(scanner, cinema);

    selectMenuOption(scanner, cinemaService);
  }

  private static void selectMenuOption(final Scanner scanner, final CinemaService cinemaService) {
    String option;

    do {
      System.out.print("""
                       1. Show the seats
                       2. Buy a ticket
                       3. Statistics
                       0. Exit
                       """);

      option = scanner.next();
      System.out.println();

      cinemaService.showMenu(option);
    } while (!option.equals("0"));

    scanner.close();
  }

}
