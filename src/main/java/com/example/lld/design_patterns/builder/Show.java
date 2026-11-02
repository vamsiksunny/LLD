package com.example.lld.design_patterns.builder;

import java.time.LocalDateTime;

/**
 * Hand-written builder, so the pattern is visible. In real code Lombok's @Builder
 * generates the same thing, but you'd still add validation like build() does here.
 *
 * The constructor is private and all fields are final: the only way to get a Show
 * is through the builder, and once built it can't change.
 */
public class Show {
    private final String movieName;          // required
    private final LocalDateTime startTime;   // required
    private final String language;           // optional
    private final String format;             // optional
    private final boolean subtitles;         // optional

    private Show(Builder builder) {
        this.movieName = builder.movieName;
        this.startTime = builder.startTime;
        this.language = builder.language;
        this.format = builder.format;
        this.subtitles = builder.subtitles;
    }

    public static Builder builder(String movieName, LocalDateTime startTime) {
        return new Builder(movieName, startTime);
    }

    @Override
    public String toString() {
        return "Show{movie='" + movieName + "', start=" + startTime + ", language='" + language
                + "', format='" + format + "', subtitles=" + subtitles + "}";
    }

    public static class Builder {
        private final String movieName;
        private final LocalDateTime startTime;
        private String language = "English";
        private String format = "2D";
        private boolean subtitles = false;

        // Required fields go in the constructor, so you can't forget them
        private Builder(String movieName, LocalDateTime startTime) {
            this.movieName = movieName;
            this.startTime = startTime;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder subtitles(boolean subtitles) {
            this.subtitles = subtitles;
            return this;
        }

        public Show build() {
            if (movieName == null || movieName.isBlank()) {
                throw new IllegalStateException("movieName is required");
            }
            if (startTime == null) {
                throw new IllegalStateException("startTime is required");
            }
            return new Show(this);
        }
    }
}
