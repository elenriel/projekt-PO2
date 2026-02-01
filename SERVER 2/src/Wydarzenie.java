public class Wydarzenie {
    private String nazwa;
    private String miasto;
    private double cena;
    private String kategoria;

    public Wydarzenie(String nazwa, String miasto, double cena, String kategoria) {
        this.nazwa = nazwa;
        this.miasto = miasto;
        this.cena = cena;
        this.kategoria = kategoria;
    }

    @Override
    public String toString() {
        return nazwa + ";" + miasto + ";" + cena + ";" + kategoria;
    }
}