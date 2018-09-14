package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.erketyper;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.*;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.DekningsgradDto;

import java.util.List;

public class ForeldrepengeSoknadDto {
    public String fnr;

    public String annenForelder;

    public Boolean brukRenXml;

    public String renXml;

    public String brukerRolle;

    public Boolean boddINorgeSiste12Mnd;

    public Boolean borINorgeNeste12Mnd;

    public Boolean iNorgeVedFoedselstidspunkt;

    public int antallBarn;

    public FoedselDto foedsel;

    public TerminDto termin;

    public OmsorgsovertakelseDto omsorg;

    public AdopsjonDto adopsjon;

    public List<LukketPeriodeDto> perioder;

    public DekningsgradDto dekningsgrad;

    public List<VedleggDto> paakrevdeVedlegg;

    public List<VedleggDto> andreVedlegg;

    public MedlemskapDto medlemskap;

    public MedlemskapDto getMedlemskap() {
        return medlemskap;
    }

    public List<VedleggDto> getPaakrevdeVedlegg() {
        return paakrevdeVedlegg;
    }

    public void setPaakrevdeVedlegg(List<VedleggDto> paakrevdeVedlegg) {
        this.paakrevdeVedlegg = paakrevdeVedlegg;
    }

    public List<VedleggDto> getAndreVedlegg() {
        return andreVedlegg;
    }

    public void setAndreVedlegg(List<VedleggDto> andreVedlegg) {
        this.andreVedlegg = andreVedlegg;
    }

    public void setMedlemskap(MedlemskapDto medlemskap) {
        this.medlemskap = medlemskap;
    }

    public DekningsgradDto getDekningsgrad() {
        return dekningsgrad;
    }

    public void setDekningsgrad(DekningsgradDto dekningsgrad) {
        this.dekningsgrad = dekningsgrad;
    }

    public List<LukketPeriodeDto> getPerioder() {
        return perioder;
    }

    public void setPerioder(List<LukketPeriodeDto> perioder) {
        this.perioder = perioder;
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    public String getAnnenForelder() {
        return annenForelder;
    }

    public void setAnnenForelder(String annenForelder) {
        this.annenForelder = annenForelder;
    }

    public Boolean getBrukRenXml() {
        return brukRenXml;
    }

    public void setBrukRenXml(Boolean brukRenXml) {
        this.brukRenXml = brukRenXml;
    }

    public String getRenXml() {
        return renXml;
    }

    public void setRenXml(String renXml) {
        this.renXml = renXml;
    }

    public String getBrukerRolle() {
        return brukerRolle;
    }

    public void setBrukerRolle(String brukerRolle) {
        this.brukerRolle = brukerRolle;
    }

    public Boolean getBoddINorgeSiste12Mnd() {
        return boddINorgeSiste12Mnd;
    }

    public void setBoddINorgeSiste12Mnd(Boolean boddINorgeSiste12Mnd) {
        this.boddINorgeSiste12Mnd = boddINorgeSiste12Mnd;
    }

    public Boolean getBorINorgeNeste12Mnd() {
        return borINorgeNeste12Mnd;
    }

    public void setBorINorgeNeste12Mnd(Boolean borINorgeNeste12Mnd) {
        this.borINorgeNeste12Mnd = borINorgeNeste12Mnd;
    }

    public Boolean getiNorgeVedFoedselstidspunkt() {
        return iNorgeVedFoedselstidspunkt;
    }

    public void setiNorgeVedFoedselstidspunkt(Boolean iNorgeVedFoedselstidspunkt) {
        this.iNorgeVedFoedselstidspunkt = iNorgeVedFoedselstidspunkt;
    }

    public int getAntallBarn() {
        return antallBarn;
    }

    public void setAntallBarn(int antallBarn) {
        this.antallBarn = antallBarn;
    }

    public FoedselDto getFoedsel() {
        return foedsel;
    }

    public void setFoedsel(FoedselDto foedsel) {
        this.foedsel = foedsel;
    }

    public TerminDto getTermin() {
        return termin;
    }

    public void setTermin(TerminDto termin) {
        this.termin = termin;
    }

    public OmsorgsovertakelseDto getOmsorg() {
        return omsorg;
    }

    public void setOmsorg(OmsorgsovertakelseDto omsorg) {
        this.omsorg = omsorg;
    }

    public AdopsjonDto getAdopsjon() {
        return adopsjon;
    }

    public void setAdopsjon(AdopsjonDto adopsjon) {
        this.adopsjon = adopsjon;
    }
}
