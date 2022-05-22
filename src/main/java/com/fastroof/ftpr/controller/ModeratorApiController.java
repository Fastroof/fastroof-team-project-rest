package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.RoleChangeRequest;
import com.fastroof.ftpr.entity.User;
import com.fastroof.ftpr.repository.AddDataFileRequestRepository;
import com.fastroof.ftpr.repository.EditDataFileRequestRepository;
import com.fastroof.ftpr.repository.RoleChangeRequestRepository;
import com.fastroof.ftpr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ModeratorApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleChangeRequestRepository roleChangeRequestRepository;
    @Autowired
    private AddDataFileRequestRepository addDataFileRequestRepository;
    @Autowired
    private EditDataFileRequestRepository editDataFileRequestRepository;

    @RequestMapping("/moderator/getUnprocessedRoleChangeRequests")
    public List<RoleChangeRequest> getUnprocessedRoleChangeRequests() {
        if (tokenHasModeratorRole()){
            return roleChangeRequestRepository.findAllByStatus(1);
        } else {
            return null;
        }
    }

    @RequestMapping("/moderator/approveRoleChangeRequest")
    public String approveRoleChangeRequest(@RequestParam("id") String requestId){
        if (tokenHasModeratorRole()){
            User moderator = getUserByToken();
            Optional<RoleChangeRequest> roleChangeRequest = roleChangeRequestRepository.findById(Integer.valueOf(requestId));
            if (roleChangeRequest.isPresent()) {
                roleChangeRequest.get().setModeratorId(moderator.getId());
                roleChangeRequest.get().setProcessedAt(LocalDate.now());
                roleChangeRequest.get().setStatus(2);
                roleChangeRequestRepository.save(roleChangeRequest.get());
                User user = userRepository.findById(roleChangeRequest.get().getUserId()).get();
                user.setRole(2);
                userRepository.save(user);
                return "Role change request approved.";
            } else {
                return "Role change request with this id does not exist.";
            }
        } else {
            return null;
        }
    }

    @RequestMapping("/moderator/declineRoleChangeRequest")
    public String declineRoleChangeRequest(@RequestParam("id") String requestId){
        if (tokenHasModeratorRole()){
            User moderator = getUserByToken();
            Optional<RoleChangeRequest> roleChangeRequest = roleChangeRequestRepository.findById(Integer.valueOf(requestId));
            if (roleChangeRequest.isPresent()) {
                roleChangeRequest.get().setModeratorId(moderator.getId());
                roleChangeRequest.get().setProcessedAt(LocalDate.now());
                roleChangeRequest.get().setStatus(3);
                roleChangeRequestRepository.save(roleChangeRequest.get());
                return "Role change request declined.";
            } else {
                return "Role change request with this id does not exist.";
            }
        } else {
            return null;
        }
    }

    private User getUserByToken(){
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    private boolean tokenHasModeratorRole(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_MODERATOR");
    }

}
