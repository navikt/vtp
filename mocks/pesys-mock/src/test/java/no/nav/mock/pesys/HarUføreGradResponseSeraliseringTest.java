package no.nav.mock.pesys;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import no.nav.vtp.PersonBuilder;
import no.nav.vtp.person.PersonRepository;
import no.nav.vtp.person.ytelse.Ytelse;
import no.nav.vtp.person.ytelse.YtelseType;

class HarUføreGradResponseSeraliseringTest {

    private PersonRepository personRepository;
    private UføreMock uføreMock;

    @BeforeEach
    void setup() {
        personRepository = new PersonRepository();
        uføreMock = new UføreMock();
    }

    @Test
    void sjekkEnPeriodeNårHarUføretrygTrue() {
        var søker = PersonBuilder.lagSøker();
        var søkerMedUføre = søker.tilBuilder()
                .medYtelser(List.of(new Ytelse(
                        YtelseType.UFØREPENSJON,
                        LocalDate.now().minusYears(1),
                        LocalDate.now().plusYears(1),
                        0, 0, List.of()
                )))
                .build();
        personRepository.leggTilPerson(søkerMedUføre);
        setPersonRepositoryOnMock();

        var ident = søkerMedUføre.personopplysninger().identifikator().value();
        assertThat(uføreMock.harUføreGrad(ident).harUforegrad()).isTrue();
        assertThat(uføreMock.harUføreGrad(ident).datoUfor()).isBefore(LocalDate.now());
    }

    @Test
    void sjekkTomPeriodeNårHarUføretrygFalse() {
        var annenPart = PersonBuilder.lagAnnenPart();
        personRepository.leggTilPerson(annenPart);
        setPersonRepositoryOnMock();

        var ident = annenPart.personopplysninger().identifikator().value();
        assertThat(uføreMock.harUføreGrad(ident).harUforegrad()).isFalse();
    }

    private void setPersonRepositoryOnMock() {
        try {
            var field = UføreMock.class.getDeclaredField("personRepository");
            field.setAccessible(true);
            field.set(uføreMock, personRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
