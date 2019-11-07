package com.jfeat;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class STest {
    @Resource
    QueryBonusDao queryBonusDao;

//    @Test
//    public void test() throws Exception {
//        System.out.println(queryBonusDao.getTeamBonusMonth(4L));
//    }


}
