<script>
import { ref } from 'vue';

export default {
  setup() {
    const comments = ref([]);

    if (localStorage.getItem('comments')) {
      comments.value = JSON.parse(localStorage.getItem('comments')).reverse();
    }

    return {
      comments,
    };
  },
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-md mx-auto overflow-hidden bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="px-6 py-4">
        <h2 class="mt-3 text-xl font-medium text-center text-gray-600 dark:text-gray-200">
          Maak een nieuwe post
        </h2>

        <form @submit.prevent="submitComment" class="mt-4">
          <textarea
              v-model="newComment.commentText"
              placeholder="Schrijf je post..."
              class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              required
          ></textarea>

          <div class="flex justify-between mt-4">
            <button
                type="submit"
                class="w-4/5 px-4 py-2 bg-red-500 text-white font-medium rounded-lg hover:bg-red-600 focus:outline-none focus:ring focus:ring-red-300 focus:ring-opacity-50 mr-2"
            >
              Posten
            </button>

            <button
                type="button"
                @click="cancelPost"
                class="w-1/5 px-4 py-2 bg-gray-500 text-white font-medium rounded-lg hover:bg-gray-400 focus:outline-none focus:ring focus:ring-gray-300 focus:ring-opacity-50"
            >
              Annuleren
            </button>
          </div>
        </form>
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
