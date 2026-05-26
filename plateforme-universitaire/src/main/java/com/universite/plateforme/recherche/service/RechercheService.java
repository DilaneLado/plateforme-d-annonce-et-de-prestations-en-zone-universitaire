package com.universite.plateforme.recherche.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.universite.plateforme.publication.document.PublicationDocument;
import com.universite.plateforme.recherche.dto.RechercheRequest;
import com.universite.plateforme.recherche.dto.RechercheResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RechercheService {

    private final ElasticsearchClient elasticsearchClient;

    public RechercheResponse rechercher(RechercheRequest request) {
        try {
            List<Query> mustQueries = new ArrayList<>();
            List<Query> filterQueries = new ArrayList<>();

            // Recherche full-text sur titre et description
            if (request.getQ() != null && !request.getQ().isBlank()) {
                mustQueries.add(Query.of(q -> q
                        .multiMatch(mm -> mm
                                .fields("titre^2", "description", "categorie")
                                .query(request.getQ())
                                .fuzziness("AUTO")
                        )
                ));
            } else {
                mustQueries.add(Query.of(q -> q.matchAll(ma -> ma)));
            }

            // Filtre statut : toujours ACTIVE
            filterQueries.add(Query.of(q -> q
                    .term(t -> t.field("statut").value("ACTIVE"))
            ));

            // Filtre par type
            if (request.getType() != null) {
                filterQueries.add(Query.of(q -> q
                        .term(t -> t.field("type").value(request.getType()))
                ));
            }

            // Filtre par catégorie
            if (request.getCategorie() != null && !request.getCategorie().isBlank()) {
                filterQueries.add(Query.of(q -> q
                        .term(t -> t.field("categorie").value(request.getCategorie()))
                ));
            }

            // Filtre par localisation
            if (request.getLocalisation() != null && !request.getLocalisation().isBlank()) {
                filterQueries.add(Query.of(q -> q
                        .match(m -> m.field("localisation").query(request.getLocalisation()))
                ));
            }

            final List<Query> finalMust = mustQueries;
            final List<Query> finalFilter = filterQueries;

            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index("publications")
                    .from(request.getPage() * request.getSize())
                    .size(request.getSize())
                    .query(q -> q
                            .bool(b -> b
                                    .must(finalMust)
                                    .filter(finalFilter)
                            )
                    )
                    .highlight(h -> h
                            .fields("titre", f -> f.numberOfFragments(0))
                            .fields("description", f -> f.numberOfFragments(2).fragmentSize(150))
                    )
            );

            SearchResponse<PublicationDocument> response = elasticsearchClient.search(searchRequest, PublicationDocument.class);

            List<RechercheResponse.ResultatItem> resultats = response.hits().hits().stream()
                    .map(this::hitToResultat)
                    .collect(Collectors.toList());

            long total = response.hits().total() != null ? response.hits().total().value() : 0;

            return RechercheResponse.builder()
                    .resultats(resultats)
                    .total(total)
                    .page(request.getPage())
                    .size(request.getSize())
                    .totalPages((int) Math.ceil((double) total / request.getSize()))
                    .build();

        } catch (IOException e) {
            log.error("Erreur Elasticsearch lors de la recherche : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche", e);
        }
    }

    private RechercheResponse.ResultatItem hitToResultat(Hit<PublicationDocument> hit) {
        PublicationDocument doc = hit.source();
        if (doc == null) return null;

        List<String> highlights = new ArrayList<>();
        if (hit.highlight() != null) {
            hit.highlight().forEach((field, fragments) -> highlights.addAll(fragments));
        }

        return RechercheResponse.ResultatItem.builder()
                .id(doc.getId())
                .type(doc.getType())
                .titre(doc.getTitre())
                .description(doc.getDescription())
                .auteurNom(doc.getAuteurNom())
                .categorie(doc.getCategorie())
                .localisation(doc.getLocalisation())
                .createdAt(doc.getCreatedAt())
                .highlights(highlights)
                .score(hit.score())
                .build();
    }
}
