package com.trs.gov.kpi;

import com.trs.gov.kpi.rabbitmq.Barista;
import com.trs.gov.kpi.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableBinding(Barista.class)
public class WCMGovKPIApplication {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(WCMGovKPIApplication.class, args);
		SpringContextUtil.setApplicationContext(app);
	}
}
