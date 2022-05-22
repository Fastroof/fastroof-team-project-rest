package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.RoleChangeRequest;
import com.fastroof.ftpr.entity.User;
import com.fastroof.ftpr.repository.RoleChangeRequestRepository;
import com.fastroof.ftpr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleChangeRequestRepository roleChangeRequestRepository;

    @RequestMapping("/user/requestModeratorRole")
    public String requestModeratorRole() {
        if (tokenHasUserRole()){
            User user = getUserByToken();
            RoleChangeRequest roleChangeRequest = new RoleChangeRequest();
            roleChangeRequest.setStatus(1);
            roleChangeRequest.setUserId(user.getId());

            roleChangeRequestRepository.save(roleChangeRequest);
            return "Success";
        } else {
            return "You are not a user!";
        }
    }

    private User getUserByToken(){
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    private boolean tokenHasUserRole(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_USER");
    }

}
