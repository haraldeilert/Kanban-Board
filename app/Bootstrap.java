import models.Board;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
        // Check if the database is empty
        if(Board.count() == 0) {
        	Fixtures.delete();//Clears a cache that might break fixture load
            Fixtures.loadModels("initial-data.yml");
        }
    }
 
}