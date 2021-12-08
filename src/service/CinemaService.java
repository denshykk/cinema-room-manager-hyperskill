package service;

import exception.AlreadyTakenSeatException;
import exception.NonExistingSeatException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CinemaService {

    private final Scanner scanner;
    private final Cinema cinema;

    public CinemaService() {
        scanner = new Scanner(System.in);
        cinema = initializeCinema();
        createSeats();
    }

    private Cinema initializeCinema() {
        System.out.println("Enter the number of rows:");
        int rows = scanner.nextInt();

        System.out.println("Enter the number of seats in each row:");
        int cols = scanner.nextInt();

        System.out.println();

        return new Cinema(rows, cols);
    }

    private void createSeats() {
        final String S = "S";
        String[][] seats = new String[cinema.getRows()][];

        for (int i = 0; i < cinema.getRows(); i++) {
            String[] row = new String[cinema.getColumns()];
            Arrays.fill(row, S);
            seats[i] = row;
        }

        cinema.setSeats(seats);
    }

    public void showMenu() {
        while (true) {
            System.out.print("""
                    1. Show the seats
                    2. Buy a ticket
                    3. Statistics
                    0. Exit
                    """);

            String input = scanner.next();
            System.out.println();

            switch (input) {
                case "0" -> {
                    scanner.close();
                    return;
                }
                case "1" -> showSeats();
                case "2" -> buyTicket();
                case "3" -> statistics();
                default -> System.out.printf("Unknown menu item - %s\n", input);
            }
        }
    }

    private void showSeats() {
        System.out.print("Cinema:\n ");
        IntStream.rangeClosed(1, cinema.getSeats()[0].length)
                .mapToObj(i -> " \t" + i)
                .forEach(System.out::print);

        System.out.println();

        int counter = 1;
        for (String[] seat : cinema.getSeats()) {
            System.out.print(counter);
            for (String s : seat) {
                System.out.print("\t".concat(s));
            }
            System.out.println();
            counter++;
        }

        System.out.println();
    }

    private void buyTicket() {
        try {
            Cinema.Seat seat = takeSeat();
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

    private void statistics() {
        System.out.printf("""
                Number of purchased tickets: %d
                Percentage: %s%%
                Current income: $%d
                Total income: $%d
                """, cinema.getSoldTickets(), getPercentage(), cinema.getCurrentIncome(), getTotalIncome());
        System.out.println();
    }

    private Cinema.Seat takeSeat() {
        System.out.println("Enter a row number:");
        int row = scanner.nextInt();

        System.out.println("Enter a seat number in that row:");
        int column = scanner.nextInt();

        System.out.println();
        return new Cinema.Seat(row, column);
    }

    private void seatValidation(Cinema.Seat seat) throws NonExistingSeatException, AlreadyTakenSeatException {
        final String B = "B";

        if (seat.row() < 1 || seat.row() > cinema.getRows() || seat.column() < 1 || seat.column() > cinema.getColumns()) {
            throw new NonExistingSeatException();
        }

        if (cinema.getSeats()[seat.row() - 1][seat.column() - 1].equals(B)) {
            throw new AlreadyTakenSeatException();
        } else {
            cinema.getSeats()[seat.row() - 1][seat.column() - 1] = B;
        }
    }

    private int getTotalSeats() {
        return cinema.getColumns() * cinema.getRows();
    }

    private int getPrice(Cinema.Seat seat) {
        int price;
        boolean isFrontSide = seat.row() <= cinema.getRows() / 2;

        if (getTotalSeats() <= 60 || isFrontSide) {
            price = 10;
        } else {
            price = 8;
        }

        return price;
    }

    private void printPrice(int price) {
        System.out.printf("Ticket price: $%s\n\n", price);
    }

    private int getTotalIncome() {
        if (getTotalSeats() <= 60) {
            return cinema.getRows() * cinema.getColumns() * 10;
        } else {
            return (cinema.getRows() / 2) * cinema.getColumns() * 10 + (cinema.getRows() - cinema.getRows() / 2) * cinema.getColumns() * 8;
        }
    }

    private String getPercentage() {
        double percentage = (double) cinema.getSoldTickets() * 100 / getTotalSeats();
        return String.format("%.2f", percentage);
    }
}
