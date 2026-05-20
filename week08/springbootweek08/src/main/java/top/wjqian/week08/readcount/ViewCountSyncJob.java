package top.wjqian.week08.readcount;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.wjqian.week08.util.RedisUtil;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountSyncJob {
    private final RedisUtil redisUtil;
    private final ArticleViewService articleViewService;

    /**
     * 每小时同步一次阅读量到数据库
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void syncViewCountToDatabase() {
        log.info("开始同步文章阅读量到数据库");

        String pattern = "article:view:*";
        Map<Long, Long> viewCounts = new HashMap<>();

        try (Cursor<String> cursor = redisUtil.getRedisTemplate()
                .scan(ScanOptions.scanOptions().match(pattern).count(100).build())) {

            while (cursor.hasNext()) {
                String key = cursor.next();
                // 提取文章ID
                String articleIdStr = key.substring("article:view:".length());
                try {
                    Long articleId = Long.parseLong(articleIdStr);
                    Long viewCount = articleViewService.getViewCount(articleId);
                    if (viewCount != null && viewCount > 0) {
                        viewCounts.put(articleId, viewCount);
                    }
                } catch (NumberFormatException e) {
                    log.warn("无效的文章ID格式: {}", articleIdStr);
                }
            }
        }
        log.info("文章阅读量同步完成，共处理{}篇文章", viewCounts.size());
    }
}
