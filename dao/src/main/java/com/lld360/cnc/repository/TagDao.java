package com.lld360.cnc.repository;

import com.lld360.cnc.model.ArticleXTag;
import com.lld360.cnc.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TagDao {

    void create(Tag tag);

    Tag find(String name);

    void addArticleXTag(ArticleXTag articleXTag);

    void addArticleXTags(List<ArticleXTag> axts);

    void deleteArticleXTagByArticleId(long id);
}