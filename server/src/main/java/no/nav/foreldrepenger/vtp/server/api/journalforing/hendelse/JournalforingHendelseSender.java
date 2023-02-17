package no.nav.foreldrepenger.vtp.server.api.journalforing.hendelse;

import java.util.UUID;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

import no.nav.foreldrepenger.vtp.kafkaembedded.LocalKafkaProducer;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BehandlingsTema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.joarkjournalfoeringhendelser.JournalfoeringHendelseRecord;

public class JournalforingHendelseSender {

    private static final String JOURNALFØRING_TOPIC = "teamdokumenthandtering.aapen-dok-journalfoering-vtp";

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
                .set("mottaksKanal", modell.getMottakskanal() != null ? modell.getMottakskanal().getKode() : "ALTINN")
                .set("kanalReferanseId", modell.getEksternReferanseId())
                .set("behandlingstema", tilBehandlingsTema(modell).getOffisiellKode())
                .build();
    }

    private String tilHendelsesType(JournalpostModell modell) {
        return switch (modell.getJournalStatus() != null ? modell.getJournalStatus().getKode() : "") {
            case "J" -> "Journalført";
            case "MO" -> "Mottatt";
            case "M" -> "MidlertidigJournalført";
            case "A" -> "Avbrutt";
            default -> "MidlertidigJournalført";
        };
    }

    private BehandlingsTema tilBehandlingsTema(JournalpostModell modell) {
        return switch (modell.getDokumentModellList().isEmpty() ? DokumenttypeId.UDEFINERT : modell.getDokumentModellList().get(0).getDokumentType()) {
            case SØKNAD_SVANGERSKAPSPENGER -> BehandlingsTema.SVANGERSKAPSPENGER;
            case SØKNAD_FORELDREPENGER_ADOPSJON -> BehandlingsTema.FORELDREPENGER_ADOPSJON;
            case SØKNAD_FORELDREPENGER_FØDSEL -> BehandlingsTema.FORELDREPENGER_FØDSEL;
            case SØKNAD_ENGANGSSTØNAD_ADOPSJON -> BehandlingsTema.ENGANGSSTØNAD_ADOPSJON;
            case SØKNAD_ENGANGSSTØNAD_FØDSEL -> BehandlingsTema.ENGANGSSTØNAD_FØDSEL;
            default -> BehandlingsTema.FORELDREPENGER_FØDSEL;
        };
    }
}
