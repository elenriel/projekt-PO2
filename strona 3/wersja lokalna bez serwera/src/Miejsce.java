/* public class Miejsce {
    private int numerMiejsca;
    private boolean zarezerwowany;

    public Miejsce(int numerMiejsca) {
        this.numerMiejsca = numerMiejsca;
        this.zarezerwowany = false;
    }

    public boolean czyZarezerwany() {
        return this.zarezerwowany;
    }

    public void zarezerwuj() {
        zarezerwowany = true;
    }

    @Override
    public String toString() {
        return "Miejsce nr " + numerMiejsca + (zarezerwowany ? "(zajęte)" : "(wolne)");
    }
}
*/

public class Miejsce {
    private String nazwa;
    private Miasto miasto;
    private String adres;

    public Miejsce(String nazwa, Miasto miasto, String adres) {
        this.nazwa = nazwa;
        this.miasto = miasto;
        this.adres = adres;
    }

    public String getNazwa() { return nazwa; }
    public Miasto getMiasto() { return miasto; }
    public String getAdres() { return adres; }

    @Override
    public String toString() {
        return nazwa + " (" + miasto.getNazwa() + ")";
    }
}
