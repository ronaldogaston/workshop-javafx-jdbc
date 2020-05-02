package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao {

	private Connection conn;
	
	public DepartamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Departamento departamento) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO departamento "
					+ "(Nome) "
					+ "VALUES "
					+ "(?)", 
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, departamento.getNome());
			
			int linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					departamento.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado, nehuma linha afetada!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Departamento departamento) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE Departamento "
					+ "SET Nome = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, departamento.getNome());
			st.setInt(2, departamento.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public void deleteById(Integer id) {
PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM departamento WHERE Id = ?");
			
			st.setInt(1, id);
			
			int linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas == 0) {
				throw new DbException("Nenhuma linha foi afetada.");
			}
			else {
				System.out.println("Deletado com sucesso!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public Departamento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM departamento "
					+ "WHERE id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Departamento dep = instanciaDepartamento(rs);
				return dep;
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Departamento instanciaDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("ID"));
		dep.setNome(rs.getString("Nome"));
		return dep;
	}

	@Override
	public List<Departamento> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT * "
					+ "FROM departamento "
					+ "ORDER BY Nome");
			
			rs = st.executeQuery();
			
			List<Departamento> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>(); // Será guardado qualquer departamento instaciado
						
			while (rs.next()) {
				
				Departamento dep = map.get(rs.getInt("Id")); // Busca o ID do Departamento do Banco
				
				if (dep == null) { // Realiza a instanciação caso o 'dep' tenha vindo como nulo acima
					dep = instanciaDepartamento(rs);
					map.put(rs.getInt("Id"), dep);
				}
				list.add(dep);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
}
