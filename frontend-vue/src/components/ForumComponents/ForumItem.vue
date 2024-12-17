<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import ReplyList from './ReplyList.vue';
import CommentAction from "@/components/ForumComponents/CommentAction.vue";

export default {
  components: { ReplyList,
    CommentAction,
  },
  setup() {
    const comments = ref([]);
    const replyTexts = ref({});
    const activeReplyId = ref(null); // Tracks active reply field for comments/replies
    const jwtToken = localStorage.getItem('jwtToken');

    const fetchComments = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/chat/comments', {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json',
          },
        });

        comments.value = response.data.reverse(); // Laatste comment bovenaan
      } catch (error) {
        console.error('Error fetching comments:', error);
      }
    };

    const toggleReplyField = (id) => {
      activeReplyId.value = activeReplyId.value === id ? null : id;
    };

    const handleReplySubmit = async (commentId) => {
      if (!replyTexts.value[commentId]) {
        alert('Reply text cannot be empty');
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
                'Content-Type': 'application/json',
              },
            }
        )
        console.log("Comment ID in ForumItem:", comment.commentId);



        const newReply = response.data;
        const comment = comments.value.find((c) => c.commentId === commentId);
        if (comment) {
          comment.replies = comment.replies || [];
          comment.replies.unshift(newReply); // Voeg nieuwe reply bovenaan toe
        }

        replyTexts.value[commentId] = '';
        activeReplyId.value = null;
      } catch (error) {
        console.error('Error submitting reply:', error.response?.data);
      }
    };

    const handleNestedReplySubmit = async (replyId) => {
      if (!replyTexts.value[replyId]) {
        alert('Reply text cannot be empty');
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
                'Content-Type': 'application/json',
              },
            }
        );

        const newNestedReply = response.data;

        const addNestedReply = (replies, replyId, newNestedReply) => {
          for (const reply of replies) {
            if (reply.replyId === replyId) {
              reply.childReplies = reply.childReplies || [];
              reply.childReplies.unshift(newNestedReply); // Voeg nieuwe nested reply bovenaan toe
              return true;
            }
            if (reply.childReplies) {
              if (addNestedReply(reply.childReplies, replyId, newNestedReply)) return true;
            }
          }
          return false;
        };

        comments.value.forEach(comment => {
          addNestedReply(comment.replies || [], replyId, newNestedReply);
        });

        replyTexts.value[replyId] = '';
        activeReplyId.value = null;
      } catch (error) {
        console.error('Error submitting nested reply:', error.response?.data);
      }
    };
    const handleUpvotes = (eventData) => {
      // eventData bevat: { replyId, commentId, updatedUpvotes }
      // Zoek de juiste comment of reply en update de waarde lokaal.

      if (eventData.commentId) {
        // Update een comment
        const comment = comments.value.find(c => c.commentId === eventData.commentId);
        if (comment) {
          comment.upvotes = eventData.updatedUpvotes;
        }
      }

      if (eventData.replyId) {
        // Update een reply
        // Je zal hiervoor door de replies heen moeten gaan.
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

        comments.value.forEach(comment => {
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
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-start bg-gray-100">
    <div class="w-full max-w-3xl px-6 py-4 flex justify-end">
      <router-link
          to="/onderwerp/1"
          class="px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
      >
        Plaats comment
      </router-link>
    </div>
    <div class="w-full max-w-3xl px-6 py-4">
      <h2 class="text-lg font-medium text-gray-600 dark:text-gray-200">Comments</h2>
      <div v-if="comments.length === 0" class="text-center text-gray-500 dark:text-gray-400">
        Er zijn nog geen comments.
      </div>
      <div v-else>
        <div
            v-for="comment in comments"
            :key="comment.commentId"
            class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600"
        >
          <p>{{ comment.userName }}</p>
          <h3 class="text-lg font-bold text-gray-700 dark:text-gray-300">{{ comment.commentTitle }}</h3>
          <p class="text-gray-600 dark:text-gray-200">{{ comment.commentText }}</p>
          <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">
            Geplaatst op: {{ new Date(comment.createdAt).toLocaleString() }}
          </small>
          <div class="mt-2 flex space-x-4">
            <CommentAction
                :upvotesCount="comment.upvotes"
                :commentId="comment.commentId"
                @update-upvotes="handleUpvotes"
            />
            <button @click="toggleReplyField(comment.commentId)" class="mt-4 text-blue-500 hover:underline">
              Reply
            </button>
          </div>
          <div v-if="activeReplyId === comment.commentId" class="mt-4">
            <textarea
                v-model="replyTexts[comment.commentId]"
                placeholder="Schrijf een reactie..."
                class="w-full p-2 border rounded dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400"
            ></textarea>
            <CommentAction
                :upvotesCount="comment.upvotes"
                :commentId="comment.commentId"
                @update-upvotes="handleUpvotes"
            />
            <button
                @click="handleReplySubmit(comment.commentId)"
                class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-400">
              Reageren
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
