package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.model.User;
import hue.edu.xiong.volunteer_travel.model.UserComment;
import hue.edu.xiong.volunteer_travel.model.UserLike;
import hue.edu.xiong.volunteer_travel.repository.LikeRepository;
import hue.edu.xiong.volunteer_travel.repository.TravelStrategyRepository;
import hue.edu.xiong.volunteer_travel.repository.UserCommentRepository;
import hue.edu.xiong.volunteer_travel.repository.UserRepository;
import hue.edu.xiong.volunteer_travel.util.CookieUitl;
import hue.edu.xiong.volunteer_travel.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LikeService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private LikeRepository likeRepository;


    public Page<UserLike> findLikeByNameAndUserId(String content, Pageable pageable) {
        //查询通过后台审核的攻略列表
        Page<UserLike> travelStrategyPage = likeRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add((cb.equal(root.get("status"), 0)));
            //攻略name模糊查询
            if (!StringUtils.isEmpty(content)) {
                predicates.add((cb.like(root.get("title"), "%" + content + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return travelStrategyPage;
    }
    public Integer findLikeCountByItemId(String id) {
        //查询通过后台审核的攻略列表
        List<UserLike> likeList = likeRepository.findLikeByItemId(id);
        return likeList.size();
    }


    @Transactional(rollbackFor = Exception.class)
    public Result saveLike(HttpServletRequest request, String id) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        UserLike like = new UserLike();
        like.setId(IdGenerator.id());
        like.setCreateTime(new Date());
        like.setUserId(user.getId());
        like.setItemId(id);
        like.setItemType(3);
        likeRepository.saveAndFlush(like);
        return ResultGenerator.genSuccessResult();
    }
}
