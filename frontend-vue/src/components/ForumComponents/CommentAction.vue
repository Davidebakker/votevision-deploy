<script>
import axios from "axios";
import CustomAlert from "@/components/CustomAlert.vue";

export default {
  components: {
    CustomAlert,
  },
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
    },
  },
  data() {
    return {
      upvotes: this.upvotesCount,
      reported: false,
      showAlert: false,
      alertData: {
        title: "",
        message: "",
        type: "success", // Default alert type
      },
    };
  },
  methods: {
    showCustomAlert(title, message, type = "success") {
      this.alertData = { title, message, type };
      this.showAlert = true;
    },
    async upvoteItem() {
      const id = this.replyId || this.commentId;
      if (!id || id <= 0) {
        console.error("Invalid ID:", id);
        this.showCustomAlert("Error", "Cannot upvote. Invalid ID.", "error");
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
        this.showCustomAlert("Success", "Upvote successful!", "success");
      } catch (error) {
        console.error("Failed to upvote:", error.response || error.message);
        this.showCustomAlert("Error", "Failed to upvote. Please try again.", "error");
      }
    },
    handleReport() {
      if (this.reported) {
        this.showCustomAlert(
          "Notice",
          "You already reported this comment/reply.",
          "info"
        );
        return;
      }
      this.reported = true;
      console.log(`Reported comment/reply with ID: ${this.commentId || this.replyId}`);
      this.showCustomAlert(
        "Reported",
        "This comment/reply has been reported. Thank you for your feedback.",
        "success"
      );
    },
  },
};
</script>


<template>
  <div class="flex items-center space-x-4">
    <!-- Upvote button -->
    <button
      v-if="showUpvote"
      @click="upvoteItem"
      class="text-green-500 hover:text-green-600 w-8 h-8 flex justify-center items-center"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="currentColor"
      >
        <polygon points="12,6 18,18 6,18" />
      </svg>
    </button>
    <span v-if="showUpvote">{{ upvotes }}</span>

    <!-- Report button -->
    <button
      v-if="showReport"
      @click="handleReport"
      class="ml-auto text-red-600 hover:underline"
    >
      Report
    </button>

    <!-- Custom Alert -->
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      :type="alertData.type"
      @close="showAlert = false"
    />
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
