package model.dao;

import java.util.List;

import model.entities.Departamento;

public interface DepartamentoDao {

	void insert (Departamento obj); // Opera��o respons�vel para inserir dados no banco
	void update (Departamento obj); // Opera��o respons�vel para atualizar dados no banco
	void deleteById (Integer id); // Opera��o respons�vel para deletar dados no banco
	Departamento findById (Integer id); // Opera��o respons�vel para consultar o objeto no banco de dados
	List<Departamento> findAll(); // Opera��o respos�vel para buscar todos os Depart. do banco de dados
}
