package com.fabiorHair.controlesalao.contoller;

import java.util.ArrayList;
import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fabiorHair.controlesalao.model.EmailErro;
import com.fabiorHair.controlesalao.model.EnvioEmail;
import com.fabiorHair.controlesalao.repository.LeitorArquivo;

import com.fabiorHair.controlesalao.service.EnvioEmailService;

// http://localhost:8080/salaoFabio

@RestController
@RequestMapping("/salaoFabio")
public class SalaoContoller {	
	
	@Autowired
	private LeitorArquivo leitorArquivo;
	
	@Autowired
	private EnvioEmailService envioEmailService;	
	
	@RequestMapping()
	public ModelAndView listaEmailErro(){
		List <EmailErro> listaRetorno = new ArrayList<EmailErro>();
		EmailErro emailErro = new EmailErro();
		emailErro.setEmail("eliael.figueiredo@gmail.com");
		emailErro.setErro("Erro");
		listaRetorno.add(emailErro);		
		ModelAndView mv = new ModelAndView("EnviarEmail");
		mv.addObject("envioEmail",new EnvioEmail());		
		mv.addObject("emailErro", new EmailErro());
		mv.addObject("emailErros", listaRetorno);
		
		return mv;
		
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String  enviarEmail(@Valid EnvioEmail envioEmail, BindingResult	result){
		if(result.hasErrors()){
			 return "redirect:/"
			 		+ "Agor";
		}
		
		List<String> emails = this.leitorArquivo.readFromTxt(true);
		List<String> emailsEnviados = this.leitorArquivo.readFromTxt(false);
		this.envioEmailService.enviaEmail(emails, emailsEnviados, envioEmail);
		//this.leitorArquivo.writeToTxt(emailsEnviados);
	    return "redirect:/salaoFabio";
	}

}
