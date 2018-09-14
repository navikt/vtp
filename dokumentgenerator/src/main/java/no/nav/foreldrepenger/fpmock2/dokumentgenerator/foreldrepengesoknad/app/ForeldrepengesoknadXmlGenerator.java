package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.SoeknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.erketyper.ForeldrepengeSoknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.felles.v1.*;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.foreldrepenger.v1.ForeldrepengerDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.FordelingDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.uttak.v1.UttaksperiodeDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.mapper.*;
import no.nav.vedtak.felles.xml.soeknad.felles.v1.Vedlegg;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v1.Innsendingstype;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Random;

public class ForeldrepengesoknadXmlGenerator implements JournalforSoknadTjeneste {


    private ForeldrepengesoknadProsessering soknadsProsessering;

    public ForeldrepengesoknadXmlGenerator() {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(new LocalDateToXmlGregorianCalender(mapper));
        mapper.addConverter(new SoekersRelasjonTilBarnetConverter(mapper));
        mapper.addConverter(new MedlemskapConverter(mapper));
        mapper.addConverter(new EgenNaeringConverter(mapper));
        mapper.addConverter(new AnnenForelderConverter(mapper));
        mapper.addConverter(new FordelingConverter(mapper));
        mapper.addConverter(new OpptjeningConverter(mapper));
        soknadsProsessering = new ForeldrepengesoknadProsessering(mapper);
    }

    @Override
    public String lagXml(SoeknadDto soknad) throws InputValideringException {
        soknadsProsessering.validerSøknad(soknad);
        return soknadsProsessering.konvertTilXML(soknad);
    }

    @Override
    public String lagXmlFraForeldrepengeDto(ForeldrepengeSoknadDto foreldrepengeSoknadDto, String aktoerid) throws InputValideringException {
        return lagXmlFraForeldrepengeDto(foreldrepengeSoknadDto, aktoerid, null);
    }

    @Override
    public String lagXmlFraForeldrepengeDto(ForeldrepengeSoknadDto soknadDto, String aktoerId, String aktoeridAnnenForelder){
        if(soknadDto.getFnr() == null || soknadDto.getFnr().isEmpty()){
            throw new IllegalArgumentException("Må oppgi fødselsnummer for hovedsøker for å mappe til aktørid");
        }


        SoeknadDto soknad = new SoeknadDto();
        soknad.setMottattDato(LocalDate.now());
        BrukerDto brukerDto = new BrukerDto();
        brukerDto.setAktoerId(aktoerId);
        brukerDto.setFullmektig(null);

        BrukerrollerDto brukerrollerDto = new BrukerrollerDto();
        brukerrollerDto.setKode(soknadDto.getBrukerRolle());
        brukerDto.setSoeknadsrolle(brukerrollerDto);
        soknad.setSoeker(brukerDto);

        ForeldrepengerDto ytelseDto = new ForeldrepengerDto();

        if(null != aktoeridAnnenForelder){
            AnnenForelderMedNorskIdentDto annenForelderMedNorskIdent = new AnnenForelderMedNorskIdentDto();
            annenForelderMedNorskIdent.setAktoerId(aktoeridAnnenForelder);
            ytelseDto.setAnnenForelder(annenForelderMedNorskIdent);
        }



        RettigheterDto rettigheterDto = new RettigheterDto();
        rettigheterDto.setHarAleneomsorgForBarnet(false);
        rettigheterDto.setHarAnnenForelderRett(true);
        rettigheterDto.setHarOmsorgForBarnetIPeriodene(true);

        ytelseDto.setRettigheter(rettigheterDto);

        if(soknadDto.getFoedsel() != null){
            FoedselDto foedselDto = new FoedselDto();
            foedselDto.setAntallBarn(soknadDto.getAntallBarn());
            foedselDto.setFoedselsdato(soknadDto.getFoedsel().getFoedselsdato());
            ytelseDto.setRelasjonTilBarnet(foedselDto);
        }

        if(soknadDto.getAdopsjon() != null){
            ytelseDto.setRelasjonTilBarnet(soknadDto.getAdopsjon());
            //todo håndter vedlegg
        }

        if(soknadDto.getTermin() != null){
            ytelseDto.setRelasjonTilBarnet(soknadDto.getTermin());
            //todo håndter vedlegg
        }

        if (null != soknadDto.getOmsorg()) {
            ytelseDto.setRelasjonTilBarnet(soknadDto.getOmsorg());
        }

        ytelseDto.setMedlemskap(soknadDto.getMedlemskap());

        FordelingDto fordelingDto = new FordelingDto();
        fordelingDto.setAnnenForelderErInformert(true); // denne er feil ift Svedman sin variant, mangler perioder

        soknadDto.getPerioder().stream().forEach(periode -> {
            if(periode.getClass() == UttaksperiodeDto.class){
                UttaksperiodeDto uttaksperiodeDto = (UttaksperiodeDto) periode;
                if(null == uttaksperiodeDto.getMorsAktivitetIPerioden().getKode() || "INGEN".equalsIgnoreCase(uttaksperiodeDto.getMorsAktivitetIPerioden().getKode())){
                    uttaksperiodeDto.setMorsAktivitetIPerioden(null);
                }
                fordelingDto.getPerioder().add(uttaksperiodeDto);
            } else {
                fordelingDto.getPerioder().add(periode);
            }
        });

        if (soknadDto.getAndreVedlegg() != null && !soknadDto.getAndreVedlegg().isEmpty()) {
            soknadDto.getAndreVedlegg().forEach(vedlegg -> {
                soknad.getAndreVedlegg().add(mapVedleggDtoTilVedlegg(vedlegg));
            });
        }

        if (soknadDto.getPaakrevdeVedlegg() != null && !soknadDto.getPaakrevdeVedlegg().isEmpty()) {
            soknadDto.getPaakrevdeVedlegg().forEach(vedlegg -> {
                soknad.getPaakrevdeVedlegg().add(mapVedleggDtoTilVedlegg(vedlegg));
            });
        }


        ytelseDto.setFordeling(fordelingDto);
        ytelseDto.setDekningsgrad(soknadDto.getDekningsgrad());

        soknad.setOmYtelse(ytelseDto);

        return soknadsProsessering.konvertTilXML(soknad);
    }


    private Vedlegg mapVedleggDtoTilVedlegg(VedleggDto vedlegg){
        Vedlegg v = new Vedlegg();
        Innsendingstype innsendingstype = new Innsendingstype();
        innsendingstype.setKode(vedlegg.getInnsendingstype().getKode());
        innsendingstype.setKodeverk(vedlegg.getInnsendingstype().getKodeverk());
        v.setInnsendingstype(innsendingstype);
        v.setId(randomString(7));
        v.setSkjemanummer(vedlegg.getSkjemanummer());
        if(vedlegg.getTilleggsinformasjon() != null) {
            v.setTilleggsinformasjon(vedlegg.getTilleggsinformasjon());
        } else {
            v.setTilleggsinformasjon("Placeholder mens paakrevet");
        }

        return v;
    }

    private String randomString(int length){
        Random rand = new Random();
        String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            sb.append(AB.charAt(rand.nextInt(AB.length())));
        }
        return sb.toString();
    }

}
