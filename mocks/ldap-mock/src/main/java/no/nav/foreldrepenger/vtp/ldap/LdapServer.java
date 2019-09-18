package no.nav.foreldrepenger.vtp.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Enumeration;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFChangeRecord;
import com.unboundid.ldif.LDIFReader;

public class LdapServer {

    private static final Logger LOG = LoggerFactory.getLogger(LdapServer.class);
    private static final String BASEDATA_USERS_LDIF = "basedata/users.ldif";
    private final int listenerPortLdaps = Integer.valueOf(System.getProperty("ldaps.port", "8636")); // 636 er default port for LDAPS
    private final int listenerPortLdap = Integer.valueOf(System.getProperty("ldap.port", "8389")); // 389 er default port for LDAP

    private InMemoryDirectoryServer directoryServer;

    private final File keystoreFile;
    private final char[] password;

    public LdapServer(File keystoreFile, char[] password) throws Exception {
        this.keystoreFile = keystoreFile;
        this.password = password;
        InMemoryDirectoryServerConfig cfg = new InMemoryDirectoryServerConfig("DC=local");

        cfg.setEnforceAttributeSyntaxCompliance(false);
        cfg.setEnforceSingleStructuralObjectClass(false);
        cfg.setSchema(null); // dropper valider schema slik at vi slipper å definere alle object classes

        SSLContext TLScontext = SSLContext.getInstance("TLS");

        KeyManager[] km = loadKeyManagers();
        TLScontext.init(km, null, null);

        InMemoryListenerConfig ldapsConfig = InMemoryListenerConfig.createLDAPSConfig("LDAPS", listenerPortLdaps, TLScontext.getServerSocketFactory());
        InMemoryListenerConfig ldapConfig = InMemoryListenerConfig.createLDAPConfig("LDAP",listenerPortLdap );

        cfg.setListenerConfigs(ldapsConfig,ldapConfig);

        directoryServer = new InMemoryDirectoryServer(cfg);
        readLdifFilesFromClasspath(directoryServer);
    }

    @SuppressWarnings("resource")
    private void readLdifFilesFromClasspath(InMemoryDirectoryServer server) throws Exception {
        Enumeration<URL> ldifs = getClass().getClassLoader().getResources(BASEDATA_USERS_LDIF);
        while(ldifs.hasMoreElements()) {
            URL ldif = ldifs.nextElement();
            try(InputStream is = ldif.openStream()){
                LDIFReader r = new LDIFReader(is);
                LDIFChangeRecord readEntry = null;
                while ((readEntry = r.readChangeRecord()) != null) {

                    LOG.info("Read entry from path {} LDIF: {}", ldif.getPath(), Arrays.toString(readEntry.toLDIF()));
                  readEntry.processChange(server);
                }
            }
        }
    }

    private KeyManager[] loadKeyManagers() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        try (InputStream is = new FileInputStream(keystoreFile)) {
            ks.load(is, password);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
            return kmf.getKeyManagers();
        }
    }

    public void start() {
        try {
            directoryServer.startListening();
        } catch (LDAPException e) {
            throw new IllegalStateException("Kunne ikke starte LdapServer", e);
        }
    }

    public InMemoryDirectoryServer getDirectoryServer() {
        return directoryServer;
    }

}
