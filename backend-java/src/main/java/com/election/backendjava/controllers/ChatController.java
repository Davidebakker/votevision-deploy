package com.election.backendjava.controllers;

import com.election.backendjava.models.form.Comment;
import com.election.backendjava.models.form.Reply;
import com.election.backendjava.models.form.Topic;
import com.election.backendjava.models.user.User;
import com.election.backendjava.payload.request.CommentRequest;
import com.election.backendjava.payload.request.ReplyRequest;
import com.election.backendjava.repositories.form.CommentRepository;
import com.election.backendjava.repositories.form.ReplyRepository;
import com.election.backendjava.repositories.form.TopicRepository;
import com.election.backendjava.repositories.user.UserRepository;
import com.election.backendjava.security.services.UserDetailsImpl;
import com.election.backendjava.dto.ReplyDTO;
import com.election.backendjava.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    TopicRepository topicRepository;

    @PostMapping("/topic/{topicId}/comment/post")
    public ResponseEntity<?> addComment(@PathVariable Long topicId, @RequestBody CommentRequest commentRequest) {
        // Validatie van request body
        if (commentRequest == null || commentRequest.getCommentText() == null || commentRequest.getCommentText().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid comment request");
        }

        // Valideer topic
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        // Valideer gebruiker
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user authentication");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Maak de nieuwe comment
        Comment comment = new Comment();
        comment.setCommentText(commentRequest.getCommentText());
        comment.setCommentTitle(commentRequest.getTitle());
        comment.setUser(user);
        comment.setTopic(topic); // Stel de relatie met Topic in

        // Sla de comment op
        Comment savedComment = commentRepository.save(comment);

        return ResponseEntity.ok(savedComment);
    }


    @PostMapping("/comment/{commentId}/reply/post")
    public ResponseEntity<?> addReply(@PathVariable Long commentId, @RequestBody ReplyRequest replyRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        Reply reply = new Reply();
        reply.setReplyText(replyRequest.getReplyText());
        reply.setComment(comment);
        reply.setUser(user);
        Reply savedReply = replyRepository.save(reply);

        return ResponseEntity.ok(savedReply);
    }

    @PostMapping("/reply/{replyId}/reply/post")
    public ResponseEntity<?> addNestedReply(@PathVariable Long replyId, @RequestBody ReplyRequest replyRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Reply parentReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent reply not found"));

        Reply reply = new Reply();
        reply.setReplyText(replyRequest.getReplyText());
        reply.setParentReply(parentReply);
        reply.setComment(parentReply.getComment());
        reply.setUser(user);

        Reply savedReply = replyRepository.save(reply);

        return ResponseEntity.ok(savedReply);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setCommentText(comment.getCommentText());
            commentDTO.setCommentTitle(comment.getCommentTitle());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            commentDTO.setUserName(comment.getUser().getUsername());
            commentDTO.setReplies(comment.getReplies().stream()
                    .filter(reply -> reply.getParentReply() == null)
                    .map(this::mapReply)
                    .toList());
            return commentDTO;
        }).toList();

        return ResponseEntity.ok(commentDTOs);
    }

    private ReplyDTO mapReply(Reply reply) {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setReplyId(reply.getReplyId());
        replyDTO.setReplyText(reply.getReplyText());
        replyDTO.setCreatedAt(reply.getCreatedAt());
        replyDTO.setUserName(reply.getUser().getUsername());
        replyDTO.setChildReplies(reply.getChildReplies().stream()
                .map(this::mapReply)
                .toList());
        return replyDTO;
    }
}
