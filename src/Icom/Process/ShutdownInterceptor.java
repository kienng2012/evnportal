package Icom.Process;


import Icom.Utils.Utils;

public class ShutdownInterceptor extends Thread {

    private Main app;

    public ShutdownInterceptor(Main app) {
        this.app = app;
    }

    @Override
	public void run()  {
    	try
    	{
        System.out.println("Call the shutdown routine");
        app.endProcess();
    	}
    	catch(Exception ex)
    	{
    		Utils.logger.error("Error at thread shutdowninterceptor :"+ex.getMessage());
    	}
    }
}
