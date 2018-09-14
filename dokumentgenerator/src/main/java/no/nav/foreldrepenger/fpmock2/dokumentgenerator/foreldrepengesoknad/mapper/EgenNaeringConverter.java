package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.EgenNaeringDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.NorskOrganisasjonDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.UtenlandskOrganisasjonDto;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.EgenNaering;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.NorskOrganisasjon;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.UtenlandskOrganisasjon;
import org.modelmapper.ModelMapper;

public class EgenNaeringConverter extends DtoMapper<EgenNaeringDto, EgenNaering> {

    public EgenNaeringConverter(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected EgenNaering convert(EgenNaeringDto egenNaeringDto) {

        if (egenNaeringDto == null) {
            return null;
        }

        if (egenNaeringDto instanceof UtenlandskOrganisasjonDto) {
            UtenlandskOrganisasjonDto dto = (UtenlandskOrganisasjonDto) egenNaeringDto;
            return mapper.map(dto, UtenlandskOrganisasjon.class);
        }

        if (egenNaeringDto instanceof NorskOrganisasjonDto) {
            NorskOrganisasjonDto dto = (NorskOrganisasjonDto) egenNaeringDto;
            return mapper.map(dto, NorskOrganisasjon.class);
        }

        throw new RuntimeException("Fant ingen passende impl for " + egenNaeringDto.getClass());
    }
}
