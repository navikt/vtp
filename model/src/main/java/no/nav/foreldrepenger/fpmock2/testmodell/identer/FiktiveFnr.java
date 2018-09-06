package no.nav.foreldrepenger.fpmock2.testmodell.identer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hent et tilfeldig gyldig men fiktivt Fødselsnummer.
 * 
 * @see https://confluence.adeo.no/pages/viewpageattachments.action?pageId=211653415&metadataLink=true (SKD fiktive identer)
 */
public class FiktiveFnr implements IdentGenerator {
    static class Innhold {
        final AtomicInteger teller = new AtomicInteger();
        final List<String> fnrs;
        private String type;

        Innhold(String type, List<String> fnrs) {
            this.type = type;
            this.fnrs = fnrs;
        }

        String neste() {
            if (teller.get() >= fnrs.size()) {
                throw new IllegalStateException("Tom for fødselsnummer:" + type);
            }
            return fnrs.get(teller.getAndIncrement());
        }
    }

    private static final Map<String, Innhold> FNRS = new ConcurrentHashMap<>();
    private static final AtomicInteger nesteTilfeldig = new AtomicInteger();

    /**
     * Bruk denne når kjønn ikke har betydning for anvendt FNR. (Bør normalt brukes slik at en sikrer at applikasjonen ikke gjør antagelser om
     * koding av kjønn i FNR.
     */
    @Override
    public String nesteFnr() {
        if (nesteTilfeldig.getAndIncrement() % 2 == 0) {
            return nesteKvinneFnr();
        } else {
            return nesteMannFnr();
        }
    }

    /** Returnerer FNR for mann > 18 år */
    @Override
    public String nesteMannFnr() {
        return neste("mann");
    }

    /** Returnerer FNR for kvinne > 18 år */
    @Override
    public String nesteKvinneFnr() {
        return neste("kvinne");
    }

    /** Returnerer FNR for barn (tilfeldig kjønn) < 18 år */
    @Override
    public String nesteBarnFnr() {
        return neste("barn");
    }

    private String neste(String key) {
        return FNRS.computeIfAbsent(key, this::read).neste();
    }

    private Innhold read(String key) {
        return new Innhold(key, readFile("/basedata/fiktive_fnr_" + key + ".csv"));
    }

    private static List<String> readFile(String fil) {
        List<String> list = new ArrayList<>();
        try (InputStream is = FiktiveFnr.class.getResourceAsStream(fil);
                Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                list.add(scanner.nextLine());
            }
            return list;
        } catch (IOException e) {
            throw new IllegalStateException("Kunne ikke lese fil: " + fil, e);

        }
    }
}
