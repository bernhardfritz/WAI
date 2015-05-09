## Bernhard
- [ ] Admin Oberfläche
  - [ ] Erstellen einer Admin Oberfläche die nur durch den User admin aufgerufen werden kann. In dieser Oberfläche soll es für einen Admin ohne großen Aufwand möglich sein upgeloadete Bilder zu verifizieren und freizuschalten.
  - [ ] Der Admin soll visuellen Zugriff auf die gesamte Datenbank haben und fal
  - [ ] Der Admin soll die Möglichkeit haben böse User zu bannen.
  - [ ] Der Admin soll auch auf offene Reports zugreifen und reagieren können.
- [ ] Erstellen einer Oberfläche zur Übersicht offener Spiele.
- [ ] Verbinden der Game Oberfläche mit der aktuellen aus der Datenbank abgefragten Spielrunde des jeweiligen Users.
- [ ] Einbauen eines Benachrichtigungssystems, das es ermöglicht dem Spieler mitzuteilen, dass ein anderer Spieler ihn zu einem Spiel herausgefordert hat, oder dass ein Spiel abgeschlossen wurde oder eine neue Runde beginnt.
- [ ] Nach einer abgeschlossenen Runde soll ersichtlich sein wo der Gegner seine Markierung gesetzt hat. Die Marker sollen unterschiedliche Farben aufweisen um einfacher feststellen zu können wer was getippt hat und wo das Ziel gewesen wäre.
- [ ] Erstellen einer Oberfläche zur Übersicht aller upgeloadeten Bilder eines Users. Hier soll auch ersichtlich sein ob das Bild bereits durch einen Admin akzeptiert worden ist oder nicht.
- [ ] Erstellen einer Oberfläche zur Übersicht von Statistiken/Achivements.


## Thibault
- [ ] Buttons bei index, play_menu, new_game_menu sollten so breit wie der Bildschirm ist sein.
- [ ] Einfügen von Back Buttons bei play_menu und new_game_menu anstatt dem Logo.
- [ ] Refactoring von search_friend und send_email
  - [ ] Implementieren von add_friend bei dem man einen Freund der bereits auf WAI registriert ist zu seinen Freunden hinzufügen kann. Dafür wird ein Suchfeld benötigt das den eingegeben Usernamen über AJAX mit der Datenbank abgleicht. Eine Liste der gefundenen Treffer soll auf der gleichen Seite dargestellt werden können.
  - [ ] Implementieren von invite_friend bei dem man einen Freund über dessen Email-Adresse ermuntern kann auch WAI zu spielen. Der Inhalt der Email soll nicht verändert werden können (hardcoded)
- [ ] Zentrieren von momentan linksbündigen Formularen
- [ ] Einbauen einer Möglichkeit zum Ändern des Passworts von der Weboberfläche. ("Forgot password?" bei Login und "Change password" im Sidebar Menü).

## Philipp
- [ ] Bilder der Sehenswürdigkeiten unskaliert überschreiben.
- [ ] Entfernen der Skalierungsfunktion beim Upload von Bildern. Bilder sind aufgrund des PictureViewers nun responsive und es nicht mehr nötig sie zu skalieren.
- [ ] Ändern der maximalen Dateigröße beim Upload von Bildern auf 5MB.
- [ ] Umbau des PictureManagers mithilfe von Thumbnailinator um die Qualität unserer Thumbnails zu verbessern. Diese Bibliothek ist in das Projekt eingebunden und kann bereits verwendet werden. Info dazu hier: https://code.google.com/p/thumbnailator/
- [ ] Report Seite
  - [ ] Hinzufügen eines Report Buttons der auf eine report Seite verweist. Dabei solltest du die PictureID als Parameter verwenden. PictureID ist auf result bereits verfügbar und somit  kann bereits beim Laden der Seite der report Button mit dem richtigen Link belegt werden.
  - [ ] Von der Report Seite aus kann man falsch gesetzte Locations von bereits freigegebenen Sehenswürdigkeiten direkt dem Administrator melden. Dabei soll es auch möglich sein eine Begründung anzugeben. Die Daten dieses Formulars sollen dann mithilfe einer Post-Request in Java griffbereit sein. Test mit System.out.println

## Mario
- [ ] Erstellen der Datenbanklogik für das Erstellen eines Spiels (Game.java). Dabei sollen 3 Runden (Round.java) beim Erstellen des Spiels mit einem zufälligen Bild angelegt werden. Das Spiel beinhaltet die konkurierenden Spieler (User.java), sowie die jeweiligen Runden (Round.java). Es soll zu jeder Zeit ersichtlich sein welche Runde schon gespielt bzw. ob das Spiel bereits fertig ist.
- [ ] Erstellen einer Funktion die offene Spiele eines Users in einer List<Game> zurückgibt.
- [ ] Falls möglich, Zählvariable der Bilder bei 0 statt 1 beginnen zu lassen um unnötige Nullpointer exceptions zu vermeiden.
- [ ] Erstellen einer Funktion im DBManager.java die alle Bilder als List<Picture> zurückgibt.
- [ ] Erstellen einer Funktion im DBManager.java die alle aktiven Bilder als List<Picture> zurückgibt.
- [ ] Erstellen einer Funktion im DBManager.java die es ermöglicht ein bestimmtes Bild zu aktivieren.
- [ ] Erstellen einer Relation Freund (Friend.java), die zwei User (User.java) miteinander verknüpft.
- [ ] Erstellen einer Funktion die alle Freunde eines gewissen Users als Liste zurückgibt.
- [ ] Überlade alle deine Funktionen die Listen zurückgeben mit 2 Parametern die es erlauben die Anzahl der zurückzugebenden Elemente zu limitieren (start, end).
- [ ] Einbauen der Funktionalität zum ändern eines Passworts.
- [ ] Überleg dir bereits wie wir Punkte und Statistiken in dein Datenbankmodell einfügen können. Es möglich sein zu beliebigen Zeitpunkten neue Achivements hinzuzufügen.
