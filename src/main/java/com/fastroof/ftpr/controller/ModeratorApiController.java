package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.*;
import com.fastroof.ftpr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
    @Autowired
    private DataSetRepository dataSetRepository;
    @Autowired
    private DataFileRepository dataFileRepository;

    @RequestMapping("/moderator/UnprocessedRoleChangeRequests")
    public List<RoleChangeRequest> getUnprocessedRoleChangeRequests() {
        if (tokenHasModeratorRole()){
            return roleChangeRequestRepository.findAllByStatus(1);
        } else {
            return null;
        }
    }

    @RequestMapping("/moderator/approveRoleChangeRequest")
    public String approveRoleChangeRequest(@RequestParam("id") String requestId){
        if (!tokenHasModeratorRole()) {
            return null;
        }
        User moderator = getUserByToken();
        Optional<RoleChangeRequest> roleChangeRequestOptional = roleChangeRequestRepository.findById(Integer.valueOf(requestId));
        if (roleChangeRequestOptional.isPresent()) {
            RoleChangeRequest roleChangeRequest = roleChangeRequestOptional.get();
            roleChangeRequest.setModeratorId(moderator.getId());
            roleChangeRequest.setProcessedAt(LocalDate.now());
            roleChangeRequest.setStatus(2);
            roleChangeRequestRepository.save(roleChangeRequest);
            User user = userRepository.findById(roleChangeRequest.getUserId()).get();
            user.setRole(2);
            userRepository.save(user);
            return "Role change request approved.";
        } else {
            return "Role change request with this id does not exist.";
        }
    }

    @RequestMapping("/moderator/declineRoleChangeRequest")
    public String declineRoleChangeRequest(@RequestParam("id") String requestId){
        if (!tokenHasModeratorRole()) {
            return null;
        }
        User moderator = getUserByToken();
        Optional<RoleChangeRequest> roleChangeRequestOptional = roleChangeRequestRepository.findById(Integer.valueOf(requestId));
        if (roleChangeRequestOptional.isPresent()) {
            RoleChangeRequest roleChangeRequest = roleChangeRequestOptional.get();
            roleChangeRequest.setModeratorId(moderator.getId());
            roleChangeRequest.setProcessedAt(LocalDate.now());
            roleChangeRequest.setStatus(3);
            roleChangeRequestRepository.save(roleChangeRequest);
            return "Role change request declined.";
        } else {
            return "Role change request with this id does not exist.";
        }
    }


    @RequestMapping("/moderator/UnprocessedAddDataFileRequests")
    public List<AddDataFileRequest> getUnprocessedAddDataFileRequests() {
        if (tokenHasModeratorRole()){
            return addDataFileRequestRepository.findAllByStatus(1);
        } else {
            return null;
        }
    }

    @RequestMapping("/moderator/approveAddDataFileRequest")
    public String approveAddDataFileRequest(@RequestParam("id") String requestId){
        if (!tokenHasModeratorRole()) {
            return null;
        }
        User moderator = getUserByToken();
        Optional<AddDataFileRequest> addDataFileRequestOptional = addDataFileRequestRepository.findById(Integer.valueOf(requestId));
        if (addDataFileRequestOptional.isPresent()) {
            AddDataFileRequest addDataFileRequest = addDataFileRequestOptional.get();
            addDataFileRequest.setStatus(2);
            addDataFileRequest.setModeratorId(moderator.getId());

            //Maybe change link_to_file to link_to_files in DB and process it as a list of links
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
            return "Add data file request approved.";
        } else {
            return "Add data file request with this id does not exist.";
        }
    }

    @RequestMapping("/moderator/declineAddDataFileRequest")
    public String declineAddDataFileRequest(@RequestParam("id") String requestId){
        if (!tokenHasModeratorRole()) {
            return null;
        }
        User moderator = getUserByToken();
        Optional<AddDataFileRequest> addDataFileRequestOptional = addDataFileRequestRepository.findById(Integer.valueOf(requestId));
        if (addDataFileRequestOptional.isPresent()) {
            AddDataFileRequest addDataFileRequest = addDataFileRequestOptional.get();
            addDataFileRequest.setModeratorId(moderator.getId());
            addDataFileRequest.setStatus(3);
            addDataFileRequestRepository.save(addDataFileRequest);
            return "Add data file request declined.";
        } else {
            return "Add data file request with this id does not exist.";
        }
    }


    @RequestMapping("/moderator/UnprocessedEditDataFileRequests")
    public List<EditDataFileRequest> getUnprocessedEditDataFileRequests() {
        if (tokenHasModeratorRole()){
            return editDataFileRequestRepository.findAllByStatus(1);
        } else {
            return null;
        }
    }

    @RequestMapping("/moderator/approveEditDataFileRequest")
    public String approveEditDataFileRequest(@RequestParam("id") String requestId){
        if (!tokenHasModeratorRole()) {
            return null;
        }
        User moderator = getUserByToken();
        Optional<EditDataFileRequest> editDataFileRequestOptional = editDataFileRequestRepository.findById(Integer.valueOf(requestId));
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
            return "Edit data file request approved.";
        } else {
            return "Edit data file request with this id does not exist.";
        }
    }

    @RequestMapping("/moderator/declineEditDataFileRequest")
    public String declineEditDataFileRequest(@RequestParam("id") String requestId){
        if (!tokenHasModeratorRole()) {
            return null;
        }
        User moderator = getUserByToken();
        Optional<EditDataFileRequest> editDataFileRequestOptional = editDataFileRequestRepository.findById(Integer.valueOf(requestId));
        if (editDataFileRequestOptional.isPresent()) {
            EditDataFileRequest editDataFileRequest = editDataFileRequestOptional.get();
            editDataFileRequest.setModeratorId(moderator.getId());
            editDataFileRequest.setStatus(3);
            editDataFileRequestRepository.save(editDataFileRequest);
            return "Edit data file request declined.";
        } else {
            return "Edit data file request with this id does not exist.";
        }
    }

    private User getUserByToken(){
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    private boolean tokenHasModeratorRole(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_MODERATOR");
    }

}
