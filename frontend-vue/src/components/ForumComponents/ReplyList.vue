<script>
import { ref, defineComponent } from "vue";
import axios from "axios";
import CommentAction from "@/components/ForumComponents/CommentAction.vue";
import { formatDistanceToNow } from "date-fns";
import { nl } from "date-fns/locale";

export default defineComponent({
  name: "ReplyList",
  components: {
    CommentAction,
  },
  props: {
    replies: {
      type: Array,
      required: true,
      default: () => [],
    },
    replyTexts: {
      type: Object,
      required: false,
      default: () => ({}),
    },
    activeReplyId: {
      type: [String, Number, null],
      required: true,
    },
    commentId: {
      type: Number,
      required: true,
    },
    currentUserId: {
      type: Number,
      required: true,
    },
  },
  emits: [
    "toggle-reply-field",
    "submit-nested-reply",
    "delete-reply",
    "update-reply-text",
    "update-replies",
  ],
  setup(props, { emit }) {
    // Format time in "time ago" format
    const formatTimeAgo = (date) => {
      return formatDistanceToNow(new Date(date), { addSuffix: true, locale: nl });
    };

    // Update the upvotes for a specific reply
    const updateReplyUpvotes = (newUpvotes) => {
      const replyIndex = props.replies.findIndex(
        (r) => r.replyId === newUpvotes.replyId
      );
      if (replyIndex !== -1) {
        const updatedReplies = [...props.replies];
        updatedReplies[replyIndex].upvotes = newUpvotes.updatedUpvotes;
        emit("update-replies", updatedReplies);
      }
    };

    // Check if the current user is the owner of the reply
    const isOwner = (reply) => {
      return reply.userId === props.currentUserId;
    };

    // Called when a <CommentAction> “delete-item” event fires
    const onDeleteItem = (payload) => {
      // payload = { replyId, commentId }
      if (payload.replyId) {
        const updated = props.replies.filter((r) => r.replyId !== payload.replyId);
        emit("update-replies", updated);
      }
    };

    return {
      formatTimeAgo,
      updateReplyUpvotes,
      isOwner,
      onDeleteItem,
    };
  },
});
</script>

<template>
  <div v-if="replies && replies.length > 0">
    <!-- Reverse so newest replies appear on top -->
    <div
      v-for="reply in replies.slice().reverse()"
      :key="reply.replyId"
      class="mt-4 p-4 bg-white border rounded-lg dark:bg-gray-800 dark:border-gray-600 flex flex-col"
    >
      <!-- Reply info + actions -->
      <div class="flex items-center justify-between">
        <div>
          <strong>{{ reply.userName }}</strong>
          <p>{{ reply.replyText }}</p>
          <small class="block text-sm text-gray-500 dark:text-gray-400">
            {{ formatTimeAgo(reply.createdAt) }}
          </small>
        </div>
        <!-- Upvote/Report/Delete actions -->
        <div class="flex items-center space-x-4">
          <CommentAction
            :upvotesCount="reply.upvotes"
            :replyId="reply.replyId"
            :commentId="commentId"
            @update-upvotes="updateReplyUpvotes"
            :showUpvote="true"
            :showDelete="isOwner(reply)"
            :showReport="true"
            @delete-item="onDeleteItem"
          />
          <!-- Nested Reply button -->
          <button
            @click="$emit('toggle-reply-field', reply.replyId)"
            class="text-blue-500 hover:underline"
          >
            Reply
          </button>
        </div>
      </div>

      <!-- Nested reply input, only shown if activeReplyId matches this reply -->
      <div v-if="activeReplyId === reply.replyId" class="mt-2">
        <textarea
          :value="replyTexts[reply.replyId]"
          @input="$emit('update-reply-text', { replyId: reply.replyId, text: $event.target.value })"
          placeholder="Write a reply..."
          class="w-full p-2 border rounded dark:bg-gray-800 dark:border-gray-600 dark:placeholder-gray-400"
        />
        <button
          @click="$emit('submit-nested-reply', reply.replyId)"
          class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-400"
        >
          Reply
        </button>
      </div>

      <!-- Child replies (recursive) -->
      <div
        v-if="reply.childReplies && reply.childReplies.length"
        class="mt-4 pl-4 border-l dark:border-gray-700"
      >
        <ReplyList
          :replies="reply.childReplies"
          :replyTexts="replyTexts"
          :activeReplyId="activeReplyId"
          :commentId="commentId"
          :currentUserId="currentUserId"
          @toggle-reply-field="$emit('toggle-reply-field', $event)"
          @submit-nested-reply="$emit('submit-nested-reply', $event)"
          @delete-reply="$emit('delete-reply', $event)"
          @update-reply-text="$emit('update-reply-text', $event)"
          @update-replies="$emit('update-replies', $event)"
        />
      </div>
    </div>
  </div>
  <div v-else>
    <p>No comments available</p>
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
