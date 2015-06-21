# Version 0.1
Wenn ein Task abgeschlossen ist, einfach ein [x] setzen.

## Bernhard
- [x] Einbinden der Google-Maps
- [x] Nach einer abgeschlossenen Runde soll ersichtlich sein wo der Gegner seine Markierung gesetzt hat. Die Marker sollen unterschiedliche Farben aufweisen um einfacher feststellen zu können wer was getippt hat und wo das Ziel gewesen wäre.
- [x] Verwendung von Geolocation sollte beim Uploaden von Bildern möglich sein
- [x] Überarbeiten/Verfeinern der Oberfläche

## Thibault
- [x] Erstellen der Login- und Register-Seiten
- [x] Erstellen der Index-Seite
- [x] Erstellen des Menüs
- [x] Zentrieren von linksbündigen Formularen

## Philipp
- [x] Erweiterung des Menüs
- [x] Bilder Upload implementieren
- [x] Bilder der Sehenswürdigkeiten unskaliert überschreiben.
- [x] Entfernen der Skalierungsfunktion beim Upload von Bildern. Bilder sind aufgrund des PictureViewers nun responsive und es nicht mehr nötig sie zu skalieren.
- [x] Ändern der maximalen Dateigröße beim Upload von Bildern auf 5MB.
- [x] Umbau des PictureManagers mithilfe von Thumbnailinator um die Qualität unserer Thumbnails zu verbessern. Diese Bibliothek ist in das Projekt eingebunden und kann bereits verwendet werden. Info dazu hier: https://code.google.com/p/thumbnailator/

## Mario
- [x] Datenbank-Architektur aufsetzen
- [x] Falls möglich, Zählvariable der Bilder bei 0 statt 1 beginnen zu lassen um unnötige Nullpointer exceptions zu vermeiden. --> Leider nicht möglich, weil Datenbank IDs bei 1 starten.
- [x] Erstellen einer Funktion im DBManager.java die alle Bilder als List<Picture> zurückgibt.
- [x] Erstellen einer Funktion im DBManager.java die alle akzeptierten Bilder als List<Picture> zurückgibt.
- [x] Erstellen einer Funktion im DBManager.java die es ermöglicht ein bestimmtes Bild zu akzeptieren.
- [x] Überlade alle deine Funktionen die Listen zurückgeben mit 2 Parametern die es erlauben die Anzahl der zurückzugebenden Elemente zu limitieren (start, end).
- [x] Erstellen einer Funktion im DBManager.java zum Editieren und Deaktivieren von Bildern.
