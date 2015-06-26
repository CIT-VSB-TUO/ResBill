package cz.vsb.resbill.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping({ "/", "/index" })
	public String index() {
		if (log.isDebugEnabled()) {
			log.debug("Logged user: " + getLoggedUserName());
		}
		return "index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	private String getLoggedUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null ? authentication.getName() : null;
	}

}
