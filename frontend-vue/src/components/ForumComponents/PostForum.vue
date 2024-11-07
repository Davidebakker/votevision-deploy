<script>
import { ref } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

export default {
  setup() {
    const newComment = ref({ title: '', commentText: '' }); // Define newComment as a ref object
    const posts = ref([]);
    const jwtToken = localStorage.getItem('jwtToken');
    const route = useRoute();
    const onderwerpNummer = route.params.onderwerpNummer;
    const isSubmitting = ref(false);

    const handleSubmit = async () => { // Ensure this matches the template
      if (!jwtToken) {
        alert('You must be logged in to post.');
        return;
      }

      if (!newComment.value.title || !newComment.value.commentText) {
        alert('Please fill in all fields.');
        return;
      }

      isSubmitting.value = true;
      try {
        const response = await axios.post(
          `http://localhost:8080/api/chat/topic/${onderwerpNummer}/comment/post`,
          {
            title: newComment.value.title,
            commentText: newComment.value.commentText,
          },
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              'Content-Type': 'application/json',
            },
          }
        );

        alert(response.data.message || 'Data submitted successfully');

        posts.value.unshift({
          title: newComment.value.title,
          commentText: newComment.value.commentText,
          date: new Date().toLocaleString(),
        });

        newComment.value.title = ''; // Clear the input fields
        newComment.value.commentText = '';
      } catch (error) {
        console.error(error);
        alert(error.response?.data?.message || 'An unexpected error occurred.');
      } finally {
        isSubmitting.value = false;
      }
    };

    const cancelPost = () => {
      newComment.value.title = '';
      newComment.value.commentText = '';
    };

    return {
      newComment,
      posts,
      handleSubmit,
      cancelPost,
      isSubmitting,
    };
  }
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-md mx-auto overflow-hidden bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="px-6 py-4">
        <h2 class="mt-3 text-xl font-medium text-center text-gray-600 dark:text-gray-200">
          Maak een nieuwe post
        </h2>

        <form @submit.prevent="handleSubmit" class="mt-4"> <!-- Update method name -->
          <input
            v-model="newComment.title"
            type="text"
            placeholder="Titel"
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
            required
          />
          <textarea
            v-model="newComment.commentText"
            placeholder="Schrijf je post..."
            class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
            required
          ></textarea>

          <div class="flex mt-4">
            <button
              type="submit"
              class="w-4/5 px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50 mr-2"
            >
              Posten
            </button>

            <button
              type="button"
              @click="cancelPost"
              class="w-1/5 px-4 py-2 bg-red-700 text-white font-small rounded-lg hover:bg-red-900 focus:outline-none focus:ring focus:ring-red-700 focus:ring-opacity-50"
            >
              Cancel
            </button>
          </div>
        </form>

        <div class="posts mt-6">
          <h3 class="text-lg font-medium text-center text-gray-600 dark:text-gray-200">Berichten</h3>
          <div v-if="posts.length === 0" class="text-center text-gray-500 dark:text-gray-400">
            Er zijn nog geen berichten.
          </div>
          <div v-else>
            <div
              v-for="(post, index) in posts"
              :key="index"
              class="post mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600"
            >
              <h4 class="text-lg font-medium text-gray-600 dark:text-gray-200">{{ post.title }}</h4>
              <p class="mt-2 text-gray-600 dark:text-gray-200">{{ post.commentText }}</p>
              <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">Geplaatst op: {{ post.date }}</small>
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
</style>
