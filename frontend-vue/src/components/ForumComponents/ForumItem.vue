<script>
import { ref, onMounted, onUnmounted } from 'vue'
import axios from 'axios'
import { formatDistanceToNow } from 'date-fns'
import { nl } from 'date-fns/locale'
import ReplyList from './ReplyList.vue'
import CommentAction from '@/components/ForumComponents/CommentAction.vue'

export default {
  components: { ReplyList, CommentAction },

  setup() {
    // *** Reactieve waarden ***
    const comments = ref([]) // Opslag van alle comments
    const replyTexts = ref({}) // Opslag van tekstvelden per reactie
    const currentUserId = ref(null) //waarde voor de ingelogde gebruiker
    const currentPage = ref(0) // Huidige pagina voor paginatie
    const pageSize = ref(10) // Aantal comments per pagina
    const isLoading = ref(false) // Controle laadstatus
    const isLastPage = ref(false) // Controle of er pagina's over zijn
    const collapsed = ref({}) // Ingeklapte status per comment
    const activeReplyId = ref(null) // Actieve reactie-ID
    const jwtToken = localStorage.getItem('jwtToken') // JWT-token ophalen

    // // Controleer of de huidige gebruiker de eigenaar is van de comment
    // const isOwner = (comment) => {
    //   return comment.userId === currentUserId
    // }

    // Toggle collapsible replies voor een gegeven commentId
    const toggleCollapse = (commentId) => {
      collapsed.value[commentId] = !collapsed.value[commentId]
    }

    // Formatteer tijd
    const formatTimeAgo = (date) => {
      return formatDistanceToNow(new Date(date), { addSuffix: true, locale: nl })
    }

    // Fetch alle comments via paginering
    const fetchComments = async () => {
      if (isLoading.value || isLastPage.value) return

      isLoading.value = true

      try {
        const response = await axios.get(`http://localhost:8080/api/chat/comments`, {
          headers: {
            Authorization: `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
          },
          params: {
            page: currentPage.value,
            size: pageSize.value
          }
        })

        comments.value = [...comments.value, ...response.data.content]
        response.data.content.forEach(
          (comment) => (collapsed.value[comment.commentId] = true) // Standaard ingeklapt
        )
        isLastPage.value = response.data.last
        currentPage.value += 1
      } catch (error) {
        console.error('Error fetching comments:', error)
      } finally {
        isLoading.value = false
      }
    }

    // Toggle reply invoerveld
    const toggleReplyField = (id) => {
      activeReplyId.value = activeReplyId.value === id ? null : id
    }

    // Verwerk reply voor een specifieke comment
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

        const newReply = response.data
        const comment = comments.value.find((c) => c.commentId === commentId)
        if (comment) {
          comment.replies = comment.replies || []
          comment.replies.unshift(newReply)
          collapsed.value = { ...collapsed.value, [commentId]: false } // Open nieuwe reply standaard
        }

        replyTexts.value[commentId] = ''
        activeReplyId.value = null
      } catch (error) {
        console.error('Error submitting reply:', error.response?.data)
      }
    }

    // Verwerk geneste reactie
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
              reply.childReplies.unshift(newNestedReply)
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

    // Verwijder een comment of reply
    const handleDelete = async (type, id) => {
      try {
        const url =
          type === 'comment'
            ? `http://localhost:8080/api/chat/comment/${id}`
            : `http://localhost:8080/api/chat/reply/${id}`

        await axios.delete(url, {
          headers: {
            Authorization: `Bearer ${jwtToken}`
          }
        })

        if (type === 'comment') {
          comments.value = comments.value.filter((comment) => comment.commentId !== id)
        } else if (type === 'reply') {
          const removeReply = (replies) =>
            replies.filter((reply) => {
              if (reply.replyId === id) return false
              if (reply.childReplies) reply.childReplies = removeReply(reply.childReplies)
              return true
            })

          comments.value.forEach((comment) => {
            if (comment.replies) {
              comment.replies = removeReply(comment.replies)
            }
          })
        }

        alert(`${type === 'comment' ? 'Comment' : 'Reply'} deleted successfully`)
      } catch (error) {
        console.error('Error deleting:', error.response?.data)
        alert('Failed to delete the item. Please try again.')
      }
    }

    // Update upvotes
    const handleUpvotes = (eventData) => {
      if (eventData.commentId) {
        const comment = comments.value.find((c) => c.commentId === eventData.commentId)
        if (comment) comment.upvotes = eventData.updatedUpvotes
      }

      if (eventData.replyId) {
        const updateReplyUpvotes = (replies) => {
          for (const reply of replies) {
            if (reply.replyId === eventData.replyId) {
              reply.upvotes = eventData.updatedUpvotes
              return true
            }
            if (reply.childReplies && updateReplyUpvotes(reply.childReplies)) return true
          }
          return false
        }

        comments.value.forEach((comment) => {
          if (comment.replies) updateReplyUpvotes(comment.replies)
        })
      }
    }

    // Lifecycle hooks
    onMounted(() => {
      fetchComments()

      const handleScroll = async () => {
        const scrollPosition = window.innerHeight + window.scrollY
        const scrollHeight = document.documentElement.offsetHeight

        if (scrollPosition >= scrollHeight - 100) await fetchComments()
      }

      window.addEventListener('scroll', handleScroll)

      onUnmounted(() => {
        window.removeEventListener('scroll', handleScroll)
      })
    })

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
      formatTimeAgo,
      isOwner,
      handleDelete
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
              <h3 class="text-lg font-bold text-gray-700 dark:text-gray-300">
                {{ comment.commentTitle }}
              </h3>
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

          <!-- 'Delete'-knop tonen indien de gebruiker eigenaar is van de comment -->
          <button
            v-if="isOwner(comment)"
            @click="() => handleDelete('comment', comment.commentId)"
            class="mt-2 px-4 py-1 text-red-500 border rounded-lg hover:bg-red-500 hover:text-white transition"
          >
            Delete
          </button>

          <!-- In- en uitklappen knop -->
          <button
            @click="toggleCollapse(comment.commentId)"
            class="mt-2 text-blue-500 hover:underline"
          >
            {{ collapsed[comment.commentId] ? 'Show replies' : 'Show less' }}
          </button>

          <!-- Optie om te reageren -->
          <div class="mt-4">
            <!-- Reply-knop -->
            <button
              class="text-blue-500 hover:underline"
              @click="toggleReplyField(comment.commentId)"
            >
              Reply
            </button>

            <!-- Tekstveld wordt alleen getoond als de actieve reply ID overeenkomt -->
            <div v-if="activeReplyId === comment.commentId" class="mt-2">
              <textarea
                v-model="replyTexts[comment.commentId]"
                placeholder="Write a reply..."
                class="w-full p-2 border rounded-lg resize-none"
              ></textarea>
              <button
                class="mt-2 px-4 py-2 bg-blue-500 text-white font-medium rounded-lg"
                @click="handleReplySubmit(comment.commentId)"
              >
                Submit Reply
              </button>
            </div>
          </div>

          <!-- ReplyList tonen/gebruiken als collapsed false is -->
          <div v-if="!collapsed[comment.commentId]" class="mt-4">
            <ReplyList
              :replies="comment.replies || []"
              :replyTexts="replyTexts"
              :activeReplyId="activeReplyId"
              :commentId="comment.commentId"
              @toggle-reply-field="toggleReplyField"
              @submit-nested-reply="handleNestedReplySubmit"
              @delete-reply="handleDelete"
            >
            </ReplyList>
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

textarea {
  background-color: #1f2937; /* Donkere achtergrondkleur */
  color: #f9fafb; /* Lichte tekstkleur */
  border-color: #374151; /* Donkerdere randkleur */
}

textarea::placeholder {
  color: #9ca3af; /* Placeholder-tekst in een lichtere kleur */
}

textarea:focus {
  outline: none;
  border-color: #3b82f6; /* Blauw accent wanneer gefocust */
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.5); /* Blauwe focusring */
}

button,
.router-link-active {
  transition: background-color 0.3s ease;
}
</style>
