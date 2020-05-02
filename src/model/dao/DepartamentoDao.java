package model.dao;

import java.util.List;

import model.entities.Departamento;

public interface DepartamentoDao {

	void insert (Departamento obj); // Operação responsável para inserir dados no banco
	void update (Departamento obj); // Operação responsável para atualizar dados no banco
	void deleteById (Integer id); // Operação responsável para deletar dados no banco
	Departamento findById (Integer id); // Operação responsável para consultar o objeto no banco de dados
	List<Departamento> findAll(); // Operação resposável para buscar todos os Depart. do banco de dados
}
