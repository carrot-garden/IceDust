package derivations;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import utils.AbstractPageServlet;
import utils.GlobalVariables;
import utils.GlobalsPageServlet;
import utils.ThreadLocalOut;
import utils.ThreadLocalPage;

public class Settings {

    static volatile boolean updatesEnabled = true;
    
    public static boolean getUpdatesEnabled(){
        return updatesEnabled;
    }
    
    public static void setUpdatesEnabled(boolean setting){
        updatesEnabled = setting;
    }
    
	private static ScheduledExecutorService ex;
	
	public static void initTimers(int n, int millis){
		ex = Executors.newScheduledThreadPool(n);
		
		java.util.TimerTask timer69 = new java.util.TimerTask() {
			public void run() {
				if(DirtyCollections.getI()==2147483647)
					return;
				
				Thread thisThread = Thread.currentThread();
				
				if (utils.GlobalVariables.globalvarsChecked && utils.GlobalInit.initChecked) {
					org.hibernate.Session hibSession = null;
					try {
						org.webdsl.servlet.ServletState
								.scheduledTaskStarted("invoke updateDerivationsAsync() every x milliseconds with y threads");
						AbstractPageServlet ps = new GlobalsPageServlet();
						ThreadLocalPage.set(ps);
						ps.initRequestVars();
						hibSession = utils.HibernateUtil.getCurrentSession();
						hibSession.beginTransaction();
						if (GlobalVariables.initGlobalVars(ps.envGlobalAndSession,
								utils.HibernateUtil.getCurrentSession())) {
							java.io.PrintWriter out = new java.io.PrintWriter(System.out);
							ThreadLocalOut.push(out);
							webdsl.generated.functions.updateDerivationsAsyncThread_.updateDerivationsAsyncThread_(thisThread);
							utils.HibernateUtil.getCurrentSession().getTransaction().commit();
							ThreadLocalOut.popChecked(out);
							ps.invalidatePageCacheIfNeeded();
						}
					} catch(org.hibernate.StaleStateException | org.hibernate.exception.LockAcquisitionException ex){
						org.webdsl.logging.Logger.error("updateDerivationsAsync() database collision, rescheduling");
						
						reschedule(thisThread);
						
						utils.HibernateUtil.getCurrentSession().getTransaction().rollback();
					} catch (Exception ex) {
						org.webdsl.logging.Logger.error( ex.getClass().getCanonicalName()); 
						org.webdsl.logging.Logger.error("exception occured while executing timed function: "
								+ "invoke updateDerivationsAsync() every x milliseconds");
						org.webdsl.logging.Logger.error("exception message: " + ex.getMessage(), ex);
						utils.HibernateUtil.getCurrentSession().getTransaction().rollback();
					} finally {
						org.webdsl.servlet.ServletState.scheduledTaskEnded();
						ThreadLocalPage.set(null);
					}
				}
			}
		};
		
		for(int i = 0; i<n; i++){
			ex.scheduleAtFixedRate(timer69, 0 + n, millis + 0, TimeUnit.MILLISECONDS);
		}
	}
	
	public static void ensureTimers(int n, int millis){
		if(ex==null){
			initTimers(n, millis);
		}
	}

    static volatile boolean logincremental = true;
    
    public static boolean getLogincremental(){
        return logincremental;
    }
    
    public static void setLogincremental(boolean setting){
    	logincremental = setting;
    }

    static volatile boolean logeventualupdate = true;
    
    public static boolean getLogeventualupdate(){
        return logeventualupdate;
    }
    
    public static void setLogeventualupdate(boolean setting){
    	logeventualupdate = setting;
    }

    static volatile boolean logeventualstatus = true;
    
    public static boolean getLogeventualstatus(){
        return logeventualstatus;
    }
    
    public static void setLogeventualstatus(boolean setting){
    	logeventualstatus = setting;
    }
    
    private static Map<Thread, Set<String>> threadFieldMap = new ConcurrentHashMap<Thread, Set<String>>();
    private static Map<Thread, String     > threadUuidMap  = new ConcurrentHashMap<Thread, String     >();
    
    public static void threadMapsSet(Thread t, Set<String> hashmap, String uuid){
    	threadFieldMap.put(t, hashmap);
    	threadUuidMap.put(t, uuid);
    }
    
    public static void reschedule(Thread t){
    	Set<String> hashmap = threadFieldMap.get(t);
    	String uuid = threadUuidMap.get(t);
    	hashmap.add(uuid);
    }
    
}
