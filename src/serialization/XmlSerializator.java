package serialization;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlSerializator<T> implements Serialization<T>{
	private Class<T> clases;

    public Class<T> getClases() {
        return clases;
    }

    public void setClases(Class<T> clases) {
        this.clases = clases;
    }

    public XmlSerializator(Class<T> clases) {
        this.clases = clases;
    }
	
	@Override
	public boolean toFile(Object object, String path) throws IOException, JAXBException {
		JAXBContext c = null;
		FileWriter outFile = null;
		Marshaller m = null;
		 
	      try {
	    	  outFile = new FileWriter(path);
	          c = JAXBContext.newInstance(getClases());
	          m = c.createMarshaller();
	            
	          m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	          m.marshal(object, outFile);
	       }
	       finally {
	    	   if(outFile != null)
	    		   outFile.close();
	        }
	        return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T fromFile(String path) throws IOException, JAXBException {
		JAXBContext c = null;
		Unmarshaller unmar = null;
		FileReader inFile = null;
		
        try {
        	inFile = new FileReader(path);
            c = JAXBContext.newInstance(getClases());
            unmar = c.createUnmarshaller();
            return (T) unmar.unmarshal(inFile);
        }
        finally {
        	if(inFile != null)
        		inFile.close();
        }
	}
	
}
