package no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.modell;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import no.nav.foreldrepenger.mock.felles.DbLeser;

public class ArenaDbLeser extends DbLeser {

    public ArenaDbLeser(EntityManager entityManager) {
        super(entityManager);
    }

    public List<ArenaYtelseskontrakt> finnArenaYtelseskontraktMedFnr(String fnr) {
        if(fnr !=null){
            TypedQuery<ArenaSvar> query = entityManager.createQuery("FROM ArenaSvar t WHERE fnr = :fnr", ArenaSvar.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("fnr", fnr);
            List<ArenaSvar> arenaSvar = query.getResultList();

            if(arenaSvar != null && !arenaSvar.isEmpty()) {
                return arenaSvar.get(0).getYtelseskontraktListe();
            }
        }
        return null;
    }

    public List<ArenaVedtak> finnArenaVedtakMedYtelseId(Long idYtelse) {
        if (idYtelse != null) {
            TypedQuery<ArenaVedtak> query = entityManager.createQuery("FROM ArenaVedtak t WHERE id_ytelseskontrakt = :idYtelse", ArenaVedtak.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("idYtelse", idYtelse);
            List<ArenaVedtak> arenaVedtak = query.getResultList();

            if(arenaVedtak != null && !arenaVedtak.isEmpty()) {
                return arenaVedtak;
            }
        }
        return null;
    }

    public ArenaSvar finnArenaSvarMedFnr(String fnr) {

            if (fnr != null) {
                TypedQuery<ArenaSvar> query = entityManager.createQuery("FROM ArenaSvar t WHERE fnr = :fnr", ArenaSvar.class); //$NON-NLS-1$ //$NON-NLS-1$
                query.setParameter("fnr", fnr);
                ArenaSvar arenaSvar = query.getResultList().get(0);
                return arenaSvar;
            }
        return null;
    }
}

