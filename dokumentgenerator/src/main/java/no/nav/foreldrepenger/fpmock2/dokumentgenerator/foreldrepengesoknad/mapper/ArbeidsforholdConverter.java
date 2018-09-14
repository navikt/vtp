package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.UtenlandskArbeidsforholdDto;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.UtenlandskArbeidsforhold;
import org.modelmapper.ModelMapper;

public class ArbeidsforholdConverter extends DtoMapper<UtenlandskArbeidsforholdDto, UtenlandskArbeidsforhold> {

    public ArbeidsforholdConverter( ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected UtenlandskArbeidsforhold convert(UtenlandskArbeidsforholdDto arbeidsforholdDto) {

        if (arbeidsforholdDto == null) {
            return null;
        }

        if (arbeidsforholdDto instanceof UtenlandskArbeidsforholdDto) {
            UtenlandskArbeidsforholdDto dto = (UtenlandskArbeidsforholdDto) arbeidsforholdDto;
            return mapper.map(dto, UtenlandskArbeidsforhold.class);
        }

        throw new RuntimeException("Fant ingen passende impl for" + arbeidsforholdDto.getClass());
    }
}