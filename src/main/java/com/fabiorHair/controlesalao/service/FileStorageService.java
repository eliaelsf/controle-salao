

package com.fabiorHair.controlesalao.service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fabioHair.controlesalao.properties.FileStorageProperties;
import com.fabiorHair.controlesalao.exception.FileStorageException;
import com.fabiorHair.controlesalao.model.Arquivo;

@Service
public class FileStorageService {
	
	private final Path fileStorageLocation;
	
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {    	
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
            .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
    public String storeFile(MultipartFile file) {        
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    public List<Arquivo> obtemArquivos(){    	
    	return Arrays.asList(this.fileStorageLocation.toFile().listFiles()).stream()
    			.filter(file -> file.isFile() && file.getName().endsWith(".txt"))
    			.map(file -> new Arquivo(file.getName(), file.getPath(),".txt" ,file.length()))
    			.collect(Collectors.toList());
    }
    
    public byte[] obtemArquivo(String fileName) {
    	try {
    		return Files.readAllBytes(this.fileStorageLocation.resolve(fileName).normalize());
    	}catch(IOException ex) {
    		throw new FileStorageException("Não pode encontar o arquvi " + fileName + ". Please try again!", ex);
    	}
    }
    
    public Path obterArquivo(String fileName) {    	
    	return this.fileStorageLocation.resolve(fileName).normalize();    	
    }
    
    public void delete(String fileName) {
    	this.fileStorageLocation.resolve(fileName).toFile().delete();
    }

}
