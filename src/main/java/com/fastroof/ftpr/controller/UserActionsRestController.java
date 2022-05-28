package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.DataSet;
import com.fastroof.ftpr.entity.Tag;
import com.fastroof.ftpr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RequestMapping("/dataset")
@RestController
public class UserActionsRestController {

    @Autowired
    private RoleChangeRequestRepository roleChangeRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private DataFileRepository dataFileRepository;
    @Autowired
    private DataSetRepository dataSetRepository;
    @Autowired
    private AddDataFileRequestRepository addDataFileRequestRepository;

    @GetMapping("/show/{id}")
    public ModelAndView showDataSet(@PathVariable("id") int id) {
        Optional<DataSet> dataSetOptional = dataSetRepository.findById(id);
        ModelAndView mav;
        if (dataSetOptional.isPresent()) {
            mav = new ModelAndView("dataset");
            DataSet dataset = dataSetOptional.get();
            mav.addObject("datasetName", dataset.getName());
            Optional<Tag> tagOptional = tagRepository.findById(dataset.getTagId());
            String tagName = "-";
            if (tagOptional.isPresent()) {
                tagName = tagOptional.get().getName();
            }
            mav.addObject("tagName", tagName);
            mav.addObject("files", dataFileRepository.findAllByDataSetId(dataset.getId()));
        } else {
            mav = new ModelAndView("info");
            mav.addObject("messnum", 1);
            mav.addObject("msg", "Трапилася помилка");
            mav.addObject("link", "/");
            mav.addObject("text", "Натисніть, щоб вийти ➜");
        }
        return mav;
    }

//    @GetMapping("/delete/{id}")
//    public ModelAndView deleteDataSet(@PathVariable("id") int id, Authentication authentication) {
//        int userId = userRepository.findByEmail(authentication.getName()).getId();
//        Optional<DataSet> dataSetOptional = dataSetRepository.findById(id);
//    }
}
