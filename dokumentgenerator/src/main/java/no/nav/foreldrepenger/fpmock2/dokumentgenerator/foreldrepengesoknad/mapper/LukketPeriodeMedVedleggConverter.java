package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.LukketPeriodeDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.*;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.*;
import org.modelmapper.ModelMapper;

public class LukketPeriodeMedVedleggConverter<T extends LukketPeriodeDto> {
    private ModelMapper mapper;

    LukketPeriodeMedVedleggConverter(ModelMapper mapper) {
        this.mapper = mapper;
    }

    protected LukketPeriodeMedVedlegg konverter(T lukketPeriodeDto) {

        if (lukketPeriodeDto == null) {
            return null;
        }

        if(lukketPeriodeDto.getClass() == GraderingDto.class && lukketPeriodeDto.getClass().isAssignableFrom(GraderingDto.class)){
            GraderingDto dto = (GraderingDto) lukketPeriodeDto;
            return mapper.map(dto, Gradering.class);
        }

        if (lukketPeriodeDto.getClass().isAssignableFrom(UttaksperiodeDto.class)) {
            UttaksperiodeDto dto = (UttaksperiodeDto) lukketPeriodeDto;
            return mapper.map(dto, Uttaksperiode.class);
        }

        if (lukketPeriodeDto.getClass().isAssignableFrom(OppholdsperiodeDto.class)) {
            OppholdsperiodeDto dto = (OppholdsperiodeDto) lukketPeriodeDto;
            return mapper.map(dto, Oppholdsperiode.class);
        }

        if (lukketPeriodeDto.getClass().isAssignableFrom(OverfoeringsperiodeDto.class)) {
            OverfoeringsperiodeDto dto = (OverfoeringsperiodeDto) lukketPeriodeDto;
            return mapper.map(dto, Overfoeringsperiode.class);
        }

        if (lukketPeriodeDto.getClass().isAssignableFrom(UtsettelsesperiodeDto.class)) {
            UtsettelsesperiodeDto dto = (UtsettelsesperiodeDto) lukketPeriodeDto;
            return mapper.map(dto, Utsettelsesperiode.class);
        }

        throw new RuntimeException("Fant ingen passende impl for " + lukketPeriodeDto.getClass());
    }



}
