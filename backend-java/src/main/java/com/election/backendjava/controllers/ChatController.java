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
import com.election.backendjava.security.services.UpvoteService;
import com.election.backendjava.security.services.UserDetailsImpl;
import com.election.backendjava.dto.ReplyDTO;
import com.election.backendjava.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private UpvoteService upvoteService;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    TopicRepository topicRepository;

    @PostMapping("/topic/{topicId}/comment/post")
    public ResponseEntity<?> addComment(@PathVariable Long topicId, @RequestBody CommentRequest commentRequest) {
        // validation of request body
        if (commentRequest == null || commentRequest.getCommentText() == null || commentRequest.getCommentText().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid comment request");
        }

        // validate topic
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        // validate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user authentication");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // make new comment
        Comment comment = new Comment();
        comment.setCommentText(commentRequest.getCommentText());
        comment.setCommentTitle(commentRequest.getTitle());
        comment.setUser(user);
        comment.setTopic(topic); // set the relation of the topic

        // save the comment
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
    public ResponseEntity<Page<CommentDTO>> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Comment> comments = commentRepository.findAll(pageable);

        Page<CommentDTO> commentDTOs = comments.map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setCommentText(comment.getCommentText());
            commentDTO.setCommentTitle(comment.getCommentTitle());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            commentDTO.setUserName(comment.getUser().getUsername());
            commentDTO.setUserId(comment.getUser().getUserId()); // Voeg userId toe
            commentDTO.setReplies(comment.getReplies().stream()
                    .filter(reply -> reply.getParentReply() == null)
                    .map(this::mapReply)
                    .toList());
            return commentDTO;
        });

        return ResponseEntity.ok(commentDTOs);
    }

    private ReplyDTO mapReply(Reply reply) {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setReplyId(reply.getReplyId());
        replyDTO.setReplyText(reply.getReplyText());
        replyDTO.setCreatedAt(reply.getCreatedAt());
        replyDTO.setUserName(reply.getUser().getUsername());
        replyDTO.setUserId(reply.getUser().getUserId());
        replyDTO.setChildReplies(reply.getChildReplies().stream()
                .map(this::mapReply)
                .toList());
        return replyDTO;
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return ResponseEntity.ok(user);
    }

    @PutMapping("/comment/{commentId}/upvote")
    public ResponseEntity<Integer> upvoteComment(@PathVariable Long commentId) {
        Integer updatedUpvotes = upvoteService.upvoteComment(commentId);
        return ResponseEntity.ok(updatedUpvotes);
    }

    @PutMapping("/reply/{replyId}/upvote")
    public ResponseEntity<Integer> upvoteReply(@PathVariable Long replyId) {
        Integer updatedUpvotes = upvoteService.upvoteReply(replyId);
        return ResponseEntity.ok(updatedUpvotes);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Controleer of de comment bestaat
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        // Controleer of de ingelogde gebruiker de eigenaar is van de comment
        if (!comment.getUser().getId().equals(userDetails.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own comments");
        }

        commentRepository.delete(comment); // Verwijder de comment

        return ResponseEntity.ok("Comment deleted successfully");
    }
    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long replyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Controleer of de reply bestaat
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

        // Controleer of de ingelogde gebruiker de eigenaar is van de reply
        if (!reply.getUser().getId().equals(userDetails.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own replies");
        }

        replyRepository.delete(reply); // Verwijder de reply

        return ResponseEntity.ok("Reply deleted successfully");
    }
}


