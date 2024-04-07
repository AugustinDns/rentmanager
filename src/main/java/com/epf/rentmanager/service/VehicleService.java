package com.epf.rentmanager.service;

import java.util.List;
import java.time.LocalDate;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.VehicleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

	@Autowired
	private ReservationDao reservationDao;

	private final VehicleDao vehicleDao;
	private VehicleService(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}
	
	public int create(Vehicle vehicle) throws ServiceException {
		try {
			String manufacturer = vehicle.constructeur();
			String modele = vehicle.modele();
			int nbPlaces = vehicle.nb_places();

			if (manufacturer == null || modele == null || manufacturer.isEmpty() || modele.isEmpty()) {
				throw new ServiceException("Le véhicule doit posséder un constructeur et un modèle.");
			} else if (nbPlaces < 2 || nbPlaces > 9) {
				throw new ServiceException("Le nombre de places dans le véhicule doit être compris entre 2 et 9.");
			} else {
				return vehicleDao.create(vehicle);
			}
		} catch  (DaoException e) {
			throw new ServiceException("Impossible de créer un nouveau véhicule.");
		}
	}

	public int delete(Vehicle vehicle) throws ServiceException {
		/**
		 * Cette fonction regarde dans un premier temps s'il existe des reservations actives avec le véhicule donné.
		 * Cette fonction modifie dans un second temps toutes les réservations liées au véhicule en remplaçant la id du véhicule par la valeur null.
		 * Cette fonction supprime dans un troisième temps le véhicule donné.
		 */
		try {
			List<Reservation> vehicleReservations = reservationDao.findResaByVehicleId(vehicle.id());
			if (!vehicleReservations.isEmpty()) {
				for (Reservation reservation : vehicleReservations) {
					if (reservation.debut().isBefore(LocalDate.now()) && reservation.fin().isAfter(LocalDate.now())) {
						throw new ServiceException("Le véhicule fait partie d'une réservation en cours. Il est donc impossible de le supprimer pour le moment.");
					}
				}
				int deleted_reservations = 0;
				for (Reservation reservation : vehicleReservations) {
					deleted_reservations += reservationDao.delete(reservation);
				}
				if (deleted_reservations != vehicleReservations.size()) {
					throw new ServiceException("Toutes les réservations du véhicule n'ont pas pu être supprimées de la base de données.");
				}
			}
		} catch (DaoException e) {
			throw new ServiceException("Problème(s) rencontré(s) lors du checking des réservations actives du véhicule.");
		}
		try {
			return vehicleDao.delete(vehicle);
		} catch (DaoException e) {
			throw new ServiceException("Impossible de supprimer le véhicule de la base de données.");
		}
	}

	public Vehicle findById(int id) throws ServiceException {
		try {
			return vehicleDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public List<Vehicle> findAll() throws ServiceException {
		try {
			return vehicleDao.findAll();
		} catch (DaoException e) {
			throw new ServiceException();
		}
	}
	
}
