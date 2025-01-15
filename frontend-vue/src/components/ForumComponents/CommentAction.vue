<script>
import axios from "axios";
import CustomAlert from "@/components/CustomAlert.vue";
import ConfirmDialog from "@/components/ConfirmDialog.vue";

export default {
  components: {
    CustomAlert,
    ConfirmDialog,
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
    showDelete: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      upvotes: this.upvotesCount,
      reported: false,
      showAlert: false,
      alertData: { title: "", message: "", type: "info" },

      // ConfirmDialog for deleting a reply
      showDeleteDialog: false,
    };
  },
  methods: {
    showCustomAlert(title, message) {
      this.alertData = { title, message, type: "info" };
      this.showAlert = true;
    },

    async upvoteItem() {
      const id = this.replyId || this.commentId;
      if (!id || id <= 0) {
        this.showCustomAlert("Error", "Cannot upvote. Invalid ID.");
        return;
      }

      const endpoint = this.replyId
        ? `http://localhost:8080/api/chat/reply/${id}/upvote`
        : `http://localhost:8080/api/chat/comment/${id}/upvote`;

      try {
        const response = await axios.put(endpoint, null, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
          },
        });
        this.upvotes = response.data;
        this.$emit("update-upvotes", {
          replyId: this.replyId,
          commentId: this.commentId,
          updatedUpvotes: this.upvotes,
        });
        this.showCustomAlert("Upvoted", "Your upvote was successful.");
      } catch (error) {
        console.error("Failed to upvote:", error.response || error.message);
        this.showCustomAlert("Error", "Failed to upvote. Please try again.");
      }
    },

    handleReport() {
      if (this.reported) {
        this.showCustomAlert("Notice", "You already reported this comment/reply.");
        return;
      }
      this.reported = true;
      this.showCustomAlert(
        "Reported",
        "Thanks for your feedback, we have recorded your report."
      );
    },

    // Step 1: open confirm dialog
    openDeleteDialog() {
      this.showDeleteDialog = true;
    },

    // Step 2: if user confirms
    async confirmDelete() {
      const id = this.replyId || this.commentId;
      window.location.reload();
      if (!id || id <= 0) {
        this.showCustomAlert("Error", "Cannot delete. Invalid ID.");
        this.showDeleteDialog = false;
        return;
      }

      try {
        await axios.delete(`http://localhost:8080/api/chat/reply/${id}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
          },
        });

        this.$emit("delete-item", {
          replyId: this.replyId,
          commentId: this.commentId,
        });

        this.showCustomAlert("Deleted", "Reply successfully deleted!");
        // Reload so user sees it removed
        // window.location.reload();
      } catch (error) {
        console.error("Failed to delete:", error.response || error.message);
        this.showCustomAlert("Error", "Failed to delete. Please try again.");
      } finally {
        this.showDeleteDialog = false;
      }
    },

    cancelDelete() {
      this.showDeleteDialog = false;
    },
  },
};
</script>

<template>
  <div class="flex items-center space-x-4">
    <!-- Custom Alert -->
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      :type="alertData.type"
      @close="showAlert = false"
    />

    <!-- Confirm Dialog for deleting a reply -->
    <ConfirmDialog
      v-if="showDeleteDialog"
      title="Confirm Deletion"
      message="Are you sure you want to delete this reply?"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
    />

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

    <!-- Report -->
    <button
      v-if="showReport"
      @click="handleReport"
      class="ml-auto text-yellow-600 hover:underline"
    >
      Report
    </button>

    <!-- Delete (only show if showDelete true) -->
    <button
      v-if="showDelete"
      @click="openDeleteDialog"
      class="text-red-600 hover:underline"
    >
      Delete
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
