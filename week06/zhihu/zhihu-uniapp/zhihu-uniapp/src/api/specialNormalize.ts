import type { SpecialItem } from '../types/special'

export function asRecord(v: unknown): Record<string, unknown> | null {
  return v && typeof v === 'object' && !Array.isArray(v)
    ? (v as Record<string, unknown>)
    : null
}

export function pickRows(payload: unknown): unknown[] {
  if (Array.isArray(payload)) return payload
  const root = asRecord(payload)
  if (!root) return []
  const keys = ['list', 'records', 'rows', 'items', 'dataList']
  for (const k of keys) {
    const v = root[k]
    if (Array.isArray(v)) return v
  }
  if (Array.isArray(root.data)) return root.data
  const data = asRecord(root.data)
  if (data) {
    for (const k of keys) {
      const v = data[k]
      if (Array.isArray(v)) return v
    }
  }
  return []
}

export function pickTotal(payload: unknown): number {
  const root = asRecord(payload)
  if (!root) return 0
  const dataRec = asRecord(root.data)
  for (const c of [root.total, root.totalCount, dataRec?.total, dataRec?.totalCount]) {
    const n = Number(c)
    if (Number.isFinite(n) && n >= 0) return n
  }
  return 0
}

export function pickOneRecord(payload: unknown): Record<string, unknown> | null {
  const root = asRecord(payload)
  if (!root) return null
  const inner = asRecord(root.data)
  if (inner && ('id' in inner || 'title' in inner)) return inner
  if ('id' in root || 'title' in root) return root
  return null
}

function formatVisit(n: number): string {
  if (n >= 10000) {
    return (n / 10000).toFixed(1) + '万'
  }
  return n.toLocaleString('zh-CN')
}

export function normalizeItem(raw: Record<string, unknown>): SpecialItem {
  const title = String(raw.title || raw.name || raw.specialTitle || '未命名专题')
  const cover = String(raw.banner || raw.cover || raw.coverUrl || '')
  const id = raw.id || raw.specialId || raw.topicId

  const introduction = String(raw.introduction || '').trim()
  
  let updateLabel = '—'
  const updated = raw.updated
  if (updated) {
    const timestamp = typeof updated === 'number' ? updated * 1000 : Number(updated) * 1000
    if (!isNaN(timestamp)) {
      const d = new Date(timestamp)
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      updateLabel = `${y}-${m}-${day} 更新`
    }
  }

  const viewCount = Number(raw.viewCount || 0)
  const followersCount = Number(raw.followersCount || 0)
  const isFollowing = raw.isFollowing === '1' || raw.isFollowing === true

  return {
    id: id as string | number | undefined,
    title,
    cover,
    introduction,
    isFollowing,
    updateLabel,
    visitLabel: `${formatVisit(viewCount)} 次浏览`,
    followersLabel: `${formatVisit(followersCount)} 关注`,
  }
}