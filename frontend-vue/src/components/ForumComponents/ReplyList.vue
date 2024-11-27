<script>
import CommentAction from "@/components/ForumComponents/CommentAction.vue";
import { formatDistanceToNow } from "date-fns";
import { nl } from "date-fns/locale"; // Nederlandse vertaling voor tijd

export default {
  name: 'ReplyList',
  components: {
    CommentAction, // Voeg de CommentAction-component toe
  },
  props: {
    replies: {
      type: Array,
      required: true,
    },
    replyTexts: {
      type: Object,
      required: true,
    },
    activeReplyId: {
      type: [String, Number, null],
      required: true,
    },
  },
  emits: ["toggle-reply-field", "submit-nested-reply"],
  methods: {
    formatTimeAgo(date) {
      return formatDistanceToNow(new Date(date), {addSuffix: true, locale: nl});
    },
  },
};
</script>

<template>
  <div>
    <div v-for="reply in replies.slice().reverse()" :key="reply.replyId" class="mt-4">
      <!-- Reply details -->
      <p>Geplaatst door: {{ reply.userName }}</p>
      <p class="text-gray-600 dark:text-gray-200">{{ reply.replyText }}</p>
      <small class="block text-sm text-gray-500 dark:text-gray-400">
        {{ formatTimeAgo(reply.createdAt) }}
      </small>
      <!-- Reply button -->
      <button @click="$emit('toggle-reply-field', reply.replyId)" class="mt-2 text-blue-500 hover:underline">
        Reply
      </button>
      <!-- comment action-->
      <CommentAction :upvotesCount="reply.upvotes || 0" :replyId="reply.replyId" />
      <!-- Reply input field -->
      <div v-if="activeReplyId === reply.replyId" class="mt-2">
        <textarea
            v-model="replyTexts[reply.replyId]"
            placeholder="Schrijf een reactie..."
            class="w-full p-2 border rounded dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400"
        ></textarea>
        <button
            @click="$emit('submit-nested-reply', reply.replyId)"
            class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-400"
        >
          Reageren
        </button>
      </div>

      <!-- Recursive rendering of child replies -->
      <div v-if="reply.childReplies && reply.childReplies.length" class="mt-4 pl-4 border-l dark:border-gray-700">
        <ReplyList
            :replies="reply.childReplies"
            :replyTexts="replyTexts"
            :activeReplyId="activeReplyId"
            @toggle-reply-field="$emit('toggle-reply-field', $event)"
            @submit-nested-reply="$emit('submit-nested-reply', $event)"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.mt-4 {
  margin-top: 1rem;
}
.pl-4 {
  padding-left: 1rem;
}
</style>
