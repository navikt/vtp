package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

public interface BegrunnelseForSenSoeknadStep <T extends BegrunnelseForSenSoeknadStep<T>>{
    T withBegrunnelseForSenSoeknad(String begrunnelseForSenSoeknad);
}
