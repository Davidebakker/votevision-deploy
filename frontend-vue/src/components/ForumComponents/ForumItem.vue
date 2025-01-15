<script>
import { ref, onMounted, onUnmounted } from 'vue';
import axios from 'axios';
import { formatDistanceToNow } from 'date-fns';
import { nl } from 'date-fns/locale';
import ReplyList from './ReplyList.vue';
import CommentAction from '@/components/ForumComponents/CommentAction.vue';
import CustomAlert from '@/components/CustomAlert.vue';
import ConfirmDialog from '@/components/ConfirmDialog.vue';

export default {
  components: {
    ReplyList,
    CommentAction,
    CustomAlert,
    ConfirmDialog,
  },
  setup() {
    // Reactive state
    const comments = ref([]);
    const replyTexts = ref({}); // Where we store typed text for all replies
    const currentUserId = ref(null);
    const currentPage = ref(0);
    const pageSize = ref(10);
    const isLoading = ref(false);
    const isLastPage = ref(false);
    const collapsed = ref({});
    const activeReplyId = ref(null);
    const jwtToken = localStorage.getItem('jwtToken');

    // Custom alert
    const showAlert = ref(false);
    const alertData = ref({ title: '', message: '', type: 'info' });
    function showCustomAlert(title, message) {
      alertData.value = { title, message, type: 'info' };
      showAlert.value = true;
    }

    // ConfirmDialog for deleting a comment
    const showDeleteDialog = ref(false);
    const commentIdToDelete = ref(null);

    const openDeleteDialog = (commentId) => {
      commentIdToDelete.value = commentId;
      showDeleteDialog.value = true;
    };

    const confirmDeleteComment = async () => {
      if (!commentIdToDelete.value) {
        showCustomAlert('Error', 'Comment ID is undefined.');
        showDeleteDialog.value = false;
        return;
      }

      try {
        await axios.delete(`http://localhost:8080/api/chat/comment/${commentIdToDelete.value}`, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        showCustomAlert('Deleted', 'Comment was successfully deleted!');
        // Reload to see it gone
        window.location.reload();
      } catch (error) {
        console.error('Failed to delete comment:', error.response || error.message);
        showCustomAlert('Delete Error', 'Unable to delete the comment. Please try again.');
      } finally {
        showDeleteDialog.value = false;
        commentIdToDelete.value = null;
      }
    };

    const cancelDeleteComment = () => {
      showDeleteDialog.value = false;
      commentIdToDelete.value = null;
    };

    // Updating the local text dictionary
    function handleUpdateReplyText({ replyId, text }) {
      replyTexts.value[replyId] = text;
    }

    const isOwner = (comment) => {
      return comment.userId === currentUserId.value;
    };

    // Fetch current user
    const fetchCurrentUserId = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/chat/user/me', {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
          },
        });
        currentUserId.value = response.data.userId;
      } catch (error) {
        console.error('Error fetching user ID:', error);
        showCustomAlert('Error', 'Unable to get the logged-in user. Check if you are logged in.');
      }
    };

    const toggleCollapse = (commentId) => {
      collapsed.value[commentId] = !collapsed.value[commentId];
    };

    const formatTimeAgo = (date) => {
      return formatDistanceToNow(new Date(date), { addSuffix: true, locale: nl });
    };

    // Fetch comments with pagination
    const fetchComments = async () => {
      if (isLoading.value || isLastPage.value) return;
      isLoading.value = true;

      try {
        const response = await axios.get('http://localhost:8080/api/chat/comments', {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json',
          },
          params: {
            page: currentPage.value,
            size: pageSize.value,
          },
        });

        comments.value = [...comments.value, ...response.data.content];
        response.data.content.forEach((c) => {
          collapsed.value[c.commentId] = true;
        });

        isLastPage.value = response.data.last;
        currentPage.value += 1;
      } catch (error) {
        console.error('Error fetching comments:', error);
        showCustomAlert('Error', 'Failed to fetch comments. Please try again.');
      } finally {
        isLoading.value = false;
      }
    };

    // Show/hide the reply field
    const toggleReplyField = (id) => {
      activeReplyId.value = activeReplyId.value === id ? null : id;
    };

    // Submit top-level reply
    const handleReplySubmit = async (commentId) => {
      const text = replyTexts.value[commentId];
      if (!text) {
        showCustomAlert('Validation Error', 'Reply text cannot be empty.');
        return;
      }

      const payload = { replyText: text };

      try {
        await axios.post(
          `http://localhost:8080/api/chat/comment/${commentId}/reply/post`,
          payload,
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              'Content-Type': 'application/json',
            },
          }
        );
        // Clear local text + reload
        replyTexts.value[commentId] = '';
        activeReplyId.value = null;
        window.location.reload();
      } catch (error) {
        console.error('Error submitting reply:', error.response?.data);
        showCustomAlert('Reply Error', 'Could not submit your reply. Please try again.');
      }
    };

    // Submit nested reply
    const handleNestedReplySubmit = async (replyId) => {
      const text = replyTexts.value[replyId];
      if (!text) {
        showCustomAlert('Validation', 'Reply text cannot be empty');
        return;
      }

      const payload = { replyText: text };

      try {
        await axios.post(
          `http://localhost:8080/api/chat/reply/${replyId}/reply/post`,
          payload,
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              'Content-Type': 'application/json',
            },
          }
        );
        // Clear local text + reload
        replyTexts.value[replyId] = '';
        activeReplyId.value = null;
        window.location.reload();
      } catch (error) {
        console.error('Error submitting nested reply:', error.response?.data);
        showCustomAlert('Nested Reply Error', 'Could not submit your nested reply. Please try again.');
      }
    };

    // Handle upvotes from <CommentAction>
    const handleUpvotes = (eventData) => {
      if (eventData.commentId) {
        const c = comments.value.find((x) => x.commentId === eventData.commentId);
        if (c) c.upvotes = eventData.updatedUpvotes;
      }

      if (eventData.replyId) {
        const updateReplyUpvotes = (replies) => {
          for (const r of replies) {
            if (r.replyId === eventData.replyId) {
              r.upvotes = eventData.updatedUpvotes;
              return true;
            }
            if (r.childReplies && updateReplyUpvotes(r.childReplies)) return true;
          }
          return false;
        };
        comments.value.forEach((c) => {
          if (c.replies) updateReplyUpvotes(c.replies);
        });
      }
    };

    onMounted(async () => {
      await fetchCurrentUserId();
      await fetchComments();

      const handleScroll = async () => {
        const scrollPosition = window.innerHeight + window.scrollY;
        const scrollHeight = document.documentElement.offsetHeight;
        if (scrollPosition >= scrollHeight - 100) {
          await fetchComments();
        }
      };

      window.addEventListener('scroll', handleScroll);
      onUnmounted(() => {
        window.removeEventListener('scroll', handleScroll);
      });
    });

    return {
      comments,
      replyTexts,
      currentUserId,
      currentPage,
      pageSize,
      isLoading,
      isLastPage,
      collapsed,
      activeReplyId,
      showAlert,
      alertData,
      showCustomAlert,

      // Confirm dialog
      showDeleteDialog,
      commentIdToDelete,
      openDeleteDialog,
      confirmDeleteComment,
      cancelDeleteComment,

      // Exposed methods
      toggleCollapse,
      formatTimeAgo,
      fetchComments,
      toggleReplyField,
      handleReplySubmit,
      handleNestedReplySubmit,
      handleUpvotes,
      isOwner,
      handleUpdateReplyText,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-start bg-gray-100">
    <!-- Custom Alert -->
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      :type="alertData.type"
      @close="showAlert = false"
    />

    <!-- Confirm Dialog for deleting a comment -->
    <ConfirmDialog
      v-if="showDeleteDialog"
      title="Confirm Deletion"
      message="Are you sure you want to delete this comment?"
      @confirm="confirmDeleteComment"
      @cancel="cancelDeleteComment"
    />

    <!-- New comment button -->
    <div class="w-full max-w-3xl px-6 py-4 flex justify-end">
      <router-link
        to="/onderwerp/1"
        class="px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
      >
        Place a comment
      </router-link>
    </div>

    <!-- Comments section -->
    <div class="w-full max-w-3xl px-6 py-4">
      <h2 class="text-lg font-medium text-gray-600 dark:text-gray-200">
        Comments
      </h2>

      <div v-if="comments.length === 0" class="text-center text-gray-500 dark:text-gray-400">
        Comments are loading...
      </div>
      <div v-else>
        <div
          v-for="comment in comments"
          :key="comment.commentId"
          class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 flex flex-col"
        >
          <!-- Comment details -->
          <div class="flex justify-between items-center">
            <div>
              <p>{{ comment.userName }}</p>
              <h3 class="text-lg font-bold text-gray-700 dark:text-gray-300">
                {{ comment.commentTitle }}
              </h3>
              <p class="text-gray-600 dark:text-gray-200">
                {{ comment.commentText }}
              </p>
              <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">
                {{ formatTimeAgo(comment.createdAt) }}
              </small>
            </div>

            <!-- Upvote -->
            <CommentAction
              :upvotesCount="comment.upvotes"
              :commentId="comment.commentId"
              @update-upvotes="handleUpvotes"
              :showUpvote="true"
              :showReport="false"
            />
          </div>

          <!-- Collapse replies button -->
          <button
            @click="toggleCollapse(comment.commentId)"
            class="mt-2 text-blue-500 hover:underline"
          >
            {{ collapsed[comment.commentId] ? "Show replies" : "Show less" }}
          </button>

          <!-- Reply & Delete -->
          <div class="mt-4">
            <button
              class="text-blue-500 hover:underline mr-4"
              @click="toggleReplyField(comment.commentId)"
            >
              Reply
            </button>
            <button
              v-if="isOwner(comment)"
              class="text-red-500 hover:underline mr-4"
              @click="openDeleteDialog(comment.commentId)"
            >
              Delete
            </button>

            <!-- If replying to this comment, show text area -->
            <div v-if="activeReplyId === comment.commentId" class="mt-2">
              <textarea
                v-model="replyTexts[comment.commentId]"
                placeholder="Write a reply..."
                class="w-full p-2 border rounded-lg resize-none"
              />
              <button
                class="mt-2 px-4 py-2 bg-blue-500 text-white font-medium rounded-lg"
                @click="handleReplySubmit(comment.commentId)"
              >
                Reply
              </button>
            </div>
          </div>

          <!-- Show <ReplyList> if not collapsed -->
          <div v-if="!collapsed[comment.commentId]" class="mt-4">
            <ReplyList
              :replies="comment.replies || []"
              :replyTexts="replyTexts"
              :activeReplyId="activeReplyId"
              :commentId="comment.commentId"
              :currentUserId="currentUserId"
              @toggle-reply-field="toggleReplyField"
              @submit-nested-reply="handleNestedReplySubmit"
              @update-reply-text="handleUpdateReplyText"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.min-h-screen {
  min-height: 100vh;
}

.bg-gray-100 {
  background-color: #111827;
}

textarea {
  background-color: #1f2937;
  color: #f9fafb;
  border-color: #374151;
}

textarea::placeholder {
  color: #9ca3af;
}

textarea:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5);
}

button,
.router-link-active {
  transition: background-color 0.3s ease;
}
</style>
