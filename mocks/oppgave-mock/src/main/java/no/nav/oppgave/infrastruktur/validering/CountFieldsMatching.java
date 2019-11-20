package no.nav.oppgave.infrastruktur.validering;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class CountFieldsMatching {
    private CountFieldsMatching() {
    } //No instantiation

    static Long count(Object o, String[] fields) {
        return Arrays.stream(fields).filter(field -> {
            try {
                return isNotBlank(BeanUtils.getProperty(o, field));
            } catch (Exception e) {
                throw new IllegalStateException("Kunne ikke telle antall felter");
            }
        }).count();
    }
}
