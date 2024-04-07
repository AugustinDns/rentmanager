package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {
	
	private VehicleDao() {}

	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	
	public int create(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, vehicle.constructeur());
			ps.setString(2, vehicle.modele());
			ps.setInt(3, vehicle.nb_places());
			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new DaoException("Aucune clé autogénérée de retourné.");
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public int delete(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY)) {
			ps.setInt(1, vehicle.id());
			int nbDeletedRows = ps.executeUpdate();
			if (nbDeletedRows == 0) {
				throw new DaoException("Aucun véhicule n'a été supprimé.");
			} else {
				return nbDeletedRows;
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public Vehicle findById(int id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY)) {
			ps.setInt(1, id);
			ResultSet resultset = ps.executeQuery();
			if (resultset.next()){
				return new Vehicle(resultset.getInt(1), resultset.getString(2), resultset.getString(3), resultset.getInt(4));
			} else {
				throw new DaoException("Le véhicule recherché n'existe pas.");
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public List<Vehicle> findAll() throws DaoException {
		List<Vehicle> res = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_VEHICLES_QUERY)) {
			ResultSet resultset = ps.executeQuery();
			while (resultset.next()){
				Vehicle vehicle = new Vehicle(resultset.getInt(1), resultset.getString(2), resultset.getString(3), resultset.getInt(4));
				res.add(vehicle);
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
		return res;
		
	}
	

}
