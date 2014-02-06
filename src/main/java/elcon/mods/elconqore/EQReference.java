package elcon.mods.elconqore;

public class EQReference {
	
	public static final String MOD_ID = "ElConCore";
	public static final String NAME = "ElConCore";
	public static final String VERSION = "${version} (build ${buildnumber})";
	public static final String MC_VERSION = "[${mcversion}]";
	public static final String DEPENDENCIES = "required-after:Forge@[10.12.0.1024,)";
	public static final String SERVER_PROXY_CLASS = "elcon.mods.core.ECCommonProxy";
    public static final String CLIENT_PROXY_CLASS = "elcon.mods.core.ECClientProxy";
    
    public static final String VERSION_URL = "https://raw.github.com/ItsMeElConquistador/ElConCore/master/version.xml";
}
