package hue.edu.xiong.controller;

import hue.edu.xiong.model.*;
import hue.edu.xiong.repository.*;
import hue.edu.xiong.core.Result;
import hue.edu.xiong.core.ServiceException;
import hue.edu.xiong.service.ReserveService;
import hue.edu.xiong.service.StrategyService;
import hue.edu.xiong.util.CookieUitl;
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
@RequestMapping("/information")
public class InformationController {

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

    @RequestMapping("/InformationListUI")
    public String InformationListUI(Model model, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 10) Pageable pageable) {
        Page<Information> page = strategyService.InformationListUI(searchName, pageable);
        List<Information> top10Strategy = strategyService.findTop10Strategy();
        model.addAttribute("top10Strategy", top10Strategy);
        model.addAttribute("page", page);
        return "information/information";
    }

    @RequestMapping("/InformationDetailsUI")
    public String InformationDetailsUI(Model model, HttpServletRequest request, @RequestParam(name = "id") String id) {
        Information information = strategyService.findInformationById(id);
        //如果用户显示已经关注,就是查看关注列表
        Boolean flag = strategyService.isStrategy(request, id);
        List<Information> top10Strategy = strategyService.findTop10Strategy();
        List<UserLike> likeList = likeRepository.findLikeByItemId(id);
        List<UserComment> commentList = userCommentRepository.findUserCommentByItemId(id);
        int numb = likeList.size();
        model.addAttribute("top10Strategy", top10Strategy);
        model.addAttribute("information", information);
        model.addAttribute("flag", flag);
        model.addAttribute("numb", numb);
        model.addAttribute("commentList", commentList);
        return "information/information_details";
    }

    @RequestMapping("/cancelInformationReserve")
    @ResponseBody
    public Result cancelInformationReserve(HttpServletRequest request, String id) {
        return strategyService.cancelInformationReserve(request, id);
    }
    @RequestMapping("/InformationLike")
    @ResponseBody
    public Result InformationLike(HttpServletRequest request, String id) {
        return strategyService.InformationLike(request, id);
    }
    @RequestMapping("/commentInformation")
    @ResponseBody
    public Result commentInformation(HttpServletRequest request, UserComment userComment) {
        return strategyService.commentInformation(request, userComment);
    }

    @RequestMapping("/strategyManageUI")
    public String strategyManageUI(Model model, HttpServletRequest request) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return "information/strategy-manage";
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
        return "information/strategy-manage";
    }

    @RequestMapping("/saveInformation")
    @ResponseBody
    public Result saveInformation(HttpServletRequest request, Information information) {
        return strategyService.saveInformation(request, information);
    }

    @RequestMapping("/pushStrategyListUI")
    public String pushStrategyListUI(HttpServletRequest request, Model model, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 10) Pageable pageable) {
        Page<Information> page = strategyService.PushStrategyListUI(request,searchName, pageable);
        List<Information> top10Strategy = strategyService.findTop10Strategy();
        model.addAttribute("top10Strategy", top10Strategy);
        model.addAttribute("page", page);
        return "information/pushStrategy";
    }
}
