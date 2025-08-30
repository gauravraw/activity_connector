package com.example.activity_connector.nonentity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCommitResponse {
    @JsonProperty("sha")
    private String sha;

    @JsonProperty("commit")
    private CommitResponse commit;

}
