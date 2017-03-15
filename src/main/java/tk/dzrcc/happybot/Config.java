package tk.dzrcc.happybot;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Maksim on 28.02.2017.
 */
@EnableAutoConfiguration
@ComponentScan("tk.dzrcc.happybot")
//@EnableWebMvc
@EnableJpaRepositories
@EnableTransactionManagement
public class Config {
}
