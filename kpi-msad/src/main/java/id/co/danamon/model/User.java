package id.co.danamon.model;

import java.util.Map;

/**
 * @author yauritux@gmail.com
 * @version 1.0.0
 * @since 1.0.0
 */
public class User implements java.io.Serializable {
	
	private static final Long serialVersionUID = 1L;
    
	private String cn;
	private String name;
	private String dn;
	private String primaryGroupId;
	private String email;
	private String description;
	
	public User() {}
	
	public String getCn() {
		return cn;
	}
	
	public User setCn(String cn) {
		this.cn = cn;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public User setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getDn() {
		return dn;
	}
	
	public User setDn(String dn) {
		this.dn = dn;
		return this;
	}
	
	public String getPrimaryGroupId() {
		return primaryGroupId;
	}
	
	public User setPrimaryGroupId(String primaryGroupId) {
		this.primaryGroupId = primaryGroupId;
		return this;
	}
	
	public String getEmail() {
		return email;
	}
	
	public User setEmail(String email) {
		this.email = email;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
	public User setDescription(String description) {
		this.description = description;
		return this;
	}
}