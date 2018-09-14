package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.FordelingDto;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Overfoeringsaarsaker;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class FordelingConverter extends DtoMapper<FordelingDto, Fordeling> {

    //private LukketPeriodeConverter lukketPeriodeConverter;

    private LukketPeriodeMedVedleggConverter lukketPeriodeMedVedleggConverter;

    public FordelingConverter(ModelMapper mapper) {
        super(mapper);
        //lukketPeriodeConverter = new LukketPeriodeConverter(mapper);
        lukketPeriodeMedVedleggConverter = new LukketPeriodeMedVedleggConverter(mapper);
    }

    @Override
    protected Fordeling convert(FordelingDto fordelingDto) {
        Fordeling fordeling = new Fordeling();
        konverterPerioder(fordelingDto, fordeling);
        konverterOverforingssaker(fordelingDto, fordeling);
        fordeling.setAnnenForelderErInformert(fordelingDto.isAnnenForelderErInformert());
        return fordeling;
    }

    private void konverterOverforingssaker(FordelingDto fordelingDto, Fordeling fordeling) {
        Optional.ofNullable(fordelingDto.getOenskerKvoteOverfoert()).ifPresent(e -> {
            Overfoeringsaarsaker overfoeringsaarsaker = mapper.map(e, Overfoeringsaarsaker.class);
            fordeling.setOenskerKvoteOverfoert(overfoeringsaarsaker);
        });
    }

    private void konverterPerioder(FordelingDto fordelingDto, Fordeling fordeling) {
        if (Optional.ofNullable(fordelingDto.getPerioder()).isPresent()) {
            fordelingDto.getPerioder()
                    .forEach(e -> {
                        fordeling.getPerioder().add(lukketPeriodeMedVedleggConverter.konverter(e));
                    });
        }
    }
}
