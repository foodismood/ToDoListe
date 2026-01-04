const rawBase = import.meta.env.VITE_APP_BACKEND_BASE_URL || ''
const baseUrl = rawBase.replace(/\/+$/, '') // entfernt trailing /

function join(path) {
    return `${baseUrl}${path}`
}

async function request(path, options = {}) {
    if (!baseUrl) {
        throw new Error('VITE_APP_BACKEND_BASE_URL ist nicht gesetzt (Render Environment Variable fehlt).')
    }

    const res = await fetch(join(path), {
        headers: {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        },
        ...options
    })

    if (!res.ok) {
        let msg = `${res.status} ${res.statusText}`
        try {
            const text = await res.text()
            // Versuch JSON zu lesen, falls möglich
            try {
                const body = JSON.parse(text)
                if (body?.message) msg = body.message
                else if (body?.error) msg = body.error
            } catch {
                // sonst Text anzeigen (hilft bei Spring Whitelabel)
                if (text) msg = `${msg}: ${text.slice(0, 200)}`
            }
        } catch {}
        throw new Error(msg)
    }

    if (res.status === 204) return null
    return res.json()
}

export const api = {
    getTodos() {
        return request('/api/todos')
    },
    createTodo(payload) {
        return request('/api/todos', { method: 'POST', body: JSON.stringify(payload) })
    },
    updateTodo(id, payload) {
        return request(`/api/todos/${id}`, { method: 'PUT', body: JSON.stringify(payload) })
    },
    toggleCompleted(id) {
        return request(`/api/todos/${id}/toggle-completed`, { method: 'PATCH' })
    },
    toggleImportant(id) {
        return request(`/api/todos/${id}/toggle-important`, { method: 'PATCH' })
    },
    deleteTodo(id) {
        return request(`/api/todos/${id}`, { method: 'DELETE' })
    }
}
