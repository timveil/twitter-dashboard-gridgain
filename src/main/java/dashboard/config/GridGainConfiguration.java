package dashboard.config;

import org.gridgain.grid.GridConfiguration;
import org.gridgain.grid.GridSpringBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class GridGainConfiguration {

    @Bean
    public GridSpringBean bean() throws Exception {
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("grid-gain.xml");

        GridSpringBean bean = new GridSpringBean();
        bean.setConfiguration( ctx.getBean(GridConfiguration.class));

        return bean;
    }
}
