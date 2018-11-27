package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junitSpring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
//@ContextConfiguration({"classpath:spring/*"}) // 也行
public class SeckillServiceImplTest {

    //Logger日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;
//    private SeckillServiceImpl seckillService; //会报错

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}",seckillList);// {} 属于Object占位符

    }

    @Test
    public void getById() {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("秒杀信息=#{}", seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("暴露的秒杀信息#{}", exposer);
        /**
         * - 暴露的秒杀信息#Exposer{exposed=true, md5='e97c9669c60015d27841831c38e65481', seckillId=1000, now=0, start=0, end=0}
         */
    }

    @Test
    public void excuteSeckill() {
        long seckillId = 1000L;
        long userPhone = 17744494563L;
        SeckillExecution seckillExecution = seckillService.excuteSeckill(seckillId, userPhone, "e97c9669c60015d27841831c38e65481");
        logger.info("执行结果#{}", seckillExecution);
    }

    @Test
    public void testSeckillLogic(){
        long seckillId = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("暴露的秒杀信息#{}", exposer);
        if (null != exposer){// 暴露了秒杀地址
            long userPhone = 17744494563L;

            try {
                SeckillExecution seckillExecution = seckillService.excuteSeckill(exposer.getSeckillId(), userPhone, exposer.getMd5());
                logger.info("暴露的秒杀信息#{}", seckillExecution);
            } catch (SeckillCloseException e) {
                logger.info("暴露的秒杀信息#{}", e.getMessage());
            }catch (RepeatKillException e) {
                logger.info("暴露的秒杀信息#{}", e.getMessage());
            }catch (SeckillException e) {
                logger.info("暴露的秒杀信息#{}", e.getMessage());
            }

        }
    }

    @Test
    public void testExecuteSeckillProcedure(){
        long seckillId = 1001L;
        long phone = 17744494563L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 =  exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }

    }


}