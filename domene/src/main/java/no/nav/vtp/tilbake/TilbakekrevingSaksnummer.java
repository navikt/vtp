package no.nav.vtp.tilbake;

import java.security.SecureRandom;
import java.util.Random;

import no.nav.foreldrepenger.vtp.kontrakter.tilbakekreving.TilbakekrevingKonsistensDto;

/**
 * Tjenesten tar vare på siste saksnummer og henvisning (behandlingId) fra Autotest,
 * slik at de kan sendes tilbake i genererte kravgrunnlag som da vil være konsistente med siste kjente sak.
 */
public class TilbakekrevingSaksnummer {

    private static final Random RANDOM =  new SecureRandom();

    private static int sisteSaksnummer = genererTilfeldigSaksnummer();
    private static String sisteHenvisning = "1";

    private TilbakekrevingSaksnummer() {
    }

    // Sørger for at kravgrunnlag som returneres av mock har riktig saksnummer og henvisning
    public static void oppdaterSisteSaksnummer(TilbakekrevingKonsistensDto request) {
        sisteSaksnummer = Integer.parseInt(request.saksnummer());
        sisteHenvisning = request.behandlingId();
    }

    public static int getSisteSaksnummer() {
        return sisteSaksnummer;
    }

    public static String getSisteHenvisning() {
        return sisteHenvisning;
    }

    private static int genererTilfeldigSaksnummer() {
        var m = (int) Math.pow(10, 9);
        return m + RANDOM.nextInt(9 * m);
    }
}
