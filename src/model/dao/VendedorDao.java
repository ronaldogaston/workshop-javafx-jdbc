package model.dao;

import java.util.List;

import model.entities.Departamento;
import model.entities.Vendedor;

public interface VendedorDao {
	
	void insert (Vendedor obj); // Operação responsável para inserir dados no banco
	void update (Vendedor obj); // Operação responsável para atualizar dados no banco
	void deleteById (Integer id); // Operação responsável para deletar dados no banco
	Vendedor findById (Integer id); // Operação responsável para consultar o objeto no banco de dados
	List<Vendedor> findAll(); // Operação resposável para buscar todos os Vendedor do banco de dados
	List<Vendedor> findByDepartamento(Departamento departamento); // Busca vendedor por departamento
}
