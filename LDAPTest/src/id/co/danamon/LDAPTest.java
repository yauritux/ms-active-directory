package id.co.danamon;

import java.util.Hashtable;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * @author yauritux
 */
public class LDAPTest {

	public static void main(String... args) throws java.io.IOException {
		Properties ldapProps = new Properties();
		ldapProps.load(LDAPTest.class.getResourceAsStream("/ldap.properties"));
	   
		Hashtable<String, String> env = new Hashtable<>();
		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		/*
		env.put(Context.SECURITY_PRINCIPAL, ldapProps.getProperty("ldap.domain") + "\\" 
			+ ldapProps.getProperty("ldap.user") + "@" + ldapProps.getProperty("ldap.server.ip")
			+ ":" + ldapProps.getProperty("ldap.ssl.port"));
		*/
		env.put(Context.SECURITY_PRINCIPAL, "CORPUAT\\KPI_APP");
		env.put(Context.SECURITY_CREDENTIALS, "P@ssw0rd");
		env.put(Context.SECURITY_PROTOCOL, "ssl");
		
		/*
		env.put(Context.PROVIDER_URL, "ldap://" + ldapProps.getProperty("ldap.server.ip") 
			+ ":" + ldapProps.getProperty("ldap.ssl.port"));
		*/
		env.put(Context.PROVIDER_URL, "LDAP://KSI-UAT-CORPUAT:636/DC=corpuat,DC=danamon,DC=co,DC=id");

		System.out.println("SECURITY_AUTHENTICATION=" + env.get(Context.SECURITY_AUTHENTICATION));
		System.out.println("SECURITY_PRINCIPAL=" + env.get(Context.SECURITY_PRINCIPAL));
		System.out.println("SECURITY_CREDENTIALS=" + env.get(Context.SECURITY_CREDENTIALS));
		System.out.println("PROVIDER_URL=" + env.get(Context.PROVIDER_URL));
		
		System.out.println("Trying to connect...");
		System.out.println();
		
		DirContext ctx = null;
		try {
			//authenticate user
			ctx = new InitialDirContext(env);
			
			System.out.println("Successfully connected...");
			System.out.println("Fetching data now...");
			
			//perform the check
			//String searchBase = "DC=corpuat,DC=danamon,DC=co,DC=id";
			String searchBase="";
			//String searchFilter = "(&((&(objectCategory=Person)(objectClass=User)))";
			//String searchFilter = "(|(objectClass=user)(objectClass=organizationalPerson))";
			//String searchFilter = "(&(objectClass=user)(cn=usertest02))";
			String searchFilter = "(objectClass=user)";
			if (args.length == 1) {
				searchFilter = "(&(objectClass=user)(cn=" + args[0] + "))";
			}
			//String searchFilter = "(objectClass=organizationalUnit)";
			SearchControls sCtrl = new SearchControls();
			sCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration answer = ctx.search(searchBase, searchFilter, sCtrl);
						
			//System.out.println("Data found.., displaying page 1 of " + (1000 / 5));
			
			List<User> users = new ArrayList<>();
			
			while (answer.hasMoreElements()) {
				SearchResult searchResult = (SearchResult) answer.nextElement();				
			
				User user = new User();
				user.cn = (String) searchResult.getAttributes().get("cn").get();
				user.name = (String) searchResult.getAttributes().get("name").get();
				user.dn = (String) searchResult.getAttributes().get("distinguishedName").get();
				user.primaryGroupId = (String) searchResult.getAttributes().get("primaryGroupID").get();
				user.mail = searchResult.getAttributes().get("mail") != null 
					? ((String) searchResult.getAttributes().get("mail").get()) : "";
				user.description = searchResult.getAttributes().get("description") != null 
					? ((String) searchResult.getAttributes().get("description").get()) : "";
				users.add(user);
			}
			
			System.out.println(users.size() + " record|s found.");
			int row = 1;
			for (User user : users) {
				System.out.println("Record #" + row);
				System.out.println("========================");
				System.out.println("CN/NIP : " + user.cn);
				System.out.println("Name : " + user.name);
				System.out.println("DN (Distinguished Name) : " + user.dn);
				System.out.println("Primary Group ID : " + user.primaryGroupId);
				System.out.println("E-mail : " + user.mail);
				System.out.println("Description : " + user.description);
				System.out.println();
				row++;
			}
		} catch (NamingException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (ctx != null) {
			try {
				ctx.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class User {
	String cn;
	String name;
	String dn;
	String primaryGroupId;
	String mail;
	String description;
}