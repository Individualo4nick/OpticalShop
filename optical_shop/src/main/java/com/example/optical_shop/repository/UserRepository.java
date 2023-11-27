package com.example.optical_shop.repository;

import com.example.optical_shop.dto.ChangeUserInfoDto;
import com.example.optical_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);
    @Transactional
    @Modifying
    @Query("update User u set u.email = :#{#dto.email}, u.name = :#{#dto.name}, u.surname = :#{#dto.surname}, u.address = :#{#dto.address} where u.login = :login")
    void changeUserInfo(@Param("login") String login, @Param("dto") ChangeUserInfoDto changeUserInfoDto);
}
