<script>
import CommentAction from "@/components/ForumComponents/CommentAction.vue";
import { formatDistanceToNow } from "date-fns";
import { nl } from "date-fns/locale"; // Nederlandse vertaling voor tijd

export default {
  name: 'ReplyList',
  components: {
    CommentAction,
  },
  props: {
    replies: {
      type: Array,
      required: true,
      default: () => [], // Zorg voor een lege array als standaard
    },
    replyTexts: {
      type: Object,
      required: true,
      default: () => ({}), // Zorg voor een leeg object als standaard
    },
    activeReplyId: {
      type: [String, Number, null],
      required: true,
    },
    commentId: {
      type: Number,
      required: true,
    },
  },

  emits: ["toggle-reply-field", "submit-nested-reply"],
  methods: {
    formatTimeAgo(date) {
      return formatDistanceToNow(new Date(date), { addSuffix: true, locale: nl });
    },
    updateReplyUpvotes(newUpvotes) {
      // Zoek de specifieke reply en update zijn upvotes
      const replyIndex = this.replies.findIndex(
          (reply) => reply.replyId === newUpvotes.replyId
      );
      if (replyIndex !== -1) {
        // eslint-disable-next-line vue/no-mutating-props
        this.replies[replyIndex].upvotes = newUpvotes.updatedUpvotes;
      }
    },
  },
};
</script>

<template>
  <div v-if="replies && replies.length > 0">
    <div
        v-for="reply in replies.slice().reverse()"
        :key="reply.replyId"
        class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 flex flex-col"
    >
      <!-- Bovenste rij: reply text links, upvote rechts -->
      <div class="flex items-center justify-between">
        <div>
          <strong>{{ reply.userName }}</strong>
          <p>{{ reply.replyText }}</p>
          <small class="block text-sm text-gray-500 dark:text-gray-400">
            {{ formatTimeAgo(reply.createdAt) }}
          </small>
        </div>
        <!-- Upvote knop rechtsboven -->
        <CommentAction
            :upvotesCount="reply.upvotes"
            :replyId="reply.replyId"
            :commentId="commentId"
            @update-upvotes="updateReplyUpvotes"
            :showUpvote="true"
            :showReport="false"
        />
      </div>

      <!-- Onderste rij: Reply en Report samen links -->
      <div class="mt-2 flex items-center space-x-4">
        <button
            @click="$emit('toggle-reply-field', reply.replyId)"
            class="text-blue-500 hover:underline"
        >
          Reply
        </button>
        <!-- Alleen Report tonen in deze CommentAction -->
        <CommentAction
            :upvotesCount="reply.upvotes"
            :replyId="reply.replyId"
            :commentId="commentId"
            @update-upvotes="updateReplyUpvotes"
            :showUpvote="false"
            :showReport="true"
        />
      </div>

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
          Reply
        </button>
      </div>

      <!-- Child replies -->
      <div v-if="reply.childReplies && reply.childReplies.length" class="mt-4 pl-4 border-l dark:border-gray-700">
        <ReplyList
            :replies="reply.childReplies || []"
            :replyTexts="replyTexts"
            :activeReplyId="activeReplyId"
            :commentId="commentId"
            @toggle-reply-field="$emit('toggle-reply-field', $event)"
            @submit-nested-reply="$emit('submit-nested-reply', reply.replyId)"
        />
      </div>
    </div>
  </div>
  <div v-else>
    <p>Geen reacties beschikbaar</p>
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
