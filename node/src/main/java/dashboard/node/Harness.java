package dashboard.node;

import org.gridgain.grid.GridConfiguration;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridGain;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Harness {


    public static void main(String[] args) throws GridException {

        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("grid-gain.xml");
        final GridConfiguration config = ctx.getBean(GridConfiguration.class);

        GridGain.start(config);

    }

}
