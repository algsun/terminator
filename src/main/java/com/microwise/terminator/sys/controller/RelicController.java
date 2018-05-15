package com.microwise.terminator.sys.controller;

import com.microwise.terminator.sys.entity.Relic;
import com.microwise.terminator.sys.service.*;
import com.microwise.terminator.sys.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;

/**
 * 文物管理Controller
 *
 * @author bai.weixing
 * @since 2017/9/20.
 */
@RequestMapping("/sys/relics")
@Controller
public class RelicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelicController.class);
    /**
     * 文物照片路径
     */
    private static final String PHOTO_PATH = "relic" + File.separator + "photo" + File.separator;

    @Value("${terminator.imagesPath}")
    private String imagesPath;

    @Autowired
    private RelicService relicService;

    @Autowired
    private EraService eraService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private TextureService textureService;

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public String index(@RequestParam(required = false) String name, @RequestParam(defaultValue = "1") int pageNumber,
                        @RequestParam(defaultValue = "20") int pageSize, Model model) {
        model.addAttribute("pageInfo", relicService.findRelicsByName(name, pageNumber, pageSize));
        model.addAttribute("name", name);
        model.addAttribute(Constants.PAGE_PATH, "relic/index");
        return "index";
    }

    @GetMapping("/new")
    public String create(Relic relic, Model model) {
        model.addAttribute("relic", relic);
        model.addAttribute("eras", eraService.findList(relic.getEra()));
        model.addAttribute("textures", textureService.findList(relic.getTexture()));
        model.addAttribute("levels", levelService.findList(relic.getLevel()));
        model.addAttribute(Constants.PAGE_PATH, "relic/new");
        return "index";
    }

    @PostMapping
    public String save(@Valid Relic relic, MultipartFile relicPhoto, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "添加文物失败");
            redirectAttributes.addFlashAttribute("relic", relic);
            return "redirect:/sys/relics/new";
        }
        try {
            relicService.insert(relic, relicPhoto);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "添加文物成功");
            LOGGER.info("添加文物成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "添加文物失败");
            redirectAttributes.addFlashAttribute("relic", relic);
            LOGGER.error("添加文物失败", e);
            return "redirect:/sys/relics/new";
        }
        return "redirect:/sys/relics";
    }


    @GetMapping("/edit")
    public String edit(String id, Model model) {
        Relic relic = relicService.get(id);
        model.addAttribute("relic", relic);
        model.addAttribute("eras", eraService.findList(relic.getEra()));
        model.addAttribute("textures", textureService.findList(relic.getTexture()));
        model.addAttribute("levels", levelService.findList(relic.getLevel()));
        model.addAttribute("photo", photoService.get(relic.getPhotoId()));
        model.addAttribute(Constants.PAGE_PATH, "relic/edit");
        return "index";
    }

    @PostMapping("/{id}/update")
    public String update(@Valid Relic relic, MultipartFile relicPhoto, String operation,
                         BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "文物修改失败");
            return "redirect:/sys/relics/edit?id={id}";
        }
        try {
            relicService.update(relic, relicPhoto, operation);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "文物修改成功");
            LOGGER.info("文物修改成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "文物修改失败");
            LOGGER.error("文物修改失败", e);
            return "redirect:/sys/relics/edit?id={id}";
        }
        return "redirect:/sys/relics";
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (relicService.isUsed(id)) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "文物正在被使用，不可删除");
            LOGGER.info("文物正在被使用，不可删除");
            return "redirect:/sys/relics";
        }
        try {
            relicService.delete(id);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "文物删除成功");
            LOGGER.info("文物删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "文物删除成功");
            LOGGER.error("文物删除失败", e);
        }
        return "redirect:/sys/relics";
    }

    @PostMapping("checkName")
    @ResponseBody
    public boolean checkName(String name, String id) {
        return relicService.findRelics(name, id).size() == 0;
    }

    @GetMapping("findRelicsByName")
    public String findRelicsByName(Model model, @ModelAttribute(name = "name") String name) {
        try {
            model.addAttribute("relics", relicService.findRelics(name, null));
        } catch (Exception e) {
            LOGGER.error("检索文物数据失败", e);
        }
        return "overview/index";
    }

    @GetMapping("/findRelic")
    @ResponseBody
    public Relic findRelic(String id) {
        return relicService.get(id);
    }
}
