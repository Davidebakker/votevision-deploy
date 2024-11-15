<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from "vue-router";

export default {
  setup() {
    const comments = ref([]);
    const replyTexts = ref({});
    const jwtToken = localStorage.getItem('jwtToken');
    const activeReplyCommentId = ref(null); // Track only one active reply field at a time
    const route = useRoute();
    const onderwerpNummer = route.params.onderwerpNummer;

    const fetchComments = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/chat/comments` ,
            {
              headers: {
                Authorization: `Bearer ${jwtToken}`,
                'Content-Type': 'application/json',
              },
            });
        // console.log("Fetched comments:", response.data); // Debug line
        comments.value = response.data.reverse();
      } catch (error) {
        console.error('Error fetching comments:', error);
      }
    };

    const toggleReplyField = (commentId) => {
      // console.log("Toggling reply for comment ID:", commentId); // Debug line
      if (activeReplyCommentId.value === commentId) {
        activeReplyCommentId.value = null;
      } else {
        activeReplyCommentId.value = commentId;
      }
      console.log("Active Reply Comment ID:", activeReplyCommentId.value); // Debug line
    };

    const handleReplySubmit = async (commentId) => {
      if (!replyTexts.value[commentId]) {
        alert('Reply text cannot be empty');
        return;
      }

      const payload = {
        replyText: replyTexts.value[commentId], // Gebruik "replyText" zoals de backend verwacht
      };

      const headers = {
        Authorization: `Bearer ${jwtToken}`,
        'Content-Type': 'application/json',
      };

      console.log("Sending payload:", payload);
      console.log("Sending headers:", headers);

      try {
        const response = await axios.post(
            `http://localhost:8080/api/chat/comment/${commentId}/reply/post`,
            payload,
            { headers }
        );
        console.log("Reply posted successfully:", response.data);

        replyTexts.value[commentId] = ''; // Clear input after posting
        activeReplyCommentId.value = null; // Close reply field after submission
        fetchComments(); // Refresh comments to include the new reply
      } catch (error) {
        console.error("Error response:", error.response.data);
        alert(error.response?.data || "An error occurred while posting the reply.");
      }
    };


    onMounted(() => {
      fetchComments();
    });

    return {
      comments,
      replyTexts,
      activeReplyCommentId,
      toggleReplyField,
      handleReplySubmit,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-start bg-gray-100">
    <!-- Header met knop -->
    <div class="w-full max-w-3xl px-6 py-4 flex justify-end">
      <router-link to="/onderwerp/1"
                   class="px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50">
        Plaats comment
      </router-link>
    </div>

    <!-- Comments lijst -->
    <div class="w-full max-w-3xl px-6 py-4">
      <h2 class="text-lg font-medium text-gray-600 dark:text-gray-200">Comments</h2>
      <div v-if="comments.length === 0" class="text-center text-gray-500 dark:text-gray-400">Er zijn nog geen comments.</div>
      <div v-else>
        <div v-for="comment in comments" :key="comment.commentId" class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600">
          <p class="text-gray-600 dark:text-gray-200">{{ comment.commentText }}</p>
          <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">Geplaatst op: {{ new Date(comment.createdAt).toLocaleString() }}</small>

          <!-- Display replies -->
          <div v-if="comment.replies && comment.replies.length" class="mt-4">
            <h5 class="font-medium text-gray-500 dark:text-gray-400">Reacties:</h5>
            <div v-for="reply in comment.replies" :key="reply.id" class="p-2 border-t dark:border-gray-700">
              <p class="text-gray-600 dark:text-gray-200">{{ reply.text }}</p>
              <small class="block text-sm text-gray-500 dark:text-gray-400">Geplaatst op: {{ new Date(reply.createdAt).toLocaleString() }}</small>
            </div>
          </div>

          <!-- Button to open reply field -->
          <button @click="toggleReplyField(comment.commentId)" class="mt-2 text-blue-500 hover:underline">
            Reageer
          </button>

          <!-- Add a reply (conditionally rendered) -->
          <div v-if="activeReplyCommentId === comment.commentId" class="mt-2">
            <textarea v-model="replyTexts[comment.commentId]" placeholder="Schrijf een reactie..." class="w-full p-2 border rounded dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400"></textarea>
            <button @click="handleReplySubmit(comment.commentId)" class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-400">
              Reageren
            </button>
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
button, .router-link-active {
  transition: background-color 0.3s ease;
}
</style>
