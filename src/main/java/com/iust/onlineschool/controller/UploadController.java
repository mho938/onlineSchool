package com.iust.onlineschool.controller;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tv.samim.tools.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/rest")
@CrossOrigin
public class UploadController {
	private static final String ROOT_ADDRESS = System.getProperty("user.home")
			+ "/online-school/";
	public static final String ROOT_URL = "/rest";
	public static final String FILE = "file";
	public static final String RESOURCE_URL = "/resource/";
	public static final String SERVER__APPLICATION_FOLDER = ROOT_ADDRESS
			+ "logo";
	Gson gson = new Gson();
	@Autowired
	private ServletContext context;

	@RequestMapping(value = "upload", method = RequestMethod.POST, produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> createapplicationScreens(HttpSession session,
			HttpServletRequest request, @RequestParam("files") MultipartFile f,
			@RequestParam("type") String type) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String extension = FilenameUtils.getExtension(f.getOriginalFilename());
		if (badFile(f, type, extension))
			return badRequest(responseMap);
		try {
			responseMap.put("type", type);
			responseMap.put("extension", extension);
			File file = uploadFile(f, type, extension);
			responseMap.put("status", "info");
			String name = file.getName();

			responseMap.put("logoPath",
					getDownloadRoot(UploadController.FILE, extension) + name);
			return new ResponseEntity<String>(gson.toJson(responseMap),
					HttpStatus.OK);
		} catch (IOException e) {
			Logger.urgent("error_code\t" + 43306);
			responseMap.put("status", "errot");
			responseMap.put("text", e.getMessage());
			return new ResponseEntity<String>(gson.toJson(responseMap),
					HttpStatus.BAD_REQUEST);
		}
	}

	private String getDownloadRoot(String type, String extension) {
		return ROOT_URL + UploadController.RESOURCE_URL + type + "/"
				+ extension + "/";
	}

	private boolean badFile(MultipartFile icon, String type, String extension) {
		if (icon.isEmpty()) {
			return true;
		}
		return false;
	}

	private ResponseEntity<String> badRequest(Map<String, Object> responseMap) {
		responseMap.put("status", "error");
		responseMap.put("text", " HttpStatus.BAD_REQUEST");
		return new ResponseEntity<String>(gson.toJson(responseMap),
				HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = RESOURCE_URL + "{type}/{extension}/{file_name}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getFile(HttpServletRequest request,
			@PathVariable("file_name") String fileName,
			@PathVariable("extension") String extension,
			@PathVariable("type") String type, HttpServletResponse response) {
		String folder;
		folder = getFolderResource(type, extension);
		FileSystemResource fileSystemResource = new FileSystemResource(folder + fileName + "." + extension);
		return fileSystemResource;
	}

	private String getFolderResource(String type, String extension) {
		String folder;

		folder = SERVER__APPLICATION_FOLDER;

		return folder + "/" + extension + "/";
	}

	public String getFilePermanetPath(String downloadUrl) {
		String[] split = downloadUrl.split("/");
		int length = split.length;
		String fileName = split[length - 1];
		String fileTemporaryPath = context.getRealPath("") + "/"
				+ SERVER__APPLICATION_FOLDER + fileName;
		return fileTemporaryPath;
	}

	public File uploadFile(MultipartFile file, String type, String extension)
			throws IOException, FileNotFoundException {
		byte[] bytes = file.getBytes();
		String pathname = UUID.randomUUID().toString() + "." + extension;
		String folder;
		folder = getFolderResource(type, extension);
		new File(folder).mkdirs();
		File thumbnailF = new File(folder + pathname);
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(thumbnailF));
		stream.write(bytes);
		stream.close();
		return thumbnailF;
	}

	public String getFullPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getRequestURI();
	}

	@RequestMapping(value = "removeFile", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> removeFile(HttpSession session,
			HttpServletRequest request, @RequestParam("fileNames") String type) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "file remove later");
		return new ResponseEntity<String>(gson.toJson(responseMap),
				HttpStatus.OK);
	}
}
