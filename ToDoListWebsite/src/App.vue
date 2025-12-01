<script setup>
import { ref, onMounted } from 'vue'
import ToDoListWebsite from './components/views.vue'
import TodoList from './components/TodoList.vue'

const liste = ref([])
const fehler = ref('')

onMounted(async () => {
  try {
    const baseUrl = import.meta.env.VITE_APP_BACKEND_BASE_URL
    const endpoint = baseUrl + '/PersonService'
    const res = await fetch(endpoint)

    if (!res.ok) throw new Error(HTTP ${res.status})
    liste.value = await res.json()
  } catch (err) {
    fehler.value = String(err)
    // Fallback-Daten, damit du sofort etwas siehst
  }
})
</script>


<template>
  <div class="layout">
    <!-- Sidebar -->
    <aside class="sidebar">
     <div class="user-avatar">
       <span class="avatar-icon">👤</span>
     </div>

      <ul class="sidebar-menu">
        <li>Mein Tag</li>
        <li>Wichtig</li>
        <li>Geplant</li>
        <li>Aufgaben</li>
      </ul>
    </aside>

    <!-- Main content -->
    <main class="content">
      <TodoList />
    </main>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  width: 100%;
}

/* Sidebar */
.sidebar {
  width: 260px;
  background: #f2f2f2;
  padding: 20px;
  border-right: 1px solid #ddd;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
}

.user-avatar {
  width: 70px;
  height: 70px;
  background: #ddd;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 25px;
  font-size: 32px;
  color: #333;
  border: 2px solid #ccc;
}

.user-avatar:hover {
  background: #ccc;
  cursor: pointer;
}

.avatar-icon {
  pointer-events: none;
}

.sidebar-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-menu li {
  padding: 10px 0;
  cursor: pointer;
}

.sidebar-menu li:hover {
  text-decoration: underline;
}

/* Content */
.content {
  flex-grow: 1;
  padding: 40px;
  margin-left: 260px;
}
</style>
