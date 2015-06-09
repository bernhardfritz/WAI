# ENDSPRINT
Wenn ein Task abgeschlossen ist, einfach in WAI/planning/endsprint/README.md ein [x] setzen.

## Bernhard
- [ ] Überarbeite alle GUI Elemente die noch nicht ausgereift sind
  - [ ] play_menu sieht noch nicht fertig aus
  - [ ] result sieht noch nicht fertig aus
  - [ ] result_map muss noch fertig implementiert werden (-> LatLng Objekte)
- [ ] Implementiere UnitTests
  - [ ] UnitTest für die Distanzberechnung (Innsbruck->Wien ca. 500km)

## Thibault
- [ ] Überarbeite send_email. Lass den grauen Kasten sowie die Überschrift im grauen Kasten weg. Ändere die Überschriften in der Navbar dementsprechend. Bleib konsistent.
- [ ] Überarbeite friends. Die Überschrift in der Navbar stimmt nicht. Das Design kann noch verbessert werden. Es ist noch nicht intuitiv dass mit dem blauen Button ein neues Spiel begonnen werden kann. Das "Play" schwebt in der Luft. Mach vielleicht so ähnlich wie bei search_user einen Kasten in dem der Name des Freundes und der Button zum Spielen drin ist. Schreib in den Button einfach "Play" hinein statt dem Bild.
- [ ] Achte auf richtige Verlinkung
  - [ ] Nachdem man einen Freund bei search_user hinzufügt, sollte auf friends redirected werden
  - [ ] Nachdem ein Spiel bei search_user begonnen wurde, sollte auf play_menu redirected werden
- [ ] Füge deine wichtigsten Funktionen in die Sidebar ein

## Philipp
- [ ] Überarbeite/Vereinfache dein resultGame damit der richtige User rot bzw. grün dargestellt wird
- [ ] Implementiere UnitTests
  - [ ] UnitTest für deinen PictureManager. Schau ob das Bild nach der Skalierung wirklich die richtige Größe hat.

## Mario
- [ ] LatLng Objekte in Runde einbauen damit wir sie in der result_map verwenden können
- [ ] Es sollte nicht möglich sein mit sich selbst ein Spiel beginnen zu können. Bei search_user beispielsweise kann man sich selbst finden und mit sich selbst ein Spiel beginnen. Sich mit sich selbst zu befreunden geht zum Glück nicht :p
- [ ] Verwende prettifyDistance damit Philipp seine Distanzen schön als km darstellen kann. Zur Berechnung sollte aber der exakte Wert verwendet werden.
- [ ] Stelle sicher, dass admin-only Webseiten auch wirklich nur durch den Admin aufgerufen werden können
- [ ] Implementiere UnitTests
  - [ ] Lass dir einige (min. 3) Testfälle für die Datenbank einfallen. Können auch Pseudo-Tests sein.
