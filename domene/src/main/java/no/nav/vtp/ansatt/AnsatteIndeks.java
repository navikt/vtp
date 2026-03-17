package no.nav.vtp.ansatt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AnsatteIndeks {

    // Konstanter for gjentakende grupper
    private static final NavAnsatt.NavGroup FPSAK_SAKSBEHANDLER =
        new NavAnsatt.NavGroup(UUID.fromString("eb211c0d-9ca6-467f-8863-9def2cc06fd3"), "0000-GA-fpsak-saksbehandler");
    private static final NavAnsatt.NavGroup K9_SAKSBEHANDLER =
        new NavAnsatt.NavGroup(UUID.fromString("5081d7b2-00df-442b-acb5-48eeaa114e96"), "0000-GA-k9-saksbehandler");
    private static final NavAnsatt.NavGroup AKTIVITETSPENGER_SAKSBEHANDLER_DEL2 =
        new NavAnsatt.NavGroup(UUID.fromString("4b3f571a-44c7-4d2b-9811-c6a1ea2cc9f4"), "0000-CA-aktivitetspenger-saksbehandler-del2");
    private static final NavAnsatt.NavGroup INNTK_8_30 =
        new NavAnsatt.NavGroup(UUID.fromString("99cff4e7-160a-42ba-899a-1cefd605d0e5"), "0000-GA-INNTK_8-30");
    private static final NavAnsatt.NavGroup INNTK_8_28 =
        new NavAnsatt.NavGroup(UUID.fromString("f9ae8c76-3230-4ae0-ba5e-b39922ae86a4"), "0000-GA-INNTK_8-28");
    private static final NavAnsatt.NavGroup INNTK_FORELDRE =
        new NavAnsatt.NavGroup(UUID.fromString("9698f8bb-96fb-470a-b65d-0b5541a61e9d"), "0000-GA-INNTK_FORELDRE");
    private static final NavAnsatt.NavGroup INNTK_PENSJONSGIVENDE =
        new NavAnsatt.NavGroup(UUID.fromString("8873e633-ac29-4463-9913-184bea045dad"), "0000-GA-INNTK_PENSJONSGIVENDE");
    private static final NavAnsatt.NavGroup INNTK =
        new NavAnsatt.NavGroup(UUID.fromString("39f2f9c8-1631-438a-a82e-8bed14adb672"), "0000-GA-INNTK");
    private static final NavAnsatt.NavGroup FPSAK_BESLUTTER =
        new NavAnsatt.NavGroup(UUID.fromString("803b1fd5-27a0-46a2-b1b3-7152f44128b4"), "0000-GA-fpsak-beslutter");
    private static final NavAnsatt.NavGroup K9_BESLUTTER =
        new NavAnsatt.NavGroup(UUID.fromString("ff2ab4cd-9ea4-4c51-b4cd-86a7999376a5"), "0000-GA-k9-beslutter");
    private static final NavAnsatt.NavGroup AKTIVITETSPENGER_BESLUTTER_DEL2 =
        new NavAnsatt.NavGroup(UUID.fromString("db408148-cf2b-4711-ad03-0ef93995eb35"), "0000-CA-aktivitetspenger-beslutter-del2");
    private static final NavAnsatt.NavGroup FPSAK_MANUELT_OVERSTYRER =
        new NavAnsatt.NavGroup(UUID.fromString("503f0cae-5bcd-484b-949c-a7e92d712858"), "0000-GA-fpsak-manuelt-overstyrer");
    private static final NavAnsatt.NavGroup K9_OVERSTYRER =
        new NavAnsatt.NavGroup(UUID.fromString("e5eccbb1-27bf-4a8f-861d-b54d7ccc8314"), "0000-GA-k9-overstyrer");
    private static final NavAnsatt.NavGroup AKTIVITETSPENGER_OVERSTYRER_DEL2 =
        new NavAnsatt.NavGroup(UUID.fromString("2d3d742f-3b00-4c7d-a261-b2084aadd702"), "0000-CA-aktivitetspenger-overstyrer-del2");
    private static final NavAnsatt.NavGroup FPSAK_VEILEDER =
        new NavAnsatt.NavGroup(UUID.fromString("edfe14fe-9a34-4ecb-8840-536ac2bc2818"), "0000-GA-fpsak-veileder");
    private static final NavAnsatt.NavGroup K9_VEILEDER =
        new NavAnsatt.NavGroup(UUID.fromString("79630737-b41d-41eb-93fb-30907e1afed7"), "0000-GA-k9-veileder");
    private static final NavAnsatt.NavGroup AKTIVITETSPENGER_VEILEDER =
        new NavAnsatt.NavGroup(UUID.fromString("6add4432-bfd1-4952-a8b4-28e746e9592c"), "0000-CA-aktivitetspenger-veileder");
    private static final NavAnsatt.NavGroup FPSAK_OPPGAVESTYRER =
        new NavAnsatt.NavGroup(UUID.fromString("d18989ec-5e07-494b-ad96-0c1f0c76de53"), "0000-GA-fpsak-Oppgavestyrer");
    private static final NavAnsatt.NavGroup K9_OPPGAVESTYRER =
        new NavAnsatt.NavGroup(UUID.fromString("d70927de-0c76-49cb-aaa7-ee67cce70aaa"), "0000-GA-k9-oppgavestyrer");
    private static final NavAnsatt.NavGroup FPSAK_DRIFT =
        new NavAnsatt.NavGroup(UUID.fromString("89c71f0c-ca57-4e6f-8545-990f9e24c762"), "0000-GA-fpsak-drift");
    private static final NavAnsatt.NavGroup K9_DRIFT =
        new NavAnsatt.NavGroup(UUID.fromString("7ac42b69-14af-479e-9e9c-b2235bea9863"), "0000-GA-k9-drift");
    private static final NavAnsatt.NavGroup AKTIVITETSPENGER_DRIFT =
        new NavAnsatt.NavGroup(UUID.fromString("91b518bd-04ce-4eed-8d93-6fb134e93598"), "0000-CA-aktivitetspenger-drift");

    private static final List<NavAnsatt.NavGroup> STANDARD_SAKSBEHANDLER_GRUPPER = List.of(
        FPSAK_SAKSBEHANDLER, K9_SAKSBEHANDLER, AKTIVITETSPENGER_SAKSBEHANDLER_DEL2,
        INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
    );

    private static final List<NavAnsatt> ALLE_ANSATTE = List.of(
        new NavAnsatt(
            "S123456",
            UUID.fromString("3c3caafb-a943-4255-aa65-6f29345b7541"),
            "Sara Saksbehandler",
            "Sara",
            "Saksbehandler",
            "sara.saksbehandler@example.com",
            "4833",
            STANDARD_SAKSBEHANDLER_GRUPPER,
            List.of()
        ),
        new NavAnsatt(
            "S666666",
            UUID.fromString("05801408-feb8-42b2-850a-8d87adb911c7"),
            "Sa6a Saksbehandler6",
            "Sa6a",
            "Saksbehandler",
            "sa6a.saksbehandler6@example.com",
            "2103",
            List.of(
                FPSAK_SAKSBEHANDLER, K9_SAKSBEHANDLER, AKTIVITETSPENGER_SAKSBEHANDLER_DEL2,
                new NavAnsatt.NavGroup(UUID.fromString("df650e66-9590-4c96-8ecb-8efea46f1306"), "0000-GA-Strengt_Fortrolig_Adresse"),
                new NavAnsatt.NavGroup(UUID.fromString("13e4ed76-6d7f-448f-ba49-41760eed0f30"), "0000-GA-GOSYS_KODE6"),
                INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "S777777",
            UUID.fromString("352b0a40-4d13-43e7-8e55-ba6060d6d430"),
            "Sa7a Saksbehandler7",
            "Sa7a",
            "Saksbehandler",
            "sa7a.saksbehandler7@example.com",
            "4833",
            List.of(
                FPSAK_SAKSBEHANDLER, K9_SAKSBEHANDLER, AKTIVITETSPENGER_SAKSBEHANDLER_DEL2,
                new NavAnsatt.NavGroup(UUID.fromString("bc7fde53-c4c3-4fff-9079-c6440ca5ff5e"), "0000-GA-Fortrolig_Adresse"),
                new NavAnsatt.NavGroup(UUID.fromString("65715e95-86b5-497d-992a-12389b1a8ff9"), "0000-GA-GOSYS_KODE7"),
                INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "E123456",
            UUID.fromString("98f1a0f3-292e-412c-b84a-d729b6169f2b"),
            "Ea Saksbehandler",
            "Ea",
            "Saksbehandler",
            "ea.saksbehandler@example.com",
            "4833",
            List.of(
                FPSAK_SAKSBEHANDLER, K9_SAKSBEHANDLER, AKTIVITETSPENGER_SAKSBEHANDLER_DEL2,
                new NavAnsatt.NavGroup(UUID.fromString("63b3f84f-1ec5-444b-ad33-2ad2d3495da1"), "0000-GA-Egne_ansatte"),
                new NavAnsatt.NavGroup(UUID.fromString("00b7a94d-99e6-455c-a497-a3f1a70a698b"), "0000-GA-GOSYS_UTVIDET"),
                INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "B123456",
            UUID.fromString("fb864a55-bc42-4196-aba9-792c78661806"),
            "Birger Beslutter",
            "Birger",
            "Beslutter",
            "birger.beslutter@example.com",
            "4833",
            List.of(
                FPSAK_SAKSBEHANDLER, FPSAK_BESLUTTER, K9_SAKSBEHANDLER, K9_BESLUTTER,
                AKTIVITETSPENGER_SAKSBEHANDLER_DEL2, AKTIVITETSPENGER_BESLUTTER_DEL2,
                INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "O123456",
            UUID.fromString("d6881997-05d7-4ff2-8ef6-818d8539c434"),
            "Ove Overstyrer",
            "Ove",
            "Overstyrer",
            "ove.overstyrer@example.com",
            "4833",
            List.of(
                FPSAK_SAKSBEHANDLER, FPSAK_MANUELT_OVERSTYRER, K9_SAKSBEHANDLER, K9_OVERSTYRER,
                AKTIVITETSPENGER_SAKSBEHANDLER_DEL2, AKTIVITETSPENGER_OVERSTYRER_DEL2,
                INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "K123456",
            UUID.fromString("cf70667f-250e-4565-a365-75f07f87fe29"),
            "Klara Klagebehandler",
            "Klara",
            "Klagebehandler",
            "klara.klagebehandler@example.com",
            "4833",
            List.of(
                FPSAK_SAKSBEHANDLER, K9_SAKSBEHANDLER, AKTIVITETSPENGER_SAKSBEHANDLER_DEL2,
                INNTK_8_30, INNTK_8_28,
                new NavAnsatt.NavGroup(UUID.fromString("6b7c6d2f-ed3d-4d11-bc6d-82cab825a09f"), "0000-GA-PENSJON_KLAGEBEH"),
                INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "V123456",
            UUID.fromString("ba6f0772-b99b-4eb9-857e-7a9e6720d582"),
            "Vegard Veileder",
            "Vegard",
            "Veileder",
            "vegard.veileder@example.com",
            "1900",
            List.of(
                FPSAK_VEILEDER, K9_VEILEDER, AKTIVITETSPENGER_VEILEDER,
                INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "L123456",
            UUID.fromString("4cd710cc-c367-4110-8126-fe495ee11c23"),
            "Oline Los-Oppgavestyrer",
            "Oline",
            "Los-Oppgavestyrer",
            "oline.los-oppgavestyrer@example.com",
            "4833",
            List.of(
                FPSAK_SAKSBEHANDLER, FPSAK_OPPGAVESTYRER, K9_SAKSBEHANDLER, K9_OPPGAVESTYRER,
                INNTK_8_30, INNTK_8_28, INNTK_FORELDRE, INNTK_PENSJONSGIVENDE, INNTK
            ),
            List.of()
        ),
        new NavAnsatt(
            "D123456",
            UUID.fromString("d3b97fcb-8778-4e09-ba7e-f8993d6e26a6"),
            "Daniel Drifter",
            "Daniel",
            "Drifter",
            "daniel.drifter@example.com",
            "2970",
            List.of(FPSAK_DRIFT, K9_DRIFT, AKTIVITETSPENGER_DRIFT),
            List.of()
        ),
        new NavAnsatt(
            "U123456",
            UUID.fromString("b11926c5-9c6f-4d20-a667-0471c4fae717"),
            "Une Ungdomsprogramveileder",
            "Une",
            "Ungdomsprogramveileder",
            "une.ungdomsprogramveileder@example.com",
            "4833",
            List.of(new NavAnsatt.NavGroup(UUID.fromString("c07ba995-a33f-4d0c-b3a8-321e7990cc03"), "0000-CA-ung-programveileder")),
            List.of()
        ),
        new NavAnsatt(
            "A111111",
            UUID.fromString("28db24a8-9973-4092-878a-4a73e9010c71"),
            "Akilles Del 1 Saksbehandler",
            "Akilles",
            "Saksbehandler",
            "akilles.del.1.saksbehandler@example.com",
            "4833",
            List.of(new NavAnsatt.NavGroup(UUID.fromString("e1bd0c68-0c47-4dd0-ba77-c6e1e46f9d30"), "0000-CA-aktivitetspenger-saksbehandler-del1")),
            List.of()
        ),
        new NavAnsatt(
            "B111111",
            UUID.fromString("463c5737-4ff1-4c33-a8ad-c81f248eb593"),
            "Akhtar Del 1 Beslutter",
            "Akhtar",
            "Beslutter",
            "akhtar.del.1.beslutter@example.com",
            "4833",
            List.of(
                new NavAnsatt.NavGroup(UUID.fromString("e1bd0c68-0c47-4dd0-ba77-c6e1e46f9d30"), "0000-CA-aktivitetspenger-saksbehandler-del1"),
                new NavAnsatt.NavGroup(UUID.fromString("4a3cb814-51dc-4587-9215-075e05d29665"), "0000-CA-aktivitetspenger-beslutter-del1")
            ),
            List.of()
        ),
        new NavAnsatt(
            "P123456",
            UUID.fromString("3d48bf40-4bfd-48d8-8280-065bf8ced9cd"),
            "Policy Information Point",
            "Policy Information",
            "Point",
            "pip@example.com",
            "2970",
            List.of(),
            List.of()
        )
    );

    private static final Map<String, NavAnsatt> ansatteByIdent = initByIdent();
    private static final Map<UUID, NavAnsatt> ansatteById = initById();

    private static Map<String, NavAnsatt> initByIdent() {
        var map = new ConcurrentHashMap<String, NavAnsatt>();
        ALLE_ANSATTE.forEach(a -> map.put(a.ident().toLowerCase(), a));
        return map;
    }

    private static Map<UUID, NavAnsatt> initById() {
        var map = new ConcurrentHashMap<UUID, NavAnsatt>();
        ALLE_ANSATTE.forEach(a -> map.put(a.oid(), a));
        return map;
    }

    public static Collection<NavAnsatt> alleAnsatte() {
        return ansatteByIdent.values();
    }

    public static NavAnsatt findByIdent(String ident) {
        return ansatteByIdent.get(ident.toLowerCase());
    }

    public static NavAnsatt findById(UUID id) {
        return ansatteById.get(id);
    }
}
