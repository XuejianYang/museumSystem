package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author admin
 * @Date 2019/4/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("root");
        user.setPassword("123456");
        User user2 = new User();
        user2.setUsername("root");
        user2.setPassword("123456");
        System.out.println(user.equals(user2));
//        Result result = loginService.login(user);
//        System.out.println(result);

        String a = "aa";
        String b = "aa";
        String aa = new String("aa");
        a.equals(b);
        String c = aa;
        Integer i=12;
        int j = 12;

        System.out.println( i.equals(j));

    }
}
