package job;

import models.StatefulModel;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart(async = true)
public class Startup extends Job {
   public void doJob() throws InterruptedException {/*
      int i = 0;

      while (true) {
    	 System.out.println("test: " + i);
         i++;
         Thread.sleep(1000);
         StatefulModel.instance.event.publish("On step " + i);
      }
   */}
}
