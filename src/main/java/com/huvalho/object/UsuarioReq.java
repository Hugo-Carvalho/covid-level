package com.huvalho.object;

import javax.persistence.Column;

public class UsuarioReq {

    private String telefone;
    private String[] contatos;
    private String[] cidades;
    private Boolean infectado;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String[] getContatos() {
        return contatos;
    }

    public void setContatos(String[] contatos) {
        this.contatos = contatos;
    }

    public String[] getCidades() {
        return cidades;
    }

    public void setCidades(String[] cidades) {
        this.cidades = cidades;
    }

    public Boolean getInfectado() {
        return infectado;
    }

    public void setInfectado(Boolean infectado) {
        this.infectado = infectado;
    }
}
