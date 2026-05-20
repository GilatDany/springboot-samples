<!-- src/components/SpecialCard.vue -->
<template>
  <view class="card-row" @click="handleClick">
    <view class="cover-wrap">
      <image 
        class="cover"
        :src="item.cover || placeholderImage"
        mode="aspectFill"
        lazy-load
        @error="onImageError"
      />
    </view>
    <view class="body">
      <text class="card-title">{{ item.title }}</text>
      <text v-if="item.introduction" class="intro">{{ item.introduction }}</text>
      <view class="meta">
        <text>{{ item.updateLabel }}</text>
        <text class="dot">·</text>
        <text>{{ item.visitLabel }}</text>
        <text class="dot">·</text>
        <text>{{ item.followersLabel }}</text>
      </view>
      <view 
        class="follow-btn"
        :class="{ 'follow-btn--following': item.isFollowing }"
        @click.stop="handleFollow"
      >
        <text>{{ item.isFollowing ? '已关注' : '关注专题' }}</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import type { SpecialItem } from '../types/special'

const props = defineProps<{ item: SpecialItem }>()

// 修改：传递整个 item，而不是只传 id
const emit = defineEmits<{
  click: [item: SpecialItem]  // 改为 item
  follow: [item: SpecialItem]
}>()

const placeholderImage = 'https://via.placeholder.com/200x120?text=No+Image'

function handleClick() {
  emit('click', props.item)  // 传递整个 item
}

function handleFollow() {
  emit('follow', props.item)
}

function onImageError(e: any) {
  const target = e.target
  if (target) {
    target.src = placeholderImage
  }
}
</script>

<style lang="scss" scoped>
.card-row {
  display: flex;
  gap: 20rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  background: #fff;
  border-radius: 8rpx;
  box-shadow: 0 2rpx 6rpx rgba(18, 18, 18, 0.06);
  border: 1rpx solid #f0f0f0;
  
  &:active {
    opacity: 0.8;
  }
}

.cover-wrap {
  flex-shrink: 0;
  width: 200rpx;
  height: 120rpx;
  border-radius: 8rpx;
  overflow: hidden;
  background: #f6f6f6;
}

.cover {
  width: 100%;
  height: 100%;
}

.body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.card-title {
  margin-bottom: 16rpx;
  font-size: 36rpx;
  font-weight: 600;
  color: #121212;
  line-height: 1.35;
}

.intro {
  margin-bottom: 16rpx;
  font-size: 28rpx;
  color: #646464;
  line-height: 1.55;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.meta {
  margin-bottom: 24rpx;
  font-size: 28rpx;
  color: #8590a6;
  line-height: 1.5;
}

.dot {
  margin: 0 12rpx;
  opacity: 0.7;
}

.follow-btn {
  padding: 12rpx 32rpx;
  border-radius: 60rpx;
  border: 1rpx solid #0084ff;
  background-color: #fff;
  color: #0084ff;
  font-size: 28rpx;
  text-align: center;
  
  &:active {
    opacity: 0.7;
  }
}

.follow-btn--following {
  border-color: #ebebeb;
  color: #8590a6;
  background-color: #f6f6f6;
}
</style>