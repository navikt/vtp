package no.nav.tjeneste.fpformidling.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public class HentBrevmalerDto {
    @NotNull
    private List<BrevmalDto> brevmalDtoListe;

    public HentBrevmalerDto() {
    }

    public HentBrevmalerDto(List<BrevmalDto> brevmalDtoListe) {
        this.brevmalDtoListe = brevmalDtoListe;
    }

    public List<BrevmalDto> getBrevmalDtoListe() {
        return brevmalDtoListe;
    }
}
