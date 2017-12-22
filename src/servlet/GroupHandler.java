package servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import dataBase.DataBase;
import models.Group;
import serialization.JsonSerializator;
import serialization.Serialization;
import serialization.TxtSerializator;
import serialization.XmlSerializator;

@WebServlet("/GroupHandler")
@MultipartConfig
public class GroupHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String FILE_PATH = "F://serverFiles/";
	private static String MAIN_JSP = "/main.jsp";
	private static String EMPTY_GROUP_LIST = "emptyGroupList.html";
	private static String INDEX = "index.html";
	private static String USER_NAME = "admin";
	private static String PASSWORD = "admin";
	
	private RequestDispatcher view = null;
	private DataBase db = null;
	
	protected String uploadFile(HttpServletRequest request) throws IOException, ServletException {
		Part filePart = request.getPart("file");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		InputStream fileContent = filePart.getInputStream();
		File targetFile = new File(FILE_PATH + fileName); // add if fileName == null
		java.nio.file.Files.copy(
			fileContent, 
			targetFile.toPath(), 
			StandardCopyOption.REPLACE_EXISTING);
				 
		IOUtils.closeQuietly(fileContent);
		fileContent.close();
		
		return fileName;
	}
	
	protected <T> void redirectTo(String path, T parameter, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("groupList", parameter);
		view = request.getRequestDispatcher(path);
		view.forward(request, response);
	}
	
	protected void downloadFile(int groupNumber, HttpServletResponse response) throws IOException {
		Serialization<Group> xmlSerialize = new XmlSerializator<Group>(Group.class);
		PrintWriter out = response.getWriter();
		int i;   
		
		try {
			Group group = db.getGroup(groupNumber);
			xmlSerialize.toFile(group, FILE_PATH + "Group" + groupNumber + ".xml");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + "Group" + groupNumber + ".xml" + "\"");
		
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(FILE_PATH + "Group"  + groupNumber + ".xml");
		  while ((i = fileInputStream.read()) != -1) {  
		    out.write(i);   
		  }   
		  fileInputStream.close();
		  out.close();
		  
		  return;
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		db = new DataBase("jdbc:mysql://localhost:3306/test", "root", "toor");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		List<Group> groups = new ArrayList<Group>();
		String action = request.getParameter("action");
		int groupNumber = 0;
		if(action == null) {
			response.sendRedirect(INDEX);
			return;
		}
		if(action.equals("delete")) {
			try {
				groupNumber = Integer.parseInt(request.getParameter("groupNumber"));
				db.deleteGroup(groupNumber);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if(action.equals("download")) {
			groupNumber = Integer.parseInt(request.getParameter("groupNumber"));
			downloadFile(groupNumber, response);
			return;
		}
		try {
			groups = db.getAllGroups();
			if(!groups.isEmpty())
				redirectTo(MAIN_JSP, groups, request, response);
			else
				response.sendRedirect(EMPTY_GROUP_LIST);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		boolean failLogin = false;
		String action = request.getParameter("action");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		PrintWriter out = response.getWriter();
		List<Group> groups = new ArrayList<Group>();
		
		if(action != null) {
			Serialization<Group> serialization = null;
			String fileName = uploadFile(request);
			if(fileName.toLowerCase().contains(".txt"))
				serialization = new TxtSerializator<Group>(Group.class);
			else if(fileName.toLowerCase().contains(".xml"))
				serialization = new XmlSerializator<Group>(Group.class);
			else
				serialization = new JsonSerializator<Group>(Group.class);
			try {
				db.addGroup(serialization.fromFile(FILE_PATH + fileName));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(!(USER_NAME.equals(userName) && PASSWORD.equals(password)))
			failLogin = true;
		
		if(!failLogin) {
			try {
				groups = db.getAllGroups();
				if(!groups.isEmpty()) {
					redirectTo(MAIN_JSP, groups, request, response);
					return;
				}
				else
					response.sendRedirect(EMPTY_GROUP_LIST);
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			out.println("Incorrect login or password!!!");
			out.print("<a href=\"http://localhost:8080/WebApp/\"><button>Back</button></a>");
		}
		
		out.close();
	}
}
