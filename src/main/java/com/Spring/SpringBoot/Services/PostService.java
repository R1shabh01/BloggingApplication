package com.Spring.SpringBoot.Services;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Sort.Direction;
import org.eclipse.angus.mail.util.ASCIIUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Spring.SpringBoot.Models.Post;
import com.Spring.SpringBoot.Repositories.PostRepository;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public Optional<Post> getById(Long id){
        return postRepository.findById(id);
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Page<Post> getAll(int offset , int pageSize , String field){
        return postRepository.findAll(PageRequest.of(offset, pageSize).withSort(Direction.ASC , field));
    }

    public void delete(Post post){
        postRepository.delete(post);
    }

    public Post save(Post post){
        if(post.getId()==null){
            post.setCreatedAt(LocalDateTime.now());
        }
        post.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

}
