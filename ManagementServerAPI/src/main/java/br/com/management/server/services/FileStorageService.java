package br.com.management.server.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.management.server.config.FileStorageConfiguration;
import br.com.management.server.exception.FileNotFoundInServerException;
import br.com.management.server.exception.FileStorageException;

@Service
public class FileStorageService {
	
	private final Path fileStorageLocation;

	public FileStorageService(FileStorageConfiguration fileStorageConfiguration) {
		Path path = Paths.get(fileStorageConfiguration.getUploadDir()).toAbsolutePath().normalize();
		
		this.fileStorageLocation = path;
		
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be storaged", e);
		}
	}
	
	public String storeFile(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			if(filename.contains("..")) {
				throw new FileStorageException("File name contains invalid path sequence " + filename);
			}
			
			Path targetLocation = this.fileStorageLocation.resolve(filename);			
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			return filename;
		} catch (Exception e) {
			throw new FileStorageException("Could not store file " + filename + ". Please try again", e);
		}
	}
	

	public Resource loadFileAsResource(String filename) {
		try {
			Path filePath = this.fileStorageLocation.resolve(filename).normalize();
			
			Resource resource = new UrlResource(filePath.toUri());
			
			if(resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundInServerException("File " + filename + " not found");
			}
		} catch (Exception e) {
			throw new FileNotFoundInServerException("File " + filename + " not found", e);
		}
	}
	
}
