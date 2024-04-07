package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ClientDao clientDao;
    @Autowired
    private VehicleDao vehicleDao;

    private final ReservationDao reservationDao;
    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public int create(Reservation reservation) throws ServiceException {
        try {
            int reservationVehicleId = reservation.vehicle().id();
            int reservationClientId = reservation.client().id();
            LocalDate dateDebut = reservation.debut();

            try {
                Client client1 = clientDao.findById(reservationClientId);
                System.out.println(client1);
            } catch (DaoException e) {
                throw new ServiceException("Impossible de créer la réservation, le client donné n'existe pas.");
            }
            try {
                vehicleDao.findById(reservationVehicleId);
            } catch (DaoException e) {
                throw new ServiceException("Impossible de créer la réservation, le véhicule donné n'existe pas.");
            }
            List<Reservation> allVehicleReservations = reservationDao.findResaByVehicleId(reservationVehicleId);
            for (Reservation reservation1 : allVehicleReservations) {
                if ((reservation1.debut()).equals(dateDebut)) {
                    // Besoin de fixer la condition car celle-ci ne prend pas en compte si un vehicule est reserve sur plusieurs jours.
                    throw new ServiceException("La voiture sélectionnée est déjà réservée sur cette journée. Veuillez choisir une autre date.");
                }
            }

            return reservationDao.create(reservation);

        } catch (DaoException e) {
            throw new ServiceException("Impossible de créer une nouvelle réservation.");
        }
    }

    public int delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de supprimer la réservation de la base de données.");
        }
    }

    public List<Reservation> findResaByClientId(int clientId) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<Reservation> findResaByVehicleId(int vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<Reservation> findResaByVehicleIdAndClientId(int clientId, int vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleIdAndClientId(clientId, vehicleId);
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException();
        }
    }

}
