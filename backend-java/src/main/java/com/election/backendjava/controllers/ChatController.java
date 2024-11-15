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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(commentRepository.findAll());
    }

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
        commentRepository.save(comment);

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

}
