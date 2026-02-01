import java.time.LocalDateTime;

public class Wydarzenie {
    private static int nextId = 1;
    private final int id;
    private String nazwa;
    private Miejsce miejsce;
    private LocalDateTime dataGodzina;
    private double cenaBiletu;
    private int liczbaMiejsc;
    private String kategoria;

    public Wydarzenie(String nazwa, Miejsce miejsce, LocalDateTime dataGodzina,
                      double cenaBiletu, int liczbaMiejsc, String kategoria) {
        this.id = nextId++;
        this.nazwa = nazwa;
        this.miejsce = miejsce;
        this.dataGodzina = dataGodzina;
        this.cenaBiletu = cenaBiletu;
        this.liczbaMiejsc = liczbaMiejsc;
        this.kategoria = kategoria;
    }

    public int getId() {
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public Miejsce getMiejsce() {
        return miejsce;
    }

    public LocalDateTime getDataGodzina() {
        return dataGodzina;
    }

    public double getCenaBiletu() {
        return cenaBiletu;
    }

    public int getLiczbaMiejsc() {
        return liczbaMiejsc;
    }

    public String getKategoria() {
        return kategoria;
    }

    @Override
    public String toString() {
        return nazwa + " | " + dataGodzina.toLocalDate() + " " + dataGodzina.toLocalTime()
                + " | " + miejsce + " | " + cenaBiletu + " zł | " + kategoria;
    }
}