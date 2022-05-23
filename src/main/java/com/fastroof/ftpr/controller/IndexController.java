package com.fastroof.ftpr.controller;

import com.fastroof.ftpr.entity.Tag;
import com.fastroof.ftpr.entity.User;
import com.fastroof.ftpr.pojo.DataSetForIndex;
import com.fastroof.ftpr.repository.DataFileRepository;
import com.fastroof.ftpr.repository.DataSetRepository;
import com.fastroof.ftpr.repository.TagRepository;
import com.fastroof.ftpr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private DataFileRepository dataFileRepository;

    @Autowired
    private DataSetRepository dataSetRepository;

    @GetMapping("/")
    public String showIndexPage(ModelMap model) {
        List<DataSetForIndex> dataSetsForIndex = new ArrayList<>();
        dataSetRepository.findAll().forEach(dataSet -> {
            Optional<Tag> tagOptional = tagRepository.findById(dataSet.getTagId());
            String tagName = null;
            if (tagOptional.isPresent()) {
                tagName = tagOptional.get().getName();
            }

            Optional<User> ownerOptional = userRepository.findById(dataSet.getOwnerId());
            String ownerName = null;
            if (ownerOptional.isPresent()) {
                ownerName = ownerOptional.get().getFirstName() + " " + ownerOptional.get().getLastName();
            }

            final int[] fileCount = {0};
            int dataSetId = dataSet.getId();
            dataFileRepository.findAll().forEach(dataFile -> {
                if (dataFile.getDataSetId() == dataSetId) {
                    fileCount[0]++;
                }
            });

            dataSetsForIndex.add(new DataSetForIndex(dataSetId, dataSet.getName(), tagName, ownerName, fileCount[0]));
        });
        Collections.reverse(dataSetsForIndex);
        model.addAttribute("dataSets", dataSetsForIndex);
        return "index";
    }

    @GetMapping("/my/datasets")
    public String showMyDatasets(ModelMap model, Authentication authentication) {
        int userId = userRepository.findByEmail(authentication.getName()).getId();
        List<DataSetForIndex> myDataSets = new ArrayList<>();
        dataSetRepository.findAll().forEach(dataSet -> {
            if (dataSet.getOwnerId() == userId) {
                Optional<Tag> tagOptional = tagRepository.findById(dataSet.getTagId());
                String tagName = null;
                if (tagOptional.isPresent()) {
                    tagName = tagOptional.get().getName();
                }

                final int[] fileCount = {0};
                int dataSetId = dataSet.getId();
                dataFileRepository.findAll().forEach(dataFile -> {
                    if (dataFile.getDataSetId() == dataSetId) {
                        fileCount[0]++;
                    }
                });

                myDataSets.add(new DataSetForIndex(dataSetId, dataSet.getName(), tagName, "", fileCount[0]));
            }
        });
        Collections.reverse(myDataSets);
        model.addAttribute("dataSets", myDataSets);
        return "my/datasets";
    }
}
