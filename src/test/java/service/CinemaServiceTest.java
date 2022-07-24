package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.Cinema;
import exception.NonExistingSeatException;
import lombok.SneakyThrows;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

  public static final String[][] SEATS = {{"S", "S", "S", "S", "S", "S"}, {"S", "S", "S", "S", "S", "S"},
      {"S", "S", "S", "S", "S", "S"}, {"S", "S", "S", "S", "S", "S"}, {"S", "S", "S", "S", "S", "S"},
      {"S", "S", "S", "S", "S", "S"}};

  @Mock
  private Cinema        cinema;
  @InjectMocks
  private CinemaService cinemaService;

  @Test
  void shouldInvokeShowSeatsMethod() {
    when(cinema.getSeats()).thenReturn(SEATS);

    cinemaService.showMenu("1");

    verify(cinema, times(2)).getSeats();
  }

  @Test
  void shouldInvokeShowBuyTicket() {
    String expected = """
                      Enter a row number:
                      Enter a seat number in that row:
                                            
                      Ticket price: $10
                                                          
                      """;

    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final PrintStream printStream = new PrintStream(out);
    System.setOut(printStream);
    InputStream in = new ByteArrayInputStream("1\n1\n".getBytes());
    CinemaService cinemaService = new CinemaService(new Scanner(in), new Cinema(6, 6));

    cinemaService.showMenu("2");

    assertEquals(expected, out.toString());
    assertNotNull(printStream.toString());
  }

  @Test
  void shouldInvokeShowStatistics() {
    when(cinema.getSoldTickets()).thenReturn(2);
    when(cinema.getCurrentIncome()).thenReturn(20);
    when(cinema.getColumns()).thenReturn(6);
    when(cinema.getRows()).thenReturn(6);

    cinemaService.showMenu("3");

    verify(cinema, times(2)).getSoldTickets();
    verify(cinema, times(5)).getRows();
    verify(cinema, times(3)).getColumns();
  }

  @Test
  void testCreateSeats() {
    cinemaService.createSeats();

    verify(cinema, times(2)).setSeats(any());
  }

  @Test
  void testShowSeats() {
    when(cinema.getSeats()).thenReturn(SEATS);

    cinemaService.showSeats();

    verify(cinema, times(2)).getSeats();
  }

  @Test
  void testReserveSeat() {
    InputStream in = new ByteArrayInputStream("1\n1\n".getBytes());
    CinemaService cinemaService = new CinemaService(new Scanner(in), cinema);

    final Cinema.Seat result = cinemaService.reserveSeat();

    assertNotNull(result);
    assertEquals(1, result.row());
    assertEquals(1, result.column());
  }

  @Test
  void testStatistics() {
    when(cinema.getSoldTickets()).thenReturn(2);
    when(cinema.getCurrentIncome()).thenReturn(20);
    when(cinema.getColumns()).thenReturn(6);
    when(cinema.getRows()).thenReturn(6);

    cinemaService.statistics();

    verify(cinema, times(2)).getSoldTickets();
    verify(cinema, times(5)).getRows();
    verify(cinema, times(3)).getColumns();
  }

  @Test
  @SneakyThrows
  void testSeatValidation() {
    when(cinema.getSeats()).thenReturn(SEATS);
    when(cinema.getRows()).thenReturn(6);
    when(cinema.getColumns()).thenReturn(6);

    assertDoesNotThrow(() -> cinemaService.seatValidation(new Cinema.Seat(1, 1)));
  }

  @Test
  @SneakyThrows
  void shouldThrowNonExistingSeatException() {
    assertThrows(NonExistingSeatException.class, () -> cinemaService.seatValidation(new Cinema.Seat(1, 1)));
  }

}
