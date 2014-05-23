package dashboard.node;

import org.apache.commons.lang3.time.StopWatch;
import org.gridgain.grid.Grid;
import org.gridgain.grid.GridConfiguration;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridGain;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * -DGRIDGAIN_QUIET=false -DGRIDGAIN_DEBUG_ENABLED=true
 */
public class Harness {


    public static void main(String[] args) throws GridException {

        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("gridgain.xml");
        final GridConfiguration config = ctx.getBean(GridConfiguration.class);

        Grid grid = GridGain.start(config);

        boolean runTest = false;

        if (args != null && args.length > 0) {
            runTest = true;
        }

        if (runTest) {

            long totalTime = 0;
            long maxTime = 0L;
            long minTime = 0L;

            int count = 1;


            for (int i = 0; i < count; i++) {

                StopWatch sw = new StopWatch();
                sw.start();

                // insert logic here

                sw.stop();

                long duration = sw.getNanoTime();

                if (i == 0) {
                    maxTime = duration;
                    minTime = duration;
                } else {
                    if (duration > maxTime) {
                        maxTime = duration;
                    }

                    if (duration < minTime) {
                        minTime = duration;
                    }
                }

                totalTime += duration;

            }

            System.out.println("\tavg:\t" + (totalTime / count) / 1000000 + " milliseconds");
            System.out.println("\tmax:\t" + maxTime / 1000000 + " milliseconds");
            System.out.println("\tmin:\t" + minTime / 1000000 + " milliseconds");


        }


    }

}
