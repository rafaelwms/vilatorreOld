package basicas;



import java.util.List;

import javax.persistence.*;


@Entity
public class Produto extends ObjetoGeral  {
	
	@Column
	private double preco;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="produto_ingredientes")
	private List<Ingrediente> ingredientes;
	
	@Enumerated(EnumType.STRING)
	private Categoria categoria;
	
	
	public enum Categoria{Bebidas, Burgers, SuperBurgers, Salgados, Sobremesa}
	
	public Produto(){}
	
	public Produto(String nome, String desc, double preco){}
	
	public double getPreco() {
		return preco;
	}


	public void setPreco(double preco) {
		this.preco = preco;
	}


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

}
