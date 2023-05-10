package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ServiceException;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.*;
import hue.edu.xiong.volunteer_travel.service.ReserveService;
import hue.edu.xiong.volunteer_travel.service.StrategyService;
import hue.edu.xiong.volunteer_travel.util.CookieUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private UserStrategyRepository userStrategyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReserveService reserveService;
    @Autowired
    private ExhibitionRepository exhibitionRepository;
    @Autowired
    private UserExhibitionRepository userExhibitionRepository;

    @RequestMapping("/travelStrategyListUI")
    public String travelStrategyListUI(Model model, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 10) Pageable pageable) {
        Page<TravelStrategy> page = strategyService.TravelStrategyListUI(searchName, pageable);
        List<TravelStrategy> top10Strategy = strategyService.findTop10Strategy();
        model.addAttribute("top10Strategy", top10Strategy);
        model.addAttribute("page", page);
        return "strategy/travelStrategy";
    }

    @RequestMapping("/travelStrategyDetailsUI")
    public String travelStrategyDetailsUI(Model model, HttpServletRequest request, @RequestParam(name = "id") String id) {
        TravelStrategy travelStrategy = strategyService.findTravelStrategyById(id);
        //如果用户显示已经关注,就是查看关注列表
        Boolean flag = strategyService.isStrategy(request, id);
        List<TravelStrategy> top10Strategy = strategyService.findTop10Strategy();
        List<UserLike> likeList = likeRepository.findLikeByItemId(id);
        List<UserComment> commentList = userCommentRepository.findUserCommentByItemId(id);
        int numb = likeList.size();
        model.addAttribute("top10Strategy", top10Strategy);
        model.addAttribute("travelStrategy", travelStrategy);
        model.addAttribute("flag", flag);
        model.addAttribute("numb", numb);
        model.addAttribute("commentList", commentList);
        return "strategy/travelStrategy-details";
    }

    @RequestMapping("/cancelTravelStrategyReserve")
    @ResponseBody
    public Result cancelTravelStrategyReserve(HttpServletRequest request, String id) {
        return strategyService.cancelTravelStrategyReserve(request, id);
    }
    @RequestMapping("/travelStrategyLike")
    @ResponseBody
    public Result travelStrategyLike(HttpServletRequest request, String id) {
        return strategyService.travelStrategyLike(request, id);
    }
    @RequestMapping("/commentTravelStrategy")
    @ResponseBody
    public Result commentTravelStrategy(HttpServletRequest request, UserComment userComment) {
        return strategyService.commentTravelStrategy(request, userComment);
    }

    @RequestMapping("/strategyManageUI")
    public String strategyManageUI(Model model, HttpServletRequest request) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return "strategy/strategy-manage";
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        List<Exhibition> top10Exhibition = reserveService.getTop10Exhibition();
        // 收藏的展馆
        List<UserExhibition> userExhibitionByUser = userExhibitionRepository.findUserExhibitionByUser(user);
        List<Exhibition> attractionList = new ArrayList<>();
        userExhibitionByUser.forEach(s->{
            Exhibition exhibition = exhibitionRepository.findById(s.getExhibition().getId()).orElseThrow(() -> new ServiceException("景点ID错误"));
            attractionList.add(exhibition);
        });
        model.addAttribute("top10Exhibition", top10Exhibition);
        model.addAttribute("userStrategyList", attractionList);
        return "strategy/strategy-manage";
    }

    @RequestMapping("/saveTravelStrategy")
    @ResponseBody
    public Result saveTravelStrategy(HttpServletRequest request, TravelStrategy travelStrategy) {
        return strategyService.saveTravelStrategy(request, travelStrategy);
    }

    @RequestMapping("/pushStrategyListUI")
    public String pushStrategyListUI(HttpServletRequest request, Model model, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 10) Pageable pageable) {
        Page<TravelStrategy> page = strategyService.PushStrategyListUI(request,searchName, pageable);
        List<TravelStrategy> top10Strategy = strategyService.findTop10Strategy();
        model.addAttribute("top10Strategy", top10Strategy);
        model.addAttribute("page", page);
        return "strategy/pushStrategy";
    }
}
