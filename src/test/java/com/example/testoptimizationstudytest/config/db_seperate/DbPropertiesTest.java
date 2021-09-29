//package com.example.testoptimizationstudytest.config.db_seperate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.example.testoptimizationstudytest.config.db_seperate.DbProperties.DbProperty;
//import java.util.Map;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class DbPropertiesTest {
//
//    @Autowired
//    DbProperties dbProperties;
//
//    @Test
//    void getValues() {
//        Map<String, DbProperty> properties = dbProperties.getProperties();
//
//        assertThat(properties).hasSize(2);
//    }
//}