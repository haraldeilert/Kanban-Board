import models.Board;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.modules.siena.SienaFixtures;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
        // Check if the database is empty
    	System.out.println("******test: " + Board.all(Board.class).count());
        if(Board.all(Board.class).count() == 0) {
        	SienaFixtures.delete();//Clears a cache that might break fixture load
            SienaFixtures.loadModels("initial-data.yml");
        }
    }
 
}