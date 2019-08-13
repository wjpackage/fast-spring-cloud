package com.sendi.signalling.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EMailService {

    /**
     * 邮件发送控制，key为发送原因，value为上次发送时间
     */
    private static ConcurrentHashMap<String,Long> EMAIL_SEND_LAST_TIME = new ConcurrentHashMap<>();

	/**
	 * 邮件消息分割符
	 */
	private final static String splitMail = "#";

    @Value("${email.redis.switch}")
    private boolean emailSwitch;

    @Value("${email.redis.queue}")
    private String emailQueue;

    @Value("${email.redis.ip}")
    private String emailRedisIP;

    @Value("${email.redis.port}")
    private int emailRedisPort;

    @Value("${email.redis.timeout}")
    private int emailRedisTimeout;

    @Value("${email.redis.password}")
    private String emailRedisPassword;

    @Value("${email.redis.emailto}")
    private String emailTo;

    @Value("${email.redis.repeattime}")
    private Long repeatTime;

    /**
     * 发送邮件
     * @param key 发送原因对应key，用于控制短时间重复发送
     * @param sub 标题
     * @param message 内容
     */
    public void send(String key,String sub, String message){
        new Thread(() ->{
            Jedis jr = null;
            if(!emailSwitch){
                log.info("Email发送开关状态:{}",emailSwitch);
                return ;
            }
            try {
                Long lastTime = EMAIL_SEND_LAST_TIME.get(key);
                if( lastTime == null || (System.currentTimeMillis()  - lastTime ) > repeatTime){
                    EMAIL_SEND_LAST_TIME.put(key,System.currentTimeMillis());
                }else{
                    return ;
                }
                log.info("send warn email key:{} sub:{}",key,sub);
                String subject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "--" + sub;
                jr = new Jedis(emailRedisIP, emailRedisPort, emailRedisTimeout);// redis服务地址和端口号
                jr.auth(emailRedisPassword);
                jr.lpush(emailQueue, packMailSend(UUID.randomUUID().toString(),subject,emailTo,null,message,null));
            } catch (Exception e) {
                log.warn("",e);
            } finally {
                if (jr != null) {
                    jr.disconnect();
                }
            }
        }).start();
    }

	/**
	 * 封装邮件发送队列信息
	 * @param vid  uid
	 * @param sub 邮件标题
	 * @param to 收件人 多个收件人逗号分割
	 * @param cc 抄送人 同上
	 * @param body  邮件内容
	 * @param addi 附加字段
	 * @return email msg
	 */
	@SuppressWarnings("all")
	private String packMailSend(String vid,String sub,String to,String cc,String body,String addi){
		//原则上非必要的字段作为预留字段的一部分
    	//uid + "#" + 标题 + "#" + 接收人（多个逗号分割）+"#"+抄送人（同左）+"#" +内容+"#"+预留字段
    	//以上各部分除 内容 外都不能带#符号
    	//预留字段 ---- 附件： file:文件名1,文件名2;
		//发送原因： desc:原因;
		//例子：file:文件名1,文件名2;desc:原因;
		return dealNull(vid)+splitMail+dealNull(sub)+splitMail+dealNull(to)+splitMail+dealNull(cc)+splitMail+dealNull(body)+splitMail+dealNull(addi);
	}
	private String dealNull(String s){
		if(s==null){
			return "";
		}
		return s;
	}

	
	public static void main(String[] args) {
		//封装 附加字段
//		String addi = packMailSendAddi("/usr/local/ddd.avi,/usr/local/ddd.avi","发送额外说明");
//		String msg = packMailSend("vid", "标题", "zhengwc@gzsendi.cn", "hesw@gzsendi.cn", "邮件正文", addi);
		//  将msg插入到redis List ：fraud_queue_mail_send
		
		//不需附加字段、抄送人
//		msg = packMailSend("vid", "标题", "zhengwc@gzsendi.cn", null, "邮件正文", null);
		//  将msg插入到redis List ：fraud_queue_mail_send
	}
}
