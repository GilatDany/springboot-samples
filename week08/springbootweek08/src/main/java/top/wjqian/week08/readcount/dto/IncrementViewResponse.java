package top.wjqian.week08.readcount.dto;

public record IncrementViewResponse(
        Long articleId,
        Long viewCount
) {
}
