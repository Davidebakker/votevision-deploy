<script>
import axios from "axios";

export default {
  props: {
    upvotesCount: {
      type: Number,
      required: true,
    },
    commentId: {
      type: Number,
      required: false, // Kan nu optioneel zijn
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
    async upvoteItem() {
      // Bepaal het juiste ID en endpoint
      const id = this.replyId || this.commentId; // replyId heeft voorrang
      if (!id || id <= 0) {
        console.error("Invalid ID:", id);
        alert("Cannot upvote. Invalid ID.");
        return;
      }

      const endpoint = this.replyId
          ? `http://localhost:8080/api/chat/reply/${id}/upvote`
          : `http://localhost:8080/api/chat/comment/${id}/upvote`;


      console.log("Upvoting ID:", id, "via endpoint:", endpoint);

      try {
        const response = await axios.put(endpoint);
        console.log("Upvote success:", response.data);

        // Bijwerken van lokale state
        this.upvotes = response.data; // Aantal upvotes wordt teruggegeven
        this.$emit("update-upvotes", {
          replyId: this.replyId,
          commentId: this.commentId,
          updatedUpvotes: this.upvotes,
        });
      } catch (error) {
        console.error("Failed to upvote:", error.response || error.message);
        alert("Failed to upvote. Please try again.");
      }
    },
    handleReport() {
      if (this.reported) {
        alert("You already reported this comment/reply.");
        return;
      }
      this.reported = true;
      console.log(
          `Reported comment/reply with ID: ${this.commentId || this.replyId}`
      );
      alert("This comment/reply has been reported.");
    },
  },
};
</script>

<template>
  <div class="flex items-center space-x-4">
    <!-- Upvote Button -->
    <button @click="upvoteItem" class="text-blue-500 hover:underline">
      Upvote
    </button>
    <span>{{ upvotes }}</span>

    <!-- Report Button -->
    <button @click="handleReport" class="text-red-600 hover:underline">
      Report
    </button>
  </div>
</template>
<CommentAction
    :upvotesCount="comment.upvotes"
    :commentId="comment.commentId"
    @update-upvotes="handleUpvotes"
/>


<style scoped>
button {
  transition: color 0.2s ease;
}

button:hover {
  color: #555;
}
</style>
