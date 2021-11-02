package no.nav.foreldrepenger.vtp.server.auth.rest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapName;

public class UserRepository {
    public static List<SearchResult> getAllUsers() throws NamingException {
        Hashtable<String, String> props = new Hashtable<>();
        props.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(javax.naming.Context.SECURITY_AUTHENTICATION, "none");
        if (null != System.getenv("LDAP_PROVIDER_URL")) {
            props.put(javax.naming.Context.PROVIDER_URL, System.getenv("LDAP_PROVIDER_URL"));
        } else {
            props.put(javax.naming.Context.PROVIDER_URL, "ldap://localhost:8389/");
        }

        InitialLdapContext ctx = new InitialLdapContext(props, null);
        LdapName base = new LdapName("ou=NAV,ou=BusinessUnits,dc=test,dc=local");

        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setCountLimit(50);

        NamingEnumeration<SearchResult> result = ctx.search(base, "cn=*", controls);

        List<SearchResult> usernames = new ArrayList<>();

        while (result.hasMore()) {
            usernames.add(result.next());
        }
        return usernames;
    }
}
