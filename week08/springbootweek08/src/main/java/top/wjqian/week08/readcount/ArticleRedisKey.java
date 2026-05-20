package top.wjqian.week08.readcount;

public final class ArticleRedisKey {
    public static final String PREFIX="article:view:";
    private ArticleRedisKey() {
    }

    public static String ofArticleId(Long articleId) {
        return PREFIX + articleId;
    }

    public static String ofArticleId(String articleId) {
        return PREFIX + articleId;
    }
}
