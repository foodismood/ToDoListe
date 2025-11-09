<template>
  <li class="todo-item">
    <input type="checkbox" v-model="todo.done" />
    <span :class="{ done: todo.done }">{{ todo.text }}</span>
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

.done {
  text-decoration: line-through;
  color: blue;
}
</style>
