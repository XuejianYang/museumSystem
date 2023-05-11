package hue.edu.xiong.controller;

import hue.edu.xiong.model.Collection;
import hue.edu.xiong.model.Exhibition;
import hue.edu.xiong.model.Information;
import hue.edu.xiong.model.TravelRoute;
import hue.edu.xiong.service.ReserveService;
import hue.edu.xiong.service.RouteService;
import hue.edu.xiong.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private StrategyService strategyService;

    @RequestMapping("/")
    public String index(Model model) {
        List<Collection> top10Collection= reserveService.getTop10collection();
        List<Exhibition> top10Exhibition = reserveService.getTop10Exhibition();
        List<TravelRoute> top10Route = routeService.findTop10Route();
        List<Information> top10Strategy = strategyService.findTop10Strategy();
        model.addAttribute("top10Strategy",top10Strategy);
        model.addAttribute("top10Route", top10Route);
        model.addAttribute("top10Collection", top10Collection);
        model.addAttribute("top10Exhibition", top10Exhibition);
        return "index";
    }
}
