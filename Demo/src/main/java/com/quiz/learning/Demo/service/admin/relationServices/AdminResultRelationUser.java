package com.quiz.learning.Demo.service.admin.relationServices;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.learning.Demo.domain.Result;
import com.quiz.learning.Demo.domain.User;
import com.quiz.learning.Demo.domain.response.admin.FetchAdminDTO;
import com.quiz.learning.Demo.repository.ResultRepository;
import com.quiz.learning.Demo.repository.UserRepository;
import com.quiz.learning.Demo.service.azure.AzureBlobService;
import com.quiz.learning.Demo.util.error.ObjectNotFound;

@Service
public class AdminResultRelationUser {
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final AzureBlobService azureBlobService;

    public AdminResultRelationUser(ResultRepository resultRepository, UserRepository userRepository,
            AzureBlobService azureBlobService) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.azureBlobService = azureBlobService;
    }

    public void handleDeleteUser(Long id) {
        Optional<User> checkUser = this.userRepository.findById(id);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFound("There is no user has id: " + id);
        }
        User realUser = checkUser.get();
        if (realUser.getResults() != null) {
            for (Result result : realUser.getResults()) {
                this.resultRepository.delete(result);
            }
        }

        if (realUser.getUserAvatarUrls() != null && !realUser.getUserAvatarUrls().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(realUser.getUserAvatarUrls());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }
        this.userRepository.delete(realUser);
    }

}
