package com.epf.rentmanager.model;

import java.time.LocalDate;

public record Reservation(int id, Client client, Vehicle vehicle, LocalDate debut, LocalDate fin) {

    public Reservation() {
        this(0, null, null, null, null);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client_id=" + client.id() +
                ", vehicle_id=" + vehicle.id() +
                ", debut=" + debut +
                ", fin=" + fin +
                '}';
    }
}
