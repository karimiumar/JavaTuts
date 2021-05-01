package com.umar.apps.java8;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GroupByTest {

    private static final List<BlogPost> posts = Arrays.asList(
            new BlogPost("News item 1", "Author 1", BlogPostType.NEWS, 15),
            new BlogPost("Tech review 1", "Author 2", BlogPostType.REVIEW, 5),
            new BlogPost("Programming guide", "Author 1", BlogPostType.GUIDE, 20),
            new BlogPost("News item 2", "Author 2", BlogPostType.NEWS, 35),
            new BlogPost("Tech review 2", "Author 1", BlogPostType.REVIEW, 15)
    );

    @Test
    void givenAListOfPosts_whenGroupedByType_thenGetAMapBetweenTypeAndPosts() {
        Map<BlogPostType, List<BlogPost>> postsPerType =
                posts.stream().collect(groupingBy(BlogPost::type));
        assertEquals(1, postsPerType.get(BlogPostType.GUIDE).size());
        assertEquals(2, postsPerType.get(BlogPostType.REVIEW).size());
        assertEquals(2, postsPerType.get(BlogPostType.NEWS).size());
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeAndSumTheLikes_thenGetAMapBetweenTypeAndPostLikes() {
        Map<BlogPostType, Integer> likesPerType =
                posts.stream().collect(groupingBy(BlogPost::type, summingInt(BlogPost::likes)));
        assertEquals(50, likesPerType.get(BlogPostType.NEWS).intValue());
        assertEquals(20, likesPerType.get(BlogPostType.REVIEW).intValue());
        assertEquals(20, likesPerType.get(BlogPostType.GUIDE).intValue());
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeInAnEnumMap_thenGetAnEnumMapBetweenTypeAndPosts() {
        EnumMap<BlogPostType, List<BlogPost>> postsPerType =
                posts.stream().collect(groupingBy(BlogPost::type, () -> new EnumMap<>(BlogPostType.class), toList()));
        assertEquals(2, postsPerType.get(BlogPostType.NEWS).size());
        assertEquals(1, postsPerType.get(BlogPostType.GUIDE).size());
        assertEquals(2, postsPerType.get(BlogPostType.REVIEW).size());
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeInSets_thenGetAMapBetweenTypesAndSetsOfPosts() {
        Map<BlogPostType, Set<BlogPost>> postsPerType =
                posts.stream().collect(groupingBy(BlogPost::type, toSet()));
        assertEquals(2, postsPerType.get(BlogPostType.NEWS).size());
        assertEquals(1, postsPerType.get(BlogPostType.GUIDE).size());
        assertEquals(2, postsPerType.get(BlogPostType.REVIEW).size());
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeConcurrently_thenGetAMapBetweenTypeAndPosts() {
        ConcurrentMap<BlogPostType, List<BlogPost>> postsPerType =
                posts.parallelStream().collect(groupingByConcurrent(BlogPost::type));
        assertEquals(2, postsPerType.get(BlogPostType.NEWS).size());
        assertEquals(1, postsPerType.get(BlogPostType.GUIDE).size());
        assertEquals(2, postsPerType.get(BlogPostType.REVIEW).size());
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeAndAveragingLikes_thenGetAMapBetweenTypeAndAverageNumberOfLikes() {
        Map<BlogPostType, Double> averageLikesPerType =
                posts.stream().collect(groupingBy(BlogPost::type, averagingInt(BlogPost::likes)));

        assertEquals(25, averageLikesPerType.get(BlogPostType.NEWS).intValue());
        assertEquals(20, averageLikesPerType.get(BlogPostType.GUIDE).intValue());
        assertEquals(10, averageLikesPerType.get(BlogPostType.REVIEW).intValue());
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeAndCounted_thenGetAMapBetweenTypeAndNumberOfPosts() {
        Map<BlogPostType, Long> numberOfPostsPerType =
                posts.stream().collect(groupingBy(BlogPost::type, counting()));

        assertEquals(2, numberOfPostsPerType.get(BlogPostType.NEWS).intValue());
        assertEquals(1, numberOfPostsPerType.get(BlogPostType.GUIDE).intValue());
        assertEquals(2, numberOfPostsPerType.get(BlogPostType.REVIEW).intValue());
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeAndMaxingLikes_thenGetAMapBetweenTypeAndMaximumNumberOfLikes() {
        Map<BlogPostType, Optional<BlogPost>> maxLikesPerPostType =
                posts.stream().collect(groupingBy(BlogPost::type, maxBy(comparingInt(BlogPost::likes))));

        assertTrue(maxLikesPerPostType.get(BlogPostType.NEWS).isPresent());
        assertEquals(35, maxLikesPerPostType.get(BlogPostType.NEWS).get().likes());
        assertTrue(maxLikesPerPostType.get(BlogPostType.GUIDE).isPresent());
        assertEquals(20, maxLikesPerPostType.get(BlogPostType.GUIDE).get().likes());
        assertTrue(maxLikesPerPostType.get(BlogPostType.REVIEW).isPresent());
        assertEquals(15, maxLikesPerPostType.get(BlogPostType.REVIEW).get().likes());
    }

    @Test
    void givenAListOfPosts_whenGroupedByAuthorAndThenByType_thenGetAMapBetweenAuthorAndMapsBetweenTypeAndBlogPosts() {
        Map<String, Map<BlogPostType, List<BlogPost>>> map =
                posts.stream().collect(groupingBy(BlogPost::author, groupingBy(BlogPost::type)));

        assertEquals(1, map.get("Author 1").get(BlogPostType.NEWS).size());
        assertEquals(1, map.get("Author 1").get(BlogPostType.GUIDE).size());
        assertEquals(1, map.get("Author 1").get(BlogPostType.REVIEW).size());
        assertEquals(1, map.get("Author 2").get(BlogPostType.NEWS).size());
        assertEquals(1, map.get("Author 2").get(BlogPostType.REVIEW).size());
        assertNull(map.get("Author 2").get(BlogPostType.GUIDE));
    }

    @Test
    void givenAListOfPosts_whenGroupedByTypeAndSummarizingLikes_thenGetAMapBetweenTypeAndSummary() {
        Map<BlogPostType, IntSummaryStatistics> likeStatisticsPerType =
                posts.stream().collect(groupingBy(BlogPost::type, summarizingInt(BlogPost::likes)));
        IntSummaryStatistics newsLikeStatistics = likeStatisticsPerType.get(BlogPostType.NEWS);
        assertEquals(2, newsLikeStatistics.getCount());
        assertEquals(50, newsLikeStatistics.getSum());
        assertEquals(25.0, newsLikeStatistics.getAverage(), 0.001);
        assertEquals(35, newsLikeStatistics.getMax());
        assertEquals(15, newsLikeStatistics.getMin());
    }

    @Test
    void givenAListOfPosts_whenGroupedByComplexMapKeyType_thenGetAMapBetweenTupleAndList() {
        Map<Tuple, List<BlogPost>> postsPerTypeAndAuthor =
                posts.stream().collect(groupingBy(post -> new Tuple(post.type(), post.author())));
        List<BlogPost> result = postsPerTypeAndAuthor.get(new Tuple(BlogPostType.GUIDE, "Author 1"));

        assertThat(result.size()).isEqualTo(1);

        BlogPost blogPost = result.get(0);

        assertThat(blogPost.title()).isEqualTo("Programming guide");
        assertThat(blogPost.type()).isEqualTo(BlogPostType.GUIDE);
        assertThat(blogPost.author()).isEqualTo("Author 1");
    }
}
