package com.esteban.tvmaze.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record CommentRequest(
        @NotNull
        @Positive
        Integer show_id,

        @NotBlank
        @Size(max = 2000)
        String comment,

        @NotNull
        @Min(0)
        @Max(5)
        Integer rating){
	}