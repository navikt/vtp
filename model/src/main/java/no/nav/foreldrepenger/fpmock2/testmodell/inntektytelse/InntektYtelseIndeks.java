package no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InntektYtelseIndeks {

    private Map<String, InntektYtelseModell> byIdent = new HashMap<>();
    
    public Optional<InntektYtelseModell> getModellForIdent(String ident) {
        return Optional.ofNullable(byIdent.get(ident));
    }

    public void leggTil(InntektYtelseModell iy) {
        byIdent.put(iy.getIdent().getIdent(), iy);
    }

}
