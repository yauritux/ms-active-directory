package id.co.danamon.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import id.co.danamon.constant.SearchingAttribute;
import id.co.danamon.model.Connection;
import id.co.danamon.model.User;
import id.co.danamon.service.ActiveDirectoryService;

/**
 * @author yauritux
 * @version 1.0.0
 * @since 1.0.0
 */
public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {
	
	private DirContext ctx;
	
	@Override
	public DirContext openConnection(Connection connection) throws Exception {
		Hashtable<String, String> env = new Hashtable<>();
		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, connection.getUser());
		env.put(Context.SECURITY_CREDENTIALS, connection.getPassword());
		if (connection.getProtocol() != null && !connection.getProtocol().isEmpty()) {
			env.put(Context.SECURITY_PROTOCOL, connection.getProtocol());
		}
		
		//env.put(Context.PROVIDER_URL, "LDAP://KSI-UAT-CORPUAT:636/DC=corpuat,DC=danamon,DC=co,DC=id");
		StringBuilder url = new StringBuilder(connection.getUrlScheme());
		url.append("://").append(connection.getDomain());
		url.append(":").append(connection.getPort());
		url.append(connection.getBaseDn() != null && (!connection.getBaseDn().isEmpty()) 
			? ("/" + connection.getBaseDn()) : "");
		env.put(Context.PROVIDER_URL, url.toString());

		System.out.println("SECURITY_AUTHENTICATION=" + env.get(Context.SECURITY_AUTHENTICATION));
		System.out.println("SECURITY_PRINCIPAL=" + env.get(Context.SECURITY_PRINCIPAL));
		System.out.println("SECURITY_CREDENTIALS=" + env.get(Context.SECURITY_CREDENTIALS));
		System.out.println("PROVIDER_URL=" + env.get(Context.PROVIDER_URL));
		
		System.out.println("Trying to connect...");
		System.out.println();
		
		try {
			//authenticate user
			this.ctx = new InitialDirContext(env);			
			System.out.println("Successfully connected...");
		} catch (Exception e) {
			throw new Exception(e);
		}
		
		return this.ctx;
	}
	
	@Override
	public Collection<User> fetchAllUsers(String searchBase) throws NamingException, Exception {
		if (ctx == null) {
			throw new Exception("No active connection. Please open the connection!");
		}
		
		String searchFilter = "(objectClass=user)";
		SearchControls sCtrl = new SearchControls();
		sCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration answer = ctx.search(searchBase, searchFilter, sCtrl);
						
		List<User> users = new ArrayList<>();
			
		while (answer.hasMoreElements()) {
			SearchResult searchResult = (SearchResult) answer.nextElement();				
			
			users.add(new User().setCn((String) searchResult.getAttributes().get("cn").get())
				.setName((String) searchResult.getAttributes().get("name").get())
				.setDn((String) searchResult.getAttributes().get("distinguishedName").get())
				.setPrimaryGroupId((String) searchResult.getAttributes().get("primaryGroupID").get())
				.setEmail(searchResult.getAttributes().get("mail") != null 
					? ((String) searchResult.getAttributes().get("mail").get()) : "")
				.setDescription(searchResult.getAttributes().get("description") != null 
					? ((String) searchResult.getAttributes().get("description").get()) : ""));
		}
			
		System.out.println(users.size() + " record|s found.");
		int row = 1;
		for (User user : users) {
			System.out.println("Record #" + row);
			System.out.println("========================");
			System.out.println("CN/NIP : " + user.getCn());
			System.out.println("Name : " + user.getName());
			System.out.println("DN (Distinguished Name) : " + user.getDn());
			System.out.println("Primary Group ID : " + user.getPrimaryGroupId());
			System.out.println("E-mail : " + user.getEmail());
			System.out.println("Description : " + user.getDescription());
			System.out.println();
			row++;
		}		
		
		return users;
	}
	
	@Override
	public User searchUser(SearchingAttribute field, String searchValue, 
			String searchBase) throws NamingException, Exception {
		if (ctx == null) {
			throw new Exception("No active connection. Please open the connection!");
		}
				
		SearchControls sCtrl = new SearchControls();
		sCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		StringBuilder searchFilter = new StringBuilder("(&(objectClass=user)");
		if (field == SearchingAttribute.NAME) {
			searchFilter.append("(name=").append(searchValue).append("))");
		} else if (field == SearchingAttribute.EMAIL) {
			searchFilter.append("(mail=").append(searchValue).append("))");
		} else if (field == SearchingAttribute.CN) {
			searchFilter.append("(cn=").append(searchValue).append("))");
		} else {
			return null;
		}
		
		try {
			NamingEnumeration answer = ctx.search(searchBase, searchFilter.toString(), sCtrl);
			if (answer.hasMoreElements()) {
				SearchResult searchResult = (SearchResult) answer.nextElement();				
			
				return new User().setCn((String) searchResult.getAttributes().get("cn").get())
					.setName((String) searchResult.getAttributes().get("name").get())
					.setDn((String) searchResult.getAttributes().get("distinguishedName").get())
					.setPrimaryGroupId((String) searchResult.getAttributes().get("primaryGroupID").get())
					.setEmail(searchResult.getAttributes().get("mail") != null 
						? ((String) searchResult.getAttributes().get("mail").get()) : "")
					.setDescription(searchResult.getAttributes().get("description") != null 
						? ((String) searchResult.getAttributes().get("description").get()) : "");
			}
		} catch (NamingException e) {
			throw e;
		}
		
		return null;
	}
	
	@Override
	public void closeConnection() throws Exception {
        try {
            if(ctx != null) {
				ctx.close();
			}                
        } catch (NamingException e) {
        	throw new Exception(e);
        }		
	}
}