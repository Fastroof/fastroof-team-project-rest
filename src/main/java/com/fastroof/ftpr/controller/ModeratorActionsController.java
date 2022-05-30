package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.*;
import com.fastroof.ftpr.pojo.AddFileRequestForCheck;
import com.fastroof.ftpr.pojo.DataSetForIndex;
import com.fastroof.ftpr.pojo.EditFileRequestForCheck;
import com.fastroof.ftpr.pojo.RoleChangeRequestForCheck;
import com.fastroof.ftpr.repository.*;
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
    private RoleChangeRequestRepository roleChangeRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddDataFileRequestRepository addDataFileRequestRepository;

    @Autowired
    private EditDataFileRequestRepository editDataFileRequestRepository;

    @Autowired
    private DataSetRepository dataSetRepository;

    @Autowired
    private DataFileRepository dataFileRepository;

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
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
        }
        model.addAttribute("link", "/auth/check");
        model.addAttribute("text", "Натисніть, щоб продовжити ➜");
        return "info";
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
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
        }
        model.addAttribute("link", "/auth/check");
        model.addAttribute("text", "Натисніть, щоб продовжити ➜");
        return "info";
    }

    @GetMapping("/data/check")
    public String getUnprocessedDataRequests(ModelMap model, Authentication authentication){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        List<AddFileRequestForCheck> addFileRequestsForCheck = new ArrayList<>();
        addDataFileRequestRepository.findAllByStatus(1).forEach(addFileRequest -> {
            int addFileRequestId = addFileRequest.getId();

            Optional<User> ownerOptional = userRepository.findById(addFileRequest.getUserId());
            String ownerName = null;
            if (ownerOptional.isPresent()) {
                ownerName = ownerOptional.get().getFirstName() + " " + ownerOptional.get().getLastName();
            }
            addFileRequestsForCheck.add(new AddFileRequestForCheck(addFileRequestId, ownerName, addFileRequest.getLinkToFile(), addFileRequest.getName()));
        });

        List<EditFileRequestForCheck> editFileRequestsForCheck = new ArrayList<>();
        editDataFileRequestRepository.findAllByStatus(1).forEach(editFileRequest -> {
            int editFileRequestId = editFileRequest.getId();

            Optional<User> ownerOptional = userRepository.findById(editFileRequest.getUserId());
            String ownerName = null;
            if (ownerOptional.isPresent()) {
                ownerName = ownerOptional.get().getFirstName() + " " + ownerOptional.get().getLastName();
            }
            editFileRequestsForCheck.add(new EditFileRequestForCheck(editFileRequestId, ownerName, editFileRequest.getLinkToFile(), editFileRequest.getName()));
        });

        model.addAttribute("editRequests", editFileRequestsForCheck);

        model.addAttribute("addRequests", addFileRequestsForCheck);
        return "data/check";

    }

    @GetMapping("/data/approve_add")
    public String approveAddFileRequest(ModelMap model, Authentication authentication, @RequestParam("id") Integer requestId){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        Optional<AddDataFileRequest> addDataFileRequestOptional = addDataFileRequestRepository.findById(requestId);
        if (addDataFileRequestOptional.isPresent()) {
            AddDataFileRequest addDataFileRequest = addDataFileRequestOptional.get();
            addDataFileRequest.setStatus(2);
            addDataFileRequest.setModeratorId(moderator.getId());

            DataFile dataFile = new DataFile();
            dataFile.setName(addDataFileRequest.getName());
            dataFile.setCreatedAt(LocalDate.now());
            dataFile.setUpdatedAt(LocalDate.now());
            dataFile.setLinkToFile(addDataFileRequest.getLinkToFile());
            dataFile.setOwnerId(addDataFileRequest.getUserId());
            dataFile.setDataSetId(addDataFileRequest.getDataSetId());

            DataSet dataSet = dataSetRepository.findById(addDataFileRequest.getDataSetId()).get();
            dataSet.setUpdatedAt(LocalDate.now());

            dataFileRepository.save(dataFile);
            addDataFileRequestRepository.save(addDataFileRequest);
            dataSetRepository.save(dataSet);
            User user = userRepository.findById(addDataFileRequest.getUserId()).get();
            model.addAttribute("messnum", 2);
            model.addAttribute("msg", "Запит користувача " + user.getFirstName() + " " + user.getLastName() +
                    " на додавання даних прийнято");
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
        }
        model.addAttribute("link", "/data/check");
        model.addAttribute("text", "Натисніть, щоб продовжити ➜");
        return "info";
    }

    @GetMapping("/data/decline_add")
    public String declineAddFileRequest(ModelMap model, Authentication authentication, @RequestParam("id") Integer requestId){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        Optional<AddDataFileRequest> addDataFileRequestOptional = addDataFileRequestRepository.findById(requestId);
        if (addDataFileRequestOptional.isPresent()) {
            AddDataFileRequest addDataFileRequest = addDataFileRequestOptional.get();
            addDataFileRequest.setModeratorId(moderator.getId());
            addDataFileRequest.setStatus(3);
            addDataFileRequestRepository.save(addDataFileRequest);
            User user = userRepository.findById(addDataFileRequest.getUserId()).get();
            model.addAttribute("messnum", 2);
            model.addAttribute("msg", "Запит користувача " + user.getFirstName() + " " + user.getLastName() +
                    " на додавання даних відхилено");
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
        }
        model.addAttribute("link", "/data/check");
        model.addAttribute("text", "Натисніть, щоб продовжити ➜");
        return "info";
    }

    @GetMapping("/data/approve_edit")
    public String approveEditFileRequest(ModelMap model, Authentication authentication, @RequestParam("id") Integer requestId){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        Optional<EditDataFileRequest> editDataFileRequestOptional = editDataFileRequestRepository.findById(requestId);
        if (editDataFileRequestOptional.isPresent()) {
            EditDataFileRequest editDataFileRequest = editDataFileRequestOptional.get();
            editDataFileRequest.setStatus(2);
            editDataFileRequest.setModeratorId(moderator.getId());

            DataFile dataFile = dataFileRepository.findById(editDataFileRequest.getDataFileId()).get();
            dataFile.setUpdatedAt(LocalDate.now());
            dataFile.setLinkToFile(editDataFileRequest.getLinkToFile());
            dataFile.setName(editDataFileRequest.getName());

            DataSet dataSet = dataSetRepository.findById(dataFile.getDataSetId()).get();
            dataSet.setUpdatedAt(LocalDate.now());

            editDataFileRequestRepository.save(editDataFileRequest);
            dataFileRepository.save(dataFile);
            dataSetRepository.save(dataSet);
            User user = userRepository.findById(editDataFileRequest.getUserId()).get();
            model.addAttribute("messnum", 2);
            model.addAttribute("msg", "Запит користувача " + user.getFirstName() + " " + user.getLastName() +
                    " на редагування даних прийнято");
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
        }
        model.addAttribute("link", "/data/check");
        model.addAttribute("text", "Натисніть, щоб продовжити ➜");
        return "info";
    }

    @GetMapping("/data/decline_edit")
    public String declineEditFileRequest(ModelMap model, Authentication authentication, @RequestParam("id") Integer requestId){
        User moderator = userRepository.findByEmail(authentication.getName());


        //Перевірка чи користувач є модератором
        if (moderator.getRole()==1){
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "На жаль ви не модератор");
            model.addAttribute("link", "/");
            model.addAttribute("text", "Натисніть, щоб продовжити ➜");
            return "info";
        }

        Optional<EditDataFileRequest> editDataFileRequestOptional = editDataFileRequestRepository.findById(requestId);
        if (editDataFileRequestOptional.isPresent()) {
            EditDataFileRequest editDataFileRequest = editDataFileRequestOptional.get();
            editDataFileRequest.setModeratorId(moderator.getId());
            editDataFileRequest.setStatus(3);
            editDataFileRequestRepository.save(editDataFileRequest);
            User user = userRepository.findById(editDataFileRequest.getUserId()).get();
            model.addAttribute("messnum", 2);
            model.addAttribute("msg", "Запит користувача " + user.getFirstName() + " " + user.getLastName() +
                    " на редагування даних відхилено");
        } else {
            model.addAttribute("messnum", 1);
            model.addAttribute("msg", "Запит з id=" + requestId + " не існує");
        }
        model.addAttribute("link", "/data/check");
        model.addAttribute("text", "Натисніть, щоб продовжити ➜");
        return "info";
    }

}
