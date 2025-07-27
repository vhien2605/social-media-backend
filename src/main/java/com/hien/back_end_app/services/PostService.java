package com.hien.back_end_app.services;


import com.hien.back_end_app.dto.response.PageResponseDTO;
import com.hien.back_end_app.dto.response.post.CommentResponseDTO;
import com.hien.back_end_app.dto.response.post.PostResponseDTO;
import com.hien.back_end_app.entities.Comment;
import com.hien.back_end_app.entities.Follow;
import com.hien.back_end_app.entities.GroupUser;
import com.hien.back_end_app.entities.Post;
import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.mappers.CommentMapper;
import com.hien.back_end_app.mappers.PostMapper;
import com.hien.back_end_app.repositories.*;
import com.hien.back_end_app.repositories.specification.SpecificationBuilder;
import com.hien.back_end_app.utils.commons.AppConst;
import com.hien.back_end_app.utils.commons.GlobalMethod;
import com.hien.back_end_app.utils.enums.CommentType;
import com.hien.back_end_app.utils.enums.ErrorCode;
import com.hien.back_end_app.utils.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.ListaggStringAggEmulation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostMediaRepository postMediaRepository;
    private final GroupUserRepository groupUserRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public PageResponseDTO<Object> getPosts(Pageable pageable) {
        // find target follows
        String followEmail = GlobalMethod.extractEmailFromContext();
        List<Follow> targetFollows = followRepository.getAllTargetFollowers(followEmail);
        List<Long> targetFollowIds = targetFollows.stream()
                .map(f -> f.getTargetUser().getId()).toList();
        // find posts of target follows
        Page<Post> posts = postRepository.findFollowPosts(targetFollowIds, PostType.WALL_POST, pageable);
        List<Long> postIds = posts.stream()
                .map(Post::getId).toList();
        // fetch
        List<Post> fetchedPosts = postRepository.findPostsWithPostMediaAndEmotions(postIds);
        Map<Long, Post> idPostMap = fetchedPosts.stream()
                .collect(Collectors.toMap(Post::getId, p -> p));
        List<PostResponseDTO> dtos = postIds
                .stream()
                .map(idPostMap::get)
                .map(postMapper::toDTO)
                .toList();

        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(posts.getTotalPages())
                .data(dtos)
                .build();
    }

    public PageResponseDTO<Object> getPostsAdvancedFilter(Pageable pageable, String[] post, String[] sortBy) {
        // find target follows
        String followEmail = GlobalMethod.extractEmailFromContext();
        List<Follow> targetFollows = followRepository.getAllTargetFollowers(followEmail);
        List<Long> targetFollowIds = targetFollows.stream()
                .map(f -> f.getTargetUser().getId()).toList();

        SpecificationBuilder<Post> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // loop conversation and build specification
        // add predicate user follow
        builder.with(null, "idCreate", "^", targetFollowIds, null, null);
        builder.with(null, "type", ":", PostType.WALL_POST, null, null);
        // loop other predicate
        for (String s : post) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Post> specification = builder.build();

        // add sort by createAt desc
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(new Sort.Order(Sort.Direction.DESC, "createAt"));
        for (String sb : sortBy) {
            Matcher sortMatcher = sortPattern.matcher(sb);
            if (sortMatcher.find()) {
                String field = sortMatcher.group(1);
                String value = sortMatcher.group(3);
                Sort.Direction direction = (value.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        Sort sort = Sort.by(sortOrders);
        // insert sort property into pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);


        // fetch post
        Page<Post> posts = postRepository.findAll(specification, sortedPageable);
        List<Long> postIds = posts.stream()
                .map(Post::getId).toList();
        List<Post> fetchedPosts = postRepository.findPostsWithPostMediaAndEmotions(postIds);
        Map<Long, Post> idPostMap = fetchedPosts.stream().collect(Collectors.toMap(
                Post::getId,
                fp -> fp
        ));
        List<PostResponseDTO> dtos = postIds
                .stream()
                .map(idPostMap::get)
                .map(postMapper::toDTO)
                .toList();


        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(posts.getTotalPages())
                .data(dtos)
                .build();
    }

    public PageResponseDTO<Object> getMyPosts(Pageable pageable) {
        String email = GlobalMethod.extractEmailFromContext();
        Page<Post> posts = postRepository.findMyPosts(email, PostType.WALL_POST, pageable);
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();
        List<Post> fetchedPosts = postRepository.findPostsWithPostMediaAndEmotions(postIds);
        Map<Long, Post> idPostMap = fetchedPosts.stream()
                .collect(Collectors.toMap(Post::getId, fp -> fp));
        List<PostResponseDTO> dtos = postIds.stream()
                .map(idPostMap::get)
                .map(postMapper::toDTO)
                .toList();

        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(posts.getTotalPages())
                .data(dtos)
                .build();
    }

    public PageResponseDTO<Object> getMyPostsWithAdvancedFilter(Pageable pageable, String[] post, String[] sortBy) {
        String email = GlobalMethod.extractEmailFromContext();

        SpecificationBuilder<Post> builder = new SpecificationBuilder<>();
        Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
        Pattern sortPattern = Pattern.compile(AppConst.SORT_BY);

        // loop conversation and build specification
        // add predicate join equal createdUser mail
        builder.with(null, "createdEmail", "^", email, null, null);
        builder.with(null, "type", ":", PostType.WALL_POST, null, null);
        // loop other predicate
        for (String s : post) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                        matcher.group(4), matcher.group(5), matcher.group(6));
            }
        }
        Specification<Post> specification = builder.build();

        // add sort by createAt desc
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(new Sort.Order(Sort.Direction.DESC, "createAt"));
        for (String sb : sortBy) {
            Matcher sortMatcher = sortPattern.matcher(sb);
            if (sortMatcher.find()) {
                String field = sortMatcher.group(1);
                String value = sortMatcher.group(3);
                Sort.Direction direction = (value.equalsIgnoreCase("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sortOrders.add(new Sort.Order(direction, field));
            }
        }
        Sort sort = Sort.by(sortOrders);
        // insert sort property into pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);

        Page<Post> posts = postRepository.findAll(specification, sortedPageable);
        List<Long> postIds = posts.stream()
                .map(Post::getId).toList();
        List<Post> fetchedPosts = postRepository.findPostsWithPostMediaAndEmotions(postIds);
        Map<Long, Post> idPostMap = fetchedPosts.stream().collect(Collectors.toMap(
                Post::getId,
                fp -> fp
        ));
        List<PostResponseDTO> dtos = postIds
                .stream()
                .map(idPostMap::get)
                .map(postMapper::toDTO)
                .toList();
        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(posts.getTotalPages())
                .data(dtos)
                .build();
    }

    public PageResponseDTO<Object> getPostComments(Long postId, Pageable pageable) {
        String email = GlobalMethod.extractEmailFromContext();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));
        //if group post,check that user is in group?
        if (post.getType().equals(PostType.GROUP_POST)) {
            List<GroupUser> groupUsers = groupUserRepository.findAllByGroupId(post.getGroup().getId());
            boolean isUserInGroup = groupUsers.stream()
                    .anyMatch(gu -> gu.getUser().getEmail().equals(email));
            if (!isUserInGroup) {
                throw new AppException(ErrorCode.ACCESS_DENIED);
            }
        }
        Page<Comment> comments = commentRepository.findCommentsByPostId(postId, CommentType.POST_COMMENT, pageable);
        List<Long> commentIds = comments.stream().map(Comment::getId).toList();
        List<Comment> fetchedComments = commentRepository.findCommentsWithEmotionsByIds(commentIds);
        Map<Long, Comment> idCommentMap = fetchedComments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> c));
        List<CommentResponseDTO> dtos = commentIds
                .stream().map(idCommentMap::get)
                .map(commentMapper::toDTO)
                .toList();

        return PageResponseDTO.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(comments.getTotalPages())
                .data(dtos)
                .build();
    }
}
