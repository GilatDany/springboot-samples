package top.wjqian.week08.readcount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.wjqian.week08.readcount.dto.ApiResult;
import top.wjqian.week08.readcount.dto.ArticleViewDTO;
import top.wjqian.week08.readcount.dto.IncrementViewResponse;
import top.wjqian.week08.readcount.dto.ViewCountResponse;

import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleViewController {
    private final ArticleViewService articleViewService;

    @PostMapping("/{articleId}/view/increment")
    public ApiResult<IncrementViewResponse> incrementView(@PathVariable Long articleId) {
        Long newCount = articleViewService.incrementViewCount(articleId);
        return ApiResult.success(new IncrementViewResponse(articleId, newCount));
    }

    @GetMapping("/{articleId}/view")
    public ApiResult<ViewCountResponse> getViewCount(@PathVariable Long articleId) {
        Long count = articleViewService.getViewCount(articleId);
        return ApiResult.success(new ViewCountResponse(articleId, count != null ? count : 0L));
    }
    @PostMapping("/views/batch")
    public ApiResult<List<ArticleViewDTO>> batchGetViewCounts(
            @RequestBody @Valid BatchQueryRequest request) {
        List<ArticleViewDTO> result = articleViewService.batchGetViewCounts(request.getArticleIds());
        return ApiResult.success(result);
    }
}
