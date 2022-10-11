package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.lang.JoseException;

import no.nav.foreldrepenger.util.KeystoreUtils;

public class JsonWebKeyHelper {
    private static RsaJsonWebKey jwk = null;

    private JsonWebKeyHelper() {
    }

    public static synchronized RsaJsonWebKey getJsonWebKey() {
        if (jwk == null) {
            init();
        }
        return jwk;
    }

    public static String getJwks() {
        return new JsonWebKeySet(getJsonWebKey()).toJson();
    }

    private static synchronized void init() {
        PublicKey myPublicKey;
        PrivateKey myPrivateKey;
        var keystorePassword = KeystoreUtils.getKeyStorePassword().toCharArray();
        var keystorePath = KeystoreUtils.getKeystoreFilePath();
        var keyAndCertAlias = KeystoreUtils.getKeyAndCertAlias();

        try (var keystoreFile = new FileInputStream(keystorePath)) {
            var ks = KeyStore.getInstance("JKS");
            ks.load(keystoreFile, keystorePassword);

            var protParam = new KeyStore.PasswordProtection(keystorePassword);
            var pk = (KeyStore.PrivateKeyEntry) ks.getEntry(keyAndCertAlias, protParam);
            myPrivateKey = pk.getPrivateKey();
            var cert = ks.getCertificate(keyAndCertAlias);
            myPublicKey = cert.getPublicKey();
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
            throw new RuntimeException("Error during loading of keystore. Do you have your keystore in order, soldier?", e);
        }

        try {
            jwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(myPublicKey);
            jwk.setPrivateKey(myPrivateKey);
            jwk.setKeyId("1");
            jwk.setAlgorithm("RS256");
            jwk.setUse("sig");
        } catch (JoseException e) {
            throw new RuntimeException("Error during init of JWK: ", e);
        }
    }

}
