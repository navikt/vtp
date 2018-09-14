package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.SoeknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Ytelse;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;
import org.modelmapper.ModelMapper;

public class ForeldrepengesoknadProsessering extends SoknadsProsessering<SoeknadDto> {

    private SoeknadDto soknadDto;

    public ForeldrepengesoknadProsessering(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected void validerSøknad(SoeknadDto soknadDto) throws InputValideringException {
        setSoknadDto(soknadDto);
    }

    @Override
    protected String konvertTilXML(SoeknadDto soknadDto) {
        setSoknadDto(soknadDto);

        try {
            Soeknad soeknad = mapTilSoeknad(soknadDto);
            return ForeldrepengesoknadBuilder.tilXML(soeknad);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void setSoknadDto(SoeknadDto soknadDto) {
        this.soknadDto = soknadDto;

    }

    @Override
    protected Ytelse mapTilYtelse() {

        Foreldrepenger foreldrepenger = mapper.map(soknadDto.getOmYtelse(), Foreldrepenger.class);
        return foreldrepenger;
    }
}
