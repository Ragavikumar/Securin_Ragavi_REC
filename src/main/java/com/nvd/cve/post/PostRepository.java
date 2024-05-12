package com.nvd.cve.post;

import org.springframework.data.repository.ListCrudRepository;

public interface PostRepository<Post> extends ListCrudRepository<Post,String> {
}
