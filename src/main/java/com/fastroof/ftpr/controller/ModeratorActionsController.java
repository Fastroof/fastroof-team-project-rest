package com.fastroof.ftpr.controller;

import com.box.sdk.*;
import com.fastroof.ftpr.entity.*;
import com.fastroof.ftpr.pojo.AddFileRequestForCheck;
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

    private static final BoxDeveloperEditionAPIConnection api = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(BoxConfig.readFrom("{\"boxAppSettings\":{\"clientID\":\"5p1dcgaaou4x1da522cwyx29hatwxhy4\",\"clientSecret\":\"ojy2wAvXNIEiIzn7jurEUR1expahtGR4\",\"appAuth\": {\"publicKeyID\": \"j861aa88\",\"privateKey\": \"-----BEGIN ENCRYPTED PRIVATE KEY-----\\nMIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIbKTYCsUB4vgCAggA\\nMBQGCCqGSIb3DQMHBAiQhH8WpEU5iwSCBMh4PtcEtD3MAoE0GA60IC5shzC5rZix\\neLvfijXF0uE4iFHDXisimzbESv8TkUvc+BN5UI/T03LTEmWvgQ38YidVk6E1q2Zr\\nu3TRgJuPFJayDTOpndtPWWDayyz4lSMRndRyLT35QrRwD7L7o1iNdDlrn2AFL7g3\\naBDGNs/RzGrubele6SWGHXXd11dmuqOnvO3Z8Ek15V1TAZioFHXMsqLOTnZ18MP8\\n9ujd7QMDY27K0mchFYUtztcZwnFDdb6Psvcg/cX83seEDI8SpaoeyRDMcvJaIkyJ\\nQvAMKFIM8hEtlcU4i881oSzPoXklMEXODIkAZi48cYYegX+FdQD3+3Qfr4z7HhDE\\n82BlmWtIy9aOBfTA55NQqBarNb9bRRR9grsjWWMDfsaA+PNNp3KrqoneBkm1oaC4\\nBo4r2Mn/J0OqZf3MIzZgiKf7GW37cB3ZIs36Skqpjl+J24shP+HRi9Xz+qpSJYtG\\ndoWanTtl9+kDRojn7rco0wLIRFQVrOVUun7MRoS+1MVc1aylnOzO1uc7v9uiysep\\nLByHHmjmL/4AzY7l3lLB7Zxhg0uGosXanKxqa9mULpsSJopSHwysZLviX/cYCVvs\\ntPu4AObMdr01oDGH98fLdjiqzI/UoNaa4ejNZ0w3SFXjJrl6a/Vy1WONEhZ4o/dU\\nYNiaHoDSUDQPcWoktPmAxMzBK6UppUCWD7l059NQhbTRmI349V3Ajh8f8gtGy3fu\\nbmkGgeNtQ+BLBLicbWTve4yG0r6Q4CSI8Ec12VhkBtQ7Ko/1IIwQ1FrgYBSaKlJZ\\nmAsQSlFT5+2ruq5bpNsb1rTeMoNsSzqbkLXM9e89UBEI3EpyFOOygwSJWMtYVWmy\\nN4DJdyKiDddw5HYqwg7iNt6YGHccwyD2loJC50FAgB+CzNle49uJmZe6nZBzgiRv\\n7T/KZYQNSPVQB6p9K9xMqOh8CbCq+SO+XHCcqkoSDgwOFtFyLV+hxSTEdWNtC5uV\\nA3o+liioX0i3zkjdkpj8++mlAc9xO9o5o9hv/BKiQqfAWodzvvFZbnuu4VLg8zrt\\n0V+32IzF24o37P0NWfiMAXiJMK7C3vewN374nEANtBQX3j0vK4K9inf/YMgWzP76\\nSKqGsFs/NqG5FD/6TuEW1s9nolTfNl4W0tjvRtY2pyc2/7b6lO68kRpZf7KR8iga\\nF2r07j3nXkNFA9mPLSdLDdEHQTfdgdIjkb1CFmBQ2HDD6m+aO/SEaPIOtNACPuff\\nPoTwTMTYg/UvBMSiq1lTM0RyQXA7wXfiBqNKJGd6KQTBO/VYmxZfruWGCkGea2WF\\nektgHrNctglxdVMRmUxp3oLrvkKjQTFT6wHsYGxcJVop6P7FRItOCo/vfJOrsqEp\\nnar3ixRpI1XIRIpVjd0WRlMoKBmzpSXfJFxtp9COdIH1RYkEkhm4vqgmncSS2+5n\\nxKmz5YIoMB5bqprf7WQDG8hCSSHWudB9G1mUOpoCNZiBH2DUXsiLdjUadi72+Zer\\niPRBvgU5iqxcyLtUOHFnq/bj78L0ecyD2Bv0AAPnIc/P5VyHmjGGUCA2Y3lIh8+N\\n5ko+iT4+2K/szzEkNoPuPZ12xM+KxGsiIkPi3+ICtG/YOWVJcrUCfk/ZtI8WFl+3\\ngow=\\n-----END ENCRYPTED PRIVATE KEY-----\\n\",\"passphrase\":\"5d05a1c6644d8e022bdf559600b8efaf\"}},\"enterpriseID\": \"900264880\"}"));

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

            BoxItem.Info itemInfo = BoxItem.getSharedItem(api, addDataFileRequest.getLinkToFile());
            BoxFile file = new BoxFile(api, itemInfo.getID());
            file.delete();

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
            DataSet dataSet = dataSetRepository.findById(dataFile.getDataSetId()).get();
            BoxItem.Info itemInfo = BoxItem.getSharedItem(api, dataFile.getLinkToFile());
            BoxFile file = new BoxFile(api, itemInfo.getID());
            file.delete();
            if (editDataFileRequest.getLinkToFile().equals("deleted")) {
                editDataFileRequestRepository.delete(editDataFileRequest);
                dataFileRepository.delete(dataFile);
            } else {
                editDataFileRequestRepository.save(editDataFileRequest);
                BoxItem.Info itemInfoNew = BoxItem.getSharedItem(api, editDataFileRequest.getLinkToFile());
                BoxFile fileNew = new BoxFile(api, itemInfoNew.getID());

                // box
                // find owner folder
                BoxFolder ownerFolder = findAndGetFolder(BoxFolder.getRootFolder(api), String.valueOf(editDataFileRequest.getUserId()));
                // find data set folder
                BoxFolder dataSetFolder = findAndGetFolder(ownerFolder, String.valueOf(dataSet.getId()));

                fileNew.move(dataSetFolder);

                dataFile.setUpdatedAt(LocalDate.now());
                dataFile.setLinkToFile(editDataFileRequest.getLinkToFile());
                dataFile.setName(editDataFileRequest.getName());
                dataFileRepository.save(dataFile);
            }
            dataSet.setUpdatedAt(LocalDate.now());
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
            BoxItem.Info itemInfoNew = BoxItem.getSharedItem(api, editDataFileRequest.getLinkToFile());
            BoxFile fileNew = new BoxFile(api, itemInfoNew.getID());
            fileNew.delete();
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
