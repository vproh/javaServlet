package models;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlRootElement
@XmlType(propOrder = {"firstName", "lastName", "birthDay", "faculty", "missings", "leader", "ship", "edu", "average", "phoneNumber", "email"})
public class Student {
	private static final int MAX_OF_MISSINGS = 50;
	
	private String firstName;
	private String lastName;
	private String faculty;
	private String email;
	private String phoneNumber;
	private boolean leader, ship;
	private LocalDate birthDay;
	private int missings;
	private float average;
	private Map<String, Integer> edu = new HashMap<String, Integer>();
	
	public Student() {
		/*firstName = null;
		lastName = null;
		faculty = null;
		birthDay = null;
		email = null;
		phoneNumber = null;
		leader = false;
		ship = false;
		missings = 0;
		edu = null;
		average = 0f;*/
	}
	
	public Student(String firstName, String lastName, LocalDate bithDay, String phoneNumber, String email,
					String faculty, boolean leader, boolean ship, int missings, float average, String[] sbj, Integer[] marks) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDay = bithDay;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.faculty = faculty;
		this.ship = ship;
		this.leader = leader;
		this.missings = missings;
		this.average = average;
		for(int j = 0; j < sbj.length; ++ j) {
			this.edu.put(sbj[j], marks[j]);
		}
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFaculty() {
		return faculty;
	}
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}
	
	@XmlJavaTypeAdapter(value = XmlLocalDateAdapter.class)
	public LocalDate getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(LocalDate birthDay) {
		this.birthDay = birthDay;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void setMissings(int missings) {
		this.missings = missings;
	}
	
	public int getMissings() {
		return this.missings;
	}
	
	public boolean isLeader () {
		return this.leader;
	}
	
	public void setLeader (boolean leader) {
		this.leader = leader;
	}
	
	public void setEdu(Map<String, Integer> edu) {
		this.edu = edu;
	}
	
	public Map<String, Integer> getEdu() {
		return this.edu;
	}
	
	public void setAverage (float average) {
		this.average = average;
	}
	
	public float getAverage () {
		return this.average;
	}
	
	public void setShip(boolean ship) {
		this.ship = ship;
		
		return;
	}
	
	public boolean isShip() {
		return this.ship;
	}
	
	public int getAge() {
		LocalDate today = LocalDate.now();
		Period period = Period.between(birthDay, today);
		
		return period.getYears();
	}
	
	protected boolean isMissingsOutOfMax(int maxMissings) {
		return this.missings > maxMissings;
	}
	
	protected boolean isMissingsOutOfMax() {
		return this.missings >= MAX_OF_MISSINGS;
	}
	
	public void addMissings() {
		this.missings ++;
	}
	
	public void addMissings(int missings) {
		if(missings < 0 || missings > MAX_OF_MISSINGS)
			throw new IllegalArgumentException("Enter correct number of missings!(not negative and not more than " + MAX_OF_MISSINGS + " )\n");
		if(this.isMissingsOutOfMax())
			throw new IllegalArgumentException("Can`t add N - this student reach max of missings! Current value is: " + this.missings + '\n');
		this.missings += missings;
		
		return;
	}
	
	@Override
	public String toString() {
		String res = "\r\nFirst name: " + firstName + ";\r\nLast name: " + lastName + ";\r\nDate of birthday: " + birthDay
				+ ";\r\nFaculty: " + faculty  + ";\r\nMissings: " + missings  + ";\r\nLeader: " + leader + ";\r\nShip: " + ship + ";\r\nEducation success: "
				+ edu.toString().replace("{", "").replace("}", "") + ";\r\nAverage mark = " + this.average + ";\r\nPhone number: " + phoneNumber + ";\r\nE-mail: " + email + "\r\n";
		if(this.isMissingsOutOfMax())
			res += "This student reached maximum number of missings !!!\r\n";
		
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;

		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;

		return true;
	}
}
	
