public class Rezerwacja {
    private Wydarzenie wydarzenie;
    private String imieNazwisko;
    private String email;
    private int liczbaMiejsc;

    public Rezerwacja(Wydarzenie wydarzenie, String imieNazwisko, String email, int liczbaMiejsc) {
        this.wydarzenie = wydarzenie;
        this.imieNazwisko = imieNazwisko;
        this.email = email;
        this.liczbaMiejsc = liczbaMiejsc;
    }

    public Wydarzenie getWydarzenie() {
        return wydarzenie;
    }

    public int getLiczbaMiejsc() {
        return liczbaMiejsc;
    }

    // DODANY SETTER
    public void setLiczbaMiejsc(int liczbaMiejsc) {
        this.liczbaMiejsc = liczbaMiejsc;
    }

    public String getImieNazwisko() {
        return imieNazwisko;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Rezerwacja dla " + imieNazwisko + " na " + wydarzenie.getNazwa()
                + " (" + liczbaMiejsc + " miejsc)";
    }
}