<template>
<li class="todo-item">
            <input
              type="checkbox"
              :checked="todo.done"
              @change="$emit('toggle', todo)"
            />

            <span :class="{ done: todo.done }">
              {{ todo.text }}
            </span>

            <!-- ⭐ Wichtig Button -->
            <button
              class="star-btn"
              @click="$emit('toggleImportant', todo)"
            >
              {{ todo.important ? '★' : '☆' }}
            </button>
          </li>

</template>

<script setup>
defineProps({
  todo: Object
})
</script>

<style scoped>
.todo-item {
  list-style: none;
  margin: 8px 0;
  padding: 8px;
  background: #ffffff;
  border-radius: 6px;
  color: black;
}
.star-btn {
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  margin-left: auto;
  color: #999;
}

.star-btn:hover {
  color: gold;
}

.star-btn.active {
  color: gold;
}


.done {
  text-decoration: line-through;
  color: blue;
}
</style>
