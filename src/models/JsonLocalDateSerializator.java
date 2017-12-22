package models;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonLocalDateSerializator implements JsonSerializer<LocalDate>{
	@Override
	public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
	}
}
