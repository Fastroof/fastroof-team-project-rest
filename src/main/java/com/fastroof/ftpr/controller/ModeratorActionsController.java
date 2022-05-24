package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.RoleChangeRequest;
import com.fastroof.ftpr.entity.Tag;
import com.fastroof.ftpr.entity.User;
import com.fastroof.ftpr.pojo.DataSetForIndex;
import com.fastroof.ftpr.pojo.RoleChangeRequestForCheck;
import com.fastroof.ftpr.repository.RoleChangeRequestRepository;
import com.fastroof.ftpr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Controller
public class ModeratorActionsController {

    @Autowired
    RoleChangeRequestRepository roleChangeRequestRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/auth/check")
    public String getUnprocessedRoleChangeRequests(ModelMap model, Authentication authentication){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        List<RoleChangeRequestForCheck> roleChangeRequestsForCheck = new ArrayList<>();
        roleChangeRequestRepository.findAllByStatus(1).forEach(roleChangeRequest -> {
                    int roleChangeRequestId = roleChangeRequest.getId();

                    Optional<User> ownerOptional = userRepository.findById(roleChangeRequest.getUserId());
                    String ownerName = null;
                    if (ownerOptional.isPresent()) {
                        ownerName = ownerOptional.get().getFirstName() + " " + ownerOptional.get().getLastName();
                    }
            roleChangeRequestsForCheck.add(new RoleChangeRequestForCheck(roleChangeRequestId, ownerName));
        });

        model.addAttribute("requests", roleChangeRequestsForCheck);
        return "auth/check";

    }
    @GetMapping("/auth/approve")
    public String approveRoleChangeRequest(ModelMap model, Authentication authentication, @RequestParam("id") Integer requestId){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        Optional<RoleChangeRequest> roleChangeRequestOptional = roleChangeRequestRepository.findById(requestId);
        if (roleChangeRequestOptional.isPresent()) {
            RoleChangeRequest roleChangeRequest = roleChangeRequestOptional.get();
            roleChangeRequest.setModeratorId(moderator.getId());
            roleChangeRequest.setProcessedAt(LocalDate.now());
            roleChangeRequest.setStatus(2);
            roleChangeRequestRepository.save(roleChangeRequest);
            User user = userRepository.findById(roleChangeRequest.getUserId()).get();
            user.setRole(2);
            userRepository.save(user);
            model.addAttribute("messnum", 2);
            model.addAttribute("msg", "Запит користувача " + user.getFirstName() + " " + user.getLastName() +
                      " на перехід прийнято");
            model.addAttribute("link", "/auth/check");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
            model.addAttribute("link", "/auth/check");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }
    }

    @GetMapping("/auth/decline")
    public String declineRoleChangeRequest(ModelMap model, Authentication authentication, @RequestParam("id") Integer requestId){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        Optional<RoleChangeRequest> roleChangeRequestOptional = roleChangeRequestRepository.findById(requestId);
        if (roleChangeRequestOptional.isPresent()) {
            RoleChangeRequest roleChangeRequest = roleChangeRequestOptional.get();
            roleChangeRequest.setModeratorId(moderator.getId());
            roleChangeRequest.setProcessedAt(LocalDate.now());
            roleChangeRequest.setStatus(3);
            roleChangeRequestRepository.save(roleChangeRequest);
            User user = userRepository.findById(roleChangeRequest.getUserId()).get();
            model.addAttribute("messnum", 2);
            model.addAttribute("msg", "Запит користувача " + user.getFirstName() + " " + user.getLastName() +
                    " на перехід відхилено");
            model.addAttribute("link", "/auth/check");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
            model.addAttribute("link", "/auth/check");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }
    }


}
