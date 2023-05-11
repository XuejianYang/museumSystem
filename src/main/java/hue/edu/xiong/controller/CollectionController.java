package hue.edu.xiong.controller;

import hue.edu.xiong.model.Collection;
import hue.edu.xiong.repository.CollectionRepository;
import hue.edu.xiong.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private ReserveService reserveService;


    @RequestMapping("/collectionTypeUI")
    public String reservecollectionListUI(Model model, @ModelAttribute("searchName") String searchName, @ModelAttribute("type") String type, @PageableDefault(size = 10) Pageable pageable) {
        Page<Collection> collectionPage = collectionRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的酒店
//            predicates.add((cb.equal(root.get("status"), 0)));
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
        List<Collection> top10collection = reserveService.getTop10collection();
        List<Collection> all = collectionRepository.findAll();
        List<String> typeList = all.stream().map(s -> s.getType()).distinct().collect(Collectors.toList());
        model.addAttribute("top10collection", top10collection);
        model.addAttribute("page", collectionPage);
        model.addAttribute("typeList", typeList);
        return "reserve/collection-type-details";
    }

}
