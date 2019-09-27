package no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.MedlemskapErketyper;
import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.SoekersRelasjonTilBarnet;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.UkjentForelder;

import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse.ForeldrepengerYtelseBuilder.standardAnnenForelder;

public class EngangstønadYtelseBuilder {
    private Engangsstønad kladd;

    public EngangstønadYtelseBuilder(SoekersRelasjonTilBarnet soekersRelasjonTilBarnet) {
        kladd = new Engangsstønad();
        kladd.setSoekersRelasjonTilBarnet(soekersRelasjonTilBarnet);
    }
    public EngangstønadYtelseBuilder medMedlemskap(Medlemskap medlemskap) {
        kladd.setMedlemskap(medlemskap);
        return this;
    }
    public EngangstønadYtelseBuilder medAnnenForelder(String annenForelderAktørId) {
        kladd.setAnnenForelder(standardAnnenForelder(annenForelderAktørId));
        return this;
    }

    public Engangsstønad build() {
        if (kladd.getMedlemskap() == null) {
            kladd.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        }
        if (kladd.getAnnenForelder() == null) {
            kladd.setAnnenForelder(new UkjentForelder());
        }

        return kladd;
    }
}
