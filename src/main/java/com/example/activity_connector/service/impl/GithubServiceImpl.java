package com.example.activity_connector.service.impl;

import com.example.activity_connector.entity.GithubCommit;
import com.example.activity_connector.entity.GithubRepository;
import com.example.activity_connector.enums.ErrorCode;
import com.example.activity_connector.exception.GlobalException;
import com.example.activity_connector.nonentity.GitHubCommitResponse;
import com.example.activity_connector.nonentity.GitHubRepositoryResponse;
import com.example.activity_connector.service.GitHubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@RequiredArgsConstructor
@Slf4j
public class GithubServiceImpl implements GitHubService {

    private final RestTemplate restTemplate;
    @Value("${github.repository-path}")
    private String githubRepositoryPath;

    @Value("${github.commit-path}")
    private String githubCommitPath;
    @Override
    public List<GithubRepository> fetchRepoAndCommits(int pageNumber, int pageSize, String userName) {
        log.info("Received request to fetch repositories for user: {}", userName);
        List<GithubRepository> githubRepositories = new ArrayList<>();
        try{

            String url = UriComponentsBuilder.fromHttpUrl(String.format(githubRepositoryPath , userName))
                    .queryParam("page", pageNumber)
                    .queryParam("per_page", pageSize)
                    .toUriString();
            ResponseEntity<GitHubRepositoryResponse[]> repositoryResponseEntity = restTemplate.getForEntity(url, GitHubRepositoryResponse[].class);
            log.info("Fetched {} repositories for user {}", repositoryResponseEntity.getBody().length, userName);
            if(repositoryResponseEntity.hasBody()){

                githubRepositories = Arrays.stream(repositoryResponseEntity.getBody())
                        .map(repo -> processCommitsForRepository(repo , userName))
                        .toList();
            }
        }
        catch (Exception e){
            log.error("Error fetching repositories for user {}: {}", userName, e.getMessage());
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR.getCode() , e.getMessage());
        }
        return githubRepositories;
    }

    private GithubRepository processCommitsForRepository(GitHubRepositoryResponse repository , String userName){
        log.info("Processing commits for repository: {}", repository.getName());
        GitHubCommitResponse[] gitHubCommitResponses = fetchCommitsForRepository(userName, repository.getName());
        return convertToEntity(repository, gitHubCommitResponses);
    }

    private GithubRepository convertToEntity(GitHubRepositoryResponse repositoryResponse , GitHubCommitResponse[] commits){
        List<GithubCommit> commitList = new ArrayList<>();
        if(commits != null && commits.length > 0){
            commitList = Arrays.stream(commits)
                    .map(commitResponse -> GithubCommit.builder()
                            .sha(commitResponse.getSha())
                            .message(commitResponse.getCommit().getMessage())
                            .authorName(commitResponse.getCommit().getAuthor().getName())
                            .authorEmail(commitResponse.getCommit().getAuthor().getEmail())
                            .date(commitResponse.getCommit().getAuthor().getDate())
                            .build())
                    .toList();
        }

       return GithubRepository.builder()
               .name(repositoryResponse.getName())
               .fullName(repositoryResponse.getFullName())
               .htmlUrl(repositoryResponse.getHtmlUrl())
               .commitList(commitList)
               .build();
    }

    private GitHubCommitResponse[] fetchCommitsForRepository(String userName, String repoName){
        log.info("Fetching commits for repository: {}", repoName);
        try{
            String url = UriComponentsBuilder.fromHttpUrl(String.format(githubCommitPath , userName, repoName))
                    .queryParam("per_page", 20)
                    .queryParam("page", 0)
                    .toUriString();
            ResponseEntity<GitHubCommitResponse[]>  responseEntity = restTemplate.getForEntity(url, GitHubCommitResponse[].class);
            return responseEntity.getBody();
        }
        catch (Exception e){
            log.error("Error fetching commits for repository {}: {}", repoName, e.getMessage());
            return new GitHubCommitResponse[0];
        }
    }
}
