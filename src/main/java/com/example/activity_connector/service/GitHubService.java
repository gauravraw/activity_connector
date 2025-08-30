package com.example.activity_connector.service;

import com.example.activity_connector.entity.GithubRepository;

import java.util.List;

public interface GitHubService {

    List<GithubRepository> fetchRepoAndCommits(int pageNumber, int pageSize, String userName);
}
