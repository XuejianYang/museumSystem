package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.model.TravelStrategy;
import hue.edu.xiong.volunteer_travel.model.UserComment;
import hue.edu.xiong.volunteer_travel.model.UserLike;
import hue.edu.xiong.volunteer_travel.model.UserStrategy;
import hue.edu.xiong.volunteer_travel.repository.LikeRepository;
import hue.edu.xiong.volunteer_travel.repository.UserCommentRepository;
import hue.edu.xiong.volunteer_travel.service.CommentService;
import hue.edu.xiong.volunteer_travel.service.StrategyService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/comment")
public class UserCommentController {
    @Autowired
    private CommentService commentService;

//    @RequestMapping("/travelStrategyLike")
//    @ResponseBody
//    public Result travelStrategyLike(HttpServletRequest request, String id) {
//        return strategyService.travelStrategyLike(request, id);
//    }
    @RequestMapping("/save")
    @ResponseBody
    public Result commentTravelStrategy(HttpServletRequest request, UserComment userComment) {
        return commentService.saveComment(request, userComment);
    }


}
