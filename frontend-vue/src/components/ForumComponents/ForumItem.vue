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
        replyText: replyTexts.value[commentId],
      };

      const headers = {
        Authorization: `Bearer ${jwtToken}`,
        'Content-Type': 'application/json',
      };

      try {
        const response = await axios.post(
            `http://localhost:8080/api/chat/comment/${commentId}/reply/post`,
            payload,
            { headers }
        );

        const newReply = response.data; // De nieuwe reply van de backend
        console.log("New reply received:", newReply);

        // Voeg de nieuwe reply toe aan de juiste comment
        const comment = comments.value.find(c => c.commentId === commentId);
        if (comment) {
          comment.replies = comment.replies || []; // Zorg dat er een replies-array is
          comment.replies.push(newReply); // Voeg de nieuwe reply toe
        }

        replyTexts.value[commentId] = ''; // Clear input after posting
        activeReplyCommentId.value = null; // Close reply field after submission
      } catch (error) {
        console.error("Error response:", error.response.data);
        alert(error.response?.data || "An error occurred while posting the reply.");
      }
    };

    const handleNestedReplySubmit = async (replyId) => {
      if (!replyTexts.value[replyId]) {
        alert('Reply text cannot be empty');
        return;
      }

      const payload = {
        replyText: replyTexts.value[replyId],
      };

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

        // Zoek de parent reply en voeg de nested reply toe
        const findNestedReply = (replies) => {
          for (const reply of replies) {
            if (reply.replyId === replyId) {
              reply.childReplies = reply.childReplies || [];
              reply.childReplies.push(newNestedReply);
              return true;
            }
            if (reply.childReplies) {
              if (findNestedReply(reply.childReplies)) return true;
            }
          }
          return false;
        };

        comments.value.forEach(comment => findNestedReply(comment.replies || []));

        replyTexts.value[replyId] = ''; // Clear input
        activeReplyCommentId.value = null; // Close reply field
      } catch (error) {
        console.error("Error posting nested reply:", error.response?.data);
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
      handleNestedReplySubmit,

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
      <div v-if="comments.length === 0" class="text-center text-gray-500 dark:text-gray-400">
        Er zijn nog geen comments.
      </div>
      <div v-else>
        <div v-for="comment in comments" :key="comment.commentId" class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600">
          <!-- Titel en tekst van de comment -->
          <p>Geplaatst door: {{ comment.userName }}</p>
          <h3 class="text-lg font-bold text-gray-700 dark:text-gray-300">{{ comment.commentTitle }}</h3>
          <p class="text-gray-600 dark:text-gray-200">{{ comment.commentText }}</p>
          <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">
            Geplaatst op: {{ new Date(comment.createdAt).toLocaleString() }}
          </small>

          <!-- Button to toggle reply field for comments -->
          <button @click="toggleReplyField(comment.commentId)" class="mt-4 text-blue-500 hover:underline">
            Reageer op deze comment
          </button>

          <!-- Reply field for comments -->
          <div v-if="activeReplyCommentId === comment.commentId" class="mt-4">
            <textarea
                v-model="replyTexts[comment.commentId]"
                placeholder="Schrijf een reactie..."
                class="w-full p-2 border rounded dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400"
            ></textarea>
            <button
                @click="handleReplySubmit(comment.commentId)"
                class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-400"
            >
              Reageren
            </button>
          </div>

          <!-- Replies -->
          <div v-if="comment.replies && comment.replies.length" class="mt-6 pl-4 border-l dark:border-gray-700">
            <h5 class="font-medium text-gray-500 dark:text-gray-400">Reacties:</h5>
            <div v-for="reply in comment.replies" :key="reply.replyId" class="mt-4">
              <p>Geplaatst door: {{ reply.userName }}</p>
              <p class="text-gray-600 dark:text-gray-200">{{ reply.replyText }}</p>
              <small class="block text-sm text-gray-500 dark:text-gray-400">
                Geplaatst op: {{ new Date(reply.createdAt).toLocaleString() }}
              </small>

              <!-- Button to reply to a reply -->
              <button @click="toggleReplyField(reply.replyId)" class="mt-2 text-blue-500 hover:underline">
                Reageer op deze reactie
              </button>

              <!-- Reply field for a reply -->
              <div v-if="activeReplyCommentId === reply.replyId" class="mt-2">
                <textarea
                    v-model="replyTexts[reply.replyId]"
                    placeholder="Schrijf een reactie..."
                    class="w-full p-2 border rounded dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400"
                ></textarea>
                <button
                    @click="handleNestedReplySubmit(reply.replyId)"
                    class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-400"
                >
                  Reageren
                </button>
              </div>

              <!-- Nested replies -->
              <div v-if="reply.childReplies && reply.childReplies.length" class="mt-4 pl-4 border-l dark:border-gray-700">
                <h6 class="font-medium text-gray-500 dark:text-gray-400">Reacties op deze reactie:</h6>
                <div v-for="nestedReply in reply.childReplies" :key="nestedReply.replyId" class="mt-2">
                  <p>Geplaatst door: {{ nestedReply.userName }}</p>
                  <p class="text-gray-600 dark:text-gray-200">{{ nestedReply.replyText }}</p>
                  <small class="block text-sm text-gray-500 dark:text-gray-400">
                    Geplaatst op: {{ new Date(nestedReply.createdAt).toLocaleString() }}
                  </small>
                </div>
              </div>
            </div>
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
