package no.nav.foreldrepenger.vtp.testmodell.ansatt;

import java.util.List;
import java.util.UUID;

public record NAVAnsatt(String ident, UUID oid, String displayName, String email, List<NAVGroup> groups, List<String> enheter) {
    public record NAVGroup(UUID oid, String name) {}
}
