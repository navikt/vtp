package no.nav.foreldrepenger.vtp.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.util.JacksonObjectMapperTestscenario;
import no.nav.foreldrepenger.vtp.kontrakter.organisasjon.NavAnsattDto;
import no.nav.foreldrepenger.vtp.kontrakter.organisasjon.NavGruppeDto;
import no.nav.vtp.ansatt.AnsatteIndeks;

/*
 * Roundtrip test av grupper og ansatte
 * Data til verdikjede-environment: System out println av "VTP_GRUPPER=\"" + serialized.replace("\"", "\\\"") + "\"" (evt VTP_ANSATTE)
 */
class AnsattGruppeEnvTest {


    @Test
    void ansatteOgGrupper() {
        var gsak = new NavGruppeDto(UUID.fromString("eb211c0d-9ca6-467f-8863-9def2cc06fd3"), "0000-GA-fpsak-saksbehandler");
        var gveil = new NavGruppeDto(UUID.fromString("edfe14fe-9a34-4ecb-8840-536ac2bc2818"), "0000-GA-fpsak-veileder");
        var gea = new NavGruppeDto(UUID.fromString("63b3f84f-1ec5-444b-ad33-2ad2d3495da1"), "0000-GA-Egne_ansatte");
        var saksbehandler = new NavAnsattDto("S123456", UUID.fromString("3c3caafb-a943-4255-aa65-6f29345b7541"),
                "Sara", "Saksbehandler", "4867", List.of("0000-GA-fpsak-saksbehandler"));
        var eaveileder = new NavAnsattDto("V123456", UUID.fromString("ba6f0772-b99b-4eb9-857e-7a9e6720d582"),
                "Vegard", "Veileder", "1900", List.of("0000-GA-fpsak-veileder", "0000-GA-Egne_ansatte"));
        var aserialized = JacksonObjectMapperTestscenario.writeValueAsString(List.of(saksbehandler, eaveileder));
        var gserialized = JacksonObjectMapperTestscenario.writeValueAsString(List.of(gsak, gea, gveil));
        System.setProperty("vtp.grupper", gserialized);
        System.setProperty("vtp.ansatte", aserialized);
        PropertiesUtils.initProperties();
        MockServer.initAnsatte();
        assertThat(AnsatteIndeks.gruppeByNavn("0000-GA-fpsak-saksbehandler").oid()).isEqualTo(gsak.oid());
        assertThat(AnsatteIndeks.gruppeByNavn("0000-GA-k9-saksbehandler")).isNull();
        assertThat(AnsatteIndeks.findByIdent("S123456").oid()).isEqualTo(saksbehandler.oid());
        assertThat(AnsatteIndeks.findByIdent("S123457")).isNull();
        assertThat(AnsatteIndeks.findByIdent("V123456").groups())
                .containsExactlyInAnyOrder(AnsatteIndeks.gruppeByNavn("0000-GA-fpsak-veileder"), AnsatteIndeks.gruppeByNavn("0000-GA-Egne_ansatte"));
    }
}
