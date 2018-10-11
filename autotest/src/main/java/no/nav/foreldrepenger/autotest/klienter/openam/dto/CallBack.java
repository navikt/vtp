package no.nav.foreldrepenger.autotest.klienter.openam.dto;

import java.util.ArrayList;
import java.util.List;

public class CallBack {
    public String type;
    public List<InputOutput> output = new ArrayList<>();
    public List<InputOutput> input = new ArrayList<>();

    public CallBack(String type) {
        this.type = type;
    }
}
