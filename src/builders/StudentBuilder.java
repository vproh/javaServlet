package builders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Student;

public class StudentBuilder {
	private static final String NAME_REGEX = "^[A-Z][a-z]{1,14}([- ][a-zA-Z]{1,15}){0,3}$";
	private static final String PHONE_NUMBER_REGEX = "^(\\+?38)?(((099)|(098)|(097)|(096)|(095)|(068)|(067)|(066)|(065))\\d{7})$";
	private static final String EMAIL_REGEX = "^\\w+([\\.-]?\\w+){1,3}@(\\w+\\.\\w{2,3})$";
	private static final int MAX_OF_MISSINGS = 50;
	private static final int MIN_AGE = 16;
	private static final int MAX_AGE = 60;
	 
	private String firstName;
	private String lastName;
	private String faculty;
	private String email;
	private String phoneNumber;
	private LocalDate birthDay;
	private boolean leader;
	private boolean ship;
	private int missings;
	private float average = 0f;
	private Map<String, Integer> edu = new HashMap<String, Integer>();
	
	public StudentBuilder (String firstName) {
		
		this.firstName = firstName;
	}
	
	public StudentBuilder setFirstName(String firstName) {
		this.firstName = firstName;
		
		return this;
	}
	
	public StudentBuilder setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	public StudentBuilder setFaculty(String faculty) {
		this.faculty = faculty;
		return this;
	}
	
	public StudentBuilder setBirthDay(LocalDate birthDay) {
		this.birthDay = birthDay;
		return this;
	}
	
	public StudentBuilder setEmail(String email) {
		this.email = email;
		return this;
	}
	
	public StudentBuilder setLeader(boolean leader) {
		this.leader = leader;
		return this;
	}
	
	public StudentBuilder setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}
	
	public StudentBuilder setAverege(float average) {
		this.average = average;
		return this;
	}
	
	public StudentBuilder setMissings(int missings) {
		this.missings = missings;
		
		return this;
	}
	
	public StudentBuilder setEdu(String[] sbj, Integer[] marks) {
		this.edu.clear();
		for(int i = 0; i < sbj.length; ++ i)
			this.edu.put(sbj[i], marks[i]);
		
		return this;
	}
	
	public StudentBuilder setShip(boolean ship) {
		this.ship = ship;
		return this;
	}
	
	public Student build() {
		Student student = new Student();
		StringBuilder errorMessage = new StringBuilder("");
		
		Pattern namesPattern = Pattern.compile(NAME_REGEX);
		Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
		Pattern phoneNumberPattern = Pattern.compile(PHONE_NUMBER_REGEX);
	
		Matcher firstNameMatch = namesPattern.matcher(this.firstName);
		Matcher lastNameMatch = namesPattern.matcher(this.lastName);
		Matcher facultyMatch = namesPattern.matcher(this.faculty);
		Matcher emailMatch = emailPattern.matcher(this.email);
		Matcher phoneNumberMatch = phoneNumberPattern.matcher(this.phoneNumber);
		
		if(!(firstNameMatch.matches()))
			errorMessage.append("Enter correct firstName!(Contains latin letters up to 15!\n");
		if(!(lastNameMatch.matches()))
			errorMessage.append("Enter correct last firstName! (Contains latin letters up to 15!)\n");
		if(!(phoneNumberMatch.matches()))
			errorMessage.append("Enter correct phone number!(begin on \"+380\" or \"0xx\")\n");
		if(!(emailMatch.matches()))
			errorMessage.append("Enter correct e-mail!(must have \'@\' and \'.\') \n");
		if(!(facultyMatch.matches()))
			errorMessage.append("Enter correct faculty firstName! (Contains latin letters up to 15!)\n");			
		if(missings < 0 || missings > MAX_OF_MISSINGS)
			errorMessage.append("Enter correct n!(not negative and not more than " + MAX_OF_MISSINGS + ")\n");
		if(average < 0 || average > 100)
			errorMessage.append("Enter correct average mark!(not negative and not more than 100)\n");
		if(LocalDate.now().compareTo(birthDay) > MAX_AGE || LocalDate.now().compareTo(birthDay) < MIN_AGE)
			errorMessage.append("Enter correct born year !\n");
		
		for(String key: edu.keySet()) {
			Matcher subjectMatch = namesPattern.matcher(key);
			if(!(subjectMatch.matches()))
				errorMessage.append("Subject '" + key + "' is incorrect entered!\n");
		}
		for(Integer v: edu.values()) {
			if(v < 0 || v > 100)
				errorMessage.append("This mark = " + v + " is incorrect!\n");
		}
		
		if(errorMessage != null && !(errorMessage.toString().equals(""))) {
			throw new IllegalArgumentException(errorMessage.toString());
		}
	
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setBirthDay(birthDay);
		student.setPhoneNumber(phoneNumber);
		student.setEmail(email);
		student.setFaculty(faculty);
		student.setMissings(missings);
		student.setLeader(leader);
		student.setEdu(edu);
		student.setAverage(average);
		student.setShip(ship);
		
		return student;
	}
}