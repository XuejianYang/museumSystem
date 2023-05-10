package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.UserRepository;
import hue.edu.xiong.volunteer_travel.repository.UserYuYueRepository;
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
public class UserYuYueService {
    @Autowired
    private UserYuYueRepository userYuYueRepository;

    @Autowired
    private UserRepository userRepository;



    public Page<UserYuYue> userYuYuePage(String searchName, Pageable pageable) {
        //查询启用的景点列表
        Page<UserYuYue> exhibitionPage = userYuYueRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的景点
//            predicates.add((cb.equal(root.get("status"), 0)));
            //景点name模糊查询
            if (!StringUtils.isEmpty(searchName)) {
                predicates.add((cb.like(root.get("name"), "%" + searchName + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
//            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return exhibitionPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveYuYue(HttpServletRequest request, List<UserYuYue> yuYueList) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        yuYueList.forEach(s->{
            s.setId(IdGenerator.id());
            s.setYuyueer(cookie.getValue());
        userYuYueRepository.saveAndFlush(s);
        });

        return ResultGenerator.genSuccessResult();
    }
    @Transactional(rollbackFor = Exception.class)
    public Result getYuyueList() {
        List<UserYuYue> all = userYuYueRepository.findAll();
        return ResultGenerator.genSuccessResult(all);
    }
    @Transactional(rollbackFor = Exception.class)
    public Result getYuyueListByCard(String card) {
        List<UserYuYue> all = userYuYueRepository.findUserYuYueByCard(card);
        return ResultGenerator.genSuccessResult(all);
    }
    @Transactional(rollbackFor = Exception.class)
    public  List<UserYuYue> getYuyueListByName(String name) {
        List<UserYuYue> all = userYuYueRepository.findUserYuYueByYuyueer(name);
        return all;
    }
    @Transactional(rollbackFor = Exception.class)
    public Result getYuyueNum( String date) {
        List<UserYuYue> userYuYueByDate = userYuYueRepository.findUserYuYueByDate(date);
        int size = userYuYueByDate.size();
        YuYueNum yuYueNum = new YuYueNum();
        yuYueNum.setPm(100);
        yuYueNum.setAm(100);
        if(size==0){
            return ResultGenerator.genSuccessResult(yuYueNum);
        }
        Long amCount = userYuYueByDate.stream().filter(s -> "0".equals(s.getDuring())).count();
        int intValue = amCount.intValue();
        yuYueNum.setAm(100-intValue);
        yuYueNum.setPm(100-size - amCount.intValue());
        return ResultGenerator.genSuccessResult(yuYueNum);
    }
}
