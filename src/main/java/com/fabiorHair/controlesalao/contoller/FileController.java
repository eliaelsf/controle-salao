package com.fabiorHair.controlesalao.contoller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fabiorHair.controlesalao.geral.Role;
import com.fabiorHair.controlesalao.model.Arquivo;
import com.fabiorHair.controlesalao.seguranca.OAuthController;
import com.fabiorHair.controlesalao.seguranca.annotation.Privado;
import com.fabiorHair.controlesalao.service.FileStorageService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/file")
public class FileController extends OAuthController {	

  private FileStorageService fileStorageService;	

  @Privado(role=Role.ROLE_ADMIN)
  @PostMapping("/upload")
  @ResponseStatus(HttpStatus.OK)
  public Arquivo uploadFile(@RequestParam("file") MultipartFile file) {
	  String fileName = this.fileStorageService.storeFile(file);
	  String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/downloadFile/")
	            .path(fileName)
	            .toUriString();
	  
	  return new Arquivo(fileName, fileDownloadUri,
	            file.getContentType(), file.getSize());
  }
  
  @Privado(role=Role.ROLE_ADMIN)
  @PostMapping("/uploadMultipleFiles")
  public ResponseEntity<List<Arquivo>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
	  
      return ResponseEntity.ok().body(Arrays.asList(files)
          .stream()
          .map(file -> uploadFile(file))
          .collect(Collectors.toList()));
  }
  
  @Privado(role=Role.ROLE_ADMIN)
  @GetMapping("/arquivos")
  public ResponseEntity<List<Arquivo>> buscarAqurivos(){
	  return ResponseEntity.ok().body(this.fileStorageService.obtemArquivos());
  }
  
  @Privado(role=Role.ROLE_ADMIN)
  @GetMapping("/download2/{fileName}")
  public HttpEntity<byte[]> download(@PathVariable String fileName) {
	  
	  byte[] arquivo = this.fileStorageService.obtemArquivo(fileName);
	  HttpHeaders httpHeaders = new HttpHeaders();
	  httpHeaders.add("Content-Disposition", "attachment;filename=\""+ fileName +"\"");
	  
	  HttpEntity<byte[]> entity = new HttpEntity<byte[]>( arquivo, httpHeaders);

	  return entity;
  }
  
  @Privado(role=Role.ROLE_ADMIN)
  @GetMapping("/download/{fileName}")
  public void download(HttpServletResponse response, @PathVariable("fileName") String fileName) {	  
      
      Path arquivo = this.fileStorageService.obterArquivo(fileName);

      if(Files.exists(arquivo)) {
          response.setHeader("Content-Disposition", "inline");
          response.setContentType("application/pdf");            
          try {
              Files.copy(arquivo, response.getOutputStream());
              response.getOutputStream().flush();
          } 
          catch (IOException ex) {
              ex.printStackTrace();
          }
      }
  }
  
  @Privado(role=Role.ROLE_ADMIN)
  @DeleteMapping(path ={"/{fileName}"})
  public ResponseEntity<?> delete (@PathVariable String fileName) {
	  this.fileStorageService.delete(fileName);
	  return ResponseEntity.ok().build();
  }

}
