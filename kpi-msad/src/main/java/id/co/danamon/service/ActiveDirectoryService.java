package id.co.danamon.service;

import java.util.Collection;
import java.util.Properties;

import javax.naming.directory.DirContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import id.co.danamon.constant.SearchingAttribute;
import id.co.danamon.model.Connection;
import id.co.danamon.model.User;

/**
 * @author yauritux
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ActiveDirectoryService {
    
    public DirContext openConnection(Connection connection) throws Exception;
	public Collection<User> fetchAllUsers(String searchBase) throws NamingException, Exception;
	public User searchUser(SearchingAttribute field, String searchValue, 
		String searchBase) throws NamingException, Exception;
	public void closeConnection() throws Exception;
}