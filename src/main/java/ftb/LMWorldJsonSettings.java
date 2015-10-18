package ftb;

/** Mirrored */
public class LMWorldJsonSettings
{
	public String startupPack;
	
	public void loadDefaults()
	{
		if(startupPack == null) startupPack = "default";
	}
}