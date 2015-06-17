# ENDSPRINT
Wenn ein Task abgeschlossen ist, einfach in WAI/planning/endsprint/README.md ein [x] setzen.

## Bernhard
- [ ] Überarbeite alle GUI Elemente die noch nicht ausgereift sind
  - [x] Überarbeite navbar
  - [x] Überarbeite index
  - [x] Überarbeite play_menu
  - [x] Überarbeite practise
  - [x] Überarbeite game
  - [x] Überarbeite login
  - [x] Überarbeite register
  - [x] Verfeinere result
  - [ ] Stelle result_map fertig (-> LatLng Objekte)
- [x] Verwendung von Geolocation sollte beim Uploaden von Bildern möglich sein
- [x] Implementiere UnitTests
  - [x] UnitTest für die Distanzberechnung (Innsbruck->Wien ca. 500km)

## Thibault
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
- [x] Überarbeite/Vereinfache dein resultGame damit der richtige User rot bzw. grün dargestellt wird
- [x] Im Moment sind im Result beide User grün/rot. Es sollte aber der Gewinner grün und der Verlierer rot sein. Der Name des Spielers sollte nicht ausgeschrieben werden sondern einfach mit "You" ersetzt werden.
- [x] Implementiere UnitTests
  - [x] UnitTest für deinen PictureManager. Schau ob das Bild nach der Skalierung wirklich die richtige Größe hat.

## Mario
- [x] LatLng Objekte in Runde einbauen damit wir sie in der result_map verwenden können
- [x] Es sollte nicht möglich sein mit sich selbst ein Spiel beginnen zu können. Bei search_user beispielsweise kann man sich selbst finden und mit sich selbst ein Spiel beginnen. Sich mit sich selbst zu befreunden geht zum Glück nicht :p
- [x] Verwende prettifyDistance damit Philipp seine Distanzen schön als km darstellen kann. Zur Berechnung sollte aber der exakte Wert verwendet werden.
- [x] Stelle sicher, dass admin-only Webseiten auch wirklich nur durch den Admin aufgerufen werden können
- [ ] Implementiere UnitTests
  - [ ] Lass dir einige (min. 3) Testfälle für die Datenbank einfallen. Können auch Pseudo-Tests sein.
- [x] Habe Bug gefunden beim game. Probier aus dass du den counter bis auf 5 Sekunden runterlaufen lässt dann lädst du die Seite neu und gibst deinen Tipp bei z.B. 50 Sekunden ab. D.h. du hättest mehr als 60 Sekunden Zeit. Weiß nicht ob man das fixen kann, wenn es zu zeitaufwendig ist einfach weglassen.
