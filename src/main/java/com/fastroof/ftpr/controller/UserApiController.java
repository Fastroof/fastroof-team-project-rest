package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.*;
import com.fastroof.ftpr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class UserApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleChangeRequestRepository roleChangeRequestRepository;
    @Autowired
    private DataFileRepository dataFileRepository;
    @Autowired
    private DataSetRepository dataSetRepository;
    @Autowired
    private AddDataFileRequestRepository addDataFileRequestRepository;
    @Autowired
    private EditDataFileRequestRepository editDataFileRequestRepository;
    @Autowired
    private TagRepository tagRepository;

    @RequestMapping("/user/requestModeratorRole")
    public String requestModeratorRole() {
        if (!tokenHasUserRole()) {
            return null;
        }
        User user = getUserByToken();

        //Перевірка чи є в цього користувача вже створений, але не опрацьований запит
        if (roleChangeRequestRepository.findByUserIdAndStatus(user.getId(),1) != null){
            return "Ви вже створили запит, зачекайте поки його опрацюють модератори";
        }

        //Перевірка чи є в цього користувача відхилений сьогодні запит
        if (roleChangeRequestRepository.findByUserIdAndStatusAndProcessedAt(user.getId(),3, LocalDate.now()) != null) {
            return "За останні 24 години ваш запит був відхилений, зачекайте перед створенням нового.";
        }

        RoleChangeRequest roleChangeRequest = new RoleChangeRequest();
        roleChangeRequest.setStatus(1);
        roleChangeRequest.setUserId(user.getId());

        roleChangeRequestRepository.save(roleChangeRequest);
        return "Запит створено, id запиту : " + roleChangeRequest.getId();
    }

    @RequestMapping("/user/createEditDataFileRequest")
    public String createEditDataFileRequest(@RequestParam("id") Integer dataFileId, @RequestParam("link") String linkToFile,
                                            @RequestParam("name") String name) {
        if (!tokenHasUserRole()) {
            return null;
        }
        User user = getUserByToken();
        Optional<DataFile> dataFileOptional = dataFileRepository.findById(dataFileId);
        if (dataFileOptional.isPresent()) {
            DataFile dataFile = dataFileOptional.get();
            EditDataFileRequest editDataFileRequest = new EditDataFileRequest();
            editDataFileRequest.setStatus(1);
            editDataFileRequest.setUserId(user.getId());
            editDataFileRequest.setDataFileId(dataFileId);
            editDataFileRequest.setUpdatedAt(LocalDate.now());
            editDataFileRequest.setLinkToFile(linkToFile);
            editDataFileRequest.setName(name);

            editDataFileRequestRepository.save(editDataFileRequest);
            return "Edit data file request created. Id: " + editDataFileRequest.getId();
        } else {
            return "Data file with this id does not exist.";
        }
    }

    @RequestMapping("/user/deleteDataFile")
    public String deleteDataFile(@RequestParam("id") Integer dataFileId){
        User user = getUserByToken();
        Optional<DataFile> dataFileOptional = dataFileRepository.findById(dataFileId);
        if (dataFileOptional.isPresent()){
            DataFile dataFile = dataFileOptional.get();
            if (dataFile.getOwnerId().equals(user.getId())){
                dataFileRepository.deleteById(dataFileId);
                return "Data file deleted.";
            } else {
                return "Only owner can delete this data file.";
            }
        } else {
            return "Data file with this id does not exist.";
        }
    }

    @RequestMapping("/user/deleteDataSet")
    public String deleteDataSet(@RequestParam("id") Integer dataSetId){
        User user = getUserByToken();
        Optional<DataSet> dataSetOptional = dataSetRepository.findById(dataSetId);
        if (dataSetOptional.isPresent()){
            DataSet dataSet = dataSetOptional.get();
            if (dataSet.getOwnerId().equals(user.getId())){
                List<DataFile> dataSetFiles = dataFileRepository.findAllByDataSetId(dataSetId);
                for (DataFile dataFile : dataSetFiles){
                    dataFileRepository.delete(dataFile);
                }
                dataSetRepository.deleteById(dataSetId);
                return "Data set deleted.";
            } else {
                return "Only owner can delete this data set.";
            }
        } else {
            return "Data set with this id does not exist.";
        }
    }

    @RequestMapping("/searchByName")
    public List<DataSet> searchByName(@RequestParam("query") String query){
        return dataSetRepository.getDataSetsByNameContains(query);
    }

    @RequestMapping("/searchByTag")
    public List<DataSet> searchByTag(@RequestParam("id") Integer tagId){
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.isPresent()) {
            return dataSetRepository.findAllByTagId(Math.toIntExact(tagId));
        } else {
            return null;
        }
    }

    private User getUserByToken(){
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    private boolean tokenHasUserRole(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString().equals("ROLE_USER");
    }

}
