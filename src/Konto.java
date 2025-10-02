public class Konto {
    private String kontoInhaber;
    private String kontonummer;
    private int bankleitzahl;
    private float kontostand;
    private float kontofuhrungsgebuhren;
    private float auszahlung;
    private float einzahlung;
    private String kontoArt;

    Konto(String kontoInhaber, String kontonummer, int bankleitzahl, String kontoArt) {
        this.kontoInhaber = kontoInhaber;
        this.kontonummer = kontonummer;
        this.bankleitzahl = bankleitzahl;
        this.kontonummer = kontonummer;
        this.kontoArt = kontoArt;
    }

    public void Einzahlung(float amount) {
        kontostand += amount;
    }

    public void Auszahlung(float amount) {
        if (kontostand > amount) {
            kontostand -= amount;
        } else {
            System.out.println("Dein Konto hat zu wenig Guthaben");
        }
    }

    public void setKontostand(float kontostand) {
        this.kontostand = kontostand;
    }

    public float getKontostand() {
        return kontostand;
    }

    public String getKontonummer() {
        return kontonummer;
    }

    public float getBankleitzahl() {
        return bankleitzahl;
    }

    public String getKontoInhaber() {
        return kontoInhaber;
    }

    public void setKontoInhaber(String kontoInhaber) {
        this.kontoInhaber = kontoInhaber;
    }



    @Override
    public String toString() {
        return ("-------Kontoauszug-------" + "\n" +
                "Kontoinhaber: " + kontoInhaber + "\n" +
                "Kontoart: " + kontoArt + "\n" +
                "Kontonummer: " + kontonummer + "\n" +
                "Bankleitzahl: " + bankleitzahl + "\n" +
                "Kontostand: " + kontostand);
    }
}