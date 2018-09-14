package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.SoeknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.erketyper.ForeldrepengeSoknadDto;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Foreldrepenger;

public interface JournalforSoknadTjeneste {
    
    String lagXml(SoeknadDto søknad) throws InputValideringException;
    String lagXmlFraForeldrepengeDto(ForeldrepengeSoknadDto foreldrepengeSoknadDto, String aktoerid) throws InputValideringException;
    String lagXmlFraForeldrepengeDto(ForeldrepengeSoknadDto foreldrepengeSoknadDto, String aktoerid, String aktoeridAnnenForelder) throws InputValideringException;

}
