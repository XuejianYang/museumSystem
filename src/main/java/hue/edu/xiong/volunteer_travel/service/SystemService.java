package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.core.ServiceException;
import hue.edu.xiong.volunteer_travel.enums.StatusEnum;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.*;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class SystemService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private TravelRouteRepository travelRouteRepository;

    @Autowired
    private TravelStrategyRepository travelStrategyRepository;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserExhibitionRepository userExhibitionRepository;
    @Autowired
    private UserRouteRepository userRouteRepository ;
    @Autowired
    private UserCommentRepository userCommentRepository;


    private Random random = new Random(100);

    public Result login(SysUser sysUser, HttpServletResponse response) {


        SysUser sysUserByUsrname = sysUserRepository.findSysUserByUsername(sysUser.getUsername());
        if (sysUserByUsrname == null) {
            return ResultGenerator.genFailResult("用户名错误！");
        } else {
            if (sysUser.getPassword().equals(sysUserByUsrname.getPassword())) {
                CookieUitl.set(response, "sysUsername", sysUser.getUsername(), 3600);
                return ResultGenerator.genSuccessResult();
            } else {

                return ResultGenerator.genFailResult("密码错误");
            }
        }

    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUitl.get(request, "sysUsername");
        if (cookie != null) {
            CookieUitl.set(response, "sysUsername", null, 0);
        }
    }

    public Page<User> getUserPage(Pageable pageable) {
        Page<User> userPage = userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("id")));
            return null;
        }, pageable);
        return userPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveUser(User user) {

        System.out.println(user.getId());


        if (StringUtils.isEmpty(user.getId())) {//没有id的情况
            user.setId(IdGenerator.id());
        }

        userRepository.saveAndFlush(user);
        return ResultGenerator.genSuccessResult();
    }

    public User getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ServiceException("用户ID错误"));
        return user;
    }


    public Page<Collection> getHotelPage(Pageable pageable) {
        Page<Collection> hotelPage = collectionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return hotelPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveCollection(Collection collection) {
        if (StringUtils.isEmpty(collection.getId())) {//没有id的情况
            collection.setId(IdGenerator.id());
            collection.setStatus(StatusEnum.DOWM_STATUS.getCode());
            collection.setCreateDate(new Date());
            String image = collection.getImage();
            collection.setImage(image.substring(0,image.lastIndexOf(".")));
        } else {
            //有id的情况
            Collection oldHotel = getCollectionById(collection.getId());
            collection.setStatus(oldHotel.getStatus());
            collection.setCreateDate(oldHotel.getCreateDate());
            collection.setImage(oldHotel.getImage());

        }
        collectionRepository.saveAndFlush(collection);
        return ResultGenerator.genSuccessResult();
    }

    public Collection getCollectionById(String id) {
        Collection collection = collectionRepository.findById(id).orElseThrow(() -> new ServiceException("藏品ID错误!"));
        return collection;
    }

    public Result updateStatus(String id) {
        Collection collection = getCollectionById(id);
        if (collection.getStatus().equals(StatusEnum.DOWM_STATUS.getCode())) {
            //改变状态
            collection.setStatus(StatusEnum.UP_STATUS.getCode());
        } else {
            collection.setStatus(StatusEnum.DOWM_STATUS.getCode());
        }
        collectionRepository.saveAndFlush(collection);
        return ResultGenerator.genSuccessResult();
    }

    public Page<Exhibition> getExhibitionPage(Pageable pageable) {
        Page<Exhibition> ExhibitionPage = exhibitionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        List<Exhibition> content = ExhibitionPage.getContent();
        content.forEach(s->{
            String id = s.getId();
            List<UserLike> likeList = likeRepository.findLikeByItemId(id);
            s.setLikeNum(likeList.size());
            List<UserComment> commentByItemId = userCommentRepository.findUserCommentByItemId(id);
            s.setCommentList(commentByItemId);
            List<UserExhibition> userExhibitionByExhibition = userExhibitionRepository.findUserExhibitionByExhibition(s);
            s.setPreNum(userExhibitionByExhibition.size());
        });

        return ExhibitionPage;
    }

    public Exhibition getExhibitionById(String id) {
        Exhibition exhibition = exhibitionRepository.findById(id).orElseThrow(() -> new ServiceException("景点ID错误"));
        return exhibition;
    }

    public Result updateExhibitionStatus(String id) {
        Exhibition exhibition = getExhibitionById(id);
        if (exhibition.getStatus().equals(StatusEnum.DOWM_STATUS.getCode())) {
            //改变状态
            exhibition.setStatus(StatusEnum.UP_STATUS.getCode());
        } else {
            exhibition.setStatus(StatusEnum.DOWM_STATUS.getCode());
        }
        exhibitionRepository.saveAndFlush(exhibition);
        return ResultGenerator.genSuccessResult();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveExhibition(Exhibition exhibition) {
        if (StringUtils.isEmpty(exhibition.getId())) {//没有id的情况
            exhibition.setId(IdGenerator.id());
            exhibition.setStatus(exhibition.getStatus());
            exhibition.setCreateDate(new Date());
            exhibition.setStartDate(exhibition.getStartDate());
            int i = random.nextInt(100);
            String images = exhibition.getImage();
            exhibition.setImage(images.substring(0,images.lastIndexOf(".")));
        } else {
            //有id的情况
            Exhibition oldExhibition = getExhibitionById(exhibition.getId());
            exhibition.setCreateDate(oldExhibition.getCreateDate());
            exhibition.setImage(oldExhibition.getImage());
            exhibition.setStartDate(exhibition.getStartDate());
            exhibition.setStatus(exhibition.getStatus());
        }
        exhibitionRepository.saveAndFlush(exhibition);
        return ResultGenerator.genSuccessResult();
    }

    public Page<TravelRoute> getTravelRoutePage(Pageable pageable) {
        Page<TravelRoute> travelRoutePage = travelRouteRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);

        List<TravelRoute> content = travelRoutePage.getContent();
        content.forEach(s->{
            String id = s.getId();
            List<UserLike> likeList = likeRepository.findLikeByItemId(id);
            s.setLikeNum(likeList.size());
            List<UserRoute> userRouteList = userRouteRepository.findUserRouteByTravelRoute(s);
            s.setPreNum(userRouteList.size());
            List<UserComment> commentByItemId = userCommentRepository.findUserCommentByItemId(id);
            s.setCommentList(commentByItemId);
        });
        return travelRoutePage;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveTravelRoute(TravelRoute travelRoute) {

        if (StringUtils.isEmpty(travelRoute.getId())) {//没有id的情况
            travelRoute.setId(IdGenerator.id());
            if (travelRoute.getStatus() == null) {
                //默认为停用
                travelRoute.setStatus(StatusEnum.UP_STATUS.getCode());
                travelRoute.setCollectNumber(0);
                travelRoute.setCreateDate(new Date());
            }
        } else {
            //有id的情况
            TravelRoute oldTravelRoute = getTravelRouteById(travelRoute.getId());
            travelRoute.setStatus(oldTravelRoute.getStatus());
            travelRoute.setCollectNumber(oldTravelRoute.getCollectNumber());
            travelRoute.setCreateDate(oldTravelRoute.getCreateDate());
        }
        travelRouteRepository.saveAndFlush(travelRoute);
        return ResultGenerator.genSuccessResult();
    }

    public TravelRoute getTravelRouteById(String id) {
        TravelRoute travelRoute = travelRouteRepository.findById(id).orElseThrow(() -> new ServiceException("路线ID错误!"));
        return travelRoute;
    }

    public Result updateTravelRouteStatus(String id) {
        TravelRoute travelRoute = getTravelRouteById(id);
        if (travelRoute.getStatus().equals(StatusEnum.DOWM_STATUS.getCode())) {
            //改变状态
            travelRoute.setStatus(StatusEnum.UP_STATUS.getCode());
        } else {
            travelRoute.setStatus(StatusEnum.DOWM_STATUS.getCode());
        }
        travelRouteRepository.saveAndFlush(travelRoute);
        return ResultGenerator.genSuccessResult();
    }

    public Page<TravelStrategy> getTravelStrategyPage(Pageable pageable) {
        Page<TravelStrategy> travelStrategyPage = travelStrategyRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
//            predicates.add((cb.equal(root.get("status"), 0)));
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return travelStrategyPage;
    }

    public TravelStrategy getTravelStrategyById(String id) {
        TravelStrategy travelStrategy = travelStrategyRepository.findById(id).orElseThrow(() -> new ServiceException("攻略ID错误!"));
        return travelStrategy;
    }

    public Result updateTravelStrategyStatus(String id) {
        TravelStrategy travelStrategy = getTravelStrategyById(id);
        if (travelStrategy.getStatus().equals(StatusEnum.DOWM_STATUS.getCode())) {
            //改变状态
            travelStrategy.setStatus(StatusEnum.UP_STATUS.getCode());
        } else {
            travelStrategy.setStatus(StatusEnum.DOWM_STATUS.getCode());
        }
        travelStrategyRepository.saveAndFlush(travelStrategy);
        return ResultGenerator.genSuccessResult();
    }

    public Result updateTravelStrategyStatusSH(String id) {
        TravelStrategy travelStrategy = getTravelStrategyById(id);
        if (travelStrategy.getStatus().equals(StatusEnum.DOWM_STATUS.getCode())) {
            //改变状态
            travelStrategy.setStatus(StatusEnum.UP_STATUS.getCode());
        } else {
            travelStrategy.setStatus(StatusEnum.DOWM_STATUS.getCode());
        }
        travelStrategyRepository.saveAndFlush(travelStrategy);
        return ResultGenerator.genSuccessResult();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result adminSaveTravelStrategy(TravelStrategy travelStrategy) {
        travelStrategy.setId(IdGenerator.id());
        travelStrategy.setCreateDate(new Date());
        travelStrategy.setDescribe(travelStrategy.getDescribe());
        travelStrategy.setStatus(StatusEnum.UP_STATUS.getCode());
        travelStrategy.setCreateDate(new Date());
        travelStrategyRepository.saveAndFlush(travelStrategy);
        return ResultGenerator.genSuccessResult();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveTravelStrategy(HttpServletRequest request,TravelStrategy travelStrategy) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        if (StringUtils.isEmpty(travelStrategy.getId())) {//没有id的情况
            travelStrategy.setId(IdGenerator.id());
            travelStrategy.setUser(user);
            if (travelStrategy.getStatus() == null) {
                //默认为停用
                travelStrategy.setStatus(StatusEnum.DOWM_STATUS.getCode());
                travelStrategy.setCreateDate(new Date());
            }
        } else {
            //有id的情况
            TravelStrategy oldTravelStrategy = getTravelStrategyById(travelStrategy.getId());
            travelStrategy.setStatus(StatusEnum.Third_STATUS.getCode());
            travelStrategy.setCreateDate(oldTravelStrategy.getCreateDate());
            travelStrategy.setUser(oldTravelStrategy.getUser());
            travelStrategy.setTitle(oldTravelStrategy.getTitle());
            travelStrategy.setDescribe(oldTravelStrategy.getDescribe());
            travelStrategy.setErrorMessage(request.getParameter("errorMessage"));
        }
        travelStrategyRepository.saveAndFlush(travelStrategy);
        return ResultGenerator.genSuccessResult();
    }
}
