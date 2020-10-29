package us.sep;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.sep.biz.user.request.EmailRequest;
import us.sep.biz.user.util.MailSendUtils;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTest {
    @Resource
    MailSendUtils mailSendUtils;

    @Test
    public void sendMail(){
        EmailRequest request = new EmailRequest();
        request.setEmailTheme("this is a test theme");
        request.setEmailContent("芜湖");
        request.setReceiverName("hujiayue");
        request.setReceiverEmailAddress("841793915@qq.com");
        mailSendUtils.sendEmailAsSysExceptionHtml(request);
    }

    @Test
    public void sendMail2(){
        EmailRequest request = new EmailRequest();
        request.setEmailTheme("this is a test theme");
        request.setEmailContent("别玩手机了");
        request.setReceiverName("黄旭");
        request.setReceiverEmailAddress("602646397@qq.com");
        mailSendUtils.sendEmailAsText(request);
    }

}
