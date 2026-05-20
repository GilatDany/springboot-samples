<template>
  <view class="page">
    <view class="back" @click="back">
      <text>← 返回列表</text>
    </view>
    
    <view v-if="loading" class="loading">加载中...</view>
    
    <template v-else-if="detail">
      <view class="banner-wrap">
        <image class="banner" :src="detail.banner" mode="aspectFill" />
      </view>
      <text class="title">{{ detail.title }}</text>
      <view class="meta">
        <text>{{ formatDate(detail.updated) }} · {{ formatNumber(detail.viewCount) }}次浏览 · {{ formatNumber(detail.followersCount) }}关注</text>
      </view>
      <text v-if="detail.introduction" class="intro">{{ detail.introduction }}</text>
    </template>
    
    <view v-else-if="!loading" class="empty">暂无数据</view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'

interface SpecialDetail {
  id: string
  title: string
  banner: string
  introduction: string
  isFollowing: string
  followersCount: number
  viewCount: number
  updated: number
}

const loading = ref(false)
const detail = ref<SpecialDetail | null>(null)

function back() {
  uni.navigateBack()
}

function formatDate(timestamp: number): string {
  if (!timestamp) return '未知'
  const date = new Date(timestamp * 1000)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} 更新`
}

function formatNumber(num: number): string {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  }
  return String(num)
}

onLoad((options: any) => {
  console.log('=== 详情页 onLoad ===')
  console.log('接收到的 options:', options)
  
  // 从 data 参数获取完整数据
  if (options && options.data) {
    try {
      const decodedData = decodeURIComponent(options.data)
      console.log('解码后的数据字符串:', decodedData)
      detail.value = JSON.parse(decodedData)
      console.log('解析成功，详情数据:', detail.value)
      
      if (!detail.value) {
        uni.showToast({ title: '数据解析失败', icon: 'none' })
      }
    } catch (e) {
      console.error('解析数据失败:', e)
      uni.showToast({ title: '数据加载失败', icon: 'none' })
    }
  } else {
    console.error('没有接收到 data 参数')
    uni.showToast({ 
      title: '请从专题列表进入', 
      icon: 'none',
      duration: 2000
    })
    setTimeout(() => {
      uni.navigateBack()
    }, 2000)
  }
  
  loading.value = false
})
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  background: white;
  padding: 30rpx;
}

.back {
  margin-bottom: 30rpx;
  color: #0084ff;
  font-size: 28rpx;
  padding: 10rpx 0;
}

.banner-wrap {
  border-radius: 12rpx;
  overflow: hidden;
  background: #f0f0f0;
}

.banner {
  width: 100%;
  height: 400rpx;
}

.title {
  display: block;
  margin: 30rpx 0 16rpx;
  font-size: 40rpx;
  font-weight: bold;
  color: #1a1a1a;
}

.meta {
  font-size: 26rpx;
  color: #999;
  margin-bottom: 20rpx;
}

.intro {
  display: block;
  font-size: 28rpx;
  line-height: 1.6;
  color: #666;
}

.loading, .empty {
  text-align: center;
  padding: 100rpx;
  color: #999;
  font-size: 28rpx;
}
</style>