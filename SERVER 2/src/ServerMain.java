import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {

    private static List<Wydarzenie> wydarzenia = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        przygotujDane();

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serwer uruchomiony na porcie 5000");

        while (true) {
            Socket klient = serverSocket.accept();
            System.out.println("Nowe połączenie: " + klient.getInetAddress());
            new ClientHandler(klient).start();
        }
    }

    private static void przygotujDane() {
        // Format: nazwa;miasto;cena;kategoria
        wydarzenia.add(new Wydarzenie("Pitbull - koncert", "Warszawa", 300.0, "koncert"));
        wydarzenia.add(new Wydarzenie("Conan Gray - koncert", "Krakow", 400.0, "koncert"));
        wydarzenia.add(new Wydarzenie("PZM FIM Speedway World Cup", "Warszawa", 150.0, "sport"));
        wydarzenia.add(new Wydarzenie("Jezioro Łabędzie - Królewski Balet", "Wroclaw", 150.0, "teatr"));
        wydarzenia.add(new Wydarzenie("Van Gogh Alive - wystawa", "Gdansk", 80.0, "wystawy"));
        wydarzenia.add(new Wydarzenie("Kabaret Moralnego Niepokoju", "Poznan", 120.0, "kabaret"));
        wydarzenia.add(new Wydarzenie("Mistrzostwa Polski w siatkówce", "Bydgoszcz", 90.0, "sport"));
        wydarzenia.add(new Wydarzenie("Chopin Live - koncert", "Gdansk", 180.0, "koncert"));
        wydarzenia.add(new Wydarzenie("Moniuszko - Straszny Dwór", "Bialystok", 120.0, "teatr"));
        wydarzenia.add(new Wydarzenie("Liga Mistrzów - futsal", "Katowice", 110.0, "sport"));
        wydarzenia.add(new Wydarzenie("Wystawa sztuki współczesnej", "Lublin", 60.0, "wystawy"));
        wydarzenia.add(new Wydarzenie("Koncert jazzowy", "Kielce", 95.0, "koncert"));
        wydarzenia.add(new Wydarzenie("Kabaret Ani Mru-Mru", "Lodz", 130.0, "kabaret"));
        wydarzenia.add(new Wydarzenie("Turniej koszykówki", "Szczecin", 85.0, "sport"));

        System.out.println("Załadowano " + wydarzenia.size() + " wydarzeń na serwerze");
    }

    static class ClientHandler extends Thread {
        private Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);

                String linia;
                while ((linia = in.readLine()) != null) {
                    System.out.println("Otrzymano komendę: " + linia);

                    if (linia.equals("LISTA")) {
                        for (Wydarzenie w : wydarzenia) {
                            out.println(w.toString());
                        }
                        out.println("KONIEC");
                        System.out.println("Wysłano listę wydarzeń (" + wydarzenia.size() + " pozycji)");
                    }
                }
            } catch (Exception e) {
                System.out.println("Błąd w komunikacji z klientem: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Błąd przy zamykaniu połączenia: " + e.getMessage());
                }
            }
        }
    }
}