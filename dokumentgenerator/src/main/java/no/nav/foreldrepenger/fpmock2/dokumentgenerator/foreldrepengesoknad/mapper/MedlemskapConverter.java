package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.MedlemskapDto;
import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Medlemskap;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.OppholdNorge;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.OppholdUtlandet;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Periode;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Land;
import org.modelmapper.ModelMapper;

import javax.xml.datatype.DatatypeConfigurationException;

public class MedlemskapConverter extends DtoMapper<MedlemskapDto, Medlemskap>{
    public MedlemskapConverter(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected Medlemskap convert(MedlemskapDto medlemskapDto) {
        if(medlemskapDto == null){
            return null;
        }

        if (medlemskapDto instanceof MedlemskapDto) {
            MedlemskapDto dto = (MedlemskapDto) medlemskapDto;
            //Medlemskap map = mapper.map(dto, Medlemskap.class);

            Medlemskap medl = new Medlemskap();

            medl.setBoddINorgeSiste12Mnd(medlemskapDto.getBoddINorgeSiste12Mnd());
            medl.setBorINorgeNeste12Mnd(medlemskapDto.getBorINorgeNeste12Mnd());
            medl.setINorgeVedFoedselstidspunkt(medlemskapDto.getiNorgeVedFoedselstidspunkt());

            medlemskapDto.getOppholdNorge().stream().forEach(oppholdNorgeDto -> {
                OppholdNorge ophn = new OppholdNorge();
                Periode per = new Periode();
                try {
                    per.setTom(DateUtil.convertToXMLGregorianCalendar(oppholdNorgeDto.getPeriode().getTom()));
                    per.setFom(DateUtil.convertToXMLGregorianCalendar(oppholdNorgeDto.getPeriode().getFom()));
                    ophn.setPeriode(per);
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }
                medl.getOppholdNorge().add(ophn);
            });

            medlemskapDto.getOppholdUtlandet().stream().forEach(oppholdUtlandetDto -> {
                OppholdUtlandet ophu = new OppholdUtlandet();
                Periode per = new Periode();

                try {
                    per.setFom(DateUtil.convertToXMLGregorianCalendar(oppholdUtlandetDto.getPeriode().getFom()));
                    per.setTom(DateUtil.convertToXMLGregorianCalendar(oppholdUtlandetDto.getPeriode().getTom()));
                    ophu.setPeriode(per);
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }

                Land land = new Land();
                land.setKode(oppholdUtlandetDto.getLand().getKode());
                land.setKodeverk(oppholdUtlandetDto.getLand().getKodeverk());
                ophu.setLand(land);

                medl.getOppholdUtlandet().add(ophu);
            });

            return medl;
        }

        throw new RuntimeException("Fant ingen passende impl for " + medlemskapDto.getClass());
    }
}
