package no.nav.foreldrepenger.vtp.testmodell.medlemskap;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonTypeName;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Landkode;

/**
 * Medlemskapperiode med defaults, de kan overstyrs av json struktur hvis satt
 */
@JsonTypeName("medlemskapperiode")
public record MedlemskapperiodeModell(Long id,
                                      LocalDate fom,
                                      LocalDate tom,
                                      LocalDate besluttetDato,
                                      Landkode land,
                                      DekningType trygdedekning,
                                      MedlemskapKildeType kilde,
                                      LovvalgType lovvalgType,
                                      PeriodeStatus status) {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(100000000);

    public MedlemskapperiodeModell {
        id = Optional.ofNullable(id).orElse(ID_GENERATOR.getAndIncrement());
        fom = Optional.ofNullable(fom).orElse(LocalDate.now().minusYears(1));
        tom = Optional.ofNullable(tom).orElse(LocalDate.now().plusYears(3));
        besluttetDato = Optional.ofNullable(besluttetDato).orElse(LocalDate.now().minusYears(1));
        land = Optional.ofNullable(land).orElse(Landkode.DEU); // EØS land
        trygdedekning = Optional.ofNullable(trygdedekning).orElse(DekningType.IHT_AVTALE); // setter til en uavklart kode default.
        kilde = Optional.ofNullable(kilde).orElse(MedlemskapKildeType.ANNEN);
        lovvalgType = Optional.ofNullable(lovvalgType).orElse(LovvalgType.ENDL);
        status = Optional.ofNullable(status).orElse(PeriodeStatus.UAVK);
    }
}
