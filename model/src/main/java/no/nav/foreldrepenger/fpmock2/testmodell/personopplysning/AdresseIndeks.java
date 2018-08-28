package no.nav.foreldrepenger.fpmock2.testmodell.personopplysning;

import java.util.ArrayList;
import java.util.List;

public class AdresseIndeks {

    private List<AdresseModell> adresser = new ArrayList<>();

    public AdresseIndeks() {
    }

    public void leggTil(AdresseModell adresse) {
        adresser.add(adresse);
    }

    public AdresseModell finn(AdresseType adresseType, Landkode landkode) {
        return finn(adresseType, landkode == null ? null : landkode.getKode());
    }

    public AdresseModell finn(AdresseType adresseType, String landkode) {
        return adresser.stream()
            .filter(a -> a.getAdresseType() == adresseType)
            .filter(a -> landkode.equals(a.getLandkode()))
            .filter(a -> !(AdresseRefModell.class.equals(a.getClass())))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Fant ingen adresser i indeks for type=" + adresseType + ", landkode=" + landkode));
    }

    /** bytt ut en generisk referansemal med en adressse fra katalogen. */
    public AdresseModell finnFra(AdresseRefModell ref) {
        AdresseModell adresse = finn(ref.getAdresseType(), ref.getLandkode());
        adresse = (AdresseModell) adresse.clone();

        // overskriv hvis felter er satt på ref
        if (ref.getFom() != null) {
            adresse.setFom(ref.getFom());
        }
        if (ref.getTom() != null) {
            adresse.setTom(ref.getTom());
        }
        return adresse;
    }
}
