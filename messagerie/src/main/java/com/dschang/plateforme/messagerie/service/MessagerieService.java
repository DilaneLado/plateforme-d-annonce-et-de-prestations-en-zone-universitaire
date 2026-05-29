package com.dschang.plateforme.messagerie.service;

import com.dschang.plateforme.messagerie.dto.ChatMessageRequest;
import com.dschang.plateforme.messagerie.dto.ConversationResponse;
import com.dschang.plateforme.messagerie.dto.MessageResponse;
import com.dschang.plateforme.messagerie.model.Conversation;
import com.dschang.plateforme.messagerie.model.Message;
import com.dschang.plateforme.messagerie.repository.ConversationRepository;
import com.dschang.plateforme.messagerie.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessagerieService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.routing-key-message}")
    private String routingKeyMessage;

    // Créer ou récupérer une conversation entre deux utilisateurs
    public ConversationResponse createOrGetConversation(UUID participant1, UUID participant2, UUID annonceId) {
        List<Conversation> existing = conversationRepository.findByParticipantsContaining(participant1);
        Optional<Conversation> optConv = existing.stream()
                .filter(c -> c.getParticipants().contains(participant2))
                .findFirst();

        Conversation conversation;
        if (optConv.isPresent()) {
            conversation = optConv.get();
            if (annonceId != null) {
                conversation.setAnnonceId(annonceId);
            }
            conversation.setDerniereActivite(Instant.now());
        } else {
            conversation = Conversation.builder()
                    .participants(List.of(participant1, participant2))
                    .annonceId(annonceId)
                    .derniereActivite(Instant.now())
                    .build();
        }
        conversation = conversationRepository.save(conversation);

        List<Message> messages = messageRepository.findByConversationIdOrderByTimestampAsc(conversation.getId());
        MessageResponse dernierMessage = null;
        if (!messages.isEmpty()) {
            dernierMessage = mapToMessageResponse(messages.get(messages.size() - 1));
        }

        return ConversationResponse.builder()
                .id(conversation.getId())
                .participants(conversation.getParticipants())
                .annonceId(conversation.getAnnonceId())
                .derniereActivite(conversation.getDerniereActivite())
                .dernierMessage(dernierMessage)
                .build();
    }

    // Envoyer un message
    public MessageResponse sendMessage(String conversationId, UUID expediteurId, String contenu) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation introuvable"));
        if (!conv.getParticipants().contains(expediteurId)) {
            throw new RuntimeException("L'utilisateur ne fait pas partie de cette conversation");
        }

        Message msg = Message.builder()
                .conversationId(conversationId)
                .expediteurId(expediteurId)
                .contenu(contenu)
                .timestamp(Instant.now())
                .lu(false)
                .build();
        msg = messageRepository.save(msg);

        conv.setDerniereActivite(msg.getTimestamp());
        conversationRepository.save(conv);

        rabbitTemplate.convertAndSend("messagerie-exchange", routingKeyMessage, msg);

        return mapToMessageResponse(msg);
    }

    // Récupérer les messages d'une conversation
    public List<MessageResponse> getMessages(String conversationId, UUID requesterId) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation introuvable"));
        if (!conv.getParticipants().contains(requesterId)) {
            throw new RuntimeException("Accès non autorisé à la conversation");
        }
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId)
                .stream().map(this::mapToMessageResponse).collect(Collectors.toList());
    }

    // Marquer un message comme lu
    public void markAsRead(String messageId, UUID readerId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message introuvable"));
        if (!message.isLu() && !message.getExpediteurId().equals(readerId)) {
            message.setLu(true);
            messageRepository.save(message);
        }
    }

    // Lister les conversations d'un utilisateur
    public List<ConversationResponse> getUserConversations(UUID userId) {
        return conversationRepository.findByParticipantsContaining(userId).stream()
                .map(conv -> {
                    List<Message> messages = messageRepository.findByConversationIdOrderByTimestampAsc(conv.getId());
                    MessageResponse dernier = null;
                    if (!messages.isEmpty()) {
                        dernier = mapToMessageResponse(messages.get(messages.size() - 1));
                    }
                    return ConversationResponse.builder()
                            .id(conv.getId())
                            .participants(conv.getParticipants())
                            .annonceId(conv.getAnnonceId())
                            .derniereActivite(conv.getDerniereActivite())
                            .dernierMessage(dernier)
                            .build();
                }).collect(Collectors.toList());
    }

    private MessageResponse mapToMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .expediteurId(message.getExpediteurId())
                .contenu(message.getContenu())
                .timestamp(message.getTimestamp())
                .lu(message.isLu())
                .build();
    }
}