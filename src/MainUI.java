import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainUI {
    private JPanel panel1;
    private JButton bankkontoErstellenButton;
    private JButton einzahlenButton;
    private JButton auszahlenButton;
    private JButton kontoauszugButton;
    private JButton kontoLöschenButton;
    private JButton überweisenButton;

    // Datenhaltung
    private final List<Konto> konten = new ArrayList<>();

    public MainUI() {
        bankkontoErstellenButton.addActionListener(e -> kontoErstellenDialog());
        einzahlenButton.addActionListener(e -> {
            int i = kontoAuswahl("Auf welches Konto einzahlen?");
            if (i < 0) return;
            Float betrag = betragDialog("Betrag einzahlen:");
            if (betrag == null || betrag <= 0) return;
            konten.get(i).Einzahlung(betrag);
            info("Neuer Kontostand: " + konten.get(i).getKontostand());
        });
        auszahlenButton.addActionListener(e -> {
            int i = kontoAuswahl("Von welchem Konto auszahlen?");
            if (i < 0) return;
            Float betrag = betragDialog("Betrag auszahlen:");
            if (betrag == null || betrag <= 0) return;
            konten.get(i).Auszahlung(betrag);
            info("Neuer Kontostand: " + konten.get(i).getKontostand());
        });
        kontoauszugButton.addActionListener(e -> {
            int i = kontoAuswahl("Kontoauszug anzeigen für:");
            if (i < 0) return;
            JOptionPane.showMessageDialog(panel1, konten.get(i).toString(),
                    "Kontoauszug", JOptionPane.INFORMATION_MESSAGE);
        });
        kontoLöschenButton.addActionListener(e -> {
            int i = kontoAuswahl("Welches Konto löschen?");
            if (i < 0) return;
            Konto k = konten.remove(i);
            info("Konto gelöscht: " + k.getKontoInhaber() + " • " + k.getKontonummer());
        });
        überweisenButton.addActionListener(e -> {
            if (konten.size() < 2) { warn("Mindestens zwei Konten nötig."); return; }
            int von = kontoAuswahl("Von welchem Konto überweisen?");
            if (von < 0) return;
            int nach = kontoAuswahl("Auf welches Konto überweisen?");
            if (nach < 0 || nach == von) { warn("Zielkonto ungültig."); return; }
            Float betrag = betragDialog("Betrag überweisen:");
            if (betrag == null || betrag <= 0) return;

            Konto quelle = konten.get(von);
            Konto ziel = konten.get(nach);
            float altQuelle = quelle.getKontostand();
            quelle.Auszahlung(betrag);
            if (quelle.getKontostand() == altQuelle && !(quelle instanceof Kreditkonto)) {
                warn("Abbuchung nicht erfolgt (Limit/Deckung?).");
                return;
            }
            ziel.Einzahlung(betrag);
            info("Überweisung OK.\nQuelle neu: " + quelle.getKontostand()
                    + "\nZiel neu: " + ziel.getKontostand());
        });
    }

    private void kontoErstellenDialog() {
        String[] typen = {"Girokonto", "Sparkonto", "Kreditkonto"};
        JComboBox<String> typBox = new JComboBox<>(typen);
        JTextField inh = new JTextField();
        JTextField knr = new JTextField();
        JTextField blz = new JTextField();
        JTextField limit = new JTextField();

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("Kontotyp:")); p.add(typBox);
        p.add(new JLabel("Inhaber:")); p.add(inh);
        p.add(new JLabel("Kontonummer:")); p.add(knr);
        p.add(new JLabel("Bankleitzahl:")); p.add(blz);
        p.add(new JLabel("Dispo-Limit (nur Giro):")); p.add(limit);

        typBox.addActionListener(e -> {
            boolean giro = typBox.getSelectedIndex() == 0;
            limit.setEnabled(giro); limit.setEditable(giro);
        });
        typBox.setSelectedIndex(0);

        int res = JOptionPane.showConfirmDialog(panel1, p, "Konto erstellen",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String inhaber = inh.getText().trim();
        String kontonr = knr.getText().trim();
        if (inhaber.isEmpty() || kontonr.isEmpty()) { warn("Bitte alle Pflichtfelder ausfüllen."); return; }

        int bankleitzahl;
        try { bankleitzahl = Integer.parseInt(blz.getText().trim()); }
        catch (Exception ex) { warn("BLZ muss eine Zahl sein."); return; }

        Konto neu;
        switch ((String) typBox.getSelectedItem()) {
            case "Girokonto":
                float dispo;
                try { dispo = Float.parseFloat(limit.getText().trim().replace(',', '.')); }
                catch (Exception ex) { warn("Bitte gültiges Dispo-Limit eingeben."); return; }
                neu = new Girokonto(inhaber, kontonr, bankleitzahl, dispo, "Girokonto");
                break;
            case "Sparkonto":
                neu = new Sparkonto(inhaber, kontonr, bankleitzahl, "Sparkonto");
                break;
            default:
                neu = new Kreditkonto(inhaber, kontonr, bankleitzahl, "Kreditkonto");
        }
        konten.add(neu);
        info("Konto angelegt: " + inhaber + " • " + kontonr);
    }

    private int kontoAuswahl(String titel) {
        if (konten.isEmpty()) { warn("Keine Konten vorhanden."); return -1; }
        String[] anzeige = new String[konten.size()];
        for (int i = 0; i < konten.size(); i++) {
            Konto k = konten.get(i);
            anzeige[i] = i + ": " + k.getKontoInhaber() + " • " + k.getKontonummer()
                    + " • " + k.getClass().getSimpleName();
        }
        String s = (String) JOptionPane.showInputDialog(panel1, titel, "Konto wählen",
                JOptionPane.PLAIN_MESSAGE, null, anzeige, anzeige[0]);
        if (s == null) return -1;
        try { return Integer.parseInt(s.substring(0, s.indexOf(':'))); }
        catch (Exception e) { return -1; }
    }

    private Float betragDialog(String titel) {
        String s = JOptionPane.showInputDialog(panel1, titel, "0.00");
        if (s == null) return null;
        try { return Float.parseFloat(s.replace(',', '.')); }
        catch (NumberFormatException e) { warn("Ungültiger Betrag."); return null; }
    }

    private void info(String msg) { JOptionPane.showMessageDialog(panel1, msg, "Info", JOptionPane.INFORMATION_MESSAGE); }
    private void warn(String msg) { JOptionPane.showMessageDialog(panel1, msg, "Hinweis", JOptionPane.WARNING_MESSAGE); }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Bank Konto");
            f.setContentPane(new MainUI().panel1);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
