package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class ServicoDepartamento {
	
	public List<Departamento> findAll(){
		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Livros"));
		list.add(new Departamento(2, "Computadores"));
		list.add(new Departamento(3, "Eletr�nicos"));
		return list;
	}

}
