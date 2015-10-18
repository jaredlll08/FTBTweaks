package ftb;

public class PackType {

	public String id;
	public String folderName;

	public PackType(String id, String folderName) {
		this.id = id;
		this.folderName = folderName;
	}

	public String getId() {
		return id;
	}

	public String getFolderName() {
		return folderName;
	}

	
	@Override
	public String toString() {
		return getId() + ":" + getFolderName();
		}
}
