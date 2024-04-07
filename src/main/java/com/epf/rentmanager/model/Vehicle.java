package com.epf.rentmanager.model;

public record Vehicle(int id, String constructeur, String modele, int nb_places) {

    public Vehicle() {
        this(0, null, null, 0);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", constructeur='" + constructeur + '\'' +
                ", modele='" + modele + '\'' +
                ", nb_places=" + nb_places +
                '}';
    }
}
