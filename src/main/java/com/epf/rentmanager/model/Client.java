package com.epf.rentmanager.model;

import java.time.LocalDate;

public record Client(int id, String nom, String prenom, String email, LocalDate dateNaissance) {

    public Client() {
        this(0, null, null, null, null);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", dateNaissance=" + dateNaissance +
                '}';
    }
}
