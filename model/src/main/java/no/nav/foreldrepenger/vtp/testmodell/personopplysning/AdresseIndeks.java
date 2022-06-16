package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdresseIndeks {

    private final List<AdresseModell> adresser = new ArrayList<>();

    public synchronized void leggTil(AdresseModell adresse) {
        adresser.add(adresse);
    }

    /** Bytter ut en generisk referansemal fra scenario med en adressse fra katalogen (adresse-maler.json) */
    public synchronized AdresseModell finnFra(AdresseRefModell ref) {
        var adresse = finn(ref.getAdresseType(), ref.getLand(), ref.getMatrikkelId());
        adresse = lagKopiAvAdresseModell(adresse);

        // overskriv hvis felter er satt på ref
        if (ref.getFom() != null) {
            adresse.setFom(ref.getFom());
        }
        if (ref.getTom() != null) {
            adresse.setTom(ref.getTom());
        }
        return adresse;
    }

    private AdresseModell finn(AdresseType adresseType, Landkode landkode, String matrikkelId) {
        return adresser.stream()
                .filter(a -> !(AdresseRefModell.class.equals(a.getClass())))
                .filter(a -> Objects.equals(adresseType, a.getAdresseType()))
                .filter(a -> Objects.equals(landkode, a.getLand()))
                .filter(a -> matrikkelId == null || Objects.equals(matrikkelId, a.getMatrikkelId())) // Skal ikke filtreres bort når matrikkelId ikke er oppgitt
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Fant ingen adresser i indeks for type=" + adresseType + ", landkode=" + landkode));
    }

    private AdresseModell lagKopiAvAdresseModell(AdresseModell adresse) {
        if (adresse instanceof UstrukturertAdresseModell u) {
            return new UstrukturertAdresseModell(u);
        } else if (adresse instanceof GateadresseModell g) {
            return new GateadresseModell(g);
        } else if (adresse instanceof PostboksadresseModell p) {
            return new PostboksadresseModell(p);
        } else {
            throw new IllegalStateException("Hentet adressemodell er av type 'ref' er ikke tillatt. Sørg for at adresse-maler.json ikke innholder addresse type 'ref'!");
        }
    }

}
