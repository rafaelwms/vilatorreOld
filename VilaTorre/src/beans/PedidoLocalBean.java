package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;


import fachadas.Fachada;

import basicas.*;
import basicas.Pagamento.Bandeira;
import basicas.Pagamento.FormaPagamento;
import basicas.Pedido.TipoPedido;

@ManagedBean
public class PedidoLocalBean {
	
	private Pedido pedido = new Pedido();
	
	private Pagamento pagamento = new Pagamento();
	
	private int condicao;
	
	private FormaPagamento forma;
	
	private FormaPagamento[] formas;
	
	private Bandeira bandeira;
	
	private Bandeira[] bandeiras;
	
	private String desconto = new String();
	
	private List<Pedido> todosPedidos = new ArrayList<Pedido>();
	
	private List<Pedido> pedidosAbertos = new ArrayList<Pedido>();
	
	private Produto produto = new Produto();
	
	private List<Produto> produtos = new ArrayList<Produto>();
	
	private ItemPedido item = new ItemPedido();
	
	private List<ItemPedido> itemsPedido = new ArrayList<ItemPedido>();
	
	private TipoPedido[] tipos;
	
	private TipoPedido tipoPedido;
	
	private int qtd;
	
	private int mesa;
	
	public PedidoLocalBean(){}
	
	
	public String abrirPedido(){
		
		try{
			
			if (tipoPedido == TipoPedido.Selecione){
				throw new Exception("� necess�rio selecionar o tipo de pedido.");
			}
			pedido.setTipo_pedido(tipoPedido);
			
			pedido.setId_pedido(null);
			item.setId_item(null);
			item.setProduto(produto);	
			item.setQtd(qtd);
			item.setNumOrdem(1);	
			pedido.setAbertura_pedido(new Date());
			pedido.setNum_mesa(mesa);
			pedido.setStatus_aberto(true);			
			itemsPedido.add(item);
			pedido.setLista_itens(itemsPedido);
			pedido.setValor_total(item.getTotalItem());
			item.setPedido(pedido);
			Fachada.getInstancia().inserirPedido(pedido);
			produto = new Produto();
			pedido = new Pedido();
			item = new ItemPedido();
			itemsPedido = new ArrayList<ItemPedido>();
			mesa = 0;
			qtd = 0;
			return null;
		}catch(Exception ex){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
			return null;	
		}
	}
	
	
	public String addItem(Pedido para1){
		try{
		item.setId_item(null);
		item.setPedido(para1);
		itemsPedido = new ArrayList<ItemPedido>();
		itemsPedido = para1.getLista_itens();
		
		item.setNumOrdem(itemsPedido.size() + 1);
		item.setProduto(produto);	
		item.setQtd(qtd);
		item.setTotalItem(item.getProduto().getPreco() * item.getQtd());
		itemsPedido.add(item);
		
		
		para1.setLista_itens(itemsPedido);
		
		for (ItemPedido it : para1.getLista_itens()){
			
			para1.setValor_total(para1.getValor_total() + it.getProduto().getPreco());
			
		}
		
		Fachada.getInstancia().adicionarItemPedido(para1, item);
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item adicionado com �xito."));
		produto = new Produto();
		pedido = new Pedido();
		item = new ItemPedido();
		itemsPedido = new ArrayList<ItemPedido>();
		mesa = 0;
		qtd = 0;
		return null;
		}catch(Exception ex){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
			return null;
		}
	}
	
	
	public String listarItems(Pedido param){
	
		
		try {
			
			pedido = param;
			
			itemsPedido = pedido.getLista_itens();
			System.out.println(itemsPedido);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Era pra pegar essa buceta."));
			return null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public String pagar(Pedido param){
		try{
			
			if(forma == FormaPagamento.Selecione){
				throw new Exception("� necess�rio selecionar uma forma de pagamento.");
			}
			
			if((forma == FormaPagamento.CR�DITO)  && (bandeira == Bandeira.Selecione)){
				throw new Exception("Para pagamento em cr�dito � necess�rio escolher uma bandeira.");
			}
			
			if((forma == FormaPagamento.D�BITO)  && (bandeira == Bandeira.Selecione)){
				throw new Exception("Para pagamento em d�bito � necess�rio escolher uma bandeira.");
			}
			
			if(!desconto.trim().equals("")){
				desconto = desconto.replace(".", "");
				desconto = desconto.replace(",", ".");
				pagamento.setDesconto(Double.parseDouble(desconto));				
			}
				pagamento.setId(null);
				pagamento.setForma_pagamento(forma);
				
			if(forma == FormaPagamento.D�BITO || forma == FormaPagamento.CR�DITO){
					pagamento.setBandeira(bandeira);
			}else{
					pagamento.setBandeira(null);
			}
				
				param.getPagamentos().add(pagamento);
				
				Fachada.getInstancia().alterarPedido(param);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Pagamento efetuado com �xito."));
			if(condicao == 1){
				param.setFechamento_pedido(new Date());
				param.setStatus_aberto(false);
			}


			return null;
		}catch(Exception ex){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
			return null;
		}
	}
	
	

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Pedido> getTodosPedidos() {
		try {
			return Fachada.getInstancia().consultarTodosPedido();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void setTodosPedidos(List<Pedido> todosPedidos) {
		this.todosPedidos = todosPedidos;
	}

	public List<Pedido> getPedidosAbertos() {
		pedidosAbertos = new ArrayList<Pedido>();
		try{
			for(Pedido check: Fachada.getInstancia().consultarTodosPedido()){
				if(check.isStatus_aberto() == true){
					pedidosAbertos.add(check);
				}
			}
			return pedidosAbertos;
		}catch(Exception ex){
			return null;
		}
	}

	public void setPedidosAbertos(List<Pedido> pedidosAbertos) {
		this.pedidosAbertos = pedidosAbertos;
	}

	public ItemPedido getItem() {
		return item;
	}

	public void setItem(ItemPedido item) {
		this.item = item;
	}

	public List<ItemPedido> getItemsPedido() {
		return itemsPedido;
	}

	public void setItemsPedido(List<ItemPedido> itemsPedido) {
		this.itemsPedido = itemsPedido;
	}

	public TipoPedido getTipoPedido() {
		return tipoPedido;
	}

	public void setTipoPedido(TipoPedido tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public TipoPedido[] getTipos() {
		return TipoPedido.values();
	}


	public Produto getProduto() {
		return produto;
	}


	public void setProduto(Produto produto) {
		this.produto = produto;
	}


	public List<Produto> getProdutos() {
		try {
			return Fachada.getInstancia().consultarTodosProduto();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}


	public int getQtd() {
		return qtd;
	}


	public void setQtd(int qtd) {
		this.qtd = qtd;
	}


	public int getMesa() {
		return mesa;
	}


	public void setMesa(int mesa) {
		this.mesa = mesa;
	}


	public Pagamento getPagamento() {
		return pagamento;
	}


	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}


	public FormaPagamento getForma() {
		return forma;
	}


	public void setForma(FormaPagamento forma) {
		this.forma = forma;
	}


	public Bandeira getBandeira() {
		return bandeira;
	}


	public void setBandeira(Bandeira bandeira) {
		this.bandeira = bandeira;
	}


	public String getDesconto() {
		return desconto;
	}


	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}


	public FormaPagamento[] getFormas() {
		return FormaPagamento.values();
	}


	public Bandeira[] getBandeiras() {
		return Bandeira.values();
	}


	public int getCondicao() {
		return condicao;
	}


	public void setCondicao(int condicao) {
		this.condicao = condicao;
	}
	

}