package hue.edu.xiong.controller;

import hue.edu.xiong.model.Exhibition;
import hue.edu.xiong.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/system")
@Controller
public class ExhibitionController {

    @Autowired
    private SystemService systemService;
    @RequestMapping("/ExhibitionListUI")
    public String ExhibitionListUI(Model model, @PageableDefault(size = 4) Pageable pageable) {
        Page<Exhibition> page = systemService.getExhibitionPage(pageable);
        model.addAttribute("page", page);
        return "system/exhibition/list";
    }

}
