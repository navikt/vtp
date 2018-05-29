package no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell;

import static no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.FeilKodeKonstanter.PERSON_IKKE_FUNNET;
import static no.nav.tjeneste.virksomhet.infotrygdfelles.v1.modell.FeilKodeKonstanter.UGYLDIG_INPUT;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.mock.felles.DbLeser;

public class InfotrygdDbLeser extends DbLeser {

    public InfotrygdDbLeser(EntityManager entityManager) {
        super(entityManager);
    }

    public List<InfotrygdYtelse> finnInfotrygdYtelseMedFnr(String fnr) {
        if(fnr !=null){
            TypedQuery<InfotrygdSvar> query = entityManager.createQuery("FROM InfotrygdSvar t WHERE fnr = :fnr", InfotrygdSvar.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("fnr", fnr);
            List<InfotrygdSvar> infotrygdSvar = query.getResultList();

            List<InfotrygdYtelse> infotrygdYtelseListe = infotrygdSvar.get(0).getInfotrygdYtelseListe();
            if(!infotrygdSvar.isEmpty()) {
                List<InfotrygdYtelse> filtrertInfotrygdYtelseListe = infotrygdYtelseListe.stream()
                        .filter(ytelse -> ytelse.feedelementType == null)
                        .collect(Collectors.toList());
                if (filtrertInfotrygdYtelseListe.size() > 0) {
                    return filtrertInfotrygdYtelseListe;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public List<InfotrygdGrunnlag> finnInfotrygdGrunnlagMedFnr(String fnr) {
        if(fnr !=null){
            TypedQuery<InfotrygdSvar> query = entityManager.createQuery("FROM InfotrygdSvar t WHERE fnr = :fnr", InfotrygdSvar.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("fnr", fnr);
            List<InfotrygdSvar> infotrygdSvar = query.getResultList();

            if(infotrygdSvar != null && !infotrygdSvar.isEmpty()) {
                return infotrygdSvar.get(0).getInfotrygdGrunnlagList();
            }
        }
        return null;
    }

    public Optional<String> finnInfotrygdSvarMedFnrOpt(String fnr) {
        String feilkode;
        if (fnr != null) {
            TypedQuery<InfotrygdSvar> query = entityManager.createQuery("FROM InfotrygdSvar t WHERE fnr = :fnr", InfotrygdSvar.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("fnr", fnr);
            List<InfotrygdSvar> infotrygdSvar = query.getResultList();
            if (infotrygdSvar == null || infotrygdSvar.isEmpty()) {
                feilkode = null;
            } else {
                feilkode = infotrygdSvar.get(0).getFeilkode();
            }
        } else {
            feilkode = null;
        }
        return Optional.ofNullable(feilkode);
    }
}
