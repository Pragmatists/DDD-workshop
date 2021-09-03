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

### Opis domeny

Domena zarządzania użytkownikami. Dodawanie użytkownika, walidowanie czy danych użytkownik już istnieje,
walidowanie hasła. Aktywowanie użytkownika, obsługa wygasania hasła.

### Umiejscowienie komponentów w poszczególnych miejscach  

* application - controller + instrumentacja domeny
* user, idgenerator interface, implementacja w infrastrukturze

## Encje

Encja to jeden z podstawowych budulców domenowych w DDD. To obiekt, który można jednoznczanie zidentyfikować po id.
Obiekt ten może zmieniać swój stan w trakcie życia systemu. Dba też o swoje niezmienniki (invariants), 
zapewnia, że jest zawsze w poprawnym, spójnym stanie.

Przykładem encji jest User.

## Generowanie idków 

Możliwe strategie generowania idków (przez application service albo przez repository). 

### Kto generuje idki  

Persistence:
* zapewniona unikalność  
* korzystamy często z gotowych mechanizmów
* czas generowania 

Application:
* szybkość
* czasami przez wymagania nie jest możliwy do zaimplementowania (idki numeryczne unikalne)

### Czas generowania idków

plusy:
* dostępność od raz - struktury hashujące

##  