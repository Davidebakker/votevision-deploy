<script>
import axios from "axios";
import {comment} from "postcss";

export default {
  props: {
    upvotesCount: {
      type: Number,
      required: true,
      default: 0,
    },
    commentId: {
      type: Number,
      required: true,
      default: null,
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
    async upvoteComment() {
      console.log("Comment ID:", this.commentId);
      try {
        const response = await axios.put(`/api/chat/comment/${this.commentId}/upvote`);
        console.log("Upvote success:", response.data);
        this.$emit("update-upvotes", response.data);
      } catch (error) {
        console.error("Failed to upvote:", error.response || error.message);
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
    handleUpvotes(newUpvotes) {
      console.log("Updated upvotes:", newUpvotes);
        // Update de state of UI met de nieuwe waarde
      },
    },
};
</script>

<template>
  <div class="flex items-center space-x-4">
    <button @click="upvoteComment">Upvote</button>
    <span>{{ upvotesCount }}</span>
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
