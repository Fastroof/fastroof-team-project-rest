package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.RoleChangeRequest;
import com.fastroof.ftpr.entity.User;
import com.fastroof.ftpr.repository.RoleChangeRequestRepository;
import com.fastroof.ftpr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.extras.springsecurity5.auth.Authorization;

import java.time.LocalDate;

@Controller
public class UserActionsController {

    @Autowired
    RoleChangeRequestRepository roleChangeRequestRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/to/moderator")
    public String createRoleChangeRequest(ModelMap model, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());

        //Якщо користувач вже модератор - відмовляємо
        //TODO: Зробити універсальну альтернативу для таких випадків(коли користувач перейшов до end-point-у модератора, або навпаки)
        if (user.getRole()==2){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Ви вже модератор!");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        //Перевірка чи є в цього користувача вже створений, але не опрацьований запит
        if (roleChangeRequestRepository.findByUserIdAndStatus(user.getId(),1) != null){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Ви вже створили запит, зачекайте поки його опрацюють модератори.");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        //Перевірка чи є в цього користувача відхилений сьогодні запит
        if (roleChangeRequestRepository.findByUserIdAndStatusAndProcessedAt(user.getId(),3, LocalDate.now()) != null){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "За останні 24 години ваш запит був відхилений, зачекайте перед створенням нового.");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        //Додавання запиту до БД

        RoleChangeRequest roleChangeRequest = new RoleChangeRequest();
        roleChangeRequest.setUserId(user.getId());
        roleChangeRequest.setStatus(1);

        roleChangeRequestRepository.save(roleChangeRequest);

        model.addAttribute("messnum", 2);
        model.addAttribute("msg", "Запит створено, чекайте поки його опрацюють модератори.");
        model.addAttribute("link", "/");
        model.addAttribute("text", "Натисніть, щоб продовжити ➜");
        return "info";
    }


}
