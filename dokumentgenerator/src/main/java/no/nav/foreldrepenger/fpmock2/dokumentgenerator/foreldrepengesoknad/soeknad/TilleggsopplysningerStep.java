package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad;

public interface TilleggsopplysningerStep <T extends TilleggsopplysningerStep<T>>{
    T withTilleggsopplysninger(String tilleggsopplysninger);
}
