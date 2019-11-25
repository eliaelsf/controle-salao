package com.fabiorHair.controlesalao.service;

import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fabiorHair.controlesalao.entity.Item;
import com.fabiorHair.controlesalao.entity.Pedido;
import com.fabiorHair.controlesalao.geral.SatusPedido;
import com.fabiorHair.controlesalao.geral.SituacaoItem;
import com.fabiorHair.controlesalao.model.ItemDTO;
import com.fabiorHair.controlesalao.model.PedidoDTO;
import com.fabiorHair.controlesalao.repository.PedidoRepository;
import com.fabiorHair.controlesalao.repository.PessoaRepository;

@Service
public class PedidoService {
	
	@Resource
	private PedidoRepository pedidoRepository;
	
	@Resource
	private PessoaRepository pessoaRepository;
	
	
	public Pedido criarPedido(PedidoDTO pedidoDTO) {
		
		return pedidoRepository.save(this.converterDTOparaPedido(pedidoDTO));
	}
	
	private Pedido converterDTOparaPedido(PedidoDTO pedidoDTO) {
		
		Pedido pedido = new Pedido();
		pedido.setStatus(SatusPedido.PAGAMENTO_EFETUADO.getStatus());
		pedido.setDataAtualizacao(new Date());
		pedido.setDataCriacao(new Date());
		pedido.setCliente(pessoaRepository.findById(pedidoDTO.getIdCliente()).get());
		
		pedido.setItens( pedidoDTO.getItens().stream()
				         .map(item -> this.converterDTOparaItem(item, pedido))
				         .collect(Collectors.toList()));
		return pedido;
		
	}
	
	private Item converterDTOparaItem(ItemDTO itemDTO, Pedido pedido) {
		Item item = new Item ();
		item.setDescricao(itemDTO.getDescricao());
		item.setQuantidade(itemDTO.getQuantidade());
		item.setValor(itemDTO.getValor());
		item.setSituacao(SituacaoItem.AGUARDANDO_AGENDAMENTO.getSituacao());
		item.setPedido(pedido);
		
		return item;
	}

}
