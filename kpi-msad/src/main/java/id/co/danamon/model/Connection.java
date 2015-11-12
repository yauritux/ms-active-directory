package id.co.danamon.model;

/**
 * @author yauritux@gmail.com
 * @version 1.0.0
 * @since 1.0.0
 */
 public class Connection {
 
	private String user;
	private String password;
	private String protocol;
	private String urlScheme;
	private String domain;
	private int port;
	private String baseDn;
	
	public Connection() {}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getUrlScheme() {
		return urlScheme;
	}
	
	public void setUrlScheme(String urlScheme) {
		this.urlScheme = urlScheme;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getBaseDn() {
		return baseDn;
	}
	
	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}
 }