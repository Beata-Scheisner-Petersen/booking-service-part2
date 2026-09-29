## Vad gör ändringen?

- Lägger till stöd för att skapa, visa, uppdatera och avboka hotellbokningar.
- Validerar bokningsdatum och antal gäster.
- Förbättrar felhanteringen med tydliga svar från API:et.

## Varför?

För att kunder ska kunna hantera sina bokningar och få tydlig återkoppling när något går fel. Valideringen förhindrar att ogiltiga bokningsuppgifter sparas i databasen.

## Hur har du testat?

- [ ] `mvn test` går igenom lokalt
- [ ] Testat att skapa, visa, uppdatera och avboka bokningar via curl/Postman
- [ ] Kontrollerat att ogiltiga uppgifter ger rätt HTTP-status och felmeddelande
- [ ] Kontrollerat att skyddade endpoints kräver en giltig JWT-token

## Checklista

- [ ] Inga hemligheter i koden
- [ ] Inga känsliga uppgifter loggas

Closes #ISSUENUMMER