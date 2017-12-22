package serialization;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import builders.GroupBuilder;
import builders.StudentBuilder;
import models.Group;
import models.Student;

public class TxtSerializator<T> implements Serialization<T> {
	private Class<T> clases;

    public Class<T> getClases() {
        return clases;
    }

    public void setClases(Class<T> clases) {
        this.clases = clases;
    }

    public TxtSerializator(Class<T> clases) {
        this.clases = clases;
    }
	
	@Override
	public boolean toFile(Object object, String path) throws IOException {
		FileWriter outFile = null;
		
		try {
			outFile = new FileWriter(path);
			outFile.write(object.toString());
		}
		finally {
			if(outFile != null)
				outFile.close();
		}
			
			return true;
		}
	
	@SuppressWarnings("unchecked")
	@Override
	public T fromFile(String path) throws IOException, ClassNotFoundException {
		Group group = null;
		GroupBuilder groupBuilder = null;
		FileReader inFile = null;
		StudentBuilder studentBuilder = null;
		Scanner input = null;
		List<Student> students = null;
		List<String> subjects = null;
		List<Integer> marks = null;
		String tmp = null;
		String[] sbj = null;
		Integer[] mrk = null;
		
		try {
			inFile = new FileReader(path);
			input = new Scanner(inFile);
			groupBuilder = new GroupBuilder(null);
			studentBuilder = new StudentBuilder(null);
			subjects = new ArrayList<String>();
			marks = new ArrayList<Integer>();
			students = new ArrayList<Student>();
			input.useDelimiter("\n|(; ?)|:|( ?= ?)|, ");
	
			while(input.hasNext()) {
				tmp = input.next();
				switch(tmp) {
				case "First name":
					//System.out.println("Name =" + input.next().trim());
					studentBuilder.setFirstName(input.next().trim());
					break;
				case "Last name":
					//System.out.println("Last name =" + input.next().trim());
					studentBuilder.setLastName(input.next().trim());
					break;
				case "Date of birthday":
					studentBuilder.setBirthDay(LocalDate.parse(input.next().trim()));
					//System.out.println("Date of birthday =" + input.next().trim());
					break;
				case "Faculty":
					//System.out.println("Faculty =" + input.next().trim());
					studentBuilder.setFaculty(input.next().trim());
					break;
				case "Missings":
					//System.out.println("Missing =" + input.next());
					studentBuilder.setMissings(Integer.parseInt(input.next().trim()));
					break;
				case "Leader":
					//System.out.println("leader =" + input.next());
					studentBuilder.setLeader(Boolean.parseBoolean(input.next().trim()));
					break;
				case "Ship":
					studentBuilder.setShip(Boolean.parseBoolean(input.next().trim()));
					break;
				case "Education success":
					tmp = input.next().trim();
					while(tmp.toCharArray().length != 0) {
						//System.out.println("Subject" + i + " =" + tmp);
						//System.out.println("Mark = " + input.next());
						subjects.add(tmp);
						marks.add(Integer.parseInt(input.next().trim()));
						tmp = input.next().trim();
					}
					sbj = new String[subjects.size()];
					mrk = new Integer[marks.size()];
					studentBuilder.setEdu(subjects.toArray(sbj), marks.toArray(mrk));
					subjects.clear();
					marks.clear();
					break;
				case "Phone number":
					//System.out.println("Phone number =" + input.next().trim());
					studentBuilder.setPhoneNumber(input.next().trim());
					break;
				case "E-mail":
					//System.out.println("E-mail =" + input.next().trim());
					studentBuilder.setEmail(input.next().trim());
					students.add(studentBuilder.build());
					studentBuilder = new StudentBuilder(null);
					break;
				case "Group name":
					groupBuilder.setGroupName(input.next().trim());
					//System.out.println("Group name =" + input.next());
					break;
				case "Group number":
					//System.out.println("Group number =" + input.next());
					groupBuilder.setGroupNumber(Integer.parseInt(input.next().trim()));
					break;
				case "curator":
					//System.out.println("curator =" + input.next().trim());
					groupBuilder.setCuratorName(input.next().trim());
					break;					
				}
			}
			if(groupBuilder.getGroupName() == null)
				return (T) students.get(0);
			groupBuilder.setStudents(students);
			group = groupBuilder.build();
		}
		finally {
			if(inFile != null)
				inFile.close();
			if(input != null)
				input.close();
		}
		
		return (T) group;
		
	}	
}
