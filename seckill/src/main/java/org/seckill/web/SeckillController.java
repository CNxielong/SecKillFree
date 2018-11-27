package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Description: 秒杀功能控制器
 * @Auther: X-Dragon
 * @Date: 2018/11/21 17:39
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET,
            produces = {"application/json;charset=utf-8"})
    public String list(Model model) {
        System.out.println("进入List");
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(value = "/{detailId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("detailId") Long detailId, Model model) {
        if (null == detailId) {// 如果传入的参数为空
            return "redirect:/list";// 重定向到显示所有的信息
//            return "redirect:/seckill/list";// 重定向到显示所有的信息
        }
        Seckill seckill = seckillService.getById(detailId);
        if (null == seckill) {
            return "forward:/list";// 转发到显示所有的信息
//            return "forward:/seckill/list";// 转发到显示所有的信息
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})// POST方式
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") long seckillId, Model model) {
        SeckillResult<Exposer> seckillResult;
        Exposer exposer = null;
        try {
            exposer = seckillService.exportSeckillUrl(seckillId);
            seckillResult = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            seckillResult = new SeckillResult<Exposer>(false, e.getMessage());
            logger.error(e.getMessage(), e);

        }
        return seckillResult;
    }

    @RequestMapping(value="/{seckillId}/{md5}/execution",method = RequestMethod.POST,
    produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") long seckillId, @PathVariable("md5") String md5
            ,@CookieValue(value = "killPhone",required = false) Long phone){//允许取不到value 让JAVA代码处理相关逻辑 而不是让SpringMVC取不到数报错
        if(null == phone){ //如果没有取到手机号
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        try {
//            SeckillExecution seckillExecution = seckillService.excuteSeckill(seckillId, phone, md5); //原始未优化
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, phone, md5); // 通过存储过程执行秒杀
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        }catch (SeckillCloseException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        }catch (SeckillException e) {
            logger.error(e.getMessage(),e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false, seckillExecution);
        }
    }

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> execute(){
        Date date = new Date();
        SeckillResult seckillResult = new SeckillResult(true, date.getTime());
        return seckillResult;
    }


}
