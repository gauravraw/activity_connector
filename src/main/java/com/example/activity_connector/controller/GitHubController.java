package com.example.activity_connector.controller;


import com.example.activity_connector.entity.GithubRepository;
import com.example.activity_connector.nonentity.BaseResponse;
import com.example.activity_connector.service.GitHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/github")
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/repos/{userName}")
    public ResponseEntity<BaseResponse<List<GithubRepository>>> getRepoDetails(@RequestParam(value = "page" , defaultValue = "0") String page,
                                         @RequestParam(value = "size" , defaultValue = "20") String size,
                                         @PathVariable("userName") String username) {

        List<GithubRepository> githubRepositoryList  =  gitHubService.fetchRepoAndCommits(Integer.parseInt(page),Integer.parseInt(size),username);

        log.info("Fetched {} repositories for user {}", githubRepositoryList.size(), username);
        BaseResponse<List<GithubRepository>> response = BaseResponse.<List<GithubRepository>>builder()
                .success(true)
                .data(githubRepositoryList)
                .build();

        return ResponseEntity.ok(response);

    }
}
