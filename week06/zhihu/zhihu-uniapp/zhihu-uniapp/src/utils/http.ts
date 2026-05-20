// 改成你的电脑 IP（手机和电脑要在同一个 WiFi）
const BASE_URL = 'http://172.20.10.6:8080' 

export async function httpGet<T = unknown>(
  url: string,
  params?: Record<string, any>
): Promise<T> {
  return new Promise((resolve, reject) => {
    // 手动构建查询字符串
    let fullUrl = url
    if (params && Object.keys(params).length > 0) {
      const queryParts: string[] = []
      for (const key in params) {
        const value = params[key]
        if (value !== undefined && value !== null && value !== '') {
          queryParts.push(`${encodeURIComponent(key)}=${encodeURIComponent(String(value))}`)
        }
      }
      if (queryParts.length > 0) {
        fullUrl = url.includes('?') ? `${url}&${queryParts.join('&')}` : `${url}?${queryParts.join('&')}`
      }
    }

    uni.request({
      url: BASE_URL + fullUrl,
      method: 'GET',
      header: {
        'Content-Type': 'application/json'
      },
      timeout: 30000,
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data as T)
        } else {
          console.error(`请求失败: ${res.statusCode}`)
          reject(res)
        }
      },
      fail: (err) => {
        console.error('网络请求失败:', err)
        reject(err)
      }
    })
  })
}