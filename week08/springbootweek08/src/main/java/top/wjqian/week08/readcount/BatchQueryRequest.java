package top.wjqian.week08.readcount;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BatchQueryRequest {
    @NotEmpty(message = "文章ID列表不能为空")
    @NotNull(message = "文章ID列表不能为null")
    private List<Long> articleIds;

    public List<Long> getArticleIds() {
        return articleIds;
    }

    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }
}
