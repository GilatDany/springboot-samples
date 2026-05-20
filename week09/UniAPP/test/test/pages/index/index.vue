<template>
	<view class="content">
		<image class="logo" src="/static/logo.png"></image>
		<view class="text-area">
			<text class="title">{{ title }}</text>
		</view>
		<view>
			<button @click="handleClick">请求接口</button>
		</view>
	</view>
</template>

<script setup>
import { ref } from 'vue';
const title = ref('');

const handleClick = () => {
	uni.request({
		url: 'http://127.0.0.1:8080/api/hello',
		method: 'get',
		header: {
			Authorization: 'admin'
		},
		success: (res) => {
			console.log('后端返回数据：', res.data);
			title.value = res.data;
		}
	});
};
</script>

<style>
.content {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
}

.logo {
	height: 200rpx;
	width: 200rpx;
	margin-top: 200rpx;
	margin-left: auto;
	margin-right: auto;
	margin-bottom: 50rpx;
}

.text-area {
	display: flex;
	justify-content: center;
}

.title {
	font-size: 36rpx;
	color: #8f8f94;
}
</style>
