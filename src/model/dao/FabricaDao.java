package model.dao;

import db.DB;
import model.dao.impl.DepartamentoDaoJDBC;
import model.dao.impl.VendedorDaoJDBC;

public class FabricaDao {
	
	/* MACETE:
	 * A classe vai expor um m�todo que
	 * retorna o tipo da interface
	 * mais internamente vai est�nciar uma implementa��o.
	 * Para n�o precisar expor a implenta��o
	 * Deixa somente a interface.
	 */
	
	public static VendedorDao cadastroVendedorDao() { 
		return new VendedorDaoJDBC(DB.getConnection());
	}
	
	public static DepartamentoDao cadastroDepartamentoDao() { 
		return new DepartamentoDaoJDBC(DB.getConnection());
	}
}
