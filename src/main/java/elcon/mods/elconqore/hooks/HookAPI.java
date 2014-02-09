package elcon.mods.elconqore.hooks;

import cpw.mods.fml.common.eventhandler.EventBus;

public class HookAPI {

	private static EventBus bus = new EventBus();

	public void test(Integer a, String b) {
		HookEvent<Void> event = new HookEvent<Void>("HookAPI.test", a, b);
		if(HookAPI.post(event)) {
			return;
		}
	}
	
	public int testReturn(int a) {
		HookEvent<Integer> event = new HookEvent<Integer>("HookAPI.testReturn", a);
		if(HookAPI.post(event)) {
			return event.hookResult;
		}
		return a;
	}
	
	public static void register(Object target) {
		bus.register(target);
	}

	public static void unregister(Object target) {
		bus.unregister(target);
	}

	public static boolean post(HookEvent<?> event) {
		return bus.post(event);
	}	
}
