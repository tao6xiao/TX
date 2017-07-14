package com.trs.gov.kpi;

import com.trs.gov.kpi.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
//@EnableDiscoveryClient
public class WCMGovKPIApplication {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(WCMGovKPIApplication.class, args);
		SpringContextUtil.setApplicationContext(app);
	}
}
