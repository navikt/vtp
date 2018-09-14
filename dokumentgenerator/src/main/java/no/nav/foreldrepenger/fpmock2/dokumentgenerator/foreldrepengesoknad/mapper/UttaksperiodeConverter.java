package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.UttaksperiodeDto;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Uttaksperiode;
import org.modelmapper.ModelMapper;

public class UttaksperiodeConverter extends DtoMapper<UttaksperiodeDto, Uttaksperiode> {

    public UttaksperiodeConverter(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected Uttaksperiode convert(UttaksperiodeDto uttaksperiodeDto) {
        UttaksperiodeDto dto = (UttaksperiodeDto) uttaksperiodeDto;
        return mapper.map(dto, Uttaksperiode.class);
    }
}
