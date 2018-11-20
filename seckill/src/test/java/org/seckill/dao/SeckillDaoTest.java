package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description: org.seckill.dao.SeckillDao 测试类Junit4
 * @Auther: X-Dragon
 * @Date: 2018/11/18 16:42
 * 配置spring和junit整合,junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junitSpring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入DAO实现类
    @Autowired
   private SeckillDao seckillDao;

    @Test
    public void queryById() {
        Long id = 1000L;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
         * java没有保存形参的记录queryAll(int offset, int limit)->queryAll(arg0, arg1)
         * Mapper文件加入@Param标注 List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
         */
        List<Seckill> seckills = seckillDao.queryAll(0, 1000);
        for ( Seckill seckill:seckills) {
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        System.out.println("Date:"+killTime);
        int number = seckillDao.reduceNumber(1000, killTime);
        System.out.println("修改了"+number+"条记录");
    }


}