package com.jqp.test.user.infrastructure;

import com.jqp.ddd.infrastructure.jdbc.SimpleJdbcRepository;
import com.jqp.test.user.domain.User;
import com.jqp.test.user.domain.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储实现
 * 支持Spring和非Spring环境
 *
 * @author JQP
 * @date 2026/02/28
 */
@Repository
public class UserRepositoryImpl extends SimpleJdbcRepository<User, Long> implements UserRepository {

    @Override
    public Optional<User> findByUsername(String username) {
        return findOneByWhere("username = ? and deleted = 0", username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findOneByWhere("email = ? and deleted = 0", email);
    }

    @Override
    public List<User> findActiveUsers() {
        return findByWhere("status = 1 and deleted = 0 order by id asc");
    }

    @Override
    public List<User> findByAgeRange(Integer minAge, Integer maxAge) {
        return findByWhere("age >= ? and age <= ? and deleted = 0 order by age asc", minAge, maxAge);
    }
}
