public class Miasto {
    private String nazwa;

    public Miasto(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() { return nazwa; }

    @Override
    public String toString() { return nazwa; }
}