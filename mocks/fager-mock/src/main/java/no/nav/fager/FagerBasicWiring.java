package no.nav.fager;

import graphql.schema.idl.RuntimeWiring;
import no.nav.fager.graphql.DateScalar;
import no.nav.fager.graphql.DateTimeScalar;
import no.nav.fager.graphql.DurationScalar;
import no.nav.fager.graphql.LocalDateTimeScalar;

public class FagerBasicWiring {

    public static RuntimeWiring lagBasicWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(DateScalar.DATE)
                .scalar(DateTimeScalar.ISO_8601_DATE_TIME)
                .scalar(LocalDateTimeScalar.ISO_8601_LOCAL_DATE_TIME)
                .scalar(DurationScalar.ISO_8601_DURATION)
                .type("Error", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof DuplikatGrupperingsid) {
                                        return env.getSchema().getObjectType("DuplikatGrupperingsid");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("NySakResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else if (javaObject instanceof DuplikatGrupperingsid) {
                                        return env.getSchema().getObjectType("DuplikatGrupperingsid");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("NyOppgaveResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NyOppgaveVellykket) {
                                        return env.getSchema().getObjectType("NyOppgaveVellykket");
                                    } else if (javaObject instanceof DuplikatEksternIdOgMerkelapp) {
                                        return env.getSchema().getObjectType("DuplikatEksternIdOgMerkelapp");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("NyBeskjedResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NyBeskjedVellykket) {
                                        return env.getSchema().getObjectType("NyBeskjedVellykket");
                                    } else if (javaObject instanceof DuplikatEksternIdOgMerkelapp) {
                                        return env.getSchema().getObjectType("DuplikatEksternIdOgMerkelapp");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("OppgaveUtgaattResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof OppgaveUtgaattVellykket) {
                                        return env.getSchema().getObjectType("OppgaveUtgaattVellykket");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("OppgaveUtfoertResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof OppgaveUtfoertVellykket) {
                                        return env.getSchema().getObjectType("OppgaveUtfoertVellykket");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("NyStatusSakResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NyStatusSakVellykket) {
                                        return env.getSchema().getObjectType("NyStatusSakVellykket");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("TilleggsinformasjonSakResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof TilleggsinformasjonSakVellykket) {
                                        return env.getSchema().getObjectType("TilleggsinformasjonSakVellykket");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("HardDeleteSakResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof HardDeleteSakVellykket) {
                                        return env.getSchema().getObjectType("HardDeleteSakVellykket");
                                    } else {
                                        throw new RuntimeException("Ikke støttet type");
                                    }
                                })
                )
                .type("MineNotifikasjonerResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NotifikasjonConnection) {
                                        return env.getSchema().getObjectType("NotifikasjonConnection");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("HentNotifikasjonResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof HentetNotifikasjon) {
                                        return env.getSchema().getObjectType("HentetNotifikasjon");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("HentSakResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof HentetSak) {
                                        return env.getSchema().getObjectType("HentetSak");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("Notifikasjon", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof Oppgave) {
                                        return env.getSchema().getObjectType("Oppgave");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("Mottaker", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof AltinnMottaker) {
                                        return env.getSchema().getObjectType("AltinnMottaker");
                                    } else {
                                        return env.getSchema().getObjectType("NaermesteLederMottaker");
                                    }
                                })
                )
                .type("NesteStegSakResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("SoftDeleteNotifikasjonResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("HardDeleteNotifikasjonResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("SoftDeleteSakResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("OppgaveUtsettFristResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("OppgaveEndrePaaminnelseResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("OppdaterKalenderavtaleResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NySakVellykket) {
                                        return env.getSchema().getObjectType("NySakVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .type("NyKalenderavtaleResultat", typeWriting ->
                        typeWriting
                                .typeResolver(env -> {
                                    var javaObject = env.getObject();
                                    if (javaObject instanceof NyKalenderavtaleVellykket) {
                                        return env.getSchema().getObjectType("NyKalenderavtaleVellykket");
                                    } else {
                                        return env.getSchema().getObjectType("UkjentProdusent");
                                    }
                                })
                )
                .build();
    }
}
