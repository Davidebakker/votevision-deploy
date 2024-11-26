<script>
import { ref, onMounted } from 'vue';
import axios from 'axios'

;

export default {
  setup() {
    const comments = ref([]);


    const fetchComments = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/chat/comments');
        comments.value = response.data.reverse();
      } catch (error) {
        console.error('Error fetching comments:', error);
      }
    };

    onMounted(() => {
      fetchComments();
    });

    return {
      comments,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-start bg-gray-100">
    <!-- Header met knop -->
    <div class="w-full max-w-3xl px-6 py-4 flex justify-end">
      <router-link
        to="/onderwerp/1"
      class="px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
      >
        Plaats comment
      </router-link>
    </div>

    <!-- Comments lijst -->
    <div class="w-full max-w-3xl px-6 py-4">
      <h2 class="text-lg font-medium text-gray-600 dark:text-gray-200">Comments</h2>
      <div v-if="comments.length === 0" class="text-center text-gray-500 dark:text-gray-400">Er zijn nog geen comments.</div>
      <div v-else>
        <div
            v-for="(comment, index) in comments"
            :key="index"
            class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600"
        >
          <p class="text-gray-600 dark:text-gray-200">{{ comment.commentText }}</p>
          <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">Geplaatst op: {{ new Date(comment.createdAt).toLocaleString() }}</small>
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
