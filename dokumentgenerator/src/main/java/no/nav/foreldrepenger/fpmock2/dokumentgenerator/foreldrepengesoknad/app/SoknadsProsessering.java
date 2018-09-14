package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.SoeknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.BrukerDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Bruker;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Ytelse;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;
import org.modelmapper.ModelMapper;

import javax.xml.datatype.DatatypeConfigurationException;


public abstract class SoknadsProsessering<T> {

    protected final ModelMapper mapper;

    protected abstract Ytelse mapTilYtelse();

    public SoknadsProsessering(ModelMapper mapper) {
        this.mapper = mapper;
    }

    protected abstract void validerSøknad(T soknadDto) throws InputValideringException;

    protected abstract String konvertTilXML(T soknadDto);

    protected Soeknad mapTilSoeknad(SoeknadDto soeknadDto) throws DatatypeConfigurationException {
        Ytelse ytelse = mapTilYtelse();

        Bruker bruker = mapTilBruker(soeknadDto.getSoeker());
        return ForeldrepengesoknadBuilder.startBuilding()
                .withMottattDato(DateUtil.convertToXMLGregorianCalendar(soeknadDto.getMottattDato()))
                .withBegrunnelseForSenSoeknad(soeknadDto.getBegrunnelseForSenSoeknad())
                .withTilleggsopplysninger(soeknadDto.getTilleggsopplysninger())
                .withForeldrepengerYtelse(ytelse)
                .withSoeker(bruker)
                .withAndreVedlegg(soeknadDto.getAndreVedlegg())
                .withPaakrevdeVedlegg(soeknadDto.getPaakrevdeVedlegg())
                .build();

    }

    private Bruker mapTilBruker(BrukerDto bruker) {
        return mapper.map(bruker, Bruker.class);
    }


}
