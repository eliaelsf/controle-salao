package com.fabiorHair.controlesalao.seguranca;

import com.fabiorHair.controlesalao.entity.SegurancaAPI;

public class SegurancaAPIThreadLocal {
	
	private static final ThreadLocal<SegurancaAPI> threadLocal = new ThreadLocal<>();

	public static void setSegurancaAPI(SegurancaAPI segurancaAPI) {
		threadLocal.remove();
		threadLocal.set(segurancaAPI);
	}

	public static SegurancaAPI getSegurancaAPI() {
		return threadLocal.get();
	}
}
