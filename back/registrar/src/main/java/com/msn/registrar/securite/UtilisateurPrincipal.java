package com.msn.registrar.securite;

import com.msn.registrar.modeles.Utilisateurs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class UtilisateurPrincipal implements UserDetails{

	private static final long serialVersionUID = 1L;
	private Long id;
    private String nom;
    private String prenom;
    private String username;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> autorites;

    public UtilisateurPrincipal(Long id, String nom, String prenom, String email, String password
    		,Collection<? extends GrantedAuthority> autorites) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.username = email;
		this.password = password;
		this.autorites = autorites;
	}
    
    public static UtilisateurPrincipal creer(Utilisateurs utilisateur) {
		List<GrantedAuthority> autorites = utilisateur.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getType().name())).collect(Collectors.toList());

        return new UtilisateurPrincipal(
        		utilisateur.getId(),
        		utilisateur.getNom(),
        		utilisateur.getPrenom(),
        		utilisateur.getEmail(),
        		utilisateur.getPassword(),
        		autorites
        );
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtilisateurPrincipal that = (UtilisateurPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return autorites;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
}
