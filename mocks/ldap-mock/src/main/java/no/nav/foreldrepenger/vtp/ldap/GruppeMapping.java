package no.nav.foreldrepenger.vtp.ldap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GruppeMapping {


    private static final Map<String, String> OID_TIL_GRUPPENAVN = new HashMap<>();

    static {
        OID_TIL_GRUPPENAVN.put("edfe14fe-9a34-4ecb-8840-536ac2bc2818", "0000-GA-fpsak-veileder");
        OID_TIL_GRUPPENAVN.put("eb211c0d-9ca6-467f-8863-9def2cc06fd3", "0000-GA-fpsak-saksbehandler");
        OID_TIL_GRUPPENAVN.put("803b1fd5-27a0-46a2-b1b3-7152f44128b4", "0000-GA-fpsak-beslutter");
        OID_TIL_GRUPPENAVN.put("503f0cae-5bcd-484b-949c-a7e92d712858", "0000-GA-fpsak-manuelt-overstyrer");
        OID_TIL_GRUPPENAVN.put("d18989ec-5e07-494b-ad96-0c1f0c76de53", "0000-GA-fpsak-Oppgavestyrer");
        OID_TIL_GRUPPENAVN.put("89c71f0c-ca57-4e6f-8545-990f9e24c762", "0000-GA-fpsak-drift");

        OID_TIL_GRUPPENAVN.put("79630737-b41d-41eb-93fb-30907e1afed7", "0000-GA-k9-veileder");
        OID_TIL_GRUPPENAVN.put("5081d7b2-00df-442b-acb5-48eeaa114e96", "0000-GA-k9-saksbehandler");
        OID_TIL_GRUPPENAVN.put("ff2ab4cd-9ea4-4c51-b4cd-86a7999376a5", "0000-GA-k9-beslutter");
        OID_TIL_GRUPPENAVN.put("e5eccbb1-27bf-4a8f-861d-b54d7ccc8314", "0000-GA-k9-overstyrer");
        OID_TIL_GRUPPENAVN.put("d70927de-0c76-49cb-aaa7-ee67cce70aaa", "0000-GA-k9-oppgavestyrer");
        OID_TIL_GRUPPENAVN.put("7ac42b69-14af-479e-9e9c-b2235bea9863", "0000-GA-k9-drift");

        OID_TIL_GRUPPENAVN.put("13e4ed76-6d7f-448f-ba49-41760eed0f30", "0000-GA-GOSYS_KODE6");
        OID_TIL_GRUPPENAVN.put("65715e95-86b5-497d-992a-12389b1a8ff9", "0000-GA-GOSYS_KODE7");
        OID_TIL_GRUPPENAVN.put("00b7a94d-99e6-455c-a497-a3f1a70a698b", "0000-GA-GOSYS_UTVIDET");

        OID_TIL_GRUPPENAVN.put("df650e66-9590-4c96-8ecb-8efea46f1306", "0000-GA-Strengt_Fortrolig_Adresse");
        OID_TIL_GRUPPENAVN.put("bc7fde53-c4c3-4fff-9079-c6440ca5ff5e", "0000-GA-Fortrolig_Adresse");
        OID_TIL_GRUPPENAVN.put("63b3f84f-1ec5-444b-ad33-2ad2d3495da1", "0000-GA-Egne_ansatte");
        OID_TIL_GRUPPENAVN.put("6b7c6d2f-ed3d-4d11-bc6d-82cab825a09f", "0000-GA-PENSJON_KLAGEBEH");

        OID_TIL_GRUPPENAVN.put("39f2f9c8-1631-438a-a82e-8bed14adb672", "0000-GA-INNTK");
        OID_TIL_GRUPPENAVN.put("f9ae8c76-3230-4ae0-ba5e-b39922ae86a4", "0000-GA-INNTK_8-28");
        OID_TIL_GRUPPENAVN.put("99cff4e7-160a-42ba-899a-1cefd605d0e5", "0000-GA-INNTK_8-30");
        OID_TIL_GRUPPENAVN.put("9698f8bb-96fb-470a-b65d-0b5541a61e9d", "0000-GA-INNTK_FORELDRE");
        OID_TIL_GRUPPENAVN.put("8873e633-ac29-4463-9913-184bea045dad", "0000-GA-INNTK_PENSJONSGIVENDE");
    }

    public static String gruppenavnFraOid(String oid){
        return Objects.requireNonNull(OID_TIL_GRUPPENAVN.get(oid), "fant ikke mapping fra " + oid + " til gruppenavn, må registreres i " + GruppeMapping.class);
    }

}
