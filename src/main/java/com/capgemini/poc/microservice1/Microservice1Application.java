package com.capgemini.poc.microservice1;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static java.lang.System.getenv;

@SpringBootApplication
public class Microservice1Application {

	final String queueName = "microservices_arch";
	
	public static void main(String[] args) {
        SpringApplication.run(new Object[]{
				Microservice1Application.class,
				MetricsConfiguration.class
        }, args);
    }
	
    @Bean
    public RabbitTemplate rabbitTemplate() 
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(this.queueName);
        template.setQueue(this.queueName);
        return template;
    }
    
    @Bean
    public ConnectionFactory connectionFactory() {
        final URI ampqUrl;
        try {
            ampqUrl = new URI(getEnvOrThrow("CLOUDAMQP_URL"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUsername(ampqUrl.getUserInfo().split(":")[0]);
        factory.setPassword(ampqUrl.getUserInfo().split(":")[1]);
        factory.setHost(ampqUrl.getHost());
        factory.setPort(ampqUrl.getPort());
        factory.setVirtualHost(ampqUrl.getPath().substring(1));

        return factory;
    }
    
    private static String getEnvOrThrow(String name) {
        String env = getenv(name);
        if (env == null) {
        	env = System.getProperty(name);
        	if(env == null)
        		throw new IllegalStateException("Environment variable [" + name + "] is not set.");
        }
        return env;
    }
}
