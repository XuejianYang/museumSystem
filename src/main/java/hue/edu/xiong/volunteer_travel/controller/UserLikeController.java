package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.model.UserComment;
import hue.edu.xiong.volunteer_travel.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/like")
public class UserLikeController {
    @Autowired
    private LikeService likeService;

    @RequestMapping("/save")
    @ResponseBody
    public Result commentInformation(HttpServletRequest request, String id) {
        return likeService.saveLike(request, id);
    }


}
