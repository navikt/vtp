package no.nav.foreldrepenger.vtp.testmodell.ansatt;

 import java.util.List;
 import java.util.UUID;

public record NAVAnsatt(String ident, UUID sub, String displayName, String email, List<String> groups, List<String> enheter) {
}
