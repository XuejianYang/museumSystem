package hue.edu.xiong.controller;

import hue.edu.xiong.core.Result;
import hue.edu.xiong.model.*;
import hue.edu.xiong.repository.LikeRepository;
import hue.edu.xiong.repository.UserCommentRepository;
import hue.edu.xiong.service.ReserveService;
import hue.edu.xiong.util.CookieUitl;
import hue.edu.xiong.model.*;
import hue.edu.xiong.service.UserYuYueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private UserYuYueService userYuYueService;


    @RequestMapping("/reserveCollectionListUI")
    public String reserveCollectionListUI(Model model, @ModelAttribute("searchName") String searchName, @ModelAttribute("type") String type, @PageableDefault(size = 10) Pageable pageable) {
        Page<Collection> page = reserveService.reserveCollectionListUI(searchName, type, pageable);
        List<Collection> top10Collection = reserveService.getTop10collection();
        List<Exhibition> top10Exhibition = reserveService.getTop10Exhibition();
        model.addAttribute("top10Collection", top10Collection);
        model.addAttribute("top10Exhibition", top10Exhibition);
        model.addAttribute("page", page);
        return "reserve-collection";
    }

    @RequestMapping("/collectionDetailsUI")
    public String CollectionDetailsUI(Model model, HttpServletRequest request, @RequestParam(name = "id") String id) {
        Collection collection = reserveService.findCollectionById(id);
        //如果用户显示已经预约,就是查看预约列表
        Boolean flag = reserveService.isReserveCollection(request, id);
        List<Collection> top10Collection = reserveService.getTop10collection();
        List<Exhibition> top10Exhibition = reserveService.getTop10Exhibition();
        model.addAttribute("top10Collection", top10Collection);
        model.addAttribute("top10Exhibition", top10Exhibition);
        model.addAttribute("collection", collection);
        model.addAttribute("flag", flag);
        return "reserve/reserve-collection-details";
    }

    @RequestMapping("/reserveManageUI")
    public String reserveManageUI(Model model, HttpServletRequest request) {
        Cookie cookie = CookieUitl.get(request, "username");
        List<UserYuYue> yuyueListByName = new ArrayList<>();
        if (cookie != null) {
            yuyueListByName = userYuYueService.getYuyueListByName(cookie.getValue());
        }
        model.addAttribute("yuYueList", yuyueListByName);
        return "reserve/reserve-user-manage";
    }

    @RequestMapping("/cancelReserve")
    @ResponseBody
    public Result cancelReserve(HttpServletRequest request, String id) {
        return reserveService.cancelReserve(request, id);
    }

    @RequestMapping("/reserveExhibitionListUI")
    public String reserveExhibitionListUI(Model model, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 4) Pageable pageable) {
        Page<Exhibition> page = reserveService.reserveExhibitionListUI(searchName, pageable);
        List<Collection> top10Collection = reserveService.getTop10collection();
        List<Exhibition> top10Exhibition = reserveService.getTop10Exhibition();
        model.addAttribute("top10Collection", top10Collection);
        model.addAttribute("top10Exhibition", top10Exhibition);
        model.addAttribute("page", page);
        return "reserve/reserve-exhibition";
    }

    /**
     * 专题展览
     */
    @RequestMapping("/reserveExhibitionListUI2")
    public String reserveExhibitionListUI2(Model model2, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 4) Pageable pageable) {
        Page<Exhibition> page = reserveService.reserveExhibitionListUI2(searchName, pageable);
        List<Collection> top10Collection = reserveService.getTop10collection();
        List<Exhibition> top10Exhibition = reserveService.getTop10Exhibition();
        model2.addAttribute("top10Collection", top10Collection);
        model2.addAttribute("top10Exhibition", top10Exhibition);
        model2.addAttribute("page", page);
        return "reserve/reserve-Exhibition2";
    }


    @RequestMapping("/yuyue")
    public String yuyue(Model model) {
        return "reserve/calendar";
    }

    @RequestMapping("/yuyueUI")
    public String yuyueUI(Model model) {
        return "reserve/yue";
    }

    @ResponseBody
    @RequestMapping("/saveYuyue")
    public Result saveYuyue(HttpServletRequest request, @RequestBody List<UserYuYue> list) {
        return userYuYueService.saveYuYue(request, list);
    }

    @ResponseBody
    @RequestMapping("/getYuyueNum")
    public Result getYuyueNum(String date) {
        return userYuYueService.getYuyueNum(date);
    }


    @RequestMapping("/ExhibitionDetailsUI")
    public String ExhibitionDetailsUI(Model model, HttpServletRequest request, @RequestParam(name = "id") String id) {
        Exhibition exhibition = reserveService.findExhibitionById(id);
        //如果用户显示已经预约,就是查看预约列表
        Boolean flag = reserveService.isReserveExhibition(request, id);
        List<Collection> top10Collection = reserveService.getTop10collection();
        List<Exhibition> top10Exhibition = reserveService.getTop10Exhibition();
        List<UserLike> likeList = likeRepository.findLikeByItemId(id);
        List<UserComment> commentList = userCommentRepository.findUserCommentByItemId(id);
        model.addAttribute("top10Collection", top10Collection);
        model.addAttribute("top10Exhibition", top10Exhibition);
        model.addAttribute("exhibition", exhibition);
        model.addAttribute("numb", likeList.size());
        model.addAttribute("commentList", commentList);
        List<String> strings = new ArrayList<>();
        if (exhibition.getImages() != null) {
            strings = Arrays.asList(exhibition.getImages().split(","));
        }
        model.addAttribute("flag", flag);
        model.addAttribute("images", strings);
        return "reserve/reserve-exhibition-details";
    }


    @RequestMapping("/cancelExhibitionReserve")
    @ResponseBody
    public Result cancelExhibitionReserve(HttpServletRequest request, String id) {
        return reserveService.cancelExhibitionReserve(request, id);
    }

}
