package no.nav.foreldrepenger.vtp.felles;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

class KeystoresGenerator {
    private static final Logger log = LoggerFactory.getLogger(KeystoresGenerator.class);

    static void generateKeystoresIfNotExists() {
        boolean shouldGenerate = false;
        String keystorePath = KeystoreUtils.getKeystoreFilePath();
        String truststorePath = KeystoreUtils.getTruststoreFilePath();
        if (!Files.exists(Paths.get(keystorePath))) {
            log.warn("Keystore file {} does not exist - will auto-generate the file now.", keystorePath);
            shouldGenerate = true;
        } else if (!Files.exists(Paths.get(truststorePath))) {
            log.warn("Truststore file {} does not exist - will auto-generate the file now.", truststorePath);
            shouldGenerate = true;
        }

        if (shouldGenerate) {
            log.info("Generating keystores {} and {}", keystorePath, truststorePath);
            new File(keystorePath).getParentFile().mkdirs();
            new File(truststorePath).getParentFile().mkdirs();
            generateKeystores(
                    keystorePath,
                    KeystoreUtils.getKeyStorePassword(),
                    truststorePath,
                    KeystoreUtils.getTruststorePassword()
            );
        }
    }
    static void generateKeystores(
            String keystorePath,
            String keystorePassword,
            String truststorePath,
            String truststorePassword
    ) {
        Security.addProvider(new BouncyCastleProvider());

        // TODO: Maybe use PKCS12? Java 9 and onwards uses that, Java 8 or below uses JKS.
        // String outputFormat = "PKCS12";
        String outputFormat = "JKS";

        try {
            // Generate public/private keypair and self-signed certificate
            java.security.KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            java.security.cert.Certificate selfCert = createMasterCert(
                    publicKey, privateKey);

            // Create the truststore, add the certificate, save to file
            KeyStore trustStore = KeyStore.getInstance(outputFormat);
            trustStore.load(null, truststorePassword.toCharArray());
            trustStore.setCertificateEntry("localhost-ssl", selfCert);
            writeKeystoreToFile(trustStore, truststorePath, truststorePassword);

            // Create the keystore, add the two entries
            // (we just use the same private key twice, for simplicity...),
            // save to file
            java.security.cert.Certificate[] outChain = { selfCert };
            KeyStore outStore = KeyStore.getInstance(outputFormat);
            outStore.load(null, keystorePassword.toCharArray());
            outStore.setKeyEntry("localhost-ssl", privateKey, keystorePassword.toCharArray(),
                    outChain);
            outStore.setKeyEntry("app-key", privateKey, keystorePassword.toCharArray(),
                    outChain);
            writeKeystoreToFile(outStore, keystorePath, keystorePassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError(e.getMessage());
        }
    }

    private static void writeKeystoreToFile(KeyStore keyStore, String filePath, String password) throws Exception {
        OutputStream outputStream = new FileOutputStream(filePath);
        keyStore.store(outputStream, password.toCharArray());
        outputStream.flush();
        outputStream.close();
    }

    private static Certificate createMasterCert(
            PublicKey       pubKey,
            PrivateKey      privKey)
            throws Exception
    {
        String  issuer = "CN=localhost";
        String  subject = "CN=localhost";

        // Valid from January 1st 1970 until ten years from now
        Date validFrom = new Date(0);
        Date validUntil = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 3650));

        X509v1CertificateBuilder builder = new JcaX509v1CertificateBuilder(
                new X500Name(issuer),
                BigInteger.valueOf(1),
                validFrom,
                validUntil,
                new X500Name(subject),
                pubKey
        );

        X509CertificateHolder holder = builder.build(
                new JcaContentSignerBuilder("SHA1WithRSA")
                        .setProvider("BC")
                        .build(privKey)
        );

        X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
        cert.checkValidity(new Date());
        cert.verify(pubKey);

        return cert;
    }
}
