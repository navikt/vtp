package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.MedlemskapErketyper;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.RettigheterErketyper;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.*;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Dekningsgrad;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Opptjening;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Dekningsgrader;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.v3.OmYtelse;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.ObjectFactory;


import javax.xml.bind.JAXBElement;
import java.util.Objects;

public class ForeldrepengerYtelseBuilder {
    private Foreldrepenger kladd;

    public ForeldrepengerYtelseBuilder(SoekersRelasjonTilBarnet soekersRelasjonTilBarnet, Fordeling fordeling) {
        kladd = new Foreldrepenger();
        kladd.setRelasjonTilBarnet(soekersRelasjonTilBarnet);
        kladd.setFordeling(fordeling);
    }
    public ForeldrepengerYtelseBuilder medDekningsgrad(Dekningsgrad dekningsgrad) {
        kladd.setDekningsgrad(dekningsgrad);
        return this;
    }
    public ForeldrepengerYtelseBuilder medDekningsgrad(String dekningsgradString){
        Dekningsgrad dekningsgrad = new Dekningsgrad();
        Dekningsgrader dekningsgrader = new Dekningsgrader();
        dekningsgrader.setKode(dekningsgradString);
        dekningsgrader.setKodeverk("IKKE_DEFINERT");
        dekningsgrad.setDekningsgrad(dekningsgrader);

        kladd.setDekningsgrad(dekningsgrad);
        return this;
    }
    public ForeldrepengerYtelseBuilder medMedlemskap(Medlemskap medlemskap) {
        kladd.setMedlemskap(medlemskap);
        return this;
    }
    public ForeldrepengerYtelseBuilder medRettigheter(Rettigheter rettigheter) {
        kladd.setRettigheter(rettigheter);
        return this;
    }
    public ForeldrepengerYtelseBuilder medAnnenForelder(AnnenForelder annenForelder) {
        kladd.setAnnenForelder(annenForelder);
        return this;
    }
    public ForeldrepengerYtelseBuilder medAnnenForelder(String annenForelderAktørId) {
        kladd.setAnnenForelder(standardAnnenForelder(annenForelderAktørId));
        return this;
    }
    public ForeldrepengerYtelseBuilder medSpesiellOpptjening(Opptjening opptjening) {
        kladd.setOpptjening(opptjening);
        return this;
    }
    protected static AnnenForelder standardAnnenForelder(String aktørId) {
        AnnenForelderMedNorskIdent forelder = new AnnenForelderMedNorskIdent();
        forelder.setAktoerId(aktørId);
        return forelder;
    }

    public Foreldrepenger build() {
        if (kladd.getDekningsgrad() == null) {
            medDekningsgrad("100");
        }
        if (kladd.getMedlemskap() == null) {
            kladd.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        }
        if (kladd.getRettigheter() == null) {
            kladd.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        }
        if (kladd.getAnnenForelder() == null) {
            kladd.setAnnenForelder(new UkjentForelder());
        }

        return kladd;
    }
}
