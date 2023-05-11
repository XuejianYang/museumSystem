package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.core.ServiceException;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.ExhibitionRepository;
import hue.edu.xiong.volunteer_travel.repository.CollectionRepository;
import hue.edu.xiong.volunteer_travel.repository.UserCollectionRepository;
import hue.edu.xiong.volunteer_travel.repository.UserExhibitionRepository;
import hue.edu.xiong.volunteer_travel.repository.UserRepository;
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
public class ReserveService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCollectionRepository userCollectionRepository;

    @Autowired
    private UserExhibitionRepository userExhibitionRepository;

    public Page<Collection> reserveCollectionListUI(String searchName, String type , Pageable pageable) {
        //查询启用的酒店列表
        Page<Collection> CollectionPage = collectionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的酒店
            predicates.add((cb.equal(root.get("status"), 0)));
            //酒店name模糊查询
            if (!StringUtils.isEmpty(searchName)) {
                predicates.add((cb.like(root.get("name"), "%" + searchName + "%")));
            }
            if (!StringUtils.isEmpty(type)) {
                predicates.add((cb.like(root.get("type"), "%" + type + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return CollectionPage;
    }

    public Collection findCollectionById(String id) {
        return collectionRepository.findById(id).orElseThrow(() -> new ServiceException("酒店id错误!"));
    }

    public Page<Exhibition> reserveExhibitionListUI(String searchName, Pageable pageable) {
        //查询启用的展览列表
        Page<Exhibition> ExhibitionPage = exhibitionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,
            predicates.add((cb.equal(root.get("status"), 0)));
            //展览name模糊查询
            if (!StringUtils.isEmpty(searchName)) {
                predicates.add((cb.like(root.get("name"), "%" + searchName + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return ExhibitionPage;
    }

    public Page<Exhibition> reserveExhibitionListUI2(String searchName, Pageable pageable) {
        //查询启用的展览列表
        Page<Exhibition> ExhibitionPage = exhibitionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的展览
            predicates.add((cb.equal(root.get("status"), 1)));
            //展览name模糊查询
            if (!StringUtils.isEmpty(searchName)) {
                predicates.add((cb.like(root.get("name"), "%" + searchName + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return ExhibitionPage;
    }

    public Exhibition findExhibitionById(String id) {
        return exhibitionRepository.findById(id).orElseThrow(() -> new ServiceException("景点id错误!"));
    }


    public List<UserCollection> getReserveCollectionByUser(HttpServletRequest request) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
//            throw new ServiceException("未能获得正确的用户名");
            return new ArrayList<>();
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        return userCollectionRepository.findUserCollectionsByUser(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result cancelReserve(HttpServletRequest request, String id) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
//            throw new ServiceException("用户没有登录!");
        }
        Collection collection = findCollectionById(id);
        User user = userRepository.findUserByUsername(cookie.getValue());
        UserCollection userCollection = userCollectionRepository.findUserCollectionByCollectionAndUser(collection, user);
        //存在值就是取消预约.不存在值就是预约
        if (userCollection != null) {
            userCollectionRepository.delete(userCollection);
        } else {
            UserCollection newUserCollection = new UserCollection();
            newUserCollection.setId(IdGenerator.id());
            newUserCollection.setCreateDate(new Date());
            newUserCollection.setUser(user);
            newUserCollection.setCollection(collection);
            userCollectionRepository.saveAndFlush(newUserCollection);
        }
        return ResultGenerator.genSuccessResult();
    }

    public Boolean isReserveCollection(HttpServletRequest request, String id) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie != null) {
            User user = userRepository.findUserByUsername(cookie.getValue());
            Collection collection = findCollectionById(id);
            UserCollection userCollection = userCollectionRepository.findUserCollectionByCollectionAndUser(collection, user);
            //每个酒店只能预约一次
            if (userCollection != null) {
                return true;
            }
        }
        return false;
    }


    public List<UserExhibition> getReserveExhibitionByUser(HttpServletRequest request) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
//            throw new ServiceException("未能获得正确的用户名");
            return new ArrayList<>();
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        return userExhibitionRepository.findUserExhibitionByUser(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result cancelExhibitionReserve(HttpServletRequest request, String id) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        Exhibition exhibition = findExhibitionById(id);
        User user = userRepository.findUserByUsername(cookie.getValue());
        UserExhibition userExhibition = userExhibitionRepository.findUserExhibitionByExhibitionAndUser(exhibition, user);
        //存在值就是取消预约.不存在值就是预约
        if (userExhibition != null) {
            userExhibitionRepository.delete(userExhibition);
        } else {
            UserExhibition newUserExhibition = new UserExhibition();
            newUserExhibition.setId(IdGenerator.id());
            newUserExhibition.setCreateDate(new Date());
            newUserExhibition.setUser(user);
            newUserExhibition.setExhibition(exhibition);
            userExhibitionRepository.saveAndFlush(newUserExhibition);
        }
        return ResultGenerator.genSuccessResult();
    }

    public Boolean isReserveExhibition(HttpServletRequest request, String id) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie != null) {
            User user = userRepository.findUserByUsername(cookie.getValue());
            Exhibition exhibition = findExhibitionById(id);
            UserExhibition userExhibition = userExhibitionRepository.findUserExhibitionByExhibitionAndUser(exhibition, user);
            //只能预约一次
            if (userExhibition != null) {
                return true;
            }
        }
        return false;
    }

    public List<Collection> getTop10collection() {
        PageRequest pageable = PageRequest.of(0, 10);
        //查询启用的酒店列表
        Page<Collection> CollectionPage = collectionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的酒店
//            predicates.add((cb.equal(root.get("status"), 0)));
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return CollectionPage.getContent();
    }

    public List<Exhibition> getTop10Exhibition() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Exhibition> ExhibitionPage = exhibitionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的景点
//            predicates.add((cb.equal(root.get("status"), 0)));
            //景点name模糊查询
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return ExhibitionPage.getContent();
    }
}

