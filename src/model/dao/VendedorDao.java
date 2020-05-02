package model.dao;

import java.util.List;

import model.entities.Departamento;
import model.entities.Vendedor;

public interface VendedorDao {
	
	void insert (Vendedor obj); // Opera��o respons�vel para inserir dados no banco
	void update (Vendedor obj); // Opera��o respons�vel para atualizar dados no banco
	void deleteById (Integer id); // Opera��o respons�vel para deletar dados no banco
	Vendedor findById (Integer id); // Opera��o respons�vel para consultar o objeto no banco de dados
	List<Vendedor> findAll(); // Opera��o respos�vel para buscar todos os Vendedor do banco de dados
	List<Vendedor> findByDepartamento(Departamento departamento); // Busca vendedor por departamento
}
