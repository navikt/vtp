package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.*;
import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.*;
import org.modelmapper.ModelMapper;

import javax.xml.datatype.DatatypeConfigurationException;

public class SoekersRelasjonTilBarnetConverter extends DtoMapper<SoekersRelasjonTilBarnetDto, SoekersRelasjonTilBarnet> {

    public SoekersRelasjonTilBarnetConverter(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected SoekersRelasjonTilBarnet convert(SoekersRelasjonTilBarnetDto soekersRelasjonTilBarnetDto) {

        if (soekersRelasjonTilBarnetDto == null) {
            return null;
        }

        if (soekersRelasjonTilBarnetDto instanceof FoedselDto) {
            FoedselDto dto = (FoedselDto) soekersRelasjonTilBarnetDto;
            return mapper.map(dto, Foedsel.class);
        }

        if (soekersRelasjonTilBarnetDto instanceof AdopsjonDto) {
            AdopsjonDto dto = (AdopsjonDto) soekersRelasjonTilBarnetDto;
            Adopsjon map = mapper.map(dto, Adopsjon.class);
            dto.getFoedselsdato().stream().forEach(fdato -> {
                try {
                    map.getFoedselsdato().add(DateUtil.convertToXMLGregorianCalendar(fdato));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            });

            return map;
        }

        if (soekersRelasjonTilBarnetDto instanceof OmsorgsovertakelseDto) {
            OmsorgsovertakelseDto dto = (OmsorgsovertakelseDto) soekersRelasjonTilBarnetDto;
            Omsorgsovertakelse map = mapper.map(dto, Omsorgsovertakelse.class);

            dto.getFoedselsdato().stream().forEach(fdato -> {
                try {
                    map.getFoedselsdato().add(DateUtil.convertToXMLGregorianCalendar(fdato));
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
            });

            return map;
        }

        if (soekersRelasjonTilBarnetDto instanceof TerminDto) {
            TerminDto dto = (TerminDto) soekersRelasjonTilBarnetDto;
            return mapper.map(dto, Termin.class);
        }

        throw new RuntimeException("Fant ingen passende impl for " + soekersRelasjonTilBarnetDto.getClass());
    }
}
