package stock.service;

import java.lang.Thread.UncaughtExceptionHandler;
import vc.pe.jutil.j4log.Logger;


public class SinaDaemonExceptionHandler implements UncaughtExceptionHandler {

	private static final Logger debugLog = Logger
			.getLogger("SinaQuoteExceptionHandler");

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// TODO Auto-generated method stub
		String subject = "线程：" + t.getName() + "抛出异常|线程状态：" + t.getState()
				+ "|类路径：" + t.getClass().getName();
		debugLog.debug(subject, e);

		// 以下代码是使用 反射机制 重新创建线程  
		try {
			Class threadClass = Class.forName(t.getClass().getName());
			Thread newThread = (Thread) threadClass.newInstance();
			SinaDaemonExceptionHandler handler = new SinaDaemonExceptionHandler();
			newThread.setUncaughtExceptionHandler(handler);
			newThread.setName(t.getName());
			newThread.start();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			debugLog.debug("uncaughtException:", e1);
		}
		catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			debugLog.debug("uncaughtException:", e1);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			debugLog.debug("uncaughtException:", e1);
		}
	}
}
