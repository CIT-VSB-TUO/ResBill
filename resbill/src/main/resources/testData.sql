-- SQL inserty, ktere do DB vlozi data pro vyvoj a testovani aplikace

INSERT INTO "tariff" VALUES (1, 0, 'Tarif1', true);

INSERT INTO "person" VALUES (1, 0, 'Obec1', 'Cast obce 1', 'CZE', '12', '23', 'Ulice 1', '78945', 'jjjj@ds.cz', 'Jméno 1', NULL, '456789123', 'Příjmení 1', NULL, NULL);

INSERT INTO "customer" VALUES (1, 0, NULL, 'Zákazník 1', NULL, 1);

INSERT INTO "contract" VALUES (1, 0, 0.00, 0, 'Kontrakt1', NULL, '2015-04-01', NULL, 1);

INSERT INTO "transaction" VALUES ('inv', 1, 0, 0.00, '2015-05-12', 12345, NULL, 1, 'Faktura 1', '2015-01-02', '2015-02-02', 1, 1, NULL, 1);

