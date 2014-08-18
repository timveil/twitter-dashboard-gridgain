package dashboard.node;

import org.gridgain.grid.GridConfiguration;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridGain;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartNode {

    /*
        Start node with the following system property to avoid 8080 web conflicts

        -DGRIDGAIN_JETTY_PORT=8090
     */

    public static void main(String[] args) throws GridException {

        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("grid-gain.xml");
        final GridConfiguration config = ctx.getBean(GridConfiguration.class);

        GridGain.start(config);

    }

}
