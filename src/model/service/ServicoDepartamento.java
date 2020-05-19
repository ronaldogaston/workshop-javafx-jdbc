package model.service;

import java.util.List;

import model.dao.DepartamentoDao;
import model.dao.FabricaDao;
import model.entities.Departamento;

public class ServicoDepartamento {
	
	private DepartamentoDao dao = FabricaDao.cadastroDepartamentoDao();
	
	public List<Departamento> findAll(){
		return dao.findAll();
	}

	public void inserirOuAtualizarDepartamento(Departamento dep) {
		if (dep.getId() == null) {
			dao.insert(dep);
		}
		else {
			dao.update(dep);
		}
	}
	
	public void remove(Departamento dep) {
		dao.deleteById(dep.getId());
	}
}
