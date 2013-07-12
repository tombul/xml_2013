WebDataBrowser
==============

XML Technologien Sommersemester 2013

Installation
-------------

Im Hauptverzeichnis liegt ein .apk, das auf Devices installiert werden kann (sofern Installationen aus unsicheren Quellen erlaubt sind). Alternativ lässt sich der Code natürlich auch direkt aus Eclipse in einem Emulator ausführen.

Third-Party-Components
-----------------------

* Zur Realisierung eines RDF-Tripel-Stores ist AndroJena angebunden.
* Zum Parsen von HTML ist JSoup angebunden.

Architektur
-----------

Das UI besteht aus drei Hauptkomponenten:
* Einem regulärem Webbrowser
* Einem Rich-Data-Browser, der auf einer Seite gefundene und direkt Verknüpfte RDF-Resourcen visualisiert
* Einem History-Browser der SPARQL-Queries auf dem RDF_Graph ermöglicht

Die Activities, die das UI implementieren, sind in fu.berlin.de.webdatabrowser.ui ui finden.

Eine Anfrage an den RichData-Browser bewirkt, das eine URL an den implementierten Parser übergeben wird. Dieser sucht einen optimalen SubParser (JSon, XML, Microdata oder LinkedData) aus, der RDF-Resourcen aus der URL extrahiert. Diese Resourcen werden im RDF-Store gespeichert und zur Visualisierung zurückgegeben.

Die Implementierung des RDF-Stores und des, für die Resourcen verwendeten, Vocabularys ist in fu.berlin.de.webdatabrowser.deeb zu finden.

Die Implementierung der Parser ist in fu.berlin.de.webdatabrowser.webdataparser zu finden.

Die History liefert bisher eher bescheidene Möglichkeiten Anfragen an den Store zu stellen - die Timestamps sind leider erst in letzter Sekunde fertig geworden und konnten mit SPARQL-Queries nicht mehr getestet werden.

