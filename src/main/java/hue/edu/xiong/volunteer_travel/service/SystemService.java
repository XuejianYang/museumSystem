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
    private InformationRepository informationRepository;

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


        String images = user.getImg();
        user.setImg(images.substring(0,images.lastIndexOf(".")));

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


    public Page<Collection> getCollectionPage(Pageable pageable) {
        Page<Collection> CollectionPage = collectionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return CollectionPage;
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
            Collection oldCollection = getCollectionById(collection.getId());
            collection.setStatus(oldCollection.getStatus());
            collection.setCreateDate(oldCollection.getCreateDate());
            collection.setImage(oldCollection.getImage());

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
            String images = exhibition.getImage();
            exhibition.setImage(images.substring(0,images.lastIndexOf(".")));
            exhibition.setCreateDate(oldExhibition.getCreateDate());
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

    public Page<Information> getInformationPage(Pageable pageable) {
        Page<Information> InformationPage = informationRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add((cb.equal(root.get("status"), 0)));
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return InformationPage;
    }

    public Information getInformationById(String id) {
        Information information = informationRepository.findById(id).orElseThrow(() -> new ServiceException("攻略ID错误!"));
        return information;
    }

    public Result updateInformationStatus(String id) {
        Information information = getInformationById(id);
        if (information.getStatus().equals(StatusEnum.DOWM_STATUS.getCode())) {
            //改变状态
            information.setStatus(StatusEnum.UP_STATUS.getCode());
        } else {
            information.setStatus(StatusEnum.DOWM_STATUS.getCode());
        }
        informationRepository.saveAndFlush(information);
        return ResultGenerator.genSuccessResult();
    }

    public Result updateInformationStatusSH(String id) {
        Information information = getInformationById(id);
        if (information.getStatus().equals(StatusEnum.DOWM_STATUS.getCode())) {
            //改变状态
            information.setStatus(StatusEnum.UP_STATUS.getCode());
        } else {
            information.setStatus(StatusEnum.DOWM_STATUS.getCode());
        }
        informationRepository.saveAndFlush(information);
        return ResultGenerator.genSuccessResult();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result adminSaveInformation(Information information) {
        information.setId(IdGenerator.id());
        information.setCreateDate(new Date());
        information.setDescribe(information.getDescribe());
        information.setStatus(StatusEnum.UP_STATUS.getCode());
        information.setCreateDate(new Date());
        informationRepository.saveAndFlush(information);
        return ResultGenerator.genSuccessResult();
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveInformation(HttpServletRequest request, Information information) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        if (StringUtils.isEmpty(information.getId())) {//没有id的情况
            information.setId(IdGenerator.id());
            information.setUser(user);
            if (information.getStatus() == null) {
                //默认为停用
                information.setStatus(StatusEnum.DOWM_STATUS.getCode());
                information.setCreateDate(new Date());
            }
        } else {
            //有id的情况
            Information oldInformation = getInformationById(information.getId());
            information.setStatus(StatusEnum.Third_STATUS.getCode());
            information.setCreateDate(oldInformation.getCreateDate());
            information.setUser(oldInformation.getUser());
            information.setTitle(oldInformation.getTitle());
            information.setDescribe(oldInformation.getDescribe());
            information.setErrorMessage(request.getParameter("errorMessage"));
        }
        informationRepository.saveAndFlush(information);
        return ResultGenerator.genSuccessResult();
    }
}
