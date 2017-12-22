package serialization;

public interface Serialization<T> {
	 boolean toFile(T object, String path) throws Exception;
	 T fromFile(String path) throws Exception;
}
