package com.election.backendjava.services.forum;

import com.election.backendjava.models.form.Comment;
import com.election.backendjava.models.form.Reply;
import com.election.backendjava.repositories.form.CommentRepository;
import com.election.backendjava.repositories.form.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UpvoteService {

    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    public UpvoteService(CommentRepository commentRepository, ReplyRepository replyRepository) {
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }

    public Integer upvoteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        comment.setUpvotes(comment.getUpvotes() + 1);
        commentRepository.save(comment);
        return comment.getUpvotes();
    }

    public Integer upvoteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

        reply.setUpvotes(reply.getUpvotes() + 1);
        replyRepository.save(reply);
        return reply.getUpvotes();
    }
}
