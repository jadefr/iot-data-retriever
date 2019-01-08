package app.controller;

import app.model.DataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

//@Controller
public class DataSourceController {
/*
    Map<Long, DataSource> dataSourceMap = new HashMap<>();

    @RequestMapping(value = "/dataSource", method = RequestMethod.GET)
    public ModelAndView showForm() {
        return new ModelAndView("dataSourceHome", "dataSource", new DataSource());
    }


    @RequestMapping(value = "/addDataSource", method = RequestMethod.POST)
    public String submit(@Valid @ModelAttribute("dataSource") DataSource dataSource, BindingResult result, ModelMap model) {
        if (result.hasErrors()){
            return "error";
        }
        model.addAttribute("name", dataSource.getName());
        model.addAttribute("latitude", dataSource.getLatitude());
        model.addAttribute("longitude", dataSource.getLongitude());

        return "dataSourceHome";
    }
*/

}
