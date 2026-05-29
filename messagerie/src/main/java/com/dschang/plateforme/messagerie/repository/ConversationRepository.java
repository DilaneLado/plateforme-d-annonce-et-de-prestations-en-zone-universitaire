package com.dschang.plateforme.messagerie.repository;

import com.dschang.plateforme.messagerie.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
    List<Conversation> findByParticipantsContaining(UUID userId);
}