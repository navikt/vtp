package no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.modell;


import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import no.nav.foreldrepenger.fpmock2.felles.DbLeser;

public class ArenaMUDLeser extends DbLeser {

    public ArenaMUDLeser(EntityManager entityManager) {
        super(entityManager);
    }

    public ArenaMUAktør finnArenaAktørId(String bruker) {
        if (bruker != null) {
            TypedQuery<ArenaMUAktør> query = entityManager.createQuery("FROM ArenaMUAktør WHERE IDENT = :ident", ArenaMUAktør.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("ident", bruker);
            List<ArenaMUAktør> arenaSvar = query.getResultList();
            if (arenaSvar != null && !arenaSvar.isEmpty()) {
                return arenaSvar.get(0);
            }
        }
        return null;
    }


    public Optional<String> finnArenaAktørFeilkode(String bruker) {
        String feilkode;
        if (bruker != null) {
            TypedQuery<ArenaMUAktør> query = entityManager.createQuery("FROM ArenaMUAktør WHERE IDENT = :ident", ArenaMUAktør.class); //$NON-NLS-1$ //$NON-NLS-1$
            query.setParameter("ident", bruker);
            List<ArenaMUAktør> arenaSvar = query.getResultList();
            if (arenaSvar == null || arenaSvar.isEmpty()) {
                feilkode = null;
            } else {
                feilkode = arenaSvar.get(0).getFeilkode();
            }
        } else {
            feilkode = null;
        }
        return Optional.ofNullable(feilkode);
    }

}

