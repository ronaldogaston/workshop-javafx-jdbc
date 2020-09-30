package model.service;

import java.util.List;

import model.dao.VendedorDao;
import model.dao.FabricaDao;
import model.entities.Vendedor;

public class ServicoVendedor {
	
	private VendedorDao dao = FabricaDao.cadastroVendedorDao();
	
	public List<Vendedor> findAll(){
		return dao.findAll();
	}

	public void inserirOuAtualizarVendedor(Vendedor dep) {
		if (dep.getId() == null) {
			dao.insert(dep);
		}
		else {
			dao.update(dep);
		}
	}
	
	public void remove(Vendedor dep) {
		dao.deleteById(dep.getId());
	}
}
