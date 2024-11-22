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
    TopicRepository topicRepository;

    @Autowired
    ReplyRepository replyRepository;

    @PostMapping("/topic/{topicId}/comment/post")
    public ResponseEntity<?> addComment(@PathVariable Long topicId, @RequestBody CommentRequest commentRequest) {
        if (commentRequest == null || commentRequest.getCommentText() == null || commentRequest.getCommentText().isEmpty()) {
            return ResponseEntity.status(400).body("Invalid comment request");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal(); // Cast to UserDetailsImpl
        Long userId = userDetails.getId();

        // Fetch the topic and user from the database
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Create a new Comment and set the fields
        Comment comment = new Comment();
        comment.setCommentText(commentRequest.getCommentText());
        comment.setCommentTitle(commentRequest.getTitle());
        comment.setTopic(topic);
        comment.setUser(user);

        // Save the comment to the repository
//        commentRepository.save(comment);
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok("Comment added");
    }

    @PostMapping("/comment/{commentId}/reply/post")
    public ResponseEntity<?> addReply(@PathVariable Long commentId, @RequestBody ReplyRequest replyRequest) {
        if (replyRequest == null || replyRequest.getReplyText() == null || replyRequest.getReplyText().isEmpty()) {
            return ResponseEntity.status(400).body("Invalid reply request");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        // Create a new Reply and set the fields
        Reply reply = new Reply();
        reply.setReplyText(replyRequest.getReplyText());
        reply.setComment(comment);
        reply.setUser(user);

        // Save the reply to the repository
        Reply savedReply = replyRepository.save(reply);

        // Return the saved reply
        return ResponseEntity.ok(savedReply);
    }

    @PostMapping("/reply/{replyId}/reply/post")
    public ResponseEntity<?> addNestedReply(@PathVariable Long replyId, @RequestBody ReplyRequest replyRequest) {
        if (replyRequest == null || replyRequest.getReplyText() == null || replyRequest.getReplyText().isEmpty()) {
            return ResponseEntity.status(400).body("Invalid reply request");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Reply parentReply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent reply not found"));

        // Gebruik de comment van de parent reply
        Comment parentComment = parentReply.getComment();

        Reply reply = new Reply();
        reply.setReplyText(replyRequest.getReplyText());
        reply.setParentReply(parentReply);
        reply.setComment(parentComment); // Stel de comment in
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
            commentDTO.setUserName(comment.getUser().getUsername()); // Voeg gebruikersnaam toe

            // Map de replies
            List<ReplyDTO> replies = comment.getReplies().stream().map(reply -> {
                ReplyDTO replyDTO = new ReplyDTO();
                replyDTO.setReplyId(reply.getReplyId());
                replyDTO.setReplyText(reply.getReplyText());
                replyDTO.setCreatedAt(reply.getCreatedAt());
                replyDTO.setUserName(reply.getUser().getUsername()); // Voeg gebruikersnaam toe
                return replyDTO;
            }).toList();

            commentDTO.setReplies(replies);
            return commentDTO;
        }).toList();

        return ResponseEntity.ok(commentDTOs);
    }



}
