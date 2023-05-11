package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.core.ServiceException;
import hue.edu.xiong.volunteer_travel.enums.StatusEnum;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.LikeRepository;
import hue.edu.xiong.volunteer_travel.repository.InformationRepository;
import hue.edu.xiong.volunteer_travel.repository.UserRepository;
import hue.edu.xiong.volunteer_travel.repository.UserStrategyRepository;
import hue.edu.xiong.volunteer_travel.util.CookieUitl;
import hue.edu.xiong.volunteer_travel.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class StrategyService {

    @Autowired
    private InformationRepository informationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserStrategyRepository userStrategyRepository;

    public Page<Information> InformationListUI(String searchName, Pageable pageable) {
        //查询通过后台审核的攻略列表
        Page<Information> InformationPage = informationRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的攻略
            predicates.add((cb.equal(root.get("status"), 0)));
            //攻略name模糊查询
            if (!StringUtils.isEmpty(searchName)) {
                predicates.add((cb.like(root.get("title"), "%" + searchName + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return InformationPage;
    }

    public Page<Information> PushStrategyListUI(HttpServletRequest request, String searchName, Pageable pageable) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            throw new ServiceException("用户未登录");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        //查询通过后台审核的攻略列表
        Page<Information> InformationPage = informationRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //攻略name模糊查询
            predicates.add((cb.equal(root.get("user"), user)));
            //攻略name模糊查询
            if (!StringUtils.isEmpty(searchName)) {
                predicates.add((cb.like(root.get("name"), "%" + searchName + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return InformationPage;
    }

    public Information findInformationById(String id) {
        return informationRepository.findById(id).orElseThrow(() -> new ServiceException("攻略id错误!"));
    }

    public Boolean isStrategy(HttpServletRequest request, String id) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie != null) {
            User user = userRepository.findUserByUsername(cookie.getValue());
            Information information1 = findInformationById(id);
            UserStrategy userStrategy = userStrategyRepository.findUserStrategyByInformationAndUser(information1, user);
            //每个路线只能关注一次
            if (userStrategy != null) {
                return true;
            }
        }
        return false;
    }
    @Transactional(rollbackFor = Exception.class)
    public Result InformationLike(HttpServletRequest request, String id) {
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

    @Transactional(rollbackFor = Exception.class)
    public Result commentInformation(HttpServletRequest request, UserComment userComment) {

        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        UserComment comment = new UserComment();
        comment.setId(IdGenerator.id());
        comment.setCreateTime(new Date());
        comment.setUserId(user.getId());
//        comment.setItemId(id);
//        comment.setItemType(3);
//        likeRepository.saveAndFlush(like);
        return ResultGenerator.genSuccessResult();
    }
//收藏
    @Transactional(rollbackFor = Exception.class)
    public Result cancelInformationReserve(HttpServletRequest request, String id) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
//            throw new ServiceException("用户没有登录!");
        }
        Information information = findInformationById(id);
        User user = userRepository.findUserByUsername(cookie.getValue());
        UserStrategy userStrategy = userStrategyRepository.findUserStrategyByInformationAndUser(information, user);
        //存在值就是取消预约.不存在值就是预约
        if (userStrategy != null) {
            userStrategyRepository.delete(userStrategy);
        } else {
            UserStrategy newUserStrategy = new UserStrategy();
            newUserStrategy.setId(IdGenerator.id());
            newUserStrategy.setCreateDate(new Date());
            newUserStrategy.setUser(user);
            newUserStrategy.setInformation(information);
            userStrategyRepository.saveAndFlush(newUserStrategy);
        }
        return ResultGenerator.genSuccessResult();
    }

    public List<UserStrategy> getInformationByUser(HttpServletRequest request) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
//            throw new ServiceException("未能获得正确的用户名");
            return new ArrayList<>();
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        return userStrategyRepository.findUserStrategyByUser(user);
    }

    public Information getInformationById(String id) {
        Information information = informationRepository.findById(id).orElseThrow(() -> new ServiceException("攻略ID错误"));
        return information;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveInformation(HttpServletRequest request, Information information) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            throw new ServiceException("未能获得正确的用户名");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());

        if (StringUtils.isEmpty(information.getId())) {//没有id的情况
            information.setId(IdGenerator.id());
            if (information.getStatus() == null) {
                //默认为停用
                information.setStatus(StatusEnum.DOWM_STATUS.getCode());
                information.setCreateDate(new Date());
                information.setUser(user);
            }
        } else {
            //有id的情况
            Information oldInformation = getInformationById(information.getId());
            information.setStatus(oldInformation.getStatus());
            information.setCreateDate(oldInformation.getCreateDate());
        }
        informationRepository.saveAndFlush(information);
        return ResultGenerator.genSuccessResult();
    }

    public List<Information> findTop10Strategy() {
        PageRequest pageable = PageRequest.of(0, 10);
        //查询启用的游览路线列表
        Page<Information> InformationPage = informationRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的路线
            predicates.add((cb.equal(root.get("status"), 0)));
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return InformationPage.getContent();
    }
}
