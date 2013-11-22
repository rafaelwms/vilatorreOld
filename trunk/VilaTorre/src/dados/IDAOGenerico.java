package dados;


import java.util.List;

public interface IDAOGenerico<Entidade> {

	public void inserir(Entidade entidade);

	public void alterar(Entidade entidade);

	public void remover(Entidade entidade);

	public Entidade consultarPorId(Integer id);

	public List<Entidade> consultarTodos();

}
