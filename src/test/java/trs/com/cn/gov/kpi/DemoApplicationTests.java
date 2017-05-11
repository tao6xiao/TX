package trs.com.cn.gov.kpi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import trs.com.cn.gov.kpi.entity.User;
import trs.com.cn.gov.kpi.service.UserService;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Resource
	UserService userService;

	@Test
	public void contextLoads() {

		User user = userService.getUserById(1);
		System.out.println(user);
		Assert.assertTrue(user != null);
	}

}
