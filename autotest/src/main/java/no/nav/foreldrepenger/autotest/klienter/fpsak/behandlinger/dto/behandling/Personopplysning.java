package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

public class Personopplysning {
    public int id;
    public List<Adresse> adresser;
    public long aktoerId;
    public Object annenPart;
    public List<Personopplysning> barn;
    public boolean bekreftetAvTps;
    public Kode diskresjonskode;
    public LocalDate doedsdato;
    public LocalDate foedselsdato;
    public String foedselsnr;
    public Kode navBrukerKjonn;
    public String navn;
    public Object nummer;
    public Kode personstatus;
    public Kode region;
    public Kode siviltilstand;
    public Object valgtOpplysning;
}
