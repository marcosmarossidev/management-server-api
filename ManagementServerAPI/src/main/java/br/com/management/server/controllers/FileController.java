package br.com.management.server.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.management.server.controllers.advice.ErrorMessage;
import br.com.management.server.core.MediaType;
import br.com.management.server.data.vo.UploadFileResponseVO;
import br.com.management.server.services.FileStorageService;
import br.com.management.server.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/file")
@Tag(name = "File", description = "Endpoints for Managing Files")
public class FileController {
	
	private Logger logger = Logger.getLogger(UserService.class.getName());
	
	@Autowired
	private FileStorageService service;
	
	@PostMapping(path = "/uploadFile", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Do upload file in server disk", description = "Do upload file in server disk", tags = { "File" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UploadFileResponseVO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public UploadFileResponseVO uploadFile(@RequestParam(name = "file") MultipartFile file) {
		
		String filename = service.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/downloadFile/").path(filename).toUriString();
		
		return new UploadFileResponseVO(filename, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping(path = "/uploadMultipleFiles", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Do upload miltiple files in server disk", description = "Do upload miltiple files in server disk", tags = { "File" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UploadFileResponseVO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public List<UploadFileResponseVO> uploadMultipleFiles(@RequestParam(name = "files") MultipartFile[] files) {
		return Arrays.asList(files).stream()
				.map(file -> uploadFile(file))
				.collect(Collectors.toList());
	}
	
	@GetMapping(path = "/downloadFile/{filename:.+}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Download file by name", description = "Download file by name", tags = { "File" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public ResponseEntity<Resource> downloadFile(@PathVariable(name = "filename") String filename, HttpServletRequest request) {
		
		Resource resource = service.loadFileAsResource(filename);
		
		String contentType = "";
		
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			logger.warning("We can't process this type of content");
		}
		
		if(contentType.isBlank()) {
			contentType = "application/octet-stream";
		}
				
		return ResponseEntity.ok()
				.contentType(org.springframework.http.MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
