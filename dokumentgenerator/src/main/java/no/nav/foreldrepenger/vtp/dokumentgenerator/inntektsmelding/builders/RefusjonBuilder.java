package no.nav.foreldrepenger.vtp.dokumentgenerator.inntektsmelding.builders;

import no.seres.xsd.nav.inntektsmelding_m._20181211.ObjectFactory;
import no.seres.xsd.nav.inntektsmelding_m._20181211.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class RefusjonBuilder {
    ObjectFactory objectFactory = new ObjectFactory();
    Refusjon refusjonKladd;

    RefusjonBuilder() {
        refusjonKladd = objectFactory.createRefusjon();
    }
    RefusjonBuilder medRefusjonsBelopPerMnd(BigDecimal refusjonsBelopPerMnd) {
        refusjonKladd.setRefusjonsbeloepPrMnd(
                objectFactory.createRefusjonRefusjonsbeloepPrMnd(refusjonsBelopPerMnd));
        return this;
    }
    RefusjonBuilder medEndringIRefusjonslist(List<EndringIRefusjon> endringIRefusjonList) {
        if (endringIRefusjonList != null && endringIRefusjonList.size() > 0) {
            EndringIRefusjonsListe endringIRefusjonsListe = objectFactory.createEndringIRefusjonsListe();
            endringIRefusjonsListe.getEndringIRefusjon().addAll(endringIRefusjonList);
            refusjonKladd.setEndringIRefusjonListe(
                    objectFactory.createRefusjonEndringIRefusjonListe(endringIRefusjonsListe)
            );
        }
        return this;
    }
    RefusjonBuilder medEndringIRefusjonslist(Map<LocalDate, BigDecimal> endringRefusjonMap) {
       medEndringIRefusjonslist(endringRefusjonMap.entrySet().stream().map(
               entry -> createEndringIRefusjon(entry.getKey(), entry.getValue())
       ).collect(Collectors.toList()));
        return this;
    }
    RefusjonBuilder medRefusjonsOpphordato(LocalDate refusjonsOpphordato) {
        refusjonKladd.setRefusjonsopphoersdato(objectFactory.createRefusjonRefusjonsopphoersdato(refusjonsOpphordato));
        return this;
    }

    Refusjon build() {
        Objects.requireNonNull(refusjonKladd.getRefusjonsbeloepPrMnd(), "refusjonsBelopPerMnd kan ikke være null");
        return refusjonKladd;
    }

    private EndringIRefusjon createEndringIRefusjon(LocalDate endringsdato, BigDecimal refusjonsbeloepPrMnd) {
        ObjectFactory objectFactory = new ObjectFactory();
        EndringIRefusjon endringIRefusjon = objectFactory.createEndringIRefusjon();
        endringIRefusjon.setEndringsdato(objectFactory.createEndringIRefusjonEndringsdato(endringsdato));
        endringIRefusjon.setRefusjonsbeloepPrMnd(objectFactory.createEndringIRefusjonRefusjonsbeloepPrMnd(refusjonsbeloepPrMnd));
        return endringIRefusjon;
    }

}
