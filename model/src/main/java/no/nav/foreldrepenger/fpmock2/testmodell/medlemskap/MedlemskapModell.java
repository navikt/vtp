package no.nav.foreldrepenger.fpmock2.testmodell.medlemskap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("meldemskap")
public class MedlemskapModell {

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("perioder")
    private List<MedlemskapperiodeModell> perioder = new ArrayList<>();

    public void leggTil(MedlemskapperiodeModell medlemskapperiode) {
        // TODO: sjekk overlapp?
        this.perioder.add(medlemskapperiode);
    }
    
    public List<MedlemskapperiodeModell> getPerioder() {
        return Collections.unmodifiableList(perioder);
    }

}
