package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.OpptjeningDto;
import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Periode;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.AnnenOpptjening;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.Opptjening;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v1.UtenlandskArbeidsforhold;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.AnnenOpptjeningTyper;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Land;
import org.modelmapper.ModelMapper;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.List;

public class OpptjeningConverter extends DtoMapper<OpptjeningDto, Opptjening>{

    public OpptjeningConverter(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    protected Opptjening convert(OpptjeningDto opptjeningDto) {

        Opptjening opptjening = new Opptjening();
        List<AnnenOpptjening> annenOpptjeningListe = new ArrayList<>();
        List<UtenlandskArbeidsforhold> utenlandskArbeidsforholds = new ArrayList<>();
        AnnenOpptjening annenOpptjening = new AnnenOpptjening();

        if(null == opptjeningDto){
            return null;
        }

        if (null != opptjeningDto.getAnnenOpptjening() && !opptjeningDto.getAnnenOpptjening().isEmpty()) {
            opptjeningDto.getAnnenOpptjening().forEach(opp -> {
                Periode per = new Periode();

                AnnenOpptjeningTyper annenOpptjeningTyper = new AnnenOpptjeningTyper();
                annenOpptjeningTyper.setKode(opp.getType().getKode());
                annenOpptjeningTyper.setKodeverk(opp.getType().getKodeverk());
                //annenOpptjeningTyper.setValue(opp.getType().getValue());
                annenOpptjening.setType(annenOpptjeningTyper);

                try {
                    per.setFom(DateUtil.convertToXMLGregorianCalendar(opp.getPeriode().getFom()));
                    per.setTom(DateUtil.convertToXMLGregorianCalendar(opp.getPeriode().getTom()));
                    annenOpptjening.setPeriode(per);
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }

                opptjening.getAnnenOpptjening().add(annenOpptjening);
            });
        }


        if (null != opptjeningDto.getUtenlandskArbeidsforhold()) {
            opptjeningDto.getUtenlandskArbeidsforhold().forEach(arbeid -> {

                    Periode periode = new Periode();
                    UtenlandskArbeidsforhold utenlandskArbeidsforhold = new UtenlandskArbeidsforhold();
                    try {
                        periode.setFom(DateUtil.convertToXMLGregorianCalendar(arbeid.getPeriode().getFom()));
                        periode.setTom(DateUtil.convertToXMLGregorianCalendar(arbeid.getPeriode().getTom()));
                        utenlandskArbeidsforhold.setPeriode(periode);
                    } catch (DatatypeConfigurationException e) {
                        e.printStackTrace();
                    }

                    utenlandskArbeidsforhold.setArbeidsgiversnavn(arbeid.getArbeidsgiversnavn());
                    Land land = new Land();
                    land.setKode(arbeid.getArbeidsland().getKode());
                    land.setKodeverk(arbeid.getArbeidsland().getKodeverk());
                    utenlandskArbeidsforhold.setArbeidsland(land);
                    opptjening.getUtenlandskArbeidsforhold().add(utenlandskArbeidsforhold);


            });

        }

        return opptjening;
    }
}
