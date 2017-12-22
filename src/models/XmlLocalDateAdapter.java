package models;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlLocalDateAdapter extends XmlAdapter<String, LocalDate> {
	@Override
	public LocalDate unmarshal(String s) throws Exception {
		return LocalDate.parse(s);
	}
	@Override
	public String marshal(LocalDate date) throws Exception {
		return date.toString();
	}
}
