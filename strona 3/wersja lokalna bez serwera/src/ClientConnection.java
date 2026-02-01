import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static List<Wydarzenie> pobierzWydarzenia() {
        List<Wydarzenie> lista = new ArrayList<>();
        try {
            Socket socket = new Socket(HOST, PORT);
            socket.setSoTimeout(3000); // Timeout 3 sekundy

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            out.println("LISTA");
            String linia;
            while (!(linia = in.readLine()).equals("KONIEC")) {
                // format: nazwa;miasto;cena;kategoria
                String[] dane = linia.split(";");
                if (dane.length >= 4) {
                    String nazwa = dane[0];
                    String miasto = dane[1];
                    double cena = Double.parseDouble(dane[2]);
                    String kategoria = dane[3];

                    Miasto m = new Miasto(miasto);
                    Miejsce miejsce = new Miejsce("Domyślne miejsce", m, "brak adresu");

                    // Ustawiamy datę na przyszłość (np. za 10 dni od teraz)
                    LocalDateTime data = LocalDateTime.now().plusDays(10);

                    Wydarzenie w = new Wydarzenie(
                            nazwa,
                            miejsce,
                            data,
                            cena,
                            100, // domyślna liczba miejsc
                            kategoria
                    );
                    lista.add(w);
                }
            }
            socket.close();
            System.out.println("Pobrano " + lista.size() + " wydarzeń z serwera");
        } catch (Exception e) {
            System.out.println("Nie można połączyć się z serwerem (port " + PORT + "): " + e.getMessage());
            // Nie wyświetlamy dialogu błędu, program użyje danych lokalnych
        }
        return lista;
    }
}