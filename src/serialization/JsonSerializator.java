package serialization;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.JsonLocalDateDeserializator;
import models.JsonLocalDateSerializator;

public class JsonSerializator<T> implements Serialization<T> {
	private Class<T> clases;
	
    public Class<T> getClases() {
        return clases;
    }
    public void setClases(Class<T> clases) {
        this.clases = clases;
    }

    public JsonSerializator(Class<T> clases) {
        this.clases = clases;
    }
	
	@Override
	public boolean toFile(T object, String path) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting()
									.registerTypeAdapter(LocalDate.class, new JsonLocalDateSerializator())
									.create();
		FileWriter outFile = null;
		
		try {
			outFile = new FileWriter(path);
			gson.toJson(object, outFile);
		}
		finally {
			if(outFile != null)
				outFile.close();
		}
		
		return true;
	}

	@Override
	public T fromFile(String path) throws FileNotFoundException, IOException {
		FileReader inFile = null;
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDate.class, new JsonLocalDateDeserializator())
				.create();
		try {
			inFile = new FileReader(path);
			return gson.fromJson(inFile, getClases());
		}
		finally {
			if(inFile != null)
				inFile.close();
		}
	}
	
}
