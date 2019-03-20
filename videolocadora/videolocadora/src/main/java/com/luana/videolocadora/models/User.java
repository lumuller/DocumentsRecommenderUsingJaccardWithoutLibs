package com.luana.videolocadora.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_users")
public class User {
	
	@Id
	private String username;
	
	private String name;
	
	private String role;
	
	private String password;
	
	/*@OneToMany(mappedBy = "usuario", cascade = {CascadeType.PERSIST})
    private List<Locacao> locacoes;*/
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String username, String name, String role, String password) {
		super();
		this.username = username;
		this.name = name;
		this.role = role;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
}

