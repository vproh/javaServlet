package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import builders.StudentBuilder;
import dataBase.DataBase;
import models.Student;

@WebServlet("/StudentHandler")
public class StudentHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String STUDENTS_JSP = "/students.jsp";
	private static String EMPTY_GROUP_JSP = "/emptyGroup.jsp";
	private static String ADD_STUDENT = "addStudent.html";
	private static String INDEX = "index.html";
	private static String ADD_MARKS_JSP = "/addMarks.jsp";
	private static String MARKS_JSP = "/viewMarks.jsp";
	private static String MARKS_TABLE = "marks";
	private static String UPDATE_MARKS_JSP = "updateMarks.jsp";
	private static int GROUP_NUMBER = 0;
	
	private DataBase db = null;
	private RequestDispatcher view = null;
	private StudentBuilder studentBuilder = null;
	private List<String> sbjList = new ArrayList<String>();
	private List<Integer> marks = new ArrayList<Integer>();
	private int studentId = 0;
	
	
	protected <T> void redirectTo(String path, T parameter, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("groupList", parameter);
		view = request.getRequestDispatcher(path);
		view.forward(request, response);
	}
	
	protected StudentBuilder initStudent(HttpServletRequest request) {
		StudentBuilder studentBuilder = new StudentBuilder(request.getParameter("firstName"));
		studentBuilder.setLastName(request.getParameter("lastName"))
						.setBirthDay(LocalDate.parse(request.getParameter("birthDay")))
						.setFaculty(request.getParameter("faculty"))
						.setLeader("on".equals(request.getParameter("leader")))
						.setShip("on".equals(request.getParameter("ship")))
						.setMissings(Integer.parseInt(request.getParameter("missings")))
						.setEmail(request.getParameter("email"))
						.setPhoneNumber(request.getParameter("phoneNumber"));
		return studentBuilder;
	}
	
	protected List<Integer> getMarks(int markCount, HttpServletRequest request) {
		List<Integer> marks = new ArrayList<Integer>();
		
		for(int i = 0; i < markCount; ++ i)
			marks.add(Integer.parseInt(request.getParameter("mark" + i)));
		
		return marks;
	}
	
	protected List<Integer> getNewMark(int count, HttpServletRequest request) {
		List<Integer> newMarks = new ArrayList<Integer>();
		for(int i = 0; i < count; ++ i) {
			newMarks.add(Integer.parseInt(request.getParameter("mark" + i)));
		}
		
		return newMarks;
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
		String action = request.getParameter("action");
		List<Student> students = new ArrayList<Student>();
		
		if(action == null) {
			response.sendRedirect(INDEX);
			return;
		}
		if(request.getParameter("groupNumber") != null)
			GROUP_NUMBER = Integer.parseInt(request.getParameter("groupNumber"));
		try {
			if(action.equals("delete")) {
				String phoneNumber = request.getParameter("phoneNumber");
				studentId = db.getStudentIdByPhoneNumber(phoneNumber);
				db.deleteStudent(studentId);
			}
			else if(action.equals("add")) {
				response.sendRedirect(ADD_STUDENT);
				return;
			}
			else if(action.equals("addStudent")) {
				sbjList = db.getColumns(MARKS_TABLE);
				studentBuilder = initStudent(request);
				redirectTo(ADD_MARKS_JSP, sbjList, request, response);
				return;
			}
			else if(action.equals("addMarks")) {
				marks = getMarks(sbjList.size(), request);
				String[] subjects = new String[sbjList.size()];
				Integer[] mrk = new Integer[marks.size()];
				
				studentBuilder.setEdu(sbjList.toArray(subjects), marks.toArray(mrk));
				db.addStudent(GROUP_NUMBER, studentBuilder.build());
			}
			else if(action.equals("viewStudent")) {
				studentId = db.getStudentIdByPhoneNumber(request.getParameter("phoneNumber"));
				List<Integer> marks = db.getStudentMarks(studentId);
				redirectTo(MARKS_JSP, marks, request, response);
				return;
			}
			else if(action.equals("updateMarks")) {
				sbjList = db.getColumns(MARKS_TABLE);
				redirectTo(UPDATE_MARKS_JSP, sbjList, request, response);
				return;
			}
			else if(action.equals("setNewMarks")) {
				List<Integer> marks = getNewMark(sbjList.size(), request);
				db.updateMarks(studentId, sbjList, marks);
				redirectTo(MARKS_JSP, marks, request, response);
				return;
			}
			students = db.getStudents(GROUP_NUMBER);
			if(!students.isEmpty()) {
				redirectTo(STUDENTS_JSP, students, request, response);
			}
			else
				redirectTo(EMPTY_GROUP_JSP, Integer.toString(GROUP_NUMBER), request, response);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
	
	}
}
