import { httpGet } from '../utils/http'
import { normalizeItem, asRecord } from './specialNormalize'
import type { SpecialItem, SpecialPageResult } from '../types/special'

export async function fetchSpecialByTitle(params: {
  title: string
  pageNum: number
  pageSize: number
}): Promise<SpecialPageResult> {
  const data = await httpGet<unknown>('/api/v1/special/page', {
    pageNum: params.pageNum,
    pageSize: params.pageSize,
    title: params.title,
  })
  
  // 处理嵌套的数据结构
  const responseData = (data as any)?.data?.data || (data as any)?.data || data
  const list = responseData?.records || responseData?.list || []
  const total = responseData?.total || 0
  
  const normalizedList = list
    .map((r: any) => asRecord(r))
    .filter(Boolean)
    .map((r: Record<string, unknown>) => normalizeItem(r))
  
  return { list: normalizedList, total }
}

export async function fetchSpecialById(id: string): Promise<SpecialItem | null> {
  // 这个方法现在可能用不到，但保留以备后用
  console.warn('fetchSpecialById 建议使用列表页传递数据的方式')
  return null
}