package no.nav.foreldrepenger.fpmock2.server.api.feed;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.fpmock2.kontrakter.DødshendelseDto;
import no.nav.foreldrepenger.fpmock2.kontrakter.FødselshendelseDto;
import no.nav.foreldrepenger.fpmock2.kontrakter.PersonhendelseDto;
import no.nav.foreldrepenger.fpmock2.testmodell.feed.DødsmeldingOpprettetHendelseContent;
import no.nav.foreldrepenger.fpmock2.testmodell.feed.FødselsmeldingOpprettetHendelseContent;
import no.nav.foreldrepenger.fpmock2.testmodell.feed.HendelseContent;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.fpmock2.testmodell.repo.impl.TestscenarioRepositoryImpl;

public class PersonhendelseAdapter {

    ObjectMapper objectMapper = new ObjectMapper();
    TestscenarioRepositoryImpl testscenarioRepository;

    public PersonhendelseAdapter(){
        try {
            testscenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


    public HendelseContent fra(PersonhendelseDto personhendelseDto) {
        if(personhendelseDto instanceof FødselshendelseDto){
            return fødselshendelseFra((FødselshendelseDto) personhendelseDto);
        }
        if(personhendelseDto instanceof DødshendelseDto){
            return dødshendelseFra((DødshendelseDto) personhendelseDto);
        }
        return null;
    }

    private HendelseContent dødshendelseFra(DødshendelseDto dødshendelseDto) {
        DødsmeldingOpprettetHendelseContent.Builder builder = new DødsmeldingOpprettetHendelseContent.Builder();

        if(erSatt(dødshendelseDto.getFnr())){
            builder.setPersonIdenter(dødshendelseDto.getFnr(),aktørIdFraFnr(dødshendelseDto.getFnr()));
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
