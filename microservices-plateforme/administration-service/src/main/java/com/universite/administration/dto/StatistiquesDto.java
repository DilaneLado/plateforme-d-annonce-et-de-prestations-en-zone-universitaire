package com.universite.administration.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class StatistiquesDto {
    private long totalPublications;
    private long publicationsActives;
    private long publicationsEnModeration;
    private long publicationsArchivees;
    private long publicationsSignalees;
    private Map<String, Long> publicationsParType;
}
