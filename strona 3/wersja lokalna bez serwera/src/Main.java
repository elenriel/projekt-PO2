import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static List<Wydarzenie> listaWydarzen = new ArrayList<>();
    private static List<Wydarzenie> wszystkieWydarzenia = new ArrayList<>();
    private static List<Rezerwacja> koszyk = new ArrayList<>();
    private static Wydarzenie wybraneWydarzenie = null;
    private static JFrame ramka;
    private static CardLayout karty;
    private static JPanel panelGlowny;
    private static JButton koszykButton;
    private static String aktualneMiasto = "Wszystkie miasta";
    private static String aktualnaKategoria = "wszystkie";

    private static final Color PRIMARY_BLUE = new Color(0, 123, 255);
    private static final Color LIGHT_BLUE = new Color(52, 152, 219);
    private static final Color DARK_BLUE = new Color(44, 62, 80);
    private static final Color BG_LIGHT = new Color(248, 249, 250);
    private static final Color INFO_BLUE = new Color(33, 150, 243);

    private static JLabel[] validationLabels;
    private static JTextField[] formFields;
    private static JTextField wyszukiwarkaField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // PRÓBUJEMY POBIERAĆ Z SERWERA
            try {
                listaWydarzen = ClientConnection.pobierzWydarzenia();
                wszystkieWydarzenia = new ArrayList<>(listaWydarzen);

                if (!listaWydarzen.isEmpty()) {
                    System.out.println("Pobrano " + listaWydarzen.size() + " wydarzeń z serwera");
                } else {
                    // Jeśli serwer nie zwrócił danych, używamy lokalnych
                    przygotujDanePrzykładowe();
                    wszystkieWydarzenia = new ArrayList<>(listaWydarzen);
                    System.out.println("Używam danych lokalnych (" + listaWydarzen.size() + " wydarzeń)");
                }
            } catch (Exception e) {
                // W przypadku błędu połączenia, używamy danych lokalnych
                System.out.println("Błąd połączenia z serwerem: " + e.getMessage());
                przygotujDanePrzykładowe();
                wszystkieWydarzenia = new ArrayList<>(listaWydarzen);
            }

            stworzIUruchomGUI();
        });
    }

    private static void przygotujDanePrzykładowe() {
        // Czyszczenie listy przed dodaniem nowych danych
        listaWydarzen.clear();

        Miasto warszawa = new Miasto("Warszawa");
        Miasto krakow = new Miasto("Krakow");
        Miasto wroclaw = new Miasto("Wroclaw");
        Miasto gdansk = new Miasto("Gdansk");
        Miasto poznan = new Miasto("Poznan");
        Miasto bydgoszcz = new Miasto("Bydgoszcz");
        Miasto katowice = new Miasto("Katowice");
        Miasto lodz = new Miasto("Lodz");
        Miasto szczecin = new Miasto("Szczecin");
        Miasto lublin = new Miasto("Lublin");
        Miasto bialystok = new Miasto("Bialystok");
        Miasto kielce = new Miasto("Kielce");

        Miejsce narodowy = new Miejsce("Stadion Narodowy", warszawa, "al. Poniatowskiego 1");
        Miejsce tauron = new Miejsce("Tauron Arena", krakow, "ul. Lema 7");
        Miejsce opera = new Miejsce("Opera Wrocławska", wroclaw, "ul. Świdnicka 35");
        Miejsce filharmonia = new Miejsce("Filharmonia Bałtycka", gdansk, "ul. Ołowianka 1");
        Miejsce teatr = new Miejsce("Teatr Wielki", poznan, "ul. Fredry 9");
        Miejsce hala = new Miejsce("Hala Łuczniczka", bydgoszcz, "ul. Toruńska 59");
        Miejsce spodek = new Miejsce("Spodek", katowice, "al. Korfantego 35");
        Miejsce atlas = new Miejsce("Atlas Arena", lodz, "al. Bandurskiego 7");
        Miejsce netto = new Miejsce("Netto Arena", szczecin, "ul. Szafera 3");
        Miejsce centrum = new Miejsce("Centrum Spotkania Kultur", lublin, "plac Teatralny 1");
        Miejsce opera_pod = new Miejsce("Opera i Filharmonia Podlaska", bialystok, "ul. Odeska 1");
        Miejsce kck = new Miejsce("Kieleckie Centrum Kultury", kielce, "ul. Warszawska 5");

        listaWydarzen.add(new Wydarzenie("Pitbull - koncert", narodowy,
                LocalDateTime.of(2026, 6, 15, 20, 0), 300.0, 5000, "koncert"));
        listaWydarzen.add(new Wydarzenie("Conan Gray - koncert", tauron,
                LocalDateTime.of(2026, 3, 20, 19, 30), 400.0, 800, "koncert"));
        listaWydarzen.add(new Wydarzenie("PZM FIM Speedway World Cup", narodowy,
                LocalDateTime.of(2026, 7, 10, 18, 0), 150.0, 3000, "sport"));
        listaWydarzen.add(new Wydarzenie("Jezioro Łabędzie - Królewski Balet", opera,
                LocalDateTime.of(2026, 5, 8, 19, 0), 150.0, 1200, "teatr"));
        listaWydarzen.add(new Wydarzenie("Van Gogh Alive - wystawa", filharmonia,
                LocalDateTime.of(2026, 4, 15, 10, 0), 80.0, 200, "wystawy"));
        listaWydarzen.add(new Wydarzenie("Kabaret Moralnego Niepokoju", teatr,
                LocalDateTime.of(2026, 6, 25, 20, 30), 120.0, 500, "kabaret"));
        listaWydarzen.add(new Wydarzenie("Mistrzostwa Polski w siatkówce", hala,
                LocalDateTime.of(2026, 8, 12, 17, 0), 90.0, 1500, "sport"));
        listaWydarzen.add(new Wydarzenie("Chopin Live - koncert", filharmonia,
                LocalDateTime.of(2026, 5, 20, 19, 0), 180.0, 300, "koncert"));
        listaWydarzen.add(new Wydarzenie("Moniuszko - Straszny Dwór", opera_pod,
                LocalDateTime.of(2026, 4, 10, 18, 30), 120.0, 400, "teatr"));
        listaWydarzen.add(new Wydarzenie("Liga Mistrzów - futsal", spodek,
                LocalDateTime.of(2026, 9, 5, 19, 0), 110.0, 2000, "sport"));
        listaWydarzen.add(new Wydarzenie("Wystawa sztuki współczesnej", centrum,
                LocalDateTime.of(2026, 3, 15, 10, 0), 60.0, 100, "wystawy"));
        listaWydarzen.add(new Wydarzenie("Koncert jazzowy", kck,
                LocalDateTime.of(2026, 7, 22, 20, 0), 95.0, 300, "koncert"));
        listaWydarzen.add(new Wydarzenie("Kabaret Ani Mru-Mru", atlas,
                LocalDateTime.of(2026, 5, 30, 19, 30), 130.0, 600, "kabaret"));
        listaWydarzen.add(new Wydarzenie("Turniej koszykówki", netto,
                LocalDateTime.of(2026, 6, 18, 16, 0), 85.0, 1200, "sport"));
    }

    private static void aktualizujKoszykButton() {
        if (koszykButton != null) {
            koszykButton.setText("koszyk (" + koszyk.size() + ")");
        }
    }

    private static void stworzIUruchomGUI() {
        ramka = new JFrame("System Rezerwacji Biletów");
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ramka.setSize(1400, 900);
        ramka.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ramka.setLocationRelativeTo(null);
        ramka.getContentPane().setBackground(BG_LIGHT);

        karty = new CardLayout();
        panelGlowny = new JPanel(karty);
        panelGlowny.setBackground(BG_LIGHT);

        panelGlowny.add(stworzPanelStartowy(), "START");
        panelGlowny.add(stworzPanelSzczegolyWydarzenia(), "SZCZEGOLY");
        panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
        panelGlowny.add(stworzPanelPotwierdzenia(), "POTWIERDZENIE");
        panelGlowny.add(stworzPanelDaneOsobowe(), "DANE_OSOBOWE");

        ramka.add(panelGlowny);
        karty.show(panelGlowny, "START");
        ramka.setVisible(true);
    }

    private static JPanel stworzPanelStartowy() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        JPanel header = stworzHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(BG_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));

        JLabel wydarzenieTitle = new JLabel("WYDARZENIA", SwingConstants.CENTER);
        wydarzenieTitle.setFont(new Font("Segoe UI", Font.BOLD, 64));
        wydarzenieTitle.setForeground(DARK_BLUE);
        wydarzenieTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollContent.add(wydarzenieTitle);
        scrollContent.add(Box.createVerticalStrut(40));

        JPanel filtry = stworzPaneleFiltrow();
        filtry.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollContent.add(filtry);
        scrollContent.add(Box.createVerticalStrut(40));

        JPanel popularnePanel = stworzSekcjeNajpopularniejsze();
        popularnePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollContent.add(popularnePanel);
        scrollContent.add(Box.createVerticalStrut(40));

        JPanel wszystkiePanel = stworzSekcjeWszystkieWydarzenia();
        wszystkiePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollContent.add(wszystkiePanel);
        scrollContent.add(Box.createVerticalStrut(20));

        JPanel gridPanel = stworzPanelWszystkichWydarzen();
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollContent.add(gridPanel);

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getViewport().setBackground(BG_LIGHT);
        mainPanel.add(scroll, BorderLayout.CENTER);

        return mainPanel;
    }

    private static void pokazOkienkoDodanoDoKoszyka(int liczbaBiletow) {
        JDialog dialog = new JDialog(ramka, "Dodano do koszyka", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 300);
        dialog.setLocationRelativeTo(ramka);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel message = new JLabel("Dodano " + liczbaBiletow + " bilet(ów) do koszyka!");
        message.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(message);

        contentPanel.add(Box.createVerticalStrut(50));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));

        JButton btnPrzegladaj = new JButton("Przeglądaj dalej");
        btnPrzegladaj.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPrzegladaj.setBackground(LIGHT_BLUE);
        btnPrzegladaj.setForeground(Color.WHITE);
        btnPrzegladaj.setFocusPainted(false);
        btnPrzegladaj.setPreferredSize(new Dimension(240, 60));
        btnPrzegladaj.addActionListener(e -> {
            dialog.dispose();
        });

        JButton btnDoKoszyka = new JButton("Przejdź do koszyka");
        btnDoKoszyka.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnDoKoszyka.setBackground(PRIMARY_BLUE);
        btnDoKoszyka.setForeground(Color.WHITE);
        btnDoKoszyka.setFocusPainted(false);
        btnDoKoszyka.setPreferredSize(new Dimension(240, 60));
        btnDoKoszyka.addActionListener(e -> {
            dialog.dispose();
            aktualizujKoszykButton();
            panelGlowny.remove(2);
            panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
            karty.show(panelGlowny, "KOSZYK");
        });

        buttonPanel.add(btnPrzegladaj);
        buttonPanel.add(btnDoKoszyka);
        contentPanel.add(buttonPanel);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private static JPanel stworzHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_LIGHT);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        koszykButton = new JButton("koszyk (" + koszyk.size() + ")");
        koszykButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        koszykButton.setBackground(PRIMARY_BLUE);
        koszykButton.setForeground(Color.WHITE);
        koszykButton.setFocusPainted(false);
        koszykButton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        koszykButton.addActionListener(e -> {
            // Odśwież panel koszyka przed pokazaniem
            aktualizujKoszykButton();
            int koszykIndex = znajdzIndexPanelu("KOSZYK");
            if (koszykIndex >= 0) {
                panelGlowny.remove(koszykIndex);
            }
            panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
            karty.show(panelGlowny, "KOSZYK");
        });

        header.add(koszykButton, BorderLayout.EAST);
        return header;
    }

    private static int znajdzIndexPanelu(String nazwaPanelu) {
        for (int i = 0; i < panelGlowny.getComponentCount(); i++) {
            Component comp = panelGlowny.getComponent(i);
            if (comp instanceof JPanel) {
                String compName = comp.getName();
                if (compName != null && compName.equals(nazwaPanelu)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static JPanel stworzPaneleFiltrow() {
        JPanel filtry = new JPanel();
        filtry.setLayout(new BoxLayout(filtry, BoxLayout.X_AXIS));
        filtry.setBackground(BG_LIGHT);
        filtry.setMaximumSize(new Dimension(1200, 80));
        filtry.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] miasta = {"Wszystkie miasta", "Warszawa", "Krakow", "Wroclaw", "Gdansk",
                "Poznan", "Bydgoszcz", "Katowice", "Lodz", "Szczecin",
                "Lublin", "Bialystok", "Kielce"};
        JComboBox<String> comboMiasta = new JComboBox<>(miasta);
        comboMiasta.setPreferredSize(new Dimension(220, 50));
        comboMiasta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboMiasta.setSelectedItem(aktualneMiasto);
        comboMiasta.addActionListener(e -> {
            aktualneMiasto = (String) comboMiasta.getSelectedItem();
            aktualizujPanelStartowy();
        });

        wyszukiwarkaField = new JTextField(30);
        wyszukiwarkaField.setText("Szukaj wydarzenia");
        wyszukiwarkaField.setForeground(Color.GRAY);
        wyszukiwarkaField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        wyszukiwarkaField.setMaximumSize(new Dimension(400, 50));
        wyszukiwarkaField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (wyszukiwarkaField.getText().equals("Szukaj wydarzenia")) {
                    wyszukiwarkaField.setText("");
                    wyszukiwarkaField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (wyszukiwarkaField.getText().isEmpty()) {
                    wyszukiwarkaField.setText("Szukaj wydarzenia");
                    wyszukiwarkaField.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnSzukaj = new JButton("SZUKAJ");
        btnSzukaj.setBackground(PRIMARY_BLUE);
        btnSzukaj.setForeground(Color.WHITE);
        btnSzukaj.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSzukaj.setFocusPainted(false);
        btnSzukaj.setPreferredSize(new Dimension(150, 50));
        btnSzukaj.addActionListener(e -> {
            String tekst = wyszukiwarkaField.getText();
            if (!tekst.equals("Szukaj wydarzenia") && !tekst.isEmpty()) {
                System.out.println("Wyszukiwanie: " + tekst);
                List<Wydarzenie> wynik = filtrujWydarzeniaPoNazwie(tekst);
                System.out.println("Znaleziono " + wynik.size() + " wyników");
                aktualizujListeWydarzen(wynik);
            } else {
                // Jeśli pole puste, pokaż wszystkie
                aktualizujPanelStartowy();
            }
        });

        // Przycisk Enter też powinien wyszukiwać
        wyszukiwarkaField.addActionListener(e -> btnSzukaj.doClick());

        JButton btnWyczysc = new JButton("WYCZYŚĆ");
        btnWyczysc.setBackground(LIGHT_BLUE);
        btnWyczysc.setForeground(Color.WHITE);
        btnWyczysc.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnWyczysc.setFocusPainted(false);
        btnWyczysc.setPreferredSize(new Dimension(150, 50));
        btnWyczysc.addActionListener(e -> {
            aktualneMiasto = "Wszystkie miasta";
            aktualnaKategoria = "wszystkie";
            wyszukiwarkaField.setText("Szukaj wydarzenia");
            wyszukiwarkaField.setForeground(Color.GRAY);
            comboMiasta.setSelectedItem("Wszystkie miasta");
            aktualizujPanelStartowy();
        });

        filtry.add(Box.createHorizontalStrut(20));
        filtry.add(new JLabel("Miasto:"));
        filtry.add(Box.createHorizontalStrut(10));
        filtry.add(comboMiasta);
        filtry.add(Box.createHorizontalStrut(30));
        filtry.add(new JLabel("Szukaj:"));
        filtry.add(Box.createHorizontalStrut(10));
        filtry.add(wyszukiwarkaField);
        filtry.add(Box.createHorizontalStrut(10));
        filtry.add(btnSzukaj);
        filtry.add(Box.createHorizontalStrut(10));
        filtry.add(btnWyczysc);
        filtry.add(Box.createHorizontalStrut(20));

        return filtry;
    }

    private static JPanel stworzSekcjeNajpopularniejsze() {
        JPanel sekcja = new JPanel();
        sekcja.setLayout(new BoxLayout(sekcja, BoxLayout.Y_AXIS));
        sekcja.setBackground(BG_LIGHT);
        sekcja.setAlignmentX(Component.CENTER_ALIGNMENT);
        sekcja.setMaximumSize(new Dimension(1200, 400));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BG_LIGHT);
        titlePanel.setOpaque(false);

        JLabel popularneTitle = new JLabel("Najpopularniejsze wydarzenia");
        popularneTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        popularneTitle.setForeground(DARK_BLUE);
        titlePanel.add(popularneTitle);

        if (!aktualneMiasto.equals("Wszystkie miasta")) {
            JLabel miastoLabel = new JLabel("w " + aktualneMiasto);
            miastoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            miastoLabel.setForeground(INFO_BLUE);
            miastoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            titlePanel.add(miastoLabel);
        }

        sekcja.add(titlePanel);
        sekcja.add(Box.createVerticalStrut(20));

        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        cardsPanel.setBackground(BG_LIGHT);
        cardsPanel.setOpaque(false);

        List<Wydarzenie> popularneWydarzenia = pobierzNajpopularniejszeWydarzenia();

        if (!popularneWydarzenia.isEmpty()) {
            for (int i = 0; i < Math.min(3, popularneWydarzenia.size()); i++) {
                JPanel karta = stworzNowoczesnaKarteWydarzenia(popularneWydarzenia.get(i), true);
                cardsPanel.add(karta);
            }
            sekcja.add(cardsPanel);
        } else {
            JPanel brakPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            brakPanel.setBackground(BG_LIGHT);
            brakPanel.setOpaque(false);

            JLabel brakLabel = new JLabel("Brak popularnych wydarzeń w wybranej lokalizacji");
            brakLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            brakLabel.setForeground(Color.GRAY);
            brakPanel.add(brakLabel);
            sekcja.add(brakPanel);
        }

        return sekcja;
    }

    private static JPanel stworzSekcjeWszystkieWydarzenia() {
        JPanel sekcja = new JPanel();
        sekcja.setLayout(new BoxLayout(sekcja, BoxLayout.Y_AXIS));
        sekcja.setBackground(BG_LIGHT);
        sekcja.setAlignmentX(Component.CENTER_ALIGNMENT);
        sekcja.setMaximumSize(new Dimension(1200, 150));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BG_LIGHT);
        titlePanel.setOpaque(false);

        JLabel wszystkieTitle = new JLabel("Wszystkie wydarzenia");
        wszystkieTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        wszystkieTitle.setForeground(DARK_BLUE);
        titlePanel.add(wszystkieTitle);

        if (!aktualneMiasto.equals("Wszystkie miasta")) {
            JLabel miastoLabel = new JLabel("w " + aktualneMiasto);
            miastoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            miastoLabel.setForeground(INFO_BLUE);
            miastoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            titlePanel.add(miastoLabel);
        }

        sekcja.add(titlePanel);
        sekcja.add(Box.createVerticalStrut(20));

        JPanel kategoriePanel = stworzPrzyciskiKategorii();
        kategoriePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sekcja.add(kategoriePanel);

        return sekcja;
    }

    private static JPanel stworzPrzyciskiKategorii() {
        JPanel kategorie = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        kategorie.setBackground(BG_LIGHT);
        kategorie.setOpaque(false);
        kategorie.setMaximumSize(new Dimension(1200, 70));

        String[] kategorieText = {"wszystkie", "koncert", "sport", "teatr", "wystawy", "kabaret"};

        for (String kat : kategorieText) {
            JButton kategoriaBtn = new JButton(kat);
            kategoriaBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));

            if (kat.equals(aktualnaKategoria)) {
                kategoriaBtn.setBackground(PRIMARY_BLUE);
                kategoriaBtn.setForeground(Color.WHITE);
            } else {
                kategoriaBtn.setBackground(new Color(240, 240, 240));
                kategoriaBtn.setForeground(DARK_BLUE);
            }

            kategoriaBtn.setFocusPainted(false);
            kategoriaBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(LIGHT_BLUE, 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));

            kategoriaBtn.addActionListener(e -> {
                aktualnaKategoria = kat;
                aktualizujPanelStartowy();
            });

            kategorie.add(kategoriaBtn);
        }

        return kategorie;
    }

    private static List<Wydarzenie> pobierzNajpopularniejszeWydarzenia() {
        return pobierzWydarzeniaDoWyswietlenia().stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    private static List<Wydarzenie> pobierzWydarzeniaDoWyswietlenia() {
        List<Wydarzenie> wynik = wszystkieWydarzenia; // Używamy oryginalnej referencji

        if (!aktualneMiasto.equals("Wszystkie miasta")) {
            wynik = wynik.stream()
                    .filter(w -> w.getMiejsce().getMiasto().getNazwa().equalsIgnoreCase(aktualneMiasto))
                    .collect(Collectors.toList());
        }

        if (!aktualnaKategoria.equals("wszystkie")) {
            wynik = wynik.stream()
                    .filter(w -> w.getKategoria().equalsIgnoreCase(aktualnaKategoria))
                    .collect(Collectors.toList());
        }

        return wynik;
    }

    private static void aktualizujPanelStartowy() {
        panelGlowny.remove(0);
        panelGlowny.add(stworzPanelStartowy(), "START");
        karty.show(panelGlowny, "START");
        ramka.revalidate();
        ramka.repaint();
    }

    private static void aktualizujListeWydarzen(List<Wydarzenie> nowaLista) {
        // ZACHOWUJEMY oryginalną listę, ale filtrujemy na bieżąco
        // Nie nadpisujemy listyWydarzen, tylko używamy pomocniczej listy do wyświetlania
        // To naprawia problem z dodawaniem do koszyka
        List<Wydarzenie> filtrowanaLista = new ArrayList<>(nowaLista);

        // Tworzymy nowy panel startowy z filtrowaną listą
        JPanel nowyPanel = stworzPanelStartowyZFiltrowanaLista(filtrowanaLista);
        panelGlowny.remove(0);
        panelGlowny.add(nowyPanel, "START");
        karty.show(panelGlowny, "START");
        ramka.revalidate();
        ramka.repaint();
    }

    private static JPanel stworzPanelStartowyZFiltrowanaLista(List<Wydarzenie> filtrowanaLista) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        JPanel header = stworzHeader();
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(BG_LIGHT);
        scrollContent.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));

        JLabel wydarzenieTitle = new JLabel("WYNIKI WYSZUKIWANIA", SwingConstants.CENTER);
        wydarzenieTitle.setFont(new Font("Segoe UI", Font.BOLD, 64));
        wydarzenieTitle.setForeground(DARK_BLUE);
        wydarzenieTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollContent.add(wydarzenieTitle);
        scrollContent.add(Box.createVerticalStrut(40));

        JPanel filtry = stworzPaneleFiltrow();
        filtry.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollContent.add(filtry);
        scrollContent.add(Box.createVerticalStrut(40));

        if (!filtrowanaLista.isEmpty()) {
            JLabel wynikLabel = new JLabel("Znaleziono " + filtrowanaLista.size() + " wydarzeń", SwingConstants.CENTER);
            wynikLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            wynikLabel.setForeground(INFO_BLUE);
            wynikLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            scrollContent.add(wynikLabel);
            scrollContent.add(Box.createVerticalStrut(20));

            JPanel gridPanel = stworzPanelWydarzenZListy(filtrowanaLista);
            gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            scrollContent.add(gridPanel);
        } else {
            JLabel brakLabel = new JLabel("Nie znaleziono wydarzeń", SwingConstants.CENTER);
            brakLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
            brakLabel.setForeground(Color.GRAY);
            brakLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            scrollContent.add(brakLabel);
        }

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getViewport().setBackground(BG_LIGHT);
        mainPanel.add(scroll, BorderLayout.CENTER);

        return mainPanel;
    }

    private static JPanel stworzPanelWydarzenZListy(List<Wydarzenie> wydarzeniaLista) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(1200, 600));

        if (!wydarzeniaLista.isEmpty()) {
            int rows = (int) Math.ceil(wydarzeniaLista.size() / 3.0);
            JPanel gridWszystkie = new JPanel(new GridLayout(rows, 3, 20, 20));
            gridWszystkie.setBackground(BG_LIGHT);
            gridWszystkie.setOpaque(false);
            gridWszystkie.setMaximumSize(new Dimension(1100, rows * 300));

            for (Wydarzenie w : wydarzeniaLista) {
                // Wydarzenia z wyszukiwania są już z oryginalnej listy
                JPanel karta = stworzNowoczesnaKarteWydarzenia(w, false);
                gridWszystkie.add(karta);
            }

            int emptySlots = rows * 3 - wydarzeniaLista.size();
            for (int i = 0; i < emptySlots; i++) {
                gridWszystkie.add(new JPanel());
            }

            JScrollPane scrollWszystkie = new JScrollPane(gridWszystkie);
            scrollWszystkie.setBorder(BorderFactory.createEmptyBorder());
            scrollWszystkie.getVerticalScrollBar().setUnitIncrement(20);
            scrollWszystkie.setPreferredSize(new Dimension(1150, 500));

            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.setBackground(BG_LIGHT);
            centerPanel.add(scrollWszystkie);

            panel.add(centerPanel, BorderLayout.CENTER);
        }

        return panel;
    }

    private static JPanel stworzNowoczesnaKarteWydarzenia(Wydarzenie w, boolean isPopularne) {
        JPanel karta = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(245, 248, 255));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };

        karta.setPreferredSize(new Dimension(isPopularne ? 380 : 350, isPopularne ? 280 : 250));
        karta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25),
                BorderFactory.createLineBorder(new Color(200, 220, 255), 2, true)
        ));
        karta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        karta.setBackground(Color.WHITE);

        // Debug: wypisz informacje o wydarzeniu
        System.out.println("Tworzę kartę dla: " + w.getNazwa() +
                " | Miasto: " + w.getMiejsce().getMiasto().getNazwa() +
                " | Czy w głównej liście: " + wszystkieWydarzenia.contains(w));

        // Używamy bezpośrednio przekazanego wydarzenia - powinno być już z oryginalnej listy
        final Wydarzenie wydarzenieDoUzycia = w;

        karta.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { karta.setBackground(new Color(240, 248, 255)); }
            @Override public void mouseExited(MouseEvent e) { karta.setBackground(Color.WHITE); }
            @Override public void mouseClicked(MouseEvent e) {
                System.out.println("Kliknięto kartę: " + wydarzenieDoUzycia.getNazwa());
                wybraneWydarzenie = wydarzenieDoUzycia;
                karty.show(panelGlowny, "SZCZEGOLY");
            }
        });

        JPanel info = new JPanel(new GridLayout(5, 1, 0, 8));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JLabel nazwa = new JLabel(w.getNazwa());
        nazwa.setFont(new Font("Segoe UI", Font.BOLD, isPopularne ? 22 : 20));
        nazwa.setForeground(DARK_BLUE);

        JLabel kategoria = new JLabel("Kategoria: " + w.getKategoria());
        kategoria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        kategoria.setForeground(Color.GRAY);

        JLabel data = new JLabel(w.getDataGodzina().toLocalDate().toString());
        data.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        data.setForeground(LIGHT_BLUE);

        JLabel miejsce = new JLabel(w.getMiejsce().toString());
        miejsce.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        miejsce.setForeground(LIGHT_BLUE);

        JLabel cena = new JLabel(w.getCenaBiletu() + " zł");
        cena.setFont(new Font("Segoe UI", Font.BOLD, 20));
        cena.setForeground(PRIMARY_BLUE);

        info.add(nazwa);
        info.add(kategoria);
        info.add(data);
        info.add(miejsce);
        info.add(cena);
        karta.add(info, BorderLayout.CENTER);

        JButton btnKup = new JButton("KUP BILET");
        btnKup.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnKup.setBackground(PRIMARY_BLUE);
        btnKup.setForeground(Color.WHITE);
        btnKup.setFocusPainted(false);
        btnKup.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btnKup.addActionListener(e -> {
            System.out.println("Kliknięto KUP BILET: " + wydarzenieDoUzycia.getNazwa());
            wybraneWydarzenie = wydarzenieDoUzycia;
            karty.show(panelGlowny, "SZCZEGOLY");
        });

        karta.add(btnKup, BorderLayout.SOUTH);
        return karta;
    }

    private static Wydarzenie znajdzOryginalneWydarzenie(Wydarzenie w) {
        // Debug: wypisz informacje o wyszukiwanym wydarzeniu
        System.out.println("Szukam oryginalnego wydarzenia:");
        System.out.println("  Nazwa: " + w.getNazwa());
        System.out.println("  Miejsce: " + w.getMiejsce().getNazwa());
        System.out.println("  Miasto: " + w.getMiejsce().getMiasto().getNazwa());
        System.out.println("  Data: " + w.getDataGodzina());
        System.out.println("  Cena: " + w.getCenaBiletu());

        // Szukamy oryginalnego obiektu w głównej liście na podstawie unikalnych właściwości
        for (Wydarzenie original : wszystkieWydarzenia) {
            if (czyToSamoWydarzenie(original, w)) {
                System.out.println("Znaleziono oryginalne wydarzenie!");
                return original;
            }
        }

        System.out.println("Nie znaleziono oryginalnego wydarzenia, używam fallback");
        return w; // fallback - jeśli nie znajdziemy, zwracamy przekazany obiekt
    }

    private static boolean czyToSamoWydarzenie(Wydarzenie w1, Wydarzenie w2) {
        return w1.getNazwa().equals(w2.getNazwa()) &&
                w1.getMiejsce().getNazwa().equals(w2.getMiejsce().getNazwa()) &&
                w1.getMiejsce().getMiasto().getNazwa().equals(w2.getMiejsce().getMiasto().getNazwa()) &&
                w1.getDataGodzina().equals(w2.getDataGodzina()) &&
                w1.getCenaBiletu() == w2.getCenaBiletu();
    }

    private static JPanel stworzPanelSzczegolyWydarzenia() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        String nazwaWydarzenia = (wybraneWydarzenie != null) ? wybraneWydarzenie.getNazwa() : "Wydarzenie";
        double cenaBiletu = (wybraneWydarzenie != null) ? wybraneWydarzenie.getCenaBiletu() : 250.0;

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nazwa = new JLabel(nazwaWydarzenia);
        nazwa.setFont(new Font("Segoe UI", Font.BOLD, 48));
        nazwa.setForeground(DARK_BLUE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        infoPanel.add(nazwa, gbc);

        JLabel cena = new JLabel("Cena biletu: " + cenaBiletu + " zł");
        cena.setFont(new Font("Segoe UI", Font.BOLD, 32));
        cena.setForeground(PRIMARY_BLUE);
        gbc.gridy++; gbc.gridwidth = 1;
        infoPanel.add(cena, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel form = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30));
        form.setBackground(BG_LIGHT);

        JButton btnPowrot = new JButton("<- POWROT DO MENU");
        btnPowrot.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnPowrot.setBackground(LIGHT_BLUE);
        btnPowrot.setForeground(Color.WHITE);
        btnPowrot.setFocusPainted(false);
        btnPowrot.setPreferredSize(new Dimension(220, 60));
        btnPowrot.addActionListener(e -> karty.show(panelGlowny, "START"));

        JLabel lblLiczba = new JLabel("Liczba biletow:");
        lblLiczba.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        spinner.setPreferredSize(new Dimension(120, 50));

        JButton btnKupTeraz = new JButton("KUP TERAZ");
        btnKupTeraz.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnKupTeraz.setBackground(PRIMARY_BLUE);
        btnKupTeraz.setForeground(Color.WHITE);
        btnKupTeraz.setFocusPainted(false);
        btnKupTeraz.setPreferredSize(new Dimension(200, 60));
        btnKupTeraz.addActionListener(e -> {
            int liczba = (Integer) spinner.getValue();
            if (wybraneWydarzenie != null) {
                Rezerwacja r = new Rezerwacja(wybraneWydarzenie, "", "", liczba);
                koszyk.add(r);
                aktualizujKoszykButton();
                pokazOkienkoDodanoDoKoszyka(liczba);
            }
        });

        form.add(btnPowrot);
        form.add(lblLiczba);
        form.add(spinner);
        form.add(btnKupTeraz);

        infoPanel.add(form, gbc);
        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel stworzPanelKoszyk() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton btnPowrot = new JButton("← POWRÓT");
        btnPowrot.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnPowrot.addActionListener(e -> karty.show(panelGlowny, "START"));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nav.setBackground(BG_LIGHT);
        nav.add(btnPowrot);
        panel.add(nav, BorderLayout.NORTH);

        if (koszyk.isEmpty()) {
            JLabel pusty = new JLabel("koszyk pusty", SwingConstants.CENTER);
            pusty.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            pusty.setForeground(new Color(150, 170, 190));
            panel.add(pusty, BorderLayout.CENTER);
        } else {
            JPanel listaKoszyka = new JPanel();
            listaKoszyka.setLayout(new BoxLayout(listaKoszyka, BoxLayout.Y_AXIS));
            listaKoszyka.setBackground(BG_LIGHT);

            JLabel tytul = new JLabel("Koszyk (" + koszyk.size() + " pozycje)");
            tytul.setFont(new Font("Segoe UI", Font.BOLD, 32));
            tytul.setForeground(DARK_BLUE);
            tytul.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaKoszyka.add(tytul);
            listaKoszyka.add(Box.createVerticalStrut(30));

            double suma = 0;
            for (int i = 0; i < koszyk.size(); i++) {
                Rezerwacja r = koszyk.get(i);
                double cena = r.getLiczbaMiejsc() * r.getWydarzenie().getCenaBiletu();
                suma += cena;

                JPanel pozycjaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                pozycjaPanel.setBackground(BG_LIGHT);
                pozycjaPanel.setMaximumSize(new Dimension(800, 100));

                JLabel nazwa = new JLabel(r.getWydarzenie().getNazwa());
                nazwa.setFont(new Font("Segoe UI", Font.BOLD, 18));
                nazwa.setPreferredSize(new Dimension(300, 30));

                JPanel iloscPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

                JButton btnMinus = new JButton("-");
                btnMinus.setFont(new Font("Segoe UI", Font.BOLD, 16));
                btnMinus.setPreferredSize(new Dimension(40, 40));
                final int index = i;
                btnMinus.addActionListener(e -> {
                    if (koszyk.get(index).getLiczbaMiejsc() > 1) {
                        koszyk.get(index).setLiczbaMiejsc(koszyk.get(index).getLiczbaMiejsc() - 1);
                        aktualizujKoszykButton();
                        panelGlowny.remove(2);
                        panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
                        karty.show(panelGlowny, "KOSZYK");
                    } else {
                        koszyk.remove(index);
                        aktualizujKoszykButton();
                        panelGlowny.remove(2);
                        panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
                        karty.show(panelGlowny, "KOSZYK");
                    }
                });

                JLabel lblIlosc = new JLabel(String.valueOf(r.getLiczbaMiejsc()));
                lblIlosc.setFont(new Font("Segoe UI", Font.BOLD, 18));
                lblIlosc.setPreferredSize(new Dimension(40, 30));

                JButton btnPlus = new JButton("+");
                btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 16));
                btnPlus.setPreferredSize(new Dimension(40, 40));
                btnPlus.addActionListener(e -> {
                    if (koszyk.get(index).getLiczbaMiejsc() < 10) {
                        koszyk.get(index).setLiczbaMiejsc(koszyk.get(index).getLiczbaMiejsc() + 1);
                        aktualizujKoszykButton();
                        panelGlowny.remove(2);
                        panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
                        karty.show(panelGlowny, "KOSZYK");
                    }
                });

                iloscPanel.add(btnMinus);
                iloscPanel.add(lblIlosc);
                iloscPanel.add(btnPlus);

                JLabel cenaLabel = new JLabel(cena + " zł");
                cenaLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                cenaLabel.setPreferredSize(new Dimension(100, 30));

                JButton btnUsun = new JButton("Usuń");
                btnUsun.setBackground(new Color(220, 53, 69));
                btnUsun.setForeground(Color.WHITE);
                btnUsun.addActionListener(e -> {
                    koszyk.remove(index);
                    aktualizujKoszykButton();
                    panelGlowny.remove(2);
                    panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
                    karty.show(panelGlowny, "KOSZYK");
                });

                pozycjaPanel.add(nazwa);
                pozycjaPanel.add(iloscPanel);
                pozycjaPanel.add(cenaLabel);
                pozycjaPanel.add(btnUsun);

                listaKoszyka.add(pozycjaPanel);
                listaKoszyka.add(Box.createVerticalStrut(10));
            }

            listaKoszyka.add(Box.createVerticalStrut(30));

            JLabel sumaLabel = new JLabel("SUMA: " + String.format("%.2f", suma) + " zł");
            sumaLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
            sumaLabel.setForeground(PRIMARY_BLUE);
            sumaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaKoszyka.add(sumaLabel);

            JScrollPane scrollLista = new JScrollPane(listaKoszyka);
            scrollLista.setPreferredSize(new Dimension(800, 500));
            panel.add(scrollLista, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(BG_LIGHT);

            JButton btnAnuluj = new JButton("ANULUJ ZAKUPY");
            btnAnuluj.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnAnuluj.setBackground(LIGHT_BLUE);
            btnAnuluj.setForeground(Color.WHITE);
            btnAnuluj.setFocusPainted(false);
            btnAnuluj.setPreferredSize(new Dimension(200, 60));
            btnAnuluj.addActionListener(e -> {
                int result = JOptionPane.showConfirmDialog(ramka,
                        "Czy na pewno chcesz anulować wszystkie zakupy?",
                        "Potwierdzenie anulowania",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    koszyk.clear();
                    aktualizujKoszykButton();
                    karty.show(panelGlowny, "START");
                }
            });

            JButton btnFinalizacja = new JButton("KUP TERAZ");
            btnFinalizacja.setFont(new Font("Segoe UI", Font.BOLD, 20));
            btnFinalizacja.setBackground(PRIMARY_BLUE);
            btnFinalizacja.setForeground(Color.WHITE);
            btnFinalizacja.setFocusPainted(false);
            btnFinalizacja.setPreferredSize(new Dimension(200, 60));
            btnFinalizacja.addActionListener(e -> {
                karty.show(panelGlowny, "DANE_OSOBOWE");
            });

            buttonPanel.add(btnAnuluj);
            buttonPanel.add(btnFinalizacja);
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        return panel;
    }

    private static JPanel stworzPanelDaneOsobowe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton btnPowrot = new JButton("← POWRÓT DO KOSZYKA");
        btnPowrot.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnPowrot.addActionListener(e -> {
            panelGlowny.remove(2);
            panelGlowny.add(stworzPanelKoszyk(), "KOSZYK");
            karty.show(panelGlowny, "KOSZYK");
        });

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nav.setBackground(BG_LIGHT);
        nav.add(btnPowrot);
        panel.add(nav, BorderLayout.NORTH);

        JLabel tytul = new JLabel("Dane osobowe i płatność", SwingConstants.CENTER);
        tytul.setFont(new Font("Segoe UI", Font.BOLD, 36));
        tytul.setForeground(DARK_BLUE);
        tytul.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panel.add(tytul, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Imię:", "Nazwisko:", "Email:", "Telefon (9 cyfr):",
                "Numer karty (16 cyfr):", "Data ważności (MM/RR):", "CVV (3 cyfry):"};

        formFields = new JTextField[labels.length];
        validationLabels = new JLabel[labels.length];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i * 2;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            formFields[i] = new JTextField(20);
            formFields[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
            formFields[i].setPreferredSize(new Dimension(300, 35));

            validationLabels[i] = new JLabel(" ");
            validationLabels[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            validationLabels[i].setForeground(new Color(220, 53, 69));
            validationLabels[i].setPreferredSize(new Dimension(300, 20));

            final int fieldIndex = i;
            formFields[i].getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    if (validationLabels != null && fieldIndex < validationLabels.length) {
                        validateField(fieldIndex);
                    }
                }
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    if (validationLabels != null && fieldIndex < validationLabels.length) {
                        validateField(fieldIndex);
                    }
                }
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    if (validationLabels != null && fieldIndex < validationLabels.length) {
                        validateField(fieldIndex);
                    }
                }
            });

            if (i == 4) {
                formFields[i].setDocument(new javax.swing.text.PlainDocument() {
                    @Override
                    public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                            throws javax.swing.text.BadLocationException {
                        if (str == null) return;
                        if ((getLength() + str.length()) <= 16 && str.matches("[0-9]*")) {
                            super.insertString(offs, str, a);
                        }
                    }
                });
            }

            if (i == 5) {
                // NAPRAWIONE: Data ważności - dowolny format MM/RR
                formFields[i].setDocument(new javax.swing.text.PlainDocument() {
                    @Override
                    public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                            throws javax.swing.text.BadLocationException {
                        if (str == null) return;
                        String currentText = getText(0, getLength());
                        String newText = currentText.substring(0, offs) + str + currentText.substring(offs);

                        // Ogranicz do 5 znaków (MM/RR)
                        if (newText.length() > 5) return;

                        // Automatyczne dodawanie "/" po 2 znakach
                        if (offs == 2 && !currentText.contains("/") && str.matches("[0-9]")) {
                            super.insertString(offs, "/" + str, a);
                        } else if (str.matches("[0-9/]")) {
                            super.insertString(offs, str, a);
                        }
                    }
                });

                // Placeholder
                formFields[i].setText("MM/RR");
                formFields[i].setForeground(Color.GRAY);
                formFields[i].addFocusListener(new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusGained(java.awt.event.FocusEvent e) {
                        if (formFields[fieldIndex].getText().equals("MM/RR")) {
                            formFields[fieldIndex].setText("");
                            formFields[fieldIndex].setForeground(Color.BLACK);
                        }
                    }
                    @Override
                    public void focusLost(java.awt.event.FocusEvent e) {
                        if (formFields[fieldIndex].getText().isEmpty()) {
                            formFields[fieldIndex].setText("MM/RR");
                            formFields[fieldIndex].setForeground(Color.GRAY);
                        }
                    }
                });
            }

            if (i == 6) {
                formFields[i].setDocument(new javax.swing.text.PlainDocument() {
                    @Override
                    public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                            throws javax.swing.text.BadLocationException {
                        if (str == null) return;
                        if ((getLength() + str.length()) <= 3 && str.matches("[0-9]*")) {
                            super.insertString(offs, str, a);
                        }
                    }
                });
            }

            formPanel.add(formFields[i], gbc);

            gbc.gridx = 1;
            gbc.gridy = i * 2 + 1;
            formPanel.add(validationLabels[i], gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = labels.length * 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setBackground(BG_LIGHT);

        JButton btnAnuluj = new JButton("ANULUJ PŁATNOŚĆ");
        btnAnuluj.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAnuluj.setBackground(LIGHT_BLUE);
        btnAnuluj.setForeground(Color.WHITE);
        btnAnuluj.setFocusPainted(false);
        btnAnuluj.setPreferredSize(new Dimension(220, 60));
        btnAnuluj.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(ramka,
                    "Czy na pewno chcesz anulować płatność? Wszystkie dane zostaną utracone.",
                    "Potwierdzenie anulowania",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                koszyk.clear();
                aktualizujKoszykButton();
                karty.show(panelGlowny, "START");
            }
        });

        JButton btnZatwierdz = new JButton("ZATWIERDŹ PŁATNOŚĆ");
        btnZatwierdz.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnZatwierdz.setBackground(PRIMARY_BLUE);
        btnZatwierdz.setForeground(Color.WHITE);
        btnZatwierdz.setFocusPainted(false);
        btnZatwierdz.setPreferredSize(new Dimension(300, 60));
        btnZatwierdz.addActionListener(e -> {
            if (validateAllFields()) {
                koszyk.clear();
                aktualizujKoszykButton();
                karty.show(panelGlowny, "POTWIERDZENIE");
            } else {
                JOptionPane.showMessageDialog(panel,
                        "Proszę poprawić wszystkie błędy w formularzu przed zatwierdzeniem.",
                        "Błędy w formularzu",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnAnuluj);
        buttonPanel.add(btnZatwierdz);

        formPanel.add(buttonPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static void validateField(int fieldIndex) {
        if (validationLabels == null || formFields == null ||
                fieldIndex >= validationLabels.length || fieldIndex >= formFields.length) {
            return;
        }

        String text = formFields[fieldIndex].getText().trim();
        String error = "";

        switch (fieldIndex) {
            case 0:
                if (text.isEmpty() || text.equals("MM/RR")) {
                    error = "Imię jest wymagane";
                } else if (!text.matches("^[A-Za-zĄąĆćĘęŁłŃńÓóŚśŹźŻż\\s-]+$")) {
                    error = "Imię może zawierać tylko litery";
                }
                break;

            case 1:
                if (text.isEmpty()) {
                    error = "Nazwisko jest wymagane";
                } else if (!text.matches("^[A-Za-zĄąĆćĘęŁłŃńÓóŚśŹźŻż\\s-]+$")) {
                    error = "Nazwisko może zawierać tylko litery";
                }
                break;

            case 2:
                if (text.isEmpty()) {
                    error = "Email jest wymagany";
                } else if (!text.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    error = "Niepoprawny format email";
                }
                break;

            case 3:
                if (text.isEmpty()) {
                    error = "Telefon jest wymagany";
                } else if (!text.matches("^[0-9]{9}$")) {
                    error = "Telefon musi mieć 9 cyfr";
                }
                break;

            case 4:
                if (text.isEmpty()) {
                    error = "Numer karty jest wymagany";
                } else if (!text.matches("^[0-9]{16}$")) {
                    error = "Numer karty musi mieć 16 cyfr";
                }
                break;

            case 5:
                if (text.isEmpty() || text.equals("MM/RR")) {
                    error = "Data ważności jest wymagana";
                } else if (!text.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) {
                    // NAPRAWIONE: Dowolny miesiąc 01-12 i dowolny rok 00-99
                    if (!text.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) {
                        error = "Format: MM/RR (np. 05/24)";
                    }
                }
                break;

            case 6:
                if (text.isEmpty()) {
                    error = "CVV jest wymagany";
                } else if (!text.matches("^[0-9]{3}$")) {
                    error = "CVV musi mieć 3 cyfry";
                }
                break;
        }

        validationLabels[fieldIndex].setText(error);
        if (!error.isEmpty()) {
            validationLabels[fieldIndex].setForeground(new Color(220, 53, 69));
        } else {
            validationLabels[fieldIndex].setText("✓ Poprawnie");
            validationLabels[fieldIndex].setForeground(new Color(40, 167, 69));
        }
    }

    private static boolean validateAllFields() {
        if (validationLabels == null || formFields == null) {
            return false;
        }

        boolean allValid = true;

        for (int i = 0; i < formFields.length; i++) {
            validateField(i);
            if (!validationLabels[i].getText().equals("✓ Poprawnie") &&
                    !validationLabels[i].getText().equals(" ")) {
                allValid = false;
            }
        }

        return allValid;
    }

    private static JPanel stworzPanelPotwierdzenia() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JButton btnPowrot = new JButton("<- POWROT");
        btnPowrot.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnPowrot.addActionListener(e -> karty.show(panelGlowny, "START"));

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nav.setBackground(BG_LIGHT);
        nav.add(btnPowrot);
        panel.add(nav, BorderLayout.NORTH);

        JLabel potwierdzenie = new JLabel("Zamówienie przyjęte!", SwingConstants.CENTER);
        potwierdzenie.setFont(new Font("Segoe UI", Font.BOLD, 40));
        potwierdzenie.setForeground(PRIMARY_BLUE);
        potwierdzenie.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        panel.add(potwierdzenie, BorderLayout.CENTER);

        JButton btnStronaGlowna = new JButton("POWRÓT DO STRONY GŁÓWNEJ");
        btnStronaGlowna.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnStronaGlowna.setBackground(PRIMARY_BLUE);
        btnStronaGlowna.setForeground(Color.WHITE);
        btnStronaGlowna.setFocusPainted(false);
        btnStronaGlowna.setPreferredSize(new Dimension(350, 70));
        btnStronaGlowna.addActionListener(e -> {
            koszyk.clear();
            aktualizujKoszykButton();
            karty.show(panelGlowny, "START");
        });

        JPanel przyciski = new JPanel(new FlowLayout(FlowLayout.CENTER));
        przyciski.setBackground(BG_LIGHT);
        przyciski.add(btnStronaGlowna);
        panel.add(przyciski, BorderLayout.SOUTH);

        return panel;
    }

    private static JPanel stworzPanelWszystkichWydarzen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(1200, 600));

        List<Wydarzenie> wydarzeniaDoWyswietlenia = pobierzWydarzeniaDoWyswietlenia();

        if (!wydarzeniaDoWyswietlenia.isEmpty()) {
            int rows = (int) Math.ceil(wydarzeniaDoWyswietlenia.size() / 3.0);
            JPanel gridWszystkie = new JPanel(new GridLayout(rows, 3, 20, 20));
            gridWszystkie.setBackground(BG_LIGHT);
            gridWszystkie.setOpaque(false);
            gridWszystkie.setMaximumSize(new Dimension(1100, rows * 300));

            for (Wydarzenie w : wydarzeniaDoWyswietlenia) {
                // Upewnij się, że używamy oryginalnego wydarzenia
                JPanel karta = stworzNowoczesnaKarteWydarzenia(w, false);
                gridWszystkie.add(karta);
            }

            int emptySlots = rows * 3 - wydarzeniaDoWyswietlenia.size();
            for (int i = 0; i < emptySlots; i++) {
                gridWszystkie.add(new JPanel());
            }

            JScrollPane scrollWszystkie = new JScrollPane(gridWszystkie);
            scrollWszystkie.setBorder(BorderFactory.createEmptyBorder());
            scrollWszystkie.getVerticalScrollBar().setUnitIncrement(20);
            scrollWszystkie.setPreferredSize(new Dimension(1150, 500));

            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.setBackground(BG_LIGHT);
            centerPanel.add(scrollWszystkie);

            panel.add(centerPanel, BorderLayout.CENTER);
        } else {
            JPanel brakPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            brakPanel.setBackground(BG_LIGHT);
            brakPanel.setOpaque(false);

            JLabel brakLabel = new JLabel("Brak zbliżających się wydarzeń");
            brakLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            brakLabel.setForeground(Color.GRAY);
            brakLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            brakPanel.add(brakLabel);

            panel.add(brakPanel, BorderLayout.CENTER);
        }

        return panel;
    }

    private static List<Wydarzenie> filtrujWydarzeniaPoNazwie(String tekst) {
        List<Wydarzenie> wynik = new ArrayList<>();
        System.out.println("\nWYSZUKIWANIE: '" + tekst + "'");
        System.out.println("Przeszukuję " + wszystkieWydarzenia.size() + " wydarzeń");

        for (Wydarzenie w : wszystkieWydarzenia) {
            if (w.getNazwa().toLowerCase().contains(tekst.toLowerCase())) {
                System.out.println("Znaleziono: " + w.getNazwa() +
                        " | Miasto: " + w.getMiejsce().getMiasto().getNazwa() +
                        " | Hash: " + w.hashCode());
                wynik.add(w);
            }
        }

        System.out.println("Znaleziono " + wynik.size() + " wyników");
        System.out.println("KONIEC WYSZUKIWANIA");
        return wynik;
    }

    private static void debugujListeWydarzen() {
        System.out.println("\n=== DEBUG LISTY WYDARZEŃ ===");
        System.out.println("Wszystkie wydarzenia: " + wszystkieWydarzenia.size());
        System.out.println("Aktualne miasto: " + aktualneMiasto);
        System.out.println("Aktualna kategoria: " + aktualnaKategoria);

        List<Wydarzenie> doWyswietlenia = pobierzWydarzeniaDoWyswietlenia();
        System.out.println("Do wyświetlenia: " + doWyswietlenia.size());

        for (Wydarzenie w : doWyswietlenia) {
            System.out.println("  - " + w.getNazwa() +
                    " (z oryginalnej listy: " + wszystkieWydarzenia.contains(w) + ")");
        }
        System.out.println("=== KONIEC DEBUG ===");
    }


}