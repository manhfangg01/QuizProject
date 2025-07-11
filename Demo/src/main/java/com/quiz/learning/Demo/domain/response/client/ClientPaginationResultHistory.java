package com.quiz.learning.Demo.domain.response.client;

import java.util.List;

import com.quiz.learning.Demo.domain.metadata.Metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientPaginationResultHistory {
    private List<ClientResultHistoryDTO> histories;
    private Metadata metadata;

}
