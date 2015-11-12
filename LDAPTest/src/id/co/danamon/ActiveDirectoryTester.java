package id.co.danamon;

import java.util.Properties;
import java.util.Collection;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import id.co.danamon.constant.SearchingAttribute;
import id.co.danamon.model.Connection;
import id.co.danamon.model.User;
import id.co.danamon.service.ActiveDirectoryService;
import id.co.danamon.service.impl.ActiveDirectoryServiceImpl;

/**
 * @author yauritux@gmail.com
 */
public class ActiveDirectoryTester {

	public static void main(String... args) throws Exception {
		Properties ldapProps = new Properties();
		ldapProps.load(LDAPTest.class.getResourceAsStream("/ldap.properties"));

		Connection connection = new Connection();
		connection.setUser(ldapProps.getProperty("ldap.user"));
		connection.setPassword(ldapProps.getProperty("ldap.password"));
		connection.setProtocol("ssl");
		connection.setUrlScheme("LDAP");
		connection.setDomain(ldapProps.getProperty("ldap.domain"));
		connection.setPort(new Integer(ldapProps.getProperty("ldap.ssl.port")));
		connection.setBaseDn(ldapProps.getProperty("ldap.baseDn"));
		
		ActiveDirectoryService adService = new ActiveDirectoryServiceImpl();
		adService.openConnection(connection);
		
		if (args.length == 0) {
			Collection<User> users = adService.fetchAllUsers("");
		} else if (args.length == 2) {
			SearchingAttribute field = SearchingAttribute.valueOf(args[0]);
			String searchValue = args[1];
			
			User user = adService.searchUser(field, searchValue, "");
			System.out.println("User name = " + user.getName());
			System.out.println("User Email = " + user.getEmail());
		}
		
		adService.closeConnection();
	}
}