package top.wjqian.week08.readcount;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.wjqian.week08.readcount.dto.ArticleViewDTO;
import top.wjqian.week08.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final RedisUtil redisUtil;

    // 阅读量过期时间（30天）
    private static final long VIEW_COUNT_TTL = 30L * 24 * 60 * 60;

    /**
     * 增加文章阅读量
     */
    public Long incrementViewCount(Long articleId) {
        String key = ArticleRedisKey.ofArticleId(articleId);

        // 第一次设置时添加过期时间
        Long count = redisUtil.increment(key);
        if (count != null && count == 1L) {
            redisUtil.expire(key, VIEW_COUNT_TTL, TimeUnit.SECONDS);
        }

        return count;
    }

    /**
     * 获取文章阅读量
     */
    public Long getViewCount(Long articleId) {
        String key = ArticleRedisKey.ofArticleId(articleId);
        return redisUtil.get(key, Long.class);
    }
    /**
     * 批量获取文章阅读量
     */
    public List<ArticleViewDTO> batchGetViewCounts(List<Long> articleIds) {
        List<ArticleViewDTO> result = new ArrayList<>();

        for (Long articleId : articleIds) {
            Long viewCount = getViewCount(articleId);
            result.add(new ArticleViewDTO(articleId, viewCount != null ? viewCount : 0L));
        }

        return result;
    }
    /**
     * 重置文章阅读量（管理员功能）
     */
    public void resetViewCount(Long articleId, Long newCount) {
        String key = ArticleRedisKey.ofArticleId(articleId);
        if (newCount == null || newCount == 0L) {
            redisUtil.delete(key);
        } else {
            redisUtil.set(key, newCount);
            redisUtil.expire(key, VIEW_COUNT_TTL, TimeUnit.SECONDS);
        }
    }

}
