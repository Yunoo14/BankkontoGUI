2aAPC Fabian Häusl
Bankkonto
Mein Programm ist eine kleine Bank-Konsolenanwendung:

Ich habe eine Basisklasse Konto, die Inhaber, Kontonummer, Bankleitzahl, Kontostand und Kontoart speichert. Dort gibt es Methoden für Ein- und Auszahlungen sowie einen Kontoauszug.

Daraus leite ich drei Kontotypen ab:

Girokonto mit Dispo-Limit,

Sparkonto ohne Besonderheiten,

Kreditkonto, das negative Stände erlaubt und nur bis 0 zurückgezahlt werden darf.

Im Hauptprogramm verwalte ich die Konten in einer ArrayList und biete über ein Menü folgende Funktionen: Konto anlegen, Geld ein- und auszahlen, Kontodaten anzeigen, Überweisen, Konto löschen und das Programm beenden.

Stärken meines Programms sind die Nutzung von Vererbung, Polymorphie und Kapselung.
Probleme gibt es bei der Überweisung (Zielkonto wird falsch belastet), bei der Abfrage (falsches | statt &&), bei der Typwahl (getBankleitzahl gibt float zurück) sowie bei ungenutzten Feldern und fehlender Eingabeprüfung.
<img src="C:\Users\FabianHäusl\Desktop\Eibiswald\ITL1\BankKonto\src\img\Screenshot 2025-10-02 194503.png">
<img src= "C:\Users\FabianHäusl\Desktop\Eibiswald\ITL1\BankKonto\src\img\Screenshot 2025-10-02 194530.png">
