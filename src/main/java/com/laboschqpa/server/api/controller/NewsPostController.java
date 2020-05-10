package com.laboschqpa.server.api.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.laboschqpa.server.api.service.NewsPostService;
import com.laboschqpa.server.config.userservice.CustomOauth2User;
import com.laboschqpa.server.entity.usergeneratedcontent.NewsPost;
import com.laboschqpa.server.enums.auth.Authority;
import com.laboschqpa.server.service.PrincipalAuthorizationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/newspost")
public class NewsPostController {
    private final NewsPostService newsPostService;

    @GetMapping("/newspost")
    public NewsPost getNewsPost(@RequestParam(name = "newsPostId") Long newsPostId) {
        return newsPostService.getNewsPost(newsPostId);
    }

    @GetMapping("/listall")
    public List<NewsPost> getListAllNewsPosts() {
        return newsPostService.listAllNewsPosts();
    }

    @PostMapping("/createNew")
    public void postCreateNewsPost(@RequestBody ObjectNode createNewsPostContent,
                                   @AuthenticationPrincipal CustomOauth2User authenticationPrincipal) {
        new PrincipalAuthorizationHelper(authenticationPrincipal).assertHasAnySufficientAuthority(Authority.Editor, Authority.Admin);
        newsPostService.createNewsPost(createNewsPostContent, authenticationPrincipal.getUserAccEntity());
    }

    @PostMapping("/edit")
    public void postEditNewsPost(@RequestBody ObjectNode editNewsPostContent,
                                 @AuthenticationPrincipal CustomOauth2User authenticationPrincipal) {
        new PrincipalAuthorizationHelper(authenticationPrincipal).assertHasAnySufficientAuthority(Authority.Editor, Authority.Admin);
        newsPostService.editNewsPost(editNewsPostContent, authenticationPrincipal.getUserAccEntity());
    }

    @DeleteMapping("/delete")
    public void deleteNewsPost(@RequestParam(name = "newsPostId") Long newsPostId,
                               @AuthenticationPrincipal CustomOauth2User authenticationPrincipal) {
        new PrincipalAuthorizationHelper(authenticationPrincipal).assertHasAnySufficientAuthority(Authority.Editor, Authority.Admin);
        newsPostService.deleteNewsPost(newsPostId, authenticationPrincipal.getUserAccEntity());
    }
}
