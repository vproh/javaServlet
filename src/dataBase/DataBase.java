package dataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import builders.GroupBuilder;
import builders.StudentBuilder;
import models.Group;
import models.Student;

public class DataBase {
	private static Connection con;
    private static String user;
    private static String url;
	
	public DataBase() throws SQLException, ClassNotFoundException {
		this("jdbc:mysql://localhost:3306/test", "root", "toor");
	}
	
	public DataBase(String url, String user, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DataBase.url = url;
			DataBase.user = user;
			con = DriverManager.getConnection(url, user, password);
			/*
			clearDB();
			createStudentGroupTable();
			createStudentsTable();
			createMarksTable();
			*/
		}
		catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;// null not null
	}
	
	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}
	protected List<String> getStudentIdSubjects(int studentId) throws SQLException {
		String sqlRequest = null;
		List<String> subjects = null;
		PreparedStatement pStmt = null;
		ResultSet res = null;
		
		try {
			subjects = getColumns("marks");
			sqlRequest = "select * from marks where markId=?";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, studentId);
			res = pStmt.executeQuery();
			res.next();
			for(int i = 0; i < subjects.size(); ++ i)
				if(res.getInt(subjects.get(i)) == 0) {
					subjects.remove(i);
					-- i;
				}
			
			return subjects;
		}
		finally {
			if(res != null)
				res.close();
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	public List<String> getColumns (String nameTable) throws SQLException {
		List<String> columns = null;
		ResultSet res = null;
		PreparedStatement pStmt = null;
		try {
			columns = new ArrayList<String>();
			pStmt = con.prepareStatement(String.format("show columns from %s;", nameTable));
			res = pStmt.executeQuery();
			res.next(); // miss markId
			while(res.next())
				columns.add(res.getString("Field"));
			return columns;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
			if(res != null)
				res.close();
		}
	}
	
	protected boolean IsSbjInDB(String sbj) throws SQLException {
		List<String> columns = null;
		try {
			columns = getColumns("marks");
			for(int i = 0; i < columns.size(); ++ i)				
				if(sbj.equals(columns.get(i)))
					return true;
			return false;
		}
		finally {
		}
	}

	protected void addToTableSubject(String nameTable, String sbj) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		
		try {
			sqlRequest = String.format("alter table %s add %s varchar(20)", nameTable, '`' + sbj + '`');
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.executeUpdate();
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	protected boolean isGroupInDB(int groupNumber) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		ResultSet res = null;
		
		try {
			sqlRequest = "select * from studentGroup where groupNumber=?;";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, groupNumber);
			res = pStmt.executeQuery();
			res.first();
			if(!res.isFirst())
				return false;
			
			return true;
		}
		finally {
			if(res != null)
				res.close();
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	protected boolean createStudentsTable() throws SQLException {
		String sqlRequest = null;
		Statement stmt = null;
		
		try {
			stmt = con.createStatement();
			sqlRequest = "create table students(studentId int not null primary key auto_increment, firstName varchar(20), lastName varchar(20), "
					+ "birthDay date, faculty varchar(20), isLeader tinyint(1), missings int, "
					+ "ship tinyint(1), email varchar(20), phoneNumber varchar(13), groupId int, foreign key(groupId) references studentGroup(groupNumber))";
			stmt.executeUpdate(sqlRequest);
			return true;
		}
		finally {
			if(stmt != null)
				stmt.close();
		}
	}
	
	protected boolean createStudentGroupTable() throws SQLException {
		String sqlRequest = null;
		Statement stmt = null;
		
		try {
			stmt = con.createStatement();
			sqlRequest = "create table studentGroup(groupNumber int not null primary key, groupName varchar(20), curatorName varchar(30))";
			stmt.executeUpdate(sqlRequest);
			return true;
		}
		finally {
			if(stmt != null)
				stmt.close();
		}
	}
	
	protected boolean createMarksTable() throws SQLException {
		String sqlRequest = null;
		Statement stmt = null;
		
		try {
			stmt = con.createStatement();
			
			sqlRequest = "create table marks(markId int not null primary key auto_increment)";
			stmt.executeUpdate(sqlRequest);
			
			return true;
		}
		finally {
			if(stmt != null)
				stmt.close();
		}
	}
	
	protected boolean addToStudentGroupTable(Group group) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		
		try {
			sqlRequest = "insert into studentGroup values(?, ?, ?);";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, group.getGroupNumber());
			pStmt.setString(2, group.getGroupName());
			pStmt.setString(3, group.getCuratorName());
			pStmt.executeUpdate();
			return true;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	protected boolean addToMarksTable(String[] sbj, Integer[] marks) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		
		try {
			for(int i = 0; i < sbj.length; ++ i) {
				if(!IsSbjInDB(sbj[i]))
					addToTableSubject("marks", sbj[i]);
			}
			
			sqlRequest = "insert into marks(markId, `";
			for(int i = 0; i < sbj.length - 1; ++ i)
				sqlRequest += sbj[i] + "`, `";
			sqlRequest += sbj[sbj.length - 1] + "`) values(markId, ";
			
			for(int i = 0; i < marks.length - 1; ++ i)
				sqlRequest += "?, ";
			sqlRequest += "?);";
			pStmt = con.prepareStatement(sqlRequest);
			for(int i = 0; i < marks.length; ++ i)
				pStmt.setInt(i + 1, marks[i]);
			pStmt.executeUpdate();
			
			return true;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	public boolean addGroup(Group group) throws SQLException {
		List<Student> students = null;
	
		try {
			students = group.getStudents();
			if(students == null)
				throw new IllegalArgumentException("This group have no students !");
		
			/*adding to studentGroup*/
			addToStudentGroupTable(group);

			/*adding students and their marks*/
			for(int i = 0; i < students.size(); ++ i)
				addStudent(group.getGroupNumber(), students.get(i));
			return true;
		}
		finally {
		}
	}
	
	public List<Student> getStudents(int groupNum) throws SQLException {
		List<Student> students = null;
		String sqlRequest = null;
		List<Integer> newMarks = null;
		List<String> newSbj = null;
		int tmpMark = 0;
		StudentBuilder studBuild = null;
		String[] sbj = null;
		Integer[] marks = null;
		PreparedStatement pStmt = null;
		ResultSet res = null;
		ResultSet resultMarks = null;
		
		try {
			students = new ArrayList<Student>();
			newSbj = getColumns("marks");
			newMarks = new ArrayList<Integer>();
			
			sqlRequest = "select * from students where groupId=?;";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, groupNum);
			res = pStmt.executeQuery();
			while(res.next()) {
				studBuild = new StudentBuilder(null);
				studBuild.setFirstName(res.getString("firstName"));
				studBuild.setLastName(res.getString("lastName"));
				studBuild.setBirthDay(res.getDate("birthDay").toLocalDate());
				studBuild.setFaculty(res.getString("faculty"));
				studBuild.setLeader(res.getBoolean("isLeader"));
				studBuild.setMissings(res.getInt("missings"));
				studBuild.setShip(res.getBoolean("ship"));
				studBuild.setEmail(res.getString("email"));
				studBuild.setPhoneNumber(res.getString("phoneNumber"));
				
				sqlRequest = "select * from marks where markId=?;";
				pStmt = con.prepareStatement(sqlRequest);
				pStmt.setInt(1, res.getInt("studentId"));
				resultMarks = pStmt.executeQuery();
				resultMarks.next();
				for(int i = 0; i < newSbj.size(); ++ i) {
					tmpMark = resultMarks.getInt(newSbj.get(i));
					if(resultMarks.wasNull()) {
						newSbj.remove(i);
						-- i;
					}
					else 
						newMarks.add(tmpMark);
				}
				marks = new Integer[newMarks.size()];
				sbj = new String[newSbj.size()];
				marks = newMarks.toArray(marks);
				sbj = newSbj.toArray(sbj);
				newMarks.clear();
				
				studBuild.setEdu(sbj, marks);
				students.add(studBuild.build());
			}
			
			return students;
		}
		finally {
			if(res != null)
				res.close();
			if(resultMarks != null)
				resultMarks.close();
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	protected Group getGroupInfo(int groupNum) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		ResultSet res = null;
		GroupBuilder groupBuilder = null;
		
		try {
			if(!isGroupInDB(groupNum))
				throw new IllegalArgumentException("Incorrect group number !!!");
			groupBuilder = new GroupBuilder();
			sqlRequest = "select * from studentgroup where groupNumber=?;";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, groupNum);
			res = pStmt.executeQuery();
			res.next();
			groupBuilder.setGroupName(res.getString("groupName"));
			groupBuilder.setGroupNumber(groupNum);
			groupBuilder.setCuratorName(res.getString("curatorName"));
			
			return groupBuilder.build();
		}
		finally {
			if(res != null)
				res.close();
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	public Group getGroup(int groupNum) throws SQLException {
		Group group = null;
		
		try {
			group = getGroupInfo(groupNum);
			group.setStudents(getStudents(groupNum));
			
			return group;
		}
		finally {
		}
	}
	
	public boolean addStudent(int groupNum, Student student) throws SQLException {
		ResultSet res = null;
		String sqlRequest = null;
		Statement stmt = null;
		PreparedStatement pStmt = null;
		
		try {
			if(!isGroupInDB(groupNum))
				throw new IllegalArgumentException("This group doesn`t exists!");
			/* adding marks */
			addToMarksTable(Group.getSubjects(student), Group.getMarks(student));
			
			/* adding Student`s info */
			sqlRequest = "insert into students values(studentId, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setString(1, student.getFirstName());
			pStmt.setString(2, student.getLastName());
			pStmt.setDate(3, Date.valueOf(LocalDate.of(student.getBirthDay().getYear(),
														student.getBirthDay().getMonth(),
														student.getBirthDay().getDayOfMonth())));
			pStmt.setString(4, student.getFaculty());
			pStmt.setBoolean(5, student.isLeader());
			pStmt.setInt(6, student.getMissings());
			pStmt.setBoolean(7, student.isShip());
			pStmt.setString(8, student.getEmail());
			pStmt.setString(9, student.getPhoneNumber());
			pStmt.setInt(10, groupNum);
			pStmt.executeUpdate();
			
			return true;
		}
		finally {
			if(res != null)
				res.close();
			if(stmt != null)
				stmt.close();
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	protected boolean deleteFromStudentsTable(int studentId) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		
		try {
			sqlRequest = "delete from students where studentId=?;";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, studentId);
			pStmt.executeUpdate();
			
			return true;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}

	protected boolean deleteFromMarksTable(int markId) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		
		try {
			sqlRequest = "delete from marks where markId=?;";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, markId);
			pStmt.executeUpdate();
			
			return true;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	public boolean deleteStudent(int studentId) throws SQLException {
		try {
			deleteFromStudentsTable(studentId);
			deleteFromMarksTable(studentId);
			
			return true;
		}
		finally {
		}
	}
	
	public boolean deleteDB(String nameDB) throws SQLException {
		PreparedStatement pStmt = null;

		try {
			/*delete DB */
			pStmt = con.prepareStatement(String.format("drop database %s", nameDB));
			pStmt.executeUpdate();
			
			return true;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	public boolean clearDB() throws SQLException {
		Statement stmt = null;
		ResultSet tables = null;
		
		try {
			stmt = con.createStatement();
			tables = stmt.executeQuery("show tables;");
			/* deleting all tables */
			stmt.executeUpdate("drop table students;");
			stmt.executeUpdate("drop table studentGroup;");
			stmt.executeUpdate("drop table marks;");
			
			return true;
		}
		finally {
			if(tables != null)
				tables.close();
			if(stmt != null)
				stmt.close();
		}
	}
	
	public boolean updateMark(int studentId, String sbj, int mark) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		
		try {
			if(!IsSbjInDB(sbj))
				throw new IllegalArgumentException("This student has not current course !!!");
			
			sqlRequest = String.format("update marks set %s=? where markId=?;", '`' + sbj + '`');
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, mark);
			pStmt.setInt(2, studentId);
			pStmt.executeUpdate();
			
			return true;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	public boolean updateMarks(int studentId, List<String> sbjs, List<Integer> marks) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		List<String> currentSubjects = null;
		
		try {
			currentSubjects = getStudentIdSubjects(studentId);
			for(int i = 0; i < sbjs.size(); ++ i) {
				if(!currentSubjects.contains(sbjs.get(i))) {
					sbjs.remove(i);
					marks.remove(i);
					-- i;
				}
			}
			if(sbjs.size() == 0)
				throw new IllegalArgumentException("Student has not this course(s) !");
			for(int i = 0; i < marks.size(); ++ i)
				if(marks.get(i) <= 0)
					throw new IllegalArgumentException("Can`t add negative or 0 mark!");
			
			sqlRequest = "update marks set `";
			for(int i = 0; i < sbjs.size() - 1; ++ i) {
				sqlRequest += sbjs.get(i) + "`=?, `";
			}
			sqlRequest += sbjs.get(sbjs.size() - 1) + "`=? where markId=?;";
			pStmt = con.prepareStatement(sqlRequest);
			for(int i = 0; i < marks.size(); ++ i)
					pStmt.setInt(i + 1, marks.get(i));
			
			pStmt.setInt(sbjs.size() + 1, studentId);
			pStmt.executeUpdate();
			
			return true;
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
	}
	
	protected List<Integer> getAllGroupNumbers() throws SQLException {
		String sqlRequest = null;
		ResultSet res = null;
		Statement stmt = null;
		List<Integer> groupNumberList = null;
		
		try {
			stmt = con.createStatement();
			groupNumberList = new ArrayList<Integer>();
			sqlRequest = "select groupNumber from studentGroup;";
			res = stmt.executeQuery(sqlRequest);
			while(res.next())
				groupNumberList.add(res.getInt("groupNumber"));
		}
		finally {
			if(res != null)
				res.close();
			if(stmt != null)
				stmt.close();
		}
		return groupNumberList;
	}
	
	public List<Group> getAllGroups() throws SQLException {
		List<Group> groupList = null;
		List<Integer> groupNumberList = null;
		
		try {
			groupList = new ArrayList<Group>();
			groupNumberList = getAllGroupNumbers();
			
			for(int i = 0; i < groupNumberList.size(); ++ i) {
				groupList.add(getGroup(groupNumberList.get(i)));
			}
		}
		finally {
		}
		
		return groupList;
	}
	
	protected List<Integer> getStudentsIdToDelete(int groupNumber) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		List<Integer> studentIDs = null;
		ResultSet res = null;
		
		try {
			studentIDs = new ArrayList<Integer>();
			sqlRequest = "select studentId from students where groupId=?";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, groupNumber);
			res = pStmt.executeQuery();
			
			while(res.next()) {
				studentIDs.add(res.getInt("studentId"));
			}
		}
		finally {
			if(res != null)
				res.close();
			if(pStmt != null)
				pStmt.close();
		}
		return studentIDs;
	}
	
	protected boolean deleteGroupFromStudentsTable(int groupNumber) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
			
		try {
			sqlRequest = "delete from students where groupId=?";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, groupNumber);
			pStmt.executeUpdate();
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
		return true;
	}
	
	protected boolean deleteGroupFromMarksTable(List<Integer> studentIDs) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
			
		try {
			for(int i = 0; i < studentIDs.size(); ++ i) {
				sqlRequest = "delete from marks where markId=?";
				pStmt = con.prepareStatement(sqlRequest);
				pStmt.setInt(1, studentIDs.get(i));
				pStmt.executeUpdate();
			}
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
		return true;
	}
	
	public boolean deleteGroup(int groupNumber) throws SQLException {
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		List<Integer> studentIDs = null;
		
		try {
			studentIDs = getStudentsIdToDelete(groupNumber);
			deleteGroupFromStudentsTable(groupNumber);
			deleteGroupFromMarksTable(studentIDs);
			sqlRequest = "delete from studentGroup where groupNumber=?";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, groupNumber);
			pStmt.executeUpdate();
		}
		finally {
			if(pStmt != null)
				pStmt.close();
		}
		
		return true;
	}

	public int getStudentIdByPhoneNumber(String phoneNumber) throws SQLException{
		int studentId = 0;
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		ResultSet res = null;
		
		try {
			sqlRequest = "select studentId from students where phoneNumber=?";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setString(1, phoneNumber);
			res = pStmt.executeQuery();
			res.next();
			studentId = res.getInt("studentId");
		}
		finally {
			if(res != null)
				res.close();
			if(pStmt != null)
				pStmt.close();
		}
		return studentId;
	}
	
	public List<Integer> getStudentMarks(int markId) throws SQLException {
		List<Integer> marksList = null;
		List<String> subjects = null;
		String sqlRequest = null;
		PreparedStatement pStmt = null;
		ResultSet res = null;
		
		try {
			marksList = new ArrayList<Integer>();
			subjects = getColumns("marks");
			sqlRequest = "select * from marks where markId=?";
			pStmt = con.prepareStatement(sqlRequest);
			pStmt.setInt(1, markId);
			res = pStmt.executeQuery();
			res.next();
			for(int i = 0; i < subjects.size(); ++ i)
				marksList.add(res.getInt(subjects.get(i)));
			
		}
		finally {
			if(res != null)
				res.close();
			if(pStmt != null)
				pStmt.close();
		}
		return marksList;
	}
}
