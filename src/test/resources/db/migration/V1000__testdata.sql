insert into avtale (ID, OPPRETTET_TIDSPUNKT, VERSJON, DELTAKER_FORNAVN, DELTAKER_ETTERNAVN, DELTAKER_FNR, BEDRIFT_NAVN,
                    BEDRIFT_NR, ARBEIDSGIVER_FNR, ARBEIDSGIVER_FORNAVN, ARBEIDSGIVER_ETTERNAVN, ARBEIDSGIVER_TLF,
                    VEILEDER_NAV_IDENT, VEILEDER_FORNAVN, VEILEDER_ETTERNAVN, VEILEDER_TLF, OPPFOLGING, TILRETTELEGGING,
                    START_DATO, ARBEIDSTRENING_LENGDE, ARBEIDSTRENING_STILLINGPROSENT, GODKJENT_AV_DELTAKER,
                    GODKJENT_AV_ARBEIDSGIVER, GODKJENT_AV_VEILEDER, JOURNALPOST_ID)
values ('6ae3be81-abcd-477e-a8f3-4a5eb5fe91e3', current_timestamp, 7, 'Didrik', 'Deltaker', '01093434109',
        'Fiskebåten', '111111111', '29118923330', 'Filip', 'Fisker', '22334455', 'X123456', 'Vera', 'Veileder',
        '33445566', 'Ingen', 'Ingen', '2019-03-25', 2, 100, TRUE, TRUE, TRUE, '123456789');

insert into maal(ID, OPPRETTET_TIDSPUNKT, KATEGORI, BESKRIVELSE, AVTALE)
values ('d16350f6-27ea-49b3-9fbc-25fcee0dd080', current_timestamp, 'Arbeidserfaring', 'Sette sjøbein.',
        '6ae3be81-abcd-477e-a8f3-4a5eb5fe91e3');

insert into oppgave (ID, OPPRETTET_TIDSPUNKT, TITTEL, BESKRIVELSE, OPPLAERING, AVTALE)
values ('76a83e6d-e668-4073-a1ac-88885ae4df90', current_timestamp, 'Fiskemann', 'Trekke opp garn. Pilking.', 'Opplæring underveis.',
        '6ae3be81-abcd-477e-a8f3-4a5eb5fe91e3');

--

insert into avtale (ID, OPPRETTET_TIDSPUNKT, VERSJON, DELTAKER_FORNAVN, DELTAKER_ETTERNAVN, DELTAKER_FNR, BEDRIFT_NAVN,
                    BEDRIFT_NR, ARBEIDSGIVER_FNR, ARBEIDSGIVER_FORNAVN, ARBEIDSGIVER_ETTERNAVN, ARBEIDSGIVER_TLF,
                    VEILEDER_NAV_IDENT, VEILEDER_FORNAVN, VEILEDER_ETTERNAVN, VEILEDER_TLF, OPPFOLGING, TILRETTELEGGING,
                    START_DATO, ARBEIDSTRENING_LENGDE, ARBEIDSTRENING_STILLINGPROSENT, GODKJENT_AV_DELTAKER,
                    GODKJENT_AV_ARBEIDSGIVER, GODKJENT_AV_VEILEDER)
values ('5ae3be81-abcd-477e-a8f3-4a5eb5fe91e3', current_timestamp, 7, 'Dagny', 'Deltaker', '01093434109',
        'Pers butikk', '222222222', '29118923330', 'Per', 'Kremmer', '22334455', 'X123456', 'Vera', 'Veileder',
        '33445566', 'Telefon hver uke', 'Ingen', '2019-03-25', 2, 100, FALSE, FALSE, FALSE);

insert into maal(ID, OPPRETTET_TIDSPUNKT, KATEGORI, BESKRIVELSE, AVTALE)
values ('e16350f6-27ea-49b3-9fbc-25fcee0dd080', current_timestamp, 'Arbeidserfaring', 'Lære butikkarbeid',
        '5ae3be81-abcd-477e-a8f3-4a5eb5fe91e3');

insert into oppgave (ID, OPPRETTET_TIDSPUNKT, TITTEL, BESKRIVELSE, OPPLAERING, AVTALE)
values ('86a83e6d-e668-4073-a1ac-88885ae4df90', current_timestamp, 'Lager', 'Rydde på lageret', 'Ryddekurs',
        '5ae3be81-abcd-477e-a8f3-4a5eb5fe91e3');

--

insert into avtale (ID, OPPRETTET_TIDSPUNKT, VERSJON, DELTAKER_FORNAVN, DELTAKER_ETTERNAVN, DELTAKER_FNR, BEDRIFT_NAVN,
                    BEDRIFT_NR, ARBEIDSGIVER_FNR, ARBEIDSGIVER_FORNAVN, ARBEIDSGIVER_ETTERNAVN, ARBEIDSGIVER_TLF,
                    VEILEDER_NAV_IDENT, VEILEDER_FORNAVN, VEILEDER_ETTERNAVN, VEILEDER_TLF, OPPFOLGING, TILRETTELEGGING,
                    START_DATO, ARBEIDSTRENING_LENGDE, ARBEIDSTRENING_STILLINGPROSENT, GODKJENT_AV_DELTAKER,
                    GODKJENT_AV_ARBEIDSGIVER, GODKJENT_AV_VEILEDER)
values ('b3adb8c8-8205-11e9-bc42-526af7764f64', current_timestamp, 7, 'Dolly', 'Deltaker', '01093434109',
        'Dollys butikk', '222222222', '29118923330', 'Dolly', 'Dimple', '22334455', 'X123456', 'Vera', 'Veileder',
        '33445566', 'Telefon hver uke', 'Ingen', '2019-03-25', 2, 100, TRUE, TRUE, TRUE);

insert into maal(ID, OPPRETTET_TIDSPUNKT, KATEGORI, BESKRIVELSE, AVTALE)
values ('13292cf0-8207-11e9-bc42-526af7764f64', current_timestamp, 'Arbeidserfaring', 'Lære butikkarbeid',
        'b3adb8c8-8205-11e9-bc42-526af7764f64');

insert into oppgave (ID, OPPRETTET_TIDSPUNKT, TITTEL, BESKRIVELSE, OPPLAERING, AVTALE)
values ('20371934-8207-11e9-bc42-526af7764f64', current_timestamp, 'Lager', 'Rydde på lageret', 'Ryddekurs',
        'b3adb8c8-8205-11e9-bc42-526af7764f64');
