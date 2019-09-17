package no.nav.foreldrepenger.vtp.server.api.feed;

import java.io.IOException;

import no.nav.foreldrepenger.vtp.kontrakter.DødfødselhendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.vtp.kontrakter.PersonhendelseDto;
import no.nav.foreldrepenger.vtp.testmodell.feed.DødfødselOpprettetHendelseContent;
import no.nav.foreldrepenger.vtp.testmodell.feed.DødsmeldingOpprettetHendelseContent;
import no.nav.foreldrepenger.vtp.testmodell.feed.FødselsmeldingOpprettetHendelseContent;
import no.nav.foreldrepenger.vtp.testmodell.feed.HendelseContent;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;

public class PersonhendelseAdapter {

    TestscenarioRepositoryImpl testscenarioRepository;

    public PersonhendelseAdapter(){
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public HendelseContent fra(PersonhendelseDto personhendelseDto) {
        if (personhendelseDto instanceof FødselshendelseDto) {
            return fødselshendelseFra((FødselshendelseDto) personhendelseDto);
        }
        if (personhendelseDto instanceof DødshendelseDto) {
            return dødshendelseFra((DødshendelseDto) personhendelseDto);
        }
        if (personhendelseDto instanceof DødfødselhendelseDto) {
            return dødfødselhendelseFra((DødfødselhendelseDto) personhendelseDto);
        }
        return null;
    }

    private HendelseContent dødfødselhendelseFra(DødfødselhendelseDto dødfødselhendelseDto) {
        DødfødselOpprettetHendelseContent.Builder builder = new DødfødselOpprettetHendelseContent.Builder();

        if (erSatt(dødfødselhendelseDto.getFnr())) {
            builder.setIdenter(dødfødselhendelseDto.getFnr(), aktørIdFraFnr(dødfødselhendelseDto.getFnr()));
        }
        if (erSatt(dødfødselhendelseDto.getDoedfoedselsdato())) {
            builder.setDoedfoedselsdato(dødfødselhendelseDto.getDoedfoedselsdato());
        }
        return builder.build();
    }

    private HendelseContent dødshendelseFra(DødshendelseDto dødshendelseDto) {
        DødsmeldingOpprettetHendelseContent.Builder builder = new DødsmeldingOpprettetHendelseContent.Builder();

        if(erSatt(dødshendelseDto.getFnr())){
            builder.setIdenter(dødshendelseDto.getFnr(),aktørIdFraFnr(dødshendelseDto.getFnr()));
        }
        if(erSatt(dødshendelseDto.getDoedsdato())){
            builder.setDoedsdato(dødshendelseDto.getDoedsdato());
        }
        return builder.build();
    }

    private HendelseContent fødselshendelseFra(FødselshendelseDto fødselshendelseDto){
        FødselsmeldingOpprettetHendelseContent.Builder builder = new FødselsmeldingOpprettetHendelseContent.Builder();
        if(erSatt(fødselshendelseDto.getFnrBarn())){
            builder.setPersonIdenterBarn(fødselshendelseDto.getFnrBarn(),genererAktørIdForBarn(fødselshendelseDto.getFnrBarn()));
        }
        if(erSatt(fødselshendelseDto.getFnrMor())){
            builder.setPersonIdenterMor(fødselshendelseDto.getFnrMor(),aktørIdFraFnr(fødselshendelseDto.getFnrMor()));
        }
        if(erSatt(fødselshendelseDto.getFnrFar())){
            builder.setPersonIdenterFar(fødselshendelseDto.getFnrFar(),aktørIdFraFnr(fødselshendelseDto.getFnrFar()));
        }
        if(erSatt(fødselshendelseDto.getFødselsdato())){
            builder.setFoedselsdato(fødselshendelseDto.getFødselsdato());
        }
        return builder.build();
    }

    private boolean erSatt(Object object) {
        if (object == null) return false;
        if (object instanceof String && ((String) object).length() == 0) {
            return false;
        }
        return true;
    }
    //Foreløpig generering av aktør id for nytt barn. Bør dette barnet legges til i testscenariet?
    private String genererAktørIdForBarn(String fnr){
        return "00" + fnr;
    }

    private String aktørIdFraFnr(String fnr){
        return testscenarioRepository.getPersonIndeks().finnByIdent(fnr).getAktørIdent();
    }
}
