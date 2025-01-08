package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import java.util.List;
import java.util.UUID;

public record NavAnsatt(String ident, UUID oid, String displayName, String givenName, String surname, String email,
                        String streetAddress, List<NavGroup> groups, List<String> enheter) {
    public record NavGroup(UUID oid, String name) {}
}
