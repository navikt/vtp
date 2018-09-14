package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;

abstract class DtoMapper<S, T> extends AbstractConverter<S, T> {

    protected final ModelMapper mapper;

    public DtoMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }
}
