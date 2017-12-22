package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "Group")
@XmlType(propOrder = {"groupName", "groupNumber", "curatorName", "shipQuantity", "payQuantity", "students"})
public class Group {
	private String curatorName;
	private String groupName;
	private int groupNumber;
	List<Student> students = new ArrayList<Student>();
	
	public Group() {
		/*
		curatorName = null;
		groupName = null;
		groupNumber = 100;
		students = null;
		shipQuantity = 0;
		payQuantity = 0;
		*/
	}
	
	public Group(ArrayList<Student> students, String curatorName, String groupName, int groupNumber) {
		this.curatorName = curatorName;
		this.groupName = groupName;
		this.groupNumber = groupNumber;
		this.students = students;
	}
	
	@XmlElement(name = "groupName")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@XmlElement(name = "curator")
	public String getCuratorName() {
		return curatorName;
	}

	public void setCuratorName(String curatorName) {
		this.curatorName = curatorName;
	}
	
	@XmlElement(name = "groupNumber")
	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	public void setStudents(List<Student> students) {
		this.students = students;
	}
	
	@XmlElement(name = "Students")
	public List<Student> getStudents() {
		return this.students;
	}

	public int amountStudents() {
		return (int) getStudents().stream().count();
	}
	
	@XmlElement(name = "shipStudents")
	public int getShipQuantity() {
		return (int) getStudents().stream().filter( (s) -> s.isShip() == true).count();
	}
	
	@XmlElement(name = "payStudents")
	public int getPayQuantity() {
		return amountStudents() - getShipQuantity();
	}
	
	public static Integer[] getMarks(Student student) {
		return student.getEdu().values().stream().toArray(size -> new Integer[size]);
	}
	
	public static String[] getSubjects(Student student) {
		
		return student.getEdu().keySet().stream().toArray(size -> new String[size]);
	}
	
	public void sortByLastName() {
		List<Student> stud = getStudents().stream().sorted((s1, s2) -> s1.getLastName()
										.compareTo(s2.getLastName())).collect(Collectors.toList());
		setStudents(stud);
		
		return;
	}
	
	public void sortByEdu() {
		Stream<Student> studStream = getStudents().stream();
		List<Student> stud = studStream.sorted((s1, s2) -> Double.compare(s1.getEdu().values().stream().mapToInt(s -> s.intValue()).average().orElse(0),
													 						s2.getEdu().values().stream().mapToInt(s -> s.intValue()).average().orElse(0)))
				 															.collect(Collectors.toList());
		 Collections.reverse(stud);
		 setStudents(stud);
		 
		 return;
	}
	
	public void sortByMissings() {
		List<Student> stud = getStudents().stream().sorted( (s1, s2) -> Integer.compare(s1.getMissings(), s2.getMissings()) ).collect(Collectors.toList());
	
		setStudents(stud);
		return;
	}
	
	public Student maxMissings() {
		return getStudents().stream().max((s1, s2) -> Integer.compare(s1.getMissings(), s2.getMissings())).get();
	}
	
	public List<Student> studentShip (float persent) {
		List<Student> res = new ArrayList<Student>();
		int m = 0;
		
		m = (int) (getShipQuantity() * persent);
		sortByEdu();
		for(int i = 0; i < m; ++ i)
			res.add(students.get(i));
		
		return res;
	}

	public Map<String, Float> averageMark () {
		Map<String, Float> averageList = new HashMap<String, Float>();
		Iterator<String> it;
		Set<String> subjects;
		float average = 0f;
		String sbj;
		
		for(int i = 0; i < students.size(); ++ i) {
			average = 0f;
			subjects = students.get(i).getEdu().keySet();
			it = subjects.iterator();
			for(int j = 0; j < subjects.size(); ++ j) {
				sbj = it.next();
				average += students.get(i).getEdu().get(sbj);
			}
			average /= subjects.size();
			students.get(i).setAverage(average);
			averageList.put(students.get(i).getLastName(), average);
		}
		
		return averageList;
	}
	
	@Override
	public String toString() {
		String res = "";
		
		for(Student i:students)
			res += i.toString();
		return res + "\r\nGroup name: " + groupName + "; Group number: " + groupNumber
				+ "; Pay students = " + this.getPayQuantity() + "; Ship students = " + this.getShipQuantity() + "; curator: " + curatorName + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((curatorName == null) ? 0 : curatorName.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + groupNumber;

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
		Group other = (Group) obj;
		if (curatorName == null) {
			if (other.curatorName != null)
				return false;
		} else if (!curatorName.equals(other.curatorName))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (groupNumber != other.groupNumber)
			return false;
		
		return true;
	}
}


