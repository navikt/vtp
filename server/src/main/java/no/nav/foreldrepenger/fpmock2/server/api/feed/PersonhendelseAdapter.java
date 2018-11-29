package no.nav.foreldrepenger.fpmock2.server.api.feed;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.fpmock2.testmodell.feed.FødselsmeldingOpprettetHendelse;
import no.nav.foreldrepenger.fpmock2.testmodell.feed.PersonHendelse;
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


    public PersonHendelse fra(PersonhendelseDto personhendelseDto) {
        if(personhendelseDto instanceof FødselshendelseDto){
            return fødselshendelseFra((FødselshendelseDto) personhendelseDto);
        }
        return null;
    }

    private PersonHendelse fødselshendelseFra(FødselshendelseDto fødselshendelseDto){
        FødselsmeldingOpprettetHendelse.Builder builder = new FødselsmeldingOpprettetHendelse.Builder();
        if(erSatt(fødselshendelseDto.getFnrBarn())){
            builder.setPersonIdenterBarn(fødselshendelseDto.getFnrBarn(),aktørIdFraFnr(fødselshendelseDto.getFnrBarn()));
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

    private String aktørIdFraFnr(String fnr){
        return testscenarioRepository.getPersonIndeks().finnByIdent(fnr).getAktørIdent();
    }


}
