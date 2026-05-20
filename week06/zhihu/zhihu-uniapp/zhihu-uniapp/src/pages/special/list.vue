<template>
  <view class="page">
    <view class="head">
      <text class="title-all">全部专题</text>
      <text v-if="totalText" class="sub">{{ totalText }}</text>
      <view class="toolbar">
        <input 
          class="search"
          v-model="titleQuery"
          placeholder="关键词，如：安全"
          confirm-type="search"
          @confirm="onSearch"
        />
        <view class="search-btn" @click="onSearch">
          <text>搜索</text>
        </view>
      </view>
    </view>

    <scroll-view 
      class="list-scroll"
      scroll-y
      @scrolltolower="onReachBottom"
      :refresher-enabled="true"
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
    >
      <SpecialCard 
        v-for="it in list" 
        :key="String(it.id ?? it.title)" 
        :item="it"
        @click="goToDetail"
        @follow="handleFollow"
      />
      
      <view v-if="loadingMore" class="status-text">加载更多...</view>
      <view v-else-if="!hasMore && list.length > 0" class="status-text">没有更多了</view>
      <view v-else-if="!loading && list.length === 0" class="status-text">暂无数据</view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import SpecialCard from '@/components/SpecialCard.vue'
import { fetchSpecialByTitle } from '@/api/special'
import type { SpecialItem } from '@/types/special'

const titleQuery = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const loadingMore = ref(false)
const refreshing = ref(false)
const list = ref<SpecialItem[]>([])
const total = ref(0)
const hasMore = ref(true)

const totalText = computed(() =>
  total.value > 0 ? `共有 ${total.value.toLocaleString('zh-CN')} 个专题` : ''
)

async function load(reset = false) {
  if (reset) {
    if (loading.value) return
    loading.value = true
  } else {
    if (loadingMore.value || !hasMore.value) return
    loadingMore.value = true
  }
  
  try {
    const page = reset ? 1 : pageNum.value
    const res = await fetchSpecialByTitle({
      title: titleQuery.value.trim() || '',
      pageNum: page,
      pageSize: pageSize.value,
    })
    
    if (reset) {
      list.value = res.list
      pageNum.value = 2
    } else {
      list.value = [...list.value, ...res.list]
      pageNum.value++
    }
    
    total.value = res.total
    hasMore.value = list.value.length < total.value
  } catch (e) {
    console.error(e)
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
    loadingMore.value = false
    refreshing.value = false
  }
}

function onSearch() {
  pageNum.value = 1
  hasMore.value = true
  load(true)
}

function onRefresh() {
  refreshing.value = true
  onSearch()
}

function onReachBottom() {
  if (hasMore.value && !loadingMore.value && !loading.value) {
    load(false)
  }
}

function goToDetail(item: SpecialItem) {
  if (!item.id) return
  
  console.log('跳转详情:', item)
  
  // 构建详情页需要的数据（使用 item 中的字段）
  const detailData = encodeURIComponent(JSON.stringify({
    id: item.id,
    title: item.title,
    banner: item.cover,  // 注意：SpecialItem 中使用的是 cover，不是 banner
    introduction: item.introduction,
    isFollowing: item.isFollowing ? 'TRUE' : 'FALSE',
    followersCount: parseInt(item.followersLabel) || 0,  // 从 followersLabel 解析数字
    viewCount: parseInt(item.visitLabel) || 0,  // 从 visitLabel 解析数字
    updated: Date.now() / 1000  // 使用当前时间，或者从其他地方获取
  }))
  
  uni.navigateTo({
    url: `/pages/special/detail?data=${detailData}`
  })
}
function handleFollow(item: SpecialItem) {
  item.isFollowing = !item.isFollowing
  uni.showToast({
    title: item.isFollowing ? '关注成功' : '已取消关注',
    icon: 'success'
  })
}

onMounted(() => {
  load(true)
})
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  background: #f6f6f6;
  padding: 24rpx 30rpx 48rpx;
  box-sizing: border-box;
}

.head {
  margin-bottom: 40rpx;
}

.title-all {
  display: block;
  margin-bottom: 12rpx;
  font-size: 52rpx;
  font-weight: 600;
  color: #1a1a1a;
}

.sub {
  display: block;
  margin-bottom: 30rpx;
  font-size: 28rpx;
  color: #8590a6;
}

.toolbar {
  display: flex;
  gap: 20rpx;
  flex-wrap: wrap;
}

.search {
  flex: 1;
  height: 70rpx;
  background: #fff;
  border-radius: 35rpx;
  padding: 0 30rpx;
  font-size: 28rpx;
  border: 1rpx solid #e0e0e0;
}

.search-btn {
  padding: 0 40rpx;
  height: 70rpx;
  line-height: 70rpx;
  background: #0084ff;
  color: white;
  font-size: 28rpx;
  border-radius: 35rpx;
  text-align: center;
}

.list-scroll {
  height: calc(100vh - 300rpx);
}

.status-text {
  text-align: center;
  padding: 40rpx;
  color: #999;
  font-size: 28rpx;
}
</style>