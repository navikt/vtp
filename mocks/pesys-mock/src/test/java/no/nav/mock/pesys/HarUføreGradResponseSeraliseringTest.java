package no.nav.mock.pesys;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.BrukerModell;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;

@ExtendWith(MockitoExtension.class)
class HarUføreGradResponseSeraliseringTest {
    private static final String FNR = "22222233333";

    @Mock
    private TestscenarioRepository testscenarioRepository;
    @Mock
    private PersonIndeks personIndeks;

    private UføreMock uføreMock;


    @BeforeEach
    void PesysMockInit() {
        when(testscenarioRepository.getPersonIndeks()).thenReturn(personIndeks);
        uføreMock = new UføreMock(testscenarioRepository);
    }

    @Test
    void sjekkEnPeriodeNårHarUføretrygTrue() {
        var søkerModell = new SøkerModell(FNR, "Tester", LocalDate.now().minusYears(25), true, BrukerModell.Kjønn.M);
        when(personIndeks.finnByIdent(any())).thenReturn(søkerModell);
        assertThat(uføreMock.harUføreGrad(FNR).harUforegrad()).isTrue();
        assertThat(uføreMock.harUføreGrad(FNR).datoUfor()).isBefore(LocalDate.now());
    }

    @Test
    void sjekkTomPeriodeNårHarUføretrygFalse() {
        var annenPartModell = new AnnenPartModell(FNR, "Tester", LocalDate.now().minusYears(25), false, BrukerModell.Kjønn.M);
        when(personIndeks.finnByIdent(any())).thenReturn(annenPartModell);
        assertThat(uføreMock.harUføreGrad(FNR).harUforegrad()).isFalse();
    }
}
