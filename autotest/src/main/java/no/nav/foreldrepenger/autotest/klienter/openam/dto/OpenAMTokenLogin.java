package no.nav.foreldrepenger.autotest.klienter.openam.dto;

import java.util.ArrayList;
import java.util.List;

public class OpenAMTokenLogin {
    public String authId;
    public String template = "";
    public String stage = "DataStore1";
    public String header = "Sign in to OpenAM";
    public List<CallBack> callbacks = new ArrayList<>();

    public OpenAMTokenLogin(String authId, String username, String password) {
        this.authId = authId;
        CallBack nameCallback = new CallBack("NameCallback");
        nameCallback.output.add(new InputOutput("prompt", "User Name:"));
        nameCallback.input.add(new InputOutput("IDToken1", username));
        callbacks.add(nameCallback);

        CallBack passwordCallback = new CallBack("PasswordCallback");
        passwordCallback.output.add(new InputOutput("prompt", "Password:"));
        passwordCallback.input.add(new InputOutput("IDToken2", password));
        callbacks.add(passwordCallback);
    }
}