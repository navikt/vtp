package no.nav.tjeneste.virksomhet.medlemskap.v2.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.foreldrepenger.mock.felles.DbLeser;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.Medlemsperiode;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.Studieinformasjon;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.GrunnlagstypeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.KildeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.LandkodeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.LovvalgMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.PeriodetypeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.StatuskodeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.TrygdedekningMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.modell.Medlemsskapsperiode;

public class MedlemDbLeser extends DbLeser {

    public MedlemDbLeser(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Medlemsperiode> finnMedlemsperioder(String fnr) {
        if (fnr != null) {
            List<Medlemsskapsperiode> medlemsperiodeList = entityManager.createQuery("FROM Medlemsskapsperiode WHERE fnr = :fnr", Medlemsskapsperiode.class)
                    .setParameter("fnr", fnr)
                    .getResultList();
            if (!medlemsperiodeList.isEmpty() && medlemsperiodeList.get(0) != null) {
                List<Medlemsperiode> periodeListe = new ArrayList<>();
                medlemsperiodeList.forEach(medlemsskapsperiode -> periodeListe.add(tilMedlemsperiode(medlemsskapsperiode)));
                return periodeListe;
            }
        }
        return null;
    }

    private Medlemsperiode tilMedlemsperiode(Medlemsskapsperiode medlemsskapsperiode) {
        return new Medlemsperiode()
                .withId(medlemsskapsperiode.getId())
                .withFraOgMed(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getDatoFom()))
                .withTilOgMed(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getDatoTom()))
                .withDatoBesluttet(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getDatoBesluttet()))
                .withTrygdedekning(new TrygdedekningMedTerm().withValue(medlemsskapsperiode.getDekning()))
                .withType(new PeriodetypeMedTerm().withValue(medlemsskapsperiode.getType()))
                .withLovvalg(new LovvalgMedTerm().withValue(medlemsskapsperiode.getLovvalg()))
                .withLand(new LandkodeMedTerm().withValue(medlemsskapsperiode.getLand()))
                .withKilde(new KildeMedTerm().withValue(medlemsskapsperiode.getKilde()))
                .withStatus(new StatuskodeMedTerm().withValue(medlemsskapsperiode.getStatus()))
                .withGrunnlagstype(new GrunnlagstypeMedTerm().withValue(medlemsskapsperiode.getGrunnlag()))
                .withStudieinformasjon(new Studieinformasjon()
                        .withStatsborgerland(new LandkodeMedTerm().withValue(medlemsskapsperiode.getStudieStatsborgerland()))
                        .withStudieland(new LandkodeMedTerm().withValue(medlemsskapsperiode.getStudieStudieland())));
    }
}
