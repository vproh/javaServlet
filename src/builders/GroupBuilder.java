package builders;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import models.Group;
import models.Student;

@XmlRootElement (name = "Group")
public class GroupBuilder {
	private static final String NAME_REGEX = "^[A-Z][a-z]{1,14}([- ][a-zA-Z]{1,15}){0,3}$";
	private static final int MIN_GROUP_NUMBER = 100;
	private static final int MAX_GROUP_NUMBER = 699;
	
	private String curatorName;
	private String groupName;
	private List<Student> students = new ArrayList<Student>();
	private int groupNumber;
	
	public GroupBuilder() {
		/*
		curatorName = null;
		groupName = null;
		students = null;
		groupNumber = 0;
		*/
	}
	
	public GroupBuilder(String groupName) {
		this.groupName = groupName;
	}
	
	@XmlElement(name = "groupName")
	public String getGroupName() {
		return groupName;
	}
	
	public GroupBuilder setGroupName(String name) {
		this.groupName = name;
		
		return this;
	}
	
	@XmlElement(name = "Students")
	public List<Student> getStudents() {
		return students;
	}
	
	public GroupBuilder setStudents(List<Student> students) {
		this.students = students;
		
		return this;
	}
	
	@XmlElement(name = "curator")
	public String getCuratorName() {
		return curatorName;
	}
	
	public GroupBuilder setCuratorName(String curatorName) {
		this.curatorName = curatorName;
		
		return this;			
	}
	
	@XmlElement(name = "groupNumber")
	public int getGroupNumber() {
		return groupNumber;
	}
	
	public GroupBuilder setGroupNumber(int groupNum) {
		this.groupNumber = groupNum;
		
		return this;
	}
	
	public Group build() {
		StringBuilder errorMessage = new StringBuilder("");
		
		Group group = new Group();
		Pattern namePattern = Pattern.compile(NAME_REGEX);
		
		Matcher curNameMatcher = namePattern.matcher(this.curatorName);
		Matcher grNameMatcher = namePattern.matcher(this.groupName);
	
		if(!(curNameMatcher.matches()))
			errorMessage.append("Enter correct curator`s last name! (Contains latin letters up to 15!)");
		if(!(grNameMatcher.matches()))
			errorMessage.append("Enter correct group`s name! (Contains latin letters up to 15!)");
		if(this.groupNumber < MIN_GROUP_NUMBER || this.groupNumber > MAX_GROUP_NUMBER)
			errorMessage.append("Enter correct group`s number! (Numbers from " + MIN_GROUP_NUMBER +
																" up to " + MAX_GROUP_NUMBER + "!)");
		
		if(errorMessage != null && !(errorMessage.toString().equals(""))) {
			throw new IllegalArgumentException(errorMessage.toString());
		}
		
		group.setGroupName(this.groupName);
		group.setCuratorName(this.curatorName);
		group.setGroupNumber(this.groupNumber);
		group.setStudents(this.students);
		group.averageMark();
		
		return group;
	}
}