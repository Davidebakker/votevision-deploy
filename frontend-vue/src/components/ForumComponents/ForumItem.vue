<script>
import { ref, onMounted } from "vue";
import axios from "axios";
import ReplyList from "./ReplyList.vue";
import CommentAction from "@/components/ForumComponents/CommentAction.vue";
import CustomAlert from "@/components/CustomAlert.vue";
import { formatDistanceToNow } from "date-fns";
import { nl } from "date-fns/locale";

export default {
  components: {
    ReplyList,
    CommentAction,
    CustomAlert,
  },
  setup() {
    const comments = ref([]);
    const replyTexts = ref({});
    const activeReplyId = ref(null);
    const jwtToken = localStorage.getItem("jwtToken");

    const showAlert = ref(false);
    const alertData = ref({
      title: "",
      message: "",
    });

    const fetchComments = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/chat/comments", {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            "Content-Type": "application/json",
          },
        });

        comments.value = response.data.reverse();
      } catch (error) {
        console.error("Error fetching comments:", error);
        alertData.value = {
          title: "Error",
          message: "Failed to load comments. Please try again later.",
        };
        showAlert.value = true;
      }
    };

    const toggleReplyField = (id) => {
      activeReplyId.value = activeReplyId.value === id ? null : id;
    };

    const handleReplySubmit = async (commentId) => {
      if (!replyTexts.value[commentId]) {
        alertData.value = {
          title: "Validation Error",
          message: "Reply text cannot be empty.",
        };
        showAlert.value = true;
        return;
      }

      const payload = { replyText: replyTexts.value[commentId] };

      try {
        const response = await axios.post(
          `http://localhost:8080/api/chat/comment/${commentId}/reply/post`,
          payload,
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              "Content-Type": "application/json",
            },
          }
        );

        const newReply = response.data;
        const comment = comments.value.find((c) => c.commentId === commentId);
        if (comment) {
          comment.replies = comment.replies || [];
          comment.replies.unshift(newReply);
        }

        replyTexts.value[commentId] = "";
        activeReplyId.value = null;

        alertData.value = {
          title: "Success",
          message: "Reply posted successfully.",
        };
        showAlert.value = true;
      } catch (error) {
        console.error("Error submitting reply:", error.response?.data);
        alertData.value = {
          title: "Error",
          message: "Failed to post reply. Please try again.",
        };
        showAlert.value = true;
      }
    };

    const handleNestedReplySubmit = async (replyId) => {
      if (!replyTexts.value[replyId]) {
        alertData.value = {
          title: "Validation Error",
          message: "Reply text cannot be empty.",
        };
        showAlert.value = true;
        return;
      }

      const payload = { replyText: replyTexts.value[replyId] };

      try {
        const response = await axios.post(
          `http://localhost:8080/api/chat/reply/${replyId}/reply/post`,
          payload,
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              "Content-Type": "application/json",
            },
          }
        );

        const newNestedReply = response.data;

        const addNestedReply = (replies, replyId, newNestedReply) => {
          for (const reply of replies) {
            if (reply.replyId === replyId) {
              reply.childReplies = reply.childReplies || [];
              reply.childReplies.unshift(newNestedReply);
              return true;
            }
            if (reply.childReplies) {
              if (addNestedReply(reply.childReplies, replyId, newNestedReply)) return true;
            }
          }
          return false;
        };

        comments.value.forEach((comment) => {
          addNestedReply(comment.replies || [], replyId, newNestedReply);
        });

        replyTexts.value[replyId] = "";
        activeReplyId.value = null;

        alertData.value = {
          title: "Success",
          message: "Reply posted successfully.",
        };
        showAlert.value = true;
      } catch (error) {
        console.error("Error submitting nested reply:", error.response?.data);
        alertData.value = {
          title: "Error",
          message: "Failed to post nested reply. Please try again.",
        };
        showAlert.value = true;
      }
    };

    const formatTimeAgo = (date) => {
      return formatDistanceToNow(new Date(date), { addSuffix: true, locale: nl });
    };

    const handleUpvotes = (eventData) => {
      if (eventData.commentId) {
        const comment = comments.value.find((c) => c.commentId === eventData.commentId);
        if (comment) {
          comment.upvotes = eventData.updatedUpvotes;
        }
      }

      if (eventData.replyId) {
        const updateReplyUpvotes = (replies) => {
          for (const reply of replies) {
            if (reply.replyId === eventData.replyId) {
              reply.upvotes = eventData.updatedUpvotes;
              return true;
            }
            if (reply.childReplies && updateReplyUpvotes(reply.childReplies)) {
              return true;
            }
          }
          return false;
        };

        comments.value.forEach((comment) => {
          if (comment.replies) {
            updateReplyUpvotes(comment.replies);
          }
        });
      }
    };

    onMounted(() => {
      fetchComments();
    });

    return {
      comments,
      replyTexts,
      activeReplyId,
      toggleReplyField,
      handleReplySubmit,
      handleNestedReplySubmit,
      handleUpvotes,
      formatTimeAgo,
      showAlert,
      alertData,
    };
  },
};
</script>


<template>
  <div class="min-h-screen flex flex-col items-center justify-start bg-gray-100">
    <CustomAlert
      v-if="showAlert"
      :title="alertData.title"
      :message="alertData.message"
      @close="showAlert = false"
    />
    <div class="w-full max-w-3xl px-6 py-4 flex justify-end">
      <router-link
          to="/onderwerp/1"
          class="px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
      >
        Place a commen
      </router-link>
    </div>
    <div class="w-full max-w-3xl px-6 py-4">
      <h2 class="text-lg font-medium text-gray-600 dark:text-gray-200">Comments</h2>
      <div v-if="comments.length === 0" class="text-center text-gray-500 dark:text-gray-400">
        Comments are loading...
      </div>
      <div v-else>
        <div
            v-for="comment in comments"
            :key="comment.commentId"
            class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 flex flex-col"
        >
          <!-- Bovenste rij: comment links, upvote rechts -->
          <div class="flex justify-between items-center">
            <div>
              <p>{{ comment.userName }}</p>
              <h3 class="text-lg font-bold text-gray-700 dark:text-gray-300">{{ comment.commentTitle }}</h3>
              <p class="text-gray-600 dark:text-gray-200">{{ comment.commentText }}</p>
              <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">
                {{ formatTimeAgo(comment.createdAt) }}
              </small>
            </div>

            <!-- Upvote alleen hier tonen -->
            <CommentAction
                :upvotesCount="comment.upvotes"
                :commentId="comment.commentId"
                @update-upvotes="handleUpvotes"
                :showUpvote="true"
                :showReport="false"
            />
          </div>

          <!-- Onderste rij: Reply en Report samen -->
          <div class="mt-2 flex items-center space-x-4">
            <button @click="toggleReplyField(comment.commentId)" class="text-blue-500 hover:underline">
              Reply
            </button>

            <!-- Alleen Report knop tonen -->
            <CommentAction
                :upvotesCount="comment.upvotes"
                :commentId="comment.commentId"
                @update-upvotes="handleUpvotes"
                :showUpvote="false"
                :showReport="true"
            />
          </div>

          <!-- Als er gereageerd wordt -->
          <div v-if="activeReplyId === comment.commentId" class="mt-4">
            <textarea
                v-model="replyTexts[comment.commentId]"
                placeholder="Schrijf een reactie..."
                class="w-full p-2 border rounded dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400"
            ></textarea>
            <button
                @click="handleReplySubmit(comment.commentId)"
                class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-400">
              Reply
            </button>
          </div>

          <ReplyList
              :replies="comment.replies || []"
              :replyTexts="replyTexts"
              :activeReplyId="activeReplyId"
              :commentId="comment.commentId"
              @toggle-reply-field="toggleReplyField"
              @submit-nested-reply="handleNestedReplySubmit"
          />
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

button,
.router-link-active {
  transition: background-color 0.3s ease;
}
</style>