package cz.vsb.resbill.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

  @RequestMapping(value = { "/", "/index" })
  public String index() {
    // return "index";
    return "redirect:/reports/agenda";
  }

}
