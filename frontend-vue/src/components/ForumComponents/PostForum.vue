<script>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const title = ref('');
    const content = ref('');

    const posts = ref([]);
    const jwtToken = localStorage.getItem('jwtToken');
    const route = useRoute();
    const onderwerpNummer = route.params.onderwerpNummer;

    const handleSubmit = async () => {
      if (!jwtToken) {
        alert('You must be logged in to post.');
        return;
      }

      const newPost = {
        title: title.value,
        commentText: content.value,
      };

    const submitComment = async () => {
      try {
        console.log('before message: ' + newPost.commentText);

        const response = await axios.post(`http://localhost:8080/api/chat/topic/${onderwerpNummer}/comment/post`, newPost, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json',
          },
        });
        console.log('after message: ' + response.data);

        alert(response.data.message || 'Data submitted successfully');

        // Add the new post/comment to the posts array
        posts.value.push({
          title: newPost.title,
          content: newPost.content,
          date: new Date().toLocaleString(),
        });

        // Clear the input fields after successful submission
        title.value = '';
        content.value = '';

        await router.push('/forum'); //
      } catch (error) {
        console.error('Error posting comment:', error);
      }
    };


    const cancelPost = () => {
      router.push('/forum');
    };

    return {
      newComment,
      submitComment,
      cancelPost,
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