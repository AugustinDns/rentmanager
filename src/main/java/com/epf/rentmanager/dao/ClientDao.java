package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exception.DaoException;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private ClientDao() {}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
	public int create(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, client.nom());
			ps.setString(2, client.prenom());
			ps.setString(3, client.email());
			ps.setDate(4, Date.valueOf(client.dateNaissance()));
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
	
	public int delete(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)) {
			ps.setInt(1, client.id());
			int nbDeletedRows = ps.executeUpdate();
			if (nbDeletedRows == 0) {
				throw new DaoException("Aucun client n'a été supprimé.");
			} else {
				return nbDeletedRows;
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public Client findById(int id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY)) {
			ps.setInt(1, id);
			ResultSet resultset = ps.executeQuery();
			if (resultset.next()){
				return new Client(resultset.getInt(1), resultset.getString(2), resultset.getString(3), resultset.getString(4), resultset.getDate(5).toLocalDate());
			} else {
				throw new DaoException("Le client recherché n'existe pas.");
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public List<Client> findAll() throws DaoException {
		List<Client> res = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_CLIENTS_QUERY)) {
			ResultSet resultset = ps.executeQuery();
			while (resultset.next()){
				Client client = new Client(resultset.getInt(1), resultset.getString(2), resultset.getString(3), resultset.getString(4), resultset.getDate(5).toLocalDate());
				res.add(client);
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
		return res;
	}

}
