package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.AnnenForelderDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.AnnenForelderMedNorskIdentDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.AnnenForelderUtenNorskIdentDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.UkjentForelderDto;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.AnnenForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.AnnenForelderMedNorskIdent;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.AnnenForelderUtenNorskIdent;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.UkjentForelder;
import org.modelmapper.ModelMapper;

public class AnnenForelderConverter extends DtoMapper<AnnenForelderDto, AnnenForelder> {

    public AnnenForelderConverter(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected AnnenForelder convert(AnnenForelderDto annenForelderDto) {

        if (annenForelderDto == null) {
            return null;
        }

        if (annenForelderDto instanceof AnnenForelderMedNorskIdentDto) {
            AnnenForelderMedNorskIdentDto dto = (AnnenForelderMedNorskIdentDto) annenForelderDto;
            return mapper.map(dto, AnnenForelderMedNorskIdent.class);
        }

        if (annenForelderDto instanceof AnnenForelderUtenNorskIdentDto) {
            AnnenForelderUtenNorskIdentDto dto = (AnnenForelderUtenNorskIdentDto) annenForelderDto;
            return mapper.map(dto, AnnenForelderUtenNorskIdent.class);
        }


        if (annenForelderDto instanceof UkjentForelderDto) {
            UkjentForelderDto dto = (UkjentForelderDto) annenForelderDto;
            return mapper.map(dto, UkjentForelder.class);
        }

        throw new RuntimeException("Fant ingen passende impl for " + annenForelderDto.getClass());
    }
}
