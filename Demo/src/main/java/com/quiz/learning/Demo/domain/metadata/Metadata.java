package com.quiz.learning.Demo.domain.metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metadata {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalObjects;
    private Boolean hasNext;
    private Boolean hasPrevious;
}
