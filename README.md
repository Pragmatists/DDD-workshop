# Domain Driven Design

## Struktura aplikacji

### Standardowa struktura (z anemicznym modelem)
Może trochę dziwić w kontekście "standardowej" struktury projektu. Pewnie przyzwyczajeni jesteście do struktury:
* controller - odpowiedzialny za rejestrownie endpointów, mapowanie jsonów do obiektów oraz wywołanie operacji na serwisie, odebranie
* service - cała logika aplikacji mieści się tutaj. Ładujemy model z bazy danych, często translacja na obiekty, na których operujemy w serwisie. Następnie wykonanie operacji bizneswej, ewentualna translacja na model do zapisu, i persystencja obiektu.
* model - w zasadzie anemiczna encja, nie posiada żadnych biznesowych metod
* repository - ładowanie i zapisywanie w bazie danych

### Struktura w Domain Driven design
W zasadzie składa się z 3 głównych "warstw":
* application - instrumentacja domeny - załadowanie obiektów, wykonanie operacji biznesowych oraz zapisanie zmienionych obiektów
* domain - zawiera cała logika znajduje się tutaj, encje, agregaty i value objecty
* infrastructure - adaptery do portów wystawionych w domenie, persystencja obiektów domenowych, implementacja usług, używanych przez domenę

### Umiejscowienie komponentów w poszczególnych miejscach  