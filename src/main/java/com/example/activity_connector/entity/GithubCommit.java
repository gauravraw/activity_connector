package com.example.activity_connector.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GithubCommit {
    private String sha;
    private String message;
    private String authorName;
    private String authorEmail;
    private String date;
}
