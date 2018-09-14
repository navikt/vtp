package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.OppholdNorgeDto;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.OppholdNorge;
import org.modelmapper.ModelMapper;

public class OppholdNorgeConverter extends DtoMapper<OppholdNorgeDto, OppholdNorge> {
    public OppholdNorgeConverter(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected OppholdNorge convert(OppholdNorgeDto oppholdNorgeDto) {
        return mapper.map(oppholdNorgeDto, OppholdNorge.class);
    }
}
