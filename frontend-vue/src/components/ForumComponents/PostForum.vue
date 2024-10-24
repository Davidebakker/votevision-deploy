<script>
import { ref } from "vue";
import axios from 'axios';
import { useRoute } from 'vue-router';

export default {
  setup() {
    const title = ref('');
    const content = ref('');

    const posts = ref([]);
    const jwtToken = localStorage.getItem('jwtToken');
    const route = useRoute();
    const onderwerpNummer = route.params.onderwerpNummer;

    const handleSubmit = async () => {
      const newPost = {
        title: title.value,
        content: content.value,
      };

      try {
        console.log(newPost);

        const response = await axios.post(`http://localhost:8080/api/chat/topic/${onderwerpNummer}/comment/post`, newPost, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json',
          },
        });
        console.log(response.data);

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

      } catch (error) {
        console.error(error);
        if (error.response && error.response.data && error.response.data.message) {
          alert(error.response.data.message);
        } else {
          alert('An unexpected error occurred.');
        }
      }
    };


    const cancelPost = () => {
      router.push('/forum');
    };

    return {
      title,
      content,
      posts,
      handleSubmit,
      cancelPost,
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

        <div class="new-post mt-6">
          <h3 class="text-lg font-medium text-center text-gray-600 dark:text-gray-200">Plaats een nieuw bericht</h3>
          <form @submit.prevent="handleSubmit" class="mt-4">
            <input
              v-model="title"
              type="text"
              placeholder="Titel"
              class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              required
            />
            <textarea
              v-model="content"
              placeholder="Inhoud van het bericht"
              class="block w-full px-4 py-2 mt-2 text-white-700 placeholder-gray-500 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400 focus:border-blue-400 dark:focus:border-blue-300 focus:ring-opacity-40 focus:outline-none focus:ring focus:ring-blue-300"
              required
            ></textarea>
            <button
              type="submit"
              class="flex center w-full px-4 py-2 mt-4 text-sm font-medium tracking-wide text-white capitalize transition-colors duration-300 transform bg-blue-500 rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
            >
              Posten
            </button>
          </form>
        </div>

        <!-- List of posts -->
        <div class="posts mt-6">
          <h3 class="text-lg font-medium text-center text-gray-600 dark:text-gray-200">Berichten</h3>
          <div v-if="posts && posts.length === 0" class="text-center text-gray-500 dark:text-gray-400">
            Er zijn nog geen berichten.
          </div>
          <div v-else>
            <div
              v-for="(post, index) in posts"
              :key="index"
              class="post mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600"
            >
              <h4 class="text-lg font-medium text-gray-600 dark:text-gray-200">{{ post.title }}</h4>
              <p class="mt-2 text-gray-600 dark:text-gray-200">{{ post.content }}</p>
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

.new-post input,
.new-post textarea {
  padding: 10px;
  font-size: 16px;
  width: 100%;
  box-sizing: border-box;
}

.new-post button {
  padding: 10px;
  font-size: 16px;
  background-color: #3498db;
  color: white;
  border: none;
  cursor: pointer;
}

.new-post button:hover {
  background-color: #2980b9;
}

.posts .post {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.posts .post h4 {
  margin: 0 0 10px;
}

.posts .post p {
  margin: 0 0 5px;
}

.posts .post small {
  color: #888;
}
</style>
