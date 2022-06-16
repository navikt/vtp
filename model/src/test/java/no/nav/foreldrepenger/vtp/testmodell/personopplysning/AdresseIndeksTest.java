package no.nav.foreldrepenger.vtp.testmodell.personopplysning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

class AdresseIndeksTest {
    private static final LocalDate NOW = LocalDate.now();

    private final AdresseIndeks adresseIndeks = BasisdataProviderFileImpl.getInstance().getAdresseIndeks();

    @Test
    void skalFinneAdresseVedMatchendeAdressetypeOgLandOgMatrikkelIdIkkeOppgitt() {
        var ref = gyldigAdresseRefModell();
        var adresseHentet = adresseIndeks.finnFra(ref);

        assertThat(adresseHentet).isInstanceOf(GateadresseModell.class);
        verifiserRefHenterAdresseMedRiktigFelter(ref, adresseHentet);
    }
    @Test
    void skalFinneUstrukturertadresseVedMatchendeAdressetypeOgLandOgMatrikkelIdIkkeOppgitt() {
        var ref = gyldigAdresseRefModell2();
        var adresseHentet = adresseIndeks.finnFra(ref);

        assertThat(adresseHentet).isInstanceOf(UstrukturertAdresseModell.class);
        verifiserRefHenterAdresseMedRiktigFelter(ref, adresseHentet);
    }

    @Test
    void skalFinneAdresseVedMatchendeAdressetypeOgLandOgMatrikkelId() {
        var ref = gyldigAdresseRefModell();
        ref.setMatrikkelId("000000002");
        var adresseHentet = adresseIndeks.finnFra(ref);

        assertThat(adresseHentet).isInstanceOf(GateadresseModell.class);
        verifiserRefHenterAdresseMedRiktigFelter(ref, adresseHentet);
    }

    @Test
    void skalHiveExceptionHvisFelteneForAdresseRefModellErNull() {
        var tomAdresseRefModell = new AdresseRefModell();
        assertThrows(IllegalArgumentException.class, () -> adresseIndeks.finnFra(tomAdresseRefModell));
    }

    @Test
    void skalHiveExceptionNårViIkkeFinnerAdresseIIndeksenSomMatcherAlle3ParametreneEllerERAdresseRefModell() {
        var ıkkeRegistretAdresseForAdresseType = AdresseType.UKJENT_ADRESSE;
        var ıkkeRegistretAdresseForLand = Landkode.DEU;
        var ıkkeRegistretAdresseForMatrikkelID = "99999999999999";

        var ıkkeRegistretAdressetypeForAdresseRefModell = gyldigAdresseRefModell();
        ıkkeRegistretAdressetypeForAdresseRefModell.setAdresseType(ıkkeRegistretAdresseForAdresseType);
        assertThrows(IllegalArgumentException.class, () -> adresseIndeks.finnFra(ıkkeRegistretAdressetypeForAdresseRefModell));

        var ıkkeRegistretLandForAdresseRefModell = gyldigAdresseRefModell();
        ıkkeRegistretLandForAdresseRefModell.setLand(ıkkeRegistretAdresseForLand);
        assertThrows(IllegalArgumentException.class, () -> adresseIndeks.finnFra(ıkkeRegistretLandForAdresseRefModell));

        var ıkkeRegistretMatrikkelIDForAdresseRefModell = gyldigAdresseRefModell();
        ıkkeRegistretMatrikkelIDForAdresseRefModell.setMatrikkelId(ıkkeRegistretAdresseForMatrikkelID);
        assertThrows(IllegalArgumentException.class, () -> adresseIndeks.finnFra(ıkkeRegistretMatrikkelIDForAdresseRefModell));
    }

    private AdresseRefModell gyldigAdresseRefModell() {
        var adresseRefModell = new AdresseRefModell();
        adresseRefModell.setAdresseType(AdresseType.BOSTEDSADRESSE);
        adresseRefModell.setLand(Landkode.NOR);
        adresseRefModell.setFom(NOW.minusYears(3));
        return adresseRefModell;
    }

    private AdresseRefModell gyldigAdresseRefModell2() {
        var adresseRefModell = new AdresseRefModell();
        adresseRefModell.setAdresseType(AdresseType.MIDLERTIDIG_POSTADRESSE);
        adresseRefModell.setLand(Landkode.USA);
        adresseRefModell.setFom(NOW.minusYears(3));
        return adresseRefModell;
    }

    private void verifiserRefHenterAdresseMedRiktigFelter(AdresseRefModell ref, AdresseModell adresseHentet) {
        assertThat(adresseHentet.getAdresseType()).isEqualTo(ref.getAdresseType());
        assertThat(adresseHentet.getLand()).isEqualTo(ref.getLand());
        assertThat(adresseHentet.getFom()).isEqualTo(ref.getFom());
        assertThat(adresseHentet.getTom()).isEqualTo(ref.getTom());

        // Verifiser at clone operasjonen i finnFra kloner felter fra submodels også
        if (adresseHentet instanceof GateadresseModell gateadresseModell) {
            assertThat(gateadresseModell.getGatenavn()).isNotNull();
            assertThat(gateadresseModell.getGatenummer()).isNotNull();
            assertThat(gateadresseModell.getPostnummer()).isNotNull();
            assertThat(gateadresseModell.getMatrikkelId()).isNotNull();
        } else if (adresseHentet instanceof UstrukturertAdresseModell ustrukturertAdresseModell) {
            assertThat(ustrukturertAdresseModell.getAdresseLinje1()).isNotNull();
            assertThat(ustrukturertAdresseModell.getAdresseLinje2()).isNotNull();
        }
    }

}
