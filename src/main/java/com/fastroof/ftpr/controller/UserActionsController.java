package com.fastroof.ftpr.controller;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.fastroof.ftpr.entity.*;
import com.fastroof.ftpr.pojo.DataSetForIndex;
import com.fastroof.ftpr.pojo.DatasetFormData;
import com.fastroof.ftpr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Controller
public class UserActionsController {

    @Autowired
    RoleChangeRequestRepository roleChangeRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private DataFileRepository dataFileRepository;
    @Autowired
    private DataSetRepository dataSetRepository;
    @Autowired
    private AddDataFileRequestRepository addDataFileRequestRepository;

    private static final String ACCESS_TOKEN = "sl.BIURThpa3NzDEARSlOwS9PRrRTr_k_4ErKYSBe5RyVKKmK1L7qnNbr9BXGGvXCfzWRqkWGtXyvI6IdAOj7bcJoEtKU-DOlAXF29slTsgif-suIayULDzEHJY65rndPz2_kyYnrjV";
    private static final DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/ftpr").build();
    private static final DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

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

    @GetMapping("/my/datasets")
    public String showMyDatasets(ModelMap model, Authentication authentication) {
        int userId = userRepository.findByEmail(authentication.getName()).getId();
        List<DataSetForIndex> myDataSets = new ArrayList<>();
        dataSetRepository.findAll().forEach(dataSet -> {
            if (dataSet.getOwnerId() == userId) {
                String tagName = "-";
                if (dataSet.getTagId() != null) {
                    Optional<Tag> tagOptional = tagRepository.findById(dataSet.getTagId());
                    if (tagOptional.isPresent()) {
                        tagName = tagOptional.get().getName();
                    }
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

    @GetMapping("/new/dataset")
    public String showNewDatasetPage(ModelMap model) {
        List<String> tagsNames = new ArrayList<>();
        tagRepository.findAll().forEach(tag -> tagsNames.add(tag.getName()));
        model.addAttribute("tagsNames", tagsNames);
        return "new/dataset";
    }

    @PostMapping("/new/dataset")
    public String createNewDataSet(ModelMap model, @ModelAttribute("datasetFormData") DatasetFormData datasetFormData, Authentication authentication) {

        LocalDate now = LocalDate.now();
        int ownerId = userRepository.findByEmail(authentication.getName()).getId();
        String datasetName = datasetFormData.getName();
        String tagName = datasetFormData.getTagName();

        // new dataset
        DataSet dataSet = new DataSet();
        dataSet.setUpdatedAt(now);
        dataSet.setCreatedAt(now);
        if (!"-".equals(tagName)) {
            dataSet.setTagId(tagRepository.findByName(tagName).getId());
        }
        dataSet.setName(datasetName);
        dataSet.setOwnerId(ownerId);
        dataSetRepository.save(dataSet);

        // add files
        MultipartFile[] files = datasetFormData.getFileIn();
        for (MultipartFile file : files) {
            String link = "error";
            String fileName = file.getOriginalFilename();
            int dataSetId = dataSet.getId();

            try (InputStream in = file.getInputStream()) {
                String path = "/"+ownerId+"/"+dataSetId+"/"+fileName;
                client.files().uploadBuilder(path).uploadAndFinish(in);
                SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(path);
                link = sharedLinkMetadata.getUrl().replaceAll("dl=0$","raw=1");
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }

            AddDataFileRequest addDataFileRequest = new AddDataFileRequest();
            addDataFileRequest.setCreatedAt(now);
            addDataFileRequest.setLinkToFile(link);
            addDataFileRequest.setName(fileName);
            addDataFileRequest.setUserId(ownerId);
            addDataFileRequest.setStatus(1);
            addDataFileRequest.setDataSetId(dataSetId);

            addDataFileRequestRepository.save(addDataFileRequest);
        }

        // success
        model.addAttribute("messnum", 2);
        model.addAttribute("msg", "Датасет створено. Файли будуть додані після перевірки модератором");
        model.addAttribute("link", "/");
        model.addAttribute("text", "Натисніть, щоб перейти на головну ➜");
        return "info";
    }
}
