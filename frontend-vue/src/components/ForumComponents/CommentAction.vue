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
      required: false,
    },
    replyId: {
      type: Number,
      required: false,
    },
    showUpvote: {
      type: Boolean,
      default: true,
    },
    showReport: {
      type: Boolean,
      default: true,
    }
  },
  data() {
    return {
      upvotes: this.upvotesCount,
      reported: false,
    };
  },
  methods: {
    async upvoteItem() {
      const id = this.replyId || this.commentId;
      if (!id || id <= 0) {
        console.error("Invalid ID:", id);
        alert("Cannot upvote. Invalid ID.");
        return;
      }

      const endpoint = this.replyId
          ? `http://localhost:8080/api/chat/reply/${id}/upvote`
          : `http://localhost:8080/api/chat/comment/${id}/upvote`;

      try {
        const response = await axios.put(endpoint);
        this.upvotes = response.data;
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
      console.log(`Reported comment/reply with ID: ${this.commentId || this.replyId}`);
      alert("This comment/reply has been reported.");
    },

    async deleteItem() {
      const id = this.replyId || this.commentId;
      if (!id || id <= 0) {
        console.error("Invalid ID:", id);
        alert("Cannot delete. Invalid ID.");
        return;
      }

      const endpoint = this.replyId
          ? `http://localhost:8080/api/chat/reply/${id}`
          : `http://localhost:8080/api/chat/comment/${id}`;

      try {
        await axios.delete(endpoint);
        this.$emit("delete-item", {
          replyId: this.replyId,
          commentId: this.commentId,
        });
        alert("Item successfully deleted!");
      } catch (error) {
        console.error("Failed to delete:", error.response || error.message);
        alert("Failed to delete. Please try again.");
      }
    }
  },
};
</script>

<template>
  <div class="flex items-center space-x-4">
    <!-- Upvote enkel tonen indien showUpvote true is -->
    <button
        v-if="showUpvote"
        @click="upvoteItem"
        class="text-green-500 hover:text-green-600 w-8 h-8 flex justify-center items-center"
    >
      <!-- Kleinere SVG driehoek -->
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
        <polygon points="12,6 18,18 6,18"/>
      </svg>
    </button>
    <span v-if="showUpvote">{{ upvotes }}</span>

    <!-- Report enkel tonen indien showReport true is -->
    <button v-if="showReport" @click="handleReport" class="ml-auto text-red-600 hover:underline">
      Report
    </button>

    <button
        v-if="showDelete"
        @click="deleteItem"
        class="text-red-600 hover:underline"
    >
      Delete
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
