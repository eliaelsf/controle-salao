package com.fabiorHair.controlesalao.service;


import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fabiorHair.controlesalao.entity.Produto;
import com.fabiorHair.controlesalao.model.ProdutoDTO;
import com.fabiorHair.controlesalao.repository.ProdutoRepository;
import com.fabiorHair.controlesalao.util.StringUtil;

@Service
public class ProdutoService {
	
	@Resource
	private ProdutoRepository produtoDao;
	
    public void  cadastrar (ProdutoDTO produtoDTO){   
    	Produto produto = new Produto();
    	this.atualizarEnt(produtoDTO, produto);
    	produtoDao.save(produto);    	
    }
	
    public void atualizar(ProdutoDTO produtoDTO){
    	Produto produto = produtoDao.findById(produtoDTO.getId()).get();
    	this.atualizarEnt(produtoDTO, produto);
    	produtoDao.saveAndFlush(produto);
    }
    
    public ProdutoDTO findById(Long id){
    	return this.converteEntToDto(produtoDao.findById(id).get());
    }
    
    public void adicionarImagemProduto(String descricao, byte[] imagem) {
    	List<Produto> produtos = produtoDao.findByDescricaoLike(descricao);
    	Produto prd = produtos.get(0);
    	prd.setImagem(imagem);
    	produtoDao.saveAndFlush(prd);
    	
    }    
    
    public List<ProdutoDTO> findProdutos(){    	
    	List <Produto> produtos = produtoDao.findAll();    	
    	return produtos.stream()
 			   .map(prod -> this.converteEntToDto(prod))
 			   .sorted(Comparator.comparing(ProdutoDTO::getDescricao))
 			   .collect((Collectors.toList()));
    }
    
    public List<ProdutoDTO> findProdutosVigentes(){    
    	Date dataAtual = new Date();
    	return this.findProdutos().stream()
    			   .filter(prod -> (prod.getDataValidade() == null || prod.getDataValidade().after(dataAtual)))
    			   .collect((Collectors.toList()));
    }  
   
    
    public List<Produto>findByDescricao(String descricao){
    	return produtoDao.findByDescricao(descricao);
    }
    
    public boolean isProdutoExistente(String descricao){
    	List<Produto> produtos = produtoDao.findByDescricao(descricao);
    	return (produtos != null && !produtos.isEmpty());
    }     
    
    private void atualizarEnt(ProdutoDTO dto, Produto produto){    	
    	produto.setDescricao(dto.getDescricao());
    	produto.setValor(dto.getValor());
    	produto.setDataFimVigencia(dto.getDataValidade());
    	if(dto.getImagem() != null) {
    		produto.setImagem(dto.getImagem());
    	}
    	
    }
    
    private ProdutoDTO converteEntToDto(Produto produto){
    	ProdutoDTO dto = new ProdutoDTO ();
    	dto.setId(produto.getId());
    	dto.setDescricao(produto.getDescricao());
    	dto.setValor(produto.getValor());
    	dto.setDataValidade(produto.getDataFimVigencia());
    	dto.setImagem(produto.getImagem());
    	return dto;
    }   
    
}
