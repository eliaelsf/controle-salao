package com.fabiorHair.controlesalao.contoller;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fabiorHair.controlesalao.geral.Role;
import com.fabiorHair.controlesalao.geral.RolePessoa;
import com.fabiorHair.controlesalao.model.EnvioEmail;
import com.fabiorHair.controlesalao.model.PessoaDTO;
import com.fabiorHair.controlesalao.model.ProdutoDTO;
import com.fabiorHair.controlesalao.seguranca.annotation.Privado;

import com.fabiorHair.controlesalao.service.EnvioEmailService;
import com.fabiorHair.controlesalao.service.PessoaService;
import com.fabiorHair.controlesalao.service.ProdutoService;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/salao")
public class SalaoController  {	
	
	private PessoaService pessoaService;	
	private ProdutoService produtoService;	
	private EnvioEmailService envioEmailService;
	
	@Privado(role=Role.ROLE_GERAL)
	@PostMapping(value="/cadastroPessoa")
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrarPessoa(@RequestBody PessoaDTO pessoa){
		
		if(this.pessoaService.existePessoaComCpf(pessoa.getCpf())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pessoa já cadastrada.");
		}
		pessoaService.cadastraPessoa(pessoa);
	}	
	
	@Privado(role=Role.ROLE_GERAL)
	@PostMapping(value="/cadastroProfissional")	
	public void cadastrarProfissional(@RequestBody PessoaDTO pessoa){
		pessoa.setTipo(RolePessoa.PROFISSIONAL);
		this.cadastrarPessoa(pessoa);
	}	
	
	@Privado(role=Role.ROLE_ADMIN)
	@RequestMapping(value="/cadastroProduto", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrarProduto(@RequestBody ProdutoDTO produto){		
		if(!this.produtoService.isProdutoExistente(produto.getDescricao())){
			 produtoService.cadastrar(produto);	
		}else{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto já casdastrado com essa descrição.");
		}			
	}
	@Privado(role=Role.ROLE_ADMIN)
	@PostMapping("/castradoProduImagem")
	public void cadastrarProdutoComImagem(@RequestParam("file") MultipartFile file, @RequestParam("descricao") String descricao){		
		try {
			this.produtoService.adicionarImagemProduto(descricao, file.getBytes());
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao vincular imagem ao produto.");
		}	
	}	
	
	@Privado(role=Role.ROLE_ADMIN)
	@GetMapping("/produtos")
	public ResponseEntity<List<ProdutoDTO>> buscarProdutos(){	
		return ResponseEntity.ok().body(produtoService.findProdutos());
	}
	
	@Privado(role=Role.ROLE_GERAL)
	@GetMapping("/produtosVigentes")
	public ResponseEntity<List<ProdutoDTO>> buscarProdutosVigentes(){	
		return ResponseEntity.ok().body(produtoService.findProdutosVigentes());
	}
	
	@Privado(role=Role.ROLE_GERAL)
	@GetMapping("/produtos/{idProduto}")	
	public ResponseEntity<ProdutoDTO> buscarProdutoByiD(@PathVariable Long idProduto){
		ProdutoDTO produto = produtoService.findById(idProduto);
		if(produto == null){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(produto);
	}
	
	@Privado(role=Role.ROLE_ADMIN)
	@RequestMapping(value="/atulizarProduto", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void atualizarProduto(@RequestBody ProdutoDTO produto){
		produtoService.atualizar(produto);
	}
	
	@Privado(role=Role.ROLE_ADMIN)
	@GetMapping("/profissionais")	
	public ResponseEntity<List<PessoaDTO>> buscarPessoas(){		
		return ResponseEntity.ok().body(pessoaService.buscarProfissionais());
	}
	
	@Privado(role=Role.ROLE_ADMIN)
	@PostMapping("/enviarEmails")
	@ResponseStatus(HttpStatus.OK)
	public void enviarEmails(@RequestBody EnvioEmail envioEmail) {
		envioEmailService.envioEmailParalelo(envioEmail);		
	}	
	
}
