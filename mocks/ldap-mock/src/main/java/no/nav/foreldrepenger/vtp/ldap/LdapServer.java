package no.nav.foreldrepenger.vtp.ldap;

import static com.unboundid.ldap.listener.InMemoryListenerConfig.createLDAPConfig;
import static com.unboundid.ldap.listener.InMemoryListenerConfig.createLDAPSConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFAddChangeRecord;
import com.unboundid.ldif.LDIFChangeRecord;
import com.unboundid.ldif.LDIFReader;

import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NavAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

public class LdapServer {
    private static final Logger LOG = LoggerFactory.getLogger(LdapServer.class);
    private static final String SETUP_LDIF = "ldap_setup.ldif";
    private static final int LDAPS_PORT = 8636; // 636 er default port for LDAPS
    private static final int LDAP_PORT = 8389;  // 389 er default port for LDAP

    private static final AnsatteIndeks ansattIndeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
    private final InMemoryDirectoryServer directoryServer;

    public LdapServer(File keystoreFile, char[] password) throws Exception {
        var cfg = new InMemoryDirectoryServerConfig("DC=local");
        cfg.setEnforceAttributeSyntaxCompliance(false);
        cfg.setEnforceSingleStructuralObjectClass(false);
        cfg.setSchema(null); // dropper valider schema slik at vi slipper å definere alle object classes

        var sslContext = SSLContext.getInstance("TLSv1.2");
        var km = loadKeyManagers(keystoreFile, password);
        sslContext.init(km, null, null);

        var ldapsConfig = createLDAPSConfig("LDAPS", LDAPS_PORT, sslContext.getServerSocketFactory());
        var ldapConfig = createLDAPConfig("LDAP", LDAP_PORT);

        cfg.setListenerConfigs(ldapsConfig,ldapConfig);

        directoryServer = new InMemoryDirectoryServer(cfg);
        readLdifFilesFromClasspath();
        readNavAnsatte();
    }

    private void readNavAnsatte() throws LDAPException {
        for (NavAnsatt navAnsatt : ansattIndeks.alleAnsatte()) {
            addNavAnsatt(navAnsatt);
        }
    }

    private void addNavAnsatt(NavAnsatt navAnsatt) throws LDAPException {
        var entry = new Entry(
                String.format("CN=%s,OU=Users,OU=NAV,OU=BusinessUnits,DC=test,DC=local", navAnsatt.ident()),
                new Attribute("objectClass", "user", "organizationalPerson", "person", "top"),
                new Attribute("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=test,DC=local"),
                new Attribute("cn", navAnsatt.ident()),
                new Attribute("displayName", navAnsatt.displayName()),
                new Attribute("givenName", navAnsatt.givenName()),
                new Attribute("sn", navAnsatt.surname()),
                new Attribute("mail", navAnsatt.email()),
                new Attribute("streetAddress", navAnsatt.streetAddress()),
                new Attribute("userPrincipalName", navAnsatt.email()),
                new Attribute("userPassword", "dummy"),
                new Attribute("memberOf", tilMemberOf(navAnsatt.groups()))
        );
        var ldifAddChangeRecord = new LDIFAddChangeRecord(entry);
        ldifAddChangeRecord.processChange(directoryServer);

    }

    private static List<String> tilMemberOf(List<NavAnsatt.NavGroup> grupper) {
        return grupper.stream()
                .map(gruppe -> String.format("CN=%s,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local", gruppe.name()))
                .toList();
    }


    @SuppressWarnings("resource")
    private void readLdifFilesFromClasspath() throws Exception {
        var ldifs = getClass().getClassLoader().getResources(SETUP_LDIF);
        while(ldifs.hasMoreElements()) {
            var ldif = ldifs.nextElement();
            try(InputStream is = ldif.openStream()){
                var r = new LDIFReader(is);
                LDIFChangeRecord readEntry;
                while ((readEntry = r.readChangeRecord()) != null) {
                    LOG.info("Read entry from path {} LDIF: {}", ldif.getPath(), Arrays.toString(readEntry.toLDIF()));
                    readEntry.processChange(directoryServer);
                }
            }
        }
    }

    private KeyManager[] loadKeyManagers(File keystoreFile, char[] password) throws Exception {
        var ks = KeyStore.getInstance("JKS");
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
