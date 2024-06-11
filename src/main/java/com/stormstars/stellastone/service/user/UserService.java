package com.stormstars.stellastone.service.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.stormstars.stellastone.model.user.User;
import com.stormstars.stellastone.repository.UserRepository;


@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository uRepo;

    @Lazy // Allocate when needed
    @Autowired
    private AuthenticationManager authenticationManager;

    public final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /********************************
     ** TEST USER
     ********************************/

    /**
     * @param u : User object
     * @return Boolean
     */
    public Boolean canConnect(User u) {
        User cmp = uRepo.findByUsername(u.getUsername());
        return (isUserRegistered(u) && arePasswordMatching(u.getPassword(), cmp.getPassword()));
    }

    /**
     * @param u       : User object
     * @param current : current User object
     * @return Boolean
     */
    public Boolean canUpdate(String username, User current) {
        return username.equals(current.getUsername()) || !isUserRegistered(username);
    }

    /**
     * @param u    : User object
     * @param pwd1 : the new password
     * @param pwd2 : the confirmation
     * @return Boolean
     */
    public Boolean canUpdatePassword(Long id, String oldPwd, String pwd1, String pwd2) {
        return !oldPwd.equals("") && pwd1.equals(pwd2)
                && arePasswordMatching(oldPwd, findById(id).getPassword());
    }

    /**
     * @param pwd1 : password
     * @param pwd2 : password
     * @return Boolean
     */
    private Boolean arePasswordMatching(String pwd1, String pwd2) {
        return encoder.matches(pwd1, pwd2);
    }

    /**
     * @param u : User object
     * @return Boolean
     */
    public Boolean isUserRegistered(User u) {
        return (uRepo.findByUsername(u.getUsername()) != null);
    }

    /**
     * @param u : User username property
     * @return Boolean
     */
    public Boolean isUserRegistered(String u) {
        return (uRepo.findByUsername(u) != null);
    }

    /********************************
     ** FIND IN DB
     ********************************/

    /**
     * @return @return ArrayList<User>
     */
    @Override
    public ArrayList<User> findAll() {
        return (ArrayList<User>) uRepo.findAll();
    }

    /**
     * @param id : User id property
     * @return User
     */
    @Override
    public User findById(Long id) {
        return uRepo.findUserById(id);
    }

    /**
     * @param rawPwd : the raw password
     * @return String
     */
    public String encodePassword(String rawPwd) {
        return encoder.encode(rawPwd);
    }

    /**
     * @param username : User username property
     * @return User
     */
    @Override
    public User findByUsername(String username) {
        return uRepo.findByUsername(username);
    }

    /********************************
     ** UPDATE DB
     ********************************/

    /**
     * @param u : User object
     */
    @Override
    public void save(User u) {
        u.setPassword(encodePassword(u.getPassword()));
        uRepo.save(u);
    }

    /**
     * @param u : User object
     */
    @Override
    public void update(User u) {
        uRepo.save(u);
    }

    /**
     * @param u : User object
     */
    @Override
    public void delete(User u) {
        uRepo.delete(u);
    }

    // @Override
    // public Fusee findFuseeByUserId(Long id) {
    //     return uRepo.findFuseeByUserId(id);
    // }

    /********************************
     ** PROCESS DATA LOCALLY
     ********************************/
    /**
     * 
     * @param arrId : Array of User id
     *              Get ArrayList of User from an array list of id User
     * @return ArrayList<User>
     */
    public ArrayList<User> getArrayUserByArrayidUser(ArrayList<Long> arrId) {
        ArrayList<User> arrUser = new ArrayList<>();
        for (Long id : arrId)
            arrUser.add(findById(id));
        return arrUser;
    }

    /********************************
     ** SESSION MANAGEMENT
     ********************************/

    /**
     * @param username : The username
     * @param password : The password
     */
    @Override
    public void autologin(String username, String password) {
        // load the user
        UserDetails userDetails = loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities());

        // Attempts to authenticate the passed Authentication object
        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Authenticated the user if possible
        if (auth.isAuthenticated())
            SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * @param request  : provide request information for HTTP servlets
     * @param response : provide HTTP-specific functionality in sending
     */
    @Override
    public void autologout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Performs a logout
        if (auth != null)
            new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    /**
     * @param username : The username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = uRepo.findByEmail(username);
        if (u == null) {
            throw new UsernameNotFoundException(username);
        }

        return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), u.getRoles());
    }


    public List<User> search(String query) {
        return uRepo.search(query);
    }


    /********************************
     ** PASSWORD MANAGEMENT
     ********************************/
    
     
     public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        User customer = uRepo.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            uRepo.save(customer);
        } else {
            throw new UsernameNotFoundException("Could not find any customer with the email " + email);
        }
    }
     
    public User getByResetPasswordToken(String token) {
        return uRepo.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);
         
        customer.setResetPasswordToken(null);
        uRepo.save(customer);
    }
}
