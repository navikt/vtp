package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;

import org.jose4j.base64url.Base64Url;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.lang.JoseException;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.x509.impl.KeyStoreX509CredentialAdapter;

import no.nav.foreldrepenger.util.KeystoreUtils;

public class KeyStoreTool {
    private static RsaJsonWebKey jwk = null;
    private static KeyStore keystore = null;

    private KeyStoreTool() {
    }

    static synchronized void init() {

        PublicKey myPublicKey;
        PrivateKey myPrivateKey;
        char[] keystorePassword = getKeyStorePassword();
        String keystorePath = getDefaultKeyStorePath();
        String keyAndCertAlias = getKeyAndCertAlias();

        try (FileInputStream keystoreFile = new FileInputStream(keystorePath)) {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(keystoreFile, keystorePassword);

            KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword);
            KeyStore.PrivateKeyEntry pk = (KeyStore.PrivateKeyEntry) ks.getEntry(keyAndCertAlias, protParam);
            myPrivateKey = pk.getPrivateKey();
            Certificate cert = ks.getCertificate(keyAndCertAlias);
            myPublicKey = cert.getPublicKey();

            KeyStoreTool.keystore = ks;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
            throw new RuntimeException("Error during loading of keystore. Do you have your keystore in order, soldier?", e);
        }

        try {
            jwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(myPublicKey);
            jwk.setPrivateKey(myPrivateKey);
            jwk.setKeyId("1");
        } catch (JoseException e) {
            throw new RuntimeException("Error during init of JWK: ", e);
        }

    }

    public static String getDefaultKeyStorePath() {
        return KeystoreUtils.getKeystoreFilePath();
    }

    public static char[] getKeyStorePassword() {
        return KeystoreUtils.getKeyStorePassword().toCharArray();
    }

    public static String getKeyAndCertAlias() {
        return System.getProperty("no.nav.modig.security.appkey", "localhost-ssl");
    }


    public static synchronized Credential getDefaultCredential() {
        if (keystore == null) {
            init();
            org.apache.xml.security.Init.init();
        }
        return new KeyStoreX509CredentialAdapter(keystore, getKeyAndCertAlias(), getKeyStorePassword());
    }

    public static synchronized RsaJsonWebKey getJsonWebKey() {
        if (jwk == null) {
            init();
        }
        return jwk;
    }

    public static String getJwks() {
        String kty = "RSA";
        String kid = "1";
        String use = "sig";
        String alg = "RS256";
        RsaJsonWebKey jsonWebKey = getJsonWebKey();
        String e = Base64Url.encode(jsonWebKey.getRsaPublicKey().getPublicExponent().toByteArray());
        RSAPublicKey publicKey = (RSAPublicKey) jsonWebKey.getPublicKey();

        byte[] bytes = publicKey.getModulus().toByteArray();
        String n = Base64Url.encode(bytes);

        return String.format("{\"keys\":[{" +
                "\"kty\":\"%s\"," +
                "\"alg\":\"%s\"," +
                "\"use\":\"%s\"," +
                "\"kid\":\"%s\"," +
                "\"n\":\"%s\"," +
                "\"e\":\"%s\"" +
                "}]}", kty, alg, use, kid, n, e);
    }

}
