<script>
import { ref, onMounted, onUnmounted } from 'vue'
import axios from 'axios'
import ReplyList from './ReplyList.vue'
import CommentAction from '@/components/ForumComponents/CommentAction.vue'
import { formatDistanceToNow } from 'date-fns'
import { nl } from 'date-fns/locale'

export default {
  components: { ReplyList, CommentAction },

  setup() {
    const comments = ref([]) // Opslag van alle comments
    const replyTexts = ref({}) // opslag van tekstvelden per reactie
    const currentPage = ref(0); // huidige pagina voor paginastie
    const pageSize = ref(10); // Aantal comments per pagina
    const isLoading = ref(false); // Controleer laadstatus
    const isLastPage = ref(false); // Controleer of er meer pagina's zijn/laatste is bereikt
    const collapsed = ref([]) // status (ingeklapt/uitgeklapt) per CommentID
    const activeReplyId = ref(null) // actieve reply ID voor tekstvelden
    const jwtToken = localStorage.getItem('jwtToken') // haal JWT-token op

    // toggle collapsibil
    const toggleCollapse = (commentId) => {
      collapsed.value[commentId] = !collapsed.value[commentId] // Inklappen of uitklappen
    }


    const fetchComments = async () => {
      // Controleer eerst of data al wordt geladen of dat er geen nieuwe pagina's meer zijn
      if (isLoading.value || isLastPage.value) return;

      isLoading.value = true; // Zet laadstatus aan

      try {
        // Haal de comments op van de backend, met paginering
        const response = await axios.get(`http://localhost:8080/api/chat/comments`, {
          headers: {
            Authorization: `Bearer ${jwtToken}`, // Geauthenticeerd met JWT token
            'Content-Type': 'application/json',
          },
          params: {
            page: currentPage.value, // Pagina van data die wordt opgevraagd
            size: pageSize.value,    // Hoeveelheid comments per pagina
          },
        });

        // Voeg nieuwe comments toe aan de lijst
        comments.value = [...comments.value, ...response.data.content];

        // Update `collapsed.value` voor elke nieuwe comment (standaard: ingeklapt)
        response.data.content.forEach((comment) => {
          collapsed.value[comment.commentId] = true; // Voeg ingeklapte status toe voor nieuwe comments
        });

        // Controleer of dit de laatste pagina is
        isLastPage.value = response.data.last;

        // Verhoog de huidige paginawaarde
        currentPage.value += 1;
      } catch (error) {
        console.error('Error fetching comments:', error);
      } finally {
        isLoading.value = false; // Zet laadstatus weer uit
      }
    };
    const toggleReplyField = (id) => {
      activeReplyId.value = activeReplyId.value === id ? null : id
    }

    const handleReplySubmit = async (commentId) => {
      if (!replyTexts.value[commentId]) {
        alert('Reply text cannot be empty')
        return
      }

      const payload = { replyText: replyTexts.value[commentId] }

      try {
        const response = await axios.post(
          `http://localhost:8080/api/chat/comment/${commentId}/reply/post`,
          payload,
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              'Content-Type': 'application/json'
            }
          }
        )
        console.log('Comment ID in ForumItem:', comment.commentId)

        const newReply = response.data
        const comment = comments.value.find((c) => c.commentId === commentId)
        if (comment) {
          comment.replies = comment.replies || []
          comment.replies.unshift(newReply) // Voeg nieuwe reply bovenaan toe
        }

        replyTexts.value[commentId] = ''
        activeReplyId.value = null
      } catch (error) {
        console.error('Error submitting reply:', error.response?.data)
      }
    }

    const formatTimeAgo = (date) => {
      return formatDistanceToNow(new Date(date), { addSuffix: true, locale: nl })
    }

    const handleNestedReplySubmit = async (replyId) => {
      if (!replyTexts.value[replyId]) {
        alert('Reply text cannot be empty')
        return
      }

      const payload = { replyText: replyTexts.value[replyId] }

      try {
        const response = await axios.post(
          `http://localhost:8080/api/chat/reply/${replyId}/reply/post`,
          payload,
          {
            headers: {
              Authorization: `Bearer ${jwtToken}`,
              'Content-Type': 'application/json'
            }
          }
        )

        const newNestedReply = response.data

        const addNestedReply = (replies, replyId, newNestedReply) => {
          for (const reply of replies) {
            if (reply.replyId === replyId) {
              reply.childReplies = reply.childReplies || []
              reply.childReplies.unshift(newNestedReply) // Voeg nieuwe nested reply bovenaan toe
              return true
            }
            if (reply.childReplies) {
              if (addNestedReply(reply.childReplies, replyId, newNestedReply)) return true
            }
          }
          return false
        }

        comments.value.forEach((comment) => {
          addNestedReply(comment.replies || [], replyId, newNestedReply)
        })

        replyTexts.value[replyId] = ''
        activeReplyId.value = null
      } catch (error) {
        console.error('Error submitting nested reply:', error.response?.data)
      }
    }
    const handleUpvotes = (eventData) => {
      // eventData bevat: { replyId, commentId, updatedUpvotes }
      // Zoek de juiste comment of reply en update de waarde lokaal.

      if (eventData.commentId) {
        // Update een comment
        const comment = comments.value.find((c) => c.commentId === eventData.commentId)
        if (comment) {
          comment.upvotes = eventData.updatedUpvotes
        }
      }

      if (eventData.replyId) {
        // Update een reply
        // Je zal hiervoor door de replies heen moeten gaan.
        const updateReplyUpvotes = (replies) => {
          for (const reply of replies) {
            if (reply.replyId === eventData.replyId) {
              reply.upvotes = eventData.updatedUpvotes
              return true
            }
            if (reply.childReplies && updateReplyUpvotes(reply.childReplies)) {
              return true
            }
          }
          return false
        }

        comments.value.forEach((comment) => {
          if (comment.replies) {
            updateReplyUpvotes(comment.replies)
          }
        })
      }
    }

    onMounted(() => {
      // Haal de eerste pagina comments op
      fetchComments();

      // Voeg een scroll-event listener toe
      window.addEventListener("scroll", async () => {
        try {
          const scrollPosition = window.innerHeight + window.scrollY;
          const scrollHeight = document.documentElement.offsetHeight;

          // Controleer of gebruiker onderaan is
          if (scrollPosition >= scrollHeight - 100) {
            await fetchComments(); // Zorgt voor correcte verwerking
          }
        }catch (error) {
          console.error('Error fetching comments:', error);
        }
      });
    });

    onUnmounted(() => {
      // Verwijder de scroll-event listener wanneer de component wordt vernietigd
      window.removeEventListener("scroll", fetchCommentsAsync);
    });

    return {
      comments,
      collapsed,
      toggleCollapse,
      replyTexts,
      activeReplyId,
      toggleReplyField,
      fetchComments,
      handleReplySubmit,
      handleNestedReplySubmit,
      handleUpvotes,
      formatTimeAgo
    }
  }
}
</script>

<template>
  <div class="min-h-screen flex flex-col items-center justify-start bg-gray-100">
    <!-- Plaats een nieuwe comment -->
    <div class="w-full max-w-3xl px-6 py-4 flex justify-end">
      <router-link
          to="/onderwerp/1"
          class="px-4 py-2 bg-blue-500 text-white font-medium rounded-lg hover:bg-blue-400 focus:outline-none focus:ring focus:ring-blue-300 focus:ring-opacity-50"
      >
        Place a comment
      </router-link>
    </div>

    <!-- Comment-sectie -->
    <div class="w-full max-w-3xl px-6 py-4">
      <h2 class="text-lg font-medium text-gray-600 dark:text-gray-200">Comments</h2>

      <!-- Laadindicatie als er geen comments zijn -->
      <div v-if="comments.length === 0" class="text-center text-gray-500 dark:text-gray-400">
        Comments are loading...
      </div>

      <!-- Toon de comments (v-for loop) -->
      <div v-else>
        <div
            v-for="comment in comments"
            :key="comment.commentId"
            class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 flex flex-col"
        >
          <!-- Comment details -->
          <div class="flex justify-between items-center">
            <div>
              <p>{{ comment.userName }}</p>
              <h3 class="text-lg font-bold text-gray-700 dark:text-gray-300">{{ comment.commentTitle }}</h3>
              <p class="text-gray-600 dark:text-gray-200">{{ comment.commentText }}</p>
              <small class="block mt-2 text-sm text-gray-500 dark:text-gray-400">
                {{ formatTimeAgo(comment.createdAt) }}
              </small>
            </div>

            <!-- Upvote functionaliteit -->
            <CommentAction
                :upvotesCount="comment.upvotes"
                :commentId="comment.commentId"
                @update-upvotes="handleUpvotes"
                :showUpvote="true"
                :showReport="false"
            />
          </div>

          <!-- In- en uitklappen knop -->
          <button
              @click="toggleCollapse(comment.commentId)"
              class="mt-2 text-blue-500 hover:underline"
          >
            {{ collapsed[comment.commentId] ? 'Show replies' : 'Show less' }}
          </button>

          <!-- ReplyList tonen/gebruiken als collapsed false is -->
          <div v-if="!collapsed[comment.commentId]">
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