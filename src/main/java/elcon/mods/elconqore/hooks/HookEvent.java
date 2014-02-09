package elcon.mods.elconqore.hooks;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@Cancelable
public class HookEvent<T> extends Event {

	public String hookName;
	public Object[] hookArgs;
	public T hookResult;
	
	public HookEvent(String hookName, Object... hookArgs) {
		this.hookName = hookName;
		this.hookArgs = hookArgs;
	}
}
