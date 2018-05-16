package com.msn.registrar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class ResourceNotFoundException extends RuntimeException {
    private String nomRessouce;
    private String nomDomaine;
    private Object valeur;

    public ResourceNotFoundException( String nomRessouce, String nomDomaine, Object valeur) {
        super(String.format("%s non trouvé avec %s : '%s'", nomRessouce, nomDomaine, valeur));
        this.nomRessouce = nomRessouce;
        this.nomDomaine = nomDomaine;
        this.valeur = valeur;
    }

    public String getNomRessouce() {
        return nomRessouce;
    }

    public String getNomDomaine() {
        return nomDomaine;
    }

    public Object valeur() {
        return valeur;
    }
}
