package no.nav.foreldrepenger.vtp.testmodell.inntektytelse;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.inntektkomponent.EndretInntektHendelse;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InntektYtelseIndeks {

    private Map<String, InntektYtelseModell> byIdent = new ConcurrentHashMap<>();

    private Map<String, List<EndretInntektHendelse>> inntektshendelserByIdent = new ConcurrentHashMap<>();


    public Optional<InntektYtelseModell> getModellForIdent(String ident) {
        return Optional.ofNullable(byIdent.get(ident));
    }

    public List<EndretInntektHendelse> getHendelserForIdent(String ident) {
        return inntektshendelserByIdent.getOrDefault(ident, Collections.emptyList());
    }


    public void leggTil(String ident, InntektYtelseModell iy) {
        if (ident != null && iy != null) {
            byIdent.put(ident, iy);
        }
    }

    public void leggTilInntektendring(String ident, YearMonth måned) {
        if (ident != null && måned != null) {
            var hendelser = inntektshendelserByIdent.getOrDefault(ident, new ArrayList<>());
            long nyttSekvensnummer = hendelser.size() + 1;
            hendelser.add(new EndretInntektHendelse(nyttSekvensnummer, ident, måned));
            inntektshendelserByIdent.put(ident, hendelser);
        }
    }

}
