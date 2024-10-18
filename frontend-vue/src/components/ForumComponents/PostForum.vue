<script>
import { ref } from "vue";
import { useRoute } from 'vue-router';
import axios from 'axios';
import { onMounted } from 'vue'


export default {
  setup() {
    const newPost = ref({
      title: "",
      content: "",
    });

    const posts = ref([
      // Voorbeeldberichten
      {
        title: "Welkom op het forum",
        content: "Dit is het eerste bericht op ons nieuwe forum!",
        date: new Date().toLocaleString(),
      },
    ]);

    const addPost = () => {
      if (newPost.value.title && newPost.value.content) {
        posts.value.push({
          ...newPost.value,
          date: new Date().toLocaleString(),
        });
        // Reset het formulier
        newPost.value.title = "";
        newPost.value.content = "";
      }
    };

    return {
      newPost,
      posts,
      addPost,
    };
  },
};
</script>

<template>

  <div class="forum">
    <h1>Forum</h1>

    <div class="new-post">
      <h2>Plaats een nieuw bericht</h2>
      <form @submit.prevent="addPost">
        <input
            v-model="newPost.title"
            type="text"
            placeholder="Titel"
            required
        />
        <textarea
            v-model="newPost.content"
            placeholder="Inhoud van het bericht"
            required
        ></textarea>
        <button type="submit">Plaats bericht</button>
      </form>
    </div>

    <!-- List of posts -->
    <div class="posts">
      <h2>Berichten</h2>
      <div v-if="posts.length === 0">Er zijn nog geen berichten.</div>
      <div v-else>
        <div
            v-for="(post, index) in posts"
            :key="index"
            class="post"
        >
          <h3>{{ post.title }}</h3>
          <p>{{ post.content }}</p>
          <small>Geplaatst op: {{ post.date }}</small>
        </div>
      </div>
    </div>
  </div>
</template>



<style scoped>
.forum {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.new-post form {
  display: flex;
  flex-direction: column;
  gap: 10px;
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

.posts .post h3 {
  margin: 0 0 10px;
}

.posts .post p {
  margin: 0 0 5px;
}

.posts .post small {
  color: #888;
}
</style>
