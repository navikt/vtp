package no.nav.foreldrepenger.vtp.testmodell.ansatt;

 import java.util.List;

 public record NAVAnsatt(String cn,
                         String displayName,
                         String email,
                         List<String> groups,
                         List<String> enheter) {

 }
