package com.stormstars.stellastone.service.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stormstars.stellastone.model.user.User;

public interface IUserService {

    void save(User u);

    void update(User u);

    void delete(User u);

    User findById(Long id);

    ArrayList<User> findAll();

    User findByUsername(String username);

    void autologin(String username, String password);

    void autologout(HttpServletRequest request, HttpServletResponse response);

    List<User> search(String keyword);
    
    // public Fusee findFuseeByUserId(Long id);

    // ArrayList<Long> findIdFuseeByUser(Long userId);

}
