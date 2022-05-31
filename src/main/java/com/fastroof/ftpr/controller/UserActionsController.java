package com.fastroof.ftpr.controller;

import com.box.sdk.*;
import com.box.sdk.sharedlink.BoxSharedLinkRequest;
import com.fastroof.ftpr.entity.*;
import com.fastroof.ftpr.pojo.DataSetForIndex;
import com.fastroof.ftpr.pojo.DatasetFormData;
import com.fastroof.ftpr.pojo.EditDatasetFormData;
import com.fastroof.ftpr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static com.box.sdk.BoxSharedLink.Access.OPEN;

@Controller
public class UserActionsController {

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
    @Autowired
    private EditDataFileRequestRepository editDataFileRequestRepository;

    private static final BoxDeveloperEditionAPIConnection api = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(BoxConfig.readFrom("{\"boxAppSettings\":{\"clientID\":\"5p1dcgaaou4x1da522cwyx29hatwxhy4\",\"clientSecret\":\"ojy2wAvXNIEiIzn7jurEUR1expahtGR4\",\"appAuth\": {\"publicKeyID\": \"j861aa88\",\"privateKey\": \"-----BEGIN ENCRYPTED PRIVATE KEY-----\\nMIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIbKTYCsUB4vgCAggA\\nMBQGCCqGSIb3DQMHBAiQhH8WpEU5iwSCBMh4PtcEtD3MAoE0GA60IC5shzC5rZix\\neLvfijXF0uE4iFHDXisimzbESv8TkUvc+BN5UI/T03LTEmWvgQ38YidVk6E1q2Zr\\nu3TRgJuPFJayDTOpndtPWWDayyz4lSMRndRyLT35QrRwD7L7o1iNdDlrn2AFL7g3\\naBDGNs/RzGrubele6SWGHXXd11dmuqOnvO3Z8Ek15V1TAZioFHXMsqLOTnZ18MP8\\n9ujd7QMDY27K0mchFYUtztcZwnFDdb6Psvcg/cX83seEDI8SpaoeyRDMcvJaIkyJ\\nQvAMKFIM8hEtlcU4i881oSzPoXklMEXODIkAZi48cYYegX+FdQD3+3Qfr4z7HhDE\\n82BlmWtIy9aOBfTA55NQqBarNb9bRRR9grsjWWMDfsaA+PNNp3KrqoneBkm1oaC4\\nBo4r2Mn/J0OqZf3MIzZgiKf7GW37cB3ZIs36Skqpjl+J24shP+HRi9Xz+qpSJYtG\\ndoWanTtl9+kDRojn7rco0wLIRFQVrOVUun7MRoS+1MVc1aylnOzO1uc7v9uiysep\\nLByHHmjmL/4AzY7l3lLB7Zxhg0uGosXanKxqa9mULpsSJopSHwysZLviX/cYCVvs\\ntPu4AObMdr01oDGH98fLdjiqzI/UoNaa4ejNZ0w3SFXjJrl6a/Vy1WONEhZ4o/dU\\nYNiaHoDSUDQPcWoktPmAxMzBK6UppUCWD7l059NQhbTRmI349V3Ajh8f8gtGy3fu\\nbmkGgeNtQ+BLBLicbWTve4yG0r6Q4CSI8Ec12VhkBtQ7Ko/1IIwQ1FrgYBSaKlJZ\\nmAsQSlFT5+2ruq5bpNsb1rTeMoNsSzqbkLXM9e89UBEI3EpyFOOygwSJWMtYVWmy\\nN4DJdyKiDddw5HYqwg7iNt6YGHccwyD2loJC50FAgB+CzNle49uJmZe6nZBzgiRv\\n7T/KZYQNSPVQB6p9K9xMqOh8CbCq+SO+XHCcqkoSDgwOFtFyLV+hxSTEdWNtC5uV\\nA3o+liioX0i3zkjdkpj8++mlAc9xO9o5o9hv/BKiQqfAWodzvvFZbnuu4VLg8zrt\\n0V+32IzF24o37P0NWfiMAXiJMK7C3vewN374nEANtBQX3j0vK4K9inf/YMgWzP76\\nSKqGsFs/NqG5FD/6TuEW1s9nolTfNl4W0tjvRtY2pyc2/7b6lO68kRpZf7KR8iga\\nF2r07j3nXkNFA9mPLSdLDdEHQTfdgdIjkb1CFmBQ2HDD6m+aO/SEaPIOtNACPuff\\nPoTwTMTYg/UvBMSiq1lTM0RyQXA7wXfiBqNKJGd6KQTBO/VYmxZfruWGCkGea2WF\\nektgHrNctglxdVMRmUxp3oLrvkKjQTFT6wHsYGxcJVop6P7FRItOCo/vfJOrsqEp\\nnar3ixRpI1XIRIpVjd0WRlMoKBmzpSXfJFxtp9COdIH1RYkEkhm4vqgmncSS2+5n\\nxKmz5YIoMB5bqprf7WQDG8hCSSHWudB9G1mUOpoCNZiBH2DUXsiLdjUadi72+Zer\\niPRBvgU5iqxcyLtUOHFnq/bj78L0ecyD2Bv0AAPnIc/P5VyHmjGGUCA2Y3lIh8+N\\n5ko+iT4+2K/szzEkNoPuPZ12xM+KxGsiIkPi3+ICtG/YOWVJcrUCfk/ZtI8WFl+3\\ngow=\\n-----END ENCRYPTED PRIVATE KEY-----\\n\",\"passphrase\":\"5d05a1c6644d8e022bdf559600b8efaf\"}},\"enterpriseID\": \"900264880\"}"));

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
        if (files != null) {
            int dataSetId = dataSet.getId();
            // box
            // find owner folder or create
            BoxFolder ownerFolder = findAndGetFolder(BoxFolder.getRootFolder(api), String.valueOf(ownerId));
            // find data set folder or create
            BoxFolder dataSetFolder = findAndGetFolder(ownerFolder, String.valueOf(dataSetId));
            // upload
            for (MultipartFile file : files) {
                uploadFileToBox(ownerId, now, dataSetId, dataSetFolder, file);
            }
        }

        // success
        model.addAttribute("messnum", 2);
        model.addAttribute("msg", "Датасет створено.\n" +
                "Якщо ви завантажили файли, то вони будуть додані після перевірки модератором");
        model.addAttribute("link", "/");
        model.addAttribute("text", "Натисніть, щоб перейти на головну ➜");
        return "info";
    }

    private static BoxFolder findAndGetFolder(BoxFolder parentFolder, String id) {
        String ownerFolderId = "";
        for (BoxItem.Info itemInfo : parentFolder) {
            if (itemInfo.getName().equals(id)) {
                ownerFolderId = itemInfo.getID();
            }
        }
        if ("".equals(ownerFolderId)) {
            BoxFolder.Info ownerFolderInfo = parentFolder.createFolder(id);
            ownerFolderId = ownerFolderInfo.getID();
        }
        return new BoxFolder(api, ownerFolderId);
    }

    @GetMapping("/show/dataset/{id}")
    @ResponseBody
    public ModelAndView showDataSet(@PathVariable("id") int id) {
        Optional<DataSet> dataSetOptional = dataSetRepository.findById(id);
        ModelAndView mav;
        if (dataSetOptional.isPresent()) {
            mav = new ModelAndView("dataset");
            DataSet dataset = dataSetOptional.get();
            mav.addObject("datasetName", dataset.getName());
            String tagName = "-";
            if (dataset.getTagId() != null) {
                Optional<Tag> tagOptional = tagRepository.findById(dataset.getTagId());
                if (tagOptional.isPresent()) {
                    tagName = tagOptional.get().getName();
                }
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

    @GetMapping("/delete/dataset/{id}")
    @ResponseBody
    public ModelAndView deleteDataSet(@PathVariable("id") int id, Authentication authentication) {
        int userId = userRepository.findByEmail(authentication.getName()).getId();
        Optional<DataSet> dataSetOptional = dataSetRepository.findById(id);
        ModelAndView mav = new ModelAndView("info");
        if (dataSetOptional.isPresent()) {
            DataSet dataSet = dataSetOptional.get();
            if (userId == dataSet.getOwnerId()) {
                int dataSetId = dataSet.getId();
                if (addDataFileRequestRepository.existsByDataSetId(dataSetId)) {
                    addDataFileRequestRepository.deleteAllByDataSetId(dataSetId);
                }
                dataFileRepository.findAllByDataSetId(dataSetId).forEach(dataFile -> {
                    if (editDataFileRequestRepository.existsByDataFileId(dataFile.getId())) {
                        editDataFileRequestRepository.deleteAllByDataFileId(dataFile.getId());
                    }
                });
                if (dataFileRepository.existsByDataSetId(dataSetId)) {
                    dataFileRepository.deleteAllByDataSetId(dataSetId);
                }
                dataSetRepository.delete(dataSet);

                // box
                // find owner folder
                BoxFolder ownerFolder = findAndGetFolder(BoxFolder.getRootFolder(api), String.valueOf(userId));
                // find data set folder
                BoxFolder dataSetFolder = findAndGetFolder(ownerFolder, String.valueOf(dataSetId));
                dataSetFolder.delete(true);

                mav.addObject("messnum", 2);
                mav.addObject("msg", "Датасет та всі пов'язані файли успішно видалений");
                mav.addObject("link", "/my/datasets");
            } else {
                mav.addObject("messnum", 1);
                mav.addObject("msg", "Недостатньо прав");
                mav.addObject("link", "/");
            }
        } else {
            mav.addObject("messnum", 1);
            mav.addObject("msg", "Трапилася помилка");
            mav.addObject("link", "/");
        }
        mav.addObject("text", "Натисніть, щоб вийти ➜");
        return mav;
    }

    @GetMapping("/edit/dataset/{id}")
    @ResponseBody
    public ModelAndView editDataSet(@PathVariable("id") int id, Authentication authentication) {
        int userId = userRepository.findByEmail(authentication.getName()).getId();
        Optional<DataSet> dataSetOptional = dataSetRepository.findById(id);
        ModelAndView mav;
        if (dataSetOptional.isPresent()) {
            DataSet dataSet = dataSetOptional.get();

            mav = isAllChecked(dataSet);
            if (mav != null) return mav;

            if (userId == dataSet.getOwnerId()) {
                mav = new ModelAndView("edit/dataset");
                mav.addObject("dataSet", dataSet);
                String tagName = "-";
                if (dataSet.getTagId() != null) {
                    Optional<Tag> tagOptional = tagRepository.findById(dataSet.getTagId());
                    if (tagOptional.isPresent()) {
                        tagName = tagOptional.get().getName();
                    }
                }
                mav.addObject("selectedTag", tagName);
                List<String> tagsNames = new ArrayList<>();
                tagRepository.findAll().forEach(tag -> tagsNames.add(tag.getName()));
                mav.addObject("tagsNames", tagsNames);
                mav.addObject("files", dataFileRepository.findAllByDataSetId(dataSet.getId()));
            } else {
                mav = new ModelAndView("info");
                mav.addObject("messnum", 1);
                mav.addObject("msg", "Недостатньо прав");
                mav.addObject("link", "/");
                mav.addObject("text", "Натисніть, щоб вийти ➜");
            }
        } else {
            mav = new ModelAndView("info");
            mav.addObject("messnum", 1);
            mav.addObject("msg", "Трапилася помилка");
            mav.addObject("link", "/");
            mav.addObject("text", "Натисніть, щоб вийти ➜");
        }
        return mav;
    }

    @PostMapping("/edit/dataset/{id}")
    @ResponseBody
    public ModelAndView editDataSetPost(@PathVariable("id") int id, @ModelAttribute("editDatasetFormData") EditDatasetFormData editDatasetFormData, Authentication authentication) {
        int userId = userRepository.findByEmail(authentication.getName()).getId();
        Optional<DataSet> dataSetOptional = dataSetRepository.findById(id);
        ModelAndView mav;
        if (dataSetOptional.isPresent()) {
            DataSet dataSet = dataSetOptional.get();
            if (userId == dataSet.getOwnerId()) {

                mav = isAllChecked(dataSet);
                if (mav != null) return mav;

                LocalDate now = LocalDate.now();
                String datasetName = editDatasetFormData.getName();
                String tagName = editDatasetFormData.getTagName();

                // edit or add files
                int dataSetId = dataSet.getId();
                // box
                // find owner folder
                BoxFolder ownerFolder = findAndGetFolder(BoxFolder.getRootFolder(api), String.valueOf(userId));
                // find data set folder
                BoxFolder dataSetFolder = findAndGetFolder(ownerFolder, String.valueOf(dataSetId));
                // create edit folder or find
                BoxFolder dataSetEditFolder = findAndGetFolder(ownerFolder, dataSetId +"_edit");

                List<MultipartFile> files = new ArrayList<>();
                List<String> newFilesNames = new ArrayList<>();
                if (editDatasetFormData.getFileIn() != null) {
                    files = List.of(editDatasetFormData.getFileIn());
                    for (MultipartFile file: files) {
                        newFilesNames.add(file.getOriginalFilename());
                    }
                }

                try {
                    List<String> oldFilesNames = new ArrayList<>();
                    if (editDatasetFormData.getOldFileName() != null) {
                        oldFilesNames = List.of(editDatasetFormData.getOldFileName());
                    }

                    for (BoxItem.Info itemInfo : dataSetFolder) {
                        if (itemInfo instanceof BoxFile.Info fileInfo) {
                            if (!oldFilesNames.contains(fileInfo.getName())) {
                                if (newFilesNames.contains(fileInfo.getName())) {
                                    String link = "error";
                                    int index = newFilesNames.indexOf(fileInfo.getName());
                                    MultipartFile file = files.get(index);

                                    try (InputStream in = file.getInputStream()) {
                                        BoxFile.Info newFileInfo = dataSetEditFolder.uploadFile(in, fileInfo.getName());
                                        BoxFile newFile = new BoxFile(api, newFileInfo.getID());
                                        BoxSharedLinkRequest sharedLinkRequest = new BoxSharedLinkRequest()
                                                .access(OPEN)
                                                .permissions(true, true);
                                        BoxSharedLink sharedLink = newFile.createSharedLink(sharedLinkRequest);
                                        link = sharedLink.getURL();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    EditDataFileRequest editDataFileRequest = new EditDataFileRequest();
                                    editDataFileRequest.setUserId(userId);
                                    editDataFileRequest.setStatus(1);
                                    DataFile dataFile = dataFileRepository.findByDataSetIdAndName(dataSetId, fileInfo.getName());
                                    editDataFileRequest.setDataFileId(dataFile.getId());
                                    editDataFileRequest.setUpdatedAt(now);
                                    editDataFileRequest.setLinkToFile(link);
                                    editDataFileRequest.setName(dataFile.getName());
                                    editDataFileRequestRepository.save(editDataFileRequest);
                                    files.remove(index);
                                } else {
                                    EditDataFileRequest editDataFileRequest = new EditDataFileRequest();
                                    editDataFileRequest.setUserId(userId);
                                    editDataFileRequest.setStatus(1);
                                    DataFile dataFile = dataFileRepository.findByDataSetIdAndName(dataSetId, fileInfo.getName());
                                    editDataFileRequest.setDataFileId(dataFile.getId());
                                    editDataFileRequest.setUpdatedAt(now);
                                    editDataFileRequest.setLinkToFile("deleted");
                                    editDataFileRequest.setName(dataFile.getName());
                                    editDataFileRequestRepository.save(editDataFileRequest);
                                }
                            }
                        }
                    }
                } catch (Throwable throwable) {
                    mav = new ModelAndView("info");
                    mav.addObject("messnum", 1);
                    mav.addObject("msg", "Трапилася помилка. Дані не відповідають потрібним.");
                    mav.addObject("link", "/");
                    mav.addObject("text", "Натисніть, щоб вийти ➜");
                    return mav;
                }

                for (MultipartFile file: files) {
                    uploadFileToBox(userId, now, dataSetId, dataSetFolder, file);
                }

                // edit dataset
                dataSet.setUpdatedAt(now);
                if (!"-".equals(tagName)) {
                    dataSet.setTagId(tagRepository.findByName(tagName).getId());
                } else {
                    dataSet.setTagId(null);
                }
                dataSet.setName(datasetName);
                dataSetRepository.save(dataSet);

                mav = new ModelAndView("info");
                mav.addObject("messnum", 2);
                mav.addObject("msg", "Датасет оновлено.\n" +
                        "Якщо ви завантажили або редагували файли, то вони будуть додані " +
                        "або оновлені після перевірки модератором");
                mav.addObject("link", "/my/datasets");
            } else {
                mav = new ModelAndView("info");
                mav.addObject("messnum", 1);
                mav.addObject("msg", "Недостатньо прав");
                mav.addObject("link", "/");
            }
            mav.addObject("text", "Натисніть, щоб вийти ➜");
        } else {
            mav = new ModelAndView("info");
            mav.addObject("messnum", 1);
            mav.addObject("msg", "Трапилася помилка");
            mav.addObject("link", "/");
            mav.addObject("text", "Натисніть, щоб вийти ➜");
        }
        return mav;
    }

    private ModelAndView isAllChecked(DataSet dataSet) {
        ModelAndView mav;
        if (addDataFileRequestRepository.existsByDataSetIdAndStatus(dataSet.getId(), 1)) {
            mav = new ModelAndView("info");
            mav.addObject("messnum", 2);
            mav.addObject("msg", "Ваш минулий запит на редагування або створення ще не повністю перевірений");
            mav.addObject("link", "/");
            mav.addObject("text", "Натисніть, щоб вийти ➜");
            return mav;
        }
        for (DataFile dataFile: dataFileRepository.findAllByDataSetId(dataSet.getId())) {
            if (editDataFileRequestRepository.existsByDataFileIdAndStatus(dataFile.getId(), 1)) {
                mav = new ModelAndView("info");
                mav.addObject("messnum", 2);
                mav.addObject("msg", "Ваш минулий запит на редагування або створення ще не повністю перевірений");
                mav.addObject("link", "/");
                mav.addObject("text", "Натисніть, щоб вийти ➜");
                return mav;
            }
        }
        return null;
    }

    private void uploadFileToBox(int userId, LocalDate now, int dataSetId, BoxFolder folder, MultipartFile file) {
        String link = "error";
        String fileName = file.getOriginalFilename();

        try (InputStream in = file.getInputStream()) {
            BoxFile.Info newFileInfo = folder.uploadFile(in, fileName);
            BoxFile newFile = new BoxFile(api, newFileInfo.getID());
            BoxSharedLinkRequest sharedLinkRequest = new BoxSharedLinkRequest()
                    .access(OPEN)
                    .permissions(true, true);
            BoxSharedLink sharedLink = newFile.createSharedLink(sharedLinkRequest);
            link = sharedLink.getURL();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AddDataFileRequest addDataFileRequest = new AddDataFileRequest();
        addDataFileRequest.setCreatedAt(now);
        addDataFileRequest.setLinkToFile(link);
        addDataFileRequest.setName(fileName);
        addDataFileRequest.setUserId(userId);
        addDataFileRequest.setStatus(1);
        addDataFileRequest.setDataSetId(dataSetId);

        addDataFileRequestRepository.save(addDataFileRequest);
    }
}
