<script setup>
import { computed, onMounted, ref } from 'vue'
import { api } from '../lib/api'
import MiniCalendar from './MiniCalendar.vue'
import TaskList from './TaskList.vue'

const views = [
  { id: 'myday', label: 'Mein Tag', hint: 'Heute' },
  { id: 'important', label: 'Wichtig', hint: 'Markiert' },
  { id: 'planned', label: 'Geplant', hint: 'Mit Datum' },
  { id: 'tasks', label: 'Aufgaben', hint: 'Alle' }
]

const activeView = ref('myday')
const selectedDate = ref(new Date())

const todos = ref([])
const error = ref('')
const loading = ref(false)

async function refresh() {
  loading.value = true
  error.value = ''
  try {
    todos.value = await api.getTodos()
  } catch (e) {
    error.value = String(e?.message || e)
  } finally {
    loading.value = false
  }
}

onMounted(refresh)

const selectedDateIso = computed(() => {
  const d = selectedDate.value
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
})

const filteredTodos = computed(() => {
  const view = activeView.value
  const list = [...todos.value]

  if (view === 'important') return list.filter(t => t.important)
  if (view === 'planned') return list.filter(t => !!t.dueDate)
  if (view === 'myday') {
    return list.filter(t => t.dueDate && t.dueDate === selectedDateIso.value)
  }
  return list
})

const title = computed(() => {
  const v = views.find(x => x.id === activeView.value)
  return v?.label ?? 'Aufgaben'
})

async function addTask(payload) {
  error.value = ''
  try {
    const created = await api.createTodo(payload)
    todos.value = [created, ...todos.value]
  } catch (e) {
    error.value = String(e?.message || e)
  }
}

async function toggleCompleted(id) {
  error.value = ''
  try {
    const updated = await api.toggleCompleted(id)
    todos.value = todos.value.map(t => (t.id === id ? updated : t))
  } catch (e) {
    error.value = String(e?.message || e)
  }
}

async function toggleImportant(id) {
  error.value = ''
  try {
    const updated = await api.toggleImportant(id)
    todos.value = todos.value.map(t => (t.id === id ? updated : t))
  } catch (e) {
    error.value = String(e?.message || e)
  }
}

async function removeTask(id) {
  error.value = ''
  try {
    await api.deleteTodo(id)
    todos.value = todos.value.filter(t => t.id !== id)
  } catch (e) {
    error.value = String(e?.message || e)
  }
}

async function updateTask(id, payload) {
  error.value = ''
  try {
    const updated = await api.updateTodo(id, payload)
    todos.value = todos.value.map(t => (t.id === id ? updated : t))
  } catch (e) {
    error.value = String(e?.message || e)
  }
}

const dueDateMap = computed(() => {
  const map = new Map()
  for (const t of todos.value) {
    if (!t.dueDate) continue
    map.set(t.dueDate, (map.get(t.dueDate) || 0) + 1)
  }
  return map
})

function setView(id) {
  activeView.value = id
}

function onPickDate(d) {
  selectedDate.value = d
  // If the user picks a date, make sure we're in "Mein Tag" so the filter makes sense.
  activeView.value = 'myday'
}
</script>

<template>
  <div class="app">
    <aside class="sidebar">
      <div class="brand">
        <div class="avatar">✓</div>
        <div>
          <div class="brand-title">To Do</div>
          <div class="muted brand-sub">Microsoft To Do–Style</div>
        </div>
      </div>

      <nav class="nav">
        <button
          v-for="v in views"
          :key="v.id"
          class="nav-item"
          :class="{ active: activeView === v.id }"
          @click="setView(v.id)"
        >
          <div class="nav-left">
            <span class="dot" />
            <span class="label">{{ v.label }}</span>
          </div>
          <span class="muted small">{{ v.hint }}</span>
        </button>
      </nav>

      <div class="sidebar-card">
        <div class="card-title">Kalender</div>
        <MiniCalendar
          :selected-date="selectedDate"
          :due-date-map="dueDateMap"
          @pick="onPickDate"
        />
      </div>
    </aside>

    <main class="main">
      <header class="header">
        <div>
          <h1 class="h1">{{ title }}</h1>
          <p class="muted" v-if="activeView === 'myday'">
            {{ selectedDate.toLocaleDateString('de-DE', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }) }}
          </p>
          <p class="muted" v-else>Ordne deine Aufgaben nach Wichtigkeit, Datum und Kategorie.</p>
        </div>

        <div class="header-actions">
          <button class="btn" @click="refresh" :disabled="loading">↻ Aktualisieren</button>
        </div>
      </header>

      <div class="error" v-if="error">{{ error }}</div>

      <TaskList
        :todos="filteredTodos"
        :active-view="activeView"
        :selected-date-iso="selectedDateIso"
        :loading="loading"
        @add="addTask"
        @toggle-completed="toggleCompleted"
        @toggle-important="toggleImportant"
        @remove="removeTask"
        @update="updateTask"
      />
    </main>
  </div>
</template>

<style scoped>
.app {
  height: 100vh;
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 18px;
  padding: 18px;
}

.sidebar {
  background: var(--panel-bg);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: var(--accent-soft);
  border: 1px solid rgba(37, 99, 235, 0.22);
  display: grid;
  place-items: center;
  color: var(--accent);
  font-weight: 700;
}

.brand-title {
  font-size: 16px;
  font-weight: 700;
}

.brand-sub {
  font-size: 12px;
}

.nav {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.nav-item {
  border: 1px solid transparent;
  background: transparent;
  border-radius: 14px;
  padding: 10px 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
}

.nav-item:hover {
  background: rgba(15, 23, 42, 0.03);
}

.nav-item.active {
  background: var(--accent-soft);
  border-color: rgba(37, 99, 235, 0.22);
}

.nav-left {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.label {
  font-weight: 600;
}

.small {
  font-size: 12px;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 999px;
  background: rgba(15, 23, 42, 0.18);
}

.nav-item.active .dot {
  background: var(--accent);
}

.sidebar-card {
  border: 1px solid var(--border);
  border-radius: 14px;
  padding: 12px;
  margin-top: auto;
}

.card-title {
  font-weight: 700;
  font-size: 13px;
  margin-bottom: 10px;
}

.main {
  background: var(--panel-bg);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  box-shadow: var(--shadow);
  padding: 20px;
  overflow: auto;
}

.header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
}

.h1 {
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.02em;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.error {
  margin: 14px 0;
  padding: 10px 12px;
  border-radius: 12px;
  border: 1px solid rgba(239, 68, 68, 0.25);
  background: rgba(239, 68, 68, 0.08);
}

@media (max-width: 980px) {
  .app {
    grid-template-columns: 1fr;
  }
  .sidebar-card {
    margin-top: 0;
  }
}
</style>
