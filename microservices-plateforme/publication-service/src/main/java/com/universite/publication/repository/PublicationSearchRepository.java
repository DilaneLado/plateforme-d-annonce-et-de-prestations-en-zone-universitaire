package com.universite.publication.repository;

import com.universite.publication.document.PublicationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationSearchRepository extends ElasticsearchRepository<PublicationDocument, String> {
}
