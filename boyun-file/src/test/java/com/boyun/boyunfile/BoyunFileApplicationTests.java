package com.boyun.boyunfile;

import com.boyun.boyunfile.domain.User;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.mapper.RecoveryFileMapper;
import com.boyun.boyunfile.mapper.UserMapper;
import com.boyun.boyunfile.mapper.UserfileMapper;
import com.boyun.boyunfile.service.UserFileService;
import com.boyun.boyunfile.util.DateUtil;
import com.boyun.boyunfile.util.PropertiesUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
class c1{
    int id;
    String name;
    Life life;
}

@Data
class c2{
    int id;
    String name;
    Life life;
}

@Data
class Father {
    private String face; // 长相
    private String height; // 身高
    private Life life; // 生命
}
@Data
class Life {
    private String status;
}

class A{
    public A(){
        System.out.println("A");
    }
}
class B extends A{
    public B(){
        System.out.println("B");
    }
}

@SpringBootTest
class BoyunFileApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private UserfileMapper userfileMapper;

    @Resource
    private RecoveryFileMapper recoveryFileMapper;

    @Test
    void test(){
//        LambdaQueryWrapper<UserFile> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UserFile::getDeleteFlag, 1);
//        List<UserFile> userFiles = userfileMapper.selectList(wrapper);
//        for (UserFile userFile : userFiles) {
//            System.out.println(userFile);
//        }
        List<UserFile> files = userfileMapper.selectRecoveryFile("6eb4842a-9b58-4913-8210-62a2f714b167");
        for (UserFile file : files) {

            System.out.println(file);
        }
    }


    @Test
    void utilTest() throws FileNotFoundException {
        String localStoragePath = PropertiesUtil.getProperty("file.local-storage-path");
        System.out.println(localStoragePath);
//        System.out.println(PathUtil.getFilePath());
//        System.out.println(PathUtil.getStaticPath());
//        System.out.println(PathUtil.getProjectRootPath());
//
//        System.out.println(localStoragePath);
//
//        System.out.println(PropertiesUtil.getProperty("jwt.payload.registerd-claims.aud"));
//
//        String url = ResourceUtils.getURL("classpath:").getPath();
//        System.out.println(url);
//        String s = PathUtil.urlDecode(new File(url).getAbsolutePath());
//        System.out.println(s);
//        System.out.println(new File(url).getAbsolutePath());
    }


    @Test
    void contextLoads() {
//        PropertiesConfig propertiesConfig = new PropertiesConfig();
//        propertiesConfig.setProperties();
//        System.out.println(propertiesConfig.getEnv());
//        System.out.println(System.currentTimeMillis());
        String filePath = "'";
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");
        System.out.println(filePath);
    }

    @Test
    void mybatisTest(){
        User user = new User();
        user.setUsername("用户名2");
        user.setPassword("密码2");
        user.setTelephone("手机号2");
        userMapper.insertUser(user);
        System.out.println("数据库字段查询结果显示");
//        List<User> list = userMapper.selectUser();
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }


    @Resource
    UserFileService userfileService;
    @Test
    void testVolatile(){
//        StringBuffer stringBuffer = new StringBuffer("asa");
//        stringBuffer.append(1);
//        System.out.println(stringBuffer.toString());
//        LambdaQueryWrapper<UserFile> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UserFile::getFileName, "titleLogo");
//        List<UserFile> files = userfileService.list(wrapper);
//        System.out.println(files);
        StringBuffer stringBuffer = new StringBuffer("123");
        stringBuffer.append(55);
        System.out.println(stringBuffer);
    }

    @Test
    void testUUID() {
        String uuid = UUID.randomUUID().toString().replace("-","");
        System.out.println(uuid);
    }

    @Test
    void time() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date1 = df.parse("2022-11-21 15:37:38");
        Date date2 = new Date();
        System.out.println(date1.getTime() < date2.getTime());
    }
}
