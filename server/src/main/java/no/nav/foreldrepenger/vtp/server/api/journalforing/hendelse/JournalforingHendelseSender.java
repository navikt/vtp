package no.nav.foreldrepenger.vtp.server.api.journalforing.hendelse;

import java.util.UUID;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.joarkjournalfoeringhendelser.JournalfoeringHendelseRecord;

public class JournalforingHendelseSender {

    private static final String JOURNALFØRING_TOPIC = "aapen-dok-journalfoering-v1-q1";
    private static final String BEHANDLINGSTEMA_FORELDREPENGER_FØDSEL = "ab0047";

    private LocalKafkaProducer localKafkaProducer;

    public JournalforingHendelseSender(LocalKafkaProducer localKafkaProducer) {
        this.localKafkaProducer = localKafkaProducer;
    }

    public void leggTilJournalføringHendelsePåKafka(JournalpostModell modell) {
        localKafkaProducer.sendMelding(JOURNALFØRING_TOPIC, lagGeneriskJournalfoeringHendelseRecord(modell));
    }

    private GenericData.Record lagGeneriskJournalfoeringHendelseRecord(JournalpostModell modell) {
        return new GenericRecordBuilder(JournalfoeringHendelseRecord.SCHEMA$)
                .set("hendelsesId", UUID.randomUUID().toString()).set("versjon", 1)
                .set("hendelsesType", tilHendelsesType(modell))
                .set("journalpostId", Long.parseLong(modell.getJournalpostId()))
                .set("journalpostStatus", modell.getJournalStatus() != null ? modell.getJournalStatus().getKode() : "M")
                .set("temaGammelt", "")
                .set("temaNytt", modell.getArkivtema() != null ? modell.getArkivtema().getKode() : "FOR")
                .set("mottaksKanal", modell.getMottakskanal() != null ? modell.getMottakskanal() : "ALTINN")
                .set("kanalReferanseId", modell.getEksternReferanseId())
                .set("behandlingstema", BEHANDLINGSTEMA_FORELDREPENGER_FØDSEL)
                .build();
    }

    private static String tilHendelsesType(JournalpostModell modell) {
        return switch (modell.getJournalStatus() != null ? modell.getJournalStatus().getKode() : "") {
            case "J" -> "Journalført";
            case "MO" -> "Mottatt";
            case "M" -> "MidlertidigJournalført";
            case "A" -> "Avbrutt";
            default -> "MidlertidigJournalført";
        };
    }
}
