package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import java.util.List;
import java.util.UUID;

public record NAVAnsatt(String ident, UUID oid, String displayName, String givenName, String surname, String email,
                        String streetAddress, List<NAVGroup> groups, List<String> enheter, List<String> vtpgrupper) {
    public record NAVGroup(UUID oid, String name) {}
}
