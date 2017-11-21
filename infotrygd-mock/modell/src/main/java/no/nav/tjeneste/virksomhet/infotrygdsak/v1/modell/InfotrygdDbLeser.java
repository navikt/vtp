package no.nav.tjeneste.virksomhet.infotrygdsak.v1.modell;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

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

            if(!infotrygdSvar.isEmpty() && infotrygdSvar != null) {
                return infotrygdSvar.get(0).getInfotrygdYtelseListe();
            }
        }
        return null;
    }

    public List<InfotrygdSvar> finnInfotrygdSvarMedFnr(String fnr) {
        if (fnr != null) {
            TypedQuery<InfotrygdSvar> query = entityManager.createQuery("FROM InfotrygdSvar t WHERE fnr = :fnr", InfotrygdSvar.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("fnr", fnr);
            List<InfotrygdSvar> infotrygdSvar = query.getResultList();

            return infotrygdSvar;
        }
        return null;
    }
}
