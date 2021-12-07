package service;

public class Cinema {

    private final int rows;
    private final int columns;
    private String[][] seats;
    private int soldTickets;
    private int currentIncome;

    public Cinema(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public String[][] getSeats() {
        return seats;
    }

    public void setSeats(String[][] seats) {
        this.seats = seats;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }

    public int getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(int currentIncome) {
        this.currentIncome = currentIncome;
    }

    static class Seat {

        int row;
        int columns;

        Seat(int row, int columns) {
            this.row = row;
            this.columns = columns;
        }
    }
}
