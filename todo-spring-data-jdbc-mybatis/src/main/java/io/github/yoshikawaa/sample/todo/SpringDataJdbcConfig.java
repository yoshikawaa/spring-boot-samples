package io.github.yoshikawaa.sample.todo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.mybatis.MyBatisDataAccessStrategy;
import org.springframework.data.jdbc.mybatis.NamespaceStrategy;

@Configuration
public class SpringDataJdbcConfig {

    @Bean
    @Primary
    public DataAccessStrategy mybatisDataAccessStrategy(SqlSession sqlSession) {
        MyBatisDataAccessStrategy strategy = new MyBatisDataAccessStrategy(sqlSession);
        strategy.setNamespaceStrategy(new NamespaceStrategy() {
            @Override
            public String getNamespace(Class<?> domainType) {
                return domainType.getPackage().getName() + ".mapper." + domainType.getSimpleName() + "Mapper";
            }
        });
        return strategy;
    }
}
