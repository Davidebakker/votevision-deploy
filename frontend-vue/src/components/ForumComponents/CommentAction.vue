<script>
import axios from "axios";
import {comment} from "postcss";

export default {
  props: {
    upvotesCount: {
      type: Number,
      required: true,
    },
    commentId: {
      type: Number,
      required: true,
    },
    replyId: {
      type: Number,
      required: false, // Alleen nodig voor replies
    },
  },
  data() {
    return {
      upvotes: this.upvotesCount,
      reported: false,
    };
  },
  methods: {
    comment,
    async upvoteComment(commentId) {
      try {
        const response = await axios.put(`/api/chat/comment/${commentId}/upvote`);
        this.comment.upvotes = response.data; // Update de UI met het nieuwe aantal upvotes
      } catch (error) {
        console.error("Failed to upvote:", error);
      }
    },
    handleReport() {
      if (this.reported) {
        alert("You already reported this comment/reply.");
        return;
      }
      this.reported = true;
      console.log(`Reported comment/reply with ID: ${this.commentId || this.replyId}`);
      alert("This comment/reply has been reported.");
    },
  },
};
</script>

<template>
  <div class="flex items-center space-x-4">
    <button @click="upvoteComment(comment.id)">Upvote</button>
    <span>{{ comment.upvotes }}</span>
    <button @click="handleReport" class="text-red-600 hover:underline">
      Report
    </button>
  </div>
</template>



<style scoped>
button {
  transition: color 0.2s ease;
}

button:hover {
  color: #555;
}
</style>
