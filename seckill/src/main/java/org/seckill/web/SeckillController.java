package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description: 秒杀功能控制器
 * @Auther: X-Dragon
 * @Date: 2018/11/21 17:39
 * @Version: 1.0
 */
@Controller
@RequestMapping(name = "/seckill")
public class SeckillController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(name = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(name = "/{detailId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("detailId") Long detailId, Model model) {
        if (null == detailId) {// 如果传入的参数为空
//            return "redirect:/list";// 重定向到显示所有的信息
            return "redirect:/seckill/list";// 重定向到显示所有的信息
        }
        Seckill seckill = seckillService.getById(detailId);
        if (null == seckill) {
            return "forward:/list";// 转发到显示所有的信息
//            return "forward:/seckill/list";// 转发到显示所有的信息
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(name = "/{seckillId}/exposer", method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})// POST方式
//    @ResponseBody
    public SeckillResult exposer(@PathVariable("seckillId") long seckillId, Model model) {
        SeckillResult seckillResult;
        Exposer exposer = null;
        try {
            exposer = seckillService.exportSeckillUrl(seckillId);
            seckillResult = new SeckillResult(true, exposer);
        } catch (Exception e) {
            seckillResult = new SeckillResult(false, e.getMessage());
            logger.error(e.getMessage(), e);

        }
        return seckillResult;
    }

}
