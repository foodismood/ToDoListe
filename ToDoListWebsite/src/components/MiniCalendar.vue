<script setup>
import { computed, ref, watch } from 'vue'

const props = defineProps({
  selectedDate: { type: Date, required: true },
  // Map<YYYY-MM-DD, count>
  dueDateMap: { type: Object, required: true }
})

const emit = defineEmits(['pick'])

const cursor = ref(new Date(props.selectedDate))

watch(
  () => props.selectedDate,
  (d) => { cursor.value = new Date(d) }
)

function monthTitle(d) {
  return d.toLocaleDateString('de-DE', { month: 'long', year: 'numeric' })
}

function iso(d) {
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

const grid = computed(() => {
  const d = cursor.value
  const year = d.getFullYear()
  const month = d.getMonth()

  const first = new Date(year, month, 1)
  const startDay = (first.getDay() + 6) % 7 // Monday=0
  const start = new Date(year, month, 1 - startDay)

  const days = []
  for (let i = 0; i < 42; i++) {
    const day = new Date(start)
    day.setDate(start.getDate() + i)
    const key = iso(day)
    days.push({
      date: day,
      key,
      inMonth: day.getMonth() === month,
      hasDue: (props.dueDateMap?.get && props.dueDateMap.get(key)) || 0
    })
  }
  return days
})

function prevMonth() {
  const d = new Date(cursor.value)
  d.setMonth(d.getMonth() - 1)
  cursor.value = d
}

function nextMonth() {
  const d = new Date(cursor.value)
  d.setMonth(d.getMonth() + 1)
  cursor.value = d
}

function pick(day) {
  emit('pick', day.date)
}

function isSelected(day) {
  return iso(day.date) === iso(props.selectedDate)
}
</script>

<template>
  <div class="cal">
    <div class="top">
      <button class="icon" @click="prevMonth" aria-label="Vorheriger Monat">‹</button>
      <div class="title">{{ monthTitle(cursor) }}</div>
      <button class="icon" @click="nextMonth" aria-label="Nächster Monat">›</button>
    </div>

    <div class="dow">
      <span v-for="d in ['Mo','Di','Mi','Do','Fr','Sa','So']" :key="d" class="dow-item">{{ d }}</span>
    </div>

    <div class="grid">
      <button
        v-for="day in grid"
        :key="day.key"
        class="day"
        :class="{ muted: !day.inMonth, selected: isSelected(day), hasDue: day.hasDue }"
        @click="pick(day)"
      >
        <span>{{ day.date.getDate() }}</span>
      </button>
    </div>

    <div class="legend">
      <span class="dot" />
      <span class="muted">= Aufgaben mit Datum</span>
    </div>
  </div>
</template>

<style scoped>
.cal {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.title {
  font-size: 13px;
  font-weight: 700;
  text-transform: capitalize;
}

.icon {
  width: 28px;
  height: 28px;
  border-radius: 10px;
  border: 1px solid var(--border);
  background: #fff;
  cursor: pointer;
}

.icon:hover {
  border-color: rgba(16, 24, 40, 0.18);
}

.dow {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 6px;
}

.dow-item {
  font-size: 11px;
  color: var(--muted);
  text-align: center;
}

.grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 6px;
}

.day {
  height: 32px;
  border-radius: 10px;
  border: 1px solid transparent;
  background: transparent;
  cursor: pointer;
  position: relative;
}

.day:hover {
  background: rgba(15, 23, 42, 0.03);
}

.day.muted {
  opacity: 0.45;
}

.day.selected {
  background: var(--accent-soft);
  border-color: rgba(37, 99, 235, 0.22);
}

.day.hasDue::after {
  content: '';
  position: absolute;
  bottom: 6px;
  left: 50%;
  transform: translateX(-50%);
  width: 6px;
  height: 6px;
  border-radius: 999px;
  background: var(--accent);
  opacity: 0.8;
}

.legend {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 999px;
  background: var(--accent);
}
</style>
