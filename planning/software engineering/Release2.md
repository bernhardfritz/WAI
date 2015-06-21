# Version 1.0
Wenn ein Task abgeschlossen ist, einfach ein [x] setzen.

## Bernhard
- [x] Admin Oberfläche
  - [x] Erstellen einer Admin Oberfläche die nur durch den User admin aufgerufen werden kann. In dieser Oberfläche soll es für einen Admin ohne großen Aufwand möglich sein upgeloadete Bilder zu verifizieren und freizuschalten.
  - [x] Der Admin soll visuellen Zugriff auf die gesamte Datenbank haben
  - [x] Der Admin soll die Möglichkeit haben böse User zu bannen.
  - [x] Der Admin soll auch auf offene Reports zugreifen und reagieren können.
- [x] Erstellen einer Oberfläche zur Übersicht aller upgeloadeten Bilder eines Users. Hier soll auch ersichtlich sein ob das Bild bereits durch einen Admin akzeptiert worden ist oder nicht.
- [x] Überarbeite alle GUI Elemente die noch nicht ausgereift sind
  - [x] Überarbeite navbar
  - [x] Überarbeite index
  - [x] Überarbeite play_menu
  - [x] Überarbeite practise
  - [x] Überarbeite game
  - [x] Überarbeite login
  - [x] Überarbeite register
  - [x] Verfeinere result
- [x] Implementiere UnitTests
  - [x] UnitTest für die Distanzberechnung (Innsbruck->Wien ca. 500km)

## Thibault
- [x] Buttons bei index, play_menu, new_game_menu sollten so breit wie der Bildschirm ist sein.
- [x] Einfügen von Back Buttons bei play_menu und new_game_menu anstatt dem Logo.
- [x] Refactoring von search_friend und send_email
  - [x] Implementieren von add_friend bei dem man einen Freund der bereits auf WAI registriert ist zu seinen Freunden hinzufügen kann. Dafür wird ein Suchfeld benötigt das den eingegeben Usernamen über AJAX mit der Datenbank abgleicht. Eine Liste der gefundenen Treffer soll auf der gleichen Seite dargestellt werden können.
  - [x] Implementieren von invite_friend bei dem man einen Freund über dessen Email-Adresse ermuntern kann auch WAI zu spielen. Der Inhalt der Email soll nicht verändert werden können (hardcoded)
- [x] Einbauen einer Möglichkeit zum Ändern des Passworts von der Weboberfläche. ("Forgot password?" bei Login und "Change password" im Sidebar Menü).
- [x] Einfügen einer friends.scala.html, die alle Freunde anzeigt.
- [x] Bei der Suche soll es die Möglichkeit geben eine neues Spiel mit einem User zu starten oder einen User als Freund hinzuzufügen.
- [x] Überarbeite send_email. Lass den grauen Kasten sowie die Überschrift im grauen Kasten weg. Ändere die Überschriften in der Navbar dementsprechend. Bleib konsistent.
- [x] Überarbeite friends. Die Überschrift in der Navbar stimmt nicht. Das Design kann noch verbessert werden. Es ist noch nicht intuitiv dass mit dem blauen Button ein neues Spiel begonnen werden kann. Das "Play" schwebt in der Luft. Mach vielleicht so ähnlich wie bei search_user einen Kasten in dem der Name des Freundes und der Button zum Spielen drin ist. Schreib in den Button einfach "Play" hinein statt dem Bild.
- [x] Achte auf richtige Verlinkung
  - [x] Nachdem man einen Freund bei search_user hinzufügt, sollte auf friends redirected werden
  - [x] Nachdem ein Spiel bei search_user begonnen wurde, sollte auf play_menu redirected werden
- [x] Füge deine wichtigsten Funktionen in die Sidebar ein
- [x] Überarbeite Account
  - [x] Bei Account ein bisschen bei beiden Seiten des grauen Kastens einrücken (probier container)
  - [x] Username und Email linksbündig machen
  - [x] Der Inhalt des Modal sollte auch linksbündig sein
- [x] Überarbeite send_email. Schaut im Moment ein wenig blank aus. Machs vielleicht so ähnlich wie Forgot Password. Von mir aus auch mit grauem Kasten.
- [x] Funktioniert das Rücksetzen des Passworts bereits?

## Philipp
- [x] Report Seite
  - [x] Hinzufügen eines Report Buttons der auf eine report Seite verweist. Dabei solltest du die PictureID als Parameter verwenden. PictureID ist auf result bereits verfügbar und somit  kann bereits beim Laden der Seite der report Button mit dem richtigen Link belegt werden.
  - [x] Von der Report Seite aus kann man falsch gesetzte Locations von bereits freigegebenen Sehenswürdigkeiten direkt dem Administrator melden. Dabei soll es auch möglich sein eine Begründung anzugeben. Die Daten dieses Formulars sollen dann mithilfe einer Post-Request in Java griffbereit sein. Test mit System.out.println
- [x] Erstellen einer Oberfläche zur Übersicht offener Spiele.
- [x] Verbinden der Game Oberfläche mit der aktuellen aus der Datenbank abgefragten Spielrunde des jeweiligen Users.
- [x] Überarbeite/Vereinfache dein resultGame damit der richtige User rot bzw. grün dargestellt wird
- [x] Im Moment sind im Result beide User grün/rot. Es sollte aber der Gewinner grün und der Verlierer rot sein. Der Name des Spielers sollte nicht ausgeschrieben werden sondern einfach mit "You" ersetzt werden.
- [x] Implementiere UnitTests
  - [x] UnitTest für deinen PictureManager. Schau ob das Bild nach der Skalierung wirklich die richtige Größe hat.

## Mario
- [x] Erstellen der Datenbanklogik für das Erstellen eines Spiels (Game.java). Dabei sollen 3 Runden (Round.java) beim Erstellen des Spiels mit einem zufälligen Bild angelegt werden. Das Spiel beinhaltet die konkurierenden Spieler (User.java), sowie die jeweiligen Runden (Round.java). Es soll zu jeder Zeit ersichtlich sein welche Runde schon gespielt bzw. ob das Spiel bereits fertig ist.
- [x] Erstellen einer Funktion die offene Spiele eines Users in einer List<Game> zurückgibt.
- [x] Erstellen einer Relation Freund (Friend.java), die zwei User (User.java) miteinander verknüpft.
- [x] Erstellen einer Funktion die alle Freunde eines gewissen Users als Liste zurückgibt.
- [x] Einbauen der Funktionalität zum ändern eines Passworts.
- [x] Einbauen einer Tabelle Report. Das Report Objekt beinhaltet ein bestehendes Bild oder die id eines bestehenden Bilds, neue Lat/Lng-Werte, Erstellungsdatum, User Objekt oder username des Nutzers der den Report verfasst, und ein boolean ob der Report behandelt wurde oder nicht.
- [x] Einbauen einer Tabelle Tokens. 1 Token (String) pro User, 1 tag gültig (Datum?). Es soll möglich sein nur mit e-mail Adresse den Token und Datum zu setzen und zu bekommen.
- [x] LatLng Objekte in Runde einbauen damit wir sie in der result_map verwenden können
- [x] Es sollte nicht möglich sein mit sich selbst ein Spiel beginnen zu können. Bei search_user beispielsweise kann man sich selbst finden und mit sich selbst ein Spiel beginnen. Sich mit sich selbst zu befreunden geht zum Glück nicht :p
- [x] Verwende prettifyDistance damit Philipp seine Distanzen schön als km darstellen kann. Zur Berechnung sollte aber der exakte Wert verwendet werden.
- [x] Stelle sicher, dass admin-only Webseiten auch wirklich nur durch den Admin aufgerufen werden können
- [x] Habe Bug gefunden beim game. Probier aus dass du den counter bis auf 5 Sekunden runterlaufen lässt dann lädst du die Seite neu und gibst deinen Tipp bei z.B. 50 Sekunden ab. D.h. du hättest mehr als 60 Sekunden Zeit. Weiß nicht ob man das fixen kann, wenn es zu zeitaufwendig ist einfach weglassen.
