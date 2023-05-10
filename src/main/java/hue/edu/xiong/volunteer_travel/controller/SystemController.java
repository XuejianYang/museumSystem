package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.*;
import hue.edu.xiong.volunteer_travel.service.SystemService;
import hue.edu.xiong.volunteer_travel.service.UserYuYueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private SystemService systemService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private UserYuYueRepository userYuYueRepository;

    @Autowired
    private UserYuYueService userYuYueService;

    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private ExhibitionRepository exhibitionRepository;
    @Autowired
    private UserStrategyRepository userStrategyRepository;

    @Autowired
    private TravelStrategyRepository travelStrategyRepository;


    @RequestMapping("")
    public String loginUI() {
        return "system/login/login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public Result login(SysUser sysUser, HttpServletResponse response) {

       return systemService.login(sysUser,response);
    }
    @RequestMapping("/userListUI")
    public String userListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<User> page = systemService.getUserPage(pageable);
        model.addAttribute("page",page);
        return "system/user/list";
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public Result saveUser(User user) {
        return systemService.saveUser(user);
    }
    @RequestMapping("/deleteUser")
    @ResponseBody
    public Result deleteUser(String id) {
         userRepository.deleteById(id);
        return ResultGenerator.genSuccessResult("删除成功！");
    }

    @RequestMapping("/getUserById")
    @ResponseBody
    public Result getUserById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getUserById(id));
    }



    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
       systemService.logout(request,response);
        return "redirect:/system";
    }

    @RequestMapping("/collectionListUI")
    public String collectionListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Collection> page = systemService.getHotelPage(pageable);
        model.addAttribute("page", page);
        return "system/collection/list";
    }

    @RequestMapping("/saveCollection")
    @ResponseBody
    public Result saveHotel(Collection collection) {
        return systemService.saveCollection(collection);
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    public Result updateStatus(String id) {
        collectionRepository.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping("/getCollectionById")
    @ResponseBody
    public Result getCollectionById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getCollectionById(id));
    }


    @RequestMapping("/getExhibitionById")
    @ResponseBody
    public Result getExhibitionById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getExhibitionById(id));
    }

    @RequestMapping("/deleteExhibition")
    @ResponseBody
    public Result updateExhibitionStatus(String id) {
        exhibitionRepository.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping("/saveExhibition")
    @ResponseBody
    public Result saveExhibition(Exhibition exhibition) {
        return systemService.saveExhibition(exhibition);
    }

    @RequestMapping("/travelRouteListUI")
    public String travelRouteListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<TravelRoute> page = systemService.getTravelRoutePage(pageable);
        model.addAttribute("page", page);
        return "system/route/list";
    }

    @RequestMapping("/getTravelRouteById")
    @ResponseBody
    public Result getTravelRouteById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getTravelRouteById(id));
    }
    @RequestMapping("/deleteComment")
    @ResponseBody
    public Result deleteComment(String id) {
        userCommentRepository.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }


    @RequestMapping("/updateTravelRouteStatus")
    @ResponseBody
    public Result updateTravelRouteStatus(String id) {
        return systemService.updateTravelRouteStatus(id);
    }

    @RequestMapping("/saveTravelRoute")
    @ResponseBody
    public Result saveTravelRoute(TravelRoute travelRoute) {
        return systemService.saveTravelRoute(travelRoute);
    }

    @RequestMapping("/travelStrategyListUI")
    public String travelStrategyListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<TravelStrategy> page2 = systemService.getTravelStrategyPage(pageable);
        model.addAttribute("page", page2);
        return "system/strategy/list";
    }
    @RequestMapping("/yuyueUI")
    public String yuyue(Model model,@PageableDefault(size = 8) Pageable pageable) {
        Page<UserYuYue> page = userYuYueService.userYuYuePage(null,pageable);
        page.getContent().stream().filter(s->"0".equals(s.getDuring())).forEach(s->s.setDuring("上午"));
        page.getContent().stream().filter(s->"1".equals(s.getDuring())).forEach(s->s.setDuring("上午"));
        model.addAttribute("page", page);
//        model.addAttribute("ampmTotal", yuYueNum);
        return "system/yuyue/list";
    }
    @RequestMapping("/yuyueZhu")
    @ResponseBody
    public Result yuyueZhu() {
        List<UserYuYue> all = userYuYueRepository.findAll();
        Map<String, List<UserYuYue>> listMap = all.stream().collect(Collectors.groupingBy(s -> s.getDate()));
        List<YuYueDateNum> yuYueDateNums = new ArrayList<>();
        for (String s:listMap.keySet()){
            YuYueDateNum yuYueDateNum = new YuYueDateNum();
            yuYueDateNum.setDate(s);
            yuYueDateNum.setNum(listMap.get(s).size());
            yuYueDateNums.add(yuYueDateNum);
        }
        return ResultGenerator.genSuccessResult(yuYueDateNums);
    }
    @RequestMapping("/collectionZhu")
    @ResponseBody
    public Result collectionZhu() {
        List<Collection> all = collectionRepository.findAll();
        Map<String, List<Collection>> listMap = all.stream().collect(Collectors.groupingBy(s -> s.getType()));
        List<YuYueDateNum> yuYueDateNums = new ArrayList<>();
        for (String s:listMap.keySet()){
            YuYueDateNum yuYueDateNum = new YuYueDateNum();
            yuYueDateNum.setDate(s);
            yuYueDateNum.setNum(listMap.get(s).size());
            yuYueDateNums.add(yuYueDateNum);
        }
        return ResultGenerator.genSuccessResult(yuYueDateNums);
    }

    @RequestMapping("/pie")
    @ResponseBody
    public Result pie() {
        List<UserYuYue> all = userYuYueRepository.findAll();
        Long amCount = all.stream().filter(s -> "0".equals(s.getDuring())).count();
        Long pmCount = all.size() - amCount;
        YuYueNum yuYueNum = new YuYueNum();
        yuYueNum.setAm(amCount.intValue());
        yuYueNum.setPm(pmCount.intValue());
        return ResultGenerator.genSuccessResult(yuYueNum);
    }
    @RequestMapping("/collectionUI")
    public String collectionUI(Model model) {
        return "system/yuyue/charts2";
    }
    @RequestMapping("/yuyueZhuUI")
    public String yuyueZhuUI(Model model) {
        return "system/yuyue/charts";
    }
    @RequestMapping("/deletYuyue")
    @ResponseBody
    public Result deletYuyue(String id) {
        userYuYueRepository.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping("/getTravelStrategyById")
    @ResponseBody
    public Result getTravelStrategyById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getTravelStrategyById(id));
    }

    @RequestMapping("/updateTravelStrategyStatus")
    @ResponseBody
    public Result updateTravelStrategyStatus(String id) {
        return systemService.updateTravelStrategyStatus(id);
    }

    /**
     * 审核
     * @param id 数据id
     * @return 返回状态
     */
    @RequestMapping("/updateTravelStrategyStatusSH")
    @ResponseBody
    public Result updateTravelStrategyStatusSH(String id) {
        return systemService.updateTravelStrategyStatusSH(id);
    }

    @RequestMapping("/saveTravelStrategy")
    @ResponseBody
    public Result saveTravelStrategy(HttpServletRequest request,TravelStrategy travelStrategy) {
        return systemService.saveTravelStrategy(request,travelStrategy);
    }
    @RequestMapping("/adminSaveTravelStrategy")
    @ResponseBody
    public Result adminSaveTravelStrategy(TravelStrategy travelStrategy) {
        return systemService.adminSaveTravelStrategy(travelStrategy);
    }
}
