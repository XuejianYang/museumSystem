package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.model.UserComment;
import hue.edu.xiong.volunteer_travel.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/comment")
public class UserCommentController {
    @Autowired
    private CommentService commentService;

//    @RequestMapping("/InformationLike")
//    @ResponseBody
//    public Result InformationLike(HttpServletRequest request, String id) {
//        return strategyService.InformationLike(request, id);
//    }
    @RequestMapping("/save")
    @ResponseBody
    public Result commentInformation(HttpServletRequest request, UserComment userComment) {
        return commentService.saveComment(request, userComment);
    }


}
