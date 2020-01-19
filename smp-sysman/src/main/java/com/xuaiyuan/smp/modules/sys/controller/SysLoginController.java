package com.xuaiyuan.smp.modules.sys.controller;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.xuaiyuan.smp.common.utils.Result;
import com.xuaiyuan.smp.modules.sys.entity.SysLoginLogEntity;
import com.xuaiyuan.smp.modules.sys.service.SysLoginLogService;
import com.xuaiyuan.smp.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录相关
 *
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;
	@Autowired
	private SysLoginLogService sysLoginLogService;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
	}
	
	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Result login(String username, String password, String captcha) {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return Result.error("验证码不正确");
		}
		try{
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
			// 保存登录日志
			SysLoginLogEntity sysLoginLogEntity = new SysLoginLogEntity();
			sysLoginLogEntity.setUsername(username);
			sysLoginLogEntity.setSystemBrowserInfo();
			sysLoginLogService.saveLoginLog(sysLoginLogEntity);
		}catch (UnknownAccountException e) {
			return Result.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			return Result.error("账号或密码不正确");
		}catch (LockedAccountException e) {
			return Result.error("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			return Result.error("账户验证失败");
		}
		return Result.success();
	}
	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	
}
